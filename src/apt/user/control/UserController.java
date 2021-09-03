package apt.user.control;

import apt.framework.common.CommonData;
import apt.framework.common.DataMap;
import apt.framework.common.control.LincActionController;
import apt.framework.common.control.MailDataSet;
import apt.framework.core.CommonFacade;
import apt.framework.util.BrowserUtil;
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

@Controller
public class UserController extends LincActionController
{
	protected CommonFacade commonFacade;
	private PlatformTransactionManager transactionManager;
	Log log = LogFactory.getLog(getClass());


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
		throws Exception
		{
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


	@RequestMapping({"/user/apt_contv.do"})
	public ModelAndView apt_cont(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response)
	{
		String modelName = "";
		try {
			modelName = "/user/apt_cont";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}

	/**
	 * Q&A 목록 조회
	 */
	@RequestMapping({"/user/apt_qnal.do"})
	public ModelAndView apt_qnal(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response
			,@RequestParam(value="PAGE_SIZE", required=false, defaultValue="10")String view_size
			,@RequestParam(value="CURR_PAGE", required=false, defaultValue="1")String page){
		String modelName = "/user/apt_qna";
		try {
		
			dataMap.put("CURR_PAGE",page);
			dataMap.put("PAGE_SIZE",view_size);
			
			dataMap.put("procedureid", "Board.getQnaBoard_CNT");
			DataMap cntMap = this.commonFacade.getObject(dataMap);
			dataMap.put("TOTAL_CNT", cntMap.getString("TOTAL_CNT"));
			
			dataMap.put("procedureid", "Board.getQnaBoard_List");
			List resultList = commonFacade.list(dataMap);
			dataMap.put("resultList", resultList);
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}

	/**
	 * Q&A 작성 및 수정
	 */
	@RequestMapping({"/user/apt_qnaw.do"})
	public ModelAndView apt_qnaw(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response)
	{
		String modelName = "/user/apt_qnaw";
		try {
		
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
	 * Q&A 상세보기
	 */
	@RequestMapping({"/user/apt_qnaD.do"})
	public ModelAndView apt_qnaD(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response)
	{
		String modelName = "/user/apt_qnaD";
		// 트렌젝션 처리
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			dataMap.put("procedureid", "Board.getQnaBoard_Detail");
			DataMap detail = this.commonFacade.getObject(dataMap);
			dataMap.put("detail", detail);
			
			// 비밀글 스크립트에서 변형하여 검색할 수 있어 체크
			if("Y".equals(detail.getString("SECRET_YN"))) {
				if(!"AMC".equals(dataMap.getString("SESSION_USER_TYPE"))) {
					if(!dataMap.getString("SESSION_USER_ID").equals(detail.getString("REG_ID"))) {
						return new ModelAndView("redirect:/user/apt_qnal.do");
					}
				}
			}
			
			dataMap.put("procedureid", "Board.setComBoardInfoViewCnt_Update");
			commonFacade.processUpdate(dataMap);
			transactionManager.commit(status);
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}


	/**
	 * Q&A 답변 등록
	 */
	@RequestMapping({"/user/apt_qnaAnswer.do"})
	public ModelAndView apt_qnaAnswer(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response)
	{
		String modelName = "/user/apt_qnaAnswer";
		try {
			dataMap.put("procedureid", "Board.getQnaBoard_Detail");
			DataMap detail = this.commonFacade.getObject(dataMap);
			dataMap.put("detail", detail);
			if(!"".equals(dataMap.getString("ANSWER_BOARD_SEQ"))) {
				dataMap.put("procedureid", "Board.getQnaBoard_AnswerDetail");
				DataMap A_detail = this.commonFacade.getObject(dataMap);
				dataMap.put("A_detail", A_detail);
			}else {
				DataMap A_detail = new DataMap();
				dataMap.put("A_detail", A_detail);
			}
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}

	/**
	 * Q&A 저장
	 */
	@RequestMapping({"/user/apt_qnaI.do"})
	public ModelAndView apt_qnaI(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response)
	{
		// 트렌젝션 처리
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
		
			String ipAddr = getRemortIP(request);
			dataMap.put("IP", ipAddr);
			
			if("".equals(dataMap.getString("SECRET_YN"))) {
				dataMap.put("SECRET_YN", "N");
			}
			dataMap.put("BOARD_TYPE", "Q");
			if("".equals(dataMap.getString("BOARD_SEQ"))) {
				dataMap.put("procedureid", "Board.setComBoardInfo_Insert");
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
		
		fm.put("TMC", dataMap.getString("TMC"));
		fm.put("LMC", dataMap.getString("LMC"));
		
		return new ModelAndView("redirect:/user/apt_qnal.do");
	}

	/**
	 * Q&A 답변 저장
	 */
	@RequestMapping({"/user/apt_qnaAnswerI.do"})
	public ModelAndView apt_qnaAnswerI(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response)
	{
		// 트렌젝션 처리
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			
			String ipAddr = getRemortIP(request);
			dataMap.put("IP", ipAddr);
			
			dataMap.put("BOARD_TYPE", "QA");
			dataMap.put("SECRET_YN", "N");
			if("".equals(dataMap.getString("ANSWER_BOARD_SEQ"))) {
				dataMap.put("procedureid", "Board.setComBoardInfo_Insert");
				commonFacade.processInsert(dataMap);
				
				// 질문 글 업데이트
				dataMap.put("procedureid", "Board.setComBoardInfoAnswer_Update");
				commonFacade.processInsert(dataMap);
			}else {
				dataMap.put("procedureid", "Board.setComBoardInfo_AanswerUpdate");
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
		
		fm.put("TMC", dataMap.getString("TMC"));
		fm.put("LMC", dataMap.getString("LMC"));
		
		return new ModelAndView("redirect:/user/apt_qnal.do");
	}


	/**
	 * Q&A 삭제
	 */
	@RequestMapping({"/user/qnaDelete.do"})
	public ModelAndView qnaDelete(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response)
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
		
		fm.put("TMC", dataMap.getString("TMC"));
		fm.put("LMC", dataMap.getString("LMC"));
		
		return new ModelAndView("redirect:/user/apt_qnal.do");
	}

	@RequestMapping({"/user/apt_reqw.do"})
	public ModelAndView apt_reqw(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response)
	{
		String modelName = "/user/apt_req";
		try {
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}

	/**
	 * 견적문의 메일 발송
	 */
	@RequestMapping({"/user/sendMail.do"})
	public ModelAndView sendMail(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response)
	{
		// 트렌젝션 처리
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
		
			dataMap.put("procedureid", "Common.setSendMail_Insert");
			commonFacade.processUpdate(dataMap);
			
			MailDataSet ms = new MailDataSet();
			ms.MailContentSet(dataMap);
			
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

	
	/**
	 * 공지사항 목록
	 */
	@RequestMapping({"/user/apt_notil.do"})
	public ModelAndView apt_notil(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response
		,@RequestParam(value="PAGE_SIZE", required=false, defaultValue="10")String view_size
		,@RequestParam(value="CURR_PAGE", required=false, defaultValue="1")String page){
		String modelName = "/user/apt_notice";
		try {
		
			dataMap.put("CURR_PAGE",page);
			dataMap.put("PAGE_SIZE",view_size);
			
			dataMap.put("procedureid", "Board.getAmcNotice_CNT");
			DataMap cntMap = this.commonFacade.getObject(dataMap);
			dataMap.put("TOTAL_CNT", cntMap.getString("TOTAL_CNT"));
			
			dataMap.put("procedureid", "Board.getAmcNotice_List");
			List resultList = commonFacade.list(dataMap);
			dataMap.put("resultList", resultList);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	/**
	* 공지사항 상세보기
	*/
	@RequestMapping({"/user/apt_notiD.do"})
	public ModelAndView apt_notiD(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response)
	{
		String modelName = "/user/apt_notiD";
		// 트렌젝션 처리
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			dataMap.put("procedureid", "Board.getQnaBoard_Detail");
			DataMap detail = this.commonFacade.getObject(dataMap);
			dataMap.put("detail", detail);
			
			dataMap.put("procedureid", "Board.setComBoardInfoViewCnt_Update");
			commonFacade.processUpdate(dataMap);
			transactionManager.commit(status);
	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}

		
	/**
	 * IP 리턴
	 */
	public String getRemortIP(HttpServletRequest request)
	{
		if (request.getHeader("x-forwarded-for") == null) {
			return request.getRemoteAddr();
		}
		return request.getHeader("x-forwarded-for");
	}
	
	
	
	/**
	 * 단지정보 목록
	 */
	@RequestMapping({"/user/apt_info1L.do"})
	public ModelAndView apt_info1L(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response
		,@RequestParam(value="PAGE_SIZE", required=false, defaultValue="10")String view_size
		,@RequestParam(value="CURR_PAGE", required=false, defaultValue="1")String page){
		String modelName = "/aptInfo/apt_info1_L";
		try {
		
			dataMap.put("CURR_PAGE",page);
			dataMap.put("PAGE_SIZE",view_size);
			
			
//			dataMap.put("APT_BOARD_TYPE", "01");
			dataMap.put("procedureid", "Board.getaptInfo1_CNT");
			DataMap cntMap = this.commonFacade.getObject(dataMap);
			dataMap.put("TOTAL_CNT", cntMap.getString("TOTAL_CNT"));
			
			dataMap.put("procedureid", "Board.getaptInfo1_List");
			List resultList = commonFacade.list(dataMap);
			dataMap.put("resultList", resultList);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	/**
	 * 단지정보 목록
	 */
	@RequestMapping({"/user/apt_info2L.do"})
	public ModelAndView apt_info2L(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response
		,@RequestParam(value="PAGE_SIZE", required=false, defaultValue="10")String view_size
		,@RequestParam(value="CURR_PAGE", required=false, defaultValue="1")String page){
		String modelName = "/aptInfo/apt_info2_L";
		try {
		
			dataMap.put("CURR_PAGE",page);
			dataMap.put("PAGE_SIZE",view_size);
			
			
			dataMap.put("APT_BOARD_TYPE", "02");
			dataMap.put("procedureid", "Board.getaptInfo1_CNT");
			DataMap cntMap = this.commonFacade.getObject(dataMap);
			dataMap.put("TOTAL_CNT", cntMap.getString("TOTAL_CNT"));
			
			dataMap.put("procedureid", "Board.getaptInfo1_List");
			List resultList = commonFacade.list(dataMap);
			dataMap.put("resultList", resultList);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	/**
	 * 평면도정보 목록
	 */
	@RequestMapping({"/user/apt_info3L.do"})
	public ModelAndView apt_info3L(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response
		,@RequestParam(value="PAGE_SIZE", required=false, defaultValue="10")String view_size
		,@RequestParam(value="CURR_PAGE", required=false, defaultValue="1")String page){
		String modelName = "/aptInfo/apt_info3_L";
		try {
		
			dataMap.put("CURR_PAGE",page);
			dataMap.put("PAGE_SIZE",view_size);
			
			
			dataMap.put("APT_BOARD_TYPE", "03");
			dataMap.put("procedureid", "Board.getaptInfo1_CNT");
			DataMap cntMap = this.commonFacade.getObject(dataMap);
			dataMap.put("TOTAL_CNT", cntMap.getString("TOTAL_CNT"));
			
			dataMap.put("procedureid", "Board.getaptInfo1_List");
			List resultList = commonFacade.list(dataMap);
			dataMap.put("resultList", resultList);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	/**
	* 아파트정보 상세보기
	*/
	@RequestMapping({"/user/apt_infod.do"})
	public ModelAndView apt_infod(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response)
	{
		String modelName = "/aptInfo/apt_info_D";
		// 트렌젝션 처리
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			
			if("".equals(dataMap.getString("TAB_GUBUN"))){
				dataMap.put("TAB_GUBUN", "01");
			}
			if("01".equals(dataMap.getString("TAB_GUBUN"))){
				dataMap.put("BOARD_SEQ", dataMap.getString("BOARD_SEQ01"));
			}else if("02".equals(dataMap.getString("TAB_GUBUN"))){
				dataMap.put("BOARD_SEQ", dataMap.getString("BOARD_SEQ02"));
			}else if("03".equals(dataMap.getString("TAB_GUBUN"))){
				dataMap.put("BOARD_SEQ", dataMap.getString("BOARD_SEQ03"));
			}
			dataMap.put("procedureid", "Board.getAptBoard_Detail");
			DataMap detail = this.commonFacade.getObject(dataMap);
			dataMap.put("detail", detail);
			
			dataMap.put("procedureid", "Board.setComAptInfoViewCnt_Update");
			commonFacade.processUpdate(dataMap);
			transactionManager.commit(status);
	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}

}