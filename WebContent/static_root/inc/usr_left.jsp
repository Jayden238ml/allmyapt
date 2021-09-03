<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script language="javascript">
$(document).ready(function(){
	
});

function fnLeftMenuMove(lmc){
	$.ajax({ 
		 type : "post"
		,url  : "/moveMenu.do"
		,data : {
			"LMC" 	  :lmc
			,"GB" : "L"
		}
		,dataType : "json"
		,success : function(transport) {					
			if(transport.RET_MSG != ''){
				alert(transport.RET_MSG);
			}else{
				var resultMst = eval(transport.leftMenu);
				var leftFrm = document.leftFrm;
				var tmc = resultMst.PRTCODE;
				$('#L_TMC').val(tmc);
				$('#L_LMC').val(lmc);
				leftFrm.target= "_self";
				if(resultMst.MENU_URL.indexOf("?") > -1){
					leftFrm.action=resultMst.MENU_URL;
				}else{
					leftFrm.action=resultMst.MENU_URL;
				}
				leftFrm.submit();
			}
			
		}
	});
}


</script>
<form name="leftFrm" id="leftFrm" method="post" action="">
	<input type="hidden" name="L_TMC" id="L_TMC" value="${sessionScope.TMC }"> 
	<input type="hidden" name="L_LMC" id="L_LMC" value="${sessionScope.LMC }"> 
</form>
	<div class="leftMenu" id="leftMenu">
		<div class="userLeftMenuWrap"><p class="userLeftMenu"></p></div>
		<c:if test="${not empty INIT_DATA.USER_LEFT_MENU.USERLEFTMENU }">
			<c:forEach var="item" items="${INIT_DATA.USER_LEFT_MENU.USERLEFTMENU}">
				<c:if test="${sessionScope.TMC eq item.PRTCODE }">
					<dl id="${item.MENU_CODE}"><dt><a href="javascript:fnLeftMenuMove('${item.MENU_CODE}');" <c:if test="${sessionScope.LMC eq item.MENU_CODE}">class="on"</c:if>>${item.MENUNM}</a></dt></dl>
				</c:if>
			</c:forEach>
		</c:if>
	</div>
