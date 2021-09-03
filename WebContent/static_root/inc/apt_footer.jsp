<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script type="text/javascript" src="/static_root/js/jquery-syaku.rolling.js" ></script>
<!-- 하단영역 -->
<script type="text/javascript">
	$(document).ready(function() {
		//롤링
			$('#srolling').srolling({
		      data : $('#srolling > li'),
		      auto : true,
		      delay_frame : 500,
		      move : 'left',
		      item_count : 1, 
		      cache_count : 3, 
	    });
	});
	
	function fnAgreePop(num){
		var type = "${INIT_DATA.ISMOBILE}";
		if(num == "1"){
			gfnOpenLayerPopup('/common/index.do?jpath=/etc/pop_privacy&TYPE='+type)
		}else{
			gfnOpenLayerPopup('/common/index.do?jpath=/etc/pop_service&TYPE='+type)
		}
	}
</script>
<style>
	/* PC 제외 */
	@media all and (max-width: 1199px){
	}
	/* 테블릿1 */
	@media all and (max-width: 989px){
	
	}
	/* 테블릿2 */
	@media all and (max-width: 767px){
	
	}
	/* 모바일 */
	@media all and (max-width: 479px){
	
	}
</style>
<div class="site_slide">
	<ul id="srolling" style="height:62px;">
		<li><a href="https://www.raemian.co.kr/main.do" target="_blank" title="새창으로 열기"><img src="/static_root/images/common/f_slide1.png" alt="삼성물산" /></a></li>
		<li><a href="https://www.hdec.kr/" target="_blank" title="새창으로 열기"><img src="/static_root/images/common/f_slide2.png" alt="현대건설" /></a></li>
		<li><a href="https://www.dlenc.co.kr/" target="_blank" title="새창으로 열기"><img src="/static_root/images/common/f_slide3.png" alt="대림산업" /></a></li>
		<li><a href="http://www.gs.co.kr/ko/branch/gs-ec?page=2" target="_blank" title="새창으로 열기"><img src="/static_root/images/common/f_slide4.png" alt="GS건설" /></a></li>
		<li><a href="https://www.poscoenc.com:446/index.asp" target="_blank" title="새창으로 열기"><img src="/static_root/images/common/f_slide5.png" alt="포스코건설" /></a></li>
		<li><a href="http://www.daewooenc.com/" target="_blank" title="새창으로 열기"><img src="/static_root/images/common/f_slide6.png" alt="대우건설" /></a></li>
		<li><a href="http://www.lottecon.co.kr/" target="_blank" title="새창으로 열기"><img src="/static_root/images/common/f_slide7.png" alt="롯데건설" /></a></li>
		<li><a href="https://www.hwenc.co.kr/" target="_blank" title="새창으로 열기"><img src="/static_root/images/common/f_slide8.png" alt="한화건설" /></a></li>
		<li><a href="https://www.ihoban.co.kr/" target="_blank" title="새창으로 열기"><img src="/static_root/images/common/f_slide9.png" alt="호반건설" /></a></li>
		<li><a href="http://www.molit.go.kr/" target="_blank" title="새창으로 열기"><img src="/static_root/images/common/f_slide10.png" alt="국토교통부" /></a></li>
	</ul>
</div>
<footer id="footer">
	<div class="footer_wrap">
		<div class="ft_top">
			<h3><img src="/static_root/images/common/apt_footer_logo.jpg" alt="올마이티컴퍼니" /></h3>
			<div class="ft_menu">
				<a href="javascript:fnAgreePop('1');" class="on">이용약관</a>
				<a href="javascript:fnAgreePop('2');" class="on">개인정보취급방침</a>
<!-- 				<a style="text-decoration:none;color:#78849e;">통신판매신고 제 2021-경기안산-2037</a> -->
<!-- 				<a style="text-decoration:none;color:#78849e;">사업자번호 : 713-88-02096</a> -->
				
			</div>
		</div>
		<ul class="ft_btm">
			<li>COPYRIGHT &copy;2021 <strong>주식회사 올마이티컴퍼니</strong>. ALL RIGHT RESERVED.
<!-- 			| 경기도 안산시 단원구 풍전로7 비동4층 410-에이 76호 대표이사 정 재 식 -->
			</li>
		</ul>
	</div>
</footer>
<form name="fileDownFrmFooter" id="fileDownFrmFooter" method="post">
	<input type="hidden" name="F_FILENM" />
	<input type="hidden" name="F_FILEPATH" />
	<input type="hidden" name="F_TRANSFILENM" />
</form>
<iframe id="fileDownFrameFooter" name="fileDownFrameFooter" src="about:blank" width="0" height="0" frameborder="0" scrolling="no" title="파일다운로드 프레임"></iframe>

