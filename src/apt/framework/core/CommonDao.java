package apt.framework.core;

import java.util.HashMap;
import java.util.List;

import apt.framework.common.DataMap;


import org.springframework.dao.DataAccessException;
@SuppressWarnings("unchecked")
public interface CommonDao {
	public List goProcess(HashMap map) throws DataAccessException;
	public DataMap goProcessObject(HashMap map) throws DataAccessException;
	public Integer goProcessUpdate(HashMap map) throws DataAccessException;
	public DataMap goProcessInsert(HashMap map) throws DataAccessException;
	public void    goProcessInsertVoid(HashMap map) throws DataAccessException;
	public Integer goProcessDelete(HashMap map) throws DataAccessException;
	public Integer goProcessInsertInt(HashMap map) throws DataAccessException;
}
