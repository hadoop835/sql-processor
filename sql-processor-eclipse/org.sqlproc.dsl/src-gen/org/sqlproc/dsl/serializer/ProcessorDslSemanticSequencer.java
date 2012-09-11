package org.sqlproc.dsl.serializer;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.serializer.acceptor.ISemanticSequenceAcceptor;
import org.eclipse.xtext.serializer.acceptor.SequenceFeeder;
import org.eclipse.xtext.serializer.diagnostic.ISemanticSequencerDiagnosticProvider;
import org.eclipse.xtext.serializer.diagnostic.ISerializationDiagnostic.Acceptor;
import org.eclipse.xtext.serializer.sequencer.AbstractDelegatingSemanticSequencer;
import org.eclipse.xtext.serializer.sequencer.GenericSequencer;
import org.eclipse.xtext.serializer.sequencer.ISemanticNodeProvider.INodesForEObjectProvider;
import org.eclipse.xtext.serializer.sequencer.ISemanticSequencer;
import org.eclipse.xtext.serializer.sequencer.ITransientValueService;
import org.eclipse.xtext.serializer.sequencer.ITransientValueService.ValueTransient;
import org.sqlproc.dsl.processorDsl.Artifacts;
import org.sqlproc.dsl.processorDsl.Column;
import org.sqlproc.dsl.processorDsl.ColumnAssignement;
import org.sqlproc.dsl.processorDsl.ColumnTypeAssignement;
import org.sqlproc.dsl.processorDsl.ColumnUsage;
import org.sqlproc.dsl.processorDsl.ColumnUsageExt;
import org.sqlproc.dsl.processorDsl.Constant;
import org.sqlproc.dsl.processorDsl.ConstantUsage;
import org.sqlproc.dsl.processorDsl.ConstantUsageExt;
import org.sqlproc.dsl.processorDsl.DatabaseColumn;
import org.sqlproc.dsl.processorDsl.DatabaseTable;
import org.sqlproc.dsl.processorDsl.ExportAssignement;
import org.sqlproc.dsl.processorDsl.Identifier;
import org.sqlproc.dsl.processorDsl.IdentifierUsage;
import org.sqlproc.dsl.processorDsl.IdentifierUsageExt;
import org.sqlproc.dsl.processorDsl.IfMetaSql;
import org.sqlproc.dsl.processorDsl.IfSql;
import org.sqlproc.dsl.processorDsl.IfSqlBool;
import org.sqlproc.dsl.processorDsl.IfSqlCond;
import org.sqlproc.dsl.processorDsl.IfSqlFragment;
import org.sqlproc.dsl.processorDsl.Import;
import org.sqlproc.dsl.processorDsl.ImportAssignement;
import org.sqlproc.dsl.processorDsl.Mapping;
import org.sqlproc.dsl.processorDsl.MappingColumn;
import org.sqlproc.dsl.processorDsl.MappingItem;
import org.sqlproc.dsl.processorDsl.MappingRule;
import org.sqlproc.dsl.processorDsl.MappingUsage;
import org.sqlproc.dsl.processorDsl.MappingUsageExt;
import org.sqlproc.dsl.processorDsl.MetaSql;
import org.sqlproc.dsl.processorDsl.MetaStatement;
import org.sqlproc.dsl.processorDsl.OptionalFeature;
import org.sqlproc.dsl.processorDsl.OrdSql;
import org.sqlproc.dsl.processorDsl.OrdSql2;
import org.sqlproc.dsl.processorDsl.PackageDeclaration;
import org.sqlproc.dsl.processorDsl.PojoDefinition;
import org.sqlproc.dsl.processorDsl.PojoEntity;
import org.sqlproc.dsl.processorDsl.PojoProperty;
import org.sqlproc.dsl.processorDsl.PojoType;
import org.sqlproc.dsl.processorDsl.ProcessorDslPackage;
import org.sqlproc.dsl.processorDsl.Property;
import org.sqlproc.dsl.processorDsl.Sql;
import org.sqlproc.dsl.processorDsl.SqlFragment;
import org.sqlproc.dsl.processorDsl.SqlTypeAssignement;
import org.sqlproc.dsl.processorDsl.TableAssignement;
import org.sqlproc.dsl.processorDsl.TableDefinition;
import org.sqlproc.dsl.processorDsl.TableUsage;
import org.sqlproc.dsl.services.ProcessorDslGrammarAccess;

