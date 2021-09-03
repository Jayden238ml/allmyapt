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
	
	<title>아파트관리 | 사이트 안내</title>
<%@ include file="/static_root/inc/usr_top.jsp" %>
</head>
<script>
	function fnreqMove(){
		$('#TMC').val("TMC001");
		$('#LMC').val("LMC003");
		$('#frm').attr("action", "/user/apt_reqw.do");
		$('#frm').submit();
	}
</script>
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
					<h3 class="subTitle">사이트 안내</h3>
				</div>
				<!-- 타이틀/네비 끝 -->
				<div id="cont">
					<section class="sect_area sect_first_area">
						<form name="frm" id="frm" method="post" action="#">
							<input type="hidden" name="TMC" id="TMC" value="${INIT_DATA.TMC}" />
							<input type="hidden" name="LMC" id="LMC" value="${INIT_DATA.LMC}" />
						</form>
						<ul class="ulCsltSelect clearfix">
							<li>
								<h4>온라인으로 아파트 관리</h4>
								<div class="con-img">&nbsp;</div>
								<div class="text-ab">
									<p>우리는 처음 아파트 분양받아 계약하는 순간부터 내 작은 소망 첫 번째 희망이 실현이 되면서 과연 내가 살 공간이 잘 지어질까 고민을 합니다.</p>
									<p>그리고 그 고민 끝에는 우리 가족을 위해 우리 이웃을 위해 내가 할 수 있는 일을 하자는 결론을 내립니다.</p>
									<p>누구나 입예협 활동을 할 수는 있지만, 동의 없이 후원 없이 할 수 있는 활동은 아닙니다.</p>
									<p>내 소중한 시간, 내 소중한 자산을 지키는데 무엇이 효율적이고 무엇을 지켜야 할지 늘 고민을 하게 됩니다.</p>
									<p>동의서를 받는 순간부터 입주민들의 이야기를 들으려면 그것을 관리하는 업무부터 할 수밖에 없습니다.</p>
									<p>하지만 내 삶도 살아가야 하는 세상에서 자료가 멸실되거나 훼손되거나 그리고 누군가의 의해 잘못된 정보가 공유가 된다면 그것을 바로 잡는데 큰 어려움이 있을 수 있습니다.</p>
								</div>
							</li>
							<li>
								<h4>언텍트 시대, 가상의 사무실</h4>
								<div class="con-img">&nbsp;</div>
								<div class="text-ab">
									<p>우리의 공간에서는 아파트 관련 여러분들이 업무를 볼 수 있는 가상의 공간을 제공 합니다.</p>
									<p>인터넷만 된다면 어디에서든 동의서를 자유자재로 업로드 다운로드 및 문자 발송까지 할 수 있습니다.</p>
									<p>우리 아파트 가격 한눈에 확인할 수 있으며 타 입예협 과의 정보 교류를 통해 우리 아파트의 단점을 보완 할 수 있습니다.</p>
									<p>또한 필요 시 분양받아 짓고 있는 아파트의 설계나 건축 업무 대행을 통해 잘 짓고 있는지 확인 할 수 있으며,</p>
									<p>민원 사항 필요시 관공서에 민원 사항을 제기 하는 대행 서비스를 이용 할 수 있습니다.</p>
								</div>
							</li>
						</ul>
						<ul class="ulCsltSelect2 clearfix">
							<li>
								<h4>세부 내역</h4>
								<div class="con-img">&nbsp;</div>
								<div class="text-ab">
									<p>청약을 해야 하는지 아니면 매매를? 전세를? 월세를?</p>
									<p>나의 소중한 보금자리 어디서부터 어떻게 해야 할지 몰라 우왕좌왕 고민 할 때가 많습니다.</p>
									<p>여기에선 한 번에 해결이 가능 합니다. 아파트의 모든 정보를 한눈에 알게 되어 더 이상 해매지 않아도 됩니다.</p>
									<p>우리 아파트 가격도 한눈에 비교 할 수 있어서 더 큰 자산 실현의 꿈도 더 이상 꿈이 아닌 현실이 됩니다.</p>
									<p>그리고 나의 아파트와의 관련된 파트너도 함께 할 수 있습니다.
								</div>
							</li>
							<li>
								<h4>참여 안내</h4>
								<div class="con-img">&nbsp;</div>
								<div class="text-ab">
									<p>아파트 입예협이면 누구나 참여 가능합니다.</p>
									<p>온라인으로 명단관리가 필요하신분도 누구나 참여 가능합니다.</p>
									<p>자세한문의는 견적문의를 이용해 주세요.</p>
									<a href="javascript:fnreqMove();" class="mt30" style="background-color:#ff9900;">견적문의</a>
								</div>
							</li>
						</ul>
					</section>
				</div>
			</li>
		</ul>

		<!-- 하단영역 -->
		<%@ include file="/static_root/inc/apt_footer.jsp" %>
		<!-- 하단영역 끝 -->
	</div>
</body>
</html>