<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<!doctype html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page import="java.io.*, java.net.*" %>
<%
	String title = "연락처 목록";
	String file_name = "연락처 목록";
	
	file_name = title + ".xls";
	
	String header = request.getHeader("User-Agent");
	if(header.indexOf("MSIE") > -1 || header.indexOf("Trident") > -1) file_name = URLEncoder.encode(file_name, "UTF-8").replaceAll("\\+", "%20");
	else if(header.indexOf("Firefox") > -1) file_name = "\"" + new String(file_name.getBytes("UTF-8"), "8859_1") + "\"";
	else if(header.indexOf("Opera") > -1) file_name = "\"" + new String(file_name.getBytes("UTF-8"), "8859_1") + "\"";
	else if(header.indexOf("Chrome") > -1) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < file_name.length(); i++) {
			char c = file_name.charAt(i);
			if(c > '~') sb.append(URLEncoder.encode("" + c, "UTF-8"));
			else sb.append(c);
		}
		file_name = sb.toString();
	}
	else throw new IOException("Not supported browser");
	
	response.setHeader("Content-Disposition", "attachment; filename="+file_name+";");
	response.setHeader("Content-Description", "JSP Generated Data");
%>
</head>
<body>
	<table border="1" cellpadding="0" cellspacing="0" summary="연락처 목록">
		<thead>
			<tr>
				<th style="background-color:#E4E4E4;" colspan="2"><span style="color:#0033ff;">공동명의자 포함 연락처 입니다.</span></th>
			</tr>
			<tr>
				<th style="background-color:#E4E4E4;">연락처</th>
				<th style="background-color:#E4E4E4;">성명</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${not empty INIT_DATA.excelList }">
					<c:forEach items="${INIT_DATA.excelList }" var="item" varStatus="rowStatus">
							<tr>
								<td style="text-align:left; mso-number-format: '@' ;">
									${item.HP }
								</td>		
								<td style="text-align:left; mso-number-format: '@' ;">
									${item.USER_NM }
								</td>		
							</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr>
						<td colspan="2">검색 결과가 없습니다.</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>

</body>
</html>