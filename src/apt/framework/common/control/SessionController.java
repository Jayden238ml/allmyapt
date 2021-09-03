package apt.framework.common.control;

import apt.framework.common.DataMap;
import apt.framework.common.LogWriter;
import apt.framework.common.LoginSession;
import apt.framework.core.CommonFacade;
import apt.framework.util.BrowserUtil;
import apt.framework.util.MessageUtil;
import apt.framework.util.PUtil;
import com.ibatis.common.logging.Log;
import com.ibatis.common.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class SessionController extends LincActionController
{
  protected CommonFacade commonFacade;
  private PlatformTransactionManager transactionManager;
  Log log = LogFactory.getLog(getClass());

//  protected DataMap paramMap = null;

  @Autowired
  public void setTransactionManager(PlatformTransactionManager transactionManager)
  {
    this.transactionManager = transactionManager;
  }
  @Autowired
  @Qualifier("commonImpl")
  public void setCommonImpl(CommonFacade commonFacade) { this.commonFacade = commonFacade; }

  @ModelAttribute("requestParam")
  public DataMap requestParam(HttpServletRequest arg0, HttpServletResponse arg1)
    throws Exception
  {
    showParameters(arg0);
    DataMap paramMap = new PUtil().getParameterDataMap(arg0);
    setSessionMenu(this.commonFacade, arg0, paramMap);
    return paramMap;
  }

  public void showParameters(HttpServletRequest request)
  {
    this.log.debug("###############################################################");
    this.log.debug("REQUEST  URL : " + request.getRequestURL());
    Enumeration paramNames = request.getParameterNames();
    try
    {
      while (paramNames.hasMoreElements()) {
        String name = ((String)paramNames.nextElement()).toString();
        String value = StringUtils.defaultIfEmpty(request.getParameter(name), "");

        this.log.debug("PARAM : " + name.toUpperCase() + "\t VALUE : " + value);
      }

      this.log.debug("###############################################################");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @RequestMapping({"/AptLogin.do"})
  public ModelAndView login(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response) {
    DataMap sessionMap = new DataMap();

    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
    def.setPropagationBehavior(0);
    TransactionStatus status = this.transactionManager.getTransaction(def);
    try
    {
      DataMap loginMap = null;
      String ipAddr = getRemortIP(request);

      dataMap.put("LOGIN_IP", ipAddr);

      dataMap.put("procedureid", "Common.getuser_InfoForLogin");
      loginMap = this.commonFacade.getObject(dataMap);
      boolean loginSucess = true;

      if (loginMap == null || "".equals(loginMap)) {
    	  loginSucess = false;
        sessionMap.put("ERROR_CD", "901");
        this.transactionManager.commit(status);
        ModelAndView localModelAndView = new ModelAndView("jsonView", sessionMap); return localModelAndView;
      }

      if (loginSucess) {
        sessionMap.put("SESSION_USER_ID", loginMap.getString("USER_ID"));
        sessionMap.put("SESSION_USER_NM", loginMap.getString("USER_NM"));
        sessionMap.put("SESSION_USER_NICK", loginMap.getString("USER_NICK"));
        sessionMap.put("SESSION_APT_CODE", loginMap.getString("APT_CODE"));
        sessionMap.put("SESSION_USER_TYPE", loginMap.getString("USER_TYPE"));
        sessionMap.put("SESSION_USER_EMAIL", loginMap.getString("USER_EMAIL"));
        sessionMap.put("SESSION_USER_HP", loginMap.getString("USER_HP"));
        sessionMap.put("SESSION_RET_YN", loginMap.getString("RET_YN"));
        sessionMap.put("SESSION_AUTH", loginMap.getString("AUTH_CODE"));
      }
      else {
        loginSucess = false;
      }

      if (loginSucess) {
        sessionMap.put("ERROR_CD", "900");
        request.getSession().removeAttribute(new LoginSession().getSessionKey(request));
        setSession(sessionMap, request, response);

        loginMap.put("LOGIN_IP", dataMap.getString("LOGIN_IP"));
        String device = BrowserUtil.isMoblieBrowser(request.getHeader("User-Agent")) == true ? "M" : "W";
        loginMap.put("DEVICE", device);
		// 접속 브라우저 정보
		String agent = UserBrowserChk(request);
		loginMap.put("AGENT", agent);

        loginMap.put("procedureid", "Common.insertLoginIp");
        this.commonFacade.processInsert(loginMap);
        this.transactionManager.commit(status);
      }
    } catch (Exception e) {
      sessionMap.put("ERROR_CD", "999");
      e.printStackTrace();
      this.transactionManager.rollback(status);
    } finally {
      if (!status.isCompleted()) this.transactionManager.rollback(status);
    }
    return new ModelAndView("jsonView", sessionMap);
  }
  
  
  @RequestMapping({"/PwdChk.do"})
  public ModelAndView PwdChk(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response) {
	  DataMap sessionMap = new DataMap();
	  
	  DefaultTransactionDefinition def = new DefaultTransactionDefinition();
	  def.setPropagationBehavior(0);
	  TransactionStatus status = this.transactionManager.getTransaction(def);
	  try
	  {
		  DataMap cntMap = null;
		  
		  dataMap.put("procedureid", "Common.getuser_PwdChk");
		  cntMap = this.commonFacade.getObject(dataMap);
		  dataMap.put("PWD_YN", cntMap.getString("CNT"));
		  boolean loginSucess = true;
		  
	  } catch (Exception e) {
		  sessionMap.put("ERROR_CD", "999");
		  e.printStackTrace();
		  this.transactionManager.rollback(status);
	  } finally {
		  if (!status.isCompleted()) this.transactionManager.rollback(status);
	  }
	  return new ModelAndView("jsonView", dataMap);
  }
  
  @RequestMapping({"/MyInfoChange.do"})
  public ModelAndView MyInfoChange(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response) {
	  DataMap sessionMap = new DataMap();
	  
	  DefaultTransactionDefinition def = new DefaultTransactionDefinition();
	  def.setPropagationBehavior(0);
	  TransactionStatus status = this.transactionManager.getTransaction(def);
	  try
	  {
		  DataMap cntMap = null;
		  
		  dataMap.put("procedureid", "Common.getuser_Change");
		  this.commonFacade.processUpdate(dataMap);
		  
		  this.transactionManager.commit(status);
		  
		  
	  } catch (Exception e) {
		  sessionMap.put("ERROR_CD", "999");
		  e.printStackTrace();
		  this.transactionManager.rollback(status);
	  } finally {
		  if (!status.isCompleted()) this.transactionManager.rollback(status);
	  }
	  return new ModelAndView("jsonView", dataMap);
  }
  
  
  
  @RequestMapping({"/KoKaoLogin.do"})
  public ModelAndView KoKaoLogin(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response) {
	  DataMap sessionMap = new DataMap();
	  
	  DefaultTransactionDefinition def = new DefaultTransactionDefinition();
	  def.setPropagationBehavior(0);
	  TransactionStatus status = this.transactionManager.getTransaction(def);
	  try
	  {
		  DataMap loginMap = null;
		  String ipAddr = getRemortIP(request);
		  
		  dataMap.put("LOGIN_IP", ipAddr);
		  
		  if(!"".equals(dataMap.getString("USER_ID"))) {
			  dataMap.put("USER_PWD", dataMap.getString("USER_ID"));
			  dataMap.put("USER_NICK", dataMap.getString("USER_NM"));
			  
			  dataMap.put("procedureid", "Common.getuser_DupChk");
			  DataMap cnt = this.commonFacade.getObject(dataMap);
			  if("N".equals(cnt.getString("CNT"))) {
				  dataMap.put("procedureid", "Common.KaKaoUser_Insert");
			      this.commonFacade.processInsert(dataMap);
			  }
		  }
		  
		  dataMap.put("procedureid", "Common.getuser_InfoForLogin");
		  loginMap = this.commonFacade.getObject(dataMap);
		  boolean loginSucess = true;
		  
		  if (loginMap == null || "".equals(loginMap)) {
			  loginSucess = false;
			  sessionMap.put("ERROR_CD", "901");
			  this.transactionManager.commit(status);
			  ModelAndView localModelAndView = new ModelAndView("jsonView", sessionMap); return localModelAndView;
		  }
		  
		  if (loginSucess) {
			  sessionMap.put("SESSION_USER_ID", loginMap.getString("USER_ID"));
			  sessionMap.put("SESSION_USER_NM", loginMap.getString("USER_NM"));
			  sessionMap.put("SESSION_USER_NICK", loginMap.getString("USER_NICK"));
			  sessionMap.put("SESSION_APT_CODE", loginMap.getString("APT_CODE"));
			  sessionMap.put("SESSION_USER_TYPE", loginMap.getString("USER_TYPE"));
			  sessionMap.put("SESSION_USER_EMAIL", loginMap.getString("USER_EMAIL"));
			  sessionMap.put("SESSION_USER_HP", loginMap.getString("USER_HP"));
			  sessionMap.put("SESSION_RET_YN", loginMap.getString("RET_YN"));
			  sessionMap.put("SESSION_AUTH", loginMap.getString("AUTH_CODE"));
		  }
		  else {
			  loginSucess = false;
		  }
		  
		  if (loginSucess) {
			  sessionMap.put("ERROR_CD", "900");
			  request.getSession().removeAttribute(new LoginSession().getSessionKey(request));
			  setSession(sessionMap, request, response);
			  
			  loginMap.put("LOGIN_IP", dataMap.getString("LOGIN_IP"));
			  String device = BrowserUtil.isMoblieBrowser(request.getHeader("User-Agent")) == true ? "M" : "W";
			  loginMap.put("DEVICE", device);
			  // 접속 브라우저 정보
			  String agent = UserBrowserChk(request);
			  loginMap.put("AGENT", agent);
			  
			  loginMap.put("procedureid", "Common.insertLoginIp");
			  this.commonFacade.processInsert(loginMap);
			  this.transactionManager.commit(status);
		  }
	  } catch (Exception e) {
		  sessionMap.put("ERROR_CD", "999");
		  e.printStackTrace();
		  this.transactionManager.rollback(status);
	  } finally {
		  if (!status.isCompleted()) this.transactionManager.rollback(status);
	  }
	  return new ModelAndView("jsonView", sessionMap);
  }
  
  
  
  @RequestMapping({"/naverSuccess.do"})
  public ModelAndView naverSuccess(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response) {
	  DataMap sessionMap = new DataMap();
	  
	  DefaultTransactionDefinition def = new DefaultTransactionDefinition();
	  def.setPropagationBehavior(0);
	  TransactionStatus status = this.transactionManager.getTransaction(def);
	  
	  String nickname = "";            
      String name = "";
      String id = "";
      String email = "";
	  
	  try
	  {
		  String clientId = "89ZmyNVzbPmazPd_3XvM";//애플리케이션 클라이언트 아이디값";
		  String clientSecret = "mQQHnJvwmF";//애플리케이션 클라이언트 시크릿값";
		  String code = request.getParameter("code");
		  String state = request.getParameter("state");
//		  String redirectURI = URLEncoder.encode("/main.do", "UTF-8");
//		  String redirectURI = URLEncoder.encode("http://localhost:8089/naverSuccess.do", "UTF-8");
		  String redirectURI = URLEncoder.encode("http://allmyapt.com/naverSuccess.do", "UTF-8");
		  String apiURL;
		  apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&";
		  apiURL += "client_id=" + clientId;
		  apiURL += "&client_secret=" + clientSecret;
		  apiURL += "&redirect_uri=" + redirectURI;
		  apiURL += "&code=" + code;
		  apiURL += "&state=" + state;
		  String access_token = "";
		  String refresh_token = "";
		  System.out.println("apiURL="+apiURL);
		  
		  try {
		      URL url = new URL(apiURL);
		      HttpURLConnection con = (HttpURLConnection)url.openConnection();
		      con.setRequestMethod("GET");
		      int responseCode = con.getResponseCode();
		      BufferedReader br;
		      System.out.print("responseCode="+responseCode);
		      if(responseCode==200) { // 정상 호출
		        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		      } else {  // 에러 발생
		        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
		      }
		      String inputLine;
		      StringBuffer res = new StringBuffer();
		      while ((inputLine = br.readLine()) != null) {
		        res.append(inputLine);
		      }
		      br.close();
		      if(responseCode==200) {
		        System.out.println(res.toString());
		        
		        JSONParser parser = new JSONParser();
		        JSONObject result = (JSONObject)parser.parse(res.toString());
		        access_token = result.get("access_token").toString();
		        String header = "Bearer " + access_token; // Bearer 다음에 공백 추가
		        
		        String callURL = "https://openapi.naver.com/v1/nid/me";
		        
		        URL url2 = new URL(callURL);
	            HttpURLConnection con2 = (HttpURLConnection)url2.openConnection();
	            con2.setRequestMethod("GET");
	            con2.setRequestProperty("Authorization", header);
	            int responseCode2 = con2.getResponseCode();
	            BufferedReader br2;
	            if(responseCode2==200) { // 정상 호출
	                br2 = new BufferedReader(new InputStreamReader(con2.getInputStream()));
	            } else {  // 에러 발생
	                br2 = new BufferedReader(new InputStreamReader(con2.getErrorStream()));
	            }
	            String inputLine2;
	            StringBuffer response2 = new StringBuffer();
	            while ((inputLine2 = br2.readLine()) != null) {
	                response2.append(inputLine2);
	            }
	            br2.close();
	            
	            System.out.println(response2.toString());
	            
	            JSONParser parser2 = new JSONParser();
	            
	            JSONObject result2 = (JSONObject)parser2.parse(response2.toString());
	            
	            ((JSONObject)result2.get("response")).get("email");
	            
	            nickname = (String)((JSONObject)result2.get("response")).get("nickname");            
	            name = (String)((JSONObject)result2.get("response")).get("nickname");
	            id = (String)((JSONObject)result2.get("response")).get("id");
	            email = (String)((JSONObject)result2.get("response")).get("email");
		        
		      }
		  } catch (Exception e) {
			  System.out.println(e);
		  }
		  
		  
		  boolean loginSucess = true;
		  DataMap loginMap = null;
		  
		  if(!"".equals(id)) {
			  dataMap.put("USER_ID", id);
			  dataMap.put("USER_EMAIL", email);
			  dataMap.put("USER_PWD", id);
			  dataMap.put("USER_NICK", nickname);
			  dataMap.put("USER_NM", name);
			  
			  dataMap.put("procedureid", "Common.getuser_DupChk");
			  DataMap cnt = this.commonFacade.getObject(dataMap);
			  if("N".equals(cnt.getString("CNT"))) {
				  dataMap.put("procedureid", "Common.NaverUser_Insert");
			      this.commonFacade.processInsert(dataMap);
			  }
		  }
		  
		  if(!"".equals(id)) {
			  dataMap.put("procedureid", "Common.getuser_InfoForLogin");
			  loginMap = this.commonFacade.getObject(dataMap);
		  }
		  
		  if (loginMap == null || "".equals(loginMap)) {
			  loginSucess = false;
			  sessionMap.put("ERROR_CD", "901");
			  this.transactionManager.commit(status);
			  String modelName = "redirect:/main.do";
			  return new ModelAndView(modelName);
		  }
		  
		  if (loginSucess) {
			  sessionMap.put("SESSION_USER_ID", loginMap.getString("USER_ID"));
			  sessionMap.put("SESSION_USER_NM", loginMap.getString("USER_NM"));
			  sessionMap.put("SESSION_USER_NICK", loginMap.getString("USER_NICK"));
			  sessionMap.put("SESSION_APT_CODE", loginMap.getString("APT_CODE"));
			  sessionMap.put("SESSION_USER_TYPE", loginMap.getString("USER_TYPE"));
			  sessionMap.put("SESSION_USER_EMAIL", loginMap.getString("USER_EMAIL"));
			  sessionMap.put("SESSION_USER_HP", loginMap.getString("USER_HP"));
			  sessionMap.put("SESSION_RET_YN", loginMap.getString("RET_YN"));
			  sessionMap.put("SESSION_AUTH", loginMap.getString("AUTH_CODE"));
		  }
		  else {
			  loginSucess = false;
		  }
		  
		  if (loginSucess) {
			  sessionMap.put("ERROR_CD", "900");
			  request.getSession().removeAttribute(new LoginSession().getSessionKey(request));
			  setSession(sessionMap, request, response);
			  
			  loginMap.put("LOGIN_IP", dataMap.getString("LOGIN_IP"));
			  String device = BrowserUtil.isMoblieBrowser(request.getHeader("User-Agent")) == true ? "M" : "W";
			  loginMap.put("DEVICE", device);
			  // 접속 브라우저 정보
			  String agent = UserBrowserChk(request);
			  loginMap.put("AGENT", agent);
			  loginMap.put("procedureid", "Common.insertLoginIp");
			  this.commonFacade.processInsert(loginMap);
			  this.transactionManager.commit(status);
		  }
	  } catch (Exception e) {
		  sessionMap.put("ERROR_CD", "999");
		  e.printStackTrace();
		  this.transactionManager.rollback(status);
	  } finally {
		  if (!status.isCompleted()) this.transactionManager.rollback(status);
	  }
	  String modelName = "redirect:/main.do";
	  return new ModelAndView(modelName);
  }

  public void setSession(DataMap dataMap, HttpServletRequest request, HttpServletResponse response)
  {
    LoginSession loginSession = new LoginSession();

    loginSession.setSessionUsrId(dataMap.getString("SESSION_USER_ID"));
    loginSession.setSessionUsrNm(dataMap.getString("SESSION_USER_NM"));
    loginSession.setSessionNick(dataMap.getString("SESSION_USER_NICK"));
    loginSession.setSessionAptCode(dataMap.getString("SESSION_APT_CODE"));
    loginSession.setSessionUserType(dataMap.getString("SESSION_USER_TYPE"));
    loginSession.setSessionEmail(dataMap.getString("SESSION_USER_EMAIL"));
    loginSession.setSessionHp(dataMap.getString("SESSION_USER_HP"));
    loginSession.setSessionRetYn(dataMap.getString("SESSION_RET_YN"));
    loginSession.setSessionAuthCd(dataMap.getString("SESSION_AUTH"));
    if("AMC".equals(dataMap.getString("SESSION_USER_TYPE"))) {
    	loginSession.setSessionAdminYn("Y");
    }
    
    
    request.getSession().setMaxInactiveInterval(Integer.parseInt(MessageUtil.getMessage("MAX.INACTIVE.INTERVAL")));
	request.getSession().setAttribute(LoginSession.getLoginSessionKey(), loginSession);

  }

  @RequestMapping({"/moveMenu.do"})
  public ModelAndView menuMove(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response)
  {
    DataMap leftMenu = new DataMap();
    try {
      	
      if ("Y".equals(request.getSession().getAttribute("ADMIN_YN"))) {
        request.getSession().setAttribute("TOP_MENU_CODE", dataMap.getString("TOP_MENU_CODE"));
        dataMap.put("procedureid", "MenuSession.getMenuAdnLeftView");
        leftMenu = this.commonFacade.getObject(dataMap);
      } else {
    	if("T".equals(dataMap.getString("GB"))) {
    		 request.getSession().setAttribute("TMC", dataMap.getString("TMC"));
    	        dataMap.put("procedureid", "Common.getMenuUserLeftView");
    	        leftMenu = this.commonFacade.getObject(dataMap);
    	}else if("L".equals(dataMap.getString("GB"))) {
    		request.getSession().setAttribute("LMC", dataMap.getString("LMC"));
	        dataMap.put("procedureid", "Common.getMenuUserLeftView2");
	        leftMenu = this.commonFacade.getObject(dataMap);
    	}
        
      }

      if (leftMenu == null) {
    	  dataMap.put("RET_MSG", "준비중 입니다.");
      }
//      else if("TMC003".equals(dataMap.getString("TMC")) && dataMap.getString("SESSION_AUTH_CD").indexOf("APT") == -1) {
//    	  dataMap.put("RET_MSG", "계약된 회원만 시스템 사용이 가능합니다.");
//      }
      else {
        dataMap.put("leftMenu", leftMenu);
        dataMap.put("RET_MSG", "");
        request.getSession().setAttribute("LMC", leftMenu.getString("MENU_CODE"));
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      LogWriter.getLogger(getClass()).error(ex.toString());
    }

    return new ModelAndView("jsonView", dataMap);
  }
  @RequestMapping({"/logOut.do"})
  public ModelAndView logOut(HttpServletRequest request, HttpServletResponse response) {
    String modelName = "";
    String sessionGb = "";
    try
    {
      String sessionKey = new LoginSession().getSessionKey(request);
      if ((sessionKey != null) && (sessionKey.equals(LoginSession.getImsiLoginSessionKey()))) {
        sessionGb = "IMSI";
      }
      request.getSession().removeAttribute(sessionKey);
      request.getSession().invalidate();
    } catch (Exception ex) {
      ex.printStackTrace();
      LogWriter.getLogger(getClass()).error(ex.toString());
    }
    modelName = "redirect:/main.do";
    return new ModelAndView(modelName);
  }

  @RequestMapping({"/Online_logOut.do"})
  public ModelAndView Online_logOut(HttpServletRequest request, HttpServletResponse response) {
    String modelName = "";
    String sessionGb = "";
    try
    {
      String sessionKey = new LoginSession().getSessionKey(request);
      if ((sessionKey != null) && (sessionKey.equals(LoginSession.getImsiLoginSessionKey()))) {
        sessionGb = "IMSI";
      }
      request.getSession().removeAttribute(sessionKey);
      request.getSession().invalidate();
    } catch (Exception ex) {
      ex.printStackTrace();
      LogWriter.getLogger(getClass()).error(ex.toString());
    }
    modelName = "redirect:/user/Od/OnlineDiagLogin.do";
    return new ModelAndView(modelName);
  }

  public String getRemortIP(HttpServletRequest request)
  {
    if (request.getHeader("x-forwarded-for") == null) {
      return request.getRemoteAddr();
    }
    return request.getHeader("x-forwarded-for");
  }

  public String getMACAddress(String ip)
  {
    String str = "";
    String macAddress = "";
    try {
      Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);
      InputStreamReader ir = new InputStreamReader(p.getInputStream());
      LineNumberReader input = new LineNumberReader(ir);
      for (int i = 1; i < 100; i++) {
        str = input.readLine();
        if ((str != null) && 
          (str.indexOf("MAC Address") > 1)) {
          macAddress = str.substring(str.indexOf("MAC Address") + 14, str.length());
          break;
        }
      }
    }
    catch (IOException e) {
      e.printStackTrace(System.out);
    }
    return macAddress;
  }
  
  private static String UserBrowserChk(HttpServletRequest request) {
		String browser = "";
		String userAgent = request.getHeader("user-agent"); 
		
		if(userAgent.indexOf("Trident") > -1 || userAgent.indexOf("MSIE") > -1) { //IE

			if(userAgent.indexOf("Trident/7") > -1) {
				browser = "IE 11";
			}else if(userAgent.indexOf("Trident/6") > -1) {
				browser = "IE 10";
			}else if(userAgent.indexOf("Trident/5") > -1) {
				browser = "IE 9";
			}else if(userAgent.indexOf("Trident/4") > -1) {
				browser = "IE 8";
			}else if(userAgent.indexOf("edge") > -1) {
				browser = "IE edge";
			}

		}else if(userAgent.indexOf("Whale") > -1){ //네이버 WHALE
			browser = "WHALE " + userAgent.split("Whale/")[1].toString().split(" ")[0].toString();
		}else if(userAgent.indexOf("Opera") > -1 || userAgent.indexOf("OPR") > -1){ //오페라
			if(userAgent.indexOf("Opera") > -1) {
				browser = "OPERA " + userAgent.split("Opera/")[1].toString().split(" ")[0].toString();
			}else if(userAgent.indexOf("OPR") > -1) {
				browser = "OPERA " + userAgent.split("OPR/")[1].toString().split(" ")[0].toString();
			}
		}else if(userAgent.indexOf("Firefox") > -1){ //파이어폭스
			browser = "FIREFOX " + userAgent.split("Firefox/")[1].toString().split(" ")[0].toString();
		}else if(userAgent.indexOf("Safari") > -1 && userAgent.indexOf("Chrome") == -1 ){ //사파리
			browser = "SAFARI " + userAgent.split("Safari/")[1].toString().split(" ")[0].toString();
		}else if(userAgent.indexOf("Chrome") > -1){ //크롬
			browser = "CHROME " + userAgent.split("Chrome/")[1].toString().split(" ")[0].toString();
		}
		
		return browser;
	}
  
  
}