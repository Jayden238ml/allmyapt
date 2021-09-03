<%@ page contentType = "text/html;charset=euc-kr" %>
<%@ page import = "java.io.File,
				   com.oreilly.servlet.MultipartRequest,
		 		   com.oreilly.servlet.multipart.DefaultFileRenamePolicy"	
%>
<%	
	
	// IE이외의 브라우져에서 SESSION문제 때문에 필요
	if (request.getParameter("JSESSIONID")!=null) 
	{ 
		Cookie userCookie = new Cookie("JSESSIONID", request.getParameter("JSESSIONID")); 
		response.addCookie(userCookie); 
	}
	

	MultipartRequest multi = new MultipartRequest( request, "D:/webPro/yestrade/WebContent/static_root/userUpload", 1000 * 1024 * 1024, "UTF-8", new DefaultFileRenamePolicy() );


	// 파라미터 테스트 
	String p1 = multi.getParameter("param1");
	String p2 = multi.getParameter("param2");
	
	System.out.println("p1 ======================> " + p1);
	System.out.println("p2 ======================> " + p2);
		
	//	==================== SESSION GET TEST =========================
	System.out.println("세션 : 소속 = " + session.getAttribute("SOSOK") );
	System.out.println("세션 : 이름 = " + session.getAttribute("NAME") );
	System.out.println("세션 : 나이 = " + session.getAttribute("AGE") );
	

	
	//Cookie[] cc = request.getCookies();	
	//for(int i=0; i<cc.length; i++) System.out.println(cc[i].getName() + "=" + cc[i].getValue());
%>