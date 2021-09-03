package apt.admin.control;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import apt.framework.common.DataMap;
import apt.framework.common.control.LincActionController;
import apt.framework.common.control.MailDataSet;
import apt.framework.core.CommonFacade;
import apt.framework.util.MessageUtil;
import apt.framework.util.PUtil;
import apt.framework.util.Utils;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.SystemOutLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.ibatis.common.logging.Log;
import com.ibatis.common.logging.LogFactory;


@Controller
public class CafeMemController extends LincActionController {
	protected CommonFacade commonFacade;
	private PlatformTransactionManager transactionManager;
	Log log = LogFactory.getLog(this.getClass());
	
	public static final String WEB_DRIVER_ID = "webdriver.chrome.driver"; //드라이버 ID
  	public static final String WEB_DRIVER_PATH = "D:\\chromedriver.exe"; //드라이버 경로
  	private static WebDriver driver;
  	private static WebElement element;

	@Autowired
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	@Autowired
	@Qualifier("commonImpl")
	public void setCommonImpl(CommonFacade commonFacade) {
		this.commonFacade = commonFacade;
	}

	// protected DataMap paramMap= null;
	@ModelAttribute("requestParam")
	public DataMap requestParam(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
		showParameters(arg0);
		DataMap paramMap = new PUtil().getParameterDataMap(arg0);
		setSessionMenu(commonFacade, arg0, paramMap);
		if ("N".equals(paramMap.getString("RETOK"))) {
			arg1.sendRedirect("/main.do");
		}

		if ("".equals(paramMap.getString("SESSION_USER_ID")) || !"Y".equals(paramMap.getString("SESSION_ADMIN_YN")) || !"AMC".equals(paramMap.getString("SESSION_USER_TYPE"))) {
			arg1.sendRedirect("/main.do");
		}
		return paramMap;
	}

	/**
	 * Show Request Parameter
	 *
	 * @param request
	 * @return void
	 * @throws Exception
	 */
	public void showParameters(HttpServletRequest request) {
		log.debug("###############################################################");
		log.debug("REQUEST  URL : " + request.getRequestURL());
		Enumeration<String> paramNames = request.getParameterNames();

		try {
			while (paramNames.hasMoreElements()) {
				String name = paramNames.nextElement().toString();
				String value = StringUtils.defaultIfEmpty(request.getParameter(name), "");
				log.debug("PARAM : " + name.toUpperCase() + "\t VALUE : " + value);
			}
			log.debug("###############################################################");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	@RequestMapping(value = "/amc/amcNaverCafeMem.do")
	public ModelAndView amcNaverCafeMem(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request,HttpServletResponse response){
		
		String modelName = "";
		
		try {
			getNaverCafeMem();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView("jsonView", dataMap);
	}
	
	public static void getNaverCafeMem() {
		try {
				driver = new ChromeDriver();
				String  base_url = "https://nid.naver.com/nidlogin.login";
		        driver.get(base_url);
		        
		        //로그인 버튼 클릭
//				element = driver.findElement(By.className("btn_global"));
//				element.click();
				
				//아이디 입력
				element = driver.findElement(By.id("id"));
				Thread.sleep(500);
				element.sendKeys("csh238ml");
				Thread.sleep(900);
				
				//패스워드 입력
				element = driver.findElement(By.id("pw"));
				element.sendKeys("dhsfkdlsa2!23");
				Thread.sleep(900);
				
				String dynamicKey = driver.findElement(By.xpath("//*[@id='dynamicKey']")).getText().toString();
				System.out.println("dynamicKey==================" + dynamicKey);
				element.sendKeys(dynamicKey);
				
				//전송
				element = driver.findElement(By.className("btn_global"));
				Thread.sleep(86);
				element.submit();
				
				Thread.sleep(10000);
				

		        System.out.println(driver.getPageSource());
	        
//		        Thread.sleep(1000);
				
//				Thread.sleep(162);
//				driver.get("https://cafe.naver.com/ManageWholeMember.nhn?clubid=29822341");
//				
//				Thread.sleep(9654);
//				element = driver.findElement(By.id("id"));
//				Thread.sleep(500);
//				element.sendKeys("almtyc");
//				
//				Thread.sleep(9654);
//				element = driver.findElement(By.id("pw"));
//				Thread.sleep(9654);
//				element.sendKeys("wjdwotlr8715");
//				
//				element = driver.findElement(By.className("btn_global"));
//				Thread.sleep(54);
//				element.submit();
//				
//				driver = new ChromeDriver();
//				driver.get("https://cafe.naver.com/ManageWholeMember.nhn?clubid=29822341");
	            System.out.println(driver.getPageSource());
		        
	    
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            driver.close();
	        }
	}

}