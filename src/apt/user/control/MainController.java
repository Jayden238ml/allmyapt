package apt.user.control;

import apt.framework.common.DataMap;
import apt.framework.common.LogWriter;
import apt.framework.common.control.LincActionController;
import apt.framework.core.CommonFacade;
import apt.framework.util.PUtil;
import com.ibatis.common.logging.Log;
import com.ibatis.common.logging.LogFactory;
import java.io.BufferedInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

@Controller
public class MainController extends LincActionController
{
	protected CommonFacade commonFacade;
	private PlatformTransactionManager transactionManager;
	Log log = LogFactory.getLog(getClass());

//protected DataMap paramMap = null;

	@Autowired
	public void setTransactionManager(PlatformTransactionManager transactionManager)
	{
		this.transactionManager = transactionManager;
	}

	@Autowired
	@Qualifier("commonImpl")
	public void setCommonImpl(CommonFacade commonFacade) {
		this.commonFacade = commonFacade;
	}
	
	@ModelAttribute("requestParam")
	public DataMap requestParam(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
		showParameters(arg0);
		DataMap paramMap = new PUtil().getParameterDataMap(arg0);
		setSessionMenu(this.commonFacade, arg0, paramMap);
		return paramMap;
	}
	
	public void showParameters(HttpServletRequest request){
		this.log.debug("###############################################################");
		this.log.debug("REQUESTURL : " + request.getRequestURL());
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
	
	@RequestMapping({"/main.do"})
	public ModelAndView Main(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response)
	{
		String modelName = "/main/main";
	
		try
		{
			// 공지사항 가져오기
			dataMap.put("procedureid", "Board.getMain_List");
			List MainBdList = commonFacade.list(dataMap);	
			dataMap.put("MainBdList", MainBdList);
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			LogWriter.getLogger(getClass()).error(ex.toString());
		}
		
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	public String getRemortIP(HttpServletRequest request) {
		if (request.getHeader("x-forwarded-for") == null) {
		return request.getRemoteAddr();
		}
		return request.getHeader("x-forwarded-for");
	}
	
	@RequestMapping({"/sukjalogin.do"})
	public ModelAndView mngmtMiracom(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response)
	{
		String modelName = "/admin/login/index";
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	@RequestMapping({"/illumiLogin.do"})
	public ModelAndView illumiState(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response)
	{
		String modelName = "/illumi/login/index";
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
}