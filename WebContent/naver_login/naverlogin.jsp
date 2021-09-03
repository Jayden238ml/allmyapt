<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.security.SecureRandom" %>
<%@ page import="java.math.BigInteger" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
  </head>
  <%
  
    String clientId = "89ZmyNVzbPmazPd_3XvM";
    String redirectURI = URLEncoder.encode("http://allmyapt.com/naverSuccess.do", "UTF-8");
//     String redirectURI = URLEncoder.encode("http://localhost:8089/naverSuccess.do", "UTF-8");
    SecureRandom random = new SecureRandom();
    String state = new BigInteger(130, random).toString();
    String apiURL = "https://nid.naver.com/oauth2.0/authorize?response_type=code";
    apiURL += "&client_id=" + clientId;
    apiURL += "&redirect_uri=" + redirectURI;
    apiURL += "&state=" + state;
    session.setAttribute("state", state);
    
    response.sendRedirect(apiURL);
 %>
<body>
</body>
</html>