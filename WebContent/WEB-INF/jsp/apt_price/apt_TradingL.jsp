<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<head>
<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	<meta name="title" content="아파트관리" />
	<meta name="description" content="아파트,입예협,입주자관리,입주예정자협의회,시세조회,분양가조회, 실거래가 비교, 분양가 비교" />
	<meta name="keywords" content="아파트,입예협,입주자관리,입주예정자협의회,시세조회,분양가조회, 실거래가 비교, 분양가 비교, 올마이아파트" />
	<meta name="robots" content="index, follow">
	<link rel="canonical" href="http://www.allmyapt.com">
	<meta property="og:type" content="article" />
	<meta property="og:site_name" content="아파트관리">
	<meta property="og:type" content="article">
	<meta property="og:url" content="http://www.allmyapt.com">
	<meta property="og:title" content="아파트정보 | 입주자관리 | 실거래가 조회 | 거래가 비교">
	<meta property="og:image" content="/static_root/images/common/meta_img.jpg">
	<meta property="og:description" content="아파트관리 " />
<title>아파트관리 | 아파트실거래가 이역</title>
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
		
		if("${INIT_DATA.SCH_TOP_LAWD_CD}" != ""){
			fnGetSidoList("${INIT_DATA.SCH_TOP_LAWD_CD}");
		}
	});
	
	//페이징
	var page_List = function(pageNo) {
	    $("#CURR_PAGE").val(pageNo);        
	    $('#frm').submit();
	};
	
	function fnSearch(){
		$('#frm').attr("action", "/user/apt_TradingL.do");
		$('#frm').submit();
	}
	
	function fnGetSidoList(){
		$.ajax({
			type: "POST",
			url : "/api/getSidoList.do",
			data:  $("#frm").serialize(),
			dataType: "json",
			success: function (transport) {
				fnSetSido(eval(transport.SidoList));
			}
		});
	}
	
	function fnSetSido(SidoList){
		$("#SCH_DONG").html("");
		var resultHtml ="";
		var row =  "<option value=''>시/군/구 선택</option>";
		$.each(SidoList,function(key,json) {
			row += '<option value="'+json.CODE+'">'+json.CODENM+'</option>';
		});
		resultHtml += row;
		$("#SCH_LAWD_CD").html(resultHtml);
		if("${INIT_DATA.SCH_LAWD_CD}" != ""){
			$("#SCH_LAWD_CD").val("${INIT_DATA.SCH_LAWD_CD}");
		}
	}
	
	function fnDetail(bonbun, area , apt_nm){
		$("#ROAD_NAME_BONBUN").val(bonbun);
		$("#AREA_EXCLUSIVE_USE").val(area);
		$("#APARTMENT_NAME").val(apt_nm);
		
		$('#frm').attr("action", "/api/aptDealDetail.do");
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
					<h3 class="subTitle">아파트 실거래이력</h3>
				</div>
				<!-- 타이틀/네비 끝 -->

				<!-- 본문내용 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// -->
				<div id="cont">
					<section class="sect_area sect_first_area">
						<!-- 검색 -->
						<form name="frm" id="frm" method="post" action="#">
							<input type="hidden" name="TMC" id="TMC" value="${INIT_DATA.TMC}" />
							<input type="hidden" name="LMC" id="LMC" value="${INIT_DATA.LMC}" />
							<input type="hidden" id="CURR_PAGE"  name="CURR_PAGE" value="${INIT_DATA.CURR_PAGE}"/>
							<input type="hidden" name="RTN_URL" id="RTN_URL" value="/user/apt_TradingL.do" />
							<input type="hidden" name="ROAD_NAME_BONBUN" id="ROAD_NAME_BONBUN" value="" />
							<input type="hidden" name="AREA_EXCLUSIVE_USE" id="AREA_EXCLUSIVE_USE" value="" />
							<input type="hidden" name="APARTMENT_NAME" id="APARTMENT_NAME" value="" />
							<fieldset>
								<div class="searchApt">
									<ul>
										<li class="scharea2">
											<ul>
												<li>
													<select name="SCH_TOP_LAWD_CD" id="SCH_TOP_LAWD_CD" title="광역시/도" class="select" onchange="fnGetSidoList();">
														<option value="">시/도 선택</option>
														<c:forEach var="item" items="${INIT_DATA.SidoList }" >
															<option value="${item.CODE}" <c:if test="${item.CODE eq INIT_DATA.SCH_TOP_LAWD_CD}">selected="selected"</c:if> >${item.CODENM}</option>
														</c:forEach>
													</select>
												</li>
												<li>
													<select name="SCH_LAWD_CD" id="SCH_LAWD_CD" title="학적상태" class="select">
														<option value="">시/군/구 선택</option>
													</select>
												</li>
											</ul>
										</li>
										<li class="scharea2">
											<ul>
												<li>
													<input type="text" name="SCH_APARTMENT_NAME" id="SCH_APARTMENT_NAME" title="단지명" value="${INIT_DATA.SCH_APARTMENT_NAME}" placeholder="단지명/시군구명" class="input" />
												</li>
												<li>
													<input type="text" name="SCH_AREA_EXCLUSIVE_USE" id="SCH_AREA_EXCLUSIVE_USE" title="전용면적(㎡)" value="${INIT_DATA.SCH_AREA_EXCLUSIVE_USE}" placeholder="전용면적(㎡)" class="input" />
												</li>
											</ul>
										</li>
									</ul>
								<div style="padding:40px 15px;"><a href="javascript:fnSearch();">검색</a></div>  
								</div>
							</fieldset>
						</form>
						<!-- 검색 끝 -->
						<dl class="titleEaB">
							<dt>
								<c:if test="${INIT_DATA.DEFAULT eq 'Y'}">
									검색조건 입력 후 검색해 주세요.&nbsp;&nbsp; (상세보기 시 클릭)
								</c:if>
								<c:if test="${INIT_DATA.DEFAULT ne 'Y'}">
									총 <strong><fmt:formatNumber value="${INIT_DATA.TOTAL_CNT}" pattern="#,###"/></strong>&nbsp;건
									&nbsp;&nbsp; (상세보기 시 클릭)
								</c:if>
							</dt>
							<dd>
							
							</dd> 
						</dl>
						<!-- 리스트 -->
						<div class="bbsScrollSm">
							<table summary="아파트분양권 거래이역">
								<caption>아파트분양권 거래이역</caption>
								<colgroup>
									<col width="150" />
									<col width="150" />
									<col width="80" />
									<col width="80" />		
									<col width="80" />
								</colgroup>
								<thead>
									<tr>
										<th>주소</th>
										<th>단지명(층수)</th>
										<th>
											거래금액
											<c:if test="${INIT_DATA.DEFAULT ne 'Y'}">
												<strong style="color:red;">&nbsp;(최고가)</strong>
											</c:if>
										</th>
										<th>전용면적</th>
										<th>거래일자</th>
									</tr>
								</thead>
								<tbody>
									<c:if test="${not empty INIT_DATA.resultList}">
										<c:forEach items="${INIT_DATA.resultList}" var="item" varStatus="status">
											<tr>
												<td><a href="javascript:fnDetail('${item.ROAD_NAME_BONBUN}', '${item.AREA_EXCLUSIVE_USE}', '${item.APARTMENT_NAME}');">${item.ADDRESS}</a></td>
												<td><a href="javascript:fnDetail('${item.ROAD_NAME_BONBUN}', '${item.AREA_EXCLUSIVE_USE}', '${item.APARTMENT_NAME}');">${item.APARTMENT_NAME}&nbsp;(${item.FLOOR}층)</a></td>
												<td><a href="javascript:fnDetail('${item.ROAD_NAME_BONBUN}', '${item.AREA_EXCLUSIVE_USE}', '${item.APARTMENT_NAME}');">${item.DEAL_AMOUNT}(만원)</a></td>
												<td><a href="javascript:fnDetail('${item.ROAD_NAME_BONBUN}', '${item.AREA_EXCLUSIVE_USE}', '${item.APARTMENT_NAME}');">${item.AREA_EXCLUSIVE_USE}</a></td>
												<td><a href="javascript:fnDetail('${item.ROAD_NAME_BONBUN}', '${item.AREA_EXCLUSIVE_USE}', '${item.APARTMENT_NAME}');">${item.DEAL_YEAR}년&nbsp;${item.DEAL_MONTH}월&nbsp;${item.DEAL_DAY}일</a></td>
											</tr>
										</c:forEach>
									</c:if>
									<c:if test="${empty INIT_DATA.resultList}">
										<tr>
											<td class="no-list" colspan="5">검색결과가 없습니다.</td>
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