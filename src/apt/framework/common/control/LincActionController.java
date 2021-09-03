package apt.framework.common.control;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import apt.framework.common.DataMap;
import apt.framework.common.LoginSession;
import apt.framework.core.CommonFacade;
import apt.framework.util.BrowserUtil;

import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.ibatis.common.logging.Log;
import com.ibatis.common.logging.LogFactory;

public class LincActionController extends MultiActionController  {
Log log = LogFactory.getLog(this.getClass());
	
	
	private CommonFacade commonFacade;
	private void setCommonFacade(CommonFacade commonFacade){
		if(this.commonFacade == null){
			this.commonFacade = commonFacade;
		}
	}
	public void CommonBoardData(CommonFacade commonFacade){
		setCommonFacade(commonFacade);
	}
	
	
	/**
	 * 
	 * @param arg0 
	 * @param paramMap 
	 * @param dataMap
	 */
	@SuppressWarnings("null")
	public void setSessionMenu( CommonFacade commonFacade, HttpServletRequest arg0, DataMap paramMap ){
		
		//세션 체크시 break
		if("checkSessionAlive".equals(paramMap.getString("METHOD"))){
			return;
		}
		
		List topInfo  = null;
		DataMap AdnLeftMenu = new DataMap();
		DataMap UserLeftMenu = new DataMap();
		LoginSession loginSession = new LoginSession();
		LoginSession session = (LoginSession) arg0.getSession().getAttribute(loginSession.getSessionKey(arg0))==null?new LoginSession():
			(LoginSession) arg0.getSession().getAttribute(loginSession.getSessionKey(arg0));
		try{
			//메뉴조회
			
			String authCode = paramMap.getString("SESSION_AUTH_CD");
			String loginYn =paramMap.getString("SESSION_YN");  						// 세션 여부
			String topMenu = paramMap.getString("TMC");

			paramMap.put("authCode",authCode); //권한 코드
			
			if("".equals(paramMap.getString("TMC"))) {
				if(!"".equals(paramMap.getString("H_TMC"))) {
					paramMap.put("TMC", paramMap.getString("H_TMC"));
				}else if(!"".equals(paramMap.getString("L_TMC"))) {
					paramMap.put("TMC", paramMap.getString("L_TMC"));
				}
			}
			if("".equals(paramMap.getString("LMC"))) {
				if(!"".equals(paramMap.getString("H_LMC"))) {
					paramMap.put("LMC", paramMap.getString("H_LMC"));
				}else if(!"".equals(paramMap.getString("L_LMC"))) {
					paramMap.put("LMC", paramMap.getString("L_LMC"));
				}
			}
			
			arg0.setAttribute("TMC", paramMap.getString("TMC"));
			arg0.setAttribute("LMC", paramMap.getString("LMC"));
			
			if("".equals(session.getSessionAptTopmenuList()) || null == session.getSessionAptTopmenuList()){
				// 관리자TOP메뉴 조회
				paramMap.put("procedureid", "Common.getMenuUserTop");
				List UserTopmenu = commonFacade.list(paramMap);
				session.setSessionAptTopmenuList(UserTopmenu);
			}
			
			if("".equals(session.getSessionAptLeftmenuList()) || null == session.getSessionAptLeftmenuList()){
				// 업무목록 조회
				
				paramMap.put("procedureid", "Common.getMenuUserLeft");
				List UserLeftMenuList = commonFacade.list(paramMap);
				UserLeftMenu.put("USERLEFTMENU", UserLeftMenuList);
				session.setSessionAptLeftmenuList(UserLeftMenu);
			}
			
			arg0.getSession().setAttribute(loginSession.getSessionKey(arg0), session);
			paramMap.put("USER_TOP_MENU", session.getSessionAptTopmenuList());
			paramMap.put("USER_LEFT_MENU", session.getSessionAptLeftmenuList());
			
			//탑메뉴 코드가 없는경우 파라메터의 탑메뉴코드 셋팅
			if(!"".equals(paramMap.getString("TMC")))
			{
				arg0.getSession().setAttribute("TMC", paramMap.getString("TMC"));
			}else{
				arg0.getSession().setAttribute("TMC", arg0.getSession().getAttribute("TMC"));
			}
			//처리시 레프트 메뉴코드 셋팅
			if(!"".equals(paramMap.getString("LMC"))){
				arg0.getSession().setAttribute("LMC", paramMap.getString("LMC"));
			}else{
				arg0.getSession().setAttribute("LMC", arg0.getSession().getAttribute("LMC"));
			}
			
			 
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		/*return;*/
		setSessionCheck(commonFacade,arg0,paramMap);

		
	}	
	
	/**
	 * 주소의 값을 받아 권한여부를 비교
	 * @param arg0 
	 * @param paramMap 
	 * @param dataMap
	 */
	@SuppressWarnings("null")
	public void setSessionCheck( CommonFacade commonFacade, HttpServletRequest arg0, DataMap paramMap ){
		
		DataMap menuChk = new DataMap();
		String reqUrl = arg0.getServletPath();
		DataMap menuInfo  = null;
		String menuCd = "";
		paramMap.put("RETOK"	, "Y");
		String refReq = arg0.getHeader("REFERER");
		String WIN_POP_CHK = paramMap.getString("POP_OPEN_YN"); // 팝업인경우
		
		if("/moveMenu.do".equals(reqUrl) || "/main.do".equals(reqUrl) || reqUrl.indexOf("/common/index.do") > -1 || reqUrl.indexOf("/common/swfUpload.do") > -1 ||
			"Y".equals(WIN_POP_CHK)	|| reqUrl.indexOf("/amc/") > -1 || reqUrl.indexOf("/Sms/") > -1
				){
			paramMap.put("RETOK"	, "Y");
		}else if("".equals(paramMap.getString("TMC")) && "".equals(paramMap.getString("LMC"))) {
			paramMap.put("RETOK"	, "N");
		}
		return;
	}
}
