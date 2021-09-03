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
	<input type="hidden" name="A_TMC" id="A_TMC" value="${INIT_DATA.A_TMC }"> 
	<input type="hidden" name="A_LMC" id="A_LMC" value="${INIT_DATA.A_LMC }"> 
</form>
	<div class="leftMenu" id="leftMenu">
		<div class="adm_userLeftMenuWrap userLeftMenuWrap"><p class="userLeftMenu"></p></div>
		<dl id="">
			<dt>
				<a href="javascript:fnLeftMenuMove('A_LMC001','/amc/amcMain.do');" <c:if test="${INIT_DATA.A_LMC eq 'A_LMC001'}">class="on"</c:if>>입예협 사용자 관리</a>
				<a href="javascript:fnLeftMenuMove('A_LMC002','/amc/amcDongHosuL.do');" <c:if test="${INIT_DATA.A_LMC eq 'A_LMC002'}">class="on"</c:if>>아파트 동/호수 관리</a>
				<a href="javascript:fnLeftMenuMove('A_LMC003','/amc/amcAptInfoL.do');" <c:if test="${INIT_DATA.A_LMC eq 'A_LMC003'}">class="on"</c:if>>단지/세대/평면도 관리</a>
				<a href="javascript:fnLeftMenuMove('A_LMC004','/amc/amcNoticeL.do');" <c:if test="${INIT_DATA.A_LMC eq 'A_LMC004'}">class="on"</c:if>>공지사항 관리</a>
				<a href="#" <c:if test="${INIT_DATA.A_LMC eq 'A_LMC005'}">class="on"</c:if>>전체회원 목록</a>
				<a href="#" <c:if test="${INIT_DATA.A_LMC eq 'A_LMC006'}">class="on"</c:if>>Q&A 관리</a>
				<a href="#" <c:if test="${INIT_DATA.A_LMC eq 'A_LMC007'}">class="on"</c:if>>견적문의 목록</a>
				<a href="#" <c:if test="${INIT_DATA.A_LMC eq 'A_LMC008'}">class="on"</c:if>>로그인 현황</a>
			</dt>
		</dl>
	</div>
