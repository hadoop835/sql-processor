package org.sqlproc.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlproc.engine.SqlProcessorLoader.EngineType;
import org.sqlproc.engine.config.SqlEngineConfiguration;
import org.sqlproc.engine.jdbc.JdbcEngineFactory;
import org.sqlproc.engine.plugin.SqlPluginFactory;
import org.sqlproc.engine.type.SqlMetaType;
import org.sqlproc.engine.type.SqlTypeFactory;
import org.sqlproc.engine.validation.SqlValidatorFactory;

/**
 * The simple implementation of the {@link SqlEngineFactory}.
 * 
 * <p>
 * It's suitable mainly for the Spring DI based configuration, like the next one for the loader
 * {@link SqlProcessorLoader}:<br>
 * 
 * <pre>
 * &lt;beans ...&gt;
 *   ...
 *   &lt;bean id="typeFactory" class="org.sqlproc.engine.jdbc.type.JdbcTypeFactory" factory-method="getInstance" /&gt;
 * 
 *   &lt;bean id="sqlFactory" class="org.sqlproc.engine.SqlDefaultFactory" init-method="init"&gt;
 *     &lt;property name="metaFilesNames">
 *       &lt;list>
 *         &lt;value>statements.qry&lt;/value>
 *       &lt;/list>
 *     &lt;/property>
 *     &lt;property name="typeFactory" ref="typeFactory" /&gt;
 *   &lt;/bean&gt;
 * &lt;/beans&gt;
 * </pre>
 * 
 * and use the next code to obtain an instance of the SQL Query engine
 * 
 * <pre>
 * SqlQueryEngine sqlEngine = sqlFactory.getQueryEngine(&quot;ALL&quot;);
 * </pre>
 * 
 * or the next code to obtain an instance of the SQL CRUD engine
 * 
 * <pre>
 * SqlCrudEngine sqlEngine = sqlFactory.getCrudEngine(&quot;ALL&quot;);
 * </pre>
 * <p>
 * In the case the SQL Processor runs on the top of the JDBC stack, there's more suitable {@link JdbcEngineFactory}.
 * 
 * <p>
 * For more info please see the <a href="https://github.com/hudec/sql-processor/wiki">Tutorials</a>.
 * 
 * @author <a href="mailto:Vladimir.Hudec@gmail.com">Vladimir Hudec</a>
 */
public class SqlDefaultFactory implements SqlEngineFactory {

    /**
     * The internal slf4j logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * The platform based line separator.
     */
    private static final String LINESEP = System.getProperty("line.separator");
    /**
     * The names of files, which hold a collection of the SQL Processor artifacts (META SQL statements, mapping rules
     * and optional features).
     */
    private List<String> metaFilesNames = new ArrayList<String>();
    /**
     * The String representation of all SQL Processor artifacts (META SQL statements, mapping rules and optional
     * features).
     */
    private StringBuilder metaStatements;
    /**
     * The factory for the META types construction.
     */
    private SqlTypeFactory typeFactory;
    /**
     * The factory for the SQL Processor plugins.
     */
    protected SqlPluginFactory pluginFactory;
    /**
     * The name of the filter to filter the META SQL statements, mapping rules and optional features.
     */
    private String filter;
    /**
     * The monitor factory used in the process of the SQL Monitor instances creation.
     */
    private SqlMonitorFactory monitorFactory;
    /**
     * The custom META types.
     */
    private List<SqlMetaType> customTypes;
    /**
     * Only statements and rules with the names in this set are picked up from the properties repository.
     */
    private String[] onlyStatements;
    /**
     * The indicator the JDBC stack is used.
     */
    private boolean jdbc;
    /**
     * The helper class for the META SQL statements and mapping rules parsing. All artifacts are loaded from new grammar
     * based files.
     */
    private volatile SqlProcessorLoader processorLoader;
    /**
     * The validator factory used in the process of the SQL Monitor instances creation.
     */
    private SqlValidatorFactory validatorFactory;
    /**
     * This flag indicates to speed up the initialization process.
     */
    private Boolean lazyInit;
    /**
     * The number of threads used for asynchronous initialization.
     */
    private Integer asyncInitThreads;
    /**
     * The overall configuration, which can be persisted.
     */
    private SqlEngineConfiguration configuration;
    /**
     * The asynchronous SQL Processor engines initialization executor. It can be a Spring TaskExecutor.
     */
    private Executor executor;
    /**
     * The flag indicating the asynchronous SQL Processor engines initialization has been finished.
     */
    private Boolean executorTerminated;
    /**
     * The indicator that Collections should be handled as standard types. This is standard behaviour for Cassandra DB..
     */
    private boolean collectionsAreStandardTypes;
    /**
     * The indicator that column aliases are not included in generated SQL command. This is standard behaviour for
     * Cassandra DB.
     */
    private boolean skipAliasesInQqlCommand;

