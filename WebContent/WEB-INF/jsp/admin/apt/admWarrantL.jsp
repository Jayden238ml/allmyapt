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
		$('#frm').attr("action", "/amc/amcDongHosuL.do");
		$('#frm').submit();
	}
	
	function fnPop(){
		gfnOpenLayerPopup('/amc/amcDongHosuPop.do?TYPE=P');	
	}
	
	function fnNaverCafe(){
// 		$('#frm').attr("action", "/amc/amcNaverCafeMem.do");
// 		$('#frm').submit();
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
					<h3 class="subTitle">아파트 동/호수 관리</h3>
				</div>
				<!-- 타이틀/네비 끝 -->
				<div id="cont">
					<section class="sect_area sect_first_area">
						<!-- 검색 -->
						<form name="frm" id="frm" method="post" action="#">
							<input type="hidden" name="A_TMC" id="A_TMC" value="${INIT_DATA.A_TMC}" />
							<input type="hidden" name="A_LMC" id="A_LMC" value="${INIT_DATA.A_LMC}" />
							<input type="hidden" name="CURR_PAGE" id="CURR_PAGE" value="${INIT_DATA.CURR_PAGE}" />
							<input type="hidden" id="USER_ID"  name="USER_ID" value=""/>
							<fieldset>
								<div class="searchCmn">
									<label for="SCH_TYPE">검색분류</label>
									<div class="search_cmn_wrap">
										<ul>
											<li class="twoFL">
												<label for="SCH_TYPE" class="hidden">검색분류</label>
												<select name="SCH_TYPE" id="SCH_TYPE" class="select" title="검색분류">
													<option value="A" <c:if test="${INIT_DATA.SCH_TYPE eq 'A' }">selected</c:if>>단지명</option>
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
							<dt><strong>*최초 1회만 생성함. 생성 이후 수정하면 안됨</strong></dt>
							<dd>
								<c:if test="${INIT_DATA.SESSION_USER_ID ne ''}">
									<a href="javascript:fnPop();" class="mBtn4">등록</a>
								</c:if>
							</dd>
						</dl>
						<!-- 리스트 -->
						<div class="bbsScrollSm">
							<table summary="아파트회원관리">
								<caption>아파트회원관리</caption>
								<colgroup>
									<col width="40" />
									<col width="150" />
									<col width="200" />
									<col width="60" />		
								</colgroup>
								<thead>
									<tr>
										<th>번호</th>
										<th>단지명</th>
										<th>동/호수 정보</th>
										<th>관리</th>
									</tr>
								</thead>
								<tbody>
									<c:if test="${not empty INIT_DATA.resultList}">
										<c:forEach items="${INIT_DATA.resultList}" var="item" varStatus="status">
											<tr>
												<td>${item.RNUMUM}</td>
												<td>${item.APT_NM}</td>
												<td>${item.HOSU_CNT} 세대</td>
												<td>
													<a href="javascript:fnNaverCafe();"  class="btn3 w40">카페</a>
												</td>
											</tr>
										</c:forEach>
									</c:if>
									<c:if test="${empty INIT_DATA.resultList}">
										<tr>
											<td class="no-list" colspan="4">등록된 아파트 정보가 없습니다.</td>
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