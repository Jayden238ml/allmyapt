package apt.admin.control;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLEncoder;
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
import apt.framework.util.PUtil;

import org.apache.commons.lang.StringUtils;
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
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.ibatis.common.logging.Log;
import com.ibatis.common.logging.LogFactory;

@Controller
public class SmsSaController extends LincActionController {
	protected CommonFacade commonFacade;
	private PlatformTransactionManager transactionManager;
	Log log = LogFactory.getLog(this.getClass());

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

		if ("".equals(paramMap.getString("SESSION_USER_ID")) || !"SMC".equals(paramMap.getString("SESSION_USER_TYPE"))) {
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

	/**
	 * 관리자 > 메인
	 * 
	 * @param dataMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/Sms/Main.do")
	public ModelAndView amcMain(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request,HttpServletResponse response
			  ,@RequestParam(value="PAGE_SIZE", required=false, defaultValue="10")String view_size
			  ,@RequestParam(value="CURR_PAGE", required=false, defaultValue="1")String page){

		String modelName = "";

		try {
			
			if("".equals(dataMap.getString("S_LMC"))) {
				dataMap.put("S_LMC", "S_LMC001");
			}
			
			dataMap.put("CURR_PAGE",page);
			dataMap.put("PAGE_SIZE",view_size);
			
			dataMap.put("procedureid", "System.getAptuser_CNT");
		    DataMap cntMap = this.commonFacade.getObject(dataMap);
		    dataMap.put("TOTAL_CNT", cntMap.getString("TOTAL_CNT"));
			
			dataMap.put("procedureid", "System.getAptuser_List");
			List resultList = commonFacade.list(dataMap);
			dataMap.put("resultList", resultList);
			
			modelName = "/admin/sms/smsMain";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	

}