    /**
     * Creates a new instance with no default values.
     */
    public SqlDefaultFactory() {
    }

    /**
     * Creates a new instance with no default values.
     * 
     * @param lazyInit
     *            this flag indicates to speed up the initialization process.
     */
    public SqlDefaultFactory(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    /**
     * Creates a new instance with no default values.
     * 
     * @param asyncInitThreads
     *            number of threads used for asynchronous initialization
     */
    public SqlDefaultFactory(int asyncInitThreads) {
        this.asyncInitThreads = asyncInitThreads;
    }

    /**
     * Creates a new instance with no default values.
     * 
     * @param lazyInit
     *            this flag indicates to speed up the initialization process.
     * @param asyncInitThreads
     *            number of threads used for asynchronous initialization
     */
    public SqlDefaultFactory(boolean lazyInit, int asyncInitThreads) {
        this.lazyInit = lazyInit;
        this.asyncInitThreads = asyncInitThreads;
    }

    /**
     * Dynamic initialization, called mainly from the Spring configuration initialization.
     */
    public void init() {
        if (processorLoader == null) {
            synchronized (this) {
                if (processorLoader == null) {
                    if (configuration == null)
                        setConfiguration(new SqlEngineConfiguration());
                    if (metaStatements == null && (metaFilesNames == null || metaFilesNames.isEmpty())) {
                        throw new SqlProcessorException("There are no META SQL statements");
                    }
                    if (metaStatements == null) {
                        metaStatements = SqlFilesLoader.getStatements(this.getClass(),
                                metaFilesNames.toArray(new String[0]));
                    }
                    if (jdbc)
                        metaStatements.append(LINESEP).append("JDBC(BOPT)=true;");
                    if (collectionsAreStandardTypes)
                        metaStatements.append(LINESEP).append("COLLECTIONS_ARE_STANDARD_TYPES(BOPT)=true;");
                    if (skipAliasesInQqlCommand)
                        metaStatements.append(LINESEP).append("SKIP_ALIASES_IN_SQL_COMMAND(BOPT)=true;");

                    SqlProcessorLoader loader = new SqlProcessorLoader(metaStatements, typeFactory, pluginFactory,
                            filter, monitorFactory, validatorFactory, customTypes, configuration, onlyStatements);

                    if (configuration != null) {
                        for (Entry<String, String> e : configuration.getDynamicQueryEngines().entrySet()) {
                            loader.getDynamicEngine(e.getKey(), EngineType.Query, e.getValue());
                            logger.warn("== init, initialized dynamic Query Engine {}", e.getKey());
                        }
                        for (Entry<String, String> e : configuration.getDynamicCrudEngines().entrySet()) {
                            loader.getDynamicEngine(e.getKey(), EngineType.Crud, e.getValue());
                            logger.warn("== init, initialized dynamic CRUD Engine {}", e.getKey());
                        }
                        for (Entry<String, String> e : configuration.getDynamicProcedureEngines().entrySet()) {
                            loader.getDynamicEngine(e.getKey(), EngineType.Procedure, e.getValue());
                            logger.warn("== init, initialized dynamic Procedure Engine {}", e.getKey());
                        }
                    }

                    if (!isLazyInit()) {
                        Executor _executor = getExecutor();
                        loader.init(_executor, null, null, null);
                        if (_executor != null && _executor instanceof ExecutorService)
                            ((ExecutorService) _executor).shutdown();
                    }

                    if (isLazyInit() && configuration != null) {
                        Executor _executor = getExecutor();
                        loader.init(_executor,
                                configuration.getQueryEnginesToInit(configuration.getInitTreshold()).keySet(),
                                configuration.getCrudEnginesToInit(configuration.getInitTreshold()).keySet(),
                                configuration.getProcedureEnginesToInit(configuration.getInitTreshold()).keySet());
                        if (_executor != null && _executor instanceof ExecutorService)
                            ((ExecutorService) _executor).shutdown();
                    }

                    if (configuration != null && configuration.getInitClearUsage() != null
                            && configuration.getInitClearUsage())
                        configuration.clearUsage();

                    processorLoader = loader;
                }
            }
        }
    }

    /**
     * Conditional dynamic initialization, called mainly from the Spring configuration initialization.
     */
    public void init0() {
        if (getLoader() == null) {
            init();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SqlQueryEngine getQueryEngine(String name) {
        init0();
        SqlQueryEngine sqlEngine = (SqlQueryEngine) getLoader().getEngine(name, EngineType.Query);
        if (sqlEngine != null && configuration != null)
            configuration.addQueryEngine(name);
        return sqlEngine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SqlCrudEngine getCrudEngine(String name) {
        init0();
        SqlCrudEngine sqlEngine = (SqlCrudEngine) getLoader().getEngine(name, EngineType.Crud);
        if (sqlEngine != null && configuration != null)
            configuration.addCrudEngine(name);
        return sqlEngine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SqlProcedureEngine getProcedureEngine(String name) {
        init0();
        SqlProcedureEngine sqlEngine = (SqlProcedureEngine) getLoader().getEngine(name, EngineType.Procedure);
        if (sqlEngine != null && configuration != null)
            configuration.addProcedureEngine(name);
        return sqlEngine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SqlQueryEngine getStaticQueryEngine(String name) {
        init0();
        SqlQueryEngine sqlEngine = (SqlQueryEngine) getLoader().getStaticEngine(name, EngineType.Query);
        if (sqlEngine != null && configuration != null) {
            configuration.addQueryEngine(name);
            configuration.removeDynamicQueryEngine(name);
        }
        return sqlEngine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SqlCrudEngine getStaticCrudEngine(String name) {
        init0();
        SqlCrudEngine sqlEngine = (SqlCrudEngine) getLoader().getStaticEngine(name, EngineType.Crud);
        if (sqlEngine != null && configuration != null) {
            configuration.addCrudEngine(name);
            configuration.removeDynamicCrudEngine(name);
        }
        return sqlEngine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SqlProcedureEngine getStaticProcedureEngine(String name) {
        init0();
        SqlProcedureEngine sqlEngine = (SqlProcedureEngine) getLoader().getStaticEngine(name, EngineType.Procedure);
        if (sqlEngine != null && configuration != null) {
            configuration.addProcedureEngine(name);
            configuration.removeDynamicProcedureEngine(name);
        }
        return sqlEngine;
    }

    /**
     * Check the SQL Engine instance is not null
     * 
     * @param name
     *            the name of the required SQL Engine instance
     * @param sqlEngine
     *            the checked SQL Engine instance
     * @throws SqlEngineException
     *             in the case the the SQL Engine instance is null
     */
    private void check(String name, SqlEngine sqlEngine) {
        if (sqlEngine == null)
            throw new SqlEngineException("Missing SqlEngine " + name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SqlQueryEngine getCheckedQueryEngine(String name) throws SqlEngineException {
        SqlQueryEngine sqlEngine = getQueryEngine(name);
        check(name, sqlEngine);
        return sqlEngine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SqlCrudEngine getCheckedCrudEngine(String name) {
        SqlCrudEngine sqlEngine = getCrudEngine(name);
        check(name, sqlEngine);
        return sqlEngine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SqlProcedureEngine getCheckedProcedureEngine(String name) {
        SqlProcedureEngine sqlEngine = getProcedureEngine(name);
        check(name, sqlEngine);
        return sqlEngine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SqlQueryEngine getCheckedStaticQueryEngine(String name) throws SqlEngineException {
        SqlQueryEngine sqlEngine = getStaticQueryEngine(name);
        check(name, sqlEngine);
        return sqlEngine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SqlCrudEngine getCheckedStaticCrudEngine(String name) {
        SqlCrudEngine sqlEngine = getStaticCrudEngine(name);
        check(name, sqlEngine);
        return sqlEngine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SqlProcedureEngine getCheckedStaticProcedureEngine(String name) {
        SqlProcedureEngine sqlEngine = getStaticProcedureEngine(name);
        check(name, sqlEngine);
        return sqlEngine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SqlQueryEngine getDynamicQueryEngine(String name, String sqlStatement) throws SqlEngineException {
        init0();
        SqlQueryEngine sqlEngine = (SqlQueryEngine) getLoader().getDynamicEngine(name, EngineType.Query, sqlStatement);
        if (sqlEngine != null && configuration != null)
            configuration.addDynamicQueryEngine(name, sqlStatement);
        return sqlEngine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SqlCrudEngine getDynamicCrudEngine(String name, String sqlStatement) {
        init0();
        SqlCrudEngine sqlEngine = (SqlCrudEngine) getLoader().getDynamicEngine(name, EngineType.Crud, sqlStatement);
        if (sqlEngine != null && configuration != null)
            configuration.addDynamicCrudEngine(name, sqlStatement);
        return sqlEngine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SqlProcedureEngine getDynamicProcedureEngine(String name, String sqlStatement) {
        init0();
        SqlProcedureEngine sqlEngine = (SqlProcedureEngine) getLoader().getDynamicEngine(name, EngineType.Procedure,
                sqlStatement);
        if (sqlEngine != null && configuration != null)
            configuration.addDynamicProcedureEngine(name, sqlStatement);
        return sqlEngine;
    }

    /**
     * Returns the names of files, which holds a collection of META SQL statements, mapping rules and optional features.
     * 
     * @return the names of files, which holds a collection of META SQL statements, mapping rules and optional features
     */
    public List<String> getMetaFilesNames() {
        return metaFilesNames;
    }

    /**
     * Sets the names of files, which holds a collection of META SQL statements, mapping rules and optional features.
     * 
     * @param metaFilesNames
     *            the names of files, which holds a collection of META SQL statements, mapping rules and optional
     *            features
     */
    public void setMetaFilesNames(List<String> metaFilesNames) {
        this.metaFilesNames = metaFilesNames;
    }

    /**
     * Sets the names of files, which holds a collection of META SQL statements, mapping rules and optional features.
     * 
     * @param metaFilesNames
     *            the names of files, which holds a collection of META SQL statements, mapping rules and optional
     *            features
     */
    public void setMetaFilesNames(String... metaFilesNames) {
        this.metaFilesNames = new ArrayList<String>();
        Collections.addAll(this.metaFilesNames, metaFilesNames);
    }

    /**
     * Returns the String representation of the META SQL statements, mapping rules and optional features.
     * 
     * @return the String representation of the META SQL statements, mapping rules and optional features
     */
    public StringBuilder getMetaStatements() {
        return metaStatements;
    }

    /**
     * Sets the String representation of the META SQL statements, mapping rules and optional features.
     * 
     * @param metaStatements
     *            the String representation of the META SQL statements, mapping rules and optional features
     */
    public void setMetaStatements(StringBuilder metaStatements) {
        this.metaStatements = metaStatements;
    }

    /**
     * Returns the factory for the META types construction.
     * 
     * @return the factory for the META types construction
     */
    public SqlTypeFactory getTypeFactory() {
        return typeFactory;
    }

    /**
     * Sets the factory for the META types construction.
     * 
     * @param typeFactory
     *            the factory for the META types construction
     */
    public void setTypeFactory(SqlTypeFactory typeFactory) {
        this.typeFactory = typeFactory;
    }

    /**
     * Returns the factory for the SQL Processor plugins.
     * 
     * @return the factory for the SQL Processor plugins
     */
    public SqlPluginFactory getPluginFactory() {
        return pluginFactory;
    }

    /**
     * Sets the factory for the SQL Processor plugins.
     * 
     * @param pluginFactory
     *            the factory for the SQL Processor plugins
     */
    public void setPluginFactory(SqlPluginFactory pluginFactory) {
        this.pluginFactory = pluginFactory;
    }

    /**
     * Returns the name of the filter to filter the META SQL statements, mapping rules and optional features.
     * 
     * @return the name of the filter to filter the META SQL statements, mapping rules and optional features
     */
    public String getFilter() {
        return filter;
    }

    /**
     * Sets the name of the filter to filter the META SQL statements, mapping rules and optional features.
     * 
     * @param filter
     *            the name of the filter to filter the META SQL statements, mapping rules and optional features
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }

    /**
     * Sets the name of the filter to filter the META SQL statements, mapping rules and optional features.
     * 
     * @param filter
     *            the name of the filter to filter the META SQL statements, mapping rules and optional features
     */
    public void setFilter(SqlFeature filter) {
        this.filter = filter.name();
    }

    /**
     * Returns the monitor factory used in the process of the SQL Monitor instances creation.
     * 
     * @return the monitor factory used in the process of the SQL Monitor instances creation
     */
    public SqlMonitorFactory getMonitorFactory() {
        return monitorFactory;
    }

    /**
     * Sets the monitor factory used in the process of the SQL Monitor instances creation.
     * 
     * @param monitorFactory
     *            the monitor factory used in the process of the SQL Monitor instances creation
     */
    public void setMonitorFactory(SqlMonitorFactory monitorFactory) {
        this.monitorFactory = monitorFactory;
    }

    /**
     * Returns the custom META types.
     * 
     * @return the custom META types
     */
    public List<SqlMetaType> getCustomTypes() {
        return customTypes;
    }

    /**
     * Sets the custom META types.
     * 
     * @param customTypes
     *            the custom META types
     */
    public void setCustomTypes(List<SqlMetaType> customTypes) {
        this.customTypes = customTypes;
    }

    /**
     * Sets the custom META types.
     * 
     * @param customTypes
     *            the custom META types
     */
    public void setCustomTypes(SqlMetaType... customTypes) {
        this.customTypes = new ArrayList<SqlMetaType>();
        Collections.addAll(this.customTypes, customTypes);
    }

    /**
     * Adds a new custom META type.
     * 
     * @param customType
     *            the custom META type
     */
    public void addCustomType(SqlMetaType customType) {
        if (customTypes == null)
            customTypes = new ArrayList<SqlMetaType>();
        customTypes.add(customType);
    }

    /**
     * Returns the only statements and rules, which are picked up from the properties repository.
     * 
     * @return the only statements and rules, which are picked up from the properties repository
     */
    public String[] getOnlyStatements() {
        return onlyStatements;
    }

    /**
     * Sets the only statements and rules, which are picked up from the properties repository.
     * 
     * @param selectQueries
     *            the only statements and rules, which are picked up from the properties repository
     */
    public void setOnlyStatements(String[] selectQueries) {
        if (selectQueries != null)
            this.onlyStatements = Arrays.copyOf(selectQueries, selectQueries.length);
        else
            this.onlyStatements = null;
    }

    /**
     * Returns the indicator the JDBC stack is used.
     * 
     * @return the indicator the JDBC stack is used
     */
    public boolean isJdbc() {
        return jdbc;
    }

    /**
     * Sets the indicator the JDBC stack is used.
     * 
     * @param jdbc
     *            the indicator the JDBC stack is used
     */
    public void setJdbc(boolean jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Returns the indicator that Collections should be handled as standard types.
     * 
     * @return the indicator that Collections should be handled as standard types.
     */
    public boolean isCollectionsAreStandardTypes() {
        return collectionsAreStandardTypes;
    }

    /**
     * Sets the indicator that Collections should be handled as standard types.
     * 
     * @param collectionsAreStandardTypes
     *            the indicator that Collections should be handled as standard types.
     */
    public void setCollectionsAreStandardTypes(boolean collectionsAreStandardTypes) {
        this.collectionsAreStandardTypes = collectionsAreStandardTypes;
    }

    /**
     * Returns the indicator that column aliases are not included in generated SQL command.
     * 
     * @return the indicator that column aliases are not included in generated SQL command
     */
    public boolean isSkipAliasesInQqlCommand() {
        return skipAliasesInQqlCommand;
    }

    /**
     * Sets the indicator that column aliases are not included in generated SQL command.
     * 
     * @param skipAliasesInQqlCommand
     *            the indicator that column aliases are not included in generated SQL command
     */
    public void setSkipAliasesInQqlCommand(boolean skipAliasesInQqlCommand) {
        this.skipAliasesInQqlCommand = skipAliasesInQqlCommand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLazyInit() {
        return configuration != null && configuration.getLazyInit() != null && configuration.getLazyInit();
    }

    /**
     * Sets the indicator to speed up the initialization process
     * 
     * @param lazyInit
     *            the indicator to speed up the initialization process
     */
    public void setLazyInit(Boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAsyncInitThreads() {
        if (configuration != null && configuration.getAsyncInitThreads() != null)
            return configuration.getAsyncInitThreads();
        return 0;
    }

    /**
     * Sets the number of threads used for asynchronous initialization
     * 
     * @param asyncInitThreads
     *            the number of threads used for asynchronous initialization
     */
    public void setAsyncInitThreads(Integer asyncInitThreads) {
        this.asyncInitThreads = asyncInitThreads;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isAsyncInitFinished() {
        return executorTerminated;
    }

    /**
     * Returns the internal SQL engine or processor loader.
     * 
     * @return the internal SQL engine or processor loader
     */
    public SqlProcessorLoader getLoader() {
        return processorLoader;
    }

    /**
     * Returns the validator factory used in the process of the SQL Monitor instances creation.
     * 
     * @return the validator factory used in the process of the SQL Monitor instances creation
     */
    public SqlValidatorFactory getValidatorFactory() {
        return validatorFactory;
    }

    /**
     * Sets the validator factory used in the process of the SQL Monitor instances creation.
     * 
     * @param validatorFactory
     *            the validator factory used in the process of the SQL Monitor instances creation
     */
    public void setValidatorFactory(SqlValidatorFactory validatorFactory) {
        this.validatorFactory = validatorFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getQueryNames() {
        return getLoader().getEngines(EngineType.Query).keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getQueryDynamicNames() {
        return getLoader().getDynamicEngines(EngineType.Query).keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, SqlEngine> getQueryEngines() {
        return getLoader().getEngines(EngineType.Query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, SqlEngine> getQueryDynamicEngines() {
        return getLoader().getDynamicEngines(EngineType.Query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getCrudNames() {
        return getLoader().getEngines(EngineType.Crud).keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getCrudDynamicNames() {
        return getLoader().getDynamicEngines(EngineType.Crud).keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, SqlEngine> getCrudEngines() {
        return getLoader().getEngines(EngineType.Crud);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, SqlEngine> getCrudDynamicEngines() {
        return getLoader().getDynamicEngines(EngineType.Crud);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getProcedureNames() {
        return getLoader().getEngines(EngineType.Procedure).keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getProcedureDynamicNames() {
        return getLoader().getDynamicEngines(EngineType.Procedure).keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, SqlEngine> getProcedureEngines() {
        return getLoader().getEngines(EngineType.Procedure);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, SqlEngine> getProcedureDynamicEngines() {
        return getLoader().getDynamicEngines(EngineType.Procedure);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getEnginesInitErrors() {
        return getLoader().getEnginesInitErrors();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEnginesInitErrorsMsg() {
        return getLoader().getEnginesInitErrorsMsg();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SqlEngineConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Sets the overall dynamic configuration
     * 
     * @param configuration
     *            the overall dynamic configuration
     */
    public void setConfiguration(SqlEngineConfiguration configuration) {
        this.configuration = configuration;
        if (configuration != null) {
            if (configuration.getLazyInit() == null)
                configuration.setLazyInit(lazyInit);
            if (configuration.getAsyncInitThreads() == null)
                configuration.setAsyncInitThreads(asyncInitThreads);
        }
        if (processorLoader != null) {
            processorLoader.setConfiguration(configuration);
        }
    }

    /**
     * The customized ThreadPoolExecutor.
     */
    public static class SqlThreadPoolExecutor extends ThreadPoolExecutor {

        protected final Logger logger = LoggerFactory.getLogger(getClass());
        protected SqlDefaultFactory factory;

        public SqlThreadPoolExecutor(int corePoolSize, SqlDefaultFactory factory) {
            super(corePoolSize, corePoolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
            this.factory = factory;
            if (factory != null)
                factory.executorTerminated = false;
        }

        @Override
        public void shutdown() {
            logger.warn("== shutdown: executed tasks: {}", getCompletedTaskCount());
            logger.warn("== shutdown: running tasks: {}", getActiveCount());
            logger.warn("== shutdown: pending tasks:, {}", getQueue().size());
            super.shutdown();
        }

        @Override
        protected void terminated() {
            logger.warn("== terminated");
            super.terminated();
            if (factory != null)
                factory.executorTerminated = true;
        }
    }

    /**
     * Returns the asynchronous SQL Processor engines initialization executor. It can be a Spring TaskExecutor or null
     * for the case of synchronous SQL Processor engines initialization.
     * 
     * @return the asynchronous SQL Processor engines initialization executor
     */
    protected Executor getExecutor() {
        if (executor != null)
            return executor;
        if (getAsyncInitThreads() > 0) {
            return new SqlThreadPoolExecutor(getAsyncInitThreads(), this);
        }
        return null;
    }

    /**
     * Sets the asynchronous SQL Processor engines initialization executor. It can be a Spring TaskExecutor or null for
     * the case of synchronous SQL Processor engines initialization.
     * 
     * @param executor
     *            the asynchronous SQL Processor engines initialization executor
     */
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }
}
