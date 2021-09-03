package apt.framework.core;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import apt.framework.common.DataMap;

@SuppressWarnings("unchecked")
public interface CommonFacade {
	List list(HashMap map);
	DataMap getObject(HashMap map);
	Integer processUpdate(HashMap map);
	DataMap processInsert(HashMap map);
	Integer processDelete(HashMap map);
	void goProcessInsertVoid(DataMap map);
	List list(HttpServletRequest request);
	List load(HttpServletRequest request);
	List delete(HttpServletRequest request);
	List insert(HttpServletRequest request);
	Integer insertInt(HttpServletRequest request);
	Integer updateInt(HttpServletRequest request);
	List insertArr(HttpServletRequest request);
	List insertArrFile(HttpServletRequest request);
	Integer processInsertInt(HashMap map);
	
}