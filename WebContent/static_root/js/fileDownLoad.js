       /******************************** 각종 파일 다운로드   ***************************************************************/	
		//게시판용 
		function fileDown(fileNo){
			var f = document.hiddenFrm;
			f.action="/adn/boardManagement.do?method=downLoad&FILE_NO="+fileNo;
			f.submit();
		}
		
		//사용자 게시판용 
		function userfileDown(fileNo){
			var f = document.hiddenFrm;
			f.action="/user/userBoard.do?method=downLoad&FILE_NO="+fileNo;
			f.submit();
		}
		