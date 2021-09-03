<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html lang="ko">
<head>
<title>이용약관</title>
<%@ include file="/static_root/inc/apt_top.jsp" %>
<script type="text/javascript">
	
</script>
</head>
<body>
<form name="popFrm" id="popFrm" method="post">
	<div class="layerWrap680 login_pop_inner">
		<div class="bg"></div>
		<!-- 로그인 -->
		<div class="loginLayer">
			<p>
				<c:if test="${INIT_DATA.TYPE eq 'P'}">
					<a href="javascript:gfnLayerClose();" class="cbtn"><img src="/static_root/images/content/popClose.jpg" alt="닫기" title="닫기" /></a>
				</c:if>
				<c:if test="${INIT_DATA.TYPE eq 'M'}">
					<a href="javascript:gfnMobileLayerClose();" class="cbtn"><img src="/static_root/images/content/popClose.jpg" alt="닫기" title="닫기" /></a>
				</c:if>
			</p>
			<div id="loginDiv">
				<h1><span>이용약관</span></h1>
				<br/>
				<fieldset>
					<div class="modal-content">
						<div class="modal-body">
							'주식회사 올마이티컴퍼니'(이하 '올마이티컴퍼니')는 이용자의 소중한 개인정보를 보호하기 위하여<br/>
							정보통신서비스제공자가 준수하여야 하는 대한민국의 관계 법령 및 개인정보보호 규정, <br/>
							가이드라인을 준수하고 있습니다. <br/>
							올마이티컴퍼니는 개인정보처리방침을 통하여 이용자가 제공하는 개인정보가 어떠한 목적으로, 
							<br/>어떠한 용도와 방식으로 이용되고 있는지, 개인정보보호를 위해 어떠한 조치가 취해지고 있는지 알려 드립니다.<br /><br/> 
	  						 올마이티컴퍼니의 개인정보처리방침은 다음과 같은 내용을 담고 있습니다.<br /><br/> 
	  						 
	  						 <b>1. 수집하는 개인정보 항목</b><br/> <br/> 
	  						 
	  						 사이트는 회원가입, 상담, 서비스 신청 등등을 위해 아래와 같은 개인정보를 수집하고 있습니다.<br/>
	  						 - 수집항목 : 이름, 로그인ID, 닉네임, 접속 IP 정보<br/> 
	  						 - 개인정보 수집방법 : 카카오톡을 이용한 홈페이지 회원가입<br/> <br/> 
	  						 
	  						 <b>2. 개인정보의 수집 및 이용목적</b><br/> <br/> 
	  						  
	  						  사이트는 수집한 개인정보를 다음의 목적을 위해 활용합니다.<br/>
	  						  - 서비스 제공에 관한 지원과 상담<br/>
	  						  - 회원 관리<br/>
	  						  회원제 서비스 이용에 따른 본인확인, 개인 식별, 불량회원의 부정 이용 방지와 비인가 사용 방지, <br/>
	  						  가입 의사 확인, 연령확인, 불만처리 등 민원처리<br/>
	  						  
	  						  마케팅 및 광고에 활용<br/>
	  						 - 신규 서비스(제품) 개발 및 특화, 이벤트 등 광고성 정보 전달, 접속 빈도 파악 또는 회원의 서비스 이용에 대한 통계<br/><br/> 
	  						  
	  						 <b>3. 개인정보의 보유 및 이용기간</b><br/> <br/> 
	  						  사이트 개인정보 수집 및 이용목적이 달성된 후에는 예외 없이 해당 정보를 지체 없이 파기합니다.<br/> <br/> 
	  						 <b>4. 고지의 의무</b><br/> <br/> 
	  						  현 개인정보처리방침 내용 추가, 삭제 및 수정이 있을 시에는 즉시 홈페이지의 게시판을 통해 <br/> 
	  						  이용자에게 고지할 것입니다. <br/>
	  						 해킹 또는 컴퓨터 바이러스로 인한 보안사고 발생 시 회사는 모든 피해 이용자에게 이를 즉시 통보합니다. <br />
						</div>
						<div class="ac mt15">
							<a href="javascript:gfnLayerClose();" class="btn3 w50">닫기</a>
						</div>
					</div>
				</fieldset>
			</div>				
		</div>
	</div>
</form>
</body>
</html>