@SuppressWarnings("all")
public class ProcessorDslSemanticSequencer extends AbstractDelegatingSemanticSequencer {

	@Inject
	private ProcessorDslGrammarAccess grammarAccess;
	
	public void createSequence(EObject context, EObject semanticObject) {
		if(semanticObject.eClass().getEPackage() == ProcessorDslPackage.eINSTANCE) switch(semanticObject.eClass().getClassifierID()) {
			case ProcessorDslPackage.ARTIFACTS:
				if(context == grammarAccess.getArtifactsRule()) {
					sequence_Artifacts(context, (Artifacts) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.COLUMN:
				if(context == grammarAccess.getColumnRule()) {
					sequence_Column(context, (Column) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.COLUMN_ASSIGNEMENT:
				if(context == grammarAccess.getColumnAssignementRule()) {
					sequence_ColumnAssignement(context, (ColumnAssignement) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.COLUMN_TYPE_ASSIGNEMENT:
				if(context == grammarAccess.getColumnTypeAssignementRule()) {
					sequence_ColumnTypeAssignement(context, (ColumnTypeAssignement) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.COLUMN_USAGE:
				if(context == grammarAccess.getColumnUsageRule() ||
				   context == grammarAccess.getPojoUsageRule()) {
					sequence_ColumnUsage(context, (ColumnUsage) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.COLUMN_USAGE_EXT:
				if(context == grammarAccess.getColumnUsageExtRule() ||
				   context == grammarAccess.getPojoUsageExtRule()) {
					sequence_ColumnUsageExt(context, (ColumnUsageExt) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.CONSTANT:
				if(context == grammarAccess.getConstantRule()) {
					sequence_Constant(context, (Constant) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.CONSTANT_USAGE:
				if(context == grammarAccess.getConstantUsageRule() ||
				   context == grammarAccess.getPojoUsageRule()) {
					sequence_ConstantUsage(context, (ConstantUsage) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.CONSTANT_USAGE_EXT:
				if(context == grammarAccess.getConstantUsageExtRule() ||
				   context == grammarAccess.getPojoUsageExtRule()) {
					sequence_ConstantUsageExt(context, (ConstantUsageExt) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.DATABASE_COLUMN:
				if(context == grammarAccess.getDatabaseColumnRule()) {
					sequence_DatabaseColumn(context, (DatabaseColumn) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.DATABASE_TABLE:
				if(context == grammarAccess.getDatabaseTableRule()) {
					sequence_DatabaseTable(context, (DatabaseTable) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.EXPORT_ASSIGNEMENT:
				if(context == grammarAccess.getExportAssignementRule()) {
					sequence_ExportAssignement(context, (ExportAssignement) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.IDENTIFIER:
				if(context == grammarAccess.getIdentifierRule()) {
					sequence_Identifier(context, (Identifier) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.IDENTIFIER_USAGE:
				if(context == grammarAccess.getIdentifierUsageRule() ||
				   context == grammarAccess.getPojoUsageRule()) {
					sequence_IdentifierUsage(context, (IdentifierUsage) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.IDENTIFIER_USAGE_EXT:
				if(context == grammarAccess.getIdentifierUsageExtRule() ||
				   context == grammarAccess.getPojoUsageExtRule()) {
					sequence_IdentifierUsageExt(context, (IdentifierUsageExt) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.IF_META_SQL:
				if(context == grammarAccess.getIfMetaSqlRule()) {
					sequence_IfMetaSql(context, (IfMetaSql) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.IF_SQL:
				if(context == grammarAccess.getIfSqlRule()) {
					sequence_IfSql(context, (IfSql) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.IF_SQL_BOOL:
				if(context == grammarAccess.getIfSqlBoolRule()) {
					sequence_IfSqlBool(context, (IfSqlBool) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.IF_SQL_COND:
				if(context == grammarAccess.getIfSqlCondRule()) {
					sequence_IfSqlCond(context, (IfSqlCond) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.IF_SQL_FRAGMENT:
				if(context == grammarAccess.getIfSqlFragmentRule()) {
					sequence_IfSqlFragment(context, (IfSqlFragment) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.IMPORT:
				if(context == grammarAccess.getAbstractPojoEntityRule() ||
				   context == grammarAccess.getImportRule()) {
					sequence_Import(context, (Import) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.IMPORT_ASSIGNEMENT:
				if(context == grammarAccess.getImportAssignementRule()) {
					sequence_ImportAssignement(context, (ImportAssignement) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.MAPPING:
				if(context == grammarAccess.getMappingRule()) {
					sequence_Mapping(context, (Mapping) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.MAPPING_COLUMN:
				if(context == grammarAccess.getMappingColumnRule()) {
					sequence_MappingColumn(context, (MappingColumn) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.MAPPING_ITEM:
				if(context == grammarAccess.getMappingItemRule()) {
					sequence_MappingItem(context, (MappingItem) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.MAPPING_RULE:
				if(context == grammarAccess.getMappingRuleRule()) {
					sequence_MappingRule(context, (MappingRule) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.MAPPING_USAGE:
				if(context == grammarAccess.getMappingUsageRule() ||
				   context == grammarAccess.getPojoUsageRule()) {
					sequence_MappingUsage(context, (MappingUsage) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.MAPPING_USAGE_EXT:
				if(context == grammarAccess.getMappingUsageExtRule() ||
				   context == grammarAccess.getPojoUsageExtRule()) {
					sequence_MappingUsageExt(context, (MappingUsageExt) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.META_SQL:
				if(context == grammarAccess.getMetaSqlRule()) {
					sequence_MetaSql(context, (MetaSql) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.META_STATEMENT:
				if(context == grammarAccess.getMetaStatementRule()) {
					sequence_MetaStatement(context, (MetaStatement) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.OPTIONAL_FEATURE:
				if(context == grammarAccess.getOptionalFeatureRule()) {
					sequence_OptionalFeature(context, (OptionalFeature) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.ORD_SQL:
				if(context == grammarAccess.getOrdSqlRule()) {
					sequence_OrdSql(context, (OrdSql) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.ORD_SQL2:
				if(context == grammarAccess.getOrdSql2Rule()) {
					sequence_OrdSql2(context, (OrdSql2) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.PACKAGE_DECLARATION:
				if(context == grammarAccess.getAbstractPojoEntityRule() ||
				   context == grammarAccess.getPackageDeclarationRule()) {
					sequence_PackageDeclaration(context, (PackageDeclaration) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.POJO_DEFINITION:
				if(context == grammarAccess.getPojoDefinitionRule()) {
					sequence_PojoDefinition(context, (PojoDefinition) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.POJO_ENTITY:
				if(context == grammarAccess.getAbstractPojoEntityRule() ||
				   context == grammarAccess.getPojoEntityRule()) {
					sequence_PojoEntity(context, (PojoEntity) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.POJO_PROPERTY:
				if(context == grammarAccess.getPojoPropertyRule()) {
					sequence_PojoProperty(context, (PojoProperty) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.POJO_TYPE:
				if(context == grammarAccess.getPojoTypeRule()) {
					sequence_PojoType(context, (PojoType) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.PROPERTY:
				if(context == grammarAccess.getPropertyRule()) {
					sequence_Property(context, (Property) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.SQL:
				if(context == grammarAccess.getSqlRule()) {
					sequence_Sql(context, (Sql) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.SQL_FRAGMENT:
				if(context == grammarAccess.getSqlFragmentRule()) {
					sequence_SqlFragment(context, (SqlFragment) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.SQL_TYPE_ASSIGNEMENT:
				if(context == grammarAccess.getSqlTypeAssignementRule()) {
					sequence_SqlTypeAssignement(context, (SqlTypeAssignement) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.TABLE_ASSIGNEMENT:
				if(context == grammarAccess.getTableAssignementRule()) {
					sequence_TableAssignement(context, (TableAssignement) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.TABLE_DEFINITION:
				if(context == grammarAccess.getTableDefinitionRule()) {
					sequence_TableDefinition(context, (TableDefinition) semanticObject); 
					return; 
				}
				else break;
			case ProcessorDslPackage.TABLE_USAGE:
				if(context == grammarAccess.getTableUsageRule()) {
					sequence_TableUsage(context, (TableUsage) semanticObject); 
					return; 
				}
				else break;
			}
		if (errorAcceptor != null) errorAcceptor.accept(diagnosticProvider.createInvalidContextOrTypeDiagnostic(semanticObject, context));
	}
	
	/**
	 * Constraint:
	 *     (
	 *         features+=OptionalFeature | 
	 *         statements+=MetaStatement | 
	 *         mappings+=MappingRule | 
	 *         pojos+=PojoDefinition | 
	 *         usages+=PojoUsage | 
	 *         properties+=Property | 
	 *         tables+=TableDefinition | 
	 *         tableUsages+=TableUsage | 
	 *         pojoPackages+=PackageDeclaration | 
	 *         usagesExt+=PojoUsageExt
	 *     )+
	 */
	protected void sequence_Artifacts(EObject context, Artifacts semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (dbColumn=IDENT newName=IDENT)
	 */
	protected void sequence_ColumnAssignement(EObject context, ColumnAssignement semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, ProcessorDslPackage.Literals.COLUMN_ASSIGNEMENT__DB_COLUMN) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ProcessorDslPackage.Literals.COLUMN_ASSIGNEMENT__DB_COLUMN));
			if(transientValues.isValueTransient(semanticObject, ProcessorDslPackage.Literals.COLUMN_ASSIGNEMENT__NEW_NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ProcessorDslPackage.Literals.COLUMN_ASSIGNEMENT__NEW_NAME));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getColumnAssignementAccess().getDbColumnIDENTTerminalRuleCall_0_0(), semanticObject.getDbColumn());
		feeder.accept(grammarAccess.getColumnAssignementAccess().getNewNameIDENTTerminalRuleCall_2_0(), semanticObject.getNewName());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (dbColumn=IDENT type=PojoType)
	 */
	protected void sequence_ColumnTypeAssignement(EObject context, ColumnTypeAssignement semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, ProcessorDslPackage.Literals.COLUMN_TYPE_ASSIGNEMENT__DB_COLUMN) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ProcessorDslPackage.Literals.COLUMN_TYPE_ASSIGNEMENT__DB_COLUMN));
			if(transientValues.isValueTransient(semanticObject, ProcessorDslPackage.Literals.COLUMN_TYPE_ASSIGNEMENT__TYPE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ProcessorDslPackage.Literals.COLUMN_TYPE_ASSIGNEMENT__TYPE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getColumnTypeAssignementAccess().getDbColumnIDENTTerminalRuleCall_0_0(), semanticObject.getDbColumn());
		feeder.accept(grammarAccess.getColumnTypeAssignementAccess().getTypePojoTypeParserRuleCall_2_0(), semanticObject.getType());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (statement=[MetaStatement|IDENT] pojo=[PojoEntity|IDENT])
	 */
	protected void sequence_ColumnUsageExt(EObject context, ColumnUsageExt semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, ProcessorDslPackage.Literals.POJO_USAGE_EXT__POJO) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ProcessorDslPackage.Literals.POJO_USAGE_EXT__POJO));
			if(transientValues.isValueTransient(semanticObject, ProcessorDslPackage.Literals.COLUMN_USAGE_EXT__STATEMENT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ProcessorDslPackage.Literals.COLUMN_USAGE_EXT__STATEMENT));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getColumnUsageExtAccess().getStatementMetaStatementIDENTTerminalRuleCall_1_0_1(), semanticObject.getStatement());
		feeder.accept(grammarAccess.getColumnUsageExtAccess().getPojoPojoEntityIDENTTerminalRuleCall_2_0_1(), semanticObject.getPojo());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (statement=[MetaStatement|IDENT] pojo=[PojoDefinition|IDENT])
	 */
	protected void sequence_ColumnUsage(EObject context, ColumnUsage semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, ProcessorDslPackage.Literals.POJO_USAGE__POJO) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ProcessorDslPackage.Literals.POJO_USAGE__POJO));
			if(transientValues.isValueTransient(semanticObject, ProcessorDslPackage.Literals.COLUMN_USAGE__STATEMENT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ProcessorDslPackage.Literals.COLUMN_USAGE__STATEMENT));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getColumnUsageAccess().getStatementMetaStatementIDENTTerminalRuleCall_1_0_1(), semanticObject.getStatement());
		feeder.accept(grammarAccess.getColumnUsageAccess().getPojoPojoDefinitionIDENTTerminalRuleCall_2_0_1(), semanticObject.getPojo());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     ((name=IDENT | name=IDENT_DOT | name=NUMBER) (type=IDENT? (vals+=IDENT | vals+=NUMBER)*)?)
	 */
	protected void sequence_Column(EObject context, Column semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (statement=[MetaStatement|IDENT] pojo=[PojoEntity|IDENT])
	 */
	protected void sequence_ConstantUsageExt(EObject context, ConstantUsageExt semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, ProcessorDslPackage.Literals.POJO_USAGE_EXT__POJO) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ProcessorDslPackage.Literals.POJO_USAGE_EXT__POJO));
			if(transientValues.isValueTransient(semanticObject, ProcessorDslPackage.Literals.CONSTANT_USAGE_EXT__STATEMENT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ProcessorDslPackage.Literals.CONSTANT_USAGE_EXT__STATEMENT));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getConstantUsageExtAccess().getStatementMetaStatementIDENTTerminalRuleCall_1_0_1(), semanticObject.getStatement());
		feeder.accept(grammarAccess.getConstantUsageExtAccess().getPojoPojoEntityIDENTTerminalRuleCall_2_0_1(), semanticObject.getPojo());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (statement=[MetaStatement|IDENT] pojo=[PojoDefinition|IDENT])
	 */
	protected void sequence_ConstantUsage(EObject context, ConstantUsage semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, ProcessorDslPackage.Literals.POJO_USAGE__POJO) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ProcessorDslPackage.Literals.POJO_USAGE__POJO));
			if(transientValues.isValueTransient(semanticObject, ProcessorDslPackage.Literals.CONSTANT_USAGE__STATEMENT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ProcessorDslPackage.Literals.CONSTANT_USAGE__STATEMENT));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getConstantUsageAccess().getStatementMetaStatementIDENTTerminalRuleCall_1_0_1(), semanticObject.getStatement());
		feeder.accept(grammarAccess.getConstantUsageAccess().getPojoPojoDefinitionIDENTTerminalRuleCall_2_0_1(), semanticObject.getPojo());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     ((case=PLUS | case=MINUS)? (name=IDENT | name=IDENT_DOT) (type=IDENT? (vals+=IDENT | vals+=NUMBER)*)?)
	 */
	protected void sequence_Constant(EObject context, Constant semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (name=IDENT | name=IDENT_DOT)
	 */
	protected void sequence_DatabaseColumn(EObject context, DatabaseColumn semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (name=IDENT | name=IDENT_DOT)
	 */
	protected void sequence_DatabaseTable(EObject context, DatabaseTable semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (dbColumn=IDENT fkTable=IDENT fkColumn=IDENT?)
	 */
	protected void sequence_ExportAssignement(EObject context, ExportAssignement semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (statement=[MetaStatement|IDENT] pojo=[PojoEntity|IDENT])
	 */
	protected void sequence_IdentifierUsageExt(EObject context, IdentifierUsageExt semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, ProcessorDslPackage.Literals.POJO_USAGE_EXT__POJO) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ProcessorDslPackage.Literals.POJO_USAGE_EXT__POJO));
			if(transientValues.isValueTransient(semanticObject, ProcessorDslPackage.Literals.IDENTIFIER_USAGE_EXT__STATEMENT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ProcessorDslPackage.Literals.IDENTIFIER_USAGE_EXT__STATEMENT));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIdentifierUsageExtAccess().getStatementMetaStatementIDENTTerminalRuleCall_1_0_1(), semanticObject.getStatement());
		feeder.accept(grammarAccess.getIdentifierUsageExtAccess().getPojoPojoEntityIDENTTerminalRuleCall_2_0_1(), semanticObject.getPojo());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (statement=[MetaStatement|IDENT] pojo=[PojoDefinition|IDENT])
	 */
	protected void sequence_IdentifierUsage(EObject context, IdentifierUsage semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, ProcessorDslPackage.Literals.POJO_USAGE__POJO) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ProcessorDslPackage.Literals.POJO_USAGE__POJO));
			if(transientValues.isValueTransient(semanticObject, ProcessorDslPackage.Literals.IDENTIFIER_USAGE__STATEMENT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ProcessorDslPackage.Literals.IDENTIFIER_USAGE__STATEMENT));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIdentifierUsageAccess().getStatementMetaStatementIDENTTerminalRuleCall_1_0_1(), semanticObject.getStatement());
		feeder.accept(grammarAccess.getIdentifierUsageAccess().getPojoPojoDefinitionIDENTTerminalRuleCall_2_0_1(), semanticObject.getPojo());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (
	 *         (mode=EQUALS | mode=LESS_THAN | mode=MORE_THAN)? 
	 *         (case=PLUS | case=MINUS)? 
	 *         (name=IDENT | name=IDENT_DOT | name=NUMBER) 
	 *         (type=IDENT? (vals+=IDENT | vals+=NUMBER)*)?
	 *     )
	 */
	protected void sequence_Identifier(EObject context, Identifier semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (
	 *         (ifs+=IfSql ifs+=IfSql*) | 
	 *         (type=QUESTI cond=IfSqlCond ifs+=IfSql ifs+=IfSql*) | 
	 *         (type=BAND ifs+=IfSql ifs+=IfSql*) | 
	 *         (type=BOR ifs+=IfSql ifs+=IfSql*)
	 *     )
	 */
	protected void sequence_IfMetaSql(EObject context, IfMetaSql semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     ((not?=NOT? cnst=Constant) | (not?=NOT? ident=Identifier) | (not?=NOT? cond=IfSqlCond))
	 */
	protected void sequence_IfSqlBool(EObject context, IfSqlBool semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (bool1=IfSqlBool ((oper+=AND | oper+=OR) bool2+=IfSqlBool)*)
	 */
	protected void sequence_IfSqlCond(EObject context, IfSqlCond semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (
	 *         value=IfSqlValue | 
	 *         col=Column | 
	 *         cnst=Constant | 
	 *         ident=Identifier | 
	 *         dbtab=DatabaseTable | 
	 *         dbcol=DatabaseColumn | 
	 *         meta=IfMetaSql
	 *     )
	 */
	protected void sequence_IfSqlFragment(EObject context, IfSqlFragment semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     sqls+=IfSqlFragment+
	 */
	protected void sequence_IfSql(EObject context, IfSql semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (dbColumn=IDENT pkTable=IDENT pkColumn=IDENT?)
	 */
	protected void sequence_ImportAssignement(EObject context, ImportAssignement semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     importedNamespace=QualifiedNameWithWildcard
	 */
	protected void sequence_Import(EObject context, Import semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, ProcessorDslPackage.Literals.IMPORT__IMPORTED_NAMESPACE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ProcessorDslPackage.Literals.IMPORT__IMPORTED_NAMESPACE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getImportAccess().getImportedNamespaceQualifiedNameWithWildcardParserRuleCall_1_0(), semanticObject.getImportedNamespace());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     ((name=IDENT | name=IDENT_DOT) (vals+=IDENT | vals+=NUMBER)*)
	 */
	protected void sequence_MappingColumn(EObject context, MappingColumn semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     ((col=IDENT | col=NUMBER) (type=IDENT? attr=MappingColumn?)?)
	 */
	protected void sequence_MappingItem(EObject context, MappingItem semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (name=IDENT type=MAPPING_TYPE filters+=IDENT* mapping=Mapping)
	 */
	protected void sequence_MappingRule(EObject context, MappingRule semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (statement=[MappingRule|IDENT] pojo=[PojoEntity|IDENT])
	 */
	protected void sequence_MappingUsageExt(EObject context, MappingUsageExt semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, ProcessorDslPackage.Literals.POJO_USAGE_EXT__POJO) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ProcessorDslPackage.Literals.POJO_USAGE_EXT__POJO));
			if(transientValues.isValueTransient(semanticObject, ProcessorDslPackage.Literals.MAPPING_USAGE_EXT__STATEMENT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ProcessorDslPackage.Literals.MAPPING_USAGE_EXT__STATEMENT));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getMappingUsageExtAccess().getStatementMappingRuleIDENTTerminalRuleCall_1_0_1(), semanticObject.getStatement());
		feeder.accept(grammarAccess.getMappingUsageExtAccess().getPojoPojoEntityIDENTTerminalRuleCall_2_0_1(), semanticObject.getPojo());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (statement=[MappingRule|IDENT] pojo=[PojoDefinition|IDENT])
	 */
	protected void sequence_MappingUsage(EObject context, MappingUsage semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, ProcessorDslPackage.Literals.POJO_USAGE__POJO) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ProcessorDslPackage.Literals.POJO_USAGE__POJO));
			if(transientValues.isValueTransient(semanticObject, ProcessorDslPackage.Literals.MAPPING_USAGE__STATEMENT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ProcessorDslPackage.Literals.MAPPING_USAGE__STATEMENT));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getMappingUsageAccess().getStatementMappingRuleIDENTTerminalRuleCall_1_0_1(), semanticObject.getStatement());
		feeder.accept(grammarAccess.getMappingUsageAccess().getPojoPojoDefinitionIDENTTerminalRuleCall_2_0_1(), semanticObject.getPojo());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (mappingItems+=MappingItem mappingItems+=MappingItem*)
	 */
	protected void sequence_Mapping(EObject context, Mapping semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (
	 *         (ifs+=IfSql ifs+=IfSql*) | 
	 *         (type=QUESTI cond=IfSqlCond ifs+=IfSql ifs+=IfSql*) | 
	 *         (type=BAND ifs+=IfSql ifs+=IfSql*) | 
	 *         (type=BOR ifs+=IfSql ifs+=IfSql*) | 
	 *         (type=EQUALS ftype=IDENT ifs+=IfSql) | 
	 *         (type=HASH ord=OrdSql)
	 *     )
	 */
	protected void sequence_MetaSql(EObject context, MetaSql semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (name=IDENT type=STATEMEN_TYPE filters+=IDENT* statement=Sql)
	 */
	protected void sequence_MetaStatement(EObject context, MetaStatement semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (name=IDENT type=OPTION_TYPE filters+=IDENT* option=FeatureValue)
	 */
	protected void sequence_OptionalFeature(EObject context, OptionalFeature semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (value=OrdSqlValue | cnst=Constant | ident=Identifier | dbcol=DatabaseColumn)
	 */
	protected void sequence_OrdSql2(EObject context, OrdSql2 semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     sqls+=OrdSql2+
	 */
	protected void sequence_OrdSql(EObject context, OrdSql semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     ((name=IDENT | name=IDENT_DOT) elements+=AbstractPojoEntity*)
	 */
	protected void sequence_PackageDeclaration(EObject context, PackageDeclaration semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (name=IDENT (class=IDENT | class=IDENT_DOT))
	 */
	protected void sequence_PojoDefinition(EObject context, PojoDefinition semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (abstract?='abstract'? name=IDENT superType=[PojoEntity|IDENT]? features+=PojoProperty*)
	 */
	protected void sequence_PojoEntity(EObject context, PojoEntity semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (
	 *         name=IDENT 
	 *         (
	 *             native='_char' | 
	 *             native='_byte' | 
	 *             native='_short' | 
	 *             native='_int' | 
	 *             native='_long' | 
	 *             native='_float' | 
	 *             native='_double' | 
	 *             native='_boolean' | 
	 *             ((ref=[PojoEntity|IDENT] | type=[JvmType|QualifiedName]) (gref=[PojoEntity|IDENT] | gtype=[JvmType|QualifiedName])?)
	 *         ) 
	 *         array?='[]'? 
	 *         required?='required'?
	 *     )
	 */
	protected void sequence_PojoProperty(EObject context, PojoProperty semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (
	 *         (
	 *             native='_char' | 
	 *             native='_byte' | 
	 *             native='_short' | 
	 *             native='_int' | 
	 *             native='_long' | 
	 *             native='_float' | 
	 *             native='_double' | 
	 *             native='_boolean' | 
	 *             ((ref=[PojoEntity|IDENT] | type=[JvmType|QualifiedName]) (gref=[PojoEntity|IDENT] | gtype=[JvmType|QualifiedName])?)
	 *         ) 
	 *         array?='[]'?
	 *     )
	 */
	protected void sequence_PojoType(EObject context, PojoType semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (
	 *         (name='resolve references' doResolvePojo=ON_OFF) | 
	 *         (name='database online' doResolveDb=ON_OFF) | 
	 *         (name='database url' dbUrl=PropertyValue) | 
	 *         (name='database username' dbUsername=PropertyValue) | 
	 *         (name='database password' dbPassword=PropertyValue) | 
	 *         (name='database schema' dbSchema=PropertyValue) | 
	 *         (name='database driver' dbDriver=PropertyValue) | 
	 *         (name='pojogen type sqltypes' sqlTypes+=SqlTypeAssignement+) | 
	 *         (name='pojogen type in table' dbTable=IDENT sqlTypes+=SqlTypeAssignement+) | 
	 *         (name='pojogen type for columns' dbTable=IDENT columnTypes+=ColumnTypeAssignement+) | 
	 *         (name='pojogen ignore tables' dbTables+=IDENT+) | 
	 *         (name='pojogen ignore columns' dbTable=IDENT dbColumns+=IDENT+) | 
	 *         (name='pojogen create columns' dbTable=IDENT columnTypes+=ColumnTypeAssignement+) | 
	 *         (name='pojogen rename tables' tables+=TableAssignement+) | 
	 *         (name='pojogen rename columns' dbTable=IDENT columns+=ColumnAssignement+) | 
	 *         (name='pojogen ignore one-to-many' dbTable=IDENT exports+=ExportAssignement+) | 
	 *         (name='pojogen ignore many-to-one' dbTable=IDENT imports+=ImportAssignement+) | 
	 *         (name='pojogen inherit many-to-one' dbTable=IDENT imports+=ImportAssignement+) | 
	 *         (name='pojogen create one-to-many' dbTable=IDENT exports+=ExportAssignement+) | 
	 *         (name='pojogen create many-to-one' dbTable=IDENT imports+=ImportAssignement+) | 
	 *         (name='pojogen many-to-many tables' dbTables+=IDENT+)
	 *     )
	 */
	protected void sequence_Property(EObject context, Property semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (
	 *         value=SqlValue | 
	 *         col=Column | 
	 *         cnst=Constant | 
	 *         ident=Identifier | 
	 *         meta=MetaSql | 
	 *         dbtab=DatabaseTable | 
	 *         dbcol=DatabaseColumn
	 *     )
	 */
	protected void sequence_SqlFragment(EObject context, SqlFragment semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (typeName=IDENT size=NUMBER? type=PojoType)
	 */
	protected void sequence_SqlTypeAssignement(EObject context, SqlTypeAssignement semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     sqls+=SqlFragment+
	 */
	protected void sequence_Sql(EObject context, Sql semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (dbTable=IDENT newName=IDENT)
	 */
	protected void sequence_TableAssignement(EObject context, TableAssignement semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, ProcessorDslPackage.Literals.TABLE_ASSIGNEMENT__DB_TABLE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ProcessorDslPackage.Literals.TABLE_ASSIGNEMENT__DB_TABLE));
			if(transientValues.isValueTransient(semanticObject, ProcessorDslPackage.Literals.TABLE_ASSIGNEMENT__NEW_NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ProcessorDslPackage.Literals.TABLE_ASSIGNEMENT__NEW_NAME));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getTableAssignementAccess().getDbTableIDENTTerminalRuleCall_0_0(), semanticObject.getDbTable());
		feeder.accept(grammarAccess.getTableAssignementAccess().getNewNameIDENTTerminalRuleCall_2_0(), semanticObject.getNewName());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (name=IDENT table=IDENT)
	 */
	protected void sequence_TableDefinition(EObject context, TableDefinition semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, ProcessorDslPackage.Literals.TABLE_DEFINITION__NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ProcessorDslPackage.Literals.TABLE_DEFINITION__NAME));
			if(transientValues.isValueTransient(semanticObject, ProcessorDslPackage.Literals.TABLE_DEFINITION__TABLE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ProcessorDslPackage.Literals.TABLE_DEFINITION__TABLE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getTableDefinitionAccess().getNameIDENTTerminalRuleCall_1_0(), semanticObject.getName());
		feeder.accept(grammarAccess.getTableDefinitionAccess().getTableIDENTTerminalRuleCall_2_0(), semanticObject.getTable());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (statement=[MetaStatement|IDENT] table=[TableDefinition|IDENT] prefix=IDENT?)
	 */
	protected void sequence_TableUsage(EObject context, TableUsage semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
}
