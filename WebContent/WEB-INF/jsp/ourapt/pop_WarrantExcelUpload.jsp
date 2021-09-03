<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html lang="ko">
<head>
<title>로그인</title>
<%@ include file="/static_root/inc/apt_top.jsp" %>
<script type="text/javascript">
	var idx = 0;
	$(document).ready(function(){
		document.popFrm.TMC.value = document.frm.TMC.value;
		document.popFrm.LMC.value = document.frm.LMC.value;
		$("body").prepend('<div id="overlay" class="ui-widget-overlay" style="z-index: 1001; display: none;"></div>');
		$("body").prepend('<div id="dialog" style="z-index: 1001; position:fixed; top:50%; left:50%; padding:.2em; width:50px; height:50px; display:none;"><img src="'+COMMON_IMAGES_CONF+'/common/j_loading.gif" /></div>');
		$("#dialog, #overlay").hide(); //첫 시작시 로딩바를 숨겨준다.
	
		}).ajaxStart(function(){
			$("#dialog, #overlay").show(); //ajax실행시 로딩바를 보여준다.
		})
		.ajaxStop(function(){
			$("#dialog, #overlay").hide(); //ajax종료시 로딩바를 숨겨준다.
	});
	//첨부파일 리턴 1
	function setConFile1(retArr){
		var file_nm = "";
		var real_file_nm = "";
		var usrHtml = "";
		if($('#FILETAG1 li').size() >= 1){ 
			alert("첨부파일은 하나만 업로드 가능 합니다.");
			return;
		}
		for ( var i = 0; i < retArr.length; i++) {
			idx++;
			file_nm = retArr[i].TRANS_FILE_NM;
			real_file_nm = retArr[i].REAL_FILE_NM;
			var ext = real_file_nm.lastIndexOf(".");
			var len = real_file_nm.length; // 파일이름의 길이 
	  		var extnm =real_file_nm.substring(ext+1); // 파일 이름의 마지막 확장자 substring
			if(extnm != "xlsx"){
				alert("xlsx 파일만 업로드 해주세요.");
				return;
			}
			usrHtml += '<li id="file_1">';
			usrHtml += '&nbsp;' + real_file_nm;
			usrHtml += '&nbsp;&nbsp;&nbsp;<a href="javascript:removefile(1);"><img src="<c:out value='${COMMON_IMAGES_CONF}' />/btnIcn/btn_cancel.gif" alt="제거" title="제거" /></a>';
			usrHtml += '<input type="hidden" name="FILEPATH" id="FILEPATH" value="'+ retArr[i].FILE_PATH +'" >';
			usrHtml += '<input type="hidden" name="TRANSFILENM" id="TRANSFILENM" value="'+ retArr[i].TRANS_FILE_NM +'" >';
			usrHtml += '</li>';
		}
		$('#FILETAG1').append(usrHtml); 
		$('#FILENAME').val(file_nm);
	}
	
	function removefile(id){
		$('#file_' + id).remove();
		if(id == "2"){
			$('#FILENAME'+ id).val("");
		}else{
			$('#FILENAME').val("");
		}
	}
	
	function fn_Appr(){
		if($('#FILETAG1 li').size() > 1){ 
			alert("첨부파일은 하나만 업로드 가능 합니다.");
			return;
		}
		if($('#FILETAG1 li').size() == 0){ 
			alert("양식을 받아 첨부 후 등록 가능 합니다.");
			return;
		}
		if(confirm("등록하시겠습니까?")){
			$.ajax({
				type: "POST",
				url  : "/apt/myWarrantExcelUpload.do",
				data:  $("#popFrm").serialize(),
				dataType: "json",
				success: function (transport){
					if(transport.ERROR_CD == "999"){
						alert("처리 중 오류가 발생 하였습니다.");
						return;
					}else{
						alert(transport.COUNT + "건이 등록 되었습니다.");
						fnSearch();
						gfnLayerClose();
					}
				}
			});
		}
	}
	
</script>
</head>
<body id="pop_form" style="min-width:500px; width:100%; min-height:100%;">
	<!-- 레이어팝업 - 상담 신청 -->
	<div class="layerWrap500 pop_inner_wrap" style="display:block;">
		<dl class="layerTitle bg450">
			<dt>위임장 일괄 업로드</dt>
			<dd><a href="javascript:gfnLayerClose();" class="cbtn"><img src="${COMMON_IMAGES_CONF }/layer/layer_close.png" alt="닫기" title="닫기" /></a></dd>
		</dl>
		<div class="popArea">
			<form name="popFrm" id="popFrm" method="post" action="#">
				<input type="hidden" name="TMC" id="TMC" value="" />
				<input type="hidden" name="LMC" id="LMC" value="" />
				<fieldset>
					<legend>위임장 일괄 업로드</legend>
					<h2 class="title2">위임장 일괄 업로드</h2>
					<div class="tableView">
						<table summary="위임장 일괄 업로드">
							<caption>위임장 일괄 업로드</caption>
							<colgroup>
								<col width="22%" />
								<col width="*" />
							</colgroup>
							<tr>
								<th>양식 다운로드</th>
								<td>
									<a href="/static_root/common/ExcelUplod.xlsx" target="Pop_excelFormDown" class="btn3 w80">다운로드</a>
								</td>
							</tr>
							<tr>
								<th>첨부파일</th>
								<td>
									<a href="javascript:fnAspFileUpload('1','CC','setConFile1','${INIT_DATA.SESSION_USR_ID}');" class="btn4 w80">파일찾기</a>
									<ul class="" id="FILETAG1">
										<input type="hidden" name="FILENAME" id="FILENAME" value="" />
									</ul>
								</td>
							</tr>
						</table>
					</div>
					
					<div class="ac mt15">
						<a href="javascript:fn_Appr();" class="btn4 w50">등록</a>
						<a href="javascript:gfnLayerClose();" class="btn1 w50 cbtn" >닫기</a>
					</div>
				</fieldset>
			</form>
		</div>
	</div>
	<iframe src="about:blank" name="Pop_excelFormDown" width="0" height="0" frameborder="0" border="0"  style="display:none;"></iframe>
</body>
</html>