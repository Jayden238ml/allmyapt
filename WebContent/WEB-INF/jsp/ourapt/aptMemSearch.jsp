<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<head>
<title>아파트관리 | 입주자 검색</title>
<%@ include file="/static_root/inc/apt_top.jsp" %>
<script type="text/javascript">
	$(document).ready(function(){
		if("${INIT_DATA.SCH_APT_CODE}" != ""){
			fnGetDongHosu("${INIT_DATA.SCH_APT_CODE}", 'D');
		}
		if("${INIT_DATA.SCH_DONG}" != ""){
			setTimeout(function() {
				fnGetDongHosu("${INIT_DATA.SCH_DONG}", 'H');
	   		}, 500); 
		}
		
		if("${INIT_DATA.HP1}" == ""){
			$('#HP1').val("010");
		}
		
		if("${INIT_DATA.DATA_YN}" == "Y"){
			$('#DIVSHOW').show();
		}
	})
	
	function fnSearch(){
		if($('#SCH_APT_CODE').val() == ""){
			alert("아파트명을 선택해 주세요.");
			$('#SCH_APT_CODE').focus();
			return;
		}
		if($('#SCH_DONG').val() == ""){
			alert("동을 선택해 주세요.");
			$('#SCH_DONG').focus();
			return;
		}
		if($('#SCH_HOSU').val() == ""){
			alert("호수를 선택해 주세요.");
			$('#SCH_HOSU').focus();
			return;
		}
		if($('#USER_NM').val() == ""){
			alert("이름을 입력해 주세요.");
			$('#USER_NM').focus();
			return;
		}
		if($('#HP1').val() == ""){
			alert("연락처를 입력해 주세요.");
			$('#HP1').focus();
			return;
		}
		if($('#HP2').val() == ""){
			alert("연락처를 입력해 주세요.");
			$('#HP2').focus();
			return;
		}
		if($('#HP3').val() == ""){
			alert("연락처를 입력해 주세요.");
			$('#HP3').focus();
			return;
		}
		
		$('#SEARCH_YN').val("Y");
		$('#frm').attr("action", "/apt/apt_Search.do");
		$('#frm').submit();
	}
	
	function fnGetDongHosu(val, type){
		$('#SELECT_TYPE').val(type);
		$.ajax({
			type: "POST",
			url : "/apt/getDongAndHosuList.do",
			data:  $("#frm").serialize(),
			dataType: "json",
			success: function (transport) {
				if(type == "H"){
					fnSetHosu(eval(transport.HosuList));
				}else if(type == "D"){
					fnSetDong(eval(transport.DongList));
				}
			}
		});
	}
	
	function fnSetDong(DongList){
		$("#SCH_DONG").html("");
		var resultHtml ="";
		var row =  "<option value=''>전체</option>";
		$.each(DongList,function(key,json) {
			row += '<option value="'+json.DONG+'">'+json.DONG+'동</option>';
		});
		resultHtml += row;
		$("#SCH_DONG").html(resultHtml);
		if("${INIT_DATA.SCH_DONG}" != ""){
			$("#SCH_DONG").val("${INIT_DATA.SCH_DONG}");
		}
	}
	function fnSetHosu(HosuList){
		$("#SCH_HOSU").html("");
		var resultHtml ="";
		var row =  "<option value=''>전체</option>";
		$.each(HosuList,function(key,json) {
			row += '<option value="'+json.HOSU+'">'+json.HOSU+'호</option>';
		});
		resultHtml += row;
		$("#SCH_HOSU").html(resultHtml);
		if("${INIT_DATA.SCH_HOSU}" != ""){
			$("#SCH_HOSU").val("${INIT_DATA.SCH_HOSU}");
		}
	}
	
	
</script>
</head>
<body id="user">
	<div id="wrap">
		<!-- 상단영역 -->
		<%@ include file="/static_root/inc/apt_header.jsp" %>
		<!-- 상단영역 끝 -->

		<ul id="content">
			<li id="left">
				<!-- 좌측영역 -->
				<%@ include file="/static_root/inc/apt_left.jsp" %>
				<!-- 좌측영역 끝 -->
			</li>
			<li id="right">
				<!-- 타이틀/네비 -->
				<div class="titleNaviNew">
					<h3 class="subTitle">입주자 검색</h3>
				</div>
				<!-- 타이틀/네비 끝 -->

				<!-- 본문내용 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// -->
				<div id="cont">
					<section class="sect_area sect_first_area">
						<!-- 검색 -->
						<form name="frm" id="frm" method="post" action="#">
							<input type="hidden" name="TMC" id="TMC" value="${INIT_DATA.TMC}" />
							<input type="hidden" name="LMC" id="LMC" value="${INIT_DATA.LMC}" />
							<input type="hidden" name="SELECT_TYPE" id="SELECT_TYPE" value="${INIT_DATA.SELECT_TYPE}" />
							<input type="hidden" name="SEARCH_YN" id="SEARCH_YN" value="${INIT_DATA.SEARCH_YN}" />
							<div class="bbsView" >
								<table summary="입주자 검색">
									<caption>입주자 검색</caption>
									<colgroup>
										<col width="20%" />
										<col width="*" />
									</colgroup>
									<tbody>
										<tr>
											<th>아파트명</th>
											<td>
												<select name="SCH_APT_CODE" id="SCH_APT_CODE" class="select" title="아파트명" onchange="fnGetDongHosu(this.value, 'D');">
													<option value="">선택</option>
													<c:forEach var="item" items="${INIT_DATA.aptList}" >
														<option value="${item.APT_CODE}" <c:if test="${item.APT_CODE eq INIT_DATA.SCH_APT_CODE}">selected="selected"</c:if> >${item.APT_NM}</option>
													</c:forEach>
												</select>
											</td>
										</tr>
										<tr>
											<th>동/호수</th>
											<td>
												<select name="SCH_DONG" id="SCH_DONG" class="select w200" title="동" onchange="fnGetDongHosu(this.value, 'H');">
													<option value="">전체</option>
												</select>
												&nbsp;&nbsp;
												<select name="SCH_HOSU" id="SCH_HOSU" class="select w200" title="호">
													<option value="">전체</option>
												</select>
											</td>
										</tr>
										<tr>
											<th>이름</th>
											<td>
												<input type="text" name="USER_NM" id="USER_NM" title="성명" class="input w99p" value="${INIT_DATA.USER_NM}" maxlength="50" placeholder="이름" />
											</td>
										</tr>
										<tr>
											<th>연락처</th>
											<td>
												<input type="text" name="HP1" id="HP1" title="연락처" class="input w100" value="${INIT_DATA.HP1}" maxlength="50" placeholder="" />
												-
												<input type="text" name="HP2" id="HP2" title="연락처" class="input w100" value="${INIT_DATA.HP2}" maxlength="50" placeholder="" />
												-
												<input type="text" name="HP3" id="HP3" title="연락처" class="input w100" value="${INIT_DATA.HP3}" maxlength="50" placeholder="" />
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</form>
						<div class="core_btn">
							<a href="javascript:fnSearch();" class="mt30" style="background-color:#ff9900;">검색</a>
						</div>
						<div id="DIVSHOW" style="display:none;">
							<h3 class="title1 mt30">상세내역</h3>
							<div class="bbsView" >
								<table summary="입주자 검색">
									<caption>입주자 검색</caption>
									<colgroup>
										<col width="20%" />
										<col width="*" />
									</colgroup>
									<tbody>
										<tr>
											<th>주소</th>
											<td>
												${INIT_DATA.AptMeminfo.ADDR}
											</td>
										</tr>
										<tr>
											<th>위임장/회비</th>
											<td>
												<c:if test="${INIT_DATA.AptMeminfo.WARRANT_YN eq 'Y'}"><span style="color:#2b70c9;">위임장 제출완료&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;</span></c:if>
												<c:if test="${INIT_DATA.AptMeminfo.WARRANT_YN eq 'N'}"><span style="color:#ff3333;">위임장 미제출&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;</span></c:if>
												
												<c:if test="${INIT_DATA.AptMeminfo.AMT_YN eq 'Y'}"><span style="color:#2b70c9;">회비납부 완료</span></c:if>
												<c:if test="${INIT_DATA.AptMeminfo.AMT_YN eq 'N'}"><span style="color:#2b70c9;">회비 미납부</span></c:if>
											</td>
										</tr>
										<tr>
											<th>기타내용</th>
											<td>
												${INIT_DATA.AptMeminfo.ETC}
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</section>
				</div>
				<!-- 본문내용 끝 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// -->
			</li>
		</ul>

		<!-- 하단영역 -->
		<%@ include file="/static_root/inc/apt_footer.jsp" %>
		<!-- 하단영역 끝 -->
	</div>
</body>
</html>