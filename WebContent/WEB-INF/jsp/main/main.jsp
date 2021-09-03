<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="ko">
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

	<title>아파트관리</title>
	<%@ include file="/static_root/inc/usr_top.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			$('#a_header a').removeClass('on');
		});
		
		function fnMove(num){
			var url = "/user/apt_contv.do";
			$('#LMC').val("LMC001");
			$('#TMC').val("TMC001");
			if(num == "2"){
				url = "/user/apt_qnal.do";
				$('#LMC').val("LMC002");
			}else if(num == "3"){
				url = "/user/apt_reqw.do";
				$('#LMC').val("LMC003");
			}else if(num == "4"){
				url = "/user/apt_notil.do";
				$('#LMC').val("LMC004");
			}
			$('#frm').attr("action", url);
			$('#frm').submit();
		}
		
		function fnWarrant(){
			$('#LMC').val("LMC005");
			$('#TMC').val("TMC003");
			$('#frm').attr("action", "/apt/apt_warrant.do");
			$('#frm').submit();
		}
		
		function fnAptInfo(){
			$('#frm').attr("action", "/user/apt_info.do");
			$('#frm').submit();
		}
		
		function fncDetailMove(url){
			$('#LMC').val("LMC004");
			$('#TMC').val("TMC001");
			$('#frm').attr("action", url);
			$('#frm').submit();
		}
		
	</script>

</head>
<body id="user" class="main_bd">
	<form name="frm" id="frm" method="post" action="">
		<input type="hidden" name= "TMC" id="TMC" value="">
		<input type="hidden" name= "LMC" id="LMC" value="">
	</form>
	<div id="wrap">
		<!-- 상단영역 -->
		<%@ include file="/static_root/inc/apt_header.jsp" %>
		<!-- 상단영역 끝 -->
		
		<!-- 메인 콘텐츠영역 -->
		<div id="main_wrap">
			<div class="mn_visual">
				<div class="mn_slogan">
					<h2>우리집 만들기의 첫걸음</h2>
				</div>
			</div>
			
			<div class="mn_cont" id="cont">
				<div class="mn_cont_btm">
					<div id="cont_box" class="box_selfAc clearfix" onclick="fnMove('1')">
						<h3>사이트 안내</h3>
						<p>아파트 관리 시스템<br/>사이트를 소개합니다.</p>
						<a href="javascript:fnMove('1');">바로 가기 <span>+</span></a>
					</div>
					<div id="cont_box" class="box_blue box_jinroRm clearfix" onclick="fnMove('2')">
						<h3>Q&A</h3>
						<p>궁금한 모든 내용을 물어보세요.</p>
						<a href="javascript:fnMove('2');">VIEW MORE <span>+</span></a>
					</div>
					<div id="cont_box" class="box_jobInfo clearfix" onclick="fnMove('3')">
						<h3>견적문의</h3>
						<p>DB관리의 편리성, 지금 문의해 보세요.</p>
						<a href="javascript:fnMove('3');">바로 가기 <span>+</span></a>
					</div>
					<div id="cont_box" class="box_notice clearfix">
						<div class="nt_title clearfix">
							<h3>공지사항</h3>
							<a href="javascript:fnMove('4');">VIEW MORE <span>+</span></a>
						</div>
						<p>공지사항</p>
						<ul class="nt_list">
 							<c:if test="${not empty INIT_DATA.MainBdList}"> 
 								<c:forEach var="item" items="${INIT_DATA.MainBdList }" varStatus="rowStatus"> 
									<li class="clearfix">
 										<a href="javascript:fncDetailMove('/user/apt_notiD.do?BOARD_SEQ=${item.BOARD_SEQ}');">${item.TITLE}</a> 
 										<span>${item.REGDATE}</span> 
									</li>
								</c:forEach> 
 							</c:if> 
 							<c:if test="${empty INIT_DATA.MainBdList}"> 
 								<li class="clearfix">등록된 공지사항이 없습니다.</li> 
							</c:if> 
						</ul>
					</div>
				</div>
				
			</div>
			
		</div>
	</div>
	<!-- 메인 콘텐츠영역 끝 -->
	<!-- 하단영역 -->
	<%@ include file="/static_root/inc/apt_footer.jsp" %>
	<!-- 하단영역 끝 -->
	
</body>
</html>
