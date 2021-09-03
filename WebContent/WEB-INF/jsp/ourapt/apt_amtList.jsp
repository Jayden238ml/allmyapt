<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<head>
<title>아파트관리 | 회비관리</title>
<%@ include file="/static_root/inc/apt_top.jsp" %>
<script type="text/javascript">
	var PAGE_SIZE = "${INIT_DATA.PAGE_SIZE}"; 
	$(document).ready(function(){
		fncMakePageBody('${INIT_DATA.TOTAL_CNT}','${INIT_DATA.CURR_PAGE}');
		$('#frm').find("input").each(function(e){
	        $(this).bind("keyup",function(){
	                if(event.keyCode == 13){
	                	fnSearch();
	                }
	        });
	    });
		if("${INIT_DATA.SCH_DANZI}" != ""){
			fnSetHosu("${INIT_DATA.SCH_DANZI}", "D");
		}
		if("${INIT_DATA.SCH_DONG}" != ""){
			fnSetHosu("${INIT_DATA.SCH_DONG}", "H");
		}
	})
	
	//페이징
	var page_List = function(pageNo) {
	    $("#CURR_PAGE").val(pageNo);        
	    $('#frm').submit();
	};
	
	function fnSearch(){
		$('#frm').attr("action", "/apt/apt_amtL.do");
		$('#frm').submit();
	}
	
	function fnGetHosu(val, type){
		$('#SELECT_TYPE').val(type);
		$.ajax({
			type: "POST",
			url : "/apt/getHosuList.do",
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
		$("#SCH_DONG").html("");
		var resultHtml ="";
		var row =  "<option value=''>전체</option>";
		$.each(DongList,function(key,json) {
			row += '<option value="'+json.DONG+'">'+json.DONG+'동</option>';
		});
		resultHtml += row;
		$("#SCH_DONG").html(resultHtml);
		if("${INIT_DATA.SCH_DONG}" != ""){
			$("#SCH_DONG").val("${INIT_DATA.SCH_DONG}");
		}
	}
	function fnSetHosu(HosuList){
		$("#SCH_HOSU").html("");
		var resultHtml ="";
		var row =  "<option value=''>전체</option>";
		$.each(HosuList,function(key,json) {
			row += '<option value="'+json.HOSU+'">'+json.HOSU+'호</option>';
		});
		resultHtml += row;
		$("#SCH_HOSU").html(resultHtml);
		if("${INIT_DATA.SCH_HOSU}" != ""){
			$("#SCH_HOSU").val("${INIT_DATA.SCH_HOSU}");
		}
	}
	
	function fnExcelPop(){
		gfnOpenLayerPopup('/common/index.do?jpath=/ourapt/pop_amtExcelUpload');
	}
	
	
	function fnDetail(seq){
		$('#AMT_SEQ').val(seq);
		$('#frm').attr("action", "/apt/apt_amtDetail.do");
		$('#frm').submit();
	}
	
	function fn_Del(seq){
		$('#AMT_SEQ').val(seq);
		if(confirm("삭제하시겠습니까?")){
			$.ajax({
				type: "POST",
				url  : "/apt/myAmtDelete.do",
				data:  $("#frm").serialize(),
				dataType: "json",
				success: function (transport){
					if(transport.ERROR_CD == "999"){
						alert("처리 중 오류가 발생 하였습니다.");
						return;
					}else{
						alert("삭제 되었습니다.");
						fnSearch();
					}
				}
			});
		}
	}
	
	function fnExcelDown(){
		$('#frm').attr("target", "_self");
		$('#frm').attr("action", "/apt/AmtExcelDown.do");
		$('#frm').submit();
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
					<h3 class="subTitle">[${INIT_DATA.detail.APT_NM}]&nbsp;회비관리</h3>
				</div>
				<!-- 타이틀/네비 끝 -->

				<!-- 본문내용 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// -->
				<div id="cont">
					<section class="sect_area sect_first_area">
						<!-- 검색 -->
						<form name="frm" id="frm" method="post" action="#">
							<input type="hidden" name="TMC" id="TMC" value="${INIT_DATA.TMC}" />
							<input type="hidden" name="LMC" id="LMC" value="${INIT_DATA.LMC}" />
							<input type="hidden" name="CURR_PAGE" id="CURR_PAGE" value="${INIT_DATA.CURR_PAGE}" />
							<input type="hidden" name="AMT_SEQ" id="AMT_SEQ" value="" />
							<fieldset>
								<div class="searchUser">
									<ul>
										<c:if test="${INIT_DATA.DANZI_YN eq 'Y'}">
											<li class="scharea3">
										</c:if>
										<c:if test="${INIT_DATA.DANZI_YN eq 'N'}">
											<li class="scharea2">
										</c:if>
											<ul>
												<c:if test="${INIT_DATA.DANZI_YN eq 'Y'}">
													<li>
														<label for="SCH_DANZI">단지</label>
														<select name="SCH_DANZI" id="SCH_DANZI" class="select" title="단지" onchange="fnGetHosu(this.value, 'D');">
															<option value="">전체</option>
															<c:forEach var="item" items="${INIT_DATA.DanziList}" >
																<option value="${item.DANZI}" <c:if test="${item.DANZI eq INIT_DATA.SCH_DANZI}">selected="selected"</c:if> >${item.DANZI}단지</option>
															</c:forEach>
														</select>
													</li>
												</c:if>
												<li>
													<label for="SCH_DONG">동</label>
													<select name="SCH_DONG" id="SCH_DONG" class="select" title="동" onchange="fnGetHosu(this.value, 'H');">
														<option value="">전체</option>
														<c:forEach var="item" items="${INIT_DATA.DongList}" >
															<option value="${item.DONG}" <c:if test="${item.DONG eq INIT_DATA.SCH_DONG}">selected="selected"</c:if> >${item.DONG}동</option>
														</c:forEach>
													</select>
												</li>
												<li>
													<label for="SCH_HOSU">호수</label>
													<select name="SCH_HOSU" id="SCH_HOSU" class="select" title="호수">
														<option value="">전체</option>
													</select>
												</li>
											</ul>
										</li>
										<li class="scharea2">
											<ul>
												<li style="width:20%;">
													<label class="SCH_WARRANT_YN">위임장납부</label>
													<select name="SCH_WARRANT_YN" id="SCH_WARRANT_YN" title="위임장납부" class="select">
														<option value="">전체</option>
														<option value="Y" <c:if test="${INIT_DATA.SCH_WARRANT_YN eq 'Y'}">selected="selected"</c:if> >납부</option>
														<option value="N" <c:if test="${INIT_DATA.SCH_WARRANT_YN eq 'N'}">selected="selected"</c:if> >미납부</option>
													</select>
												</li>
												<li style="width:79%;">
													<label class="SCH_AMT_YN">회비납부</label>
													<ul>
														<li class="area_small ml0">
															<select name="SCH_AMT_YN" id="SCH_AMT_YN" title="회비납부" class="select">
																<option value="">전체</option>
																<option value="Y" <c:if test="${INIT_DATA.SCH_AMT_YN eq 'Y'}">selected="selected"</c:if> >납부</option>
																<option value="N" <c:if test="${INIT_DATA.SCH_AMT_YN eq 'N'}">selected="selected"</c:if> >미납부</option>
															</select>
														</li>
														<li class="area_large"><input type="text" name="SCH_WORD" id="SCH_WORD"  title="검색어" class="input" value="${INIT_DATA.SCH_WORD}" placeholder="입금자명"/></li>
													</ul>
												</li>
											</ul>
										</li>
									</ul>
									<div><a href="#" onclick="fnSearch(); return false;">SEARCH</a></div>
								</div>
							</fieldset>
						</form>
						<!-- 검색 끝 -->
						<dl class="titleEaB">
							<dt>
								총 <strong><c:out value="${INIT_DATA.TOTAL_CNT}"/></strong> 건&nbsp;&nbsp;&nbsp; | &nbsp;&nbsp;&nbsp; 
								전체 금액 : <strong> <fmt:formatNumber value="${INIT_DATA.TOTAL_AMT}" pattern="#,###"/></strong> 원
							</dt>
							<dd>
								<c:if test="${INIT_DATA.ISMOBILE eq 'P'}">
									<a href="javascript:fnExcelPop();" class="mBtn3">회비 내역 업로드</a>
									<a href="javascript:fnExcelDown();" class="mBtn4">내역다운로드</a>
								</c:if>
							</dd> 
						</dl>
						<!-- 리스트 -->
						<div class="bbsScrollSm">
							<table summary="회비 관리">
								<caption>회비 관리</caption>
								<colgroup>
									<col width="60" />
									<col width="60" />
									<col width="80" />
									<col width="100" />		
									<col width="100" />
									<col width="60" />
								</colgroup>
								<thead>
									<tr>
										<th>동</th>
										<th>호수</th>
										<th>입금자명</th>
										<th>입금일자</th>
										<th>입금액</th>
										<th>관리</th>
									</tr>
								</thead>
								<tbody>
									<c:if test="${not empty INIT_DATA.resultList}">
										<c:forEach items="${INIT_DATA.resultList}" var="item" varStatus="status">
											<tr>
												<td><a href="javascript:fnDetail('${item.AMT_SEQ}');">${item.DONG}</a></td>
												<td><a href="javascript:fnDetail('${item.AMT_SEQ}');">${item.HOSU}</a></td>
												<td><a href="javascript:fnDetail('${item.AMT_SEQ}');">${item.USER_NM}</a></td>
												<td><a href="javascript:fnDetail('${item.AMT_SEQ}');">${item.AMT_IN_DATE}</a></td>
												<td><a href="javascript:fnDetail('${item.AMT_SEQ}');"><fmt:formatNumber value="${item.AMT}" pattern="#,###"/> 원 </a></td>
												<td><a href="javascript:fn_Del('${item.AMT_SEQ}');" class="btn4 w50">삭제</a></td>
											</tr>
										</c:forEach>
									</c:if>
									<c:if test="${empty INIT_DATA.resultList}">
										<tr>
											<td class="no-list" colspan="6">검색결과가 없습니다.</td>
										</tr>
									</c:if>
								</tbody>
							</table>
						</div>
						<p class="textPoint mbArea mt5">스크롤하실 수 있습니다.</p>
						<div class="listPaging mt30" id="paging_bar"></div>
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