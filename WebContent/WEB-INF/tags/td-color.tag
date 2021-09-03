<%@ tag body-content="empty" pageEncoding="utf-8" %><%@ attribute name="value" required="true" type="java.lang.String"%><%

	if(value != null && !"".equals(value)) {
		double dbl = Double.parseDouble(value);
	
		if(dbl >= 151) {
			out.print("color1");
		} else if(dbl >= 126) {
			out.print("color2");
		} else if(dbl >= 100) {
			out.print("color3");
		} else if(dbl >= 76) {
			out.print("color4");
		} else if(dbl >= 51) {
			out.print("color5");
		} else if(dbl >= 0) {
			out.print("color6");
		} else {
			out.print("");
		}
	}
%>