<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<%@ include file="/static_root/inc/admin_head.jsp" %>
</head>
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
	});
	
	//페이징
	var page_List = function(pageNo) {
	    $("#CURR_PAGE").val(pageNo);        
	    $('#frm').submit();
	};
	
	function fnSearch(){
		$('#frm').attr("action", "/user/apt_info1L.do");
		$('#frm').submit();
	}
	
	function fnCreate(){
		$('#frm').attr("action", "/amc/amcAptMstrInfoWrite.do");
		$('#frm').submit();
	}
	
	function fnMstrDetail(seq){
		$('#MST_BOARD_SEQ').val(seq);
		$('#frm').attr("action", "/amc/amcAptMstrInfoWrite.do");
		$('#frm').submit();
	}
	
	function fnDetail(mst_seq, seq, type){
		$('#MST_BOARD_SEQ').val(mst_seq);
		$('#BOARD_SEQ').val(seq);
		$('#APT_BOARD_TYPE').val(type);
		$('#frm').attr("action", "/amc/amcAptInfoWrite.do");
		$('#frm').submit();
	}
	
	function fnDelete(seq) {
		$('#MST_BOARD_SEQ').val(seq);
		if(confirm("삭제하시겠습니까?")){
			$.ajax({
				type: "POST",
				url : "/amc/AptBoardDelete.do",
				data:  $("#frm").serialize(),
				dataType: "json",
				success: function (transport) {
					if(transport.ERROR_CD == "999"){
						alert("삭제하다 에러발생.");
						return;
					}else{
						alert("삭제되었습니다.");
						fnSearch();
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
					<h3 class="subTitle">단지/세대/평면도 관리</h3>
				</div>
				<!-- 타이틀/네비 끝 -->
				<div id="cont">
					<section class="sect_area sect_first_area">
						<!-- 검색 -->
						<form name="frm" id="frm" method="post" action="#">
							<input type="hidden" name="A_TMC" id="A_TMC" value="${INIT_DATA.A_TMC}" />
							<input type="hidden" name="A_LMC" id="A_LMC" value="${INIT_DATA.A_LMC}" />
							<input type="hidden" name="CURR_PAGE" id="CURR_PAGE" value="${INIT_DATA.CURR_PAGE}" />
							<input type="hidden" name="MST_BOARD_SEQ" id="MST_BOARD_SEQ" value="" />
							<input type="hidden" name="APT_BOARD_TYPE" id="APT_BOARD_TYPE" value="" />
							<fieldset>
								<div class="searchCmn">
									<label for="SCH_TYPE">검색분류</label>
									<div class="search_cmn_wrap">
										<ul>
											<li class="twoFL">
												<label for="SCH_TYPE" class="hidden">검색분류</label>
												<select name="SCH_TYPE" id="SCH_TYPE" class="select" title="검색분류">
													<option value="" >전체</option>
													<option value="A" <c:if test="${INIT_DATA.SCH_TYPE eq 'A' }">selected</c:if>>지역</option>
													<option value="B" <c:if test="${INIT_DATA.SCH_TYPE eq 'B' }">selected</c:if>>세대명</option>
												</select>
											</li>
											<li class="twoFR">
												<label for="SCH_WORD" class="hidden">검색어</label>
												<input type="text" maxlength="50" name="SCH_WORD" id="SCH_WORD" title="검색어" class="input" value="${INIT_DATA.SCH_WORD }"/>
											</li>
										</ul>
										<a href="javascript:fnSearch();"><span><i class="fa fa-search" aria-hidden="true"></i></span></a>
									</div>
								</div>
							</fieldset>
						</form>
						<!-- 검색 끝 -->
						<dl class="titleEaB">
							<dt>총 <strong><c:out value="${INIT_DATA.TOTAL_CNT}"/></strong> 건</dt>
							<dd>
								<c:if test="${INIT_DATA.SESSION_USER_ID ne ''}">
									<a href="javascript:fnCreate();" class="mBtn4">등록</a>
								</c:if>
							</dd>
						</dl>
						<!-- 리스트 -->
						<div class="bbsScrollSm">
							<table summary="단지/세대/평면도 관리">
								<caption>단지/세대/평면도 관리</caption>
								<colgroup>
									<col width="30" />
									<col width="70" />
									<col width="120" />		
									<col width="30" />		
									<col width="30" />		
									<col width="40" />		
									<col width="130" />		
								</colgroup>
								<thead>
									<tr>
										<th>번호</th>
										<th>지역</th>
										<th>아파트명</th>
										<th>조회수</th>
										<th>삭제여부</th>
										<th>등록일</th>
										<th>관리</th>
									</tr>
								</thead>
								<tbody>
									<c:if test="${not empty INIT_DATA.resultList}">
										<c:forEach items="${INIT_DATA.resultList}" var="item" varStatus="status">
											<tr>
												<td>${item.RNUMUM}</td>
												<td>${item.SIDO_NM} - ${item.SIGUNGU_NM}</td>
												<td><a href="javascript:fnMstrDetail('${item.MST_BOARD_SEQ}');">${item.APT_NM}</a></td>
												<td>${item.VIEW_CNT}</td>
												<td>${item.DEL_YN}</td>
												<td>${item.REGDATE}</td>
												<td>
													<a href="javascript:fnDetail('${item.MST_BOARD_SEQ}', '${item.BOARD_SEQ01}', '01');"  class="btn1 w50">단지</a>&nbsp;
													<a href="javascript:fnDetail('${item.MST_BOARD_SEQ}', '${item.BOARD_SEQ02}', '02');"  class="btn3 w50">세대</a>&nbsp;
													<a href="javascript:fnDetail('${item.MST_BOARD_SEQ}', '${item.BOARD_SEQ03}', '03');"  class="btn6 w80">평면도</a> &nbsp;  
													<a href="javascript:fnDelete('${item.MST_BOARD_SEQ}');"  class="btn4 w40">삭제</a>
												</td>
											</tr>
										</c:forEach>
									</c:if>
									<c:if test="${empty INIT_DATA.resultList}">
										<tr>
											<td class="no-list" colspan="7">등록된 데이터가 없습니다.</td>
										</tr>
									</c:if>
								</tbody>
							</table>
						</div>
						<p class="textPoint mbArea mt5">스크롤하실 수 있습니다.</p>
						
						<div class="listPaging mt30" id="paging_bar"></div>
					</section>
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