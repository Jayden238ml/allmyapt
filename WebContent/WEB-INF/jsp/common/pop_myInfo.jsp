<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html lang="ko">
<head>
<title>나의 정보 확인</title>
<%@ include file="/static_root/inc/apt_top.jsp" %>
<script type="text/javascript">
	function fn_Modify(){
		var rtn = true;
		if($('#USER_NM').val() == ""){
			alert("이름을 입력해 주세요.");
			return;
		}
		if($('#USER_PWD').val() != ""){
			if($('#NEW_USER_PWD').val() == ""){
				alert("비밀번호 변경 할 경우 새로운 비밀번호도 입력해 주세요.\n(비밀번호 미 변경 시 현재 비밀번호 미입력)");
				return;
			}
			if($('#NEW_USER_PWD').val() != ""){
				if($('#NEW_USER_PWD').val().length < 7){
					alert("비밀번호는 8자 이상으로 입력해 주세요.");
					return;
				}
				rtn = fnPwdChk();
			}
		}
		
		setTimeout(function() {
			if(rtn){
				fnMyInfoChange();
			}
    	}, 1000); 
	}	
	
	function fnPwdChk(){
		var rtn = true;
		$.ajax({
			 type		: "POST"
			,url		: "/PwdChk.do"
			,dataType	: "json"
			,async : false
			,cache : false
			,data		: {
				"USER_ID" : $('#USER_ID').val()
				,"USER_PWD" : $('#USER_PWD').val()
				,"NEW_USER_PWD" : $('#NEW_USER_PWD').val()
			}
			,success : function(transport) {
				if(transport.PWD_YN == 'N'){
					alert("입력한 현재 비밀번호가 다릅니다.\n현재 비밀번호를 다시 확인해 주세요.");	
					rtn = false;
					return;
				}else{
					rtn = true;
				}
			}
		});
		return rtn;
	}
	
	function fnMyInfoChange(){
		$.ajax({
			 type		: "POST"
			,url		: "/MyInfoChange.do"
			,dataType	: "json"
			,data		: {
				"USER_NM" : $('#USER_NM').val()
				,"USER_HP" : $('#USER_HP').val()
				,"USER_PWD" : $('#USER_PWD').val()
				,"NEW_USER_PWD" : $('#NEW_USER_PWD').val()
			}
			,success : function(transport) {
				if(transport.ERROR_CD == '999'){
					alert("처리중 에러가 발생하였습니다.");	
					return;
				}else{
					alert("변경되었습니다.로그인 후 다시 이용해 주세요.");
					gfnLayerClose();
				}
			}
		});
	}
	
</script>
</head>
<body>
<form name="popFrm" id="popFrm" method="post">
	<div class="layerWrap680 login_pop_inner">
		<div class="bg"></div>
		<!-- 로그인 -->
		<div class="loginLayer">
			<p>
				<c:if test="${INIT_DATA.TYPE eq 'P'}">
					<a href="javascript:gfnLayerClose();" class="cbtn"><img src="/static_root/images/content/popClose.jpg" alt="닫기" title="닫기" /></a>
				</c:if>
				<c:if test="${INIT_DATA.TYPE eq 'M'}">
					<a href="javascript:gfnMobileLayerClose();" class="cbtn"><img src="/static_root/images/content/popClose.jpg" alt="닫기" title="닫기" /></a>
				</c:if>
			</p>
			<div id="loginDiv">
				<h1><span>내 정보</span> 확인</h1>
				<br/>
				<fieldset>
					<div class="tableView">
						<table summary="위임장 일괄 업로드">
							<caption>위임장 일괄 업로드</caption>
							<colgroup>
								<col width="30%" />
								<col width="*" />
							</colgroup>
							<tr>
								<th>이름</th>
								<td>
									<input type="text" name="USER_NM" id="USER_NM" title="이름" class="input w99p" value="${INIT_DATA.SESSION_USER_NM}" maxlength="50"  placeholder="이름"/>
								</td>
							</tr>
							<tr>
								<th>연락처</th>
								<td>
									<input type="text" name="USER_HP" id="USER_HP" title="연락처" class="input w99p" value="${INIT_DATA.SESSION_USER_HP}" maxlength="50"  placeholder="연락처"/>
								</td>
							</tr>
							<tr>
								<th colspan="2">
									* 비밀번호 변경 시에만 입력
								</th>
							</tr>
							<tr>
								<c:if test="${INIT_DATA.ISMOBILE eq 'M'}">
									<th>현재<br/>비밀번호</th>
								</c:if>
								<c:if test="${INIT_DATA.ISMOBILE eq 'P'}">
									<th>현재 비밀번호</th>
								</c:if>
								<td>
									<input type="password" name="USER_PWD" id="USER_PWD" title="현재 비밀번호" class="input w99p" value="" maxlength="50"  placeholder="현재 비밀번호"/>
								</td>
							</tr>
							<tr>
								<c:if test="${INIT_DATA.ISMOBILE eq 'M'}">
									<th>새로운<br/>비밀번호</th>
								</c:if>
								<c:if test="${INIT_DATA.ISMOBILE eq 'P'}">
									<th>새로운 비밀번호</th>
								</c:if>
								<td>
									<input type="password" name="NEW_USER_PWD" id="NEW_USER_PWD" title="새로운 비밀번호" class="input w99p" value="" maxlength="50"  placeholder="새로운 비밀번호"/>
								</td>
							</tr>
						</table>
					</div>
					<div class="ac mt15">
						<a href="javascript:fn_Modify();" class="btn3 w50">저장</a>
					</div>
					<br/>
				</fieldset>
			</div>				
		</div>
		<!-- 로그인 끝 -->
	</div>
</form>
</body>
</html>