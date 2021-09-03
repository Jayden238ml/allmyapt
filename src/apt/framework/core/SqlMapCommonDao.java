package apt.framework.core;

import java.util.HashMap;
import java.util.List;



import javax.annotation.Resource;

import apt.framework.common.DataMap;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
@SuppressWarnings("unchecked")
public class SqlMapCommonDao extends SqlSessionDaoSupport implements CommonDao {

	public List goProcess(HashMap map) throws DataAccessException
	{
	    String procedureID = (String)map.get("procedureid");
	    List result = getSqlSession().selectList(procedureID, map);
	    return result;
	}
	  
	public DataMap goProcessObject(HashMap map) throws DataAccessException
	{
	    String procedureID = (String)map.get("procedureid");
	    DataMap result = (DataMap)getSqlSession().selectOne(procedureID, map);
	    return result;
	}
	  
	public Integer goProcessUpdate(HashMap map) throws DataAccessException
	{
	  String procedureID = (String)map.get("procedureid");
	  return Integer.valueOf(getSqlSession().update(procedureID, map));
	}
	
	public DataMap goProcessInsert(HashMap map) throws DataAccessException
	{
	  String procedureID = (String)map.get("procedureid");
	  DataMap result = new DataMap();
	  result.put("result", Integer.valueOf(getSqlSession().insert(procedureID, map)));
	  return result;
	}
	
	public void goProcessInsertVoid(HashMap map) throws DataAccessException
	{
	  String procedureID = (String)map.get("procedureid");
	  getSqlSession().insert(procedureID, map);
	}
	
	public Integer goProcessInsertInt(HashMap map) throws DataAccessException
	{
	  String procedureID = (String)map.get("procedureid");
	  return Integer.valueOf(getSqlSession().insert(procedureID, map));
	}
	
	public Integer goProcessDelete(HashMap map) throws DataAccessException
	{
	  String procedureID = (String)map.get("procedureid");
	  return Integer.valueOf(getSqlSession().delete(procedureID, map));
	}
}
