package apt.framework.common.datagokr;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

class DgkService {
	String url;
	String[] cols;
	
	String[] params;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String[] getCols() {
		return cols;
	}
	public void setCols(String[] cols) {
		this.cols = cols;
	}
	public String[] getParams() {
		return params;
	}
	public void setParams(String[] params) {
		this.params = params;
	}
	
	public List<String> getParamList(){
		if (params != null) {
			return Arrays.asList(params);  
		}
		return null;
	}
	
	public List<String> getColList(){
		if (cols != null) {
			return Arrays.asList(cols);  
		}
		return null;
	}
	
	public boolean containsParam(String p){
		List<String> pramList = getParamList();
		if (pramList == null){
			return false;
		}else{
			return pramList.contains(p);
		}
	}
}

public class DataGoKr {
	String serviceKey;
	Map<String, DgkService> codeService;
	Map<String, DgkService> univCompetitiveNoticeService;
	Map<String, DgkService> universityComparisonStatisticsService;
	Map<String, DgkService> regionalStatisticsService;
	public String getServiceKey() {
		return serviceKey;
	}
	public void setServiceKey(String serviceKey) {
		this.serviceKey = serviceKey;
	}
	public Map<String, DgkService> getCodeService() {
		return codeService;
	}
	public void setCodeService(Map<String, DgkService> codeService) {
		this.codeService = codeService;
	}
	public Map<String, DgkService> getUnivCompetitiveNoticeService() {
		return univCompetitiveNoticeService;
	}
	public void setUnivCompetitiveNoticeService(Map<String, DgkService> univCompetitiveNoticeService) {
		this.univCompetitiveNoticeService = univCompetitiveNoticeService;
	}
	public Map<String, DgkService> getUniversityComparisonStatisticsService() {
		return universityComparisonStatisticsService;
	}
	public void setUniversityComparisonStatisticsService(Map<String, DgkService> universityComparisonStatisticsService) {
		this.universityComparisonStatisticsService = universityComparisonStatisticsService;
	}
	public Map<String, DgkService> getRegionalStatisticsService() {
		return regionalStatisticsService;
	}
	public void setRegionalStatisticsService(Map<String, DgkService> regionalStatisticsService) {
		this.regionalStatisticsService = regionalStatisticsService;
	}
}