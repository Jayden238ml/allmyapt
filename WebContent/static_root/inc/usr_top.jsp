<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%
response.setHeader("Pragma", "no-cache"); 
response.setHeader("Cache-Control", "no-cache"); 
%>
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
<title>올마이티컴퍼니</title>
<%@ include file="apt_common.jsp" %>
<link rel="shortcut icon" href="/static_root/images/common/favicon.ico">
<link type="text/css" rel="stylesheet" href="<c:out value='${COMMON_CSS_CONF}' />/apt_common.css" media=""/><!-- 초기화/공통/레이아웃 -->
<link type="text/css" rel="stylesheet" href="<c:out value='${COMMON_CSS_CONF}' />/apt_option.css" media=""/><!-- css 옵션 -->
<link type="text/css" rel="stylesheet" href="<c:out value='${COMMON_CSS_CONF}' />/apt_bbsTable.css" media=""/><!-- 게시물/테이블 -->
<link type="text/css" rel="stylesheet" href="<c:out value='${COMMON_CSS_CONF}' />/apt_content.css" media=""/><!-- 개별컨텐츠 -->
<link type="text/css" rel="stylesheet" href="<c:out value='${COMMON_CSS_CONF}' />/apt_main.css" media=""/><!-- 메인페이지 -->
<link type="text/css" rel="stylesheet" href="<c:out value='${COMMON_CSS_CONF}' />/apt.css" media=""/><!-- 메인페이지 -->
<link type="text/css" rel="stylesheet" href="<c:out value='${COMMON_CSS_CONF}' />/nyroModal.css" media=""/>


<script type="text/javascript" src="<c:out value='${COMMON_JS_CONF}' />/jquery-1.7.2.min.js" ></script>
<script type="text/javascript" src="<c:out value='${COMMON_JS_CONF}' />/jquery.form.js" ></script>
<script type="text/javascript" src="<c:out value='${COMMON_JS_CONF}' />/jquery.cookie.js" ></script>
<script type="text/javascript" src="<c:out value='${COMMON_JS_CONF}' />/jqueryPaging.js"></script><!-- 페이징  -->
<script type="text/javascript" src="<c:out value='${COMMON_JS_CONF}' />/valid.js"></script> <!--  공통  js  -->
<script type="text/javascript" src="<c:out value='${COMMON_JS_CONF}' />/_link.js"></script> <!--  공통  js  -->
<script type="text/javascript" src="<c:out value='${COMMON_JS_CONF}' />/fileDownLoad.js"></script> <!--  공통  js  -->
<script type="text/javascript" src="<c:out value='${COMMON_JS_CONF}' />/apt_common.js"></script> <!--  공통  js  -->
<script type="text/javascript" src="<c:out value='${COMMON_JS_CONF}' />/jquery.nyroModal.custom.js"></script> <!--  레이어  js  -->
<script type="text/javascript" src="<c:out value='${COMMON_JS_CONF}' />/calendar/jquery-ui.min.js" ></script><!-- 달력 javascript -->
<script type="text/javascript" src="<c:out value='${COMMON_JS_CONF}' />/json2.js"></script> <!--  공통  js  -->

</head>
<%
//치환 변수 선언
pageContext.setAttribute("cn", "\n"); //Enter
pageContext.setAttribute("newcn", "<br>"); //br 태그
pageContext.setAttribute("cr", "\u0020"); //Space
pageContext.setAttribute("newcr", "&nbsp;"); //Space
%>

<script>

$(document).ready(function () {
	
});


</script>
<body>
<script type="text/javascript" src="//wcs.naver.net/wcslog.js"></script> <script type="text/javascript"> if(!wcs_add) var wcs_add = {}; wcs_add["wa"] = "e5a374cfaf69d8"; if(window.wcs) {wcs_do(); } </script>
</body>