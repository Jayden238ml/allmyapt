<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<%@ include file="/static_root/inc/admin_head.jsp" %>
</head>
<script type="text/javascript">	
	$(document).ready(function(){
		
	});
	
	
	function fnList(){
		$('#frm').attr("action", "/amc/amcMain.do");
		$('#frm').submit();
	}
	
	function fnCodeCreate(){
		$.ajax({
			type: "POST",
			url  : "/amc/AptCodeCreate.do",
			data:  $("#frm").serialize(),
			dataType: "json",
			success: function (transport){
				if(transport.ERROR_CD == "999"){
					alert("처리 중 오류가 발생 하였습니다.");
					return;
				}else{
					var dupMap = transport.dupMap;
					var new_code = transport.NEW_CODE;
					if(dupMap.DUP_YN == "Y"){
						alert("중복된 코드가 존재합니다. 다시 시도해 주세요.");
						return;
					}else{
						alert("코드생성 완료");
						$('#CODE_NM').text(new_code);
						$('#APT_CODE').val(new_code);
					}
				}
			}
		});
	}
	
	function fnDupChk(){
		if($('#FIRST_USER_ID').val() == ""){
			alert("ID 입력하세요");
			$('#FIRST_USER_ID').focus();
			return;
		}
		$.ajax({
			type: "POST",
			url  : "/amc/MemIdDupChk.do",
			data:  $("#frm").serialize(),
			dataType: "json",
			success: function (transport){
				if(transport.ERROR_CD == "999"){
					alert("처리 중 오류가 발생 하였습니다.");
					return;
				}else{
					var dupMap = transport.dupMap;
					var new_code = transport.NEW_CODE;
					if(dupMap.DUP_YN == "Y"){
						alert("중복된 ID가 존재합니다. 다시 입력해 주세요.");
						$('#ID_DUP_CHK').val("N");
						return;
					}else{
						alert("사용 가능한 ID 입니다.");
						$('#ID_DUP_CHK').val("Y");
					}
				}
			}
		});
	}
	
	function fnResetPw(){
		if(confirm("ID와 동일하게 초기화 됩니다. 초기화 하시겠습니까?")){
			$.ajax({
				type: "POST",
				url  : "/amc/MemPwReset.do",
				data:  $("#frm").serialize(),
				dataType: "json",
				success: function (transport){
					if(transport.ERROR_CD == "999"){
						alert("처리 중 오류가 발생 하였습니다.");
						return;
					}else{
						alert("[" + $('#USER_ID').val() + "] 로 변경되었습니다.");
					}
				}
			});
		}
	}
	
	function fnSave(){
		if($('#APT_NM').val() == ""){
			alert("단지명 입력하세요");
			$('#APT_NM').focus();
			return;
		}
		if($('#COMPANY_NM').val() == ""){
			alert("건설사 입력하세요");
			$('#COMPANY_NM').focus();
			return;
		}
		if($('#APT_NUM').val() == ""){
			alert("세대 수 입력하세요");
			$('#APT_NUM').focus();
			return;
		}
		if($('#AREA_NM').val() == ""){
			alert("지역 입력하세요");
			$('#AREA_NM').focus();
			return;
		}
		
		if("${INIT_DATA.USER_ID}" == ""){
			if($('#APT_CODE').val() == ""){
				alert("코드 생성 필수임");
				return;
			}
			if($('#FIRST_USER_ID').val() == ""){
				alert("ID 입력하세요");
				$('#FIRST_USER_ID').focus();
				return;
			}
			if($('#ID_DUP_CHK').val() == "N"){
				alert("ID 중복체크를 해주세요");
				return;
			}
			if($('#USER_PWD').val() == ""){
				alert("최초 비밀번호 입력하세요");
				$('#USER_PWD').focus();
				return;
			}
		}
		
		if($('#USER_NM').val() == ""){
			alert("이름 입력하세요");
			$('#USER_NM').focus();
			return;
		}
		if($('#USER_NICK').val() == ""){
			alert("닉네임 입력하세요");
			$('#USER_NICK').focus();
			return;
		}
		if(confirm("저장하시겠습니까?")){
			$.ajax({
				type: "POST",
				url  : "/amc/AptMemInsert.do",
				data:  $("#frm").serialize(),
				dataType: "json",
				success: function (transport){
					if(transport.ERROR_CD == "999"){
						alert("처리 중 오류가 발생 하였습니다.");
						return;
					}else{
						if("${INIT_DATA.USER_ID}" == ""){
							alert("아파트 회원 생성 완료");
						}else{
							alert("수정되었습니다.");
						}
						fnList();
					}
				}
			});
		}
		
	}
	
	function fnChk(){
		$('#ID_DUP_CHK').val("N");
	}
	
	function fnSmsAmtAdd(){
		if(confirm("충전하시겠습니까?")){
			$.ajax({
				type: "POST",
				url  : "/amc/SmsSendAmtAdd.do",
				data:  $("#frm").serialize(),
				dataType: "json",
				success: function (transport){
					if(transport.ERROR_CD == "999"){
						alert("처리 중 오류가 발생 하였습니다.");
						return;
					}else{
						alert("충전되었습니다.");
						fnList();
					}
				}
			});
		}
	}
	
