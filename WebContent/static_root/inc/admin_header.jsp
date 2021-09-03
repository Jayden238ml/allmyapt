<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt"        prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"        prefix="fmt" %>
<link type="text/css" rel="stylesheet" href="<c:out value='${COMMON_CSS_CONF}' />/top-style.css" media=""/>
<%@ page import="apt.framework.common.DataMap" %>
<%
	DataMap dataMap = (DataMap)request.getAttribute("INIT_DATA");
	if ("".equals(dataMap.getString("SESSION_USER_ID")) || !"Y".equals(dataMap.getString("SESSION_ADMIN_YN")) || !"AMC".equals(dataMap.getString("SESSION_USER_TYPE"))) {
		response.sendRedirect("/main.do");
	}
%>

<script language="javascript">
function fnMain(){
	location.href = "/amc/amcMain.do";
}

function logout(){
	location.href="/logOut.do";
}

function openLogin(TYPE){
	gfnOpenLayerPopup('/common/index.do?jpath=/ourapt/pop_login&TYPE='+TYPE);
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
	
	// 모바일 전체메뉴 //
	var mobileLnbChk = 0;
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

function fnMobileClickTopMenu(url){
	var headerfrm = document.headerfrm;
	headerfrm.target= "_self";
	headerfrm.action=url;
	headerfrm.submit();
}

function fnAmcSa(){
	var headerfrm = document.headerfrm;
	headerfrm.target= "_self";
	headerfrm.action= "/amc/amcMain.do";
	headerfrm.submit();
}
</script>
<!-- 상단영역 (모바일) -->
<form name="headerfrm" id="headerfrm" method="post">
	<input type="hidden" name="A_TMC" id="A_TMC" value="${INIT_DATA.A_TMC}" />
	<input type="hidden" name="A_LMC" id="A_LMC" value="${INIT_DATA.A_LMC}" />
</form>
<div id="a_header">
	<dl>
		<dt><a href="/main.do"><img src="/static_root/images/common/apt_adm_logo.jpg" alt="올마이컴퍼니" title="올마이컴퍼니" /></a></dt>
		<dd>
			<ul>
				<li><a href="javascript:fnClickTopMenu('A_TMC0001');" >시스템 관리</a></li>
			</ul>
		</dd>
		<dt style="padding-top:2%;">
			<div>
				<b><c:out value="${INIT_DATA.SESSION_USER_NM}"/></b> 님 반갑습니다.&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="/main.do">사용자</a>	
				<a href="/logOut.do">로그아웃</a>				
			</div>
		</dt>
	</dl>
</div>
<!-- 상단영역 끝 -->


<!-- 상단영역 - 모바일 -->
<div id="header-mobile">
	<ul class="hm-top">
		<li>
			<h1><a href="javascript:fnMain();"><img src="/static_root/images/common/apt_adm_logo.jpg" height="32" alt="올마이컴퍼니" title="올마이컴퍼니" /></a></h1>
		</li>
		<li><i class="ion-android-menu" aria-hidden="true" id="mobileLnbOpen"></i></li>
	</ul>
	
	<div class="total-bg">&nbsp;</div>
	
		<div class="hm-total">
		<ul class="total-tit">
			<li><img src="/static_root/images/common/apt_adm_logo.jpg" height="30" alt="올마이컴퍼니" title="올마이컴퍼니" /></li>
			<li><i id="mobileLnbClose" class="ion-close" aria-hidden="true"></i></li>
		</ul>
		<ul class="total-btn">
			<li>
				<a href="/main.do">사용자</a> 
				<li><a href="/logOut.do" id="mobileLogin">로그아웃</a></li>
			</li>
		</ul>
		<div class="total-list">
			<ul class="hm-menu">
				<li class="hm-sub sub-depth1"><a href="#"><span>시스템 관리</span> <i class="ion-android-add" aria-hidden="true"></i></a>
				<ul>
					<li class="sub-depth2"><a href="javascript:fnMobileClickTopMenu('/amc/amcMain.do');"><span>입예협 사용자 관리</span></a></li>
					<li class="sub-depth2"><a href="javascript:fnMobileClickTopMenu('/amc/amcDongHosuL.do');"><span>아파트 동/호수 관리</span></a></li>
					<li class="sub-depth2"><a href="javascript:fnMobileClickTopMenu('/amc/amcAptInfoL.do');"><span>단지/세대/평면도 관리</span></a></li>
					<li class="sub-depth2"><a href="javascript:fnMobileClickTopMenu('/amc/amcNoticeL.do');"><span>공지사항 관리</span></a></li>
				</ul>
			</ul>
		</div>
	</div>
</div>
<!-- 상단영역 끝 -->