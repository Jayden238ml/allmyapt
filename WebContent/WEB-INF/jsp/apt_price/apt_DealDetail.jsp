<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<head>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
	<title>아파트정보 | 아파트 실거래가 이력</title>
<link rel='stylesheet' href='/static_root/tuiChart/tui-chart.css' type='text/css'>
<script type="text/javascript" src="/static_root/tuiChart/tui-chart-all.js" ></script>	
<%@ include file="/static_root/inc/usr_top.jsp" %>

<script type="text/javascript">
	$(document).ready(function(){
		<c:if test="${not empty INIT_DATA.chartList}">
			setChart();
		</c:if>
		
	});
	
	function setChart(){
		
		var w;
		var h;

		w = $('#chart-area').parent('div').width()-2;
		h = $('#chart-area').parent('div').height()-5;
		
		var tit = '${INIT_DATA.APARTMENT_NAME}' + '(' + '${INIT_DATA.AREA_EXCLUSIVE_USE}'+ ' )';
		var maxVal = [];
		var increment = 10;
		var aJsonArray = new Array();
		<c:forEach var="item" items="${INIT_DATA.chartList}" varStatus="status">
			var aJson = new Object();
			aJson.all = '${item.MAX_AMT}';
			aJsonArray.push(aJson);
		</c:forEach>
		
		$.each(aJsonArray,function(key,json){
			var maxhArr = new Array();
			maxhArr.push(json.all);
			maxVal.push(maxhArr);
			
		}); 
		
		// 최대값 구하기
		var max = Math.max.apply(null, maxVal);
		if(max == NaN || max == "0"){
			max = "100000";
		}
		increment = Math.ceil(max / 10);
		var container = document.getElementById('chart-area');
		var data = {
		    categories: [
			    		 <c:forEach items="${INIT_DATA.chartList}" var="item" varStatus="status">
			         		'${item.DEAL_YEAR}년 ${item.DEAL_MONTH}월',
			        	 </c:forEach>
		          		],
		    series: [
		        {
		            name: '금액',
		            data: 
		            	[
		            		 <c:forEach items="${INIT_DATA.chartList}" var="item" varStatus="status">
		            	 		${item.MAX_AMT},
			        	 	</c:forEach>
		            	 ]
		        }
		    ]
		};
		var options = {
		    chart: {
		        width: 950,
		        height: 450,
		        title: tit ,
		        format: increment
		    },
		    xAxis: {
		        title: '',
		        min: 0,
		        max: max,
		        suffix: ''
		    },
		     series: {
		         showLabel: true
		     },
			chartExportMenu: {
		    	visible: false 
			} ,
			legend: {
				visible: false
			}
		};
		var theme = {
		    series: {
		        colors: [
		            '#E58B23'
		        ]
		    }
		};

		var chart2 = tui.chart.columnChart(container, data, options);
		
		$(window).bind('resize', function(e) {
			chart2.resize({
		        width: $('#chart-area').parent('div').width()-2,
		        height:$('#chart-area').parent('div').height()-5
			});
		}); 
	}
	
	function fnList(){
		var url = $('#RTN_URL').val();
		$('#frm').attr("action", url);
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
					<h3 class="subTitle">아파트 실거래가 이력</h3>
				</div>
				<!-- 타이틀/네비 끝 -->
				<div id="cont">
					<form name="frm" id="frm" method="post" action="#">
						<input type="hidden" name="TMC" id="TMC" value="${INIT_DATA.TMC}" />
						<input type="hidden" name="LMC" id="LMC" value="${INIT_DATA.LMC}" />
						<input type="hidden" name="RTN_URL" id="RTN_URL" value="${INIT_DATA.RTN_URL}" />
						<input type="hidden" name="CURR_PAGE" id="CURR_PAGE" value="${INIT_DATA.CURR_PAGE}" />
						<input type="hidden" name="SCH_TOP_LAWD_CD" id="SCH_TOP_LAWD_CD" value="${INIT_DATA.SCH_TOP_LAWD_CD}" />
						<input type="hidden" name="SCH_LAWD_CD" id="SCH_LAWD_CD" value="${INIT_DATA.SCH_LAWD_CD}" />
						<input type="hidden" name="SCH_APARTMENT_NAME" id="SCH_APARTMENT_NAME" value="${INIT_DATA.SCH_APARTMENT_NAME}" />
						<input type="hidden" name="SCH_AREA_EXCLUSIVE_USE" id="SCH_AREA_EXCLUSIVE_USE" value="${INIT_DATA.SCH_AREA_EXCLUSIVE_USE}" />
					</form>
					<section class="sect_area sect_first_area">
						<h3 class="title1 mt10"><c:out value="${INIT_DATA.APARTMENT_NAME}"/> (<c:out value="${INIT_DATA.AREA_EXCLUSIVE_USE}"/>) </h3>
						<div class="viewArea mt20">
							<div class="videoWrap" style="color:#000000">
								 <div id="chart-area"></div>
							</div>
							<div class="videoWrap" style="color:#000000">
								<div class="bbsScrollSm">
									<table summary="아파트분양권 거래이력">
										<caption>아파트분양권 거래이력</caption>
										<colgroup>
											<col width="150" />
											<col width="80" />
											<col width="150" />
										</colgroup>
										<thead>
											<tr>
												<th>거래일자</th>
												<th>층수</th>
												<th>거래금액</th>
											</tr>
										</thead>
										<tbody>
											<c:if test="${not empty INIT_DATA.resultList}">
												<c:forEach items="${INIT_DATA.resultList}" var="item" varStatus="status">
													<tr>
														<td>${item.DEAL_YEAR}년&nbsp;${item.DEAL_MONTH}월&nbsp;${item.DEAL_DAY}일</td>
														<td>${item.FLOOR}층</td>
														<td>${item.DEAL_AMOUNT}(만원)</td>
													</tr>
												</c:forEach>
											</c:if>
											<c:if test="${empty INIT_DATA.resultList}">
												<tr>
													<td class="no-list" colspan="3">검색결과가 없습니다.</td>
												</tr>
											</c:if>
										</tbody>
									</table>
								</div>
							</div>
						</div>
						<div class="videoWrap mt20" style="color:#000000">
							 <c:if test="${INIT_DATA.X_LOCATION ne '' and INIT_DATA.Y_LOCATION ne ''}">
								<div id="map" style="width:100%;height:400px;"></div>
								<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=e5c7d9bd5172ded7a64bab139c62c331"></script>
								<script type="text/javascript">
									var container = document.getElementById('map'); 
									var options = { 
									center: new kakao.maps.LatLng(${INIT_DATA.X_LOCATION}, ${INIT_DATA.Y_LOCATION}), 
									level: 3 
									};
									var map = new kakao.maps.Map(container, options); 
								</script>
							</c:if>
						</div>
						<div class="core_btn">
							<a href="javascript:fnList();" class="mt30" style="background-color:#ff9900;">목록</a>
						</div>
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