</script>
<body id="user">
	<div id="wrap">
			<%@ include file="/static_root/inc/admin_header.jsp" %>
		<ul id="content">
			<li id="left">
				<!-- 좌측영역 -->
				<%@ include file="/static_root/inc/admin_left.jsp" %>
				<!-- 좌측영역 끝 -->
			</li>
			<li id="right">
				<!-- 타이틀/네비 -->
				<div class="titleNaviNew">
					<h3 class="subTitle">입예협 사용자 관리</h3>
				</div>
				<!-- 타이틀/네비 끝 -->
				<div id="cont">
					<form name="frm" id="frm" method="post" action="#">
						<input type="hidden" name="A_TMC" id="A_TMC" value="${INIT_DATA.A_TMC}" />
						<input type="hidden" name="A_LMC" id="A_LMC" value="${INIT_DATA.A_LMC}" />
						<input type="hidden" name="APT_CODE" id="APT_CODE" value="${INIT_DATA.detail.APT_CODE}">
						<input type="hidden" name="USER_ID" id="USER_ID" value="${INIT_DATA.USER_ID}">
						<input type="hidden" name="ID_DUP_CHK" id="ID_DUP_CHK" value="N">
						<section class="sect_area sect_first_area">
							<h3 class="title1 mt30">아파트 정보</h3>
							<div class="bbsView" >
								<table summary="입예협 사용자 관리">
									<caption>입예협 사용자 관리</caption>
									<colgroup>
										<col width="20%" />
										<col width="*" />
									</colgroup>
									<tbody>
										<tr>
											<th>단지 명</th>
											<td>
												<input type="text" name="APT_NM" id="APT_NM" title="단지 명" class="input w99p" value="${INIT_DATA.detail.APT_NM}" maxlength="50" placeholder="부천 일루미스테이트" />
											</td>
										</tr>
										<tr>
											<th>건설사</th>
											<td>
												<input type="text" name="COMPANY_NM" id="COMPANY_NM" title="건설사" class="input w99p" value="${INIT_DATA.detail.COMPANY_NM}" maxlength="100"  placeholder=""/>
											</td>
										</tr>
										<tr>
											<th>세대 수</th>
											<td>
												<input type="text" name="APT_NUM" id="APT_NUM" title="세대 수" class="input w99p" value="${INIT_DATA.detail.APT_NUM}" maxlength="50"  placeholder="3,000" />
											</td>
										</tr>
										<tr>
											<th>조합 유무</th>
											<td>
												<label for="JOHAP_YN"><input type="radio" name="JOHAP_YN" id="JOHAP_YN" value="Y" <c:if test="${INIT_DATA.detail.JOHAP_YN eq 'Y' or INIT_DATA.detail.JOHAP_YN eq ''}">checked="checked"</c:if> />&nbsp;&nbsp;조합있음&nbsp;</label>
												<label for="JOHAP_YN"><input type="radio" name="JOHAP_YN" id="JOHAP_YN" value="N" <c:if test="${INIT_DATA.detail.JOHAP_YN eq 'N' }">checked="checked"</c:if> />&nbsp;&nbsp;조합없음&nbsp;</label>
											</td>
										</tr>
										<tr>
											<th>단지 유무</th>
											<td>
												<label for="DANZI_YN"><input type="radio" name="DANZI_YN" id="DANZI_YN" value="Y" <c:if test="${INIT_DATA.detail.DANZI_YN eq 'Y' or INIT_DATA.detail.DANZI_YN eq ''}">checked="checked"</c:if> />&nbsp;&nbsp;단지있음&nbsp;</label>
												<label for="DANZI_YN"><input type="radio" name="DANZI_YN" id="DANZI_YN" value="N" <c:if test="${INIT_DATA.detail.DANZI_YN eq 'N' }">checked="checked"</c:if> />&nbsp;&nbsp;단지없음&nbsp;</label>
											</td>
										</tr>
										<tr>
											<th>지역</th>
											<td>
												<input type="text" name="AREA_NM" id="AREA_NM" title="지역" class="input w99p" value="${INIT_DATA.detail.AREA_NM}" maxlength="50"  placeholder="경기도 부천시"/>
											</td>
										</tr>
										<tr>
											<th>설명</th>
											<td>
												<textarea name="ETC" id="ETC" rows="" cols="" title="설명" style="width:99%; height:70px; letter-spacing:-0.5px; word-spacing:2px; line-height:24px;" class="input" maxlength="1000" placeholder="설명">${INIT_DATA.detail.ETC}</textarea>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
							<h3 class="title1 mt30">회원정보</h3>
							<div class="bbsView" >
								<table summary="입예협 사용자 관리">
									<caption>입예협 사용자 관리</caption>
									<colgroup>
										<col width="20%" />
										<col width="*" />
									</colgroup>
									<tbody>
										<tr>
											<th>아파트 코드</th>
											<td id="CODE_NM">
												<c:if test="${INIT_DATA.USER_ID eq ''}">
													<a href="javascript:fnCodeCreate();"  class="btn3 w60">생성하기</a>
												</c:if>
												<c:if test="${INIT_DATA.USER_ID ne ''}">
													${INIT_DATA.detail.APT_CODE}
												</c:if>
											</td>
										</tr>
										<tr>
											<th>ID</th>
											<td>
												<c:if test="${INIT_DATA.USER_ID eq ''}">
													<input type="text" name="FIRST_USER_ID" id="FIRST_USER_ID" title="ID" class="input w200" value="" maxlength="100"  placeholder="" onkeypress="fnChk();"/>
													&nbsp;&nbsp;<a href="javascript:fnDupChk();"  class="btn4 w60">중복조회</a>
												</c:if>
												<c:if test="${INIT_DATA.USER_ID ne ''}">
													${INIT_DATA.detail.USER_ID}
												</c:if>
											</td>
										</tr>
										<tr>
											<th>비밀번호</th>
											<td>
												<c:if test="${INIT_DATA.USER_ID eq ''}">
													<input type="text" name="USER_PWD" id="USER_PWD" title="최초 비밀번호" class="input w200" value="" maxlength="50"  placeholder="" />
												</c:if>
												<c:if test="${INIT_DATA.USER_ID ne ''}">
													<a href="javascript:fnResetPw();"  class="btn4 w120">비밀번호 초기화</a>
												</c:if>
											</td>
										</tr>
										<tr>
											<th>회원등급</th>
											<td>
												<label for="USER_TYPE"><input type="radio" name="USER_TYPE" id="USER_TYPE" value="AG" <c:if test="${INIT_DATA.detail.USER_TYPE eq 'AG' or INIT_DATA.detail.USER_TYPE eq ''}">checked="checked"</c:if> />&nbsp;&nbsp;일반&nbsp;</label>
												<label for="USER_TYPE"><input type="radio" name="USER_TYPE" id="USER_TYPE" value="NG" <c:if test="${INIT_DATA.detail.USER_TYPE eq 'NG'}">checked="checked"</c:if> />&nbsp;&nbsp;네이버&nbsp;</label>
												<label for="USER_TYPE"><input type="radio" name="USER_TYPE" id="USER_TYPE" value="TG" <c:if test="${INIT_DATA.detail.USER_TYPE eq 'TG'}">checked="checked"</c:if> />&nbsp;&nbsp;프리미엄&nbsp;</label>
											</td>
										</tr>
										<tr>
											<th>이름</th>
											<td>
												<input type="text" name="USER_NM" id="USER_NM" title="이름" class="input w99p" value="${INIT_DATA.detail.USER_NM}" maxlength="50"  placeholder="일루미스테이트"/>
											</td>
										</tr>
										<tr>
											<th>닉네임</th>
											<td>
												<input type="text" name="USER_NICK" id="USER_NICK" title="닉네임" class="input w99p" value="${INIT_DATA.detail.USER_NICK}" maxlength="50"  placeholder="일루미스테이트"/>
											</td>
										</tr>
										<tr>
											<th>문자발송 연락처</th>
											<td>
												<input type="text" name="SEND_TEL" id="SEND_TEL" title="문자발송 연락처" class="input w99p" value="${INIT_DATA.detail.SEND_TEL}" maxlength="50"  placeholder="문자발송 연락처"/>
											</td>
										</tr>
										<tr>
											<th>문자발송 금액 충전</th>
											<td>
												<input type="text" name="SEND_AMT" id="SEND_AMT" title="문자발송 금액 충전" class="input w250" value="" maxlength="50"  placeholder="숫자만 입력"/>
												<a href="javascript:fnSmsAmtAdd();"  class="btn4 w120">충전하기</a>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
							<div class="core_btn">
								<a href="javascript:fnSave();" class="mt30" style="background-color:#ff9900;">저장</a>
								<a href="javascript:fnList();" class="mt30" style="background-color:#a0a0a0;">목록</a>
							</div>
						</section>
					</form>
				</div>
			</li>
		</ul>
	</div>
	<!-- 메인 콘텐츠영역 끝 -->
	<!-- 하단영역 -->
	<%@ include file="/static_root/inc/admin_footer.jsp" %>
	<!-- 하단영역 끝 -->
	
</body>
</html>