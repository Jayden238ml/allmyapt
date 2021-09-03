$(document).ready(function() {
	// 메뉴 //
	$('#lnb-open').click(function(){ 
		$('#lnb').animate({ marginLeft : '0' }, 350, 'easeOutQuad');
		$('#lnb-close-bg').css({ display : 'block' });
	});
	$('#lnb-close-bg').click(function(){ 
		$('#lnb').animate({ marginLeft : '-250px' }, 350, 'easeOutQuad');
		$('#lnb-close-bg').css({ display : 'none' });
	});
	$('#lnb-close').click(function(){ 
		$('#header > dl').animate({ marginLeft : '0' }, 350, 'easeOutQuad');
		$('#lnb').animate({ marginLeft : '-250px' }, 350, 'easeOutQuad');
		$('#lnb-open-btn').animate({ marginLeft : '0' }, 350, 'easeOutQuad');
		$('#lnb-close-bg').css({ display : 'none' });
		$('#content').animate({ marginLeft : '0' }, 350, 'easeOutQuad');
		$('#footer > dl').animate({ marginLeft : '0' }, 350, 'easeOutQuad');
	});
	$('#lnb-open-btn').click(function(){ 
		$('#header > dl').animate({ marginLeft : '250px' }, 350, 'easeOutQuad');
		$('#lnb').animate({ marginLeft : '0' }, 350, 'easeOutQuad');
		$('#lnb-open-btn').animate({ marginLeft : '-25px' }, 350, 'easeOutQuad');
		$('#lnb-close-bg').css({ display : 'none' });
		$('#content').animate({ marginLeft : '250px' }, 350, 'easeOutQuad');
		$('#footer > dl').animate({ marginLeft : '250px' }, 350, 'easeOutQuad');
	});


	// 메뉴 - 브라우져 창 움직일때 //
	$(window).bind('resize', function(e){
		window.resizeEvt;
		$(window).resize(function(){
			clearTimeout(window.resizeEvt);
			window.resizeEvt = setTimeout(function(){
				if ($(window).width() < 991) {
					$('#header > dl').animate({ marginLeft : '0' }, 350, 'easeOutQuad');
					$('#lnb').animate({ marginLeft : '-250px' }, 350, 'easeOutQuad');
					$('#lnb-open-btn').css({ display : 'none' });
					$('#lnb-close-bg').css({ display : 'none' });
					$('#content').animate({ marginLeft : '0' }, 350, 'easeOutQuad');
					$('#footer > dl').animate({ marginLeft : '0' }, 350, 'easeOutQuad');
				} else {
					$('#header > dl').animate({ marginLeft : '250px' }, 350, 'easeOutQuad');
					$('#lnb').animate({ marginLeft : '0' }, 350, 'easeOutQuad');
					$('#lnb-open-btn').css({ display : 'block' });
					$('#lnb-open-btn').animate({ marginLeft : '-25px' }, 350, 'easeOutQuad');
					$('#lnb-close-bg').css({ display : 'none' });
					$('#content').animate({ marginLeft : '250px' }, 350, 'easeOutQuad');
					$('#footer > dl').animate({ marginLeft : '250px' }, 350, 'easeOutQuad');
				}
			}, 50);
		});
	});


	// 기간검색 - 달력 //
	$( "#from" ).datepicker({
		showOn: "both",
		buttonImage: "/statice_root/images/btnIcn/btn_calendar.png",
		buttonImageOnly: true,
		buttonText: "Select date",
		dateFormat:'yy-mm-dd',
		onClose: function( selectedDate ) {
			$( "#to" ).datepicker( "option", "minDate", selectedDate );
		}
	});
	$( "#to" ).datepicker({
		showOn: "both",
		buttonImage: "/statice_root/images/btnIcn/btn_calendar.png",
		buttonImageOnly: true,
		buttonText: "Select date",
		dateFormat:'yy-mm-dd',
		onClose: function( selectedDate ) {
			$( "#from" ).datepicker( "option", "maxDate", selectedDate );
		}
	});

	
	// 오늘 날짜 //
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1; //January is 0!
	var yyyy = today.getFullYear();

	if(dd<10) {
		dd='0'+dd
	} 
	if(mm<10) {
		mm='0'+mm
	} 
	today = yyyy+'-'+mm+'-'+dd
	$('.title-today > dd > span em').text(today);

	
	// 부트스트랩 - 툴팁 //
	$('[data-toggle="tooltip"]').tooltip()
	
});


//부트스트랩 멀티 모달 음영
var count = 0; // 모달이 열릴 때 마다 count 해서  z-index값을 높여줌
$(document).on('show.bs.modal', '.modal', function () {
    var zIndex = 1040 + (10 * count);
    $(this).css('z-index', zIndex);
    setTimeout(function() {
        $('.modal-backdrop').not('.modal-stack').css('z-index', zIndex - 1).addClass('modal-stack');
    }, 0);
    count = count + 1
});


// 부트스트랩 멀티모달 스크롤
$(document).on('hidden.bs.modal', '.modal', function () {
    $('.modal:visible').length && $(document.body).addClass('modal-open');
});


// 부트스트랩 모달 가운데정렬
/*
function alignModal(){
	var modalDialog = $(this).find(".modal-dialog");
	modalDialog.css("margin-top", Math.max(0, ($(window).height() - modalDialog.height()) / 2));
}

$(".modal").on("shown.bs.modal", alignModal);

$(window).on("resize", function(){
	$(".modal:visible").each(alignModal);
});   
*/

// 링크점선 제거 //
function bluring(){
	if(event.srcElement.tagName=="A" || event.srcElement.tagName=="IMG")
	document.body.focus();
}
document.onfocusin=bluring;