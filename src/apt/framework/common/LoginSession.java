package apt.framework.common;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class LoginSession implements Serializable{

	public static  final String LOGIN_SESSION_KEY	= "CAREER_SESSION_KEY";
	public static  final String IMSI_LOGIN_SESSION_KEY	= "IMSI_LOGIN_SESSION_KEY";
	
	private String sessionId = null;
	
	
	private String sessionUsrId = null;			//ID
	private String sessionUsrNm = null;		//이름
	private String sessionNick = null;			//닉네임
	private String sessionAptCode = null;		//아파트 코드
	private String sessionUserType = null;		//사용자구분
	private String sessionEmail = null;			//이메일
	private String sessionHp = null;		//연락처
	private String sessionRetYn = null;		//탈퇴여부
	
	private String sessionAuthCd = null;		//권한코드 세션
	private String sessionAdminYn = null;		//권한코드 세션
	
	private DataMap sessionAptLeftmenuList = null ;//탑 메뉴
	private List<DataMap> sessionAptTopmenuList = null ;//레프트 메뉴
	
	//모바일 여부
	private String sessionMobileYn = null;
	
	
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getSessionUsrId() {
		return sessionUsrId;
	}
	public void setSessionUsrId(String sessionUsrId) {
		this.sessionUsrId = sessionUsrId;
	}
	public String getSessionUsrNm() {
		return sessionUsrNm;
	}
	public void setSessionUsrNm(String sessionUsrNm) {
		this.sessionUsrNm = sessionUsrNm;
	}
	public String getSessionEmail() {
		return sessionEmail;
	}
	public void setSessionEmail(String sessionEmail) {
		this.sessionEmail = sessionEmail;
	}
	public String getSessionNick() {
		return sessionNick;
	}
	public void setSessionNick(String sessionNick) {
		this.sessionNick = sessionNick;
	}
	public static String getLoginSessionKey() {
		return LOGIN_SESSION_KEY;
	}
	public static String getImsiLoginSessionKey() {
		return IMSI_LOGIN_SESSION_KEY;
	}
	public String getSessionAptCode() {
		return sessionAptCode;
	}
	public void setSessionAptCode(String sessionAptCode) {
		this.sessionAptCode = sessionAptCode;
	}
	public String getSessionUserType() {
		return sessionUserType;
	}
	public void setSessionUserType(String sessionUserType) {
		this.sessionUserType = sessionUserType;
	}
	public String getSessionHp() {
		return sessionHp;
	}
	public void setSessionHp(String sessionHp) {
		this.sessionHp = sessionHp;
	}
	public String getSessionRetYn() {
		return sessionRetYn;
	}
	public void setSessionRetYn(String sessionRetYn) {
		this.sessionRetYn = sessionRetYn;
	}
	public String getSessionAuthCd() {
		return sessionAuthCd;
	}
	public void setSessionAuthCd(String sessionAuthCd) {
		this.sessionAuthCd = sessionAuthCd;
	}
	public String getSessionAdminYn() {
		return sessionAdminYn;
	}
	public void setSessionAdminYn(String sessionAdminYn) {
		this.sessionAdminYn = sessionAdminYn;
	}
	public DataMap getSessionAptLeftmenuList() {
		return sessionAptLeftmenuList;
	}
	public void setSessionAptLeftmenuList(DataMap sessionAptLeftmenuList) {
		this.sessionAptLeftmenuList = sessionAptLeftmenuList;
	}
	public List<DataMap> getSessionAptTopmenuList() {
		return sessionAptTopmenuList;
	}
	public void setSessionAptTopmenuList(List<DataMap> sessionAptTopmenuList) {
		this.sessionAptTopmenuList = sessionAptTopmenuList;
	}
	public String getSessionKey(HttpServletRequest request){
		
		if(request.getSession().getAttribute(LoginSession.getImsiLoginSessionKey()) != null){
			return getImsiLoginSessionKey(); 
		}else{
			return getLoginSessionKey();
		}
	}
	public String getSessionMobileYn() {
		return sessionMobileYn;
	}
	public void setSessionMobileYn(String sessionMobileYn) {
		this.sessionMobileYn = sessionMobileYn;
	}
}
