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
	
	<title>아파트관리 | 견적문의</title>
<%@ include file="/static_root/inc/usr_top.jsp" %>
<script type="text/javascript">
	$(document).ready(function() {
		$("body").prepend('<div id="overlay" class="ui-widget-overlay" style="z-index: 1001; display: none;"></div>');
		$("body").prepend('<div id="dialog" style="z-index: 1001; position:fixed; top:50%; left:50%; padding:.2em; width:50px; height:50px; display:none;"><img src="'+COMMON_IMAGES_CONF+'/common/j_loading.gif" /></div>');
		$("#dialog, #overlay").hide(); //첫 시작시 로딩바를 숨겨준다.
	
	}).ajaxStart(function(){
		$("#dialog, #overlay").show(); //ajax실행시 로딩바를 보여준다.
	})
	.ajaxStop(function(){
		$("#dialog, #overlay").hide(); //ajax종료시 로딩바를 숨겨준다.
	});
	
	function fnSendMail(){
		if($('#USER_NM').val() == "" && $('#AGENCY_NM').val() == ""){
			alert("성명 또는 기관명을 입력해 주세요.");
			return;
		}
		if($('#RETURN_HP').val() == "" && $('#RETURN_EMAIL').val() == ""){
			alert("답변받을 연락처 또는 이메일을 입력해 주세요.");
			return;
		}
		if($('#CONTENTS').val() == ""){
			alert("문의 내용을 입력해 주세요.");
			return;
		}
		
		if(confirm("견적문의 하시겠습니까?")){
			$.ajax({
				type: "POST",
				url  : "/user/sendMail.do",
				data:  $("#frm").serialize(),
				dataType: "json",
				success: function (transport){
					if(transport.ERROR_CD == "999"){
						alert("처리 중 오류가 발생 하였습니다.");
						return;
					}else{
						alert("메일이 발송 되었습니다.\n빠른 시일내에 답변 드리도록 하겠습니다.");
						fnMain();
					}
				}
			});
		}
	}
	
	function fnMain(){
		$('#frm').attr("action", "/main.do");
		$('#frm').submit();
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
				<%@ include file="/static_root/inc/usr_left.jsp" %>
				<!-- 좌측영역 끝 -->
			</li>
			<li id="right">
				<!-- 타이틀/네비 -->
				<div class="titleNaviNew">
					<h3 class="subTitle">견적문의</h3>
				</div>
				<!-- 타이틀/네비 끝 -->

				<!-- 본문내용 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// -->
				<div id="cont" class="apt_core">
					<form name="frm" id="frm" method="post" action="#">
						<input type="hidden" name="TMC" id="TMC" value="${INIT_DATA.TMC}" />
						<input type="hidden" name="LMC" id="LMC" value="${INIT_DATA.LMC}" />
						<section class="sect_area sect_first_area">
							<h4 class="title1">문의내용을 적어서 보내주시면 빠른시일내에 답변 드리겠습니다.</h4>
							<br/>
								<div class="bbsView" >
									<table summary="견적요청">
										<caption>견적문의</caption>
										<colgroup>
											<col width="20%" />
											<col width="*" />
										</colgroup>
										<tbody>
											<tr>
												<th>성명</th>
												<td>
													<input type="text" name="USER_NM" id="USER_NM" title="성명" class="input w99p" value="" maxlength="50" placeholder="문의자 성명" />
												</td>
											</tr>
											<tr>
												<th>기관명</th>
												<td>
													<input type="text" name="AGENCY_NM" id="AGENCY_NM" title="기관명" class="input w99p" value="" maxlength="50"  placeholder="입예협 명 또는 기관명"/>
												</td>
											</tr>
											<tr>
												<th>답변받을 연락처</th>
												<td>
													<input type="text" name="RETURN_HP" id="RETURN_HP" title="답변받을 연락처" class="input w99p" value="" maxlength="50"  placeholder="답변 받을 연락처"/>
												</td>
											</tr>
											<tr>
												<th>이메일</th>
												<td>
													<input type="text" name="RETURN_EMAIL" id="RETURN_EMAIL" title="이메일" class="input w99p" value="" maxlength="50"  placeholder="답변 받을 이메일 주소"/>
												</td>
											</tr>
											<tr>
												<th>내용</th>
												<td>
													<textarea name="CONTENTS" id="CONTENTS" rows="" cols="" title="문의내용" style="width:99%; height:200px; letter-spacing:-0.5px; word-spacing:2px; line-height:24px;" class="input" maxlength="2000" placeholder="문의 내용"></textarea>
												</td>
											</tr>
										</tbody>
									</table>
								</div>
							<div class="core_btn">
								<a href="javascript:fnSendMail();" class="mt30" style="background-color:#ff9900;">견적문의</a>
							</div>
						</section>
					</form>
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