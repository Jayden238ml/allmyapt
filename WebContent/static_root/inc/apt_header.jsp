<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt"        prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"        prefix="fmt" %>
<link type="text/css" rel="stylesheet" href="<c:out value='${COMMON_CSS_CONF}' />/top-style.css" media=""/>
<script language="javascript">
	//모바일 전체메뉴 //
	var mobileLnbChk = 0;
	function fnMain(){
		location.href = "/main.do";
	}
	
	function logout(){
		location.href="/logOut.do";
	}
	
	function openLogin(TYPE){
		gfnOpenLayerPopup('/common/index.do?jpath=/common/pop_login&TYPE='+TYPE);
	}
	
	function fnMyInfoPop(TYPE){
		if(TYPE == "M"){
			$('#header-mobile .hm-total').animate({ marginRight : '-100%' }, 350, 'easeOutQuad');
			mobileLnbChk = 0;
		}else{
			$('#header-mobile .hm-total').animate({ marginRight : '-100%' }, 350, 'easeOutQuad');
		}
		gfnOpenLayerPopup('/common/index.do?jpath=/common/pop_myInfo&TYPE='+TYPE);
	}
	
	$(document).ready(function() {
		$(".subGnb").hover(
			function() {
				$(".subGnb").css("display", "block");
			},
			function() {
				$(".subGnb").css("display", "none");
			}
		);
		
		$(".sub_depth2 > li > a").hover(
			function() {
				$(".sub_depth3 > ul").css("display", "block");
			}
		);
		
		$('#mobileLnbOpen').click(function(){ 
			if (mobileLnbChk == 0) {
				$('#header-mobile .hm-total').animate({ marginRight : '0' }, 350, 'easeOutQuad');
				mobileLnbChk = 1;
			} else {
				$('#header-mobile .hm-total').animate({ marginRight : '-100%' }, 350, 'easeOutQuad');
				mobileLnbChk = 0;
			}
		});
		$('#mobileLnbClose').click(function(){ 
			$('#header-mobile .hm-total').animate({ marginRight : '-100%' }, 350, 'easeOutQuad');
			mobileLnbChk = 0;
		});	
		$('#mobileLogin').click(function(){ 
			$('#header-mobile .hm-total').animate({ marginRight : '-100%' }, 350, 'easeOutQuad');
			mobileLnbChk = 0;
		});	
		
		$('.total-list li.hm-sub>a').on('click', function(){
			$(this).removeAttr('href');
			var element = $(this).parent('li');
			if (element.hasClass('open')) {
				element.removeClass('open');
				element.find('li').removeClass('open');
				element.find('ul').slideUp();
			}
			else {
				element.addClass('open');
				element.children('ul').slideDown();
				element.siblings('li').children('ul').slideUp();
				element.siblings('li').removeClass('open');
				element.siblings('li').find('li').removeClass('open');
				element.siblings('li').find('ul').slideUp();
			}
		});
		
		//팝업창
		$('#header-mobile > .hm-top > li:nth-child(2)').click(function(){
			$('.total-bg').show();
		});
		$('#header-mobile > .hm-total > ul > li:nth-child(2)').click(function(){
			$('.total-bg').hide();
		});	
	});		
	
	 
	function fnClickTopMenu(tmc){
		
		if(tmc == "TMC003"){
			if("${INIT_DATA.SESSION_USER_ID}" == ""){
				alert("로그인 이후 시스템 사용이 가능합니다.");
				return;
			}
		}
		 
		 $.ajax({ 
			 type : "post"
			,url  : "/moveMenu.do"
			,data : {
				"TMC" 	  :tmc
				,"GB" : "T"
			}
			,dataType : "json"
			,success : function(transport) {					
				if(transport.RET_MSG != ''){
					alert(transport.RET_MSG);
				}else{
					var resultMst = eval(transport.leftMenu);
					var headerfrm = document.headerfrm;
					currentMenuCd = resultMst.MENU_CODE;
					$('#H_TMC').val(tmc);
					$('#H_LMC').val(currentMenuCd);
					headerfrm.target= "_self";
					if(resultMst.MENU_URL.indexOf("?") > -1){
						headerfrm.action=resultMst.MENU_URL;
					}else{
						headerfrm.action=resultMst.MENU_URL;
					}
					headerfrm.submit();
				}
				
			}
		});
	}
	
	function fnMobileClickTopMenu(lmc){
		$.ajax({ 
			 type : "post"
			,url  : "/moveMenu.do"
			,data : {
				"LMC" 	  :lmc
				,"GB" : "L"
			}
			,dataType : "json"
			,success : function(transport) {					
				if(transport.RET_MSG != ''){
					alert(transport.RET_MSG);
				}else{
					var resultMst = eval(transport.leftMenu);
					var headerfrm = document.headerfrm;
					var tmc = resultMst.PRTCODE;
					$('#H_TMC').val(tmc);
					$('#H_LMC').val(lmc);
					headerfrm.target= "_self";
					if(resultMst.MENU_URL.indexOf("?") > -1){
						headerfrm.action=resultMst.MENU_URL;
					}else{
						headerfrm.action=resultMst.MENU_URL;
					}
					headerfrm.submit();
				}
				
			}
		});
	}
	
	function fnAmcSa(){
		var headerfrm = document.headerfrm;
		headerfrm.target= "_self";
		headerfrm.action= "/amc/amcMain.do";
		headerfrm.submit();
	}
	
	function fnSmcSa(){
		var headerfrm = document.headerfrm;
		headerfrm.target= "_self";
		headerfrm.action= "/Sms/Main.do";
		headerfrm.submit();
	}
	
	function fnLoginChk(){
		if("${INIT_DATA.SESSION_USER_ID}" == ""){
			alert("로그인 이후 시스템 사용이 가능합니다.");
			return;
		}
		var headerfrm = document.headerfrm;
		headerfrm.target= "_self";
		headerfrm.action= "/apt/apt_Search.do?TMC=TMC003&LMC=LMC018";
		headerfrm.submit();
	}
