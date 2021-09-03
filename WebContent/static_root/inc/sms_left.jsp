<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script language="javascript">
$(document).ready(function(){
	
});

function fnLeftMenuMove(a_lmc, url){
	var leftFrm = document.leftFrm;
	leftFrm.A_LMC.value= a_lmc;
	leftFrm.target= "_self";
	leftFrm.action=url;
	leftFrm.submit();
}


</script>
<form name="leftFrm" id="leftFrm" method="post" action="">
	<input type="hidden" name="S_TMC" id="S_TMC" value="${INIT_DATA.S_TMC }"> 
	<input type="hidden" name="S_LMC" id="S_LMC" value="${INIT_DATA.S_LMC }"> 
</form>
	<div class="leftMenu" id="leftMenu">
		<div class="sms_userLeftMenuWrap userLeftMenuWrap"><p class="userLeftMenu"></p></div>
		<dl id="">
			<dt>
				<a href="javascript:fnLeftMenuMove('S_LMC001','/Sms/Main.do');" <c:if test="${INIT_DATA.S_LMC eq 'S_LMC001'}">class="on"</c:if>>문자발송</a>
			</dt>
		</dl>
	</div>
