package apt.user.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
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

import apt.framework.common.DataMap;
import apt.framework.common.control.LincActionController;
import apt.framework.core.CommonFacade;
import apt.framework.util.MessageUtil;
import apt.framework.util.PUtil;
import apt.framework.util.Utils;

@Controller
public class AptController extends LincActionController{
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
	public void setCommonImpl(CommonFacade commonFacade) { this.commonFacade = commonFacade; }
	
	@ModelAttribute("requestParam")
	public DataMap requestParam(HttpServletRequest arg0, HttpServletResponse arg1)
	  throws Exception{
		  showParameters(arg0);
		  DataMap paramMap = new PUtil().getParameterDataMap(arg0);
		  setSessionMenu(this.commonFacade, arg0, paramMap);
		  if("N".equals(paramMap.getString("RETOK"))){
			  arg1.sendRedirect("/main.do");
		  }
	
		  return paramMap;
	}
	
	public void showParameters(HttpServletRequest request)
	{
		this.log.debug("###############################################################");
		this.log.debug("REQUEST  URL : " + request.getRequestURL());
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
	
	
	@RequestMapping({"/apt/apt_warrant.do"})
	public ModelAndView apt_warrant(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response
		,@RequestParam(value="PAGE_SIZE", required=false, defaultValue="10")String view_size
		,@RequestParam(value="CURR_PAGE", required=false, defaultValue="1")String page){
		String modelName = "/ourapt/apt_warrant";
		try {
			dataMap.put("CURR_PAGE",page);
			dataMap.put("PAGE_SIZE",view_size);
			
			dataMap.put("procedureid", "Warrant.getWarrAvg");
			DataMap avgMap = this.commonFacade.getObject(dataMap);
			dataMap.put("TOTAL_AVG", avgMap.getString("TOTAL_AVG"));
			
			dataMap.put("procedureid", "Warrant.getMyAptInfo");
			DataMap detail = this.commonFacade.getObject(dataMap);
			dataMap.put("detail", detail);
			dataMap.put("DANZI_YN", detail.getString("DANZI_YN"));
			
			dataMap.put("procedureid", "Warrant.getMyWarrant_CNT");
			DataMap cntMap = this.commonFacade.getObject(dataMap);
			if(cntMap == null || "".equals(cntMap)){
				dataMap.put("TOTAL_CNT", "0");
			}else {
				dataMap.put("TOTAL_CNT", cntMap.getString("TOTAL_CNT"));
			}
			
			// 단지 있을경우 단지 조회
			if("Y".equals(dataMap.getString("DANZI_YN"))) {
				// 전체 동 조회
				dataMap.put("procedureid", "Warrant.getMyDanzi_List");
				List DanziList = commonFacade.list(dataMap);
				dataMap.put("DanziList", DanziList);
			}
			
			// 전체 동 조회
			dataMap.put("procedureid", "Warrant.getMyDong_List");
			List DongList = commonFacade.list(dataMap);
			dataMap.put("DongList", DongList);
			
			// 전체 호수 조회
			dataMap.put("procedureid", "Warrant.getMyHosu_List");
			List HosuList = commonFacade.list(dataMap);
			dataMap.put("HosuList", HosuList);
			
			dataMap.put("procedureid", "Warrant.getMyWarrant_List");
			List resultList = commonFacade.list(dataMap);
			dataMap.put("resultList", resultList);
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	@RequestMapping({"/apt/getHosuList.do"}) 
	public ModelAndView getHosuList(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response){
		try {
			
			if("D".equals(dataMap.getString("SELECT_TYPE"))) {
				// 전체 동 조회
				dataMap.put("procedureid", "Warrant.getMyDong_List");
				List DongList = commonFacade.list(dataMap);
				dataMap.put("DongList", DongList);
			}else {
				// 전체 호수 조회
				dataMap.put("procedureid", "Warrant.getMyHosu_List");
				List HosuList = commonFacade.list(dataMap);
				dataMap.put("HosuList", HosuList);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		FlashMap fm = RequestContextUtils.getOutputFlashMap(request);
		fm.put("TMC", dataMap.getString("TMC"));
		fm.put("LMC", dataMap.getString("LMC"));
		return new ModelAndView("jsonView", dataMap);
	}
	
	
	@RequestMapping({"/apt/getAmtDetailHosuList.do"}) 
	public ModelAndView getAmtDetailHosuList(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response){
		try {
			
			if("D".equals(dataMap.getString("SELECT_TYPE"))) {
				// 전체 동 조회
				dataMap.put("procedureid", "Warrant.getMyDongNoSch_List");
				List DongList = commonFacade.list(dataMap);
				dataMap.put("DongList", DongList);
			}else {
				// 전체 호수 조회
				dataMap.put("procedureid", "Warrant.getMyHosuNoSch_List");
				List HosuList = commonFacade.list(dataMap);
				dataMap.put("HosuList", HosuList);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		FlashMap fm = RequestContextUtils.getOutputFlashMap(request);
		fm.put("TMC", dataMap.getString("TMC"));
		fm.put("LMC", dataMap.getString("LMC"));
		return new ModelAndView("jsonView", dataMap);
	}
	
	
	@RequestMapping({"/apt/apt_warrantDetail.do"})
	public ModelAndView apt_warrantDetail(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response){
		String modelName = "/ourapt/apt_warrantDetail";
		try {
			
			dataMap.put("procedureid", "Warrant.getMyAptInfo");
			DataMap aptMap = this.commonFacade.getObject(dataMap);
			dataMap.put("aptMap", aptMap);
			dataMap.put("DANZI_YN", aptMap.getString("DANZI_YN"));
			dataMap.put("JOHAP_YN", aptMap.getString("JOHAP_YN"));
			
			dataMap.put("procedureid", "Warrant.getMyAptDetailInfo");
			DataMap detail = this.commonFacade.getObject(dataMap);
			dataMap.put("detail", detail);
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	/**
	 * 
	 */
	@RequestMapping(value = "/apt/apt_WarrantInfo_Insert.do")
	public ModelAndView apt_WarrantInfo_Insert(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request,HttpServletResponse response){
		 // 트렌젝션 처리
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			
			// 수정 전 히스토리 데이터 insert
			dataMap.put("procedureid", "Warrant.aptWarrantHisory_Insert");
			commonFacade.processInsert(dataMap);
			
			dataMap.put("procedureid", "Warrant.aptWarrantInfo_Update");
			commonFacade.processUpdate(dataMap);
			
			transactionManager.commit(status);
		}catch (Exception e) {
			  transactionManager.rollback(status);
			  e.printStackTrace();
			  dataMap.put("ERROR_CD","999");
			  dataMap.put("ERR_MSG","999");
		  } finally {
			  if (!status.isCompleted()) transactionManager.rollback(status);
		  }
		return new ModelAndView("jsonView", dataMap);
	}
	
	
	/*
	 * 아파트 위임장 엑셀 업로드
	 */
	@RequestMapping(value = "/apt/myWarrantExcelUpload.do")
	public ModelAndView myWarrantExcelUpload(@ModelAttribute("requestParam")DataMap dataMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//트렌젝션 처리
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);

		
		try {

			// 첨부파일정보 db 저장/삭제
			DataMap headerMap = new DataMap();

			// 상담일지 저장 데이터 목록
			List<DataMap> extExcelUploadList = new ArrayList<DataMap>();
			List<String> str = new ArrayList<>();
			int count = 0; // 저장된 갯수 count

			try {
					String[] FILEPATH    = request.getParameterValues("FILEPATH");  	// 파일경로
					String[] TRANSFILENM = request.getParameterValues("TRANSFILENM");	// 변경파일명

					for ( int i = 0; i < FILEPATH.length; i++ ) {
					String excelFilePath = FILEPATH[i] + TRANSFILENM[i];
					List<List<String>> excelList = Utils.getExcelData(MessageUtil.getMessage("SYSTEM.FILE_DOWN_PATH") + excelFilePath);

					if ( excelList.size() > 3 ) {  // 위 2줄은 제목
						for ( int j = 3; j <= excelList.size(); j++ ) {
							List<String> rowList = excelList.get(j-1);

							if ( rowList.size() == 0 ) {  // 데이터가 존재하지 않으면 다음줄로 이동.
								continue;
							}
							
							// 저장 dataMap 생성
							DataMap extExcelUploadMap = new DataMap();
							boolean datacheck = true;
							
							if(!"".equals(rowList.get(1))){
								extExcelUploadMap.put("DONG" , rowList.get(0));
								extExcelUploadMap.put("HOSU" , rowList.get(1));
								extExcelUploadMap.put("USER_NM" , rowList.get(2));
								extExcelUploadMap.put("HP" , rowList.get(3));
								extExcelUploadMap.put("ADDR" , rowList.get(4));
							}
							// 저장할 Data 를  Row 단위로 담기
							extExcelUploadList.add(extExcelUploadMap);
						}
					}
				}
			} catch ( Exception e ) {
				dataMap.put("ERROR_MSG", "업로드한 Excel자료 분석도중에 오류가 발생했습니다.");
				throw new Exception(e);
			}
			
			for ( int i = 0; i < extExcelUploadList.size(); i++ ) {
				DataMap rstMap = extExcelUploadList.get(i);
				rstMap.put("APT_CODE", dataMap.getString("SESSION_APT_CODE"));
				if(!"".equals(rstMap.getString("DONG")) && !"".equals(rstMap.getString("HOSU")) && !"".equals(rstMap.getString("USER_NM"))){
					count++;
					// 수정 전 히스토리 데이터 insert
					rstMap.put("procedureid", "Warrant.aptWarrantHisoryExcel_Insert");
					commonFacade.processInsert(rstMap);
					
					rstMap.put("procedureid", "Warrant.setTbWarrant_info_Update");
					commonFacade.processInsert(rstMap);
				}
			}
			dataMap.put("COUNT", count);
			transactionManager.commit(status);
		} catch ( Exception e ) {
			e.printStackTrace();
			dataMap.put("ERROR_CD" , "999");
			dataMap.put("ERROR_MSG", "에러메시지 : "+e.getMessage());
			log.error("Error", e);
			transactionManager.rollback(status);
		} finally {
			if (!status.isCompleted()) transactionManager.rollback(status);
		}
		return new ModelAndView("jsonView", dataMap);
	}
	
	
	@RequestMapping({"/apt/warrant_histL.do"})
	public ModelAndView warrant_histL(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response
		,@RequestParam(value="PAGE_SIZE", required=false, defaultValue="10")String view_size
		,@RequestParam(value="CURR_PAGE", required=false, defaultValue="1")String page){
		String modelName = "/ourapt/warrant_HistList";
		try {
			dataMap.put("CURR_PAGE",page);
			dataMap.put("PAGE_SIZE",view_size);
			
			
			dataMap.put("procedureid", "Warrant.getMyAptInfo");
			DataMap detail = this.commonFacade.getObject(dataMap);
			dataMap.put("detail", detail);
			dataMap.put("DANZI_YN", detail.getString("DANZI_YN"));
			
			dataMap.put("procedureid", "Warrant.getMyWarrantHist_CNT");
			DataMap cntMap = this.commonFacade.getObject(dataMap);
			if(cntMap == null || "".equals(cntMap)){
				dataMap.put("TOTAL_CNT", "0");
			}else {
				dataMap.put("TOTAL_CNT", cntMap.getString("TOTAL_CNT"));
			}
			
			// 단지 있을경우 단지 조회
			if("Y".equals(dataMap.getString("DANZI_YN"))) {
				// 전체 동 조회
				dataMap.put("procedureid", "Warrant.getMyDanzi_List");
				List DanziList = commonFacade.list(dataMap);
				dataMap.put("DanziList", DanziList);
			}
			
			// 전체 동 조회
			dataMap.put("procedureid", "Warrant.getMyDong_List");
			List DongList = commonFacade.list(dataMap);
			dataMap.put("DongList", DongList);
			
			// 전체 호수 조회
			dataMap.put("procedureid", "Warrant.getMyHosu_List");
			List HosuList = commonFacade.list(dataMap);
			dataMap.put("HosuList", HosuList);
			
			dataMap.put("procedureid", "Warrant.getMyWarrantHist_List");
			List resultList = commonFacade.list(dataMap);
			dataMap.put("resultList", resultList);
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	/**
	 * 위임장 정보 수정 상세내용
	 */
	@RequestMapping({"/apt/warrant_hist_Detail.do"})
	public ModelAndView warrant_hist_Detail(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response){
		String modelName = "/ourapt/warrant_hist_detail";
		try {
			
			dataMap.put("procedureid", "Warrant.getMyAptInfo");
			DataMap aptMap = this.commonFacade.getObject(dataMap);
			dataMap.put("aptMap", aptMap);
			dataMap.put("DANZI_YN", aptMap.getString("DANZI_YN"));
			dataMap.put("JOHAP_YN", aptMap.getString("JOHAP_YN"));
			
			dataMap.put("procedureid", "Warrant.getMyAptDetailInfo");
			DataMap detail = this.commonFacade.getObject(dataMap);
			dataMap.put("detail", detail);
			
			dataMap.put("procedureid", "Warrant.getMyWarrantHist_Detail_List");
			List histList = commonFacade.list(dataMap);
			dataMap.put("histList", histList);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	
	@RequestMapping({"/apt/apt_amtL.do"})
	public ModelAndView apt_amtL(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response
		,@RequestParam(value="PAGE_SIZE", required=false, defaultValue="10")String view_size
		,@RequestParam(value="CURR_PAGE", required=false, defaultValue="1")String page){
		String modelName = "/ourapt/apt_amtList";
		try {
			dataMap.put("CURR_PAGE",page);
			dataMap.put("PAGE_SIZE",view_size);
			
			dataMap.put("procedureid", "Warrant.getAptAmtTotal");
			DataMap totMap = this.commonFacade.getObject(dataMap);
			if(totMap == null || "".equals(totMap)){
				dataMap.put("TOTAL_AMT", "0");
			}else {
				dataMap.put("TOTAL_AMT", totMap.getString("TOTAL_AMT"));
			}
			
			dataMap.put("procedureid", "Warrant.getMyAptInfo");
			DataMap detail = this.commonFacade.getObject(dataMap);
			dataMap.put("detail", detail);
			dataMap.put("DANZI_YN", detail.getString("DANZI_YN"));
			
			dataMap.put("procedureid", "Warrant.getMyamt_CNT");
			DataMap cntMap = this.commonFacade.getObject(dataMap);
			if(cntMap == null || "".equals(cntMap)){
				dataMap.put("TOTAL_CNT", "0");
			}else {
				dataMap.put("TOTAL_CNT", cntMap.getString("TOTAL_CNT"));
			}
			
			// 단지 있을경우 단지 조회
			if("Y".equals(dataMap.getString("DANZI_YN"))) {
				// 전체 동 조회
				dataMap.put("procedureid", "Warrant.getMyDanzi_List");
				List DanziList = commonFacade.list(dataMap);
				dataMap.put("DanziList", DanziList);
			}
			
			// 전체 동 조회
			dataMap.put("procedureid", "Warrant.getMyDong_List");
			List DongList = commonFacade.list(dataMap);
			dataMap.put("DongList", DongList);
			
			// 전체 호수 조회
			dataMap.put("procedureid", "Warrant.getMyHosu_List");
			List HosuList = commonFacade.list(dataMap);
			dataMap.put("HosuList", HosuList);
			
			dataMap.put("procedureid", "Warrant.getMyamt_List");
			List resultList = commonFacade.list(dataMap);
			dataMap.put("resultList", resultList);
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	/*
	 * 아파트 회비내역 일괄 업로드
	 */
	@RequestMapping(value = "/apt/myAmtExcelUpload.do")
	public ModelAndView myAmtExcelUpload(@ModelAttribute("requestParam")DataMap dataMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//트렌젝션 처리
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);

		
		try {

			// 첨부파일정보 db 저장/삭제
			DataMap headerMap = new DataMap();

			// 상담일지 저장 데이터 목록
			List<DataMap> extExcelUploadList = new ArrayList<DataMap>();
			List<String> str = new ArrayList<>();
			int count = 0; // 저장된 갯수 count

			try {
					String[] FILEPATH    = request.getParameterValues("FILEPATH");  	// 파일경로
					String[] TRANSFILENM = request.getParameterValues("TRANSFILENM");	// 변경파일명

					for ( int i = 0; i < FILEPATH.length; i++ ) {
					String excelFilePath = FILEPATH[i] + TRANSFILENM[i];
					List<List<String>> excelList = Utils.getExcelData(MessageUtil.getMessage("SYSTEM.FILE_DOWN_PATH") + excelFilePath);

					if ( excelList.size() > 3 ) {  // 위 2줄은 제목
						for ( int j = 3; j <= excelList.size(); j++ ) {
							List<String> rowList = excelList.get(j-1);

							if ( rowList.size() == 0 ) {  // 데이터가 존재하지 않으면 다음줄로 이동.
								continue;
							}
							
							// 저장 dataMap 생성
							DataMap extExcelUploadMap = new DataMap();
							boolean datacheck = true;
							
							if(!"".equals(rowList.get(2))){
								extExcelUploadMap.put("DONG" , rowList.get(0));
								extExcelUploadMap.put("HOSU" , rowList.get(1));
								extExcelUploadMap.put("USER_NM" , rowList.get(2));
								extExcelUploadMap.put("AMT" , rowList.get(3));
								extExcelUploadMap.put("AMT_IN_DATE" , rowList.get(4));
							}
							// 저장할 Data 를  Row 단위로 담기
							extExcelUploadList.add(extExcelUploadMap);
						}
					}
				}
			} catch ( Exception e ) {
				dataMap.put("ERROR_CD", "404");
				throw new Exception(e);
			}
			
			for ( int i = 0; i < extExcelUploadList.size(); i++ ) {
				DataMap rstMap = extExcelUploadList.get(i);
				rstMap.put("APT_CODE", dataMap.getString("SESSION_APT_CODE"));
				if(!"".equals(rstMap.getString("AMT")) && !"".equals(rstMap.getString("USER_NM"))){
					if(!"".equals(rstMap.getString("DONG")) && !"".equals(rstMap.getString("HOSU")) ){
						rstMap.put("procedureid", "Warrant.setTbWarrant_info_AmtUpdate");
						commonFacade.processInsert(rstMap);
					}
					count++;
					
					rstMap.put("procedureid", "Warrant.setTbAmt_Insert");
					commonFacade.processInsert(rstMap);
				}
			}
			dataMap.put("COUNT", count);
			transactionManager.commit(status);
		} catch ( Exception e ) {
			e.printStackTrace();
			dataMap.put("ERROR_CD" , "999");
			dataMap.put("ERROR_MSG", "에러메시지 : "+e.getMessage());
			log.error("Error", e);
			transactionManager.rollback(status);
		} finally {
			if (!status.isCompleted()) transactionManager.rollback(status);
		}
		return new ModelAndView("jsonView", dataMap);
	}
	
	
	
	/*
	 * 아파트 회비내역 삭제
	 */
	@RequestMapping(value = "/apt/myAmtDelete.do")
	public ModelAndView myAmtDelete(@ModelAttribute("requestParam")DataMap dataMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//트렌젝션 처리
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {

			dataMap.put("procedureid", "Warrant.setTbAmt_Delete");
			commonFacade.processInsert(dataMap);
			
			transactionManager.commit(status);
		} catch ( Exception e ) {
			e.printStackTrace();
			dataMap.put("ERROR_CD" , "999");
			dataMap.put("ERROR_MSG", "에러메시지 : "+e.getMessage());
			log.error("Error", e);
			transactionManager.rollback(status);
		} finally {
			if (!status.isCompleted()) transactionManager.rollback(status);
		}
		return new ModelAndView("jsonView", dataMap);
	}
	
	
	/**
	 * 회비관리 상세
	 */
	@RequestMapping({"/apt/apt_amtDetail.do"})
	public ModelAndView apt_amtDetail(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response){
		String modelName = "/ourapt/apt_amtDetail";
		try {
			
			dataMap.put("procedureid", "Warrant.getMyAptInfo");
			DataMap aptMap = this.commonFacade.getObject(dataMap);
			dataMap.put("aptMap", aptMap);
			dataMap.put("DANZI_YN", aptMap.getString("DANZI_YN"));
			dataMap.put("JOHAP_YN", aptMap.getString("JOHAP_YN"));
			
			// 단지 있을경우 단지 조회
			if("Y".equals(dataMap.getString("DANZI_YN"))) {
				// 전체 동 조회
				dataMap.put("procedureid", "Warrant.getMyDanzi_List");
				List DanziList = commonFacade.list(dataMap);
				dataMap.put("DanziList", DanziList);
			}
			
			// 전체 동 조회
			dataMap.put("procedureid", "Warrant.getMyDong_List");
			List DongList = commonFacade.list(dataMap);
			dataMap.put("DongList", DongList);
			
			// 전체 호수 조회
			dataMap.put("procedureid", "Warrant.getMyHosu_List");
			List HosuList = commonFacade.list(dataMap);
			dataMap.put("HosuList", HosuList);
			
			dataMap.put("procedureid", "Warrant.getMyAptAmtDetailInfo");
			DataMap detail = this.commonFacade.getObject(dataMap);
			dataMap.put("detail", detail);
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	@RequestMapping(value = "/apt/apt_AmtUpdate.do")
	public ModelAndView apt_AmtUpdate(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request,HttpServletResponse response){
		 // 트렌젝션 처리
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			
			// 위임장 업데이트
			dataMap.put("procedureid", "Warrant.aptWarrantAmt_Update");
			commonFacade.processUpdate(dataMap);
			
			// 회비 업데이트
			dataMap.put("procedureid", "Warrant.aptAmtInfo_Update");
			commonFacade.processUpdate(dataMap);
			
			transactionManager.commit(status);
		}catch (Exception e) {
			  transactionManager.rollback(status);
			  e.printStackTrace();
			  dataMap.put("ERROR_CD","999");
			  dataMap.put("ERR_MSG","999");
		  } finally {
			  if (!status.isCompleted()) transactionManager.rollback(status);
		  }
		return new ModelAndView("jsonView", dataMap);
	}
	
	
	
	
	@RequestMapping({"/apt/smsSend.do"})
	public ModelAndView smsSend(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response
		,@RequestParam(value="PAGE_SIZE", required=false, defaultValue="10")String view_size
		,@RequestParam(value="CURR_PAGE", required=false, defaultValue="1")String page){
		String modelName = "/ourapt/smsSendList";
		try {
			dataMap.put("CURR_PAGE",page);
			dataMap.put("PAGE_SIZE",view_size);
			
			dataMap.put("procedureid", "Warrant.getMySmsAmtTot");
			DataMap AmtMap = this.commonFacade.getObject(dataMap);
			String now_amt = AmtMap.getString("NOW_AMT");
			dataMap.put("NOW_AMT", now_amt);
			
			dataMap.put("procedureid", "Warrant.getMyAptInfo");
			DataMap detail = this.commonFacade.getObject(dataMap);
			dataMap.put("detail", detail);
			dataMap.put("DANZI_YN", detail.getString("DANZI_YN"));
			
			dataMap.put("procedureid", "Warrant.getMyWarrantSms_CNT");
			DataMap cntMap = this.commonFacade.getObject(dataMap);
			if(cntMap == null || "".equals(cntMap)){
				dataMap.put("TOTAL_CNT", "0");
			}else {
				dataMap.put("TOTAL_CNT", cntMap.getString("TOTAL_CNT"));
			}
			
			// 단지 있을경우 단지 조회
			if("Y".equals(dataMap.getString("DANZI_YN"))) {
				// 전체 동 조회
				dataMap.put("procedureid", "Warrant.getMyDanzi_List");
				List DanziList = commonFacade.list(dataMap);
				dataMap.put("DanziList", DanziList);
			}
			
			// 전체 동 조회
			dataMap.put("procedureid", "Warrant.getMyDong_List");
			List DongList = commonFacade.list(dataMap);
			dataMap.put("DongList", DongList);
			
			// 전체 호수 조회
			dataMap.put("procedureid", "Warrant.getMyHosu_List");
			List HosuList = commonFacade.list(dataMap);
			dataMap.put("HosuList", HosuList);
			
			dataMap.put("procedureid", "Warrant.getMyWarrantSms_List");
			List resultList = commonFacade.list(dataMap);
			dataMap.put("resultList", resultList);
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	/**
	 * 회비관리 상세
	 */
	@RequestMapping({"/apt/smsSendPop.do"})
	public ModelAndView smsSendPop(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response){
		String modelName = "/common/pop_smsSend";
		try {
			
			dataMap.put("procedureid", "Warrant.getMyWarrantSmsPop_CNT");
			DataMap cntMap = this.commonFacade.getObject(dataMap);
			if(cntMap == null || "".equals(cntMap)){
				dataMap.put("TOTAL_CNT", "0");
			}else {
				dataMap.put("TOTAL_CNT", cntMap.getString("TOTAL_CNT"));
			}
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	@RequestMapping(value = "/apt/smsSend_Insert.do")
	public ModelAndView smsSend_Insert(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request,HttpServletResponse response){
		// 트렌젝션 처리
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			
			
			dataMap.put("procedureid", "Warrant.getMyAptInfo");
			DataMap detail = this.commonFacade.getObject(dataMap);
			String send_tel = detail.getString("SEND_TEL");
			
			String result = "";
			
			int tc = dataMap.getInt("TOTAL_CNT");
			if(tc > 1000) {
				dataMap.put("procedureid", "Warrant.getTmp");
				DataMap cntMap = this.commonFacade.getObject(dataMap);
				int avg = cntMap.getInt("TC");
				for(int i =0; i < avg; i ++) {
					dataMap.put("MANY_CNT", "Y");
					int s_tmp = 0;
					int e_tmp = 0;
					if(i == 0) {
						s_tmp = 1;
						e_tmp = 1000;
					}else if(i == 1) {
						s_tmp = 1000;
						e_tmp = 2000;
					}else if(i == 2) {
						s_tmp = 2000;
						e_tmp = 3000;
					}else if(i == 3) {
						s_tmp = 3000;
						e_tmp = 4000;
					}else if(i == 4) {
						s_tmp = 4000;
						e_tmp = 5000;
					}else if(i == 5) {
						s_tmp = 5000;
						e_tmp = 6000;
					}else if(i == 6) {
						s_tmp = 6000;
						e_tmp = 7000;
					}
					
					dataMap.put("LIMIT_S_CNT", s_tmp);
					dataMap.put("LIMIT_E_CNT", e_tmp);
					dataMap.put("procedureid", "Warrant.getNowSendTelInfo");
					DataMap dMap = this.commonFacade.getObject(dataMap);
					dMap.put("SMS_CONT", dataMap.getString("SMS_CONT"));
					dMap.put("TITLE", dataMap.getString("TITLE"));
					result = sendSms(send_tel, dMap);
					
					System.out.println("result==========" + result);
					if(result != null && !"".equals(result)) {
						Map<String, Object> map = new ObjectMapper().readValue(result, HashMap.class);
						String result_code = (String) map.get("result_code");
						String message = (String) map.get("message");
						int success_cnt = (int) map.get("success_cnt");
						String msg_type = (String) map.get("msg_type");
						
						int amt = 0;
						if("SMS".equals(msg_type)) {
							amt = 10;
						}else {
							amt = 30;
						}
						if(success_cnt > 0) {
							dataMap.put("NOW_AMT", success_cnt * amt);
							dataMap.put("SUCCESS_CNT", success_cnt);
							
							dataMap.put("procedureid", "Warrant.aptSendSms_Update");
							commonFacade.processUpdate(dataMap);
							
							dataMap.put("APT_CODE", dataMap.getString("SESSION_APT_CODE"));
							dataMap.put("TIT_GUBUN", "출금");
							dataMap.put("CONT", dataMap.getString("SMS_CONT"));
							dataMap.put("AMT", success_cnt * amt);
							dataMap.put("SEND_CNT", success_cnt);
							// 히스토리 저장
							dataMap.put("procedureid", "Warrant.aptSMSSendDtl_Insert");
							commonFacade.processUpdate(dataMap);
							
						}
					}
					
				}
			}else {
				dataMap.put("procedureid", "Warrant.getNowSendTelInfo");
				DataMap dMap = this.commonFacade.getObject(dataMap);
				dMap.put("SMS_CONT", dataMap.getString("SMS_CONT"));
				dMap.put("TITLE", dataMap.getString("TITLE"));
				
				result = sendSms(send_tel, dMap);
				
				System.out.println("result==========" + result);
				if(result != null && !"".equals(result)) {
					Map<String, Object> map = new ObjectMapper().readValue(result, HashMap.class);
					String result_code = (String) map.get("result_code");
					String message = (String) map.get("message");
					int success_cnt = (int) map.get("success_cnt");
					String msg_type = (String) map.get("msg_type");
					
					int amt = 0;
					if("SMS".equals(msg_type)) {
						amt = 10;
					}else {
						amt = 30;
					}
					if(success_cnt > 0) {
						dataMap.put("NOW_AMT", success_cnt * amt);
						dataMap.put("SUCCESS_CNT", success_cnt);
						
						dataMap.put("procedureid", "Warrant.aptSendSms_Update");
						commonFacade.processUpdate(dataMap);
						
						dataMap.put("TIT_GUBUN", "출금");
						dataMap.put("CONT", dataMap.getString("SMS_CONT"));
						dataMap.put("AMT", success_cnt * amt);
						dataMap.put("SEND_CNT", success_cnt);
						// 히스토리 저장
						dataMap.put("procedureid", "Warrant.aptSMSSendDtl_Insert");
						commonFacade.processUpdate(dataMap);
					}
				}
			}
			
			
			transactionManager.commit(status);
		}catch (Exception e) {
			transactionManager.rollback(status);
			e.printStackTrace();
			dataMap.put("ERROR_CD","999");
			dataMap.put("ERR_MSG","999");
		} finally {
			if (!status.isCompleted()) transactionManager.rollback(status);
		}
		
		FlashMap fm = RequestContextUtils.getOutputFlashMap(request);
		fm.put("TMC", dataMap.getString("TMC"));
		fm.put("LMC", dataMap.getString("LMC"));
		
		return new ModelAndView("jsonView", dataMap);
	}
	
	
	public String sendSms(String send_tel, DataMap dataMap) {
		final String encodingType = "utf-8";
		final String boundary = "____boundary____";
		String result = "";
		
		try {
			/******************** 인증정보 ********************/
			String sms_url = "https://apis.aligo.in/send/"; // 전송요청 URL
			
			Map<String, String> sms = new HashMap<String, String>();
			
			sms.put("user_id", "almtyc87"); // SMS 아이디
			sms.put("key", "qbq1rif0nofyitww140i96s33m7bdfme"); //인증키
			
			sms.put("msg", dataMap.getString("SMS_CONT")); // 메세지 내용
//			sms.put("receiver", "01111111111,01111111112"); // 수신번호
			sms.put("receiver", dataMap.getString("HP")); // 수신번호
			sms.put("destination", dataMap.getString("P_NM")); // 수신인 %고객명% 치환
			sms.put("sender", send_tel); // 발신번호
			sms.put("testmode_yn", ""); // Y 인경우 실제문자 전송X , 자동취소(환불) 처리
			sms.put("title", dataMap.getString("TITLE")); //  LMS, MMS 제목 (미입력시 본문중 44Byte 또는 엔터 구분자 첫라인)
			
			String image = "";
			
			/******************** 전송정보 ********************/
			
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			
			builder.setBoundary(boundary);
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			builder.setCharset(Charset.forName(encodingType));
			
			for(Iterator<String> i = sms.keySet().iterator(); i.hasNext();){
				String key = i.next();
				builder.addTextBody(key, sms.get(key)
						, ContentType.create("Multipart/related", encodingType));
			}
			
			HttpEntity entity = builder.build();
			
			HttpClient client = HttpClients.createDefault();
			HttpPost post = new HttpPost(sms_url);
			post.setEntity(entity);
			
			HttpResponse res = client.execute(post);
			
			if(res != null){
				BufferedReader in = new BufferedReader(new InputStreamReader(res.getEntity().getContent(), encodingType));
				String buffer = null;
				while((buffer = in.readLine())!=null){
					result += buffer;
					System.out.println("buffer==========" + buffer);
				}
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	
	/**
	 * 입금/발송이력
	 */
	@RequestMapping({"/apt/smsSendHist.do"})
	public ModelAndView smsSendHist(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response
			 ,@RequestParam(value="PAGE_SIZE", required=false, defaultValue="10")String view_size
			  ,@RequestParam(value="CURR_PAGE", required=false, defaultValue="1")String page){
		String modelName = "/ourapt/smsHistList";
		try {
			
			dataMap.put("CURR_PAGE",page);
			dataMap.put("PAGE_SIZE",view_size);
			
			dataMap.put("procedureid", "Warrant.getSmsSendDtl_CNT");
			DataMap cntMap = this.commonFacade.getObject(dataMap);
			dataMap.put("TOTAL_CNT", cntMap.getString("TOTAL_CNT"));
			
			dataMap.put("procedureid", "Warrant.getSmsSendDtl_List");
			List resultList = commonFacade.list(dataMap);
			dataMap.put("resultList", resultList);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}

	
	
	/**
	 * 입주자 검색
	 */
	@RequestMapping({"/apt/apt_Search.do"})
	public ModelAndView apt_Search(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response){
		String modelName = "/ourapt/aptMemSearch";
		try {
			
			dataMap.put("procedureid", "Warrant.getAptMstr_List");
			List aptList = commonFacade.list(dataMap);
			dataMap.put("aptList", aptList);
			
			if("Y".equals(dataMap.getString("SEARCH_YN"))) {
				dataMap.put("procedureid", "Warrant.getAptWarrant_Info");
				DataMap AptMeminfo = commonFacade.getObject(dataMap);
				
				if(AptMeminfo == null || "".equals(AptMeminfo)) {
					AptMeminfo = new DataMap();
				}
				
				dataMap.put("AptMeminfo", AptMeminfo);
				dataMap.put("DATA_YN", "Y");
			}else {
				DataMap AptMeminfo = new DataMap();
				dataMap.put("AptMeminfo", AptMeminfo);
				dataMap.put("DATA_YN", "N");
			}
			dataMap.put("SEARCH_YN", "N");
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	@RequestMapping({"/apt/getDongAndHosuList.do"}) 
	public ModelAndView getDongAndHosuList(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response){
		try {
			
			if("D".equals(dataMap.getString("SELECT_TYPE"))) {
				// 전체 동 조회
				dataMap.put("procedureid", "Warrant.getSearchDong_List");
				List DongList = commonFacade.list(dataMap);
				dataMap.put("DongList", DongList);
			}else {
				// 전체 호수 조회
				dataMap.put("procedureid", "Warrant.getSearchHosu_List");
				List HosuList = commonFacade.list(dataMap);
				dataMap.put("HosuList", HosuList);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		FlashMap fm = RequestContextUtils.getOutputFlashMap(request);
		fm.put("TMC", dataMap.getString("TMC"));
		fm.put("LMC", dataMap.getString("LMC"));
		return new ModelAndView("jsonView", dataMap);
	}

}