</script>
<!-- 상단영역 -->
<form name="headerfrm" id="headerfrm" method="post">
	<input type="hidden" name="H_TMC" id="H_TMC" value="${sessionScope.TMC}" />
	<input type="hidden" name="H_LMC" id="H_LMC" value="${sessionScope.LMC}" />
</form>
<div id="a_header">
	<dl>
		<dt><a href="/main.do"><img src="/static_root/images/common/apt_logo.jpg" alt="올마이컴퍼니" title="올마이컴퍼니" /></a></dt>
		<dd>
			<ul>
				<c:if test="${not empty INIT_DATA.USER_TOP_MENU}">
					<c:forEach var="item" items="${INIT_DATA.USER_TOP_MENU}">
						<c:if test="${item.MENU_CODE eq 'TMC003'}">
							<li><a href="javascript:fnClickTopMenu('${item.MENU_CODE}');" <c:if test="${sessionScope.TMC eq item.MENU_CODE}">class="on"</c:if> title="<c:out value='${item.MENUNM}'/>"><c:out value="${item.MENUNM}"/></a></li>
						</c:if>
						<c:if test="${item.MENU_CODE ne 'TMC003'}">
							<c:set var="left_code" value="LMC001"/>
							<c:if test="${item.MENU_CODE eq 'TMC002'}">
								<c:set var="left_code" value="LMC005"/>
							</c:if>
							<c:if test="${item.MENU_CODE eq 'TMC004'}">
								<c:set var="left_code" value="LMC014"/>
							</c:if>
							<li><a href="${item.MENU_URL}?LMC=${left_code}&TMC=${item.MENU_CODE}" <c:if test="${sessionScope.TMC eq item.MENU_CODE}">class="on"</c:if> title="<c:out value='${item.MENUNM}'/>"><c:out value="${item.MENUNM}"/></a></li>
						</c:if>
					</c:forEach>
				</c:if>
			</ul>
		</dd>
		<dt style="padding-top:2%;">
			<c:if test="${empty INIT_DATA.SESSION_USER_ID}">
				<div>
					<span><a href="javascript:openLogin('P');">로그인</a>	</span>
				</div>
			</c:if>
			<c:if test="${not empty INIT_DATA.SESSION_USER_ID }">
				<div>
					<b><c:out value="${INIT_DATA.SESSION_USER_NM}"/></b> 님 반갑습니다.&nbsp;&nbsp;&nbsp;&nbsp;
					<c:if test="${INIT_DATA.SESSION_USER_TYPE ne 'K' and INIT_DATA.SESSION_USER_TYPE ne 'N' and INIT_DATA.SESSION_USER_TYPE ne 'SMC'}">
						<c:if test="${INIT_DATA.SESSION_ADMIN_YN eq 'Y'}">
							<a href="javascript:fnAmcSa();">관리자</a>	
						</c:if>
						<c:if test="${INIT_DATA.SESSION_ADMIN_YN ne 'Y'}">
							<a href="javascript:fnMyInfoPop('P');">나의정보</a>	
						</c:if>
						<a href="/logOut.do">로그아웃</a>				
					</c:if>
					<c:if test="${INIT_DATA.SESSION_USER_TYPE eq 'SMC'}">
						<a href="javascript:fnSmcSa();">문자발송</a>	
						<a href="/logOut.do">로그아웃</a>				
					</c:if>
					<c:if test="${INIT_DATA.SESSION_USER_TYPE eq 'K' or INIT_DATA.SESSION_USER_TYPE eq 'N'}">
						<span><a href="/logOut.do">로그아웃</a></span>	
					</c:if>
				</div>
			</c:if>
		</dt>
	</dl>
