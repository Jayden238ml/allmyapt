package apt.framework.common.datagokr;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.jasper.tagplugins.jstl.core.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ibatis.common.logging.Log;
import com.ibatis.common.logging.LogFactory;

import apt.framework.common.DataMap;
import apt.framework.common.control.LincActionController;
import apt.framework.core.CommonFacade;
import apt.framework.util.PUtil;



@Controller
public class DataGoKrController extends LincActionController {
	
	static final String INJE_INDCT_ID = "0000164";
	static final String DGK_VERSION = "31";
	
	Log log = LogFactory.getLog(this.getClass());
	
	// #############################################################################################################
	// # 공통
	// #############################################################################################################
	// 공통 모듈
	@Autowired
	@Qualifier("commonImpl")
	protected CommonFacade commonFacade;
	// ?��?��?��?��처리.
	@Autowired
	private PlatformTransactionManager transactionManager;
	
//	protected DataMap paramMap = null;
	
	@ModelAttribute("requestParam")
	public DataMap requestParam(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
		showParameters(arg0);
		DataMap paramMap = new PUtil().getParameterDataMap(arg0);
		paramMap.put("DGK_VERSION", DGK_VERSION);
		setSessionMenu(commonFacade, arg0,paramMap);
//		if("N".equals(paramMap.getString("RETOK"))){
//			arg1.sendRedirect("/main.do");
//		}
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
		    log.error("Error", e);
			e.printStackTrace();
		}
	}
	
	
	/**
	 * �?리자 > LINC?��?��?���? �?�? > ?��?��?�� ?��?���?�?
	 * @param dataMap
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * http://localhost:8080/admin/dgk/info.do?CURRENT_MENU_CODE=MENU1007&TOP_MENU_CODE=MENU1001
	 */
    @RequestMapping(value = "/admin/dgk/info.do")
    public ModelAndView info(@ModelAttribute("requestParam")DataMap dataMap, HttpServletRequest request, HttpServletResponse response
    		) throws Exception {
    	
    	String modelName = "/admin/dgk/info";
    	List<?> znCdList = null;
    	List<?> indctidList = null;
    	
    	
    	try {
    		dataMap.put("procedureid", "Dgk.getZnCdList");
        	znCdList = commonFacade.list(dataMap);
        	System.out.println(znCdList);
        	//[{cdnm=?��?��, cdid=11}, {cdnm=�??��, cdid=21}, {cdnm=??�?, cdid=22}, {cdnm=?���?, cdid=23}, {cdnm=광주, cdid=24}, {cdnm=???��, cdid=25}, {cdnm=?��?��, cdid=26}, {cdnm=경기, cdid=41}, {cdnm=강원, cdid=42}, {cdnm=충북, cdid=43}, {cdnm=충남, cdid=44}, {cdnm=?���?, cdid=45}, {cdnm=?��?��, cdid=46}, {cdnm=경북, cdid=47}, {cdnm=경남, cdid=48}, {cdnm=?���?, cdid=49}, {cdnm=?���?, cdid=50}]
        	dataMap.put("znCdList", znCdList);	
        	
        	dataMap.put("procedureid", "Dgk.getIndctidList");
        	indctidList = commonFacade.list(dataMap);
        	System.out.println(indctidList);
        	dataMap.put("indctidList", indctidList);
        	
		} catch (Exception e) {
			log.error("Error", e);
			e.printStackTrace();
		}
    	
        
    	return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
    
    
    /**
	 * �?리자 > LINC?��?��?���? �?�? > ?��?��?�� ?��?���?�?
	 * @param dataMap
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * http://localhost:8080/admin/dgk/info.do?CURRENT_MENU_CODE=MENU1007&TOP_MENU_CODE=MENU1001
	 */
    @RequestMapping(value = "/admin/dgk/search_schl.do")
    public ModelAndView schlSearch(@ModelAttribute("requestParam")DataMap dataMap, HttpServletRequest request, HttpServletResponse response
    		) throws Exception {
    	
    	List<?> schlidList = null;
    	
    	try {
    		dataMap.put("procedureid", "Dgk.getSchlidListByName");
    		schlidList = commonFacade.list(dataMap);
        	System.out.println(schlidList);
        	dataMap.put("schlidList", schlidList);	
        	
		} catch (Exception e) {
			log.error("Error", e);
			e.printStackTrace();
		}
    	
        
    	return new ModelAndView( "jsonView", dataMap);
	}
    
    //http://localhost:8080/admin/dgk/graph.do?schlids%5B%5D=0000450&schlids%5B%5D=0000019&schlids%5B%5D=0000125&schlids%5B%5D=0000126&indctid=3&svyYr=2016
    @RequestMapping(value = "/admin/dgk/graph.do")
    public ModelAndView graph(@ModelAttribute("requestParam")DataMap dataMap, 
    		@RequestParam(value="sType") String sType,
    		@RequestParam(value="schlids[]", required=false) String[] schlids,
    		@RequestParam(value="zns[]", required=false) String[] zns,
    		@RequestParam(value="indctid") String indctid,
    		@RequestParam(value="svyyr") String svyYr,
    		HttpServletRequest request, HttpServletResponse response
    		) throws Exception {
    	
    	String modelName = "/admin/dgk/graph";
    	
    	dataMap.put("svyYr", svyYr);
    	dataMap.put("indctid", indctid);
    	
    	String[] schlidsNew = null;
    	
    	if ("dx".equals(sType)){
    		schlidsNew = new String[schlids.length + 1];
    		for (int i = 0; i < schlids.length; i++) {
        		schlidsNew[i] = schlids[i];
    		}
        	schlidsNew[schlidsNew.length-1] = INJE_INDCT_ID;
        	dataMap.put("schlids", schlidsNew);
    	}else{
    		dataMap.put("zns", zns);
    		dataMap.put("procedureid", "Dgk.getSchlidListByZnCd");
    		List<HashMap> schlFromZns = commonFacade.list(dataMap);
    		HashSet<String> shlidSet = new HashSet<String>();
    		for (Iterator iterator = schlFromZns.iterator(); iterator.hasNext();) {
    			HashMap map = (HashMap) iterator.next();
    			String schlid = (String) map.get("schlid");
    			shlidSet.add(schlid);
			}
    		String[] shlidList = (String[]) shlidSet.toArray(new String[shlidSet.size()]);
    		
    		schlidsNew = new String[shlidList.length + 1];
    		for (int i = 0; i < shlidList.length; i++) {
        		schlidsNew[i] = shlidList[i];
    		}
        	schlidsNew[schlidsNew.length-1] = INJE_INDCT_ID;
        	dataMap.put("schlids", schlidsNew);
    	}
    	
    	
    	List<?> dataSheet = null;
    	
    	try {
    		dataMap.put("procedureid", "Dgk.getDataSheetForGraph");
    		dataSheet = commonFacade.list(dataMap);
        	System.out.println(dataSheet);
        	dataMap.put("dataSheet", dataSheet);	
        	
        	dataMap.put("procedureid", "Dgk.getIndctNm");
        	DataMap indctNm = commonFacade.getObject(dataMap);
        	dataMap.put("indctNm", indctNm.getString("cdnm"));
        	
        	
		} catch (Exception e) {
			log.error("Error", e);
			e.printStackTrace();
		}
    	
        
    	return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
    
    
    
}
