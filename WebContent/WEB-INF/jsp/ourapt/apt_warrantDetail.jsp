<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<head>
<title>아파트관리 | 위임장 관리</title>
<%@ include file="/static_root/inc/apt_top.jsp" %>
<script type="text/javascript">
	$(document).ready(function(){
		
	})
	
	
	function fnList(){
		$('#frm').attr("action", "/apt/apt_warrant.do");
		$('#frm').submit();
	}
	
	function fnModify(){
		if($('#USER_NM').val() == ""){
			alert("성명을 입력해 주세요.");
			$('#USER_NM').focus();
			return;
		}
		if($('#HP').val() == ""){
			alert("핸드폰번호를 입력해 주세요.");
			$('#HP').focus();
			return;
		}
		if(confirm("저장하시겠습니까?")){
			$.ajax({
				type: "POST",
				url  : "/apt/apt_WarrantInfo_Insert.do",
				data:  $("#frm").serialize(),
				dataType: "json",
				success: function (transport){
					if(transport.ERROR_CD == "999"){
						alert("처리 중 오류가 발생 하였습니다.");
						return;
					}else{
						if(confirm("저장되었습니다.목록으로 이동하시겠습니까?")){
							fnList();
						}
					}
				}
			});
		}
	}
	
	
</script>
</head>
<body id="user">
	<div id="wrap">
		<!-- 상단영역 -->
		<%@ include file="/static_root/inc/apt_header.jsp" %>
		<!-- 상단영역 끝 -->

		<ul id="content">
			<li id="left">
				<!-- 좌측영역 -->
				<%@ include file="/static_root/inc/apt_left.jsp" %>
				<!-- 좌측영역 끝 -->
			</li>
			<li id="right">
				<!-- 타이틀/네비 -->
				<div class="titleNaviNew">
					<h3 class="subTitle">[${INIT_DATA.aptMap.APT_NM}]&nbsp;입주자 정보 상세</h3>
				</div>
				<!-- 타이틀/네비 끝 -->

				<!-- 본문내용 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// -->
				<div id="cont">
					<section class="sect_area sect_first_area">
						<!-- 검색 -->
						<form name="frm" id="frm" method="post" action="#">
							<input type="hidden" name="TMC" id="TMC" value="${INIT_DATA.TMC}" />
							<input type="hidden" name="LMC" id="LMC" value="${INIT_DATA.LMC}" />
							<input type="hidden" name="WARRANT_SEQ" id="WARRANT_SEQ" value="${INIT_DATA.WARRANT_SEQ}" />
							<input type="hidden" name="DONG" id="DONG" value="${INIT_DATA.DONG}" />
							<input type="hidden" name="HOSU" id="HOSU" value="${INIT_DATA.HOSU}" />
							<input type="hidden" name="CURR_PAGE" id="CURR_PAGE" value="${INIT_DATA.CURR_PAGE}" />
							<input type="hidden" name="SCH_DANZI" id="SCH_DANZI" value="${INIT_DATA.SCH_DANZI}" />
							<input type="hidden" name="SCH_DONG" id="SCH_DONG" value="${INIT_DATA.SCH_DANZI}" />
							<input type="hidden" name="SCH_HOSU" id="SCH_HOSU" value="${INIT_DATA.SCH_HOSU}" />
							<input type="hidden" name="SCH_WARRANT_YN" id="SCH_WARRANT_YN" value="${INIT_DATA.SCH_WARRANT_YN}" />
							<input type="hidden" name="SCH_WORD" id="SCH_WORD" value="${INIT_DATA.SCH_WORD}" />
							<div class="bbsView" >
								<table summary="입주자 상세">
									<caption>입주자 상세</caption>
									<colgroup>
										<col width="20%" />
										<col width="*" />
									</colgroup>
									<tbody>
										<tr>
											<th>동/호수</th>
											<td>
												<c:out value="${INIT_DATA.detail.DONG}"/>동 - <c:out value="${INIT_DATA.detail.HOSU}"/>호
											</td>
										</tr>
										<tr>
											<th>위임장 제출여부</th>
											<td>
												<label for="WARRANT_YN"><input type="radio" name="WARRANT_YN" id="AMT_YN" value="Y" <c:if test="${INIT_DATA.detail.WARRANT_YN eq 'Y' }">checked="checked"</c:if> />&nbsp;&nbsp;제출&nbsp;</label>
												<label for="WARRANT_YN"><input type="radio" name="WARRANT_YN" id="AMT_YN" value="N" <c:if test="${INIT_DATA.detail.WARRANT_YN eq 'N' or INIT_DATA.detail.WARRANT_YN eq '' }">checked="checked"</c:if> />&nbsp;&nbsp;미제출&nbsp;</label>
											</td>
										</tr>
										<c:if test="${INIT_DATA.JOHAP_YN  eq 'Y'}">
											<tr>
												<th>조합원 여부</th>
												<td>
													<label for="JOHAP_YN"><input type="radio" name="JOHAP_YN" id="JOHAP_YN" value="Y" <c:if test="${INIT_DATA.detail.JOHAP_YN eq 'Y' }">checked="checked"</c:if> />&nbsp;&nbsp;조합&nbsp;</label>
													<label for="JOHAP_YN"><input type="radio" name="JOHAP_YN" id="JOHAP_YN" value="N" <c:if test="${INIT_DATA.detail.JOHAP_YN eq 'N' or INIT_DATA.detail.JOHAP_YN eq '' }">checked="checked"</c:if> />&nbsp;&nbsp;일반 분양자&nbsp;</label>
												</td>
											</tr>
										</c:if>
										<tr>
											<th>회비납부 여부</th>
											<td>
												<label for="AMT_YN"><input type="radio" name="AMT_YN" id="AMT_YN" value="Y" <c:if test="${INIT_DATA.detail.AMT_YN eq 'Y' }">checked="checked"</c:if> />&nbsp;&nbsp;납부&nbsp;</label>
												<label for="AMT_YN"><input type="radio" name="AMT_YN" id="AMT_YN" value="N" <c:if test="${INIT_DATA.detail.AMT_YN eq 'N' or INIT_DATA.detail.AMT_YN eq '' }">checked="checked"</c:if> />&nbsp;&nbsp;미납부&nbsp;</label>
											</td>
										</tr>
										<tr>
											<th>성명</th>
											<td>
												<input type="text" name="USER_NM" id="USER_NM" title="성명" class="input w99p" value="${INIT_DATA.detail.USER_NM}" maxlength="50"  placeholder="계약자 명"/>
											</td>
										</tr>
										<tr>
											<th>공동명의자명</th>
											<td>
												<input type="text" name="USER_NM2" id="USER_NM2" title="공동명의자명" class="input w99p" value="${INIT_DATA.detail.USER_NM2}" maxlength="50"  placeholder="공동명의자명"/>
											</td>
										</tr>
										<tr>
											<th>핸드폰번호</th>
											<td>
												<input type="text" name="HP" id="HP" title="핸드폰번호" class="input w99p" value="${INIT_DATA.detail.HP}" maxlength="50"  placeholder="핸드폰번호"/>
											</td>
										</tr>
										<tr>
											<th>공명자 연락처</th>
											<td>
												<input type="text" name="HP2" id="HP2" title="공명자 연락처" class="input w99p" value="${INIT_DATA.detail.HP2}" maxlength="50"  placeholder="공명자 연락처"/>
											</td>
										</tr>
										<tr>
											<th>주소</th>
											<td>
												<input type="text" name="ADDR" id="ADDR" title="주소" class="input w99p" value="${INIT_DATA.detail.ADDR}" maxlength="50"  placeholder="주소"/>
											</td>
										</tr>
										<tr>
											<th>비고</th>
											<td>
												<textarea name="ETC" id="ETC" rows="" cols="" title="문의내용" style="width:99%; height:100px; letter-spacing:-0.5px; word-spacing:2px; line-height:24px;" class="input" maxlength="2000" placeholder="비고 내용">${INIT_DATA.detail.ETC}</textarea>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
							<div class="core_btn">
								<a href="javascript:fnModify();" class="mt30" style="background-color:#ff9900;">저장</a>
								<a href="javascript:fnList();" class="mt30" style="background-color:#ababab;">목록</a>
							</div>
						</form>
					</section>
				</div>
				<!-- 본문내용 끝 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// -->
			</li>
		</ul>

		<!-- 하단영역 -->
		<%@ include file="/static_root/inc/apt_footer.jsp" %>
		<!-- 하단영역 끝 -->
	</div>
</body>
</html>