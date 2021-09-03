<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt"        prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"        prefix="fmt" %>
<html lang="ko">
<head>
<title>문자전송</title>
<%@ include file="/static_root/inc/apt_top.jsp" %>
<script type="text/javascript">
	
	function fn_SendSms(){
		
		if($('#TITLE').val() == ")"){
			alert("제목을 입력해 주세요.");
			return;
		}
		if($('#SMS_CONT').val() == ")"){
			alert("내용을 입력해 주세요.");
			return;
		}
		var len = $('#SMS_CONT').val().length;
		var amt = "0";
		if(len > 40){
			amt = Number($('#TOTAL_CNT').val()) * 27;
		}else{
			amt = Number($('#TOTAL_CNT').val()) * 10
		}
		if(Number(amt) > Number($('#NOW_AMT').val())){
			alert("금액 충전 후 발송이 가능합니다.");
			return;
		}
		
		if(confirm("발송하시겠습니까?")){
			$.ajax({
				 type		: "POST"
				,url		: "/apt/smsSend_Insert.do"
				,dataType	: "json"
				,async : false
				,cache : false
				,data		: $("#sendFrm").serialize()
				,success : function(transport) {
					if(transport.ERROR_CD == '999'){
						alert("발송 중 에러가 발생하였습니다.");	
						return;
					}else{
						alert("발송되었습니다.");	
						if("${INIT_DATA.TYPE}" == "P"){
							gfnLayerClose();
						}else{
							gfnMobileLayerClose();
						}
					}
				}
			});
		}
	}
	
	
</script>
</head>
<body>
<form name="sendFrm" id="sendFrm" method="post">
	<input type="hidden" name="TMC" id="TMC" value="${INIT_DATA.TMC}">
	<input type="hidden" name="LMC" id="LMC" value="${INIT_DATA.LMC}">
	<input type="hidden" name="APT_CODE" id="APT_CODE" value="${INIT_DATA.APT_CODE}">
	<input type="hidden" name="SCH_DANZI" id="SCH_DANZI" value="${INIT_DATA.SCH_DANZI}">
	<input type="hidden" name="SCH_DONG" id="SCH_DONG" value="${INIT_DATA.SCH_DONG}">
	<input type="hidden" name="SCH_HOSU" id="SCH_HOSU" value="${INIT_DATA.SCH_HOSU}">
	<input type="hidden" name="SCH_WARRANT_YN" id="SCH_WARRANT_YN" value="${INIT_DATA.SCH_WARRANT_YN}">
	<input type="hidden" name="SCH_AMT_YN" id="SCH_AMT_YN" value="${INIT_DATA.SCH_AMT_YN}">
	<input type="hidden" name="SCH_WORD" id="SCH_WORD" value="${INIT_DATA.SCH_WORD}">
	<input type="hidden" name="TOTAL_CNT" id="TOTAL_CNT" value="${INIT_DATA.TOTAL_CNT}">
	<input type="hidden" name="NOW_AMT" id="NOW_AMT" value="${INIT_DATA.NOW_AMT}">
	<div class="layerWrap680 login_pop_inner">
		<div class="bg"></div>
		<div class="loginLayer">
			<p>
				<c:if test="${INIT_DATA.TYPE eq 'P'}">
					<a href="javascript:gfnLayerClose();" class="cbtn"><img src="/static_root/images/content/popClose.jpg" alt="닫기" title="닫기" /></a>
				</c:if>
				<c:if test="${INIT_DATA.TYPE eq 'M'}">
					<a href="javascript:gfnMobileLayerClose();" class="cbtn"><img src="/static_root/images/content/popClose.jpg" alt="닫기" title="닫기" /></a>
				</c:if>
			</p>
			<div id="loginDiv">
				<h1><span>문자전송</span></h1>
				<br/>
				<fieldset>
					<div class="tableView">
						<table summary="문자전송">
							<caption>문자전송</caption>
							<colgroup>
								<col width="30%" />
								<col width="*" />
							</colgroup>
							<tr>
								<th>발송건수</th>
								<td>
									${INIT_DATA.TOTAL_CNT} 건 (공동명의 포함)
								</td>
							</tr>
							<tr>
								<th>제목</th>
								<td>
									<input type="text" name="TITLE" id="TITLE"  title="제목" class="input w99p" value="" placeholder="제목"/>
								</td>
							</tr>
							<tr>
								<th>문자내용</th>
								<td>
									<textarea name="SMS_CONT" id="SMS_CONT" rows="" cols="" title="문자내용" style="width:99%; height:150px; letter-spacing:-0.5px; word-spacing:2px; line-height:24px;" class="input" maxlength="2000" placeholder="문자내용"></textarea>
								</td>
							</tr>
						</table>
					</div>
					<div class="ac mt15">
						<a href="javascript:fn_SendSms();" class="btn3 w50">발송</a>
					</div>
					<br/>
				</fieldset>
			</div>				
		</div>
	</div>
</form>
</body>
</html>