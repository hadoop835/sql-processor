package org.sqlproc.sample.simple.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlproc.engine.SqlControl;
import org.sqlproc.engine.SqlCrudEngine;
import org.sqlproc.engine.SqlEngineFactory;
import org.sqlproc.engine.SqlQueryEngine;
import org.sqlproc.engine.SqlSessionFactory;
import org.sqlproc.engine.impl.SqlStandardControl;
import org.sqlproc.sample.simple.model.BankAccount;

public class BankAccountDao {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private SqlEngineFactory sqlEngineFactory;
  private SqlSessionFactory sqlSessionFactory;
    	
  public BankAccountDao(SqlEngineFactory sqlEngineFactory, SqlSessionFactory sqlSessionFactory) {
    this.sqlEngineFactory = sqlEngineFactory;
    this.sqlSessionFactory = sqlSessionFactory;
  }
  
  
  public BankAccount insert(BankAccount bankAccount, SqlControl sqlControl) {
    if (logger.isTraceEnabled()) {
      logger.trace("insert bankAccount: " + bankAccount + " " + sqlControl);
    }
    SqlCrudEngine sqlInsertBankAccount = sqlEngineFactory.getCrudEngine("INSERT_BANK_ACCOUNT");
    int count = sqlInsertBankAccount.insert(sqlSessionFactory.getSqlSession(), bankAccount);
    if (logger.isTraceEnabled()) {
      logger.trace("insert bankAccount result: " + count + " " + bankAccount);
    }
    return (count > 0) ? bankAccount : null;
  }
  
  public BankAccount insert(BankAccount bankAccount) {
    return insert(bankAccount, null);
  }
}
