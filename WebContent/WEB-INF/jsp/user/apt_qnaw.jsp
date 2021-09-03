<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	<meta name="title" content="아파트관리" />
	<meta name="description" content="아파트,입예협,입주자관리,입주예정자협의회,시세조회,분양가조회, 실거래가 비교, 분양가 비교" />
	<meta name="keywords" content="아파트,입예협,입주자관리,입주예정자협의회,시세조회,분양가조회, 실거래가 비교, 분양가 비교" />
	<meta name="robots" content="index, follow">
	<link rel="canonical" href="http://www.allmyapt.com">
	<meta property="og:type" content="article" />
	<meta property="og:site_name" content="아파트관리">
	<meta property="og:type" content="article">
	<meta property="og:url" content="http://www.allmyapt.com">
	<meta property="og:title" content="아파트정보 | 입주자관리 | 실거래가 조회 | 거래가 비교">
	<meta property="og:image" content="/static_root/images/common/meta_img.jpg">
	<meta property="og:description" content="아파트관리 " />
	
	<title>아파트관리 | Q&A</title>
<link rel="stylesheet" href="/static_root/daumeditor/css/editor.css" type="text/css" charset="utf-8"/>
<script src="/static_root/daumeditor/js/editor_loader.js" type="text/javascript" charset="utf-8"></script>
<%@ include file="/static_root/inc/usr_top.jsp" %>
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
		$('#Listfrm').attr("action", "/user/apt_qnal.do");
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


</script>
</head>
<body id="user">
	<div id="wrap">
		<!-- 상단영역 -->
		<%@ include file="/static_root/inc/apt_header.jsp" %>
		<!-- 상단영역 -->
		
		<ul id="content">
			<li id="left">
				<!-- 좌측영역 -->
				<%@ include file="/static_root/inc/apt_left.jsp" %>
				<!-- 좌측영역 -->
			</li>
			<li id="right">
				<!-- 타이틀/네비 -->
				<div class="titleNaviNew">
					<h3 class="subTitle">Q&A 작성</h3>
				</div>
				<!-- 타이틀/네비 끝 -->

				<!-- 본문영역 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// -->
				<div id="cont">
					<form name="Listfrm" id="Listfrm" method="post" action="#">
						<input type="hidden" name="TMC" id="TMC" value="${INIT_DATA.TMC}" />
						<input type="hidden" name="LMC" id="LMC" value="${INIT_DATA.LMC}" />
					</form>
					<form name="frm" id="frm" method="post" action="/user/apt_qnaI.do">
						<input type="hidden" name="TMC" id="TMC" value="${INIT_DATA.TMC}" />
						<input type="hidden" name="LMC" id="LMC" value="${INIT_DATA.LMC}" />
						<input type="hidden" name="BOARD_SEQ" id="BOARD_SEQ" value="${INIT_DATA.BOARD_SEQ}" />
						<input type="hidden" name="ANSWER_YN"	id="ANSWER_YN" value="<c:out value="${INIT_DATA.ANSWER_YN}"/>"/>
						<input type="hidden" name="CURR_PAGE"	id="CURR_PAGE" value="<c:out value="${INIT_DATA.CURR_PAGE}"/>"/>
						<input type="hidden" name="PAGE_SIZE"	id="PAGE_SIZE" value="<c:out value="${INIT_DATA.PAGE_SIZE}"/>"/>
						<section class="sect_area sect_first_area">
							
							<div class="bbsView">
								<table summary="Q&A">
									<caption>Q&A</caption>
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
											<th>비밀글</th>
											<td>
												<label><input type="checkbox" name="SECRET_YN" id="SECRET_YN" value="Y" />&nbsp;&nbsp;비밀글</label>
											</td>
										</tr>
										<tr>
											<td colspan="2">
												<div id="editor_frame">
													<%@ include file="../editor/daumEditor.jsp" %>
													<textarea name="CONTENTS" id="CONTENTS" rows="10" cols="100" style="width:766px; height:412px;display: none;">${INIT_DATA.detail.CONTENTS}</textarea>
												</div>
											</td>	
										</tr>
									</tbody>
								</table>
							</div>
						</section>
						<section class="sect_btn_area">
							<ul>
								<li><a href="javascript:saveContent();" class="mBtn1">저장</a></li>
								<li><a href="#" onclick="fnList(); return false;" class="mBtn3">취소</a></li>
							</ul>							
						</section>
					</form>
				</div>
				<!-- 본문영역 끝 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// -->
			</li>
		</ul>
		<!-- 하단영역 -->
		<%@ include file="/static_root/inc/apt_footer.jsp" %>
		<!-- 하단영역 끝 -->
	</div>
</body>
</html>