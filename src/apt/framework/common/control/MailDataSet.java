package apt.framework.common.control;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import apt.framework.common.DataMap;
import apt.framework.util.MessageUtil;
import apt.framework.util.PUtil;

import org.springframework.web.bind.annotation.ModelAttribute;

public class MailDataSet {
	public MailDataSet(){}
	
	public void MailContentSet(DataMap dataMap){
		
		String NM = "";
		String TITLE = "";
		if(!"".equals(dataMap.getString("USER_NM"))) {
			NM = dataMap.getString("USER_NM");
		}else if(!"".equals(dataMap.getString("AGENCY_NM"))) {
			NM = dataMap.getString("AGENCY_NM");
		}
		HashMap ContentMap = null;
		StringBuffer mdata = new StringBuffer();
		
		ContentMap = new HashMap();
		
		TITLE = "[올마이티주식회사] " + NM + " 견적문의가 접수 되었습니다.";

		mdata.append("<p style='font-weight:bold; color:#0264b6; font-size:14px'>견적문의 내용 입니다.</p>");
		mdata.append("<br/>");
		mdata.append("<table style='width: 800px;border-top:#838383 1px solid; border-left:#e4e4e4 1px solid; '>");
		mdata.append("	<colgroup>");
		mdata.append("		<col width='15%'/>");
		mdata.append("		<col width='*'>");
		mdata.append("	</colgroup>");
		mdata.append("   <tbody>");
		mdata.append("      <tr>");
		mdata.append("         <th style='font-size:13px; padding:10px 10px 10px 15px; color:#555; font-weight:bold; text-align:left; background:#f1f1f6; border-right:#e4e4e4 1px solid; border-bottom:1px solid #e4e4e4;'>성명</th>");
		mdata.append("         <td style='font-size:13px; padding:10px; background:#fff; border-right:#e4e4e4 1px solid; border-bottom:1px solid #e4e4e4;'>" + dataMap.getString("USER_NM") + "</td>");
		mdata.append("      </tr>");
		mdata.append("      <tr>");
		mdata.append("         <th style='font-size:13px; padding:10px 10px 10px 15px; color:#555; font-weight:bold; text-align:left; background:#f1f1f6; border-right:#e4e4e4 1px solid; border-bottom:1px solid #e4e4e4;'>기관명</th>");
		mdata.append("         <td style='font-size:13px; padding:10px; background:#fff; border-right:#e4e4e4 1px solid; border-bottom:1px solid #e4e4e4;'>" + dataMap.getString("AGENCY_NM") + "</td>");
		mdata.append("      </tr>");
		mdata.append("      <tr>");
		mdata.append("         <th style='font-size:13px; padding:10px 10px 10px 15px; color:#555; font-weight:bold; text-align:left; background:#f1f1f6; border-right:#e4e4e4 1px solid; border-bottom:1px solid #e4e4e4;'>연락받을 연락처</th>");
		mdata.append("         <td style='font-size:13px; padding:10px; background:#fff; border-right:#e4e4e4 1px solid; border-bottom:1px solid #e4e4e4;'>" + dataMap.getString("RETURN_HP") + "</td>");
		mdata.append("      </tr>");
		mdata.append("      <tr>");
		mdata.append("         <th style='font-size:13px; padding:10px 10px 10px 15px; color:#555; font-weight:bold; text-align:left; background:#f1f1f6; border-right:#e4e4e4 1px solid; border-bottom:1px solid #e4e4e4;'>연락받을 이메일</th>");
		mdata.append("         <td style='font-size:13px; padding:10px; background:#fff; border-right:#e4e4e4 1px solid; border-bottom:1px solid #e4e4e4;'>" + dataMap.getString("RETURN_EMAIL") + "</td>");
		mdata.append("      </tr>");
		mdata.append("      <tr>");
		mdata.append("         <td style='font-size:13px; padding:10px; background:#fff; border-right:#e4e4e4 1px solid; border-bottom:1px solid #e4e4e4;' colspan='2'>" + dataMap.getString("CONTENTS") + "</td>");
		mdata.append("      </tr>");
		mdata.append("   </tbody>");
		mdata.append("</table>");
		
		ContentMap.put("TITLE", TITLE);
		ContentMap.put("CONTENT", mdata);
		ContentMap.put("TO_USER", "almtyc@naver.com");
		
		try{
			
			SendMail CallSendMail = new SendMail();
			CallSendMail.sendmail(ContentMap );
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
