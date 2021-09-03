<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html lang="ko">
<head>
<title>로그인</title>
<%@ include file="/static_root/inc/apt_top.jsp" %>
<script type="text/javascript">
	function fnAptLogin(){
		if($('#USER_ID').val() == "" || $('#USER_PWD').val() == ""){
			alert("아이디 또는 패스워드를 입력해 주세요");
			return;
		}
		
		$.ajax({
			 type		: "POST"
			,url		: "/AptLogin.do"
			,dataType	: "json"
			,data		: {
				"USER_ID" : $('#USER_ID').val()
				,"USER_PWD" : $('#USER_PWD').val()
			}
			,success : function(transport) {
				if(transport.ERROR_CD != '900'){
					alert("로그인에 실패하였습니다.아이디 또는 패스워드를 확인하세요.");	
					return;
				}else{
					fileDownFrmFooter.action= '/main.do';
					fileDownFrmFooter.submit();		
				}
			}
			,error : function(transport) { //ERROR
				alert("로그인에 실패하였습니다.아이디 또는 패스워드를 확인하세요.");	
			}
		});
	}	
	
	
	function fnNaverLogin(){
		if($('#USER_ID').val() == "" || $('#USER_PWD').val() == ""){
			alert("아이디 또는 패스워드를 입력해 주세요");
			return;
		}
		
		$.ajax({
			 type		: "POST"
			,url		: "/AptLogin.do"
			,dataType	: "json"
			,data		: {
				"USER_ID" : $('#USER_ID').val()
				,"USER_PWD" : $('#USER_PWD').val()
			}
			,success : function(transport) {
				if(transport.ERROR_CD != '900'){
					alert("로그인에 실패하였습니다.아이디 또는 패스워드를 확인하세요.");	
					return;
				}else{
					fileDownFrmFooter.action= '/main.do';
					fileDownFrmFooter.submit();		
				}
			}
			,error : function(transport) { //ERROR
				alert("로그인에 실패하였습니다.아이디 또는 패스워드를 확인하세요.");	
			}
		});
	}
	
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
				<h1>입주자<span>&nbsp;회원관리 시스템</span></h1>
				<h2>발급받은 아이디와 비밀번호를 사용해 주세요.<br/>발급받지 않은 계정 로그인 시에는 카카오 또는 네이버로 로그인 해 주세요.</h2>
				<div>&nbsp;</div>
				<fieldset>
					<legend>로그인</legend>
					<dl>
						<dt>
							<ul>
								<li><input type="text" name="USER_ID" id="USER_ID" value="" title="아이디" class="lgID" placeholder="ID" maxlength="20" onkeyup="if(event.keyCode==13){fnAptLogin();}"/></li>
								<li><input type="password" name="USER_PWD" id="USER_PWD" value="" title="비밀번호" class="lgPW" placeholder="Password" onkeyup="if(event.keyCode==13){fnAptLogin();}"/></li>
							</ul> 
							<div>
								<a href="javascript:fnAptLogin();">로그인</a>
								<a href="javascript:fnKaKaoLogin();" style="background:#fae630; color:black;margin-top: 5px;">카카오 로그인</a>
								<a href="/naver_login/naverlogin.jsp" style="background:#ffffff;margin-top: 5px;"><img alt="네이버 로그인" src="/static_root/images/btnIcn/naver_Login.png"></a>
							</div>
						</dt>
					</dl>
				</fieldset>
			</div>				
		</div>
		<!-- 로그인 끝 -->
	</div>
</form>
</body>
<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
<script type="text/javascript">
	//카카오 로그인
	function fnKaKaoLogin() {
		Kakao.Auth.login({
		      success: function (response) {
		        Kakao.API.request({
		          url: '/v2/user/me',
		          success: function (res) {
		        	  $.ajax({
		     			 type		: "POST"
		     			,url		: "/KoKaoLogin.do"
		     			,dataType	: "json"
		     			,data		: {
		     				"USER_ID" : res.id
		     				,"USER_NM" : res.properties['nickname']
		     				,"PROFILE_IMAGE" : res.properties['profile_image']
		     			}
		     			,success : function(transport) {
		     				if(transport.ERROR_CD != '900'){
		     					alert("로그인에 실패하였습니다.아이디 또는 패스워드를 확인하세요.");	
		     					return;
		     				}else{
		     					fileDownFrmFooter.action= '/main.do';
		     					fileDownFrmFooter.submit();		
		     				}
		     			}
		     			,error : function(transport) { //ERROR
		     				alert("로그인에 실패하였습니다.아이디 또는 패스워드를 확인하세요.");	
		     			}
		     		});
		          },
		          fail: function (error) {
		            console.log(error)
		          },
		        })
		      },
		      fail: function (error) {
		        console.log(error)
		      },
		    })
	}
</script>
</html>