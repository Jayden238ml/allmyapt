var modalResizeWidth = 0;
/*********************************************
************** 체크 박스일괄 삭제
**********************************************/
	function allCheck(ob) {

		var checkboxs = document.getElementsByName("check");
		//조회된 것이 없을때 예외처리   
		if (!checkboxs) { 
			return false;
		}

		if (ob.checked) {
			if (checkboxs.length) {
				for (i=0; i< checkboxs.length; i++) {
					checkboxs[i].checked=true;
				}
			} else {
				checkboxs.checked=true;
			}
		} else {
			if (checkboxs.length) {
				for (i=0; i< checkboxs.length; i++) {
					checkboxs[i].checked=false;
				}
			} else {
				checkboxs.checked=false;
			}
		}

	}


	//체크박스에 체크되어있는지 검사
	function checkCheckbox(obj) {
		var checked = false;
		var temp ="";

		if (obj.length) {
			for (i=0; i<obj.length; i++) {
				if (obj[i].checked) {
					checked = true;
				}
			}
		} else {
			if (obj.checked) {
				checked = true;
			}
		}
		return checked;
	}

	/*********************************************
	**************  숫자만 입력하기
	**********************************************/
	function onlyNumber(obj) { // 숫자만 입력받는 함수 onkeyup="onlyNumber(this);"
		if ((event.keyCode >= 48 && event.keyCode <= 57)
				|| (event.keyCode >= 96 && event.keyCode <= 105)
				|| (event.keyCode == 8) // 백 스페이스
				|| (event.keyCode == 9) // 탭키
				|| (event.keyCode == 46 || event.keyCode == 13
						|| event.keyCode == 35 || event.keyCode == 36
						|| event.keyCode == 46 || event.keyCode == 37
						|| event.keyCode == 39 || event.keyCode == 110 || event.keyCode == 190
						//|| event.keyCode == 189
				)) {
		} else {
			if (isNaN($(obj).val())) {
				alert("숫자만 입력 가능 합니다");
			}
			$(obj).val($(obj).val().replace(/[^0-9]/gi,""));
		}
	}

	function onlyNumber_h(obj) { // 숫자만 입력받는 함수 onkeyup="onlyNumber_h(this);"
		if ((event.keyCode >= 48 && event.keyCode <= 57)
				|| (event.keyCode >= 96 && event.keyCode <= 105)
				|| (event.keyCode == 8) // 백 스페이스
				|| (event.keyCode == 9) // 탭키
				|| (event.keyCode == 46 || event.keyCode == 13
						|| event.keyCode == 35 || event.keyCode == 36
						|| event.keyCode == 46 || event.keyCode == 37
						|| event.keyCode == 39 || event.keyCode == 189)) {
		} else {
			if (isNaN($(obj).val())) {
				alert("숫자만 입력 가능 합니다");
			}

			$(obj).val($(obj).val().replace(/[^0-9.]/gi,""));
		}
	}
	
	function onlyNumberC(obj) { // 숫자만 입력받는 함수 onkeyup="onlyNumber(this);"
		if ((event.keyCode >= 48 && event.keyCode <= 57)
				|| (event.keyCode >= 96 && event.keyCode <= 105)
				|| (event.keyCode == 8) // 백 스페이스
				|| (event.keyCode == 9) // 탭키
				|| (event.keyCode == 110) // 소수점
				|| (event.keyCode == 190) // 소수점
				|| (event.keyCode == 46 || event.keyCode == 13
						|| event.keyCode == 35 || event.keyCode == 36
						|| event.keyCode == 46 || event.keyCode == 37
						|| event.keyCode == 39
						//|| event.keyCode == 189
				)) {
		} else {
			if (isNaN($(obj).val())) {
				alert("숫자만 입력 가능 합니다");
			}

			$(obj).val($(obj).val().replace(/[^0-9]/gi,""));
		}
	}

	function onlyTime(obj) { // 숫자만 입력받는 함수 onkeyup="onlyNumber(this);"
		if ((event.keyCode >= 48 && event.keyCode <= 57)
				|| (event.keyCode >= 96 && event.keyCode <= 105)
				|| (event.keyCode == 8) // 백 스페이스
				|| (event.keyCode == 9) // 탭키
				|| (event.keyCode == 46 || event.keyCode == 13
						|| event.keyCode == 35 || event.keyCode == 36
						|| event.keyCode == 46 || event.keyCode == 37
						|| event.keyCode == 39 || event.keyCode == 189 || event.keyCode == 186 || event.keyCode == 16)) {
		} else {
			if (isNaN($(obj).val())) {
				alert("숫자만 입력 가능 합니다");
			}

			$(obj).val($(obj).val().replace(/[^0-9]/gi,""));
		}
	}

	/** ******************달력 관련********************* */
	function setDatePicker(id) {

		$("#"+id).attr("readonly","true");

		$("#"+id).datepicker({
			dateFormat: 'yy.mm.dd', //데이터 포멧형식
			changeMonth: true,   //달별로 선택 할 수 있다.
			changeYear: true ,   //년별로 선택 할 수 있다.
			showOn: "both", // 버튼과 텍스트 필드 모두 캘린더를 보여준다.
			buttonImage: "../../static_root/images/btnIcn/btn_calendar.gif", // 버튼 이미지
			buttonImageOnly: true, // 버튼에 있는 이미지만 표시한다
			monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'], // 월의 한글 형식.
			dayNamesMin : ['일','월','화','수','목','금','토'],
			showMonthAfterYear: true,
		      onSelect: function( selectedDate ) {
				    var selectIdNm = $(this).attr("id");
					var selectId = $(this).attr("id").replace(/[^0-9]/g, "");;

						if(selectIdNm.indexOf("ST_DATE")> -1){
							$('#ED_DATE' + selectId).datepicker();
						    $('#ED_DATE' + selectId).datepicker("option", "minDate", $("#ST_DATE" + selectId).val());
						    $('#ED_DATE' + selectId).datepicker("option", "onClose", function ( selectedDate ) {
						        $("#ST_DATE" + selectId).datepicker( "option", "maxDate", selectedDate );
						    });
						}else{
							$('#ST_DATE' + selectId).datepicker();
						    $('#ST_DATE' + selectId).datepicker("option", "maxDate", $("#ED_DATE" + selectId).val());
						    $('#ST_DATE' + selectId).datepicker("option", "onClose", function ( selectedDate ) {
						        $("#ED_DATE" + selectId).datepicker( "option", "minDate", selectedDate );
						    });
						}

				},
			beforeShow : function () {
				//$(this).val("");
			}
		});
	}

	//달력 금일날짜 이후 선택 금지
	function noBefore(date){
		if (date > new Date())
		        return [false];
		    return [true];
	}


	/************************    코드 리스트 가져오기 ***********************************************************/

	function fncAutoComboSet(tag_nm, grpCode, uperCode, pFlag, pValue, callBack) {

		//초기화
		$("#"+tag_nm).find('option').remove().end();

		$.ajax({
			type: "POST",
			url: "/admin/survey.do?method=codeList",
			data: {
				"GRP_CODE": grpCode
				,"UPER_CODE": uperCode
			},
			dataType: "json",
			success: function (transport) {

				$("#" + tag_nm).find('option').remove().end();
				var pPartCd = eval(transport.codeList);

				if (pFlag != "") {
					var addOption = document.createElement('option');

					addOption.setAttribute("value", "");

					if (pFlag == 'A') {
						addOption.appendChild(document.createTextNode("전체"));
					}
					else if (pFlag == 'S') {
						addOption.appendChild(document.createTextNode("선택"));
					}

					$("#" + tag_nm).append(addOption);
				}

				$.each(pPartCd, function (key, json) {

					var option = document.createElement("option");
					option.setAttribute("value", json.CODE);
					option.appendChild(document.createTextNode(json.CODENM));
					$("#" + tag_nm).append(option);
				});

				if ($.trim(pValue) != "") {
					$("#" + tag_nm).val(pValue);
				}

				if (callBack != "") {
					eval(callBack);
				}
			}
		});
	}

	function fncAutoComboCateSet(tag_nm, bdNo, pFlag, pValue, callBack) {

		//초기화
		$("#"+tag_nm).find('option').remove().end();

		$.ajax({
			type: "POST",
			url: "/user/board.do?method=boardCateList",
			data: {
				"BD_NO": bdNo
				,"CATE_NOT_NULL" : "Y"
			},
			dataType: "json",
			success: function (transport) {

				$("#" + tag_nm).find('option').remove().end();
				var pPartCd = eval(transport.boardCateList);

				if (pFlag != "") {
					var addOption = document.createElement('option');

					addOption.setAttribute("value", "");

					if (pFlag == 'A') {
						addOption.appendChild(document.createTextNode("전체"));
					}
					else if (pFlag == 'S') {
						addOption.appendChild(document.createTextNode("선택"));
					}

					$("#" + tag_nm).append(addOption);
				}

				$.each(pPartCd, function (key, json) {

					var option = document.createElement("option");
					option.setAttribute("value", json.CATE_SEQ);
					option.appendChild(document.createTextNode(json.CATE_NM));
					$("#" + tag_nm).append(option);
				});

				$("#" + tag_nm).val(pValue);

				if (callBack != "") {
					eval(callBack);
				}
			}
		});
	}

	function fncAutoComboUrlSet(tag_nm, url, pFlag, pValue, callBack) {

		//초기화
		$("#"+tag_nm).find('option').remove().end();

		$.ajax({
			type: "POST",
			url: url,
			data: {

			},
			dataType: "json",
			success: function (transport) {

				$("#" + tag_nm).find('option').remove().end();
				var pPartCd = eval(transport.resultList);

				if (pFlag != "") {
					var addOption = document.createElement('option');

					addOption.setAttribute("value", "");

					if (pFlag == 'A') {
						addOption.appendChild(document.createTextNode("전체"));
					}
					else if (pFlag == 'S') {
						addOption.appendChild(document.createTextNode("선택"));
					}

					$("#" + tag_nm).append(addOption);
				}

				$.each(pPartCd, function (key, json) {

					var option = document.createElement("option");
					option.setAttribute("value", json.CODE);
					option.appendChild(document.createTextNode(json.CODE_NM));
					$("#" + tag_nm).append(option);
				});

				$("#" + tag_nm).val(pValue);

				if (callBack != "") {
					eval(callBack);
				}
			}
		});
	}


	 /*공통 라디오  Set*/

	function fncAutoRadioSet(tag_nm, id, grpCode, uperCode, pFlag, pValue, callBack, br) {

		$.ajax({
			type: "POST",
			url: "/adn/code.do?method=codeList",
			data: {
				"GRP_CODE": grpCode
				,"UPER_CODE": uperCode
			},
			dataType: "json",
			success: function (transport) {

				var pPartCd = eval(transport.codeList);

				var resultHtml = "";

				var i = 1;

				$.each(pPartCd, function (key, json) {

					var rdo = "";

					//rdo += '<input type="hidden" name="' + json.CODE + '" id="' + json.CODE + '" value="' + json.CODE + '" /> ';
					rdo += '<input type="radio" name="' + id + '"    id="' + id + '"';
					rdo += ' value="' + json.CODE + '"   ';
					rdo += ' title="' + json.CODENM + '" style="border:0px"';

					if (pValue == json.CODE) {
						rdo += ' checked';
					}

					rdo += '/> ';
					rdo += json.CODENM + "&nbsp;&nbsp;";

					if (br != undefined && key == br) rdo += "</br>";

					resultHtml += rdo;
					i++;

				});

				$("#" + tag_nm).html(resultHtml);
				if (callBack != "") {
					eval(callBack);
				}
			}
		});
	}

	// 라디오버튼 셋팅해주기
	function setRadio(form, name, value) {
		for (var i = 0; i < form.elements.length; i++) {
			var element = form.elements[i];
			if (element.name == name && element.value == value) {
				element.checked = "true";
				break;
			}
		}
	}

	// 라디오버튼 셋팅해주기
	function getRadio(form, name, value) {
		for (var i = 0; i < form.elements.length; i++) {
			var element = form.elements[i];
			if (element.name == name && element.value == value) {
				element.checked = "true";
				break;
			}
		}
	}


	/************************   로그인 체크  ***********************************************************/

	function isLogin() {

		var isLoginFlag = false;
		var req_val = '${INIT_DATA.USER_NM}';

		if ('' == req_val ) {
			isLoginFlag= false;
		} else {
			isLoginFlag=true;
		}

		return isLoginFlag;

	}

	//태그 제거 : 정규식 이용
	function removeAtag(text) {
		text = text.replace(/<a(.*?)>/gi,"");  //<a href에 포함됨 모든 내용 제거
		text = text.replace(/<(\/?)a>/gi,"");  //</a>태그 제거
		return text;
	}
	
	// 로그인 체크
	function loginChk(id) {
		var flag = false;
		
		if(id != '') flag = true;
		else flag = false;
		return flag;
	}

	/************************** 프린트  *********************************************/
	var PrintPage = null; //인쇄 팝업창

	/*function pagePrint(Obj) {
		var W = 780;// Obj.offsetWidth; //screen.availWidth;
		var H = 600;

		var temp =  removeAtag (Obj.innerHTML);
		// var temp = Obj.innerHTML;
		try {

			var features = "menubar=no,toolbar=no,location=no,directories=no,status=no,scrollbars=yes,resizable=yes,width="
				+ W + ",height=" + H + ",left=0,top=0";

			PrintPage = window.open("about:blank", Obj.id, features);


			PrintPage.document.open();
			PrintPage.document
				.write("<html><head><title>계명대학교</title>"+
						"<link rel='stylesheet' type='text/css' href='/static_root/common/css/common/common.css'/>"+
						"<link rel='stylesheet' type='text/css' href='/static_root/common/css/career/career.css'/>"+
						"<link rel='stylesheet' type='text/css' href='/static_root/common/css/calendar/calendar.css'/>"+
						"</head>"+
				"<script>function chkClose() {"+
				"opener.PrintPage = null;"+
				"self.close();"+
				 " }"+
				"</script>"+
				"<body onunload='chkClose();'>"+
				"<div class='popup_wrap'><div class='popup_contents'><a href='javascript:print();'><img src='/static_root/common/images/common/btn/btn_print.gif' alt='인쇄' /></a>"+
				"<a href='javascript:chkClose();'><img src='/static_root/common/images/common/btn/btn_close.gif' alt='닫기' /></a><br/>"+
				""+ temp + "<br/><a href='javascript:chkClose();'><img src='/static_root/common/images/common/btn/btn_close.gif' alt='닫기' /></a></div></div></body></html>");
			PrintPage.document.close();
			PrintPage.document.title = document.domain;
			PrintPage.focus();

		} catch(e) {
			pagePrint(Obj);
		}
	}

*/

	/*********************************************
	************** 글자수 자르기
	**********************************************/
	function chr_byte(chr) {

		if (escape(chr).length > 4)	return 2;

		else						return 1;

	}

	//문자열 잘라서 화면에 표시하기
	function cutStr(str,limit) {
		/*str = str.replaceAll(/\n/g, "");*/
		str = str.replaceAll(/\r/g, "");
		str = str.replaceAll(/\n/g, "");
		str = str.replaceAll(/\r\n/g, "");
		str = str.replaceAll(/br/g, "");
		str = str.replaceAll(/LF/g, "");
		var tmpStr = str;
		var byte_count = 0;
		var len = str.length;
		var dot = "";
		for(i=0; i<len; i++) {
			byte_count += chr_byte(str.charAt(i));
			if (byte_count == limit-1) {
				if (chr_byte(str.charAt(i+1)) == 2) {
					tmpStr = str.substring(0,i+1);
					dot = "...";
				} else {
					if (i+2 != len) dot = "...";
					tmpStr = str.substring(0,i+2);
				}
				break;
			} else if (byte_count == limit) {
				if (i+1 != len) dot = "...";
				tmpStr = str.substring(0,i+1);
				break;
			}
		}
		var result = tmpStr+dot;
		result = result.replaceAll("∞","\"");
		document.write(result);

		return result;
	}

	String.prototype.trim = function() {
	 return this.replace(/(^\s*)|(\s*$)/gi, "");
	};

	//문자열 빼기
	String.prototype.replaceAll = function(str1, str2)
	{
	  var temp_str = this.trim();
	  temp_str = temp_str.replace(eval("/" + str1 + "/gi"), str2);
	  return temp_str;
	};

	function f_chk_byte(aro_name,ari_max) {

		var ls_str = aro_name.value;
		var li_str_len = ls_str.length;
		var li_max = ari_max;
		var i = 0;
		var li_byte = 0;
		var li_len = 0;
		var ls_one_char = "";
		var ls_str2 = "";

		for (i=0; i< li_str_len; i++) {

			ls_one_char = ls_str.charAt(i);

			if (escape(ls_one_char).length > 4)
				li_byte += 2;
			else
				li_byte++;

			if (li_byte <= li_max) li_len = i + 1;

		}

		if (li_byte > li_max) {
			alert("한글 " +  ari_max + "글자를 초과 입력할수 없습니다. 초과된 내용은 자동으로 삭제 됩니다.");
			ls_str2 = ls_str.substr(0, li_len);
			aro_name.value = ls_str2;
		}

		aro_name.focus();

	}

	function f_chk_byte_min(aro_name,ari_min) {

		var ls_str = aro_name.value;
		var li_str_len = ls_str.length;
		var i = 0;
		var li_byte = 0;
		var ls_one_char = "";

		for (i=0; i< li_str_len; i++) {
			ls_one_char = ls_str.charAt(i);
			if (escape(ls_one_char).length > 4)
				li_byte += 2;
			else
				li_byte++;
		}

		if (li_byte < ari_min) {
			alert("한글 " +  ari_min + "글자 이상 입력하셔야됩니다.");
		}

		aro_name.focus();
	}

	/*******************************  특정 영역 인쇄하기  ***************************************************************/

	function beforePrint() {
		initBody = document.body.innerHTML;
		document.body.innerHTML = print_page.innerHTML;
	}

	function afterPrint() {
		document.body.innerHTML = initBody;
	}

	function removeAtag(text) {
		text = text.replace(/<input(.*?)>/gi,"");  //<intput 에 포함됨 모든 내용 제거
		text = text.replace(/<(\/?)input>/gi,"");  //</a>태그 제거
		return text;
	}

	function pageprint() {
		document.getElementById("frm").style.display="none";
		window.onbeforeprint = beforePrint;
		window.onafterprint = afterPrint;
		window.print();
	}

	/****************************** 제목 자르기 .. ************************************************************************************/
	function getStrCuts(str, max) {
		ns = str.substr(0, max);
		if (ns.length != str.length) {
			ns = ns + "...";
		}

		return ns;
	}

	/******************************  발리데이션 체크하기  ************************************************************************************/

	function fncValidate() {
		var result = true;
		$(".required").each(function (i) {
			if (this.value == "") {
				this.focus();
				this.style.color = 'red';
				result =  false;
				return false;

			} else {
				this.style.color = 'gray';
			}
		});

		return result;
	}


	/***************************그룹 리스트 가져오기  ************************************************************************************/
	function fncAutoUserGroupComboSet(tag_nm,callBack) {

		//초기화
		$("#"+tag_nm).find('option').remove().end();

		$.ajax({
			type: "POST",
			url  : "/adn/userGroupManager.do?method=groupList",
			data: {

			},
			dataType: "json",
			success: function (transport){

				$("#" + tag_nm).find('option').remove().end();

				var pPartCd = eval(transport.groupList);
				//var addOption = document.createElement('option');
				//addOption.setAttribute("value", "");
				//$("#" + tag_nm).append(addOption);

				$.each(pPartCd, function (key, json){
					var option = document.createElement("option");
					option.setAttribute("value", json.AUTHCODE);
					option.appendChild(document.createTextNode(json.AUTHNM));
					$("#" + tag_nm).append(option);

				});

				if (callBack != ""){
					eval(callBack);
				}
			}
		});
	}

	//유효성 체크
	function chkVal() {
		var chkRst = true;
		var formCheck = new FormCheck();

		$('input').each(function () {

			if (this.id != "" && formCheck.checkInput(this.id)) {
				chkRst = false;
				return false;
			}
		});

		return chkRst;
	}

	function FormCheck() {
		/*
		 -필독
		 해당 클래스는 jQuery를 사용함으로 이 클래스보다 위에 jQuery를 먼저 include하여야함.


		 -지원하는 속성
		 required : 필수입력 필드를 나타냄
		 minlength="0~9" : 문자의 최소입력 수치를 나타냄
		 email : 입력내용이 e메일 형태인지 체크
		 numeric : 입력내용이 숫자인지 확인
		 alpha : 입력내용이 영문인지 확인
		 alphanumeric : 입력내용이 영문또는 숫자인지 확인
		 idcheck : 특수문자가 들어갔는지 체크(ID입력용)


		 -사용법
		 먼저 이 클래스를 이용하여 객체선언 하고
		 체크할 INPUT의 ID값을 checkInput() 메소드를 호출하여 검사함
		 checkInput() 메소드는 해당 INPUT의 에 설정한 속성을 체크하여 해당 속성의 유효성과 맞지 않을시에 리턴값으로 true를 반환 한다.



		 -사용예제
		 var objTest = new FormCheck();
		 if (objTest.checkInput("INPUT_ID")) {
			return;
		 }
		*/

		var input_el = null; //INPUT 엘리먼트를 저장할 변수
		var itemname = null; //INPUT 엘리먼트의 이름을 저장할 변수

		//INPUT의 ID값을 받아 유효성검사를 한다.
		this.checkInput = function (input_id) {
			//INPUT 엘리먼트를 저장
			this.input_el = $("#" + input_id);
			//INPUT 엘리먼트의 이름을 저장
			if ($(this.input_el).attr("itemname") != "" && $(this.input_el).attr("itemname") != null) {
				this.itemname = $(this.input_el).attr("itemname");
			} else {
				this.itemname = $(this.input_el).attr("id");
			}

			//유효성 검사를 위해 함수 호출
			if (this.hasAttr("required")) {
				if (!this.isRequired()) {
					return true;
				}
			}

			if (this.hasAttr("minlength")) {
				if (!this.checkMinlength()) {
					return true;
				}
			}

			if (this.hasAttr("email")) {
				if (!this.isEmail()) {
					return true;
				}
			}

			if (this.hasAttr("numeric")) {
				if (!this.isNumeric()) {
					return true;
				}
			}

			if (this.hasAttr("alpha")) {
				if (!this.isAlpha()) {
					return true;
				}
			}

			if (this.hasAttr("alphanumeric")) {
				if (!this.isAlphaNumeric()) {
					return true;
				}
			}

			if (this.hasAttr("idcheck")) {
				if (!this.checkId()) {
					return true;
				}
			}
		}

		//해당 속성을 가지고 있는 확인
		this.hasAttr = function (attrName) {
			var IE = /*@cc_on!@*/false;

			if (IE) {
				if ( typeof($(this.input_el).attr(attrName)) != undefined &&  ((typeof($(this.input_el).attr(attrName)) == 'string' && $(this.input_el).attr(attrName) == "") || $(this.input_el).attr(attrName) == true)) {
					return true;
				} else {
					return false;
				}
			} else {
				if ($(this.input_el).attr(attrName) == true) {
					return true;
			  			} else {
			  				return false;
			  			}
			}
		}

		//INPUT의 값을 TRIM함
		this.doTrim = function () {
			var text = $(this.input_el).val();
			var pattern = /(^s*)|(s*$)/g; // s 공백 문자
			text = text.replace(pattern,"");
			return text;
		}

		//INPUT의 값을 리턴함
		this.getText = function () {
			var text = $(this.input_el).val();
			return text;
		}

		//INPUT이 필수입력인지 확인
		this.isRequired = function () {
			if (this.doTrim() == "") {
				alert(this.itemname + "는(은) 필수 입력입니다.");
				$(this.input_el).focus();
				return false;
			} else {
				return true;
			}
		}

		//INPUT의 최소입력 값을 확인
		this.checkMinlength = function () {
			var minlength = $(this.input_el).attr("minlength");
			var length = this.getText().length;

			if (minlength == "") {
				minlength = 0;
			}

			if (length < minlength) {
				alert(this.itemname + "는(은) " + minlength + "자 이상 입력하셔야 합니다.");
				$(this.input_el).focus();
				return false;
			} else {
				return true;
			}
		}

		//INPUT의 입력형식이 이메일인지 확인
		this.isEmail = function () {
			var pattern = /^([0-9a-zA-Z_-]+[0-9a-zA-Z_-])@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}/;
			var text = this.getText();

			if (!pattern.test(text)) {
				alert(this.itemname + "은(는) E-메일주소 형식이 아닙니다.");
				$(this.input_el).focus();
				return false;
			} else {
				return true;
			}
		}

		//INPUT의 입력형식이 숫자인지 확인
		this.isNumeric = function () {
			var text = this.getText();
			var length = text.length;
			var flag = true;

			for (i=0;i < length;i++) {
				if (text.charAt(i) < "0" || text.charAt(i) > "9") {
					flag = false;
				}
			}

			if (!flag) {
				alert(this.itemname + "은(는) 숫자만 입력하실수 있습니다.");
				$(this.input_el).focus();
				return false;
			} else {
				return true;
			}
		}

		//INPUT의 입력형식이 영문인지 확인
		this.isAlpha = function () {
			var text = this.getText();
			var pattern = /(^[a-zA-Z]+$)/;

			if (!pattern.test(text)) {
				alert(this.itemname + "은(는) 영문만 입력하실수 있습니다.");
				$(this.input_el).focus();
				return false;
			} else {
				return true;
			}
		}

		//INPUT의 입력형식이 영문/숫자 인지 확인
		this.isAlphaNumeric = function () {
			var text = this.getText();
			var pattern = /(^[a-zA-Z0-9]+$)/;

			if (!pattern.test(text)) {
				alert(this.itemname + "은(는) 영문 또는 숫자만 입력하실수 있습니다.");
				$(this.input_el).focus();
				return false;
			} else {
				return true;
			}
		}

		//INPUT의 입력형식이 ID인지 확인
		this.checkId = function () {
			var text = this.getText();
			var pattern = /(^[a-zA-Z0-9_]+$)/;

			if (!pattern.test(text)) {
				alert(this.itemname + "은(는) 영문, 숫자, 언더하이픈 기호(_)만 입력하실수 있습니다.");
				$(this.input_el).focus();
				return false;
			} else {
				return true;
			}
		}
	}

	//페이징
	function fncMakePageBody(total_size,cur_page_no) {

		var pagingParam = {
				'totalSize'   : total_size,
				'pageNo'      : cur_page_no,
				'pageSize'    : PAGE_SIZE,
				'pageListSize': PAGE_SIZE,
				'pageClickFunctionName': 'page_List',
				'showUnlinkedSymbols' : false
		};

		$(document).ready(function () {
			$('#paging_bar').magefister4jPaging(pagingParam);
		});
	}


	/*----------------------------------------------------------------------------------------------------------
		swf object
		swfprint('Object ID','경로','width','height','투명여부 (o-불투명 / t-투명)','변수명=xml경로&변수명=경로');
		----------------------------------------------------------------------------------------------------------*/
		//브라우져 체크
	appname = navigator.appName;
	useragent = navigator.userAgent;
	if (appname == "Microsoft Internet Explorer") appname = "IE";
	IE55 = (useragent.indexOf('MSIE 5.5')>0);  //5.5 버전
	IE6 = (useragent.indexOf('MSIE 6')>0);     //6.0 버전
	IE7 = (useragent.indexOf('MSIE 7')>0);     //7.0 버전
	IE8 = (useragent.indexOf('MSIE 8')>0);     //8.0 버전
	IE9 = (useragent.indexOf('MSIE 9')>0);     //9.0 버전

	//오브젝트 호출
	function swfReturn(objid,furl,fwidth,fheight,transoption,flashvars,pscale) {
		if (pscale == null) {
			pscale = "noscale";
		}

		if (typeof(scheme) == 'undefined' || scheme == null) {
			scheme = 'http';
		}

		var ieTxt = '<object id="'+ objid +'" classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="'+scheme+'"://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=10,0,0,0" width="'+ fwidth +'" height="' + fheight +'" align="middle">';
		ieTxt += '<param name="allowScriptAccess" value="always"/>';
		ieTxt += '<param name="movie" value="'+ furl +'"/>';
		ieTxt += '<param name="quality" value="high"/>';
		ieTxt += '<param name="bgcolor" value="#ffffff"/> ';
		ieTxt += '<param name="salign" value="T"/> ';
		ieTxt += '<param name="menu" value="false"/> ';
		if (flashvars) ieTxt += '<param name="flashVars" value="'+ flashvars +'">';
		if (transoption == "t") {
			ieTxt += '<param name="wmode" value="transparent"/>';
		} else if	(transoption == "o") {
			ieTxt += '<param name="wmode" value="opaque"/>';
		}
		ieTxt += '</object>';

		var ffTxt = '<object id="'+ objid +'" type="application/x-shockwave-flash" data="'+ furl +'" width="'+ fwidth +'" height="' + fheight +'"';
		if (flashvars) ffTxt += ' flashVars="'+ flashvars +'" ';
		if (transoption == "t")	{
			ffTxt += ' wmode="transparent"';
		} else if (transoption == "o") {
			ffTxt += ' wmode="opaque"';
		}
		ffTxt +='allowScriptAccess="always"';
		ffTxt += '></object>';

		if (appname=="IE") return ieTxt;
		else  return ffTxt;
	}

	/*
	 * 나이계산
	 * */
	function ageChk(obj,nobj) {
		var str = $(obj).val();

		str = str.replace(/[^0-9]/gi, '');

		if(str.length == 8){
			var dt = new Date()
			var yy = dt.getFullYear()

			// 존재하는 날자(유효한 날자) 인지 체크
			var bDateCheck = true;

			var nYear = str.substring(0, 4);
			var nMonth = str.substring(4, 6);
			var nDay = str.substring(6, 8);

			if (nYear < 1900 || nYear > yy) {	// 사용가능 하지 않은 년도 체크
				bDateCheck = false;
			}

			if (nMonth < 1 || nMonth > 12) {	// 사용가능 하지 않은 월도 체크
				bDateCheck = false;
			}

			// 해당달의 마지막 일자 구하기
			var lastDay = new Date( nYear, nMonth, "");
			if (nDay < 1 || nDay > lastDay) {	// 사용가능 하지 않은 날자 체크
				bDateCheck = false;
			}

			if(bDateCheck == false) {
				alert("존재하지 않은 년월일을 입력하셨습니다. 다시한번 확인해주세요");
				$(obj).val('').focus();
				return false;
			}

			if (bDateCheck == true) {
				$("#"+nobj).val(yy-nYear+1);
			}
		}
	}
	/*-----------------------------------------------
	전화번호 체크(휴대폰,일반전화포함)
	------------------------------------------------*/

function OnCheckPhone(str, field){
	var str;
	if(!isNumber(str)){
		error_numbr(str, field);
	}else{
		str = checkDigit(str);
		len = str.length;
		if(len > 0){
			if(len==8){
				if(str.substring(0,2)==02){
				error_numbr(str, field);
				}else{
				field.value  = phone_format(1,str);
				}
			}else if(len==9){
				if(str.substring(0,2)==02){
				field.value = phone_format(2,str);
				}else{
				error_numbr(str, field);
				}
			}else if(len==10){
				if(str.substring(0,2)==02){
				field.value = phone_format(2,str);
				}else{
				field.value = phone_format(3,str);
				}
			}else if(len==11){
				if(str.substring(0,2)==02){
				error_numbr(str, field);
				}else{
				field.value  = phone_format(3,str);
				}
			}else if(len==12){
				if(str.substring(0,2)==02){
				error_numbr(str, field);
				}else{
				field.value  = phone_format(4,str);
				}
			}else{
				error_numbr(str, field);
			}
		}
	}
}

function isNumber(str) {
    var pattern=/^(-?)([0-9]+)/;
    return (pattern.test(str) || str.length == 0) ? true : false;
}

 function checkDigit(num){
  var Digit = "1234567890";
  var string = num;
  var len = string.length
  var retVal = "";
  for (i = 0; i < len; i++){
   if (Digit.indexOf(string.substring(i, i+1)) >= 0){
    retVal = retVal + string.substring(i, i+1);
   }
  }
  return retVal;
 }
 function phone_format(type, num){
  if(type==1){
   return num.replace(/([0-9]{4})([0-9]{4})/,"$1-$2");
  }else if(type==2){
   return num.replace(/([0-9]{2})([0-9]+)([0-9]{4})/,"$1-$2-$3");
  }else if(type==3){
   return num.replace(/(^01.{1}|[0-9]{3})([0-9]+)([0-9]{4})/,"$1-$2-$3");
  }else if(type==4){
   return num.replace(/(^01.{1}|[0-9]{4})([0-9]+)([0-9]{4})/,"$1-$2-$3");
  }
 }
 function error_numbr(str, field){
  alert("정상적인 번호가 아닙니다.");
  field.value = "";
  field.focus();
  return;
 }
	/*-----------------------------------------------
	자동 하이푼 넣어 주기
	------------------------------------------------*/
	/*function OnCheckPhone(obj) {	//onkeyup="OnCheckPhone(this);"
		var str = $(obj).val();

		str = str.replace(/[^0-9]/gi, '');

		var exnum1 = str.substring(0, 1);
		var exnum2 = str.substring(1, 2);
		var exnum3 = str.substring(2, 3);
		var Ntype = "";

		if (exnum1 == "0") {
			if (exnum2 == "2") {			//02 시작
				str = str.substring(0, 10); //10자리 이상입력 못하도록 제어함

				if (str.length > 2 && str.length < 6)			Ntype = 1;
				else if (str.length > 5 && str.length < 10)		Ntype = 4;
				else if (str.length > 9)						Ntype = 7;
			} else {										//01~09 시작
				if (exnum2 == "5" && exnum3 == "0") {		//050 시작
					if (str.length > 4 && str.length < 8)		Ntype = 3;
					else if (str.length > 7 && str.length < 12)	Ntype = 6;
					else if (str.length > 11)					Ntype = 9;
				} else {
					if (str.length > 3 && str.length < 7)		Ntype = 2;
					else if (str.length > 6 && str.length < 11)	Ntype = 5;
					else if (str.length > 10)					Ntype = 8;
				}
			}
		} else if (exnum1 == "1") {							//1 시작
			if (str.length > 4 && str.length < 8)				Ntype = 3;
			else if (str.length > 7 && str.length < 12)			Ntype = 6;
			else if (str.length > 11)							Ntype = 9;
		} else {

		}

		if (Ntype == 1 )		str = str.replace(/([0-9]{2})([0-9]+)/, "$1-$2");
		else if (Ntype == 2 )	str = str.replace(/([0-9]{3})([0-9]+)/, "$1-$2");
		else if (Ntype == 3 )	str = str.replace(/([0-9]{4})([0-9]+)/, "$1-$2");

		else if (Ntype == 4 )	str = str.replace(/([0-9]{2})([0-9]{3})([0-9]+)/, "$1-$2-$3");
		else if (Ntype == 5 )	str = str.replace(/([0-9]{3})([0-9]{3})([0-9]+)/, "$1-$2-$3");
		else if (Ntype == 6 )	str = str.replace(/([0-9]{4})([0-9]{3})([0-9]+)/, "$1-$2-$3");

		else if (Ntype == 7 )	str = str.replace(/([0-9]{2})([0-9]{4})([0-9]+)/, "$1-$2-$3");
		else if (Ntype == 8 )	str = str.replace(/([0-9]{3})([0-9]{4})([0-9]+)/, "$1-$2-$3");
		else if (Ntype == 9 )	str = str.replace(/([0-9]{4})([0-9]{4})([0-9]+)/, "$1-$2-$3");

		$(obj).val(str);
	}
*/
	function RemoveDash2(sNo) {
		var reNo = "";
		for (var i=0; i<sNo.length; i++) {
			if ( sNo.charAt(i) != "-" ) {
				reNo += sNo.charAt(i);
			}
		}

		return reNo;
	}

	function GetMsgLen(sMsg) { // 0-127 1byte, 128~ 2byte
		var count = 0;
		for (var i=0; i<sMsg.length; i++) {
			if ( sMsg.charCodeAt(i) > 127 ) {
				count += 2;
			} else {
				count++;
			}
		}
		return count;
	}

	function checkDigit(num) {
		var Digit = "1234567890";
		var string = num;
		var len = string.length;
		var retVal = "";

		for (var i = 0; i < len; i++){
			if (Digit.indexOf(string.substring(i, i+1)) >= 0) {
				retVal = retVal + string.substring(i, i+1);
			}
		}
		return retVal;
	}

	//오늘의 날짜 리턴하기
	function getTodayDate(division) {

		var _date = new Date();
		var _year = _date.getFullYear();
		var _month = "" + (_date.getMonth()+1);
		var _day = ""+_date.getDate();

		if (_month.length == 1) _month ="0"+_month;
		if (_day.length == 1) _day ="0"+_day;

		var tmp = ""+_year+"년 "+_month+"월 "+_day+"일 ";

		if(division != undefined){
			tmp = _year+ division +_month+ division +_day;
		}

		return tmp;
	}

	// 필수항목체크
	function gfnIsNvl(obj) {
		if (obj.val() == "") {
			alert(obj.attr("title")+"은(는) 필수입력 항목입니다.");
			obj.focus();
			return true;
		}
	}

	// 세자리마다 콤마
	function gfnAddComma(number) {
		var sosu = '';
		number = number + '';
		try {
			if (number.indexOf('.') > -1) {
				sosu = number.substring(number.indexOf('.'), number.length);
				number = number.substring(0, number.indexOf('.')) + '';
			}
		} catch(e) {}

		number = ('' + number).split(',').join('');
		if (number.length > 3) {
			var mod = number.length % 3;
			var output = (mod > 0 ? (number.substring(0,mod)) : '');
			for (var i = 0; i < Math.floor(number.length / 3); i++) {
				if ((mod == 0) && (i == 0))
					output += number.substring(mod+ 3 * i, mod + 3 * i + 3);
				else
					output+= ',' + number.substring(mod + 3 * i, mod + 3 * i + 3);
			}
			return (output + sosu);
		}
		else
			return number + sosu;
	}

	// 숫자인지 체크하는 함수
	function gfnIsNumeric(numStr) {
		return !isNaN(numStr);
	}

	//
	function gfnOnlyNumber() {	// 숫자만 입력받는 함수

		if (( event.keyCode >= 48 && event.keyCode <= 57 )
				|| ( event.keyCode >= 96 && event.keyCode <= 105 )
				|| ( event.keyCode == 8 )	//백 스페이스
				|| ( event.keyCode == 9 )	//탭키
				|| ( event.keyCode == 13 )	//엔터
				|| ( event.keyCode == 46 )
		) {
			event.returnValue=true;
		} else {
			alert("숫자만 입력해주세요! ");
			event.returnValue=false;
		}

	}

	// 공백제거
	function gfnTrim(str) {
		return str.split(" ").join("");
	}

	// 바이트체크
	function gfnByteLength (str) {
		var tcount = 0;
		var length = str.length;

		for (var i = 0; i < length; i++) {
			var byteStr = str.charAt(i);
			if (escape(byteStr).length > 4) {
				tcount += 2;
			} else {
				tcount += 1;
			}
		}

		return tcount;
	};

	// 길이만큼 자르고 ...
	function gfnCutString(str, len) {
		if (str.length > len) {
			str = str.substring(0,len) + "...";
		}
		return str;
	}

	function gfnOpenLayerPopup(url) {
		var origWidth = "";
		try{
			modalResizeWidth = 10; 
			$.nmManual(url
				,{callbacks :{
						afterShowCont :function (nm) {
							try {
								origWidth = $('.nyroModalCont').width();
								if($.nmTop() != null){
									$.nmTop().resize(true, origWidth);
								}
								//fnAfterShowLayer();
							} catch(e) {}
						}
					}
				}
			);
			setTimeout(function(){modalResizeWidth=0;},5000);
		}catch(e){alert(e);}
	}


	function gfnLayerResize(){
		try {
			origWidth = $('.nyroModalCont').width();
			if($.nmTop() != null){
				$.nmTop().resize(true, origWidth);
			}
			//fnAfterShowLayer();
		} catch(e) {}
	}


	function gfnOpenMoveLayerPopup(url) {
		var origWidth = "";
		
		try{			
			$.nmManual(url
				,{callbacks :{
						afterShowCont :function (nm) {
							try {
								origWidth = $('.nyroModalCont').width();
								if($.nmTop() != null){
									$.nmTop().resize(true, origWidth);
								}
								//fnAfterShowLayer();
							} catch(e) {}
						}
					},ajax:{data:$("#popFrm").serialize(), type:"POST"}
				}
			);
			
		}catch(e){alert(e);}
	}


	function gfnLayerClose() {

		if ($.nmTop() != null || $.nmTop() != 'undefined') {
			try {
				$.nmTop().close();
			} catch(e) {}
		}
	}
	
	
	function gfnMobileLayerClose() {
		
		if ($.nmTop() != null || $.nmTop() != 'undefined') {
			try {
				$.nmTop().close();
			} catch(e) {}
		}
		$('.total-bg').hide();
	}



	/*
	*  DOCUMENT ELEMENT VALIDATION CHECK
	*  required  : 필수항목(INPUT,SELECT-ONE,TEXTAREA)
					<input type="radio" required />
	*  numeric   : 숫자입력항목(INPUT,TEXTAREA)
					<input type="text" numeric="numeric" />
	*  maxlength : 입력 최대 길이(INPUT,TEXTAREA)
					<input type="text" maxlength="100" />
	*  minlength : 입력 최소 길이(INPUT,TEXTAREA)
					<input type="text" minlength="3" />
	*  matchedlength : 입력 길이 매치(INPUT,TEXTAREA)
					<input type="text" matchlength="3" />
	*  email	 : 이메일 형식(INPUT,TEXTAREA)
					<input type="text" email="email" />
	*  ntemail	 : 이메일 형식(INPUT,TEXTAREA)
					<input type="text" ntemail="ntemail" />
	*  alpah	 : 알파벳 입력 형식(INPUT,TEXTAREA)
					<input type="text" alpah="alpah" />
	*/
	function elementCheck(V_FORM) {
		var formChk = true;
		var isFocus = false;
		if (V_FORM == null || V_FORM == 'undefined') {
			$form = $(document);
		} else {
			$form = $('#' + V_FORM);
		}

		$form.find("INPUT,TEXTAREA,SELECT").each(function () {
			var $element = $(this);
			var value = '';
			var title = '';
			var pattern;
			var valueLen;

			if ( $element.prop('tagName') == "SELECT") {
				value = $element.val() == null ? '' : $element.val();
				title = $element.attr('title');
			} else {
				if ( $element.attr('type') == "checkbox") {
					if ($("input[name=" + $element[0].name + "]:checkbox:checked").length > 0) {
						value = $('input[name='+ $element[0].name +']:checked').val();
					}

					title = $('input[name='+ $element[0].name +']:eq(0)').attr('title');
				} else if ($element.attr('type') == "radio") {
					if ($("input[name=" + $element[0].name + "]:radio:checked").length > 0) {
						value = $('input[name='+ $element[0].name +']:checked').val();
					}

					title = $('input[name='+ $element[0].name +']:eq(0)').attr('title');
				} else {
					isFocus = true;
					value = $element.val().trim();
					title = $element.attr('title');
				}
			}

			if (value == '') {
				valueLen = 0;
			} else {
				valueLen = (value.length + (escape(value)+"%u").match(/%u/g).length-1);
			}

			// required 체크
			if ( $element.attr('required') == 'required') {
				if (value == '') {
					alert(title + "는(은) 필수 입력입니다.");
					if (isFocus) {	$element.attr('tabIndex', -1).val(value).focus();	}
					formChk = false;
					return false;
				} else {
					if (isFocus) {	$element.val(value);	}
				}
			}

			if ( $element.attr('numeric') == 'numeric' && $element.val() != '') {
				pattern = /(^[0-9]+$)/;
				if (!pattern.test(value)) {
					alert(title + "는(은) 숫자로만 입력하셔야 합니다.");
					if (isFocus) {	$element.attr('tabIndex', -1).focus();	}
					formChk = false;
					return false;
				}
			}

			if ( $element.attr('alpah') == 'alpah') {
				pattern = /(^[a-zA-Z]+$)/;
				if (!pattern.test(value)) {
					alert(title + "는(은) 영문으로만 입력하셔야 합니다.");
					if (isFocus) {	$element.attr('tabIndex', -1).focus();	}
					formChk = false;
					return false;
				}
			}

			if ( $element.attr('alphanum') == 'alphanum') {
				pattern = /((^[a-zA-Z]+[0-9]+$)|(^[0-9]+[a-zA-Z]+$))/;
				if (!pattern.test(value)) {
					alert(title + "는(은) 영문,숫자 혼합으로 입력하셔야 합니다.");
					if (isFocus) {	$element.attr('tabIndex', -1).focus();	}
					formChk = false;
					return false;
				}
			}

//			if ( $element.attr('maxlength') > 0 && valueLen > $element.attr('maxlength')) {
//				alert(title + "는(은) 영문 " + $element.attr('maxlength') + "자(한글 " + (parseInt($element.attr('maxlength')) / 2) + "자) 이내로 작성 하십시오.");
//				if (isFocus) {	$element.attr('tabIndex', -1).focus();	}
//				formChk = false;
//				return false;
//			}

			/*if ( $element.attr('minlength') > 0 && valueLen < $element.attr('minlength')) {
				alert(title + "는(은) 영문 " + $element.attr('minlength') + "자(한글 " + (parseInt($element.attr('minlength')) / 2) + "자) 이상 작성 하십시오.");
				if (isFocus) {	$element.attr('tabIndex', -1).focus();	}
				formChk = false;
				return false;
			}*/

			/*if ( $element.attr('matchedlength') > 0 && $element.attr('matchedlength') != valueLen) {
				alert(title + "는(은) " + $element.attr('matchedlength') + "자 입니다.");
				if (isFocus) {	$element.attr('tabIndex', -1).focus();	}
				formChk = false;
				return false;
			}*/

			if ( $element.attr('email') == 'email' && $element.val() != '') {
				pattern = /^([0-9a-zA-Z_-]+[0-9a-zA-Z_-])@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}/;
				if (!pattern.test(value)) {
					alert("메일주소 형식이 잘못되었습니다.");
					if (isFocus) {	$element.attr('tabIndex', -1).focus();	}
					formChk = false;
					return false;
				}
			}

			if ( $element.attr('ntemail') == 'ntemail' && valueLen >0 ) {
				pattern = /^([0-9a-zA-Z_-]+[0-9a-zA-Z_-])@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}/;
				if (!pattern.test(value)) {
					alert("메일주소 형식이 잘못되었습니다.");
					if (isFocus) {	$element.attr('tabIndex', -1).focus();	}
					formChk = false;
					return false;
				}
			}
		});

		return formChk;
	}

	/*
	 *  품목/기술정보 저장체크 함수
	 *  값이 있을때 리턴
	 */
	function productCheckBlank(V_FORM) {
		var formChk = true;
		if (V_FORM == null || V_FORM == 'undefined') {
			$form = $(document);
		} else {
			$form = $('#' + V_FORM);
		}

		$form.find("INPUT,TEXTAREA,SELECT").each(function () {
			var $element = $(this);
			var value = '';

			if ( $element.prop('tagName') == "SELECT") {
				value = $element.val() == null ? '' : $element.val();
			} else {
				if ( $element.attr('type') == "checkbox") {
					if ($("input[name=" + $element[0].name + "]:checkbox:checked").length > 0) {
						value = $('input[name='+ $element[0].name +']:checked').val();
					}
				} else if ($element.attr('type') == "radio") {
					if ($("input[name=" + $element[0].name + "]:radio:checked").length > 0) {
						value = $('input[name='+ $element[0].name +']:checked').val();
					}
				} else {
					value = $element.val();
				}
			}

			if ( $element.attr('required') == 'required') {
				if (value != '') {
					alert("입력 중인 품목정보가 있습니다.\n품목정보를 모두 입력하신 후 품목추가 버튼을 눌러주세요.");
					$('#goodsInfo').attr('tabIndex', -1).focus();
					formChk = false;
					return false;
				}
			}

		});

		return formChk;
	}

	/*공통 셀렉트박스  Set*/

	function gfncAutoSelectSet(MEGACATEG, SUBCATEG, bindId, selectType, selectedVal, callBack) {

		$.ajax({
			type: "POST",
			url: "/common/common.do?method=selectListCommonCode",
			data: {
				"MEGACATEG": MEGACATEG
				,"SUBCATEG" : SUBCATEG
				,"USEYN"	   : "Y"
			},
			dataType: "json",
			success: function (transport) {

				var pPartCd = eval(transport.resultList);

				var resultHtml = "";
				if (selectType == "A") {
					resultHtml += '<option value="">전체</option>';
				} else if (selectType == "S") {
					resultHtml += '<option value="">선택</option>';
				}

				$.each(pPartCd, function (key, json) {

					var sel = "";

					sel += '<option value="'+json.UNITCATEG+'" '+((json.UNITCATEG == selectedVal) ? 'selected' : '')+'>'+json.UNITCATEGDET+'</option>';

					resultHtml += sel;

				});

				$("#" + bindId).html(resultHtml);
				if (callBack != ""){
					eval(callBack);
				}
			}
		});
	}

	function fnNvl(args1, args2) {
		return (args1 == null || args1 == 'undefined' ? args2 : args1);
	}

	// 프로그래스바 시작
	function gfnStartProgress() {
		try {
			if ($("#dialog").length < 1) {
				if(N_DEPT_LOCT_CD == 'C'){
					var dialogHtml = '<div id="dialog" style="position:fixed; top:50%; left:50%; padding:.2em; width:50px; height:50px; overflow:hidden;"><img src="'+COMMON_IMAGES_CONF+'/common/c_loading.gif" /></div>';
				}else if(N_DEPT_LOCT_CD== 'J'){
					var dialogHtml = '<div id="dialog" style="position:fixed; top:50%; left:50%; padding:.2em; width:50px; height:50px; overflow:hidden;"><img src="'+COMMON_IMAGES_CONF+'/common/j_loading.gif" /></div>';
				}else if(N_DEPT_LOCT_CD== 'A'){
					var dialogHtml = '<div id="dialog" style="position:fixed; top:50%; left:50%; padding:.2em; width:50px; height:50px; overflow:hidden;"><img src="'+COMMON_IMAGES_CONF+'/common/j_loading.gif" /></div>';
				}

				var theForm = document.forms[0];
				$(theForm).append(dialogHtml);
				$("#dialog").dialog({
					autoOpen: false,
					modal: true
				});
			}
			$("#dialog").parent(".ui-dialog").children(".ui-dialog-titlebar").remove();
			$("#dialog").parent(".ui-dialog").children(".ui-icon").remove();
			$("#dialog").parent().parent().removeAttr("class");
			$("#dialog").parent().removeAttr("class");
			$("#dialog").parent().removeAttr("class");
			$("#dialog").removeAttr("class");
			$("#dialog").dialog("open");
			//$("#dialog").mousedown(function(){return false;});
			$(".ui-resizable-handle ui-resizable-se ui-icon ui-icon-gripsmall-diagonal-se ui-icon-grip-diagonal-se").removeAttr("class");
		} catch(e) {}
	}

	// 프로그래스바 끝
	function gfnEndProgress() {
		try {
			$("#dialog").dialog("close");
		} catch(e) {}
	}


	// 롤오버 //
	(function () {
		$(function () {
			$(".mQuick > a").each(function () {
				var image = $(this).children("img");
				var imgsrc = $(image).attr("src");

				$(this).mouseover(function () {
					var on = imgsrc.replace(/_off.gif/,"_on.gif");
					$(image).attr("src",on);
				});

				$(this).mouseout(function () {
					var off = imgsrc.replace(/_on.gif/,"_off.gif");
					$(image).attr("src",off);
				});

				$(this).mousedown(function () {
					var dn = imgsrc.replace(/_off.gif/,"_on.gif");
					$(image).attr("src",dn);
				});
			});
		});
	})();


	// 레이어팝업 //
	function layer_open(el) {
		var temp = $('#' + el);		//레이어의 id를 temp변수에 저장
		temp.fadeIn();

		temp.find('a.cbtn').click(function (e) {
			temp.fadeOut();
			e.preventDefault();
		});
	}


	// 사이트맵 //
	$(document).ready(function () {
		$(".smMenu").slideUp(0);
		$('.smTitle > dd > a').eq(0).click(function () {
			$('.smTitle > dd > a').eq(0).css('display', 'none');
			$('.smTitle > dd > a').eq(1).css('display', 'block');
			$(".smMenu").slideUp(350,'easeOutQuad');
		});

		$('.smTitle > dd > a').eq(1).click(function () {
			$('.smTitle > dd > a').eq(1).css('display', 'none');
			$('.smTitle > dd > a').eq(0).css('display', 'block');
			$(".smMenu").slideDown(350,'easeOutQuad');
		});
	});


	// 상세검색 달력 스크립트
	$(document).ready(function () {
		var clareCalendar = {
				monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
				dayNamesMin: ['일','월','화','수','목','금','토'],
				weekHeader: 'Wk',
				dateFormat: 'yy-mm-dd', //형식(20120303)
				autoSize: false, //오토리사이즈(body등 상위태그의 설정에 따른다)
				changeMonth: true, //월변경가능
				changeYear: true, //년변경가능
				showMonthAfterYear: true, //년 뒤에 월 표시
				buttonImageOnly: true, //이미지표시
				buttonText: '달력선택', //버튼 텍스트 표시
				buttonImage: '../../images/btnIcn/btn_calendar.png', //이미지주소
				showOn: "both", //엘리먼트와 이미지 동시 사용(both,button)
				yearRange: '1990:2020' //1990년부터 2020년까지
			};
		//$("#fromDt").datepicker(clareCalendar);
		//$("#toDt").datepicker(clareCalendar);
		//$("img.ui-datepicker-trigger").attr("style","margin-left:5px; vertical-align:middle; cursor:pointer;"); //이미지버튼 style적용
		//$("#ui-datepicker-div").hide(); //자동으로 생성되는 div객체 숨김
	});

	// swfUpload 팝업
	function fnFileUpload(LIMIT_FILE_CNT, AES_YN) {
		if (LIMIT_FILE_CNT == undefined || LIMIT_FILE_CNT == "") {
			LIMIT_FILE_CNT = 500;
		}
		
		if (AES_YN == undefined || AES_YN == "") {
			AES_YN = "N";
		}

		window.open("/common/index.do?jPath=popUpload/swfPopup&LIMIT_FILE_CNT="+LIMIT_FILE_CNT+"&AES_YN="+AES_YN
				,"swfUpload","top=0px, left=100px, height=450px, width=480px, status=no, scrollbars=yes,location=no ,toolbar=no,menubar=no,resizable=no");
	}
	// swfUpload 팝업 (닷넷소스
	function fnAspFileUpload(LIMIT_FILE_CNT, F_PATH, RTN_FN_NM, INTG_UID) {  
		if (LIMIT_FILE_CNT == undefined || LIMIT_FILE_CNT == "") {
			LIMIT_FILE_CNT = 500;
		}
		window.open("/common/index.do?jPath=popUpload/swfPopup&LIMIT_FILE_CNT="+LIMIT_FILE_CNT+"&AES_YN=N&F_PATH="+ F_PATH + "&RTN_FN_NM="+RTN_FN_NM + "&USRID=" + INTG_UID
				,"swfUpload","top=0px, left=100px, height=450px, width=480px, status=no, scrollbars=yes,location=no ,toolbar=no,menubar=no,resizable=no");
	}

	// 첨부파일 다운로드
	function gfnAttachFileDown(FILENM, FILEPATH, TRANSFILENM) {
		var fileFrm = document.fileDownFrmFooter;
		fileFrm.F_FILENM.value=FILENM;
		fileFrm.F_FILEPATH.value=FILEPATH;
		fileFrm.F_TRANSFILENM.value=TRANSFILENM;
		fileFrm.action = "/common/fileDown.do";
		fileFrm.target = "fileDownFrameFooter";
		fileFrm.submit();
	}
	
	function gfnAttachFileDownFullnm(FILEPATH, FILENM) {
		var fileFrm = document.fileDownFrmFooter;
		if(FILEPATH.indexOf("../") > -1){
			alert("해당경로에 파일이 존재하지 않습니다.");
			return;
		}else{
			fileFrm.F_FILENM.value=FILENM;
			fileFrm.F_FILEPATH.value=FILEPATH;
			fileFrm.F_TRANSFILENM.value="";
		}
		
		fileFrm.action = "/common/fileDownFull.do";
		fileFrm.target = "fileDownFrameFooter";
		fileFrm.submit();
	}

	
	function fncValChk() {
		var result= true;
		$(".required").each(function (i) {
			if($.trim($(this).val())=="") {
				alert($(this).attr("title")+"는(은) 필수 입력입니다.");
				$(this).focus();
				result= false;
				return false;
			}
		});
		return result;
	}


	// 행사/프로그램 - 참가자관리 신청자 SMS/Email
	// 행사/프로그램 - 참가자관리 신청자 SMS/Email
	function fnUsrSendLayer(url, checkId) {

		$("#hiddenFrm").html("");
		var i=0;
		var inputHtml = "";
		var valTxt = "";
		$("[name="+checkId+"]").each(function( index ) {

			if($( this ).is(":checked")){
				valTxt = $("#userId"+index+"").val();
				inputHtml += "<input type='hidden' name='filememberList["+i+"].userId' value='"+valTxt+"'/> ";
				valTxt = $("#userNm"+index+"").val();
				inputHtml += "<input type='hidden' name='filememberList["+i+"].userNm' value='"+valTxt+"'/> ";
				valTxt = $("#userEm"+index+"").val();
				inputHtml += "<input type='hidden' name='filememberList["+i+"].userEm' value='"+valTxt+"'/> ";
				valTxt = $("#userHp"+index+"").val();
				inputHtml += "<input type='hidden' name='filememberList["+i+"].userHp' value='"+valTxt+"'/> ";
				i++;
			}
		});
		if(inputHtml == ""){
			alert("선택된 인원이 없습니다.");
			return;
		}

		$("#hiddenFrm").html(inputHtml );
		window.open("about:blank", "sendPop", "width=1300,height=800,scrollbars=yes,top=10,left=10,resizable=yes");
		$("#hiddenFrm").attr("action",url);
		$("#hiddenFrm").attr("target","sendPop");
		$("#hiddenFrm").submit();
	}


	//#######채용정보 인재검색 팝업창 ######=======
	//채용신청(학생정보)
	function fnRePsDetail(usrid) {
		//pop_RePsD
		if(usrid != ""){
			var winpop = window.open('/Sa/Re/RePs010D.do?usrid='+usrid+'&POP_OPEN_YN=Y', 'RePs010D', 'width=1000, height=650, scrollbars=yes');
			winpop.focus();
		}
	}
	//#######채용정보 인재검색 팝업창 ######=======
	
	function pop_StuInfo(intg_uid){
		var popupWidth = 1000;
		var popupHeight = 900;
		if(intg_uid != ""){
			var winpop = window.open('/St/Si/StInfo010D.do?INTG_UID='+intg_uid+'&POP_OPEN_YN=Y', 'StInfo010D', 'width='+ popupWidth +', height='+ popupHeight +', scrollbars=yes');
			winpop.focus();
		}
	}


	//학생 이력서 팝업
	function fnResume(resseq,ty,type,usrid) {
		if (resseq) {
			if (usrid){
				var winpopres = window.open('/'+type+'/Re/ReRs010D.do?RES_SEQ='+resseq+'&PERSON='+ty+'&POP_OPEN_YN=Y&USRID='+usrid, 'ReRs010D1', 'width=820, height=700, scrollbars=yes');
				winpopres.focus();
			}else{
				var winpopres = window.open('/'+type+'/Re/ReRs010D.do?RES_SEQ='+resseq+'&PERSON='+ty+'&POP_OPEN_YN=Y', 'ReRs010D1', 'width=820, height=700, scrollbars=yes');
				winpopres.focus();
			}
		} else {
			alert('대표설정된 이력서가 없습니다.');
		}
	}

	//학생 자기소개서 팝업
	function fnInfo(selfinfseq,ty,type) {
		if (selfinfseq) {
			var winpopres = window.open('/'+type+'/Re/ReSi010D.do?SELF_INF_SEQ='+selfinfseq+'&PERSON='+ty+'&POP_OPEN_YN=Y', 'ReRs010D2', 'width=820, height=700, scrollbars=yes');
			winpopres.focus();
		} else {
			alert('대표설정된 자기소개서가 없습니다.');
		}
	}
	
	function fnRetUrl(retUrl,retTp){
		$("#headerfrm").attr("action",retUrl);
		$("#headerfrm").attr("target",retTp);
		$("#headerfrm").submit();
		$("#headerfrm").attr("action","#");
		$("#headerfrm").attr("target","_self");
	}

	//추천채용이력 팝업
	function fnReInfo(usrid,rerdseq,ty,type) {
		if (rerdseq) {
			var winpopres = window.open('/'+type+'/Re/Rerd010DPop.do?USRID='+usrid+'&RERD_INF_SEQ='+rerdseq+'&PERSON='+ty+'&POP_OPEN_YN=Y', 'ReRd010D2', 'width=820, height=700, scrollbars=yes');
			winpopres.focus();
		}
	}


	// 레이어팝업 //
	function layerOpen(el){
		var total = $('#' + el);
		var temp = $('.' + el);

		total.fadeIn(100);
		temp.css('display','block')

		if (temp.outerHeight() < $(document).height() ) temp.css('margin-top', '-400px');
		else temp.css('top', '0px');
		if (temp.outerWidth() < $(document).width() ) temp.css('margin-left', '-'+temp.outerWidth()/2+'px');
		else temp.css('left', '0px');

		temp.find('a.cbtn').click(function(e){
			total.fadeOut(100);
			e.preventDefault();
		});

		/*
		$('.bg').click(function(e){
			total.fadeOut();
			e.preventDefault();
		});
		*/
	}
	
	
	//멀티 이력서
	function fnResMulti(arrseq) {
		if(arrseq != ""){
			var winpop = window.open('/Sa/Re/ReStRs010MULTI.do?ARRUSRID='+arrseq+'&POP_OPEN_YN=Y', 'RESMULTI', 'width=1000, height=650, scrollbars=yes');
			winpop.focus();
		}else{
			alert("선택후 클릭하여 주시기바랍니다.");
		}
	}
	
	
	//NCS S.T.A.R 팝업상세
	function fnNcsStar(seq,usrId) {
		var winpop = window.open('/user/Ss/SsNc010Pop.do?NCS_SEQ='+seq+'&USRID='+usrId+'&POP_OPEN_YN=Y', 'SsNc010Pop', 'width=800, height=650, scrollbars=yes');
		winpop.focus();
	}
	
	//만족도조사 미리보기
	function fnPreSurvey(surveySeq) {
		//pop_PreSurvey
		var winpop = window.open('/adn/Pg/PgPreSurvey.do?SURVEY_SEQ='+surveySeq+'&POP_OPEN_YN=Y', 'PgPreSurvey', 'width=1240, height=650, scrollbars=yes');
		winpop.focus();
	}
	
	
	function fnPersonCfmPrint(prm_seq,app_seq,pg_cd) {
		window.open("/common/personCfmPrint.do?&PRM_SEQ="+prm_seq+"&PRM_APP_SEQ="+app_seq+ "&PG_CD=" + pg_cd
				,"confirmPrint","width=800,height=800,scrollbars=yes");
	}
	
	
	function fnGroupCfmPrint(prm_seq,app_seq) {
		window.open("/common/groupCfmPrint.do?&PRM_SEQ="+prm_seq+"&PRM_GROUP_SEQ="+app_seq
				,"confirmPrint","width=800,height=800,scrollbars=yes");
	}
	
	function fnPersonMultiCfmPrint(prm_seq,usrId) {
		window.open("/common/persoMultiCfmPrint.do?&PRM_SEQ="+prm_seq+"&USRID="+usrId
				,"confirmPrint","width=800,height=800,scrollbars=yes");
	}
	
	// 역량점수현황 2016.7.12 추가 홍성현
	function fnExMultiCfmPrint(item_app_seq,prm_seq,usrId) {
		window.open("/user/Ex/ExMultiCfmPrint.do?ITEM_APP_SEQ="+item_app_seq+"&PRM_APP_SEQ="+prm_seq+"&USRID="+usrId
				,"confirmPrint","width=800,height=800,scrollbars=yes");
	}
	
	
	// swfUpload 팝업(엑셀만) 
	function fnFileUploadToExcelFile(LIMIT_FILE_CNT) {
		if (LIMIT_FILE_CNT == undefined || LIMIT_FILE_CNT == "") {
			LIMIT_FILE_CNT = 500;
		}
		
		window.open("/common/index.do?jPath=swfUpload/swfPopup&LIMIT_FILE_CNT="+LIMIT_FILE_CNT+"&FILE_TYPES=EXCEL"
				,"swfUpload","top=100px, left=100px, height=500px, width=500px, status=no, scrollbars=no,location=no ,toolbar=no,menubar=no,resizable=no");
	}
	
	
	
	function fnGoSite(objNm){
		$("#siteFormFooter").attr("target","_blank");
		$("#siteFormFooter").attr("action",$("#"+objNm).val());
		$("#siteFormFooter").submit();
	}
	
	
	
	
	// swfUpload 팝업(엑셀만) 리턴함수 추가 20180302
	function fnFileUploadToExcelFile_1(LIMIT_FILE_CNT , RTN_FN_NM) {
		if (LIMIT_FILE_CNT == undefined || LIMIT_FILE_CNT == "") {
			LIMIT_FILE_CNT = 500;
		}
		
		window.open("/common/index.do?jPath=swfUpload/swfPopup&LIMIT_FILE_CNT="+LIMIT_FILE_CNT+"&FILE_TYPES=EXCEL&RTN_FN_NM=" + RTN_FN_NM
				,"swfUpload","top=100px, left=100px, height=500px, width=500px, status=no, scrollbars=no,location=no ,toolbar=no,menubar=no,resizable=no");
	}
	
	function fnIrMultiCfmPrint(item_app_seq,prm_seq,usrId) {
		window.open("/common/IrMultiCfmPrint.do?ITEM_APP_SEQ="+item_app_seq+"&PRM_APP_SEQ="+prm_seq+"&USRID="+usrId
				,"confirmPrint","width=850,height=800,scrollbars=yes");
	}

	/**
	 *  기간 선택 달력 (제이쿼리 달력)
	 *  from_id : 시작일 input Id
	 *  to_id : 끝일 input Id
	 */
	function setFromToDatePicker(from_id, to_id){	
		setDatePicker(from_id);
		setDatePicker(to_id);
		
		$('#'+from_id).datepicker();
	    $('#'+from_id).datepicker("option", "maxDate", $("#"+to_id).val());
	    $('#'+from_id).datepicker("option", "onClose", function ( selectedDate ) {
	        $("#"+to_id).datepicker( "option", "minDate", selectedDate );
	    });
	 
	    $('#'+to_id).datepicker();
	    $('#'+to_id).datepicker("option", "minDate", $("#"+from_id).val());
	    $('#'+to_id).datepicker("option", "onClose", function ( selectedDate ) {
	        $("#"+from_id).datepicker( "option", "maxDate", selectedDate );
	    });    
	}

    // 해당 문자열에서 선택된 값 삭제
    function RemoveMark(ValueC,RMark) {
    	var arr, result="", i=0;
    	if(RMark) arr = ValueC.split(RMark);
    	else arr = ValueC.split(",");
    	do{
    		result = result + arr[i];
    	}while(++i < arr.length);
    	return result;
    }

    // 문자열에 콤마추가
    function StringWithComma(ValueC) {
    	ValueC = ""+ValueC;
    	var result="", i;
    	for(i=0 ; i < ValueC.length ; i++){
    		result = result + ValueC.charAt(i);
    		iWhere = ValueC.length - 1 - i;
    		if (!(iWhere%3)&&(ValueC.length>3)&&iWhere) result = result + ",";
    	}
    	if(result.charAt(0) == "-" && result.charAt(1) == ",") result = result.charAt(0)+result.substring(2,result.length);
    	return result;
    }
	
	// 숫자 천단위 구분
    function NumComma_V(val) {
    	return val.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")
    }

    // 숫자 천단위 구분
    function NumComma_I(obj) {
    	var Value = $(obj).val(), ValueTmp = '', SosuFlag = false;
    	if(Value.indexOf(".") >= 0) {
    		SosuFlag = true;
    		if(Value.split(".")[1] != '') ValueTmp = Value.split(".")[1];
    		Value = Value.split(".")[0];
    	}
    	Value = OnlyNum(Value);
    	Value = RemoveMark(Value, ',');
    	Value = StringWithComma(Value);
    	if(SosuFlag) Value = Value + "." + ValueTmp;
    	$(obj).val(Value);
    }
	
	// swfUpload 팝업(이미지만) 
	function fnFileUploadToImageFile(LIMIT_FILE_CNT) {
		if (LIMIT_FILE_CNT == undefined || LIMIT_FILE_CNT == "") {
			LIMIT_FILE_CNT = 500;
		}
		
		window.open("/common/index.do?jPath=swfUpload/swfPopup&LIMIT_FILE_CNT="+LIMIT_FILE_CNT+"&FILE_TYPES=IMAGE"
				,"swfUpload","top=100px, left=100px, height=500px, width=500px, status=no, scrollbars=no,location=no ,toolbar=no,menubar=no,resizable=no");
	}
    
	/**
	 * Jquery를 이용한 숫자만 입력 받는함수
	 * 사용방법 : onkeyup="onlyNumberKeyUp(this);"
	 */
	function onlyNumberKeyUp(obj) {	
		$(obj).val( $(obj).val().replace(/[^0-9:\-]/gi,"") );
	}
	
	/**
	 * Jquery를 이용한 DATE 포맷
	 */
	Date.prototype.format = function (f) {
	    if (!this.valueOf()) return " ";

	    var weekKorName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
	    var weekKorShortName = ["일", "월", "화", "수", "목", "금", "토"];
	    var weekEngName = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
	    var weekEngShortName = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
	    var d = this;

	    return f.replace(/(yyyy|yy|MM|dd|KS|KL|ES|EL|HH|hh|mm|ss|a\/p)/gi, function ($1) {

	        switch ($1) {
	            case "yyyy": return d.getFullYear(); // 년 (4자리)
	            case "yy": return (d.getFullYear() % 1000).zf(2); // 년 (2자리)
	            case "MM": return (d.getMonth() + 1).zf(2); // 월 (2자리)
	            case "dd": return d.getDate().zf(2); // 일 (2자리)
	            case "KS": return weekKorShortName[d.getDay()]; // 요일 (짧은 한글)
	            case "KL": return weekKorName[d.getDay()]; // 요일 (긴 한글)
	            case "ES": return weekEngShortName[d.getDay()]; // 요일 (짧은 영어)
	            case "EL": return weekEngName[d.getDay()]; // 요일 (긴 영어)
	            case "HH": return d.getHours().zf(2); // 시간 (24시간 기준, 2자리)
	            case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2); // 시간 (12시간 기준, 2자리)
	            case "mm": return d.getMinutes().zf(2); // 분 (2자리)
	            case "ss": return d.getSeconds().zf(2); // 초 (2자리)
	            case "a/p": return d.getHours() < 12 ? "오전" : "오후"; // 오전/오후 구분
	            default: return $1;
	        }
	    });
	};
	
	String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
	String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
	Number.prototype.zf = function(len){return this.toString().zf(len);};


	 
	 function fileChk(obj, event, target, idx) {
		var imgExt = ["jpg", "jpeg", "gif", "png", "bmp"];
		var docExt	= ["doc", "docx", "ppt", "pptx", "xls", "xlsx", "hwp", "txt", "pdf", "jpg", "jpeg", "gif", "png", "bmp"];
		
		var tmp = $(obj).val();
		var ext = tmp.slice(tmp.lastIndexOf(".")+1).toLowerCase();
		
		if(event == 'img') {
			if(imgExt.indexOf(ext) < 0) {	// 이미지 파일이 아니면
				alert('이미지의 확장자가 '+ imgExt +' 파일만 업로드 가능합니다.');
				$(obj).val('');
				$('#'+ target + idx).val('');
				return;
			}
			else {
				$('#'+ target + idx).val(tmp);
			}
		}
		else if(event == 'xlsUp') {
			if(ext != 'xls') {
				alert('엑셀 업로드는 확장자가 xls 파일만 업로드 가능합니다.');
				$(obj).val('');
				$('#'+ target + idx).val('');
				return;
			}
			else {
				$('#'+ target + idx).val(tmp);
			}
		}
		else {
			if(docExt.indexOf(ext) < 0) {	// 이미지 파일이 아니면
				alert('첨부파일은 확장자가 '+ docExt +' 파일만 업로드 가능합니다.');
				$(obj).val('');
				$('#'+ target + idx).val('');
				return;
			}
			else {
				$('#'+ target + idx).val(tmp);
			}
		}
	}

	 
	// 첨부파일 다운로드
	function gfnBoardAttachFileDown(FILENM, FILEPATH, TRANSFILENM) {
		var fileFrm = document.fileDownFrmFooter;
		fileFrm.F_FILENM.value=FILENM;
		fileFrm.F_FILEPATH.value=FILEPATH;
		fileFrm.F_TRANSFILENM.value=TRANSFILENM;
		fileFrm.action = "/common/BoardfileDown.do";
		fileFrm.target = "fileDownFrameFooter";
		fileFrm.submit();
	}
	
	
	var encrypt = {
	    // private property
	    _keyStr : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",
	 
	    // public method for encoding
	    encode : function (input) {
	        var output = "";
	        var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
	        var i = 0;
	 
	        input = encrypt._utf8_encode(input);
	 
	        while (i < input.length) {
	 
	            chr1 = input.charCodeAt(i++);
	            chr2 = input.charCodeAt(i++);
	            chr3 = input.charCodeAt(i++);
	 
	            enc1 = chr1 >> 2;
	            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
	            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
	            enc4 = chr3 & 63;
	 
	            if (isNaN(chr2)) {
	                enc3 = enc4 = 64;
	            } else if (isNaN(chr3)) {
	                enc4 = 64;
	            }
	 
	            output = output +
	            this._keyStr.charAt(enc1) + this._keyStr.charAt(enc2) +
	            this._keyStr.charAt(enc3) + this._keyStr.charAt(enc4);
	 
	        }
	 
	        return output;
	    },
	 
	    // public method for decoding
	    decode : function (input) {
	        var output = "";
	        var chr1, chr2, chr3;
	        var enc1, enc2, enc3, enc4;
	        var i = 0;
	 
	        input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
	 
	        while (i < input.length) {
	 
	            enc1 = this._keyStr.indexOf(input.charAt(i++));
	            enc2 = this._keyStr.indexOf(input.charAt(i++));
	            enc3 = this._keyStr.indexOf(input.charAt(i++));
	            enc4 = this._keyStr.indexOf(input.charAt(i++));
	 
	            chr1 = (enc1 << 2) | (enc2 >> 4);
	            chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
	            chr3 = ((enc3 & 3) << 6) | enc4;
	 
	            output = output + String.fromCharCode(chr1);
	 
	            if (enc3 != 64) {
	                output = output + String.fromCharCode(chr2);
	            }
	            if (enc4 != 64) {
	                output = output + String.fromCharCode(chr3);
	            }
	 
	        }
	 
	        output = encrypt._utf8_decode(output);
	 
	        return output;
	 
	    },
	 
	    // private method for UTF-8 encoding
	    _utf8_encode : function (string) {
	        string = string.replace(/\r\n/g,"\n");
	        var utftext = "";
	 
	        for (var n = 0; n < string.length; n++) {
	 
	            var c = string.charCodeAt(n);
	 
	            if (c < 128) {
	                utftext += String.fromCharCode(c);
	            }
	            else if((c > 127) && (c < 2048)) {
	                utftext += String.fromCharCode((c >> 6) | 192);
	                utftext += String.fromCharCode((c & 63) | 128);
	            }
	            else {
	                utftext += String.fromCharCode((c >> 12) | 224);
	                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
	                utftext += String.fromCharCode((c & 63) | 128);
	            }
	 
	        }
	 
	        return utftext;
	    },
	 
	    // private method for UTF-8 decoding
	    _utf8_decode : function (utftext) {
	        var string = "";
	        var i = 0;
	        var c = c1 = c2 = 0;
	 
	        while ( i < utftext.length ) {
	 
	            c = utftext.charCodeAt(i);
	 
	            if (c < 128) {
	                string += String.fromCharCode(c);
	                i++;
	            }
	            else if((c > 191) && (c < 224)) {
	                c2 = utftext.charCodeAt(i+1);
	                string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
	                i += 2;
	            }
	            else {
	                c2 = utftext.charCodeAt(i+1);
	                c3 = utftext.charCodeAt(i+2);
	                string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
	                i += 3;
	            }
	 
	        }
	 
	        return string;
	    }
	}
	
	// ajax
	function ajaxModule(DATA, Url, Fn) {
		$.ajax({
			type:'POST',
			url:Url,
			dataType:'json',
			data:DATA,
			timeout:30000,
			cache:false,
			success:function whenSuccess(arg) {
				Fn(arg);
			},
			error : function(xhr, ajaxOptions, thrownError) { 
	        	if(xhr.status == 403) {
	                alert("세션이 종료되었거나 접근이 유효하지 않은 URL 입니다.");
	                sessionout();
	            }
	        	else {
	                alert('xrs.status = ' + xhr.status + '\n' +
	                		'thrown error = ' + thrownError + '\n' +
	                		'xhr.statusText = '  + xhr.statusText);
	            }
	        }
		});
	}
	
	/**
     * Iframe 영역 인쇄
     */
     function fnPrinIframetInPopup(title)  {
     	
 		var objWin=null;
         var strFeature;
         strFeature = "width=900,height=700,toolbar=no,location=no,directories=no";
         strFeature += ",status=no,menubar=no,scrollbars=yes,resizable=no";

         objWin = window.open('', 'print', strFeature);
         self.focus();

         objWin.document.open();
         objWin.document.write('<html>');
         objWin.document.write('<head>');
         //css 가져오기
         $.each( $('head > link[rel=stylesheet]'), function (index) {
         	objWin.document.write($(this)[0].outerHTML);
         });
         
         objWin.document.write('<style>');
         //버튼삭제
         objWin.document.write('a[class^="btn"] {display : none;}' );
         objWin.document.write('a {visibility:hidden}' );
         objWin.document.write('div[class="titlePrint"] {width : 80% }' );
         objWin.document.write('div[class="popArea"] {width : 80% }' );
         objWin.document.write('</style>');
         objWin.document.write('</head>');
         // 백그라운드 제거
         
         objWin.document.write('<div class=\"titlePrint\">');
         objWin.document.write(title);
         objWin.document.write('</div>');
         objWin.document.write('<div class=\"popArea\">');
         objWin.document.write('<body style="">');
         objWin.document.write('<div class=\"iframeCtn\">');
         objWin.document.write($('div.iframeCtn').html());
         objWin.document.write('</div><br/></body></div></html>');
         objWin.document.close();
         
         objWin.print();
         objWin.close();
     }

 	function fnSystemAgree() {
 		gfnOpenLayerPopup('/common/systemAgree.do');
 	}
	
	//메인팝업
	function doPopup(){
		$.ajax({  
			 type : "POST"
			,url  : "/admin/System/mainPopupList.do"
			,dataType : "json"
			,success : function(transport){
				fncPopupBuild(transport.mainPopupList);
			}
		});
	}

	function fncPopupBuild(dataList){
		var resultHtml = "";
		if(dataList == "" || dataList == null ){			
		}else{
			var leftSize = 100;
			$.each(dataList,function(idx,json){
				var row =  ""; 
				row +='<div class="layerPopup" id="layer_'+idx+'" style="top:150px; left:'+leftSize +'px; width:'+json.WIDTH+'px; height:auto; z-index:998;">';
				row +='<div>';
				row +=json.CONTENTS;
				row +='</div>';
				row +='<dl>';
				row +='<dt onclick="popClose('+idx+',\'Y\');">오늘 더 이상 열지 않기</dt>';
				row +='<dd onclick="popClose('+idx+',\'N\');">닫기</dd>';
				row +='</dl>';
				row +='</div>';
				
				if(getcookie("ippPop"+idx)==null){
					resultHtml += row;
					leftSize += parseInt(json.WIDTH)+50;
				}				
			});			
			$("#mainPopup").html(resultHtml);
		}
	}

	function getcookie(cname){
		a=document.cookie.split("; ");
		for (var i=0; i<a.length; i++){
		  aa=a[i].split("=");
		  if (cname==aa[0] && aa[1]!=null){
			  return unescape(aa[1]);
		  }
		}
		return  null
	}

	function popClose(idx,flag){
		if(flag == "Y"){
			exday=new Date();
			exday.setDate(exday.getDate()+1);
			document.cookie="ippPop"+idx+"="+escape("yes")+"; expires="+exday.toGMTString()+"; ";
		}
		$("#layer_"+idx).hide();
	}
	
	//오직 숫자만 입력받고, 3자리마다 콤마를 붙여 현재 obj에 value를 세팅(input text에 금액을 입력받을 때)
	function setCommaOnlyNumber(obj) { 
		var x;
	  	x = obj.value.replace(/[^0-9]/g,'');   // 입력값이 숫자가 아니면 공백
	  	x = x.replace(/,/g,'');          // ,값 공백처리
	  	obj.value =  x.replace(/\B(?=(\d{3})+(?!\d))/g, ","); // 정규식을 이용해서 3자리 마다 , 추가 
	}
	
	
	function allCheckInLayer(obj,layerId) {
		$('#'+layerId).find("input[type=checkbox]").attr("checked",checkCheckbox(obj));
	}
	
