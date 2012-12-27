package org.sqlproc.sample.simple.dao;

import java.util.List;
import org.sqlproc.engine.SqlControl;
import org.sqlproc.sample.simple.model.Library;

public interface LibraryDao {

  
  public Library insert(Library library, SqlControl sqlControl);
  
  public Library insert(Library library);
  
  public Library get(Library library, SqlControl sqlControl);
  	
  public Library get(Library library);
  
  public int update(Library library, SqlControl sqlControl);
  
  public int update(Library library);
  
  public int delete(Library library, SqlControl sqlControl);
  
  public int delete(Library library);
  
  public List<Library> list(Library library, SqlControl sqlControl);
  
  public List<Library> list(Library library);
}
