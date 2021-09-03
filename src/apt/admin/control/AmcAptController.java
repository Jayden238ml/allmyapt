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
public class AmcAptController extends LincActionController {
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

	/**
	 * 관리자 > 아파트 동 호수 관리
	 * 
	 * @param dataMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/amc/amcDongHosuL.do")
	public ModelAndView amcDongHosuL(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request,HttpServletResponse response
			,@RequestParam(value="PAGE_SIZE", required=false, defaultValue="10")String view_size
			,@RequestParam(value="CURR_PAGE", required=false, defaultValue="1")String page){
		
		String modelName = "";
		
		try {
			if("".equals(dataMap.getString("A_LMC"))) {
				dataMap.put("A_LMC", "A_LMC002");
			}
			
			dataMap.put("CURR_PAGE",page);
			dataMap.put("PAGE_SIZE",view_size);
			
			dataMap.put("procedureid", "Warrant.getAmcWarrant_CNT");
			DataMap cntMap = this.commonFacade.getObject(dataMap);
			if(cntMap == null || "".equals(cntMap)){
				dataMap.put("TOTAL_CNT", "0");
			}else {
				dataMap.put("TOTAL_CNT", cntMap.getString("TOTAL_CNT"));
			}
			
			dataMap.put("procedureid", "Warrant.getAmcWarrant_List");
			List resultList = commonFacade.list(dataMap);
			dataMap.put("resultList", resultList);
			
			modelName = "/admin/apt/admWarrantL";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	/**
	 * 관리자 > 아파트 동 호수 관리
	 * 
	 * @param dataMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/amc/amcDongHosuPop.do")
	public ModelAndView amcDongHosuPop(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request,HttpServletResponse response){
		
		String modelName = "";
		
		try {
			if("".equals(dataMap.getString("A_LMC"))) {
				dataMap.put("A_LMC", "A_LMC002");
			}
			
			dataMap.put("procedureid", "Warrant.getAptCode_List");
			List resultList = commonFacade.list(dataMap);
			dataMap.put("resultList", resultList);
			
			modelName = "/common/pop_WarrantWrite";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	/*
	 * 아파트 동호수 엑셀 업로드
	 */
	@RequestMapping(value = "/amc/amcWarrantExcelUpload.do")
	public ModelAndView amcWarrantExcelUpload(@ModelAttribute("requestParam")DataMap dataMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
								extExcelUploadMap.put("DANZI" , rowList.get(0));
								extExcelUploadMap.put("DONG" , rowList.get(1));
								extExcelUploadMap.put("HOSU" , rowList.get(2));
								if("".equals(rowList.get(3))) {
									extExcelUploadMap.put("WARRANT_YN" , "N");
								}else {
									extExcelUploadMap.put("WARRANT_YN" , rowList.get(3));
								}
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
				rstMap.put("APT_CODE", dataMap.getString("POP_APT_CODE"));
				if(!"".equals(rstMap.getString("DONG")) && !"".equals(rstMap.getString("HOSU"))){
					count++;
					rstMap.put("procedureid", "Warrant.setTbWarrant_info_Insert");
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
	
	

}