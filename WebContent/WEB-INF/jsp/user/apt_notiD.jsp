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
	
	<title>아파트관리 | 공지사항</title>
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
	
	function fnList(){
		$('#frm').attr("action", "/user/apt_notil.do");
		$('#frm').submit();
	}
	
	

</script>
</head>
<body id="user">
	<div id="wrap">
		<!-- 상단영역 -->
		<%@ include file="/static_root/inc/apt_header.jsp" %>
		<!-- 상단영역 -->
		
		<ul id="content">
			<li id="left">
				<!-- 좌측영역 -->
				<%@ include file="/static_root/inc/apt_left.jsp" %>
				<!-- 좌측영역 -->
			</li>
			<li id="right">
				<!-- 타이틀/네비 -->
				<div class="titleNaviNew">
					<h3 class="subTitle">공지사항</h3>
				</div>
				<!-- 타이틀/네비 끝 -->

				<!-- 본문영역 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// -->
				<div id="cont">
					<form name="frm" id="frm" method="post" action="/user/apt_notil.do">
						<input type="hidden" name="TMC" id="TMC" value="${INIT_DATA.TMC}" />
						<input type="hidden" name="LMC" id="LMC" value="${INIT_DATA.LMC}" />
						<input type="hidden" name="BOARD_SEQ" id="BOARD_SEQ" value="${INIT_DATA.BOARD_SEQ}" />
						<section class="sect_area sect_first_area">
							
							<div class="viewArea">
								<div class="bvInfo">
									<dl class="infoTitle">
										<dt>
											${INIT_DATA.detail.TITLE}
										</dt>
									</dl>
									<dl class="infoArea">
										<dt><span>작성자 &nbsp;|&nbsp; ${INIT_DATA.detail.REG_NICK}<span>&nbsp;|&nbsp;등록일 &nbsp;|&nbsp; </span>${INIT_DATA.detail.REGDATE}</dt>
										<dd><span>조회수 &nbsp;|&nbsp; </span>${INIT_DATA.detail.VIEW_CNT}</dd>
									</dl>
								</div>
								<div class="videoWrap" style="color:#000000">
									${fn:replace(INIT_DATA.detail.CONTENTS, crcn, br)}
								</div>
							</div>
							<div class="core_btn">
								<a href="javascript:fnList();" class="mt30" style="background-color:#ff9900;">목록</a>
							</div>
						</section>
					</form>
				</div>
				<!-- 본문영역 끝 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// -->
			</li>
		</ul>
		<!-- 하단영역 -->
		<%@ include file="/static_root/inc/apt_footer.jsp" %>
		<!-- 하단영역 끝 -->
	</div>
</body>
</html>