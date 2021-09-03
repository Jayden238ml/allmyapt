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
	<title>아파트정보</title>
<%@ include file="/static_root/inc/usr_top.jsp" %>
<%
   //치환 변수 선언
    pageContext.setAttribute("cr", "\r"); //Space
    pageContext.setAttribute("cn", "\n"); //Enter
    pageContext.setAttribute("crcn", "\r\n"); //Space, Enter
    pageContext.setAttribute("br", "<br/>"); //br 태그
%>
<script type="text/javascript">
	$(document).ready(function(){
		$(window).resize(function() { 
			fnAutoResize();
		});
	});
	
	window.onload = function () {
		fnAutoResize();
	}
	
	function fnAutoResize(){
		var windowWidth = $( window ).width();	
		$('.videoWrap img').each(function() {
			if($('.va-content').width() < this.naturalWidth){
				$(this).css("width", "100%");
				$(this).css("height", "auto");
			}else{
				$(this).css("width", this.naturalWidth);
				$(this).css("height", "auto");
			}
		});
	}
	
	function fnChangeTab(tab){
		$('#TAB_GUBUN').val(tab);
		$('.proJect_tab li a').removeClass("on");
		$('#tab'+tab).attr("class", "on");
		fnSearch();
	}
	
	function fnList(){
		var url = "/user/apt_info"+ $('#RTN_URL').val() +"L.do";
		$('#frm').attr("action", url);
		$('#frm').submit();
	}
	
	function fnSearch(){
		$('#frm').attr("action", "/user/apt_infod.do");
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
					<h3 class="subTitle">아파트정보</h3>
				</div>
				<!-- 타이틀/네비 끝 -->
				<div id="cont">
					<form name="frm" id="frm" method="post" action="#">
						<input type="hidden" name="TMC" id="TMC" value="${INIT_DATA.TMC}" />
						<input type="hidden" name="LMC" id="LMC" value="${INIT_DATA.LMC}" />
						<input type="hidden" name="RTN_URL" id="RTN_URL" value="${INIT_DATA.RTN_URL}" />
						<input type="hidden" name="CURR_PAGE" id="CURR_PAGE" value="${INIT_DATA.CURR_PAGE}" />
						<input type="hidden" name="SCH_TYPE" id="SCH_TYPE" value="${INIT_DATA.SCH_TYPE}" />
						<input type="hidden" name="SCH_WORD" id="SCH_WORD" value="${INIT_DATA.SCH_WORD}" />
						<input type="hidden" name="MST_BOARD_SEQ" id="MST_BOARD_SEQ" value="${INIT_DATA.MST_BOARD_SEQ}" />
						<input type="hidden" name="BOARD_SEQ" id="BOARD_SEQ" value="${INIT_DATA.BOARD_SEQ}" />
						<input type="hidden" id="BOARD_SEQ01"  name="BOARD_SEQ01" value="${INIT_DATA.BOARD_SEQ01}"/>
						<input type="hidden" id="BOARD_SEQ02"  name="BOARD_SEQ02" value="${INIT_DATA.BOARD_SEQ02}"/>
						<input type="hidden" id="BOARD_SEQ03"  name="BOARD_SEQ03" value="${INIT_DATA.BOARD_SEQ03}"/>
						<input type="hidden" name="TAB_GUBUN" id="TAB_GUBUN" value="${INIT_DATA.TAB_GUBUN}" />
					</form>
					<section class="sect_area pb0">
						<ul class="proJect_tab">
							<li style="width:33.3%;"><a href="javascript:fnChangeTab('01');" <c:if test="${INIT_DATA.TAB_GUBUN eq '01'}">class="on"</c:if> id="tab1">단지정보</a></li>
							<li style="width:33.3%;"><a href="javascript:fnChangeTab('02');" <c:if test="${INIT_DATA.TAB_GUBUN eq '02'}">class="on"</c:if> id="tab3">세대정보</a></li>
							<li style="width:33.3%;"><a href="javascript:fnChangeTab('03');" <c:if test="${INIT_DATA.TAB_GUBUN eq '03'}">class="on"</c:if> id="tab3">평면도</a></li>
						</ul>
					</section>
					<section class="sect_area sect_first_area">
						<h3 class="title1 mt10"><c:out value="${INIT_DATA.detail.APT_NM}"/> </h3>
						<div class="viewArea mt20">
							<div class="videoWrap" style="color:#000000">
								${fn:replace(INIT_DATA.detail.CONTENTS, crcn, br)}
							</div>
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