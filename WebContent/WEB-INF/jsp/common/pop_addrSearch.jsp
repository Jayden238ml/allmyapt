<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ include file="/static_root/inc/admin_head.jsp" %>
<script type="text/javascript">
var vRetFnName = 'fnSetPostNum';

function getAddr(){
	$.ajax({
		 url :"https://www.juso.go.kr/addrlink/addrLinkApiJsonp.do"  
		,type:"post"
		,data:$("#popFrm").serialize()
		,dataType:"jsonp"
		,beforeSend : gfnStartProgress
		,complete   : gfnEndProgress
		,crossDomain:true
		,success:function(xmlStr){
			if(navigator.appName.indexOf("Microsoft") > -1){
				var xmlData = new ActiveXObject("Microsoft.XMLDOM");
				xmlData.loadXML(xmlStr.returnXml)
			}else{
				var xmlData = xmlStr.returnXml;
			}
			$("#bodyData").html("");
			var errCode = $(xmlData).find("errorCode").text();
			var errDesc = $(xmlData).find("errorMessage").text();
			if(errCode != "0"){
				alert(errDesc);
			}else{
				if(xmlStr != null){
					makeList(xmlData);
				}
			}
		}
	    ,error: function(xhr,status, error){
	    	alert("에러발생");
	    }
	});
}

function makeList(xmlStr){
	var htmlStr = "";
    var seq = 0;
   
	$(xmlStr).find("juso").each(function(){
		var vReturn = vRetFnName + "(\'" + $(this).find('zipNo').text().substring(0,3) + "\',\'" + $(this).find('zipNo').text().substring(3,6) + "\',\'" + $(this).find('roadAddrPart1').text() + "\');gfnLayerClose();";
		seq++;
		htmlStr += '<tr>';
		htmlStr += '	<th style="text-align:center; padding:8px 5px 8px 5px">'+seq+'</th>';
		htmlStr += '	<th style="text-align:center; padding:8px 5px 8px 5px">'+ $(this).find('zipNo').text().substring(0,3) + '-' + $(this).find('zipNo').text().substring(3,6) +'</th>';
		htmlStr += '	<th><a href="javascript:' + vReturn + '">'+ $(this).find('roadAddrPart1').text() +'</a></th>';
		htmlStr += '</tr>';
	});
	if(htmlStr==""){
		htmlStr += '<tr>';
		htmlStr += '	<th style="text-align:center; padding:8px 5px 8px 5px" colspan="3">검색 결과가 없습니다</th>';
		htmlStr += '</tr>';
	}
	$("#bodyData").html(htmlStr);
}
</script>
<!--주소 검색 -->
<div class="layerWrap500Dev layerIDSearch" style="display:block;">
	<dl class="layerTitle">
		<dt>주소 검색</dt>
		<dd><a href="javascript:gfnLayerClose();" class="cbtn"><img src="${COMMON_IMAGES_CONF }/layer/layer_close.png" alt="닫기" title="닫기" /></a></dd>
	</dl>
	<div class="layerTLine">&nbsp;</div>

	<div class="layerArea">
		<!-- 검색 -->
		<form name="popFrm" id="popFrm" method="post" action="#" onsubmit="return false;">
			<input type="hidden" name="currentPage" id="currentPage" value="1">
			<input type="hidden" name="countPerPage" id="countPerPage" value="1000">
			<input type="hidden" name="confmKey" id="confmKey" value="U01TX0FVVEgyMDE1MDYwMjE1NDkyMA==">
			<fieldset>
				<legend>검색</legend>
				<div class="searchTable">
					<table cellpadding="0" cellspacing="0" summary="검색하세요.">
						<caption>검색</caption>
						<colgroup>
							<col width="28%" />
							<col width="*" />
						</colgroup>
						<tr>
							<th>Search</th>
							<td>
								<input type="text" name="keyword" id="keyword" onkeyup="if(event.keyCode==13){getAddr();}" title="검색어" class="input w200" value="" maxlength="50" />
								<input type="button" value="검색" class="btnS w50" title="검색" onclick="getAddr();" />
							</td>
						</tr>
					</table>
				</div>
			</fieldset>
		</form>
		<!-- 검색 끝 -->

		<!-- 리스트 -->
		<div class="tableListTitle mt20">
			<table cellpadding="0" cellspacing="0" summary="목록">
					<caption>목록</caption>
					<colgroup>
						<col width="60" />
						<col width="120" />
						<col width="*" />
					</colgroup>
					<tr>
						<th>순번</th>
						<th>우편번호</th>
						<th>주소</th>
					</tr>
			</table>
		</div>
		<div class="tableListCtn">
			<table cellpadding="0" cellspacing="0" summary="내용">
				<caption>내용</caption>
				<colgroup>
					<col width="60" />
					<col width="120" />
					<col width="*" />
				</colgroup>
				<tbody id="bodyData">
				</tbody>
			</table>
		</div>
		<!-- 리스트 끝 -->

		<div class="ac mt20">
			<a href="#" class="btn1 w50 cbtn" onclick="gfnLayerClose();return false;">닫기</a>
		</div>
	</div>

	<div class="layerBLine">&nbsp;</div>
</div>
<!-- ID 검색 끝 -->