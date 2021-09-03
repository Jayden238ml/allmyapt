<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<head>
<title>아파트관리 | 회비 관리</title>
<%@ include file="/static_root/inc/apt_top.jsp" %>
<script type="text/javascript">
	$(document).ready(function(){
		if("${INIT_DATA.detail.DONG}" != ""){
			fnGetHosu("${INIT_DATA.detail.DONG}", "H");
		}
	})
	
	function fnList(){
		$('#frm').attr("action", "/apt/apt_amtL.do");
		$('#frm').submit();
	}
	
	function fnGetHosu(val, type){
		$('#SELECT_TYPE').val(type);
		$.ajax({
			type: "POST",
			url : "/apt/getAmtDetailHosuList.do",
			data:  $("#frm").serialize(),
			dataType: "json",
			success: function (transport) {
				if(type == "H"){
					fnSetHosu(eval(transport.HosuList));
				}else if(type == "D"){
					fnSetDong(eval(transport.DongList));
				}
			}
		});
	}
	
	function fnSetDong(DongList){
		$("#DONG").html("");
		var resultHtml ="";
		var row =  "<option value=''>전체</option>";
		$.each(DongList,function(key,json) {
			row += '<option value="'+json.DONG+'">'+json.DONG+'동</option>';
		});
		resultHtml += row;
		$("#DONG").html(resultHtml);
		if("${INIT_DATA.detail.DONG}" != ""){
			$("#DONG").val("${INIT_DATA.detail.DONG}");
		}
	}
	function fnSetHosu(HosuList){
		$("#HOSU").html("");
		var resultHtml ="";
		var row =  "<option value=''>전체</option>";
		$.each(HosuList,function(key,json) {
			row += '<option value="'+json.HOSU+'">'+json.HOSU+'호</option>';
		});
		resultHtml += row;
		$("#HOSU").html(resultHtml);
		if("${INIT_DATA.detail.HOSU}" != ""){
			$("#HOSU").val("${INIT_DATA.detail.HOSU}");
		}
	}
	
	function fnModify(){
		if("${INIT_DATA.DANZI_YN}" == "Y"){
			if($('#DANZI').val() == ""){
				alert("단지를 선택해 주세요.");
				$('#DANZI').focus();
				return;
			}
		}
		if($('#DONG').val() == ""){
			alert("동을 선택해 주세요.");
			$('#DONG').focus();
			return;
		}
		if($('#HOSU').val() == ""){
			alert("단지를 선택해 주세요.");
			$('#HOSU').focus();
			return;
		}
		if(confirm("저장하시겠습니까?")){
			$.ajax({
				type: "POST",
				url  : "/apt/apt_AmtUpdate.do",
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
							<input type="hidden" name="AMT_SEQ" id="AMT_SEQ" value="${INIT_DATA.AMT_SEQ}" />
							<input type="hidden" name="AMT" id="AMT" value="${INIT_DATA.detail.AMT}" />
							<input type="hidden" name="AMT_IN_DATE" id="AMT_IN_DATE" value="${INIT_DATA.detail.AMT_IN_DATE}" />
							<input type="hidden" name="SELECT_TYPE" id="SELECT_TYPE" value="" />
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
										<c:if test="${INIT_DATA.DANZI_YN eq 'Y'}">
											<tr>
												<th>단지</th>
												<td>
													<select name="DANZI" id="DANZI" class="select w150" title="단지" onchange="fnGetHosu(this.value, 'D');">
														<option value="">전체</option>
														<c:forEach var="item" items="${INIT_DATA.DanziList}" >
															<option value="${item.DANZI}" <c:if test="${item.DANZI eq INIT_DATA.detail.DANZI}">selected="selected"</c:if> >${item.DANZI}단지</option>
														</c:forEach>
													</select>
												</td>
											</tr>
										</c:if>
										<tr>
											<th>동</th>
											<td>
												<select name="DONG" id="DONG" class="select w150" title="동" onchange="fnGetHosu(this.value, 'H');">
													<option value="">전체</option>
													<c:forEach var="item" items="${INIT_DATA.DongList}" >
														<option value="${item.DONG}" <c:if test="${item.DONG eq INIT_DATA.detail.DONG}">selected="selected"</c:if> >${item.DONG}동</option>
													</c:forEach>
												</select>
											</td>
										</tr>
										<tr>
											<th>호수</th>
											<td>
												<select name="HOSU" id="HOSU" class="select w150" title="호수">
													<option value="">전체</option>
												</select>
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