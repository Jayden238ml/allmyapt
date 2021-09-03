package apt.framework.core;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import apt.framework.common.DataMap;
import apt.framework.common.DeleteFileAndDirUtil;


import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.ServletRequestUtils;

@SuppressWarnings("deprecation")
public class CommonImpl implements CommonFacade {
	
	private CommonDao commonDao;

	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}
		
	public List list(HashMap map) throws DataAccessException {
		
		return this.commonDao.goProcess(map);
	}

	public DataMap getObject(HashMap map) throws DataAccessException{
		
		return (DataMap)this.commonDao.goProcessObject(map);
	}
		
	public Integer processUpdate(HashMap map) throws DataAccessException{
		
		return this.commonDao.goProcessUpdate(map);
	}
	
	public DataMap processInsert(HashMap map) throws DataAccessException{
		
		return this.commonDao.goProcessInsert(map);
	}
	
	public Integer processDelete(HashMap map) throws DataAccessException{
		
		return this.commonDao.goProcessDelete(map);
	}
	
	public List list(HttpServletRequest request) throws DataAccessException{
		
		HashMap param = new HashMap();
		Enumeration eParam = request.getParameterNames();
		while(eParam.hasMoreElements()){
			String pName = (String)eParam.nextElement();
			String[] pValue = request.getParameterValues(pName);

			param.put(pName, StringUtils.defaultString(pValue[0]));
		}
		
		return this.commonDao.goProcess(param);
	}
	
	
	public List load(HttpServletRequest request) throws DataAccessException{
		
		String procedureID =  ServletRequestUtils.getStringParameter(request, "procedureid", "");
		String[] tmpKey = StringUtils.split(StringUtils.defaultString(request.getParameter("loadKey")),",");

		HashMap param = new HashMap();
		for(int i=0 ; i<tmpKey.length;i++){
			param.put("key"+(i+1) , tmpKey[i]);
		}
		param.put("procedureid", procedureID);

		return this.commonDao.goProcess(param);

	}
	
	public List delete(HttpServletRequest request) throws DataAccessException{
		int errorcode = 0;
		
		String procedureID =  ServletRequestUtils.getStringParameter(request, "procedureid", "");
		String[] chk = request.getParameterValues("check");
		
		List list = null;
		for(int i=0; i < chk.length; i++){
			String[] tmpKey = StringUtils.split(chk[i],"^");
			HashMap param = new HashMap();
			for(int j=0 ; j<tmpKey.length;j++){
				param.put("key"+(j+1) , tmpKey[j]);
				
			}
			param.put("procedureid", procedureID);

			list = this.commonDao.goProcess(param);
			errorcode = Utils.getErrcode(list);
			
			if(errorcode != 3){
				try{
					throw new Exception("");
				}catch(Exception ex){
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}
			}
		}
		return list;

	}
	
	public List insert(HttpServletRequest request) throws DataAccessException{
		int errorcode = 0;

		String[] ProcedureID = StringUtils.split(StringUtils.defaultString(request.getParameter("procedureid")),",");

		HashMap param = new HashMap();
		Enumeration eParam = request.getParameterNames();
		while(eParam.hasMoreElements()){
			String pName = (String)eParam.nextElement();
			String[] pValue = request.getParameterValues(pName);

			if(pValue.length == 1){
				if(pName.trim().equals("etc")){
					//param.put(pName, StringUtils.defaultString(MimeContentsDecode.getDecodeing(request,pValue[0])));
				}else{
					param.put(pName, StringUtils.defaultString(pValue[0]));
				}
			}
		}
		param.put("procedureid", ProcedureID[0]);
		
		List list = this.commonDao.goProcess(param);
		errorcode = Utils.getErrcode(list);
		
		if(errorcode != 1 && errorcode != 2){
			try{
				throw new Exception("");
			}catch(Exception ex){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
		}

		return list;		
	}		
	
	public Integer insertInt(HttpServletRequest request) throws DataAccessException{
		Integer errorMsg = 0;
		
		String[] ProcedureID = StringUtils.split(StringUtils.defaultString(request.getParameter("procedureid")),",");

		HashMap param = new HashMap();
		Enumeration eParam = request.getParameterNames();
		while(eParam.hasMoreElements()){
			String pName = (String)eParam.nextElement();
			String[] pValue = request.getParameterValues(pName);

			if(pValue.length == 1){
				param.put(pName, StringUtils.defaultString(pValue[0]));
			}
		}
		param.put("procedureid", ProcedureID[0]);
		errorMsg = this.commonDao.goProcessInsertInt(param);

		if(errorMsg.intValue() != 1){
			try{
				throw new Exception("");
			}catch(Exception ex){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
		}

		return errorMsg;
	}
	
	public Integer updateInt(HttpServletRequest request) throws DataAccessException{
		Integer errorMsg = 0;
		String[] ProcedureID = StringUtils.split(StringUtils.defaultString(request.getParameter("procedureid")),",");

		HashMap param = new HashMap();
		Enumeration eParam = request.getParameterNames();
		while(eParam.hasMoreElements()){
			String pName = (String)eParam.nextElement();
			String[] pValue = request.getParameterValues(pName);

			if(pValue.length == 1){
				param.put(pName, StringUtils.defaultString(pValue[0]));
			}
		}
		param.put("procedureid", ProcedureID[0]);
		
		errorMsg = this.commonDao.goProcessUpdate(param);
		if(errorMsg.intValue() != 1){
			try{
				throw new Exception("");
			}catch(Exception ex){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
		}
		return errorMsg;
	}
		
	public List insertArr(HttpServletRequest request) throws DataAccessException{
		int errorcode = 0;

		String[] ProcedureID = StringUtils.split(StringUtils.defaultString(request.getParameter("procedureid")),",");
		
		int dclParamCount = 0;
		int dclParamArray = 0;
		
		Enumeration eCnt = request.getParameterNames();
		while(eCnt.hasMoreElements()){
			String cntName = (String)eCnt.nextElement();
			String[] cntValue = request.getParameterValues(cntName);
			if(cntValue.length > 1){
				++dclParamCount;
				dclParamArray = cntValue.length-1;
			}
		}
		
		String[]   ParamCount = new String[dclParamCount];
		String[][] ParamArray = new String[dclParamCount][dclParamArray];
		int paramIdx = 0;
		int GlobalIdx = 0;

		HashMap param = new HashMap();
		Enumeration eParam = request.getParameterNames();
		while(eParam.hasMoreElements()){
			String pName = (String)eParam.nextElement();
			String[] pValue = request.getParameterValues(pName);

			if(pValue.length == 1){
				param.put(pName, StringUtils.defaultString(pValue[0]));
			}else{
				ParamCount[paramIdx] = pName;
				
				for(int valueIdx=0; valueIdx<pValue.length-1; valueIdx++){
					ParamArray[paramIdx][valueIdx] 	= pValue[valueIdx+1];
				}
				paramIdx++;
				GlobalIdx = pValue.length;
			}
			
		}
		param.put("procedureid", ProcedureID[0]);
		
		List listMain = this.commonDao.goProcess(param);
		errorcode = Utils.getErrcode(listMain);
		
		if(errorcode == 1 || errorcode == 2){
			if(dclParamCount > 0){
				Map listMap = (Map)listMain.get(0);
				String[] returnKey = new String[listMap.size()-1];
				for(int i=0; i<listMap.size()-1; i++){
					returnKey[i] = listMap.get("returnKey"+(i+1))+"";
				}
				
				for(int i=1; i<GlobalIdx; i++){
					HashMap paramSub = new HashMap();

					for(int key=0; key<returnKey.length; key++){
						paramSub.put("returnKey"+(key+1), returnKey[key]);
					}
					
					for(int val=0; val<ParamCount.length; val++){
						paramSub.put(ParamCount[val],ParamArray[val][i-1]);
					}
					paramSub.put("procedureid", ProcedureID[1]);

					
					List listSub = this.commonDao.goProcess(param);
					errorcode = Utils.getErrcode(listSub);


					if(errorcode != 1 && errorcode != 2){
						try{
							throw new Exception("");
						}catch(Exception ex){
							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						}
					}
				}
			}
		}else{
			try{
				throw new Exception("");
			}catch(Exception ex){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
		}

		return listMain;
		
	}
	
	public List insertArrFile(HttpServletRequest request) throws DataAccessException{
		int errorcode = 0;
		
		String[] ProcedureID = StringUtils.split(StringUtils.defaultString(request.getParameter("procedureid")),",");

		int dclParamCount = 0;
		int dclParamArray = 0;
		
		Enumeration eCnt = request.getParameterNames();
		while(eCnt.hasMoreElements()){
			String cntName = (String)eCnt.nextElement();
			String[] cntValue = request.getParameterValues(cntName);
			if(cntValue.length > 1){
				++dclParamCount;
				dclParamArray = cntValue.length-1;
			}
		}
		
		String[]   ParamCount = new String[dclParamCount];
		String[][] ParamArray = new String[dclParamCount][dclParamArray];
		int paramIdx = 0;
		int GlobalIdx = 0;

		HashMap param = new HashMap();
		Enumeration eParam = request.getParameterNames();
		while(eParam.hasMoreElements()){
			String pName = (String)eParam.nextElement();
			String[] pValue = request.getParameterValues(pName);

			if(pValue.length == 1){
				param.put(pName, StringUtils.defaultString(pValue[0]));
			}else{
				ParamCount[paramIdx] = pName;
				
				for(int valueIdx=0; valueIdx<pValue.length-1; valueIdx++){
					ParamArray[paramIdx][valueIdx] 	= pValue[valueIdx+1];
				}
				paramIdx++;
				GlobalIdx = pValue.length;
			}
		}
		param.put("procedureid", ProcedureID[0]);
		
		List listMain = this.commonDao.goProcess(param);
		errorcode = Utils.getErrcode(listMain);
			
		
		if(errorcode == 1 || errorcode == 2){
			if(dclParamCount > 0){

				String dbPath = StringUtils.defaultString(request.getParameter("filepath"));
				String SystemDirctoryReal = request.getRealPath(dbPath);
				DeleteFileAndDirUtil.MakeDir(request.getRealPath(dbPath)); 
				
				int fileInsertMode = 0;
				
				String[] temp_file_path = request.getParameterValues("file_path");
				String[] file_nm_real = request.getParameterValues("file_nm_real");
				String[] delflag = request.getParameterValues("delflag");
				if(dbPath.trim().equals("null")){
					fileInsertMode = 1;
				}

				Map listMap = (Map)listMain.get(0);
				String[] returnKey = new String[listMap.size()-1];
				for(int i=0; i<listMap.size()-1; i++){
					returnKey[i] = listMap.get("returnKey"+(i+1))+"";
				}
				
				for(int i=1; i<GlobalIdx; i++){
					HashMap paramSub = new HashMap();

					for(int key=0; key<returnKey.length; key++){
						paramSub.put("returnKey"+(key+1), returnKey[key]);
					}
					
					for(int val=0; val<ParamCount.length; val++){

						if(ParamCount[val].trim().equals("file_path")){
							paramSub.put(ParamCount[val],dbPath);
						}else{
							paramSub.put(ParamCount[val],ParamArray[val][i-1]);
						}
					}
					paramSub.put("procedureid", ProcedureID[1]);

					
					List listSub = this.commonDao.goProcess(paramSub);
					errorcode = Utils.getErrcode(listSub);

					if(errorcode==1 || errorcode==2){
						if(fileInsertMode==0){
							if(delflag[i].trim().equals("1")){
								DeleteFileAndDirUtil.deleteFile(SystemDirctoryReal+"/"+ file_nm_real[i]);	
							}else if(delflag[i].trim().equals("0")){
								try{
									DeleteFileAndDirUtil.FileMove(temp_file_path[i]+"/"+file_nm_real[i] , SystemDirctoryReal+"/"+ file_nm_real[i]);
								}catch(Exception ex){
									ex.printStackTrace();
								}
							}
						}
					}else{
						try{
							throw new Exception("");
						}catch(Exception ex){
							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						}
					}

				}
			}
			
		}else{
			try{
				throw new Exception("");
			}catch(Exception ex){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
		}
		
		return listMain;
			
	}
	
	public void goProcessInsertVoid(DataMap map) {
		this.commonDao.goProcessInsertVoid(map);
		
	}
	
	public Integer processInsertInt(HashMap map) throws DataAccessException{
		
		return this.commonDao.goProcessInsertInt(map);
	}
}
