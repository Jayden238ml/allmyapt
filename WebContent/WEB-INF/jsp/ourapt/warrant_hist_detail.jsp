<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<head>
<title>아파트관리 | 위임장 관리</title>
<%@ include file="/static_root/inc/apt_top.jsp" %>
<script type="text/javascript">
	$(document).ready(function(){
		
	})
	
	
	function fnList(){
		$('#frm').attr("action", "/apt/warrant_histL.do");
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
					<h3 class="subTitle">[${INIT_DATA.aptMap.APT_NM}]&nbsp;위임장 변경이력</h3>
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
							<h3 class="title1 mt30"><c:out value="${INIT_DATA.detail.DONG}"/>동 - <c:out value="${INIT_DATA.detail.HOSU}"/>호 현재</h3>
							<div class="bbsView" >
								<table summary="위임장 변경이력">
									<caption>위임장 변경이력</caption>
									<colgroup>
										<col width="20%" />
										<col width="*" />
									</colgroup>
									<tbody>
										<tr>
											<th>위임장 제출여부</th>
											<td>
												<c:out value="${INIT_DATA.detail.WARRANT_YN_NM}"/>
											</td>
										</tr>
										<tr>
											<th>회비납부 여부</th>
											<td>
												<c:out value="${INIT_DATA.detail.AMT_YN_NM}"/>
											</td>
										</tr>
										<tr>
											<th>성명</th>
											<td>
												<c:out value="${INIT_DATA.detail.USER_NM}"/>
											</td>
										</tr>
										<tr>
											<th>핸드폰번호</th>
											<td>
												<c:out value="${INIT_DATA.detail.HP}"/>
											</td>
										</tr>
										<tr>
											<th>주소</th>
											<td>
												<c:out value="${INIT_DATA.detail.ADDR}"/>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
							<h3 class="title1 mt30">변경내역</h3>
							<div class="bbsScrollSm">
								<table summary="위임장 관리">
									<caption>위임장 관리</caption>
									<colgroup>
										<col width="60" />
										<col width="80" />
										<col width="220" />
										<col width="80" />		
										<col width="60" />
										<col width="60" />	
									</colgroup>
									<thead>
										<tr>
											<th>이름</th>
											<th>연락처</th>
											<th>주소</th>
											<th>위임장제출</th>
											<th>회비납부</th>
											<th>변경일</th>
										</tr>
									</thead>
									<tbody>
										<c:if test="${not empty INIT_DATA.histList}">
											<c:forEach items="${INIT_DATA.histList}" var="item" varStatus="status">
												<tr>
													<td>${item.USER_NM}</td>
													<td>${item.HP}</td>
													<td>${item.ADDR}</td>
													<td>${item.WARRANT_YN_NM}</td>
													<td>${item.AMT_YN_NM}</td>
													<td>${item.UPDDATE}</td>
												</tr>
											</c:forEach>
										</c:if>
										<c:if test="${empty INIT_DATA.histList}">
											<tr>
												<td class="no-list" colspan="6">검색결과가 없습니다.</td>
											</tr>
										</c:if>
									</tbody>
								</table>
							</div>
							<div class="core_btn">
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