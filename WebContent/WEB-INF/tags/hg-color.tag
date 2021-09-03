<%@ tag body-content="empty" pageEncoding="utf-8" %>
<%@ attribute name="value" required="true" type="java.lang.String"%>
<%

	String[] hak_nm = null;

	hak_nm = new String[] {
		"나노융합공학부",
		"산업경영공학과",
		"전자IT기계자동차공학부",
		"컴퓨터공학부",
		"물리치료학과",
		"식품생명과학부",
		"의용공학부",
		"디자인학부",
		"보건행정학과"
	};

	if(value != null && !"".equals(value)) {
		for(int i = 0; i < hak_nm.length; i++){
			if(hak_nm[i].equals(value)) {
				out.print("colorCha");
			} else {
				out.print("");
			}
		}
	}
%>