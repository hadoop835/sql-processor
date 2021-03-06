package org.sample.dao;

import java.util.List;
import org.sample.model.NewPersonOutRs;
import org.sample.model.Person;
import org.slf4j.Logger;
import org.sqlproc.engine.SqlControl;
import org.sqlproc.engine.SqlEngineFactory;
import org.sqlproc.engine.SqlSession;
import org.sqlproc.engine.SqlSessionFactory;

@SuppressWarnings("all")
public class NewPersonOutRsDao {
  protected final Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());
  
  public NewPersonOutRsDao() {
  }
  
  public NewPersonOutRsDao(final SqlEngineFactory sqlEngineFactory) {
    this.sqlEngineFactory = sqlEngineFactory;
  }
  
  public NewPersonOutRsDao(final SqlEngineFactory sqlEngineFactory, final SqlSessionFactory sqlSessionFactory) {
    this.sqlEngineFactory = sqlEngineFactory;
    this.sqlSessionFactory = sqlSessionFactory;
  }
  
  protected SqlEngineFactory sqlEngineFactory;
  
  protected SqlSessionFactory sqlSessionFactory;
  
  public List<Person> newPersonOutRs(final SqlSession sqlSession, final NewPersonOutRs newPersonOutRs, SqlControl sqlControl) {
    if (logger.isTraceEnabled()) {
    	logger.trace("sql newPersonOutRs: " + newPersonOutRs + " " + sqlControl);
    }
    org.sqlproc.engine.SqlProcedureEngine sqlProcNewPersonOutRsDao = sqlEngineFactory.getCheckedProcedureEngine("PROC_NEW_PERSON_OUT_RS");
    List<Person> list = sqlProcNewPersonOutRsDao.callQuery(sqlSession, Person.class, newPersonOutRs, sqlControl);
    if (logger.isTraceEnabled()) {
    	logger.trace("sql newPersonOutRs result: " + list);
    }
    return list;
  }
  
  public List<Person> newPersonOutRs(final NewPersonOutRs newPersonOutRs, SqlControl sqlControl) {
    return newPersonOutRs(sqlSessionFactory.getSqlSession(), newPersonOutRs, sqlControl);
  }
  
  public List<Person> newPersonOutRs(final SqlSession sqlSession, final NewPersonOutRs newPersonOutRs) {
    return newPersonOutRs(sqlSession, newPersonOutRs, null);
  }
  
  public List<Person> newPersonOutRs(final NewPersonOutRs newPersonOutRs) {
    return newPersonOutRs(newPersonOutRs, null);
  }
}
