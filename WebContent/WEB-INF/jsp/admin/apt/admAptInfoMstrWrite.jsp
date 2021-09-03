<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/static_root/inc/admin_head.jsp" %>
</head>
<script type="text/javascript">	
	$(document).ready(function(){
	    if("${INIT_DATA.detail.AREA_SIDO}" != ""){
	    	fnGetGunGu("${INIT_DATA.detail.AREA_SIDO}");
	    }
	});
	
	
	
	function fnList(){
		$('#Ajaxfrm').attr("action", "/amc/amcAptInfoL.do");
		$('#Ajaxfrm').submit();
	}
	
	function saveContent(){
		if($('#AREA_SIDO').val() == ""){
			alert("지역을 선택해 주세요.");
			$('#AREA_SIDO').focus();
			return false;
		}
		if($('#AREA_SIGUNGU').val() == ""){
			alert("지역을 선택해 주세요.");
			$('#AREA_SIGUNGU').focus();
			return false;
		}
		if($('#APT_BOARD_TYPE').val() == ""){
			alert("구분을 선택해 주세요.");
			$('#APT_BOARD_TYPE').focus();
			return false;
		}
		if($('#APT_NM').val() == ""){
			alert("아파트명을 입력해 주세요.");
			$('#APT_NM').focus();
			return false;
		}
		if($('#ZIP_NO').val() == ""){
			alert("주소를 입력해 주세요.");
			$('#ZIP_NO').focus();
			return false;
		}
		if(confirm("저장하시겠습니까?")){
			$('#frm').attr("action", "/amc/amcAptInfoMstrInsert.do");
			$('#frm').submit();
		}
	}
	
	
	//우편번호
	function fnPostSearch() {
		gfnOpenLayerPopup('/common/addrSearch.do?POP_OPEN_YN=Y');
	}
	
	function fnSetPostNum(zip1,zip2,addr){
		$('#ZIP_NO').val(zip1 + zip2);
		$('#ADDR1').val(addr);
	}
	
	function fnGetGunGu(val){
		$('#TMP_AREA_SIDO').val(val);
		$.ajax({
			type: "POST",
			url : "/amc/getAreaSiGunGuList.do",
			data:  $("#Ajaxfrm").serialize(),
			dataType: "json",
			success: function (transport) {
				fnSetSiGunGu(eval(transport.SiGunGuList));
			}
		});
	}
	
	function fnSetSiGunGu(SiGunGuList){
		$("#AREA_SIGUNGU").html("");
		var resultHtml ="";
		var row =  "<option value=''>선택</option>";
		$.each(SiGunGuList,function(key,json) {
			row += '<option value="'+json.CODE+'">'+json.CODENM+'</option>';
		});
		resultHtml += row;
		$("#AREA_SIGUNGU").html(resultHtml);
		if("${INIT_DATA.detail.AREA_SIGUNGU}" != ""){
			$("#AREA_SIGUNGU").val("${INIT_DATA.detail.AREA_SIGUNGU}");
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
					<h3 class="subTitle">단지/세대/평면도 관리</h3>
				</div>
				<!-- 타이틀/네비 끝 -->
				<div id="cont">
					<form name="Ajaxfrm" id="Ajaxfrm" method="post" action="#">
						<input type="hidden" name="A_TMC" id="A_TMC" value="${INIT_DATA.A_TMC}" />
						<input type="hidden" name="A_LMC" id="A_LMC" value="${INIT_DATA.A_LMC}" />
						<input type="hidden" name="TMP_AREA_SIDO" id="TMP_AREA_SIDO" value="" />
					</form>
					<form name="frm" id="frm" method="post" action="/amc/amcAptInfoMstrInsert.do">
						<input type="hidden" name="A_TMC" id="A_TMC" value="${INIT_DATA.A_TMC}" />
						<input type="hidden" name="A_LMC" id="A_LMC" value="${INIT_DATA.A_LMC}" />
						<input type="hidden" name="MST_BOARD_SEQ" id="MST_BOARD_SEQ" value="${INIT_DATA.MST_BOARD_SEQ}" />
						<section class="sect_area sect_first_area">
							
							<div class="bbsView">
								<table summary="단지/세대/평면도 관리">
									<caption>단지/세대/평면도 관리</caption>
									<colgroup>
										<col width="20%" />
										<col width="*" />
									</colgroup>
									<tbody>
										<tr>
											<th>지역</th>
											<td>
												<select name="AREA_SIDO" id="AREA_SIDO" class="select w100" title="지역" onchange="fnGetGunGu(this.value);">
													<option value="">선택</option>
													<c:forEach var="item" items="${INIT_DATA.SidoList}" >
														<option value="${item.CODE}" <c:if test="${item.CODE eq INIT_DATA.detail.AREA_SIDO}">selected="selected"</c:if> >${item.CODENM}</option>
													</c:forEach>
												</select>&nbsp;&nbsp;
												<select name="AREA_SIGUNGU" id="AREA_SIGUNGU" class="select w150" title="지역" >
													<option value="">선택</option>
												</select>
											</td>
										</tr>
										<tr>
											<th>아파트명</th>
											<td>
												<input type="text" name="APT_NM" id="APT_NM" value="<c:out value='${INIT_DATA.detail.APT_NM}'/>" class="input w99p" title="제목" maxlength="100"  />
											</td>
										</tr>
										<tr>
											<th>건설사</th>
											<td>
												<select name="APT_ERECT_CODE" id="APT_ERECT_CODE" class="select w150" title="구분">
													<option value="">선택</option>
													<c:forEach var="item" items="${INIT_DATA.erecList}" >
														<option value="${item.CODE}" <c:if test="${item.CODE eq INIT_DATA.detail.APT_ERECT_CODE}">selected="selected"</c:if> >${item.CODENM}</option>
													</c:forEach>
												</select>
											</td>
										</tr>
										<tr>
											<th>주소</th>
											<td>
												<input type="text" name="ZIP_NO" id="ZIP_NO" title="우편번호" class="input w80" required="required" readonly="readonly" value='<c:out value="${ INIT_DATA.detail.ZIP_NO }" />'/>&nbsp;
												<a href="javascript:fnPostSearch();" class="btn4 w60">주소검색</a>&nbsp;
												<input type="text" name="ADDR1" id="ADDR1" title="주소" class="input w300" required="required" readonly="readonly" value='<c:out value="${ INIT_DATA.detail.ADDR1 }" />' />
											</td>
										</tr>
									</tbody>
								</table>
							</div>
							<dl class="bvBtn">
								<dt>&nbsp;</dt>
								<dd>
									<a href="javascript:saveContent();" class="mBtn1">저장</a>
									<a href="javascript:fnList();" class="mBtn3">취소</a>
								</dd>
							</dl>
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