package apt.framework.common;

import apt.framework.core.CommonFacade;
import apt.framework.util.PUtil;
import com.ibatis.common.logging.Log;
import com.ibatis.common.logging.LogFactory;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

@Controller
public class ExcelDownload extends MultiActionController
{

	@Autowired
	private ExcelView ExcelView;
	private String ExcelTC = "100000000";
	protected CommonFacade commonFacade;
	Log log = LogFactory.getLog(getClass());
	
	
	@Autowired
	@Qualifier("commonImpl")
	public void setCommonImpl(CommonFacade commonFacade){
		this.commonFacade = commonFacade;
	}
	
	@ModelAttribute("requestParam")
	public DataMap requestParam(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
		showParameters(arg0);
		DataMap paramMap = new PUtil().getParameterDataMap(arg0);
		return paramMap;
	}
	
	public void showParameters(HttpServletRequest request){
		this.log.debug("###############################################################");
		this.log.debug("REQUESTT URL : " + request.getRequestURL());
		Enumeration paramNames = request.getParameterNames();
		try{
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
	
	public void setExcelView(ExcelView ExcelView){
		this.ExcelView = ExcelView;
	}
	
	@RequestMapping({"/apt/ExcelDown.do"})
	public ModelAndView ExcelDown(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List excelList = null;
		DataMap excel_Resource = new DataMap();
		
		String fileName = null;
		String sheetName = null;
		String tbName = null;
		
		String[] col_nm = null;
		String[] key_nm = null;
		try{
			dataMap.put("TOTAL_CNT", "0");
			dataMap.put("PAGE_SIZE", "1000000");
			dataMap.put("CURR_PAGE", "1");
			
			dataMap.put("procedureid", "Warrant.getMyAptInfo");
			DataMap detail = this.commonFacade.getObject(dataMap);
			
			String danzi_yn = detail.getString("DANZI_YN");
			String johap_yn = detail.getString("JOHAP_YN");
			String apt_nm = detail.getString("APT_NM");
			
			dataMap.put("procedureid", "Warrant.getMyWarrant_CNT");
			DataMap cntMap = this.commonFacade.getObject(dataMap);
			if(cntMap == null || "".equals(cntMap)){
				dataMap.put("TOTAL_CNT", "0");
			}else {
				dataMap.put("TOTAL_CNT", cntMap.getString("TOTAL_CNT"));
			}
			
			dataMap.put("procedureid", "Warrant.getMyWarrant_List");
			excelList = this.commonFacade.list(dataMap);
			
			fileName = apt_nm + " 입주자 목록";
			sheetName = apt_nm + " 입주자 목록";
			tbName = apt_nm + " 입주자 목록";
			
			if("Y".equals(danzi_yn) && "Y".equals(johap_yn)){
				col_nm = new String[] { "단지", "동", "호수", "성명", "공동명의자 명", "연락처", "공동명의자 연락처", "위임장 제출여부", "회비 제출 여부", "조합여부", "주소", "기타내용"};
				key_nm = new String[] { "DANZI", "DONG", "HOSU", "USER_NM", "USER_NM2", "HP", "HP2", "WARRANT_YN_NM", "AMT_YN_NM", "JOHAP_YN_NM", "ADDR", "ETC" };
			}else if("Y".equals(danzi_yn) && "N".equals(johap_yn)){
				col_nm = new String[] { "단지", "동", "호수", "성명", "공동명의자 명", "연락처", "공동명의자 연락처", "위임장 제출여부", "회비 제출 여부", "주소", "기타내용"};
				key_nm = new String[] { "DANZI", "DONG", "HOSU", "USER_NM", "USER_NM2", "HP", "HP2", "WARRANT_YN_NM", "AMT_YN_NM", "ADDR", "ETC" };
			}else if("N".equals(danzi_yn) && "Y".equals(johap_yn)) {
				col_nm = new String[] { "동", "호수", "성명", "공동명의자 명", "연락처", "공동명의자 연락처", "위임장 제출여부", "회비 제출 여부", "조합여부", "주소", "기타내용"};
				key_nm = new String[] { "DONG", "HOSU", "USER_NM", "USER_NM2", "HP", "HP2", "WARRANT_YN_NM", "AMT_YN_NM", "JOHAP_YN_NM", "ADDR", "ETC" };
			}else {
				col_nm = new String[] { "동", "호수", "성명", "공동명의자 명", "연락처", "공동명의자 연락처", "위임장 제출여부", "회비 제출 여부", "주소", "기타내용"};
				key_nm = new String[] { "DONG", "HOSU", "USER_NM", "USER_NM2", "HP", "HP2", "WARRANT_YN_NM", "AMT_YN_NM", "ADDR", "ETC" };
			}
			
			excel_Resource.put("fileName", fileName);
			excel_Resource.put("sheetName", sheetName);
			excel_Resource.put("tbName", tbName);
			
			excel_Resource.put("excelList", excelList);
			excel_Resource.put("key_nm", key_nm);
			excel_Resource.put("col_nm", col_nm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ModelAndView mav = new ModelAndView(this.ExcelView);
		mav.addObject("excel_Resource", excel_Resource);
		
		return mav;
	}
	
	
	@RequestMapping({"/apt/LmsExcelDown.do"})
	public ModelAndView LmsExcelDown(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List excelList = null;
		List emPamList = null;
		String modelName = "";
		DataMap excel_Resource = new DataMap();
		try{
			dataMap.put("procedureid", "Warrant.LmsExcelDown");
			excelList = this.commonFacade.list(dataMap);
			dataMap.put("excelList", excelList);
			
			modelName = "/ourapt/lmsList_XLS";
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	@RequestMapping({"/apt/AmtExcelDown.do"})
	public ModelAndView AmtExcelDown(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List excelList = null;
		DataMap excel_Resource = new DataMap();
		
		String fileName = null;
		String sheetName = null;
		String tbName = null;
		
		String[] col_nm = null;
		String[] key_nm = null;
		try{
			dataMap.put("TOTAL_CNT", "0");
			dataMap.put("PAGE_SIZE", "1000000");
			dataMap.put("CURR_PAGE", "1");
			
			dataMap.put("procedureid", "Warrant.getMyAptInfo");
			DataMap detail = this.commonFacade.getObject(dataMap);
			
			String danzi_yn = detail.getString("DANZI_YN");
			String johap_yn = detail.getString("JOHAP_YN");
			String apt_nm = detail.getString("APT_NM");
			
			dataMap.put("procedureid", "Warrant.getMyamt_CNT");
			DataMap cntMap = this.commonFacade.getObject(dataMap);
			if(cntMap == null || "".equals(cntMap)){
				dataMap.put("TOTAL_CNT", "0");
			}else {
				dataMap.put("TOTAL_CNT", cntMap.getString("TOTAL_CNT"));
			}
			
			dataMap.put("procedureid", "Warrant.getMyamt_List");
			excelList = commonFacade.list(dataMap);
			
			fileName = apt_nm + " 회비내역 목록";
			sheetName = apt_nm + " 회비내역 목록";
			tbName = apt_nm + " 회비내역 목록";
			
			col_nm = new String[] { "동", "호수", "입금자명", "입금일자", "입금액"};
			key_nm = new String[] { "DONG", "HOSU", "USER_NM", "AMT_IN_DATE", "AMT"};
			
			excel_Resource.put("fileName", fileName);
			excel_Resource.put("sheetName", sheetName);
			excel_Resource.put("tbName", tbName);
			
			excel_Resource.put("excelList", excelList);
			excel_Resource.put("key_nm", key_nm);
			excel_Resource.put("col_nm", col_nm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ModelAndView mav = new ModelAndView(this.ExcelView);
		mav.addObject("excel_Resource", excel_Resource);
		
		return mav;
	}
	
	
	@RequestMapping({"/apt/SmsHistExcelDown.do"})
	public ModelAndView SmsHistExcelDown(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List excelList = null;
		DataMap excel_Resource = new DataMap();
		
		String fileName = null;
		String sheetName = null;
		String tbName = null;
		
		String[] col_nm = null;
		String[] key_nm = null;
		try{
			dataMap.put("TOTAL_CNT", "0");
			dataMap.put("PAGE_SIZE", "1000000");
			dataMap.put("CURR_PAGE", "1");
			
			dataMap.put("procedureid", "Warrant.getSmsSendDtl_CNT");
			DataMap cntMap = this.commonFacade.getObject(dataMap);
			if(cntMap == null || "".equals(cntMap)){
				dataMap.put("TOTAL_CNT", "0");
			}else {
				dataMap.put("TOTAL_CNT", cntMap.getString("TOTAL_CNT"));
			}
			
			dataMap.put("procedureid", "Warrant.getSmsSendDtl_List");
			excelList = commonFacade.list(dataMap);
			
			fileName = "입금_발송이력";
			sheetName = "입금_발송이력";
			tbName = "입금_발송이력";
			
			col_nm = new String[] { "구분", "금액", "일자", "입금/발송건수"};
			key_nm = new String[] { "TIT_GUBUN", "AMT", "REGDATE", "SEND_CNT"};
			
			excel_Resource.put("fileName", fileName);
			excel_Resource.put("sheetName", sheetName);
			excel_Resource.put("tbName", tbName);
			
			excel_Resource.put("excelList", excelList);
			excel_Resource.put("key_nm", key_nm);
			excel_Resource.put("col_nm", col_nm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ModelAndView mav = new ModelAndView(this.ExcelView);
		mav.addObject("excel_Resource", excel_Resource);
		
		return mav;
	}
}