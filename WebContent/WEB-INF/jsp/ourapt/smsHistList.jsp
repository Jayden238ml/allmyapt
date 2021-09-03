<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<head>
<title>아파트관리 | 문자발송</title>
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
	})
	
	//페이징
	var page_List = function(pageNo) {
	    $("#CURR_PAGE").val(pageNo);        
	    $('#frm').submit();
	};
	
	function fnSearch(){
		$('#frm').attr("action", "/apt/smsSendHist.do");
		$('#frm').submit();
	}
	
	
	function fnExcelDown(){
		$('#frm').attr("target", "_self");
		$('#frm').attr("action", "/apt/SmsHistExcelDown.do");
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
					<h3 class="subTitle">입금/발송이력</h3>
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
							<input type="hidden" name="NOW_AMT" id="NOW_AMT" value="${INIT_DATA.NOW_AMT}" />
							<input type="hidden" name="SELECT_TYPE" id="SELECT_TYPE" value="" />
							<input type="hidden" name="WARRANT_SEQ" id="WARRANT_SEQ" value="" />
							<input type="hidden" name="DONG" id="DONG" value="" />
							<input type="hidden" name="HOSU" id="HOSU" value="" />
							<input type="hidden" name="TYPE" id="TYPE" value="" />
							<fieldset>
								<div class="searchUser">
									<ul>
										<li class="scharea2">
											<ul>
												<li style="width:99%;">
													<label class="SCH_INOUT_GUBUN">입/출금</label>
													<select name="SCH_INOUT_GUBUN" id="SCH_INOUT_GUBUN" title="입/출금" class="select">
														<option value="">전체</option>
														<option value="IN" <c:if test="${INIT_DATA.SCH_INOUT_GUBUN eq 'IN'}">selected="selected"</c:if> >입금</option>
														<option value="OUT" <c:if test="${INIT_DATA.SCH_INOUT_GUBUN eq 'OUT'}">selected="selected"</c:if> >출금</option>
													</select>
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
							<dd>
								<a href="javascript:fnExcelDown();" class="mBtn3">엑셀 다운로드</a>
							</dd> 
						</dl>
						<!-- 리스트 -->
						<div class="bbsScrollSm">
							<table summary="위임장 관리">
								<caption>위임장 관리</caption>
								<colgroup>
									<col width="80" />
									<col width="80" />
									<col width="80" />		
									<col width="80" />		
								</colgroup>
								<thead>
									<tr>
										<th>구분</th>
										<th>금액</th>
										<th>일자</th>
										<th>입금/발송건수</th>
									</tr>
								</thead>
								<tbody>
									<c:if test="${not empty INIT_DATA.resultList}">
										<c:forEach items="${INIT_DATA.resultList}" var="item" varStatus="status">
											<tr>
												<td>${item.TIT_GUBUN}</td>
												<td><fmt:formatNumber value="${item.AMT}" pattern="#,###"/>원</td>
												<td>${item.REGDATE}</td>
												<td>${item.SEND_CNT}</td>
											</tr>
										</c:forEach>
									</c:if>
									<c:if test="${empty INIT_DATA.resultList}">
										<tr>
											<td class="no-list" colspan="4">검색결과가 없습니다.</td>
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