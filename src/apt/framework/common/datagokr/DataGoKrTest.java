package apt.framework.common.datagokr;
import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
@Controller
public class DataGoKrTest {
	
	@Autowired 
	private DataGoKrRunner dataGoKrRunner;
	
	/*
	 * DataGoKrRunner ?�� run method �? ?��?��?�� ?��?�� ?��?�� ?�� ?��?�� ?�� �?
	 * */
	@RequestMapping(value = "/test1/test1.do")
    public ModelAndView dgkRun(HttpServletRequest request, HttpServletResponse response)  {    	
		
		try {
			dataGoKrRunner.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@Test
	public void test1(){
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
        	ClassPathResource classPathResource = new ClassPathResource("apt/framework/common/datagokr/DataGoKr.yml");
    		File file = classPathResource.getFile();
        	DataGoKr conf = mapper.readValue(file, DataGoKr.class);
        	System.out.println(conf);
        	
        	String serviceKey = conf.getServiceKey();
        	System.out.println(serviceKey);
        	
        	Map<String, DgkService> codeService = conf.getCodeService();
        	Map<String, DgkService> univCompetitiveNoticeService = conf.getUnivCompetitiveNoticeService();
        	Map<String, DgkService> universityComparisonStatisticsService = conf.getUniversityComparisonStatisticsService();
        	Map<String, DgkService> regionalStatisticsService = conf.getRegionalStatisticsService();
        	
        	for (String key : codeService.keySet()){
        		System.out.println(key);
        		DgkService dgkService = codeService.get(key);
        		String[] cols = dgkService.getCols();
        		String[] params = dgkService.getParams();
        		String url = dgkService.getUrl();
        		if (cols != null){
        			for(String c : cols){
            			System.out.println(c);
            		}
        		}
        		
        		System.out.println(params);
        		if (params != null){
        			for(String p : params){
            			System.out.println(p);
            		}
        		}
        		System.out.println(url);
        	}
        	
        	for (String key : univCompetitiveNoticeService.keySet()){
        		System.out.println(key);
        		DgkService dgkService = univCompetitiveNoticeService.get(key);
        		String[] cols = dgkService.getCols();
        		String[] params = dgkService.getParams();
        		String url = dgkService.getUrl();
        		if (cols != null){
        			for(String c : cols){
            			System.out.println(c);
            		}
        		}
        		
        		System.out.println(params);
        		if (params != null){
        			for(String p : params){
            			System.out.println(p);
            		}
        		}
        		
        		System.out.println(url);
        	}
        	
        	for (String key : universityComparisonStatisticsService.keySet()){
        		System.out.println(key);
        		DgkService dgkService = universityComparisonStatisticsService.get(key);
        		String[] cols = dgkService.getCols();
        		String[] params = dgkService.getParams();
        		String url = dgkService.getUrl();
        		if (cols != null){
        			for(String c : cols){
            			System.out.println(c);
            		}
        		}
        		
        		System.out.println(params);
        		if (params != null){
        			for(String p : params){
            			System.out.println(p);
            		}
        		}
        		System.out.println(url);
        	}
        	
        	for (String key : regionalStatisticsService.keySet()){
        		System.out.println(key);
        		DgkService dgkService = regionalStatisticsService.get(key);
        		String[] cols = dgkService.getCols();
        		String[] params = dgkService.getParams();
        		String url = dgkService.getUrl();
        		if (cols != null){
        			for(String c : cols){
            			System.out.println(c);
            		}
        		}
        		
        		System.out.println(params);
        		if (params != null){
        			for(String p : params){
            			System.out.println(p);
            		}
        		}
        		System.out.println(url);
        	}
        	
        	
        } catch (Exception e) {
            e.printStackTrace();
        }
				
	}
	
}
