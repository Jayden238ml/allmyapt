package apt.framework.common.datagokr;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

//@Component
@Controller
public class DataGoKrRunner {
	
	@Autowired
	private DataGoKrService dataGoKrService;
	
	private int newVersion;
//    private final String svyYrs[] = new String[]{"2014","2015","2016"};
    private final String svyYrs[] = new String[]{"2014"};
    
    private final String serviceKey;
    private final Map<String, DgkService> codeService;
    private final Map<String, DgkService> univCompetitiveNoticeService;
    private final Map<String, DgkService> universityComparisonStatisticsService;
    private final Map<String, DgkService> regionalStatisticsService;
    
    
	public DataGoKrRunner() throws IOException {
        
		
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    	ClassPathResource classPathResource = new ClassPathResource("apt/framework/common/datagokr/DataGoKr.yml");
		File file = classPathResource.getFile();
    	DataGoKr conf = mapper.readValue(file, DataGoKr.class);
    	System.out.println(conf);
    	
    	serviceKey = conf.getServiceKey();
    	System.out.println(serviceKey);
    	
    	codeService = conf.getCodeService();
    	univCompetitiveNoticeService = conf.getUnivCompetitiveNoticeService();
    	universityComparisonStatisticsService = conf.getUniversityComparisonStatisticsService();
    	regionalStatisticsService = conf.getRegionalStatisticsService();
    }
	

	public void run() throws Exception {
	// ?��?�� ?��보는  private final String svyYrs[] �? ?��?��?�� �?
		
    // 버전 ?���? ?��?��    
//        newVersion = getNewDgkVersion(); // ?��?�� 버전 보다 1 추�?
//		newVersion = 12;	// 버전?�� 강제�? ?��?��
//		dataGoKrService.setNewVersion(newVersion);
		
        
        // ?��?�� 목록�? ?��?��?�� ?��비스 모두 주석?�� ?��?��?��?�� ?��?��
        // ?��?��간의 걸친 ?��?�� ?��간이 ?��?��?���?�? ?��?��?�� ?��?�� ?�� 것을 권장
		// 코드 ?��비스?�� 반드?�� ?��?��?��?��?�� 
//    	boolean result = procCodeService();  // 1.코드 ?��비스
//    	if (result){
//          getUnivCompetitiveNoticeService();  // 2.경쟁?�� ?���? ?��비스
//          getUniversityComparisonStatisticsService();  // 3.???�� 비교 ?���? ?��비스
//          getRegionalStatisticsService(); // 4.�??���? ?���? ?��비스
//    	}
		
		
       
    }
	
	
	/**
	 * data.go.kr ???��공시?���? ?���? 갱신 
	 * @return null
	 * @throws Exception
	 */
	@RequestMapping(value = "/dgk/all.do")
    public ModelAndView runAll(HttpServletRequest request, HttpServletResponse response) throws Exception  {
		
		String v = request.getParameter("v");
		if (v != null && !"".equals(v)){
			try {
				Integer pVersion = Integer.valueOf(v);
				newVersion = pVersion;
			} catch (Exception e) {
				int dgkNewVersion = getNewDgkVersion();
		        newVersion = dgkNewVersion;
			}
		}else{
			int dgkNewVersion = getNewDgkVersion();
	        newVersion = dgkNewVersion;
		}
		
		
        try {
        	boolean result = procCodeService();
        	if (result){
              getUnivCompetitiveNoticeService();
              getUniversityComparisonStatisticsService();
              getRegionalStatisticsService();
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
        
		return null;
	}
	
	/**
	 * data.go.kr ???�� 공시?���? 코드
	 * @return null
	 * @throws Exception
	 */
	@RequestMapping(value = "/dgk/procCodeService.do")
    public ModelAndView runProcCodeService(HttpServletRequest request, HttpServletResponse response) throws Exception  {
		
        
		String v = request.getParameter("v");
		if (v != null && !"".equals(v)){
			try {
				Integer pVersion = Integer.valueOf(v);
				newVersion = pVersion;
			} catch (Exception e) {
				int dgkNewVersion = getNewDgkVersion();
		        newVersion = dgkNewVersion;
			}
		}else{
			int dgkNewVersion = getNewDgkVersion();
	        newVersion = dgkNewVersion;
		}
		
		
        try {
        	procCodeService();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * data.go.kr ???��공시?���? 경쟁?��?���? 
	 * @param v - version (버전 직접 ?��?��?�� ?��?�� 버전?��?��)
	 * @return null
	 * @throws Exception
	 */
	@RequestMapping(value = "/dgk/univCompetitiveNoticeService.do")
    public ModelAndView runGetUnivCompetitiveNoticeService(HttpServletRequest request, HttpServletResponse response) throws Exception  {    	
        
		String v = request.getParameter("v");
		if (v != null && !"".equals(v)){
			try {
				Integer pVersion = Integer.valueOf(v);
				newVersion = pVersion;
			} catch (Exception e) {
				int dgkNewVersion = getNewDgkVersion();
		        newVersion = dgkNewVersion;
			}
		}else{
			int dgkNewVersion = getNewDgkVersion();
	        newVersion = dgkNewVersion;
		}
		

        try {
        	getUnivCompetitiveNoticeService();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
		return null;
	}
	
	/** 
	 * data.go.kr ???��공시?���? ???��비교?���? ?��비스  
	 * @param v - version (버전 직접 ?��?��?�� ?��?�� 버전?��?��)
	 * @return null
	 * @throws Exception
	 */
	@RequestMapping(value = "/dgk/universityComparisonStatisticsService.do")
    public ModelAndView runGetUniversityComparisonStatisticsService(HttpServletRequest request, HttpServletResponse response) throws Exception  {    	
        
		String v = request.getParameter("v");
		if (v != null && !"".equals(v)){
			try {
				Integer pVersion = Integer.valueOf(v);
				newVersion = pVersion;
			} catch (Exception e) {
				int dgkNewVersion = getNewDgkVersion();
		        newVersion = dgkNewVersion;
			}
		}else{
			int dgkNewVersion = getNewDgkVersion();
	        newVersion = dgkNewVersion;
		}
		
		
        try {
        	getUniversityComparisonStatisticsService();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** 
	 * data.go.kr ???��공시?���? ???��비교?���? ?��비스  
	 * @param v - version (버전 직접 ?��?��?�� ?��?�� 버전?��?��)
	 * @return null
	 * @throws Exception
	 */
	@RequestMapping(value = "/dgk/regionalStatisticsService.do")
    public ModelAndView runGetRegionalStatisticsService(HttpServletRequest request, HttpServletResponse response) throws Exception  {    	
		String v = request.getParameter("v");
		if (v != null && !"".equals(v)){
			try {
				Integer pVersion = Integer.valueOf(v);
				newVersion = pVersion;
			} catch (Exception e) {
				int dgkNewVersion = getNewDgkVersion();
		        newVersion = dgkNewVersion;
			}
		}else{
			int dgkNewVersion = getNewDgkVersion();
	        newVersion = dgkNewVersion;
		}
		
        try {
        	getRegionalStatisticsService();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

	
	private Integer getNewDgkVersion() throws Exception{
		int dgkNewVersion = dataGoKrService.getDgkNewVersion();
		return dgkNewVersion;
	}
	
	private boolean procCodeService() throws Exception{
		
		System.out.println("serviceKey : "+ serviceKey);
    	Map<String, String> vars = new HashMap<String, String>();
    	vars.put("serviceKey", serviceKey);
    	vars.put("pageNo", "1");
    	
    	ArrayList<Future<String>> codeServiceAyncs = new ArrayList<Future<String>>();
	    for (String apiName : codeService.keySet()) {
	    	DgkService dgkService = codeService.get(apiName);	    	
			String url = dgkService.getUrl();
			//create Table
			String tableName = "DGK_" + apiName;
			tableName = tableName.toUpperCase();
			List<String> cols = dgkService.getColList();
			this.createTable(tableName, cols);
			List<String> params = dgkService.getParamList();
			
			//request api
			if (params != null){
				System.out.println(params);
				if (params.contains("svyYr")){
					for (int i = 0; i < svyYrs.length; i++) {
						vars.put("svyYr", svyYrs[i]);
						vars.put("pageNo", "1");
						Future<String> apiResponse = dataGoKrService.getApiResponse(tableName, cols, url, vars);
						codeServiceAyncs.add(apiResponse);
					}
				}
				
			}else{
				vars.put("pageNo", "1"); // ?��?���? 번호�? 1번으�? ?��?�� ?��?��
				Future<String> apiResponse = dataGoKrService.getApiResponse(tableName, cols, url, vars);
				codeServiceAyncs.add(apiResponse);
			}
			
				
		}
	    
	    while (true) {
	    	if (codeServiceAyncs.size() == 0){
	    		break;
	    	}
	    	for (Iterator<Future<String>> iterator = codeServiceAyncs.iterator(); iterator.hasNext(); ) {
	    		Future<String> future = (Future<String>) iterator.next();
	    		if (future.isDone()){
	    			iterator.remove();
	    		}else{
	    			break;
	    		}
			}
	    	Thread.sleep(10);
		}
	    System.out.println("ALL DONE");
	    return true;
	}
	
	
	
	private boolean getUnivCompetitiveNoticeService() throws Exception{
		
		
    	Map<String, String> vars = new HashMap<String, String>();
    	vars.put("serviceKey", serviceKey);
    	vars.put("pageNo", "1");

    	ArrayList<Future<String>> codeServiceAyncs = new ArrayList<Future<String>>();
	    for (String apiName : univCompetitiveNoticeService.keySet()) {
	    	DgkService dgkService = univCompetitiveNoticeService.get(apiName);
	    	String url = dgkService.getUrl();
			
			//create Table
			String tableName = "DGK_" + apiName;
			tableName = tableName.toUpperCase();
			
			List<String> cols = dgkService.getColList();
			this.createTable(tableName, cols);
			
			List<String> params = dgkService.getParamList();
			//request api
			if (params != null){
				if (params.contains("svyYr") && params.contains("schlId")){
					for (String svyYr : this.svyYrs) {
						vars.put("svyYr", svyYr);
						List<String> schlIds = dataGoKrService.getSchlIds(svyYr, newVersion);
						for (int i = 0; i < schlIds.size(); i++) {
							vars.put("schlId", schlIds.get(i));
							vars.put("pageNo", "1");
							Future<String> apiResponse = dataGoKrService.getApiResponse(tableName,cols,  url, vars);
							codeServiceAyncs.add(apiResponse);
						}
					}
				}
			}else{
				vars.put("pageNo", "1");
				Future<String> apiResponse = dataGoKrService.getApiResponse(tableName,cols,  url, vars);
			}
			
				
		}
	    
	    while (true) {
	    	if (codeServiceAyncs.size() == 0){
	    		break;
	    	}
	    	for (Iterator<Future<String>> iterator = codeServiceAyncs.iterator(); iterator.hasNext(); ) {
	    		Future<String> future = (Future<String>) iterator.next();
	    		if (future.isDone()){
	    			iterator.remove();
	    		}else{
	    			break;
	    		}
			}
	    	Thread.sleep(10L);
		}
	    System.out.println("ALL DONE");
	    return true;
	}
	
	
	private boolean getUniversityComparisonStatisticsService() throws Exception{
		
    	Map<String, String> vars = new HashMap<String, String>();
    	vars.put("serviceKey", serviceKey);
    	vars.put("pageNo", "1");
    	
    	ArrayList<Future<String>> codeServiceAyncs = new ArrayList<Future<String>>();
	    for (String apiName : universityComparisonStatisticsService.keySet()) {
	    	DgkService dgkService = universityComparisonStatisticsService.get(apiName);
			String url = dgkService.getUrl();
			
			//create Table
			String tableName = "DGK_" + apiName;
			tableName = tableName.toUpperCase();
			
			List<String> cols = dgkService.getColList();
			this.createTable(tableName, cols);
			
			List<String> params = dgkService.getParamList();
			//request api
			if (params != null){
				System.out.println("parameters :");
				System.out.println(params);
				if (params.contains("svyYr") && params.contains("schlId") && params.contains("indctId")){
					System.out.print("parameters contains : svyYr , schlId, indctId");
					
					for (String svyYr : this.svyYrs) {
						 List<String> schlIds = dataGoKrService.getSchlIds(svyYr, newVersion);
						 List<String> indctIds = dataGoKrService.getIndctId(svyYr, newVersion);
						 vars.put("svyYr", svyYr);
						 System.out.println("put svyYr :" + svyYr);
						 for (String schlId : schlIds) {
							 vars.put("schlId", schlId);
							 System.out.print("put schlId :" + schlId);
							 for (int i = 0; i < indctIds.size(); i++) {
								 vars.put("pageNo", "1");
								 vars.put("indctId", indctIds.get(i));
								 Future<String> apiResponse = dataGoKrService.getApiResponse(tableName , cols, url, vars);
								 codeServiceAyncs.add(apiResponse);
							}
						}
					}
				}
				else if (params.contains("svyYr") && params.contains("schlId")){
					System.out.print("parameters contains : svyYr , schlId");
					for (String svyYr : this.svyYrs) {
						 List<String> schlIds = dataGoKrService.getSchlIds(svyYr, newVersion);
						 vars.put("svyYr", svyYr);
						 System.out.print("put svyYr :" + svyYr);
						 for (int i = 0; i < schlIds.size(); i++) {
							 vars.put("schlId", schlIds.get(i));
							 System.out.print("put schlId :" + schlIds.get(i));
							 vars.put("pageNo", "1");
							 Future<String> apiResponse = dataGoKrService.getApiResponse(tableName , cols, url, vars);
							 codeServiceAyncs.add(apiResponse);
						 }
					}
				}
				else if (params.contains("svyYr")){
					System.out.print("parameters contains : svyYr");
					for (String svyYr : this.svyYrs) {
						System.out.print("put svyYr :" + svyYr);
						vars.put("svyYr", svyYr);
						vars.put("pageNo", "1");
						Future<String> apiResponse = dataGoKrService.getApiResponse(tableName , cols, url, vars);
						codeServiceAyncs.add(apiResponse);
					}
				}
				
			}else{
				System.out.print("parameters contains nothing!!");
				vars.put("pageNo", "1");
				Future<String> apiResponse = dataGoKrService.getApiResponse(tableName , cols, url, vars);
				codeServiceAyncs.add(apiResponse);
			}
			
				
		}
	    
	    while (true) {
	    	if (codeServiceAyncs.size() == 0){
	    		break;
	    	}
	    	for (Iterator<Future<String>> iterator = codeServiceAyncs.iterator(); iterator.hasNext(); ) {
	    		Future<String> future = (Future<String>) iterator.next();
	    		if (future.isDone()){
	    			iterator.remove();
	    		}else{
	    			break;
	    		}
			}
	    	Thread.sleep(10L);
		}
	    System.out.println("ALL DONE");
	    return true;
	}
	
	
	private boolean getRegionalStatisticsService() throws Exception{
		
    	Map<String, String> vars = new HashMap<String, String>();
    	vars.put("serviceKey", serviceKey);
    	vars.put("pageNo", "1");

    	
	    
    	ArrayList<Future<String>> codeServiceAyncs = new ArrayList<Future<String>>();
	    for (String apiName : regionalStatisticsService.keySet()) {
	    	DgkService dgkService = regionalStatisticsService.get(apiName);
			String url = dgkService.getUrl();
			
			//create Table
			String tableName = "DGK_" + apiName;
			tableName = tableName.toUpperCase();
			
			List<String> cols = dgkService.getColList();
			this.createTable(tableName, cols);
			
			List<String> params = dgkService.getParamList();
			
			List<String> schlDivCds = dataGoKrService.getSchlDivCd(newVersion);
			List<String> indctIds = dataGoKrService.getIndctId(newVersion);
			//request api
			if (params != null){
				if (params.contains("schlDivCd") && params.contains("indctId")){
					 for (String schlDivCd : schlDivCds) {
						 vars.put("schlDivCd", schlDivCd);
						 for (String indctId : indctIds) {
							 vars.put("pageNo", "1");
							 vars.put("indctId", indctId);
							 Future<String> apiResponse = dataGoKrService.getApiResponse(tableName , cols, url, vars);
							 codeServiceAyncs.add(apiResponse);
						}
					}
				}
				else if (params.contains("schlDivCd")){
					 for (String schlDivCd : schlDivCds) {
						 vars.put("schlDivCd", schlDivCd);
						 vars.put("pageNo", "1");
						 Future<String> apiResponse = dataGoKrService.getApiResponse(tableName , cols, url, vars);
						 codeServiceAyncs.add(apiResponse);
					}
				}
				
			}else{
				vars.put("pageNo", "1");
				Future<String> apiResponse = dataGoKrService.getApiResponse(tableName , cols, url, vars);
				codeServiceAyncs.add(apiResponse);
			}
			
				
		}
	    
	    while (true) {
	    	if (codeServiceAyncs.size() == 0){
	    		break;
	    	}
	    	for (Iterator<Future<String>> iterator = codeServiceAyncs.iterator(); iterator.hasNext(); ) {
	    		Future<String> future = (Future<String>) iterator.next();
	    		if (future.isDone()){
	    			iterator.remove();
	    		}else{
	    			break;
	    		}
			}
	    	Thread.sleep(10L);
		}
	    System.out.println("ALL DONE");
	    return true;
	}
	

	private void createTable(String tableName, List<String> cols) {
		
		boolean ifExistTable = dataGoKrService.checkTableExists(tableName);
		if (ifExistTable){
			System.out.println(tableName + " IS EXISTS");
		}else{
			System.out.println(tableName + " IS NOT EXISTS");
			dataGoKrService.createTable(cols, tableName);
		}
	}
	
	
}
