<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<link rel="stylesheet" href="/static_root/daumeditor/css/editor.css" type="text/css" charset="utf-8"/>
<script src="/static_root/daumeditor/js/editor_loader.js" type="text/javascript" charset="utf-8"></script>
<%@ include file="/static_root/inc/admin_head.jsp" %>
</head>
<script type="text/javascript">	
	$(document).ready(function(){
		var config = {
		        txHost:'',
		        txPath:'',
		        txService:'sample',
		        txProject:'sample',
		        initializedId:"",
		        wrapper:"tx_trex_container",
		        form:'frm'+"",
		        txDecoPath:"/static_root/daumeditor/images/deco/contents/",
		        canvas: {
		            styles: {
		                color:"#123456",/* 기본 글자색 */
		                fontFamily:"굴림",/* 기본 글자체 */
		                fontSize:"10pt",/* 기본 글자크기 */
		                backgroundColor:"#fff",/*기본 배경색 */
		                lineHeight:"1.5",/*기본 줄간격 */
		                padding:"8px" /* 위지윅 영역의 여백 */
		            },
		            showGuideArea: false
		        },
		        events: {
		            preventUnload: false
		        },
		        sidebar: {
		            attachbox: {
		                show: true,
		                confirmForDeleteAll: true
		            }
		        },
		        size: {
		            contentWidth: 1000
		        }
		    };
		 
		    EditorJSLoader.ready(function(Editor) {
		        var editor =new Editor(config);
		    });
		    
		    Editor.modify({
	    	  "content": document.getElementById("CONTENTS") /* 내용 문자열, 주어진 필드(textarea) 엘리먼트 */
	    	});
	});
	
	
	function fnList(){
		$('#Listfrm').attr("action", "/amc/amcNoticeL.do");
		$('#Listfrm').submit();
	}
	
	function saveContent(){
		if($('#TITLE').val() == ""){
			alert("제목을 입력해 주세요.");
			$('#TITLE').focus();
			return;
		}
		if(confirm("저장하시겠습니까?")){
			Editor.save();
		}
	}
	
	function validForm(editor) {
        var validator = new Trex.Validator();
        var content = editor.getContent();
        if (!validator.exists(content)) {
            alert('내용을 입력하세요');
            return false;
        }
        return true;
    }
	
	function setForm(editor) {
        var content = editor.getContent();
        $("#CONTENTS").val(content);
        return true;
    }
	
	function fnDaumFileUpload(){
		window.open("/common/index.do?jPath=editor/daumFileAttach"
				,"swfUpload","top=65px, left=250px, height=350px, width=380px, status=no, location=no ,toolbar=no,menubar=no,resizable=no");
	}
	
	function fnDelete(){
		if(confirm("삭제 하시겠습니까?")){
			$('#Listfrm').attr("action", "/amc/amcNoticeDelete.do");
			$('#Listfrm').submit();
		}
	}
	
	
</script>
<body id="user">
	<div id="wrap">
			<%@ include file="/static_root/inc/admin_header.jsp" %>
		<ul id="content">
			<li id="left">
				<!-- 좌측영역 -->
				<%@ include file="/static_root/inc/admin_left.jsp" %>
				<!-- 좌측영역 끝 -->
			</li>
			<li id="right">
				<!-- 타이틀/네비 -->
				<div class="titleNaviNew">
					<h3 class="subTitle">공지사항 관리</h3>
				</div>
				<!-- 타이틀/네비 끝 -->
				<div id="cont">
					<form name="Listfrm" id="Listfrm" method="post" action="#">
						<input type="hidden" name="A_TMC" id="A_TMC" value="${INIT_DATA.A_TMC}" />
						<input type="hidden" name="A_LMC" id="A_LMC" value="${INIT_DATA.A_LMC}" />
						<input type="hidden" name="BOARD_SEQ" id="BOARD_SEQ" value="${INIT_DATA.BOARD_SEQ}" />
					</form>
					<form name="frm" id="frm" method="post" action="/amc/amcNoticeInsert.do">
						<input type="hidden" name="A_TMC" id="A_TMC" value="${INIT_DATA.A_TMC}" />
						<input type="hidden" name="A_LMC" id="A_LMC" value="${INIT_DATA.A_LMC}" />
						<input type="hidden" name="BOARD_SEQ" id="BOARD_SEQ" value="${INIT_DATA.BOARD_SEQ}" />
						<section class="sect_area sect_first_area">
							
							<div class="bbsView">
								<table summary="공지사항">
									<caption>공지사항</caption>
									<colgroup>
										<col width="20%" />
										<col width="*" />
									</colgroup>
									<tbody>
										<tr>
											<th>제목</th>
											<td>
												<input type="text" name="TITLE" id="TITLE" value="<c:out value='${INIT_DATA.detail.TITLE}'/>" class="input w99p" title="제목" maxlength="100"  />
											</td>
										</tr>
										<tr>
											<td colspan="2">
												<div id="editor_frame">
													<%@ include file="../../editor/daumEditor.jsp" %>
													<textarea name="CONTENTS" id="CONTENTS" rows="10" cols="100" style="width:766px; height:412px;display: none;">${INIT_DATA.detail.CONTENTS}</textarea>
												</div>
											</td>	
										</tr>
									</tbody>
								</table>
							</div>
							<dl class="bvBtn">
								<dt>&nbsp;</dt>
								<dd>
									<a href="javascript:saveContent();" class="mBtn1">저장</a>
									<c:if test="${INIT_DATA.BOARD_SEQ ne ''}">
										<a href="javascript:fnDelete();" class="mBtn4">삭제</a>
									</c:if>
									<a href="javascript:fnList();" class="mBtn3">취소</a>
								</dd>
							</dl>
						</section>
					</form>
				</div>
			</li>
		</ul>
	</div>
	<!-- 메인 콘텐츠영역 끝 -->
	<!-- 하단영역 -->
	<%@ include file="/static_root/inc/admin_footer.jsp" %>
	<!-- 하단영역 끝 -->
	
</body>
</html>