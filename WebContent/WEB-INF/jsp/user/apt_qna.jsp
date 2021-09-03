<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	<meta name="title" content="아파트관리" />
	<meta name="description" content="아파트,입예협,입주자관리,입주예정자협의회,시세조회,분양가조회, 실거래가 비교, 분양가 비교" />
	<meta name="keywords" content="아파트,입예협,입주자관리,입주예정자협의회,시세조회,분양가조회, 실거래가 비교, 분양가 비교" />
	<meta name="robots" content="index, follow">
	<link rel="canonical" href="http://www.allmyapt.com">
	<meta property="og:type" content="article" />
	<meta property="og:site_name" content="아파트관리">
	<meta property="og:type" content="article">
	<meta property="og:url" content="http://www.allmyapt.com">
	<meta property="og:title" content="아파트정보 | 입주자관리 | 실거래가 조회 | 거래가 비교">
	<meta property="og:image" content="/static_root/images/common/meta_img.jpg">
	<meta property="og:description" content="아파트관리 " />
	
	<title>아파트관리 | Q&A</title>
<%@ include file="/static_root/inc/usr_top.jsp" %>
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
		$('#frm').attr("action", "/user/apt_qnal.do");
		$('#frm').submit();
	}
	
	function fnWrite(){
		$('#frm').attr("action", "/user/apt_qnaw.do");
		$('#frm').submit();
	}
	
	function fnDetail(seq){
		$('#BOARD_SEQ').val(seq);
		$('#frm').attr("action", "/user/apt_qnaD.do");
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
				<%@ include file="/static_root/inc/usr_left.jsp" %>
				<!-- 좌측영역 끝 -->
			</li>
			<li id="right">
				<!-- 타이틀/네비 -->
				<div class="titleNaviNew">
					<h3 class="subTitle">Q&A</h3>
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
							<input type="hidden" id="BOARD_SEQ"  name="BOARD_SEQ" value=""/>
							<fieldset>
								<div class="searchCmn">
									<label for="SCH_TYPE">검색분류</label>
									<div class="search_cmn_wrap">
										<ul>
											<li class="twoFL">
												<label for="SCH_TYPE" class="hidden">검색분류</label>
												<select name="SCH_TYPE" id="SCH_TYPE" class="select" title="검색분류">
													<option value="" >전체</option>
													<option value="T" <c:if test="${INIT_DATA.SCH_TYPE eq 'T' }">selected</c:if>>제목</option>
													<option value="C" <c:if test="${INIT_DATA.SCH_TYPE eq 'R' }">selected</c:if>>내용</option>
													<option value="N" <c:if test="${INIT_DATA.SCH_TYPE eq 'R' }">selected</c:if>>작성자</option>
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
									<a href="javascript:fnWrite();" class="mBtn3">글쓰기</a>
								</c:if>
							</dd>
						</dl>
						<!-- 리스트 -->
						<div class="bbsScrollSm">
							<table summary="Q&A">
								<caption>Q&A</caption>
								<colgroup>
									<col width="60" />
									<col width="200" />
									<col width="100" />
									<col width="80" />		
									<col width="80" />		
								</colgroup>
								<thead>
									<tr>
										<th>번호</th>
										<th>제목</th>
										<th>작성자</th>
										<th>작성일</th>
										<th>조회수</th>
									</tr>
								</thead>
								<tbody>
									<c:if test="${not empty INIT_DATA.resultList}">
										<c:forEach items="${INIT_DATA.resultList}" var="item" varStatus="status">
											<tr>
												<td>${item.RNUMUM}</td>
												<td>
													<c:if test="${item.SECRET_YN eq 'Y'}">
														<c:choose>
															<c:when test="${item.REG_ID eq INIT_DATA.SESSION_USER_ID or INIT_DATA.SESSION_ADMIN_YN eq 'Y'}">
																<a href="javascript:fnDetail('${item.BOARD_SEQ}');">
																	<img src="/static_root/images/common/cecret.png" height="18" alt="비밀글" title="비밀글" />&nbsp;&nbsp;비밀글 입니다.
																	<c:if test="${item.REP_YN eq 'Y'}">
																		&nbsp;&nbsp;&nbsp;&nbsp;<img src="/static_root/images/common/answer.png" height="18" alt="완료" title="완료" />
																	</c:if> 
																</a>
															</c:when>
															<c:otherwise>
																<img src="/static_root/images/common/cecret.png" height="18" alt="비밀글" title="비밀글" />&nbsp;&nbsp;비밀글 입니다.
																<c:if test="${item.REP_YN eq 'Y'}">
																	&nbsp;&nbsp;&nbsp;&nbsp;<img src="/static_root/images/common/answer.png" height="18" alt="완료" title="완료" />
																</c:if> 
															</c:otherwise>
														</c:choose>
													</c:if>
													<c:if test="${item.SECRET_YN eq 'N'}">
														<a href="javascript:fnDetail('${item.BOARD_SEQ}');">${item.TITLE}</a>
														<c:if test="${item.REP_YN eq 'Y'}">
															&nbsp;&nbsp;&nbsp;&nbsp;<img src="/static_root/images/common/answer.png" height="18" alt="완료" title="완료" />
														</c:if> 
													</c:if>
												</td>
												<td>${item.REG_NICK}</td>
												<td>${item.REGDATE}</td>
												<td>${item.VIEW_CNT}</td>
											</tr>
										</c:forEach>
									</c:if>
									<c:if test="${empty INIT_DATA.resultList}">
										<tr>
											<td class="no-list" colspan="5">작성된 Q&A가 없습니다.</td>
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