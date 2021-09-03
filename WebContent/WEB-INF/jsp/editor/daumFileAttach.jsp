<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>이미지 첨부</title> 
<%-- <%@ include file="/static_root/inc/usr_top.jsp" %> --%>
<script type="text/javascript" src="/static_root/js/jquery-1.7.2.min.js" ></script>
<script src="/static_root/daumeditor/js/popup.js" type="text/javascript" charset="utf-8"></script>
<link rel="stylesheet" href="/static_root/daumeditor/css/popup.css" type="text/css"  charset="utf-8"/>
<script type="text/javascript">
// <![CDATA[
	$(document).ready(function() {
		
	});
	
	
	
// 	function done() {
// 		if (typeof(execAttach) == 'undefined') { //Virtual Function
// 	        return;
// 	    }
		
// 		var _mockdata = {
// 			'imageurl': 'http://cfile284.uf.daum.net/image/116E89154AA4F4E2838948',
// 			'filename': 'editor_bi.gif',
// 			'filesize': 640,
// 			'imagealign': 'C',
// 			'originalurl': 'http://cfile284.uf.daum.net/original/116E89154AA4F4E2838948',
// 			'thumburl':    'http://cfile284.uf.daum.net/P150x100/116E89154AA4F4E2838948'
// 		};
// 		execAttach(_mockdata);
// 		closeWindow();
// 	}
	
	function done(){
		var form = $('#attachFrm');
		var formData = [];
		formData = new FormData(form[0]);
		$.ajax({
			url : "/common/swfUpload.do",
			data : formData,
			type : 'POST',
			enctype : 'multipart/form-data',
			processData : false,
			contentType : false,
			dataType : 'json',
			async : false,
			cache : false,
			success : function(transport) {
				var obj = transport.jobj
				alert(obj.imageurl);
				var _mockdata = {
		 			'imageurl': obj.imageurl,
		 			'filename': obj.filename,
		 			'filesize': obj.filesize,
		 			'imagealign': 'C',
		 			'originalurl': obj.originalurl,
		 			'thumburl':    obj.imageurl
		 		};
		 		execAttach(_mockdata);
		 		closeWindow();
			}
		});
	}

// ]]>
</script>
</head>
<body>
<div class="wrapper">
	<div class="header">
		<h1>사진 첨부</h1>
	</div>	
	<div class="body">
		<dl class="alert">
		    <dt>사진 첨부 확인</dt>
		    <dd>
		    	<form name="attachFrm" id="attachFrm" method="post" enctype="multipart/form-data" action="#">
		    		<input type="file" name="atte1" id="atte1" class="file_input_hidden" />
		    	</form>
			</dd>
		</dl>
	</div>
	<div class="footer">
		<p><a href="#" onclick="closeWindow();" title="닫기" class="close">닫기</a></p>
		<ul>
			<li class="submit"><a href="#" onclick="done();" title="등록" class="btnlink">등록</a> </li>
			<li class="cancel"><a href="#" onclick="closeWindow();" title="취소" class="btnlink">취소</a></li>
		</ul>
	</div>
</div>
</body>
</html>
