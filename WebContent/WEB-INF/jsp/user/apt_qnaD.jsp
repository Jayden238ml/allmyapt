<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	<meta name="title" content="아파트관리" />
	<meta name="description" content="아파트,입예협,입주자관리,입주예정자협의회,시세조회,분양가조회, 실거래가 비교, 분양가 비교" />
	<meta name="keywords" content="아파트,입예협,입주자관리,입주예정자협의회,시세조회,분양가조회, 실거래가 비교, 분양가 비교, 올마이아파트" />
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
<%@ include file="/static_root/inc/usr_top.jsp" %>
<%
   //치환 변수 선언
    pageContext.setAttribute("cr", "\r"); //Space
    pageContext.setAttribute("cn", "\n"); //Enter
    pageContext.setAttribute("crcn", "\r\n"); //Space, Enter
    pageContext.setAttribute("br", "<br/>"); //br 태그
%>
<script type="text/javascript">
	$(document).ready(function(){

	});
	
	function fnList(){
		$('#frm').attr("action", "/user/apt_qnal.do");
		$('#frm').submit();
	}
	
	function fnModify(){
		$('#frm').attr("action", "/user/apt_qnaw.do");
		$('#frm').submit();
	}
	
	function fnDel(){
		if(confirm("삭제하시겠습니까?")){	
			$('#frm').attr("action", "/user/qnaDelete.do");
			$('#frm').submit();
		}
	}
	
	function fnAnswer(){
		$('#ANSWER_YN').val("Y");
		$('#frm').attr("action", "/user/apt_qnaAnswer.do");
		$('#frm').submit();
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
					<h3 class="subTitle">Q&A</h3>
				</div>
				<!-- 타이틀/네비 끝 -->

				<!-- 본문영역 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// -->
				<div id="cont">
					<form name="frm" id="frm" method="post" action="/user/apt_qnaD.do">
						<input type="hidden" name="TMC" id="TMC" value="${INIT_DATA.TMC}" />
						<input type="hidden" name="LMC" id="LMC" value="${INIT_DATA.LMC}" />
						<input type="hidden" name="BOARD_SEQ" id="BOARD_SEQ" value="${INIT_DATA.BOARD_SEQ}" />
						<input type="hidden" name="ANSWER_BOARD_SEQ" id="ANSWER_BOARD_SEQ" value="${INIT_DATA.detail.ANSWER_BOARD_SEQ}" />
						<input type="hidden" name="ANSWER_YN" id="ANSWER_YN" value="" />
						<section class="sect_area sect_first_area">
							
							<div class="viewArea">
								<div class="bvInfo">
									<dl class="infoTitle">
										<dt>
											${INIT_DATA.detail.TITLE}
										</dt>
									</dl>
									<dl class="infoArea">
										<dt><span>작성자 &nbsp;|&nbsp; ${INIT_DATA.detail.REG_NICK}<span>&nbsp;|&nbsp;등록일 &nbsp;|&nbsp; </span>${INIT_DATA.detail.REGDATE}</dt>
										<dd><span>조회수 &nbsp;|&nbsp; </span>${INIT_DATA.detail.VIEW_CNT}</dd>
									</dl>
								</div>
								<div class="videoWrap" style="color:#000000">
									${fn:replace(INIT_DATA.detail.CONTENTS, crcn, br)}
								</div>
								<c:if test="${INIT_DATA.detail.ANSWER_BOARD_SEQ ne ''}">
									<div class="bbsView">
										<table summary="Q&A">
											<caption>Q&A</caption>
											<colgroup>
												<col width="*" />
											</colgroup>
											<tbody>
												<tr>
													<th>답변내용</th>
												</tr>
											</tbody>
										</table>
									</div>
									<div class="videoWrap" style="color:#000000">
										${fn:replace(INIT_DATA.detail.ANSWER_CONTENTS, crcn, br)}
									</div>
								</c:if>
							</div>
							<dl class="bvBtn">
								<dt>&nbsp;</dt>
								<dd>
									<c:if test="${INIT_DATA.SESSION_USER_ID eq INIT_DATA.detail.REG_ID}">
										<a href="javascript:fnModify();" class="mBtn4">수정</a>
										<a href="javascript:fnDel();" class="mBtn6">삭제</a>
									</c:if>
									<c:if test="${INIT_DATA.SESSION_ADMIN_YN eq 'Y'}">
										<a href="javascript:fnAnswer();" class="mBtn5">답변</a>
									</c:if>
									<a href="javascript:fnList();" class="mBtn3">목록</a>
								</dd>
							</dl>
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