</div>
<!-- 상단영역 끝 -->


<!-- 상단영역 - 모바일 -->
<div id="header-mobile">
	<ul class="hm-top">
		<li>
			<h1><a href="javascript:fnMain();"><img src="/static_root/images/common/apt_logo.jpg" height="32" alt="올마이컴퍼니" title="올마이컴퍼니" /></a></h1>
		</li>
		<li><i class="ion-android-menu" aria-hidden="true" id="mobileLnbOpen"></i></li>
	</ul>
	
	<div class="total-bg">&nbsp;</div>
	
		<div class="hm-total">
		<ul class="total-tit">
			<li><img src="/static_root/images/common/apt_logo.jpg" height="30" alt="올마이컴퍼니" title="올마이컴퍼니" /></li>
			<li><i id="mobileLnbClose" class="ion-close" aria-hidden="true"></i></li>
		</ul>
		<ul class="total-btn">
			<li>
				<c:if test="${INIT_DATA.SESSION_USER_ID eq ''}">
					<li><a href="javascript:openLogin('M');" id="mobileLogin" style="width:200%;">로그인</a></li>
				</c:if>
				<c:if test="${INIT_DATA.SESSION_USER_ID ne ''}">
					<c:if test="${INIT_DATA.SESSION_USER_TYPE ne 'K' and INIT_DATA.SESSION_USER_TYPE ne 'N' and INIT_DATA.SESSION_USER_TYPE ne 'SMC'}">
						<a href="javascript:logout();">로그아웃</a> 
						<c:if test="${INIT_DATA.SESSION_ADMIN_YN eq 'Y'}">
							<li><a href="javascript:fnAmcSa();">관리자</a></li>
						</c:if>
						<c:if test="${INIT_DATA.SESSION_ADMIN_YN ne 'Y'}">
							<li><a href="javascript:fnMyInfoPop('M');">나의정보</a></li>
						</c:if>
					</c:if>
					<c:if test="${INIT_DATA.SESSION_USER_TYPE eq 'SMC'}">
						<a href="javascript:logout();">로그아웃</a> 
						<li><a href="javascript:fnSmcSa();">문자발송</a></li>
					</c:if>
					<c:if test="${INIT_DATA.SESSION_USER_TYPE eq 'K' or INIT_DATA.SESSION_USER_TYPE eq 'N'}">
						<a href="javascript:logout();" style="width:200%;">로그아웃</a> 
					</c:if>
				</c:if>
			</li>
		</ul>
		<div class="total-list">
			<ul class="hm-menu">
				<li class="hm-sub sub-depth1"><a href="#"><span>사이트 안내</span> <i class="ion-android-add" aria-hidden="true"></i></a>
				<ul>
					<li class="sub-depth2"><a href="/user/apt_contv.do?TMC=TMC001&LMC=LMC001" title="사이트 안내"><span>사이트 안내</span></a></li>
					<li class="sub-depth2"><a href="/user/apt_qnal.do?TMC=TMC001&LMC=LMC002" title="Q&A"><span>Q&A</span></a></li>
					<li class="sub-depth2"><a href="/user/apt_reqw.do?TMC=TMC001&LMC=LMC003" title="견적문의"><span>견적문의</span></a></li>
					<li class="sub-depth2"><a href="/user/apt_notil.do?TMC=TMC001&LMC=LMC004" title="공지사항"><span>공지사항</span></a></li>
				</ul>
				<li class="hm-sub sub-depth1"><a href="#"><span>아파트 정보</span> <i class="ion-android-add" aria-hidden="true"></i></a>
				<ul>
					<li class="sub-depth2"><a href="/user/apt_info1L.do?TMC=TMC002&LMC=LMC005" title="아파트정보"><span>아파트정보</span></a></li>
				</ul>
					<li class="hm-sub sub-depth1"><a href="#"><span>입주민 전용</span> <i class="ion-android-add" aria-hidden="true"></i></a>
				<c:if test="${fn:indexOf(INIT_DATA.SESSION_AUTH_CD, 'APT') ne -1}">
					<ul>
						<li class="sub-depth2"><a href="javascript:fnMobileClickTopMenu('LMC008');"><span>위임장 정보</span></a></li>
						<li class="sub-depth2"><a href="javascript:fnMobileClickTopMenu('LMC009');"><span>위임장 변경이력</span></a></li>
						<li class="sub-depth2"><a href="javascript:fnMobileClickTopMenu('LMC010');"><span>회비관리</span></a></li>
						<li class="sub-depth2"><a href="javascript:fnMobileClickTopMenu('LMC017');"><span>문자발송</span></a></li>
						<li class="sub-depth2"><a href="javascript:fnMobileClickTopMenu('LMC019');"><span>입금/발송이력</span></a></li>
<%-- 						<c:if test="${INIT_DATA.SESSION_USER_TYPE  eq 'NG'}"> --%>
<!-- 							<li class="sub-depth2"><a href="#"><span>네이버 회원</span></a></li> -->
<%-- 						</c:if> --%>
<%-- 						<c:if test="${INIT_DATA.SESSION_USER_TYPE  eq 'TG'}"> --%>
<!-- 							<li class="sub-depth2"><a href="#"><span>네이버 회원</span></a></li> -->
<!-- 							<li class="sub-depth2"><a href="#"><span>컨설팅 관리</span></a></li> -->
<%-- 						</c:if> --%>
					</ul>
				</c:if>
				<ul>
					<li class="sub-depth2"><a href="javascript:fnLoginChk();" title="아파트 실거래이력"><span>입주자 검색</span></a></li>
				</ul>
				<li class="hm-sub sub-depth1"><a href="#"><span>실거래가 조회</span> <i class="ion-android-add" aria-hidden="true"></i></a>
				<ul>
					<li class="sub-depth2"><a href="/user/apt_TradingL.do?TMC=TMC004&LMC=LMC014" title="아파트 실거래이력"><span>아파트 실거래이력</span></a></li>
					<li class="sub-depth2"><a href="/user/apt_ConceL.do?TMC=TMC004&LMC=LMC013" title="아파트분양권 거래이력"><span>아파트분양권 거래이력</span></a></li>
					<li class="sub-depth2"><a href="/user/apt_CompareL.do?TMC=TMC004&LMC=LMC015" title="실거래가 비교"><span>실거래가 비교</span></a></li>
					<li class="sub-depth2"><a href="/user/apt_OutCompareL.do?TMC=TMC004&LMC=LMC016" title="분양권 비교"><span>분양권 비교</span></a></li>
				</ul>
			</ul>
		</div>
	</div>
</div>
<!-- 상단영역 - 모바일 끝 -->
<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
<script type="text/javascript">
	Kakao.init('e5c7d9bd5172ded7a64bab139c62c331');
</script>

