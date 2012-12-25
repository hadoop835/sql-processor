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
import org.sqlproc.sample.simple.model.PhysicalMedia;

public class PhysicalMediaDao {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private SqlEngineFactory sqlEngineFactory;
  private SqlSessionFactory sqlSessionFactory;
    	
  public PhysicalMediaDao(SqlEngineFactory sqlEngineFactory, SqlSessionFactory sqlSessionFactory) {
    this.sqlEngineFactory = sqlEngineFactory;
    this.sqlSessionFactory = sqlSessionFactory;
  }
  
  
  public PhysicalMedia insert(PhysicalMedia physicalMedia, SqlControl sqlControl) {
    if (logger.isTraceEnabled()) {
      logger.trace("insert physicalMedia: " + physicalMedia + " " + sqlControl);
    }
    SqlCrudEngine sqlInsertPhysicalMedia = sqlEngineFactory.getCrudEngine("INSERT_PHYSICAL_MEDIA");
    int count = sqlInsertPhysicalMedia.insert(sqlSessionFactory.getSqlSession(), physicalMedia);
    if (logger.isTraceEnabled()) {
      logger.trace("insert physicalMedia result: " + count + " " + physicalMedia);
    }
    return (count > 0) ? physicalMedia : null;
  }
  
  public PhysicalMedia insert(PhysicalMedia physicalMedia) {
    return insert(physicalMedia, null);
  }
  
  public PhysicalMedia get(PhysicalMedia physicalMedia, SqlControl sqlControl) {
    if (logger.isTraceEnabled()) {
      logger.trace("get get: " + physicalMedia + " " + sqlControl);
    }
    SqlCrudEngine sqlEnginePhysicalMedia = sqlEngineFactory.getCrudEngine("GET_PHYSICAL_MEDIA");
    //sqlControl = getMoreResultClasses(physicalMedia, sqlControl);
    PhysicalMedia physicalMediaGot = sqlEnginePhysicalMedia.get(sqlSessionFactory.getSqlSession(), PhysicalMedia.class, physicalMedia, sqlControl);
    if (logger.isTraceEnabled()) {
      logger.trace("get physicalMedia result: " + physicalMediaGot);
    }
    return physicalMediaGot;
  }
  	
  public PhysicalMedia get(PhysicalMedia physicalMedia) {
    return get(physicalMedia, null);
  }
  
  public int update(PhysicalMedia physicalMedia, SqlControl sqlControl) {
    if (logger.isTraceEnabled()) {
      logger.trace("update physicalMedia: " + physicalMedia + " " + sqlControl);
    }
    SqlCrudEngine sqlEnginePhysicalMedia = sqlEngineFactory.getCrudEngine("UPDATE_PHYSICAL_MEDIA");
    int count = sqlEnginePhysicalMedia.update(sqlSessionFactory.getSqlSession(), physicalMedia);
    if (logger.isTraceEnabled()) {
      logger.trace("update physicalMedia result count: " + count);
    }
    return count;
  }
  
  public int update(PhysicalMedia physicalMedia) {
    return update(physicalMedia, null);
  }
  
  public int delete(PhysicalMedia physicalMedia, SqlControl sqlControl) {
    if (logger.isTraceEnabled()) {
      logger.trace("delete physicalMedia: " + physicalMedia + " " + sqlControl);
    }
    SqlCrudEngine sqlEnginePhysicalMedia = sqlEngineFactory.getCrudEngine("DELETE_PHYSICAL_MEDIA");
    int count = sqlEnginePhysicalMedia.delete(sqlSessionFactory.getSqlSession(), physicalMedia);
    if (logger.isTraceEnabled()) {
      logger.trace("delete physicalMedia result count: " + count);
    }
    return count;
  }
  
  public int delete(PhysicalMedia physicalMedia) {
    return delete(physicalMedia, null);
  }
  
  public List<PhysicalMedia> list(PhysicalMedia physicalMedia, SqlControl sqlControl) {
    if (logger.isTraceEnabled()) {
      logger.trace("list physicalMedia: " + physicalMedia + " " + sqlControl);
    }
    SqlQueryEngine sqlEnginePhysicalMedia = sqlEngineFactory.getQueryEngine("SELECT_PHYSICAL_MEDIA");
    //sqlControl = getMoreResultClasses(physicalMedia, sqlControl);
    List<PhysicalMedia> physicalMediaList = sqlEnginePhysicalMedia.query(sqlSessionFactory.getSqlSession(), PhysicalMedia.class, physicalMedia, sqlControl);
    if (logger.isTraceEnabled()) {
      logger.trace("list physicalMedia size: " + ((physicalMediaList != null) ? physicalMediaList.size() : "null"));
    }
    return physicalMediaList;
  }
  
  public List<PhysicalMedia> list(PhysicalMedia physicalMedia) {
    return list(physicalMedia, null);
  }
}