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
<title>아파트관리 | 분양권 비교</title>
<link rel='stylesheet' href='/static_root/tuiChart/tui-chart.css' type='text/css'>
<script type="text/javascript" src="/static_root/tuiChart/tui-chart-all.js" ></script>
<%@ include file="/static_root/inc/usr_top.jsp" %>
<script type="text/javascript">
	
	$(document).ready(function(){
		$("body").prepend('<div id="overlay" class="ui-widget-overlay" style="z-index: 1001; display: none;"></div>');
		 $("body").prepend('<div id="dialog" style="z-index: 1001; position:fixed; top:50%; left:50%; padding:.2em; width:50px; height:50px; display:none;"><img src="'+COMMON_IMAGES_CONF+'/common/j_loading.gif" /></div>');
		 $('#frm').submit(function() {
			var pass = true;
			if(pass == false){
				return false;
			}
			$("#dialog, #overlay").show();

			return true;
		});
		 
		<c:if test="${not empty INIT_DATA.chartList}">
			setChart();
			$('#ChartDiv').show();
		</c:if>
	});
	
	
	function setChart(){
		var w;
		var h;

		w = $('#chart-area').parent('div').width()-2;
		h = $('#chart-area').parent('div').height()-5;
		
		var tit = '${INIT_DATA.MY_APARTMENT_NAME}' + ' VS ' + '${INIT_DATA.YOU_APARTMENT_NAME}';
		var t1 = '${INIT_DATA.MY_APARTMENT_NAME}';
		var t2 = '${INIT_DATA.YOU_APARTMENT_NAME}';
		var maxVal = [];
		var increment = 10;
		var aJsonArray = new Array();
		<c:forEach var="item" items="${INIT_DATA.chartList}" varStatus="status">
			var aJson = new Object();
			var aJson2 = new Object();
			aJson.all = '${item.MAX_AMT}';
			aJson2.all = '${item.BMAX_AMT}';
			aJsonArray.push(aJson);
			aJsonArray.push(aJson2);
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
		    		name: t1,
		            data: 
		            	[
		            		<c:forEach items="${INIT_DATA.chartList}" var="item" varStatus="status">
		            	 		${item.MAX_AMT},
			        	 	</c:forEach>
		            	 ],
		          },
		          {
		        	  name: t2,
			            data: 
			            	[
			            		<c:forEach items="${INIT_DATA.chartList}" var="item" varStatus="status">
			            	 		${item.BMAX_AMT},
				        	 	</c:forEach>
			            	 ],
		          }
		    ]
		};
		var options = {
		    chart: {
		        width: 960,
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
	
	function fnSearch(){
		$('#frm').attr("action", "/user/apt_OutCompareL.do");
		$('#frm').submit();
	}
	
	
	function fnSearchMyhome(gubun){
		$('#SEARCH_TYPE').val(gubun);
		if(gubun == "M"){ 
			if($('#MY_SIDO_CD').val() == ""){
				alert("시/도를 선택해 주세요.");
				$('#MY_SIDO_CD').focus();
				return;
			}
			if($('#MY_APART_NM').val() == ""){
				alert("아파트명을 입력해 주세요.");
				$('#MY_APART_NM').focus();
				return;
			}
		}else{
			if($('#YOU_SIDO_CD').val() == ""){
				alert("시/도를 선택해 주세요.");
				$('#YOU_SIDO_CD').focus();
				return;
			}
			if($('#YOU_APART_NM').val() == ""){
				alert("아파트명을 입력해 주세요.");
				$('#YOU_APART_NM').focus();
				return;
			}
		}
		
		$.ajax({
			type: "POST",
			url : "/api/serachComPareAptOut_List.do",
			data:  $("#frm").serialize(),
			dataType: "json",
			success: function (transport) {
				fnsetMyGrid(eval(transport.resultList), gubun);
			}
		});
	}
	
	function fnsetMyGrid(dataSet, gubun){
		var resultHtml ="";
		if (dataSet == null || dataSet == "") {
			resultHtml += '<div class="bbsScrollSm">';
			resultHtml += '		<table summary="실거래가 비교">';
			resultHtml += '			<colgroup>';
			resultHtml += '				<col width="150" />';
			resultHtml += '				<col width="150" />';
			resultHtml += '				<col width="80" />';
			resultHtml += '				<col width="80" />	';	
			resultHtml += '			</colgroup>';
			resultHtml += '			<thead>';
			resultHtml += '				<tr>';
			resultHtml += '					<td colspan="4">검색결과가 없습니다.</td>';
			resultHtml += '				</tr>';
			resultHtml += '			</thead>';
			resultHtml += '		</table>';
			resultHtml += '</div>';
		} else {
			$.each(dataSet,function(key,json) {
				resultHtml += '<div class="bbsScrollSm">';
				resultHtml += '		<table summary="실거래가 비교">';
				resultHtml += '			<colgroup>';
				resultHtml += '				<col width="150" />';
				resultHtml += '				<col width="150" />';
				resultHtml += '				<col width="80" />';
				resultHtml += '				<col width="80" />	';	
				resultHtml += '			</colgroup>';
				resultHtml += '			<thead>';
				resultHtml += '				<tr>';
				resultHtml += '					<td>'+json.APARTMENT_NAME+'</td>';
				resultHtml += '					<td>'+json.AREA_EXCLUSIVE_USE+'</td>';
				resultHtml += '					<td>'+json.ADDRESS+'</td>';
				if(gubun == "M"){
					resultHtml += '					<td><a href="javascript:fnMySet(\'' + json.APARTMENT_NAME + '\',\'' + json.JIBUN + '\',\'' + json.AREA_EXCLUSIVE_USE +'\',\'' + json.REGIONAL_CODE +'\');" class="mBtn3">선택</a> </td>';
				}else{
					resultHtml += '					<td><a href="javascript:fnYouSet(\'' + json.APARTMENT_NAME + '\',\'' + json.JIBUN + '\',\'' + json.AREA_EXCLUSIVE_USE +'\',\'' + json.REGIONAL_CODE +'\');" class="mBtn3">선택</a> </td>';
				}
				resultHtml += '				</tr>';
				resultHtml += '			</thead>';
				resultHtml += '		</table>';
				resultHtml += '</div>';
			});
		}
		if(gubun == "M"){
			$('#mydataBody').show();
			$("#mydataBody").html(resultHtml);
		}else{
			$('#youdataBody').show();
			$("#youdataBody").html(resultHtml);
		}
	}
	
	function fnMySet(a_nm, bonbun, area, r_code){
		$('#MY_APARTMENT_NAME').val(a_nm);
		$('#MY_JIBUN').val(bonbun);
		$('#MY_AREA_EXCLUSIVE_USE').val(area);
		$('#MY_REGIONAL_CODE').val(r_code);
		$('#mydataBody').html("");
		$('#mytr').show();
		$('#mytd').html(a_nm + '(' + area + ')');
		
		if($('#MY_JIBUN').val() != "" && $('#YOU_JIBUN').val() != ""){
			$('#compareBtn').show();
		}else{
			$('#compareBtn').hide();
		}
	}
	
	function fnYouSet(a_nm, bonbun, area, r_code){
		$('#YOU_APARTMENT_NAME').val(a_nm);
		$('#YOU_JIBUN').val(bonbun);
		$('#YOU_AREA_EXCLUSIVE_USE').val(area);
		$('#YOU_REGIONAL_CODE').val(r_code);
		$('#youdataBody').html("");
		$('#youtr').show();
		$('#youtd').html(a_nm + '(' + area + ')');
		
		if($('#MY_JIBUN').val() != "" && $('#YOU_JIBUN').val() != ""){
			$('#compareBtn').show();
		}else{
			$('#compareBtn').hide();
		}
	}
	
	
	function fnReset(){
		$('#compareBtn').hide();
		$('#ChartDiv').hide();
		$('#MY_APARTMENT_NAME').val("");
		$('#MY_JIBUN').val("");
		$('#MY_AREA_EXCLUSIVE_USE').val("");
		$('#YOU_APARTMENT_NAME').val("");
		$('#YOU_JIBUN').val("");
		$('#YOU_AREA_EXCLUSIVE_USE').val("");
		$('#MY_SIDO_CD').val("");
		$('#MY_APART_NM').val("");
		$('#YOU_SIDO_CD').val("");
		$('#YOU_APART_NM').val("");
		$('#chart-area').html("");
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
					<h3 class="subTitle">실거래가 비교</h3>
				</div>
				<!-- 타이틀/네비 끝 -->

				<!-- 본문내용 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// -->
				<div id="cont">
					<section class="sect_area sect_first_area">
						<!-- 검색 -->
						<form name="frm" id="frm" method="post" action="#">
							<input type="hidden" name="TMC" id="TMC" value="${INIT_DATA.TMC}" />
							<input type="hidden" name="LMC" id="LMC" value="${INIT_DATA.LMC}" />
							<input type="hidden" name="SEARCH_TYPE" id="SEARCH_TYPE" value="" />
							<input type="hidden" name="MY_APARTMENT_NAME" id="MY_APARTMENT_NAME" value="${INIT_DATA.MY_APARTMENT_NAME}" />
							<input type="hidden" name="MY_JIBUN" id="MY_JIBUN" value="${INIT_DATA.MY_JIBUN}" />
							<input type="hidden" name="MY_AREA_EXCLUSIVE_USE" id="MY_AREA_EXCLUSIVE_USE" value="${INIT_DATA.MY_AREA_EXCLUSIVE_USE}" />
							<input type="hidden" name="MY_REGIONAL_CODE" id="MY_REGIONAL_CODE" value="${INIT_DATA.MY_REGIONAL_CODE}" />
							<input type="hidden" name="YOU_APARTMENT_NAME" id="YOU_APARTMENT_NAME" value="${INIT_DATA.YOU_APARTMENT_NAME}" />
							<input type="hidden" name="YOU_JIBUN" id="YOU_JIBUN" value="${INIT_DATA.YOU_JIBUN}" />
							<input type="hidden" name="YOU_AREA_EXCLUSIVE_USE" id="YOU_AREA_EXCLUSIVE_USE" value="${INIT_DATA.YOU_AREA_EXCLUSIVE_USE}" />
							<input type="hidden" name="YOU_REGIONAL_CODE" id="YOU_REGIONAL_CODE" value="${INIT_DATA.YOU_REGIONAL_CODE}" />
							<h4 class="title1">우리집과 비교집 검색 후 분양권 가격을 비교를 할 수 있습니다.</h4>
							<div class="bbsView" >
								<table summary="견적요청">
									<caption>견적문의</caption>
									<colgroup>
										<col width="20%" />
										<col width="*" />
									</colgroup>
									<tbody>
										<tr>
											<th>
												<a href="javascript:fnSearchMyhome('M');" class="mBtn4">우리집검색</a> 
											</th>
											<td>
												<select name="MY_SIDO_CD" id="MY_SIDO_CD" title="광역시/도" style="height:40px;box-sizing:border-box; padding: 0 5px; border: 1px solid #e5e5e5; border-radius: 3px;">
													<option value="">시/도 선택</option>
													<c:forEach var="item" items="${INIT_DATA.SidoList }" >
														<option value="${item.CODE}" <c:if test="${item.CODE eq INIT_DATA.MY_SIDO_CD}">selected="selected"</c:if> >${item.CODENM}</option>
													</c:forEach>
												</select>&nbsp;&nbsp;
												<input type="text" name="MY_APART_NM" id="MY_APART_NM" title="단지명" value="${INIT_DATA.MY_APART_NM}" placeholder="단지명" class="input" style="width:50%;height: 40px;box-sizing: border-box; padding: 0 5px;border: 1px solid #e5e5e5; border-radius: 3px; background: #fff;"/>
											</td> 
										</tr> 
										<tr style="display:none;" id="mytr">
											<th>선택된 우리집</th>
											<td id="mytd"></td>
										</tr>
									</tbody>
									<tbody id="mydataBody" style="display:none;">
									</tbody>
									<tbody class="mt20">
										<tr>
											<th>
												<a href="javascript:fnSearchMyhome('Y');" class="mBtn6">비교집검색</a> 
											</th>
											<td> 
												<select name="YOU_SIDO_CD" id="YOU_SIDO_CD" title="광역시/도" style="height:40px;box-sizing:border-box; padding: 0 5px; border: 1px solid #e5e5e5; border-radius: 3px;">
													<option value="">시/도 선택</option>
													<c:forEach var="item" items="${INIT_DATA.SidoList }" >
														<option value="${item.CODE}" <c:if test="${item.CODE eq INIT_DATA.YOU_SIDO_CD}">selected="selected"</c:if> >${item.CODENM}</option>
													</c:forEach>
												</select>&nbsp;&nbsp;
												<input type="text" name="YOU_APART_NM" id="YOU_APART_NM" title="단지명" value="${INIT_DATA.YOU_APART_NM}" placeholder="단지명" style="width:50%;height: 40px;box-sizing: border-box; padding: 0 5px;border: 1px solid #e5e5e5; border-radius: 3px; background: #fff;"/>
											</td> 
										</tr> 
										<tr style="display:none;" id="youtr">
											<th>비교 아파트명</th>
											<td id="youtd"></td>
										</tr>
									</tbody>
									<tbody id="youdataBody" style="display:none;">
									</tbody>
								</table>
							</div>
							<div class="core_btn" style="display:none;" id="compareBtn">
								<a href="javascript:fnSearch();" class="mt30" style="background-color:#ff9900;">비교하기</a>
							</div>
						</form>
						<div class="videoWrap" style="color:#000000; display:none;" id="ChartDiv" >
							 <div id="chart-area"></div>
							 <div class="core_btn mt10" >
								<a href="javascript:fnReset();" class="mt30" style="background-color:#ff9900;">초기화</a>
							</div>
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