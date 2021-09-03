<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt"        prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"        prefix="fmt" %>
<%@ taglib tagdir="/WEB-INF/tags"        				prefix="uTld" %>
<%@ page import="apt.framework.util.MessageUtil" %>
<%
	String COMMON_JS_CONF = MessageUtil.getMessage("COMMON_JS_CONF");				// 사용자 JS 경로
	String COMMON_IMAGES_CONF = MessageUtil.getMessage("COMMON_IMAGES_CONF");		// 사용자 이미지 경로
	String COMMON_CSS_CONF = MessageUtil.getMessage("COMMON_CSS_CONF");			// 사용자 CSS 경로
	String COMMON_INC_CONF = MessageUtil.getMessage("COMMON_INC_CONF");				// 사용자 공통 JSP 경로
	String EDITOR_INC_URL_CONF = MessageUtil.getMessage("EDITOR.INC.URL");				// 에디터 경로
	String STATIC_ROOT = MessageUtil.getMessage("STATIC.ROOT");							// 포털 SSL 경로
%>
<c:set var="COMMON_JS_CONF" value="<%=COMMON_JS_CONF %>" />
<c:set var="COMMON_IMAGES_CONF" value="<%=COMMON_IMAGES_CONF %>" />
<c:set var="COMMON_CSS_CONF" value="<%=COMMON_CSS_CONF %>" />
<c:set var="COMMON_INC_CONF" value="<%=COMMON_INC_CONF %>" />
<c:set var="EDITOR_INC_URL_CONF" value="<%=EDITOR_INC_URL_CONF %>" />
<c:set var="REQL_URL" value="<%=request.getServletPath()%>" />
<script language="javascript">
var COMMON_JS_CONF = "<%=COMMON_JS_CONF%>";						// 사용자 JS 경로

var COMMON_IMAGES_CONF = "<%=COMMON_IMAGES_CONF%>";				// 사용자 이미지 경로 
var COMMON_CSS_CONF = "<%=COMMON_CSS_CONF%>";						// 사용자 CSS 경로  
var COMMON_INC_CONF = "<%=COMMON_INC_CONF%>";						// 사용자 공통 JSP 경로
var EDITOR_INC_URL_CONF = "<%=EDITOR_INC_URL_CONF%>";			// 에디터 경로
var N_DEPT_LOCT_CD = '${sessionScope.COMMON_CAMP_GB}';			// 에디터 경로
</script>