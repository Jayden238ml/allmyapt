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
public class AmcController extends LincActionController {
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
	 * 관리자 > 메인
	 * 
	 * @param dataMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/amc/amcMain.do")
	public ModelAndView amcMain(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request,HttpServletResponse response
			  ,@RequestParam(value="PAGE_SIZE", required=false, defaultValue="10")String view_size
			  ,@RequestParam(value="CURR_PAGE", required=false, defaultValue="1")String page){

		String modelName = "";

		try {
			if("".equals(dataMap.getString("A_LMC"))) {
				dataMap.put("A_LMC", "A_LMC001");
			}
			
			dataMap.put("CURR_PAGE",page);
			dataMap.put("PAGE_SIZE",view_size);
			
			dataMap.put("procedureid", "System.getAptuser_CNT");
		    DataMap cntMap = this.commonFacade.getObject(dataMap);
		    dataMap.put("TOTAL_CNT", cntMap.getString("TOTAL_CNT"));
			
			dataMap.put("procedureid", "System.getAptuser_List");
			List resultList = commonFacade.list(dataMap);
			dataMap.put("resultList", resultList);
			
			modelName = "/admin/apt/admAptUserInfo";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	/**
	 * 관리자 > 아파트 회원 등록 및 수정
	 * 
	 * @param dataMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/amc/AptMemCreate.do")
	public ModelAndView AptMemCreate(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request,HttpServletResponse response){
		
		String modelName = "";
		
		try {
			if(!"".equals(dataMap.getString("USER_ID"))) {
				dataMap.put("procedureid", "System.getUserInfo_Detail");
			    DataMap detail = this.commonFacade.getObject(dataMap);
			    dataMap.put("detail", detail);
			}else {
				DataMap detail = new DataMap();
				dataMap.put("detail", detail);
			}
			modelName = "/admin/apt/admAptUserDetail";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	/**
	 * 관리자 > 아파트 코드 생성
	 * 
	 * @param dataMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/amc/AptCodeCreate.do")
	public ModelAndView AptCodeCreate(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request,HttpServletResponse response){
		
		try {
			DateFormat sdFormat = new SimpleDateFormat("yyyyMM");
			Calendar cal = Calendar.getInstance();
			String Today = sdFormat.format(cal.getTime());
			
			dataMap.put("YYDD_DATE", Today);
			
			String tmp_code = getMathAptCodeNo();
			String new_code = dataMap.getString("YYDD_DATE") + "-" + tmp_code;
			dataMap.put("NEW_CODE", new_code);
			
			dataMap.put("procedureid", "System.getAptCodeDup_Chk");
		    DataMap dupMap = this.commonFacade.getObject(dataMap);
		    dataMap.put("dupMap", dupMap);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			dataMap.put("ERROR_CD", "999");
		}
		return new ModelAndView("jsonView", dataMap);
	}
	
	
	/**
	 * 관리자 > 비밀번호 초기화
	 * 
	 * @param dataMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/amc/MemPwReset.do")
	public ModelAndView MemPwReset(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request,HttpServletResponse response){
		 // 트렌젝션 처리
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			
			dataMap.put("procedureid", "System.getPwReset_Update");
			commonFacade.processInsert(dataMap);
			
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
	
	/**
	 * 관리자 > id 중복조회
	 * 
	 * @param dataMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/amc/MemIdDupChk.do")
	public ModelAndView MemIdDupChk(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request,HttpServletResponse response){
		
		try {
			
			dataMap.put("procedureid", "System.getUserInfoDup_Chk");
			DataMap dupMap = this.commonFacade.getObject(dataMap);
			dataMap.put("dupMap", dupMap);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			dataMap.put("ERROR_CD", "999");
		}
		return new ModelAndView("jsonView", dataMap);
	}
	
	/**
	 * 아파트 회원 등록 및 수정
	 */
	@RequestMapping({"/amc/AptMemInsert.do"})
	  public ModelAndView sendMail(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response)
	  {
		  // 트렌젝션 처리
		  DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		  def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		  TransactionStatus status = transactionManager.getTransaction(def);
		  try {
			 
			  if("".equals(dataMap.getString("USER_ID"))) {
				  dataMap.put("USER_ID", dataMap.getString("FIRST_USER_ID"));
				  // 아파트 정보 저장
				  dataMap.put("procedureid", "System.setAptInfoMstr_Insert");
				  commonFacade.processInsert(dataMap);
				  
				  // 회원 저장
				  dataMap.put("procedureid", "System.setUserInfo_Insert");
				  commonFacade.processInsert(dataMap);
				  
				  // 메뉴권한 생성
				  dataMap.put("procedureid", "System.setAuthUser_Insert");
				  commonFacade.processInsert(dataMap);
			  }else {
				  // 아파트 정보 저장
				  dataMap.put("procedureid", "System.setAptInfoMstr_Update");
				  commonFacade.processUpdate(dataMap);
				  
				  //회원 저장
				  dataMap.put("procedureid", "System.setUserInfo_Update");
				  commonFacade.processUpdate(dataMap);
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
		  
		  return new ModelAndView("jsonView", dataMap);
	  }
	
	
	/**
	 * 문자발송 금액 충전
	 */
	@RequestMapping({"/amc/SmsSendAmtAdd.do"})
	  public ModelAndView SmsSendAmtAdd(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response)
	  {
		  // 트렌젝션 처리
		  DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		  def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		  TransactionStatus status = transactionManager.getTransaction(def);
		  try {
			 
			  dataMap.put("procedureid", "System.getSmsSend_infoData");
			  DataMap cntMap = this.commonFacade.getObject(dataMap);
			  if("Y".equals(cntMap.getString("CNT"))) {
				  dataMap.put("procedureid", "System.setSmsSend_info_Update");
				  commonFacade.processUpdate(dataMap);
			  }else {
				  dataMap.put("procedureid", "System.setSmsSend_info_Insert");
				  commonFacade.processInsert(dataMap);
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
		  
		  return new ModelAndView("jsonView", dataMap);
	  }

	/**
	 * 관리자 > 공지사항 목록
	 * 
	 * @param dataMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/amc/amcNoticeL.do")
	public ModelAndView amcNoticeL(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request,HttpServletResponse response
			  ,@RequestParam(value="PAGE_SIZE", required=false, defaultValue="10")String view_size
			  ,@RequestParam(value="CURR_PAGE", required=false, defaultValue="1")String page){

		String modelName = "";

		try {
			if("".equals(dataMap.getString("A_LMC"))) {
				dataMap.put("A_LMC", "A_LMC004");
			}
			
			dataMap.put("CURR_PAGE",page);
			dataMap.put("PAGE_SIZE",view_size);
			
			dataMap.put("procedureid", "Board.getAmcNotice_CNT");
		    DataMap cntMap = this.commonFacade.getObject(dataMap);
		    dataMap.put("TOTAL_CNT", cntMap.getString("TOTAL_CNT"));
			
			dataMap.put("procedureid", "Board.getAmcNotice_List");
			List resultList = commonFacade.list(dataMap);
			dataMap.put("resultList", resultList);
			
			modelName = "/admin/notice/admNoticeL";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	/**
	 * 관리자 > 공지사항 등록 및 수정화면
	 * 
	 * @param dataMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/amc/amcNoticeWrite.do")
	public ModelAndView amcNoticeWrite(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request,HttpServletResponse response){
		
		String modelName = "/admin/notice/admNoticeWrite";
		
		try {
			if("".equals(dataMap.getString("A_LMC"))) {
				dataMap.put("A_LMC", "A_LMC004");
			}
			
			if(!"".equals(dataMap.getString("BOARD_SEQ"))) {
				dataMap.put("procedureid", "Board.getQnaBoard_Detail");
			    DataMap detail = this.commonFacade.getObject(dataMap);
			    dataMap.put("detail", detail);
			}else {
				DataMap detail = new DataMap();
				dataMap.put("detail", detail);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	/**
	* 관리자 > 공지사항 등록 및 수정
	*/
	@RequestMapping({"/amc/amcNoticeInsert.do"})
	  public ModelAndView amcNoticeInsert(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response)
	  {
		// 트렌젝션 처리
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		  try {
			
			  String ipAddr = getRemortIP(request);
		      dataMap.put("IP", ipAddr);
		      
		      dataMap.put("SECRET_YN", "N");
		      dataMap.put("BOARD_TYPE", "N");
		      dataMap.put("NOTI_YN", "Y");
		      if("".equals(dataMap.getString("BOARD_SEQ"))) {
		    	  dataMap.put("procedureid", "Board.setComBoardInfoAmc_Insert");
			      commonFacade.processInsert(dataMap);
		      }else {
		    	  dataMap.put("procedureid", "Board.setComBoardInfo_Update");
			      commonFacade.processUpdate(dataMap);
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
			
		  fm.put("A_TMC", dataMap.getString("A_TMC"));
		  fm.put("A_LMC", dataMap.getString("A_LMC"));
		  
		  return new ModelAndView("redirect:/amc/amcNoticeL.do");
	  }
	  
	  
	@RequestMapping({"/amc/amcNoticeDelete.do"})
	public ModelAndView amcNoticeDelete(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response)
	{
		// 트렌젝션 처리
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			  
			dataMap.put("procedureid", "Board.setComBoardInfo_Delete");
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
		  
			FlashMap fm = RequestContextUtils.getOutputFlashMap(request);
			  
			fm.put("A_TMC", dataMap.getString("A_TMC"));
			fm.put("A_LMC", dataMap.getString("A_LMC"));
		  
			 return new ModelAndView("redirect:/amc/amcNoticeL.do");
	}
	
	
	/**
	 * 단지/세대/평면도 관리 목록
	 * 
	 * @param dataMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/amc/amcAptInfoL.do")
	public ModelAndView amcAptInfoL(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request,HttpServletResponse response
			  ,@RequestParam(value="PAGE_SIZE", required=false, defaultValue="10")String view_size
			  ,@RequestParam(value="CURR_PAGE", required=false, defaultValue="1")String page){

		String modelName = "";

		try {
			if("".equals(dataMap.getString("A_LMC"))) {
				dataMap.put("A_LMC", "A_LMC003");
			}
			
			dataMap.put("CURR_PAGE",page);
			dataMap.put("PAGE_SIZE",view_size);
			
			dataMap.put("procedureid", "Board.getAmcApt_CNT");
		    DataMap cntMap = this.commonFacade.getObject(dataMap);
		    dataMap.put("TOTAL_CNT", cntMap.getString("TOTAL_CNT"));
			
			dataMap.put("procedureid", "Board.getAmcApt_List");
			List resultList = commonFacade.list(dataMap);
			dataMap.put("resultList", resultList);
			
			modelName = "/admin/apt/amcAptInfoL";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	/**
	 * 단지/세대/평면도 관리 등록, 수정화면
	 * 
	 * @param dataMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/amc/amcAptInfoWrite.do")
	public ModelAndView amcAptInfoWrite(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request,HttpServletResponse response){
		
		String modelName = "/admin/apt/admAptInfoWrite";
		
		try {
			if("".equals(dataMap.getString("A_LMC"))) {
				dataMap.put("A_LMC", "A_LMC003");
			}
			
			dataMap.put("procedureid", "Common.getSido_List");
			List SidoList = commonFacade.list(dataMap);
			dataMap.put("SidoList", SidoList);
			
			dataMap.put("procedureid", "Common.getErect_List");
			List erecList = commonFacade.list(dataMap);
			dataMap.put("erecList", erecList);
			
			if(!"".equals(dataMap.getString("BOARD_SEQ"))) {
				dataMap.put("procedureid", "Board.getAptBoard_Detail");
			    DataMap detail = this.commonFacade.getObject(dataMap);
			    dataMap.put("detail", detail);
			}else {
				DataMap detail = new DataMap();
				dataMap.put("detail", detail);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	@RequestMapping(value = "/amc/amcAptMstrInfoWrite.do")
	public ModelAndView amcAptMstrInfoWrite(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request,HttpServletResponse response){
		
		String modelName = "/admin/apt/admAptInfoMstrWrite";
		
		try {
			if("".equals(dataMap.getString("A_LMC"))) {
				dataMap.put("A_LMC", "A_LMC003");
			}
			
			dataMap.put("procedureid", "Common.getSido_List");
			List SidoList = commonFacade.list(dataMap);
			dataMap.put("SidoList", SidoList);
			
			dataMap.put("procedureid", "Common.getErect_List");
			List erecList = commonFacade.list(dataMap);
			dataMap.put("erecList", erecList);
			
			if(!"".equals(dataMap.getString("MST_BOARD_SEQ"))) {
				dataMap.put("procedureid", "Board.getAptBoard_Mstr_Detail");
			    DataMap detail = this.commonFacade.getObject(dataMap);
			    dataMap.put("detail", detail);
			}else {
				DataMap detail = new DataMap();
				dataMap.put("detail", detail);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	/**
	* 관리자 > 단지/세대/평면도 관리 등록, 수정
	*/
	@RequestMapping({"/amc/amcAptInfoInsert.do"})
	public ModelAndView amcAptInfoInsert(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response)
	{
		// 트렌젝션 처리
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		DataMap xyMap = new DataMap();
		PUtil pu = new PUtil();
		  try {
			
			  if(!"".equals(dataMap.getString("ADDR1"))){
				  xyMap = pu.addrToLocation(URLEncoder.encode(dataMap.getString("ADDR1"),"UTF-8"));
			  }
			  if(!"".equals(xyMap)){
				  dataMap.put("X_LOCATION", xyMap.getString("X_LOCATION"));
				  dataMap.put("Y_LOCATION", xyMap.getString("Y_LOCATION"));
			  }
			  
		      if("".equals(dataMap.getString("BOARD_SEQ"))) {
		    	  dataMap.put("procedureid", "Board.setAptBoardInfoAmc_Insert");
			      commonFacade.processInsert(dataMap);
		      }else {
		    	  dataMap.put("procedureid", "Board.setAptBoardInfoAmc_Update");
			      commonFacade.processUpdate(dataMap);
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
			
		  fm.put("A_TMC", dataMap.getString("A_TMC"));
		  fm.put("A_LMC", dataMap.getString("A_LMC"));
		  
		  return new ModelAndView("redirect:/amc/amcAptInfoL.do");
	  }
	
	
	@RequestMapping({"/amc/getAreaSiGunGuList.do"}) 
	public ModelAndView getAreaSiGunGuList(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response){
		try {
			
			dataMap.put("procedureid", "Common.getAreaSiGunGu_List");
			List SiGunGuList = commonFacade.list(dataMap);
			dataMap.put("SiGunGuList", SiGunGuList);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		FlashMap fm = RequestContextUtils.getOutputFlashMap(request);
		fm.put("A_TMC", dataMap.getString("A_TMC"));
		fm.put("A_LMC", dataMap.getString("A_LMC"));
		return new ModelAndView("jsonView", dataMap);
	}
	
	
	/**
	 * 아파트 관리정보 삭제
	 */
	@RequestMapping(value = "/amc/AptBoardDelete.do")
	public ModelAndView AptBoardDelete(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request,HttpServletResponse response){
		 // 트렌젝션 처리
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			
			dataMap.put("procedureid", "Board.AptBoardDelYn_Update");
			commonFacade.processInsert(dataMap);
			
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
	
	
	@RequestMapping({"/amc/amcAptInfoMstrInsert.do"})
	public ModelAndView amcAptInfoMstrInsert(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response)
	{
		// 트렌젝션 처리
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		DataMap xyMap = new DataMap();
		PUtil pu = new PUtil();
		  try {
			
			  if(!"".equals(dataMap.getString("ADDR1"))){
				  xyMap = pu.addrToLocation(URLEncoder.encode(dataMap.getString("ADDR1"),"UTF-8"));
			  }
			  if(!"".equals(xyMap)){
				  dataMap.put("X_LOCATION", xyMap.getString("X_LOCATION"));
				  dataMap.put("Y_LOCATION", xyMap.getString("Y_LOCATION"));
			  }
			  
		      if("".equals(dataMap.getString("MST_BOARD_SEQ"))) {
		    	  dataMap.put("procedureid", "Board.setAptBoardInfoAmc_MstrInsert");
			      commonFacade.processInsert(dataMap);
		      }else {
		    	  dataMap.put("procedureid", "Board.setAptBoardInfoAmc_MstrUpdate");
			      commonFacade.processUpdate(dataMap);
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
			
		  fm.put("A_TMC", dataMap.getString("A_TMC"));
		  fm.put("A_LMC", dataMap.getString("A_LMC"));
		  
		  return new ModelAndView("redirect:/amc/amcAptInfoL.do");
	  }
	
	// 아파트 코드 랜덤 생성
	private String getMathAptCodeNo()
	  {
	    String rtn = "";

	    for (int i = 0; i < 6; i++) {
	      double doubleNum = Math.random();
	      int intNum = (int)(Math.random() * 10.0D);
	      rtn = rtn + intNum;
	    }

	    return rtn;
	  }
	
	
	public String getRemortIP(HttpServletRequest request)
	  {
	    if (request.getHeader("x-forwarded-for") == null) {
	      return request.getRemoteAddr();
	    }
	    return request.getHeader("x-forwarded-for");
	  }
	
	
	
	/**
	 * 관리자 > 분양권데이터 insert
	 * 
	 * @param dataMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/amc/ParcelOutInfo_Insert.do")
	public ModelAndView ParcelOutInfo_Insert(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request,HttpServletResponse response){
		 // 트렌젝션 처리
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			int cnt = 0;
			
			dataMap.put("procedureid", "Common.getAreaSiGunGu_AllList");
			List<DataMap> SiGunGuList = commonFacade.list(dataMap);
			
			String tmpyear = dataMap.getString("DEAL_YMD"); 
			String deal_ymd = "";
//			StringBuilder urlBuilder = new StringBuilder("http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcSilvTrade");
//	        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=FmRJggnPbuErC7S3g3D1K51bawXyTDd7hh%2FJZP%2Bdkyl5OdU79rlNJ%2BNZWXUfncUYfKzWtgUj8Ks6oxWvRQdPSg%3D%3D");
//	        urlBuilder.append("&" + URLEncoder.encode("DEAL_YMD", "UTF-8") + "=" + URLEncoder.encode(deal_ymd, "UTF-8"));
	        
	        for(int m = 1 ; m <= 12; m ++) {
	        	String mm = "";
	        	if(m < 10) {
	        		mm = "0" + m;
	        	}else {
	        		mm = Integer.toString(m);
	        	}
	        	deal_ymd = tmpyear + mm;
	        	System.out.println("년월==========" + deal_ymd);
	        	
	        	for(DataMap tmp : SiGunGuList) {
        			StringBuilder urlBuilder = new StringBuilder("http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcSilvTrade");
	    	        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=FmRJggnPbuErC7S3g3D1K51bawXyTDd7hh%2FJZP%2Bdkyl5OdU79rlNJ%2BNZWXUfncUYfKzWtgUj8Ks6oxWvRQdPSg%3D%3D");
	    	        urlBuilder.append("&" + URLEncoder.encode("DEAL_YMD", "UTF-8") + "=" + URLEncoder.encode(deal_ymd, "UTF-8"));
					urlBuilder.append("&" + URLEncoder.encode("LAWD_CD", "UTF-8") + "=" + URLEncoder.encode(tmp.getString("CODE"), "UTF-8"));
					URL url = new URL(urlBuilder.toString());
					System.out.println("년월==========" + deal_ymd);
					System.out.println("시군구명==========" + tmp.getString("CODENM"));
					
					XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			        factory.setNamespaceAware(true);
			        XmlPullParser xpp = factory.newPullParser();
			        BufferedInputStream bis = new BufferedInputStream(url.openStream());
			        xpp.setInput(bis, "utf-8");

			        String tag = null;
			        int event_type = xpp.getEventType();

			        ArrayList list = new ArrayList();
			        DataMap tempMap = tempMap = new DataMap();

			        while (event_type != 1) {
			          if (event_type == 2) {
			            tag = xpp.getName();
			          } else if (event_type == 4)
			          {
			        	  
			            if ("거래금액".equals(tag)) tempMap.put("DEAL_AMOUNT", xpp.getText());
			            if ("년".equals(tag)) tempMap.put("DEAL_YEAR", xpp.getText());
			            if ("단지".equals(tag)) tempMap.put("APARTMENT_NAME", xpp.getText());
			            if ("법정동".equals(tag)) tempMap.put("DONG", xpp.getText());
			            if ("시군구".equals(tag)) tempMap.put("SIGUNGU", xpp.getText());
			            if ("월".equals(tag)) tempMap.put("DEAL_MONTH", xpp.getText());
			            if ("일".equals(tag)) tempMap.put("DEAL_DAY", xpp.getText());
			            if ("전용면적".equals(tag)) tempMap.put("AREA_EXCLUSIVE_USE", xpp.getText());
			            if ("지번".equals(tag)) tempMap.put("JIBUN", xpp.getText());
			            if ("지역코드".equals(tag)) tempMap.put("REGIONAL_CODE", xpp.getText());
			            if ("층".equals(tag)) tempMap.put("FLOOR", xpp.getText());

			          }
			          else if (event_type == 3) {
			            tag = xpp.getName();
			            if (tag.equals("item")) {
			              list.add(tempMap);
			              tempMap = new DataMap();
			            }
			          }

			          event_type = xpp.next();
			        }
			        for(int i =0; i < list.size(); i ++) {
			        	DataMap insertMap = new DataMap();
			        	insertMap = (DataMap) list.get(i);
			        	
			        	insertMap.put("procedureid", "Api.ParcelOutInfo_Insert");
						commonFacade.processInsert(insertMap);
						cnt++;
			        }
				}
	        }
			
			
			dataMap.put("CNT", cnt);
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
	
	
	/**
	 * 관리자 > 실거래데이터 insert
	 * 
	 * @param dataMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/amc/RealInfo_Insert.do")
	public ModelAndView RealInfo_Insert(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request,HttpServletResponse response){
		// 트렌젝션 처리
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			int cnt = 0;
			
			dataMap.put("procedureid", "Common.getAreaSiGunGu_AllList");
			List<DataMap> SiGunGuList = commonFacade.list(dataMap);
			
			String tmpyear = dataMap.getString("DEAL_YMD"); 
			String deal_ymd = tmpyear;
//			StringBuilder urlBuilder = new StringBuilder("http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcSilvTrade");
//	        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=FmRJggnPbuErC7S3g3D1K51bawXyTDd7hh%2FJZP%2Bdkyl5OdU79rlNJ%2BNZWXUfncUYfKzWtgUj8Ks6oxWvRQdPSg%3D%3D");
//	        urlBuilder.append("&" + URLEncoder.encode("DEAL_YMD", "UTF-8") + "=" + URLEncoder.encode(deal_ymd, "UTF-8"));
			
//			for(int m = 5 ; m <= 5; m ++) {
//				String mm = "";
//				if(m < 10) {
//					mm = "0" + m;
//				}else {
//					mm = Integer.toString(m);
//				}
//				deal_ymd = tmpyear + mm;
				
				for(DataMap tmp : SiGunGuList) {
					StringBuilder urlBuilder = new StringBuilder("http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTradeDev");
					urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=FmRJggnPbuErC7S3g3D1K51bawXyTDd7hh%2FJZP%2Bdkyl5OdU79rlNJ%2BNZWXUfncUYfKzWtgUj8Ks6oxWvRQdPSg%3D%3D");
					
					urlBuilder.append("&" + URLEncoder.encode("DEAL_YMD", "UTF-8") + "=" + URLEncoder.encode(deal_ymd, "UTF-8"));
					urlBuilder.append("&" + URLEncoder.encode("LAWD_CD", "UTF-8") + "=" + URLEncoder.encode(tmp.getString("CODE"), "UTF-8"));
			        urlBuilder.append("&numOfRows=100000"); 
			        urlBuilder.append("&pageNo=1"); 
			        System.out.println("년월==========" + deal_ymd);
					System.out.println("시군구명==========" + tmp.getString("CODENM"));
					
					URL url = new URL(urlBuilder.toString());
					
					XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
					factory.setNamespaceAware(true);
					XmlPullParser xpp = factory.newPullParser();
					BufferedInputStream bis = new BufferedInputStream(url.openStream());
					xpp.setInput(bis, "utf-8");
					
					String tag = null;
					int event_type = xpp.getEventType();
					
					ArrayList list = new ArrayList();
					DataMap tempMap = tempMap = new DataMap();
					
					while (event_type != 1) {
						if (event_type == 2) {
							tag = xpp.getName();
						} else if (event_type == 4)
						{
							
							if ("거래금액".equals(tag)) tempMap.put("DEAL_AMOUNT", xpp.getText());
				            if ("년".equals(tag)) tempMap.put("DEAL_YEAR", xpp.getText());
				            if ("아파트".equals(tag)) tempMap.put("APARTMENT_NAME", xpp.getText());
				            if ("법정동".equals(tag)) tempMap.put("DONG", xpp.getText());
				            if ("시군구".equals(tag)) tempMap.put("SIGUNGU_CODE", xpp.getText());
				            if ("월".equals(tag)) tempMap.put("DEAL_MONTH", xpp.getText());
				            if ("일".equals(tag)) tempMap.put("DEAL_DAY", xpp.getText());
				            if ("전용면적".equals(tag)) tempMap.put("AREA_EXCLUSIVE_USE", xpp.getText());
				            if ("지번".equals(tag)) tempMap.put("JIBUN", xpp.getText());
				            if ("지역코드".equals(tag)) tempMap.put("REGIONAL_CODE", xpp.getText());
				            if ("층".equals(tag)) tempMap.put("FLOOR", xpp.getText());
				            if ("건축년도".equals(tag)) tempMap.put("BUILD_YEAR", xpp.getText());
				            if ("도로명".equals(tag)) tempMap.put("ROAD_NAME", xpp.getText());
				            if ("도로명건물본번호코드".equals(tag)) tempMap.put("ROAD_NAME_BONBUN", xpp.getText());
				            if ("도로명건물부번호코드".equals(tag)) tempMap.put("ROAD_NAME_BUBUN", xpp.getText());
				            if ("도로명시군구코드".equals(tag)) tempMap.put("ROAD_NAME_SIGUNGU_CODE", xpp.getText());
				            if ("도로명일련번호코드".equals(tag)) tempMap.put("ROAD_NAME_SEQ", xpp.getText());
				            if ("도로명코드".equals(tag)) tempMap.put("ROAD_NAME_CODE", xpp.getText());
							
						}
						else if (event_type == 3) {
							tag = xpp.getName();
							if (tag.equals("item")) {
								list.add(tempMap);
								tempMap = new DataMap();
							}
						}
						
						event_type = xpp.next();
					}
					for(int i =0; i < list.size(); i ++) {
						DataMap insertMap = new DataMap();
						insertMap = (DataMap) list.get(i);
						String sido_code = insertMap.getString("REGIONAL_CODE").substring(0,2);
						insertMap.put("SIDO_CODE", sido_code);
						
						insertMap.put("procedureid", "Api.getSiGunGuNmData");
					    DataMap addrMap = this.commonFacade.getObject(insertMap);
						
						int bonbun = insertMap.getInt("ROAD_NAME_BONBUN");
						int bubun = insertMap.getInt("ROAD_NAME_BUBUN");
						
						String doro_jibun = "";
						if(bonbun > 0) {
							doro_jibun = String.valueOf(bonbun);
						}
						if(bubun > 0) {
							doro_jibun = doro_jibun + "-" + String.valueOf(bubun);
						}
						
						String address = addrMap.getString("UP_NM") + " " + addrMap.getString("CODENM") + " " + insertMap.getString("ROAD_NAME") + " " +  doro_jibun;
//						System.out.println("주소==========" + address);
//						DataMap xyMap = new DataMap();
//						PUtil pu = new PUtil();
						if(!"".equals(address)){
//							  xyMap = pu.addrToLocation(URLEncoder.encode(address,"UTF-8"));
							  insertMap.put("ADDRESS", address);
						  }
//						  if(!"".equals(xyMap)){
//							  insertMap.put("X_LOCATION", xyMap.getString("X_LOCATION"));
//							  insertMap.put("Y_LOCATION", xyMap.getString("Y_LOCATION"));
//						  }
						
						insertMap.put("procedureid", "Api.RealAptInfo_Insert");
						commonFacade.processInsert(insertMap);
						cnt++;
					}
							
				}	
//			} // 일자 for 끝
			
			
			dataMap.put("CNT", cnt);
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
	
	
	

}