<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE>
<html>
	<head>
	<%@ include file="/static_root/inc/usr_top.jsp" %>
    <link type="text/css" rel="stylesheet" href="<c:out value='${COMMON_CSS_CONF}' />/fUpload.css" media=""/>
		<script type="text/javascript">
			$(document).ready(function() {
				$("#input_file").bind('change', function() {
					selectFile(this.files);
				});
				
				$("body").prepend('<div id="overlay" class="ui-widget-overlay" style="z-index: 1001; display: none;"></div>');
				$("body").prepend('<div id="dialog" style="z-index: 1001; position:fixed; top:50%; left:50%; padding:.2em; width:50px; height:50px; display:none;"><img src="'+COMMON_IMAGES_CONF+'/common/loading.gif" /></div>');
				$("#dialog, #overlay").hide(); //첫 시작시 로딩바를 숨겨준다.

				}).ajaxStart(function(){
					$("#dialog, #overlay").show(); //ajax실행시 로딩바를 보여준다.
				})
				.ajaxStop(function(){
					$("#dialog, #overlay").hide(); //ajax종료시 로딩바를 숨겨준다.
			});
		
			// 파일 리스트 번호
			var fileIndex = 0;
			// 등록할 전체 파일 사이즈
			var totalFileSize = 0;
			// 파일 리스트
			var fileList = new Array();
			// 파일 사이즈 리스트
			var fileSizeList = new Array();
			// 등록 가능한 파일 사이즈 MB
			var uploadSize = 500;
			// 등록 가능한 총 파일 사이즈 MB
			var maxUploadSize = 500;
	
			$(function() {
				// 파일 드롭 다운
				fileDropDown();
			});
	
			// 파일 드롭 다운
			function fileDropDown() {
				var dropZone = $("#dropZone");
				//Drag기능 
				dropZone.on('dragenter', function(e) {
					e.stopPropagation();
					e.preventDefault();
					// 드롭다운 영역 css
					dropZone.css('background-color', '#E3F2FC');
				});
				dropZone.on('dragleave', function(e) {
					e.stopPropagation();
					e.preventDefault();
					// 드롭다운 영역 css
					dropZone.css('background-color', '#FFFFFF');
				});
				dropZone.on('dragover', function(e) {
					e.stopPropagation();
					e.preventDefault();
					// 드롭다운 영역 css
					dropZone.css('background-color', '#E3F2FC');
				});
				dropZone.on('drop', function(e) {
					e.preventDefault();
					// 드롭다운 영역 css
					dropZone.css('background-color', '#FFFFFF');
	
					var files = e.originalEvent.dataTransfer.files;
					if (files != null) {
						if (files.length < 1) {
							console.log("폴더 업로드 불가");
							return;
						} else {
							selectFile(files)
						}
					} else {
						alert("ERROR");
					}
				});
			}
	
			// 파일 선택시
			function selectFile(fileObject) {
				var files = null;
	
				if (fileObject != null) {
					// 파일 Drag 이용하여 등록시
					files = fileObject;
				} else {
					// 직접 파일 등록시
					files = $('#multipaartFileList_' + fileIndex)[0].files;
				}
	
				var uploadFileList = Object.keys(fileList);
				// 다중파일 등록
				if (files != null) {
					
					if (files != null && files.length > 0) {
						$("#fileDragDesc").hide(); 
						$("fileListTable").show();
					} else {
						if (uploadFileList.length == 0) {
							$("#fileDragDesc").show(); 
						}
						$("fileListTable").hide();
					}
					
					for (var i = 0; i < files.length; i++) {
						// 파일 이름
						var fileName = files[i].name;
						var fileNameArr = fileName.split("\.");
						// 확장자
						var ext = fileNameArr[fileNameArr.length - 1].toLowerCase();
						
						var fileSize = files[i].size; // 파일 사이즈(단위 :byte)
						console.log("fileSize="+fileSize);
						if (fileSize <= 0) {
							console.log("0kb file return");
							return;
						}
						
						// 파일 사이즈(단위 :kb)
						var fileSizeKb = fileSize / 1024; 
						// 파일 사이즈(단위 :Mb)
						var fileSizeMb = fileSizeKb / 1024;	
						
						var fileSizeStr = "";
						if ((1024*1024) <= fileSize) {	// 파일 용량이 1메가 이상인 경우 
							console.log("fileSizeMb="+fileSizeMb.toFixed(2));
							fileSizeStr = fileSizeMb.toFixed(2) + " Mb";
						} else if ((1024) <= fileSize) {
							console.log("fileSizeKb="+parseInt(fileSizeKb));
							fileSizeStr = parseInt(fileSizeKb) + " kb";
						} else {
							console.log("fileSize="+parseInt(fileSize));
							fileSizeStr = parseInt(fileSize) + " byte";
						}
	
						var alertMsg = "";
						if($('#FILE_TYPES').val() == "IMAGE"){
							if ($.inArray(ext, [ 'png', 'jpg', 'jpeg', 'gif', 'bmp' ]) < 0) {
								alertMsg = "이미지 파일만 등록 가능합니다.\n등록불가 : " + fileName;
							}
						}else if($('#FILE_TYPES').val() == "DOCUMENT"){
							if ($.inArray(ext, [ 'hwp', 'doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx', 'txt', 'pdf' ]) < 0) {
								alertMsg = "문서 파일만 등록 가능합니다.\n등록불가 : " + fileName;
							}
						}else if($('#FILE_TYPES').val() == "EXCEL"){
							if ($.inArray(ext, [ 'xls', 'xlsx' ]) < 0) {
								alertMsg = "엑셀 파일만 등록 가능합니다.\n등록불가 : " + fileName;
							}
						}else if($('#LIMIT_FILE_EXT').val() != ""){
							if ( ext != $('#LIMIT_FILE_EXT').val() ){
								alertMsg = $('#LIMIT_FILE_EXT').val() + " 파일만 등록 가능합니다.\n등록불가 : " + fileName;
							}
						}
						
						if(alertMsg != ""){
							alert(alertMsg);
						}else if ($.inArray(ext, [ 'hwp', 'doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx', 'txt', 'png', 'pdf', 'jpg', 'jpeg', 'gif', 'zip', 'bmp' ]) < 0) {
							alert("등록이 불가능한 파일 입니다.("+fileName+")");
// 						} else if (fileSizeMb > uploadSize) { 
// 							// 파일 사이즈 체크
// 							alert("용량 초과\n업로드 가능 용량 : " + uploadSize + " MB");
// 							break;
						} else {
							// 전체 파일 사이즈
							totalFileSize += fileSizeMb;
							// 파일 배열에 넣기
							fileList[fileIndex] = files[i];
							// 파일 사이즈 배열에 넣기
							fileSizeList[fileIndex] = fileSizeMb;
							// 업로드 파일 목록 생성
							addFileList(fileIndex, fileName, fileSizeStr);
							// 파일 번호 증가
							fileIndex++;
						}
					}
				} else {
					alert("ERROR");
				}
			}
	
			// 업로드 파일 목록 생성
			function addFileList(fIndex, fileName, fileSizeStr) {
				var html = '';
				html += '<div id="fileTr_' + fIndex + '" class="file-row">';
		        html += '    	<p class="name" data-dz-name="">'+ fileName +'  ('+ fileSizeStr +'  KB)';
		        html += '    		<a href="#" onclick="deleteFile(' + fIndex + '); return false;"><img src="/static_root/images/btnIcn/btn_cancel.gif" alt="삭제" title="삭제" class="btn small bg_02" /></a>';
		        html += '    	</p>';
		        html += '    	<span id="uploadName_'+fIndex+'">업로드 준비</span>';
		      	html += '</div>';
	
				$('#fileTableTbody').append(html);
			}
	
			// 업로드 파일 삭제
			function deleteFile(fIndex) {
				console.log("deleteFile.fIndex=" + fIndex);
				// 전체 파일 사이즈 수정
				totalFileSize -= fileSizeList[fIndex];
	
				// 파일 배열에서 삭제
				delete fileList[fIndex];
	
				// 파일 사이즈 배열 삭제
				delete fileSizeList[fIndex];
				// 업로드 파일 테이블 목록에서 삭제
				$("#fileTr_" + fIndex).remove();
				console.log("totalFileSize="+totalFileSize);
				
				var uploadFileList = Object.keys(fileList);
				if (totalFileSize > 0) {
					$("#fileDragDesc").hide(); 
					$("fileListTable").show();
				} else {
					if (uploadFileList.length == 0) {
						$("#fileDragDesc").show(); 
					}
					$("fileListTable").hide();
				}
			}
			var fileArr = new Array();
			// 파일 등록
			function uploadFile() {
				// 등록할 파일 리스트
				var uploadFileList = Object.keys(fileList);
	
				// 파일이 있는지 체크
				if (uploadFileList.length == 0) {
					// 파일등록 경고창
					alert("선택된 파일이 없습니다.");
					return;
				}
				if($('#LIMIT_FILE_CNT').val() != "" && $('#LIMIT_FILE_CNT').val() != "0"){
					if (Number(uploadFileList.length) > Number($('#LIMIT_FILE_CNT').val())) {
						// 파일등록 경고창
						alert($('#LIMIT_FILE_CNT').val() + "개의 파일만 업로드 가능합니다.");
						return;
					}
				}
	
				// 용량을 500MB를 넘을 경우 업로드 불가
				if (totalFileSize > maxUploadSize) {
					// 파일 사이즈 초과 경고창
					alert("총 용량 초과\n총 업로드 가능 용량 : " + maxUploadSize + " MB");
					return;
				}
				if (confirm("등록 하시겠습니까?")) {
					// 등록할 파일 리스트를 formData로 데이터 입력
					var form = $('#uploadForm');
					var formData = [];
					var idxnm = 0;
					var finish = false;
					for (var i = 0; i < uploadFileList.length; i++) {
						formData = new FormData(form[0]);
						formData.append('files', fileList[uploadFileList[i]]);
						
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
							success : function(result) {
								var serverData = result.DATA;
			    	    		var rtn = serverData.substring(serverData.indexOf("/^")+2,serverData.lastIndexOf("/^"));
			    	    		var dataArr = rtn.split("|");
			    	    		var revObj = new Object();
			    	    		revObj.FILE_TYPE = dataArr[0];
			    	    		revObj.FILE_PATH = dataArr[1];
			    	    		revObj.REAL_FILE_NM = dataArr[2];
			    	    		revObj.TRANS_FILE_NM = dataArr[3];
			    	    		revObj.FILE_SIZE = 0;
			    	    		
			    	    		fileArr.push(revObj);
			    	    		
			    	    		$('#uploadName_'+idxnm).html("업로드 완료");
			    	    		idxnm ++;
							}
						});
						
						if(i==(uploadFileList.length-1)){
							finish= true;
						}

					}
					if(finish){
						setTimeout("fnUpFinish()", 300);
					}
				}
			}
			
			function fnUpFinish(){
				if(fileArr == null || fileArr == ""){
					alert("업로드 된 파일이 없습니다.");
					return;
				}
				if($('#RTN_FN_NM').val() != ""){
					eval("opener.${INIT_DATA.RTN_FN_NM}(fileArr)");
				}else{
					eval(opener.fnFileRetObject(fileArr));
				}
				self.close();
		}
		</script>
	</head>
	<body>
		<div id="uploadWrap">
			<div class="uploadTop" >
				<h1>업로드</h1>
				<p><a href="javascript:self.close();"><img src="/static_root/images/layer/layer_close.png" alt="닫기" title="닫기" /></a></p>
			</div>
			
			<div class="uploadFileArea">
				<div class="upload-btn-wrapper">
					<input type="file" id="input_file" multiple="multiple" style="height:100%;"/>
					<span class="btn btn-success">
						<i style="cursor:pointer;"></i>
						<span>파일선택</span>
					</span>
				</div>
			
				<form name="uploadForm" id="uploadForm" enctype="multipart/form-data" method="post">
					<input type="hidden" name="LIMIT_FILE_CNT" id="LIMIT_FILE_CNT" value="${INIT_DATA.LIMIT_FILE_CNT}">
					<input type="hidden" name="LIMIT_FILE_EXT" id="LIMIT_FILE_EXT" value="${INIT_DATA.LIMIT_FILE_EXT}">
					<input type="hidden" name="FILE_TYPES" id="FILE_TYPES" value="${INIT_DATA.FILE_TYPES}">
					<input type="hidden" name="RTN_FN_NM" id="RTN_FN_NM" value="${INIT_DATA.RTN_FN_NM}">
					<input type="hidden" name="F_PATH" id="F_PATH" value="${INIT_DATA.F_PATH}">
					<input type="hidden" name="USRID" id="USRID" value="${INIT_DATA.USRID}">
					<div id="dropZone">
						<div id="fileDragDesc"> 상단에 파일 선택 후 업로드 버튼을 눌러주세요. </div>
						<div id="fileListTable" style="width: 100%;">
							<div id="fileTableTbody">
							</div>
						</div>
					</div>
				</form>
			</div>
			
			<div class="uploadBottom">
				<a href="javascript:uploadFile();" id="UploadBtn">업로드</a>
				<a href="javascript:self.close();">취소</a>
			</div>
		</div>
	</body>
</html>