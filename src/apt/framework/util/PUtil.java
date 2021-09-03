package apt.framework.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javapns.back.PushNotificationManager;
import javapns.back.SSLConnectionHelper;
import javapns.data.Device;
import javapns.data.PayLoad;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import apt.framework.common.DataMap;
import apt.framework.common.LoginSession;
import apt.framework.core.CommonFacade;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

public class PUtil {
	private static final String API_KEY= "AAAAryYMyO0:APA91bGUViloMHa2Cjrdp01fycUDs9Q5WT1SYopmIYGrde33La_eL1b5LgAWarvcKllh0NPEu6WEKs1omVsezBOiaIYFkWkczBipG3KY3TRGOWExdwN9gTy4uUh9otNQgdT8qoV-E6yV"; // ?��?��로이?�� API KEY
	private static final String IP_PROF_PATH = "D:\\webPro\\ipp_ut\\app\\ios\\semaforo_push_cert.p12"; // ?��증서?���?
	private static final String IP_PROF_PASS = "1111"; // ?��증서비�?번호
	private static Sender sender;
	private static com.google.android.gcm.server.Message msg;

	public static String getRandomKey() {
		return RandomStringUtils.randomAlphanumeric(20);
	}


	public static String getFloat(String str, String win_cnt) {
		String num = null;
		double temp = 0;

		temp = (Math.floor((Integer.parseInt(str) / Integer.parseInt(win_cnt)) * 100));
		num = getDeimalFormat(temp);

		return num;
	}

	public static String getDeimalFormat(int number) {
		return getDecimalFormat(number, ".##");
	}

	public static String getDeimalFormat(long number) {
		return getDecimalFormat(number, "###,###,###,###");
	}

	public static String getDeimalFormat(double number) {
		return getDecimalFormat(number, "###,###,###,###");
	}

	public static String getDecimalFormat(int number, String pattern) {
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		return decimalFormat.format(number);
	}

	public static String getDecimalFormat(float number, String pattern) {
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		return decimalFormat.format(number);
	}

	public static String getDecimalFormat(double number, String pattern) {
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		return decimalFormat.format(number);
	}

	public static boolean isNullTrimSpace(String str) {
		if (str == null || "".equals(str.trim()))
			return true;

		return false;
	}

	public static String getSimpleSizeFormat(long value) {
		String size = "";
		if ((1024 < value) && (value < 1024 * 1024))
			size = getDecimalFormat((float) value / 1024, "###,###.##") + " KB";
		else if (1024 * 1024 <= value)
			size = getDecimalFormat((float) value / (1024 * 1024), "###,###.##") + " MB";
		else if (1024 * 1024 * 1024 <= value)
			size = getDecimalFormat((float) value / (1024 * 1024), "###,###,###.##") + " GB";
		else
			size = getDecimalFormat(value, "###,###.##") + " Bytes";

		return size;
	}

	public static String getSimpleSizePercent(double voteCnt, String total) {
		String temp = null;
		NumberFormat pf = NumberFormat.getPercentInstance();

		double cnt = Double.parseDouble(total);
		temp = pf.format(voteCnt / cnt);

		return temp;
	}

	public static void htmlAlertPrintClose(HttpServletResponse res, String message) throws IOException {
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		out.println("<script type=\"text/javascript\">");
		out.println("alert('" + message + "');window.close();");
		out.println("</script>");
	}

	public static String parseStringByBytes(String raw, int len) {
		if (raw == null)
			return null;

		raw = removeTag(raw);
		String[] ary = null;
		String returnStr = "";

		try {
			
			byte[] rawBytes = raw.getBytes();
			int rawLength = rawBytes.length;

			int index = 0;
			int minus_byte_num = 0;
			int offset = 0;

			if (rawLength > len) {
				int aryLength = (rawLength / len) + (rawLength % len != 0 ? 1 : 0);
				ary = new String[aryLength];

				for (int i = 0; i < 1; i++) {
					minus_byte_num = 0;
					offset = len;
					if (index + offset > rawBytes.length) {
						offset = rawBytes.length - index;
					}
					for (int j = 0; j < offset; j++) {
						if ((rawBytes[index + j] & 0x80) != 0) {
							minus_byte_num++;
						}
					}
					if (minus_byte_num % 3 != 0) {
						offset -= minus_byte_num % 3;
					}
					ary[i] = new String(rawBytes, index, offset);
					index += offset;

				}
				returnStr = ary[0] + "...";
			} else {
				ary = new String[] { raw };
				returnStr = ary[0];
			}
		} catch (Exception e) {

		}

		return returnStr;
	}

	public static String getLimitChar(String string, int len) {
		if (len == 0)
			return string;
		else
			return getLimitChar(string, len, '+', "...");
	}

	public static String getLimitChar(String str, int length, char type, String tail) {
		if (length == 0)
			return str;

		if (str == null || str.length() == 0)
			return "";

		byte[] bytes = str.getBytes();
		int len = bytes.length;
		int counter = 0;

		if (length >= len)
			return str;

		StringBuffer sb = new StringBuffer();
		sb.append(str);
		for (int i = 0; i < length - len; i++) {
			sb.append(' ');
		}

		for (int i = length - 1; i >= 0; i--) {
			if ((bytes[i] & 0x80) != 0)
				counter++;
		}
		String f_str = null;
		if (type == '+') {
			f_str = new String(bytes, 0, length + (counter % 2));
		} else if (type == '-') {
			f_str = new String(bytes, 0, length - (counter % 2));
		} else {
			f_str = new String(bytes, 0, length - (counter % 2));
		}

		return f_str + tail;
	}

	public static String nullTrimReplace(String str) {
		return nullTrimReplace(str, "");
	}

	public static String nullTrimReplace(String str, String replace) {
		if (str == null || str.trim().length() == 0)
			return replace;

		return str;
	}

	public static String nullTrimReplace(Object str, String replace) {
		if (str == null)
			return replace;
		str = str.toString().trim();
		if (str.equals("")) {
			return replace;
		} else {
			return str.toString().trim();
		}
	}

	public static String nullTrimReplace(Object str) {
		if (str == null)
			return "";

		return str.toString().trim();
	}

	/*
	 * 주어진 길이보다 짧은 문자열의 앞부분을 특정 문자로 채워주는 메소드
	 *
	 * @ para : 문자열, 채울 문자, 문자열길이
	 */
	public static String fillChar(String str, char fill, int length) {
		StringBuffer fixed = null;
		int src_len = 0;
		if (str == null)
			return null;
		src_len = str.length();
		fixed = new StringBuffer(str);
		for (; ++src_len <= length;) {
			fixed.insert(0, fill);
		}

		return fixed.toString();
	}

	public static String fillChar(int digit, char fill, int length) {
		StringBuffer fixed = null;
		String str = Integer.toString(digit);
		int src_len = 0;
		if (str == null)
			return null;
		src_len = str.length();
		fixed = new StringBuffer(str);
		for (; ++src_len <= length;) {
			fixed.insert(0, fill);
		}

		return fixed.toString();
	}

	/*
	 * 주어진 문자열의 주어진 길이 다음 문자들을 특정 문자로 바꿔주는 메서드
	 *
	 * @ para : 문자열, 바꿀 문자, 주어진 길이 다음문자를 바꿈
	 */
	public static String changeNextChar(String str, char change_char, int length) {
		if (str == null)
			return null;
		int str_len = str.length();
		if (str_len <= length)
			return str;

		str = str.substring(0, length);
		StringBuffer strBuf = new StringBuffer(str);
		for (int i = 0; i < str_len; i++) {
			strBuf.append(change_char);
		}

		return strBuf.toString();
	}

	/**
	 * 숫자문자열을 xx,xxx 같은 형식의 콤마숫자열로 변환
	 *
	 */
	public static String toCommaDigit(String src) {
		int intsrc = 0;
		int len = 0;

		if (src == null)
			return src;
		StringBuffer cd = null;

		try {
			intsrc = Integer.parseInt(src);
		} catch (NumberFormatException e) {
			return null;
		}

		src = "" + intsrc;
		cd = new StringBuffer("" + intsrc);
		len = src.length();
		for (int i = 1; i <= (len - 1) / 3; ++i) {
			cd.insert(len - 3 * i, ',');
		}

		return cd.toString();
	}

	/**
	 * 숫자를 xx,xxx 같은 형식의 콤마숫자열로 변환
	 *
	 */
	public static String toCommaDigit(int src) {
		String srcstr = null;
		int len = 0;
		StringBuffer cd = null;

		srcstr = "" + Math.abs(src);

		cd = new StringBuffer(srcstr);
		len = srcstr.length();
		for (int i = 1; i <= (len - 1) / 3; ++i) {
			cd.insert(len - 3 * i, ',');
		}

		if (src < 0)
			cd.insert(0, '-');

		return cd.toString();
	}

	/**
	 * 숫자를 xx,xxx 같은 형식의 콤마숫자열로 변환
	 *
	 */
	public static String toCommaDigit(long src) {
		String srcstr = null;
		int len = 0;
		StringBuffer cd = null;

		srcstr = "" + Math.abs(src);

		cd = new StringBuffer(srcstr);
		len = srcstr.length();
		for (int i = 1; i <= (len - 1) / 3; ++i) {
			cd.insert(len - 3 * i, ',');
		}

		if (src < 0)
			cd.insert(0, '-');

		return cd.toString();
	}

	/************ HTML ******************/

	/**
	 * 줄바꿈문자 <br>
	 * 로
	 *
	 * @param str
	 * @return
	 */
	public static String cr2br(String str) {
		return StringUtils.replace(StringUtils.replace(str, "\n\r", "<br/>"), "\n", "<br/>");
	}

	public static String cr2br2(String str) {
		return StringUtils.replace(str, "\n", "");
	}

	public static String cr2br3(String str) {
		return StringUtils.replace(str, "&nbsp", "");
	}

	public static String escapeHtml(String string) {
		return StringEscapeUtils.escapeHtml(string);
	}

	public static String unEscapeHtml(String str) {
		return StringEscapeUtils.unescapeHtml(str);
	}

	/**
	 * "",'' 제거 (http://www.jakartaproject.com) kiho
	 *
	 * @param s
	 * @return
	 */
	public static String removeDoubleAndSingleQuote(String str) {
		if (str == null)
			return "";
		String aa = str.replace("\"", "");
		String bb = aa.replace("\'", "");

		return bb;
	}

	/**
	 * p제거 Auth : munho Date : 2011. 09. 08
	 *
	 * @param
	 * @param
	 * @return String
	 * @TODO : TODO
	 */
	public static String removeDoubleQuoteP(String str) {
		if (str == null)
			return "";
		String aa = str.replace("\"", "");
		String bb = aa.replace("\'", "");
		String cc = bb.replace("<p>", "");
		String dd = cc.replace("</p>", "");
		return dd;

	}

	/**
	 * 태그제거 (http://www.jakartaproject.com) 유니콘보드 유틸
	 *
	 * @param s
	 * @return
	 */
	public static String removeTag(String s) {
		if (s == null)
			return "";

		final int NORMAL_STATE = 0;
		final int TAG_STATE = 1;
		final int START_TAG_STATE = 2;
		final int END_TAG_STATE = 3;
		final int SINGLE_QUOT_STATE = 4;
		final int DOUBLE_QUOT_STATE = 5;
		int state = NORMAL_STATE;
		int oldState = NORMAL_STATE;
		char[] chars = s.toCharArray();
		StringBuffer sb = new StringBuffer();
		char a;
		for (int i = 0; i < chars.length; i++) {
			a = chars[i];
			switch (state) {
			case NORMAL_STATE:
				if (a == '<')
					state = TAG_STATE;
				else
					sb.append(a);
				break;
			case TAG_STATE:
				if (a == '>')
					state = NORMAL_STATE;
				else if (a == '\"') {
					oldState = state;
					state = DOUBLE_QUOT_STATE;
				} else if (a == '\'') {
					oldState = state;
					state = SINGLE_QUOT_STATE;
				} else if (a == '/')
					state = END_TAG_STATE;
				else if (a != ' ' && a != '\t' && a != '\n' && a != '\r' && a != '\f')
					state = START_TAG_STATE;
				break;
			case START_TAG_STATE:
			case END_TAG_STATE:
				if (a == '>')
					state = NORMAL_STATE;
				else if (a == '\"') {
					oldState = state;
					state = DOUBLE_QUOT_STATE;
				} else if (a == '\'') {
					oldState = state;
					state = SINGLE_QUOT_STATE;
				} else if (a == '\"')
					state = DOUBLE_QUOT_STATE;
				else if (a == '\'')
					state = SINGLE_QUOT_STATE;
				break;
			case DOUBLE_QUOT_STATE:
				if (a == '\"')
					state = oldState;
				break;
			case SINGLE_QUOT_STATE:
				if (a == '\'')
					state = oldState;
				break;
			}
		}

		return sb.toString();
	}

	/**
	 * 날짜관련
	 */
	public static String getDate(java.util.Date date, String format) {
		if (date == null || format == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
		return sdf.format(date);
	}

	public static String getFormatDate(String format) {
		return getFormatDate(new Date(), format);
	}

	public static String getFormatDate(java.util.Date date, String format) {
		return (new java.text.SimpleDateFormat(format)).format(date);
	}

	public static String getCurrentYear() {
		String currYear = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		Date currentTime = new Date();

		return currYear = formatter.format(currentTime);
	}

	public static String getCurrentMonth() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM");
		Date currentTime = new Date();

		return formatter.format(currentTime);
	}

	public static String getCurrentDay() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd");
		Date currentTime = new Date();
		return formatter.format(currentTime);
	}

	public static String getCurrentMonth01() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int field = calendar.getActualMinimum(Calendar.DATE);

		return getDate(new Date(), "yyyyMM") + (field < 10 ? "0" + field : String.valueOf(field));
	}

	public static String getCurrentMonth31() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		return getDate(new Date(), "yyyyMM") + calendar.getActualMaximum(Calendar.DATE);
	}

	public static String getCurrentDate(String format) {
		return getDate(new Date(), format);
	}

	public static String getCurrentDate() {
		return getDate(new Date(), "yyyyMMdd");
	}

	public static String getDate(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, day);

		return getDate(calendar.getTime(), "yyyyMMdd");
	}

	public static String getDate(int day, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, day);

		return getDate(calendar.getTime(), format);
	}

	public static int getLastDay(int intYear, int intMonth) {
		Calendar cal = new GregorianCalendar();
		cal.setLenient(false);
		cal.set(intYear, intMonth - 1, 1);

		return cal.getActualMaximum(GregorianCalendar.DATE);
	}

	public static int getLastDay(String yyyymm) {
		Calendar cal = new GregorianCalendar();
		cal.setLenient(false);
		cal.set(Integer.parseInt(yyyymm.substring(0, 4)), Integer.parseInt(yyyymm.substring(4)) - 1, 1);

		return cal.getActualMaximum(GregorianCalendar.DATE);
	}

	public static int getFirstDayOfWeek(String yyyymm) {
		Calendar cal = new GregorianCalendar();
		cal.setLenient(false);
		cal.set(Integer.parseInt(yyyymm.substring(0, 4)), Integer.parseInt(yyyymm.substring(4)) - 1, 1);

		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public static String addDay(String day, int inc) throws ParseException {
		Date today = new SimpleDateFormat("yyyyMMddHHmm").parse(day);
		Date addDay = new Date(today.getTime() + (inc * ((long) 1000 * 60 * 60 * 24)));

		String pattern = "yyyyMMddHHmm";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);

		String addDayString = sdf.format(addDay);

		return addDayString;
	}

	public static String addDay(String day, int inc, String pattern) throws ParseException {
		Date today = new SimpleDateFormat("yyyyMMdd").parse(day);
		Date addDay = new Date(today.getTime() + (inc * ((long) 1000 * 60 * 60 * 24)));

		if ("".equals(pattern)) {
			pattern = "yyyy/MM/dd";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);

		String addDayString = sdf.format(addDay);

		return addDayString;
	}

	public static int getMaximumDate(String date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREA);
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(sdf.parse(date));
			return calendar.getActualMaximum(Calendar.DATE);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public static String getForamtDate(String format) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

		return simpleDateFormat.format(new Date());
	}

	public static String getFormatStringDate(String yyyymmdd, String sep) {
		if (yyyymmdd == null || yyyymmdd.length() < 8)
			return "";

		return yyyymmdd.substring(0, 4) + sep + yyyymmdd.substring(4, 6) + sep + yyyymmdd.substring(6);
	}

	public static String getFormatStringDate1(String yyyymmddhhmiss, String sep) {
		if (yyyymmddhhmiss == null || yyyymmddhhmiss.length() < 8)
			return "";
		String c_sta_dt = yyyymmddhhmiss.substring(0, 8);

		return getFormatStringDate(c_sta_dt, sep);
	}

	public static String getFormatStringDate2(String yyyymmddhhmiss, String sep, String sep2) {
		if (yyyymmddhhmiss == null || yyyymmddhhmiss.length() < 14)
			return "";
		String c_sta_dt = yyyymmddhhmiss.substring(0, 14);

		return getFormatStringDate3(c_sta_dt, sep, sep2);
	}

	public static String getFormatStringDate3(String yyyymmdd, String sep, String sep2) {
		if (yyyymmdd == null || yyyymmdd.length() < 14)
			return "";

		return yyyymmdd.substring(0, 4) + sep + yyyymmdd.substring(4, 6) + sep + yyyymmdd.substring(6, 8) + "&nbsp;" + sep2 + yyyymmdd.substring(8, 10) + ":" + yyyymmdd.substring(10, 12) + ":"
				+ yyyymmdd.substring(12, 14) + sep2;
	}

	public static String getFormatStringDate4(String mmdd, String sep) {
		if (mmdd == null || mmdd.length() < 4)
			return "";

		return mmdd.substring(0, 2) + sep + mmdd.substring(2, 4);
	}

	// format ?��?��?�� ?���? 문자?��?�� 받아 format2?��?��?���? �??��
	public static String getFormatStringDate(String date, String format, String format2) throws Exception {
		Date dToday = new SimpleDateFormat(format).parse(date);

		return new SimpleDateFormat(format2).format(dToday);
	}


	public static String getGameTime6(String hhmmss, String sep) {
		if (hhmmss == null || hhmmss.length() < 6)
			return "";

		return hhmmss.substring(0, 2) + sep + hhmmss.substring(2, 4) + sep + hhmmss.substring(4, 6);
	}

	public static String getGameTime4(String hhmmss, String sep) {
		if (hhmmss == null || hhmmss.length() < 4)
			return "";

		return hhmmss.substring(0, 2) + sep + hhmmss.substring(2, 4);
	}


	public static String getMmdd(String hhmmss, String sep) {
		if (hhmmss == null || hhmmss.length() < 4)
			return "";

		return hhmmss.substring(0, 2) + sep + hhmmss.substring(2, 4);
	}

	public static String getMmdd2(String hhmmss) {
		if (hhmmss == null || hhmmss.length() < 4)
			return "";

		return hhmmss.substring(0, 2) + "?��" + hhmmss.substring(2, 4) + "?��";
	}

	public static HashMap getParameterMap(HttpServletRequest request) {
		return getParameterMap(request, 1);
	}

	private static HashMap<String, String> getParameterMap(HttpServletRequest request, int capiatlOption) {
		Enumeration<String> paramNames = request.getParameterNames();

		HashMap<String, String> paramMap = new HashMap<String, String>();

		while (paramNames.hasMoreElements()) {
			String name = paramNames.nextElement().toString();
			String value = StringUtils.defaultIfEmpty(request.getParameter(name), "");

			if (capiatlOption == 1)
				paramMap.put(name.toUpperCase(), value);
			if (capiatlOption == 2)
				paramMap.put(name.toLowerCase(), value);
			if (capiatlOption == 3)
				paramMap.put(name, value);
		}

		return paramMap;
	}

	/**
	 * getParameterDataMap과 동일하되 리턴 DataMap
	 *
	 * @param request
	 * @return
	 */
	public DataMap getParameterDataMap(HttpServletRequest request) {
		return getParameterDataMap(request, 1);
	}

	/**
	 * getParameterDataMap과 동일하되 리턴 DataMap
	 *
	 * @param request
	 * @param capiatlOption
	 * @return
	 */
	public static DataMap getParameterDataMap(HttpServletRequest request, int capiatlOption) {
		Enumeration<String> paramNames = request.getParameterNames();

		DataMap paramMap = new DataMap();
		LoginSession session = (LoginSession) request.getSession().getAttribute(new LoginSession().getSessionKey(request));
		try{
			while (paramNames.hasMoreElements()) {
				String name = paramNames.nextElement().toString();
				String value = request.getParameter(name);
				value = unscript(name,value);
	
				paramMap.put(name.toUpperCase(), value);
			}
			
			Map flashMap = RequestContextUtils.getInputFlashMap(request);
			
		        if(flashMap != null){
		        	Iterator it = paramMap.keySet().iterator();
		        	it = flashMap.keySet().iterator();
		        	while (it.hasNext()) {
		        		Object name =  it.next();
		                String value = (String)flashMap.get(name);
		                paramMap.put(name, value);
		                
		        	}
		        }
		      
	        String device = BrowserUtil.isMoblieBrowser(request.getHeader("User-Agent")) == true ? "M" : "P";
	        paramMap.put("ISMOBILE", device);
			if (session != null) {
				
				paramMap.put("SESSION_YN", "Y");
				paramMap.put("SESSION_USER_ID"		,		session.getSessionUsrId());			
				paramMap.put("SESSION_USER_NM"		,		session.getSessionUsrNm());			
				paramMap.put("SESSION_USER_NICK"	,		session.getSessionNick());			
				paramMap.put("SESSION_USER_EMAIL"	,		session.getSessionEmail());			
				paramMap.put("SESSION_USER_TYPE"	,		session.getSessionUserType());
				paramMap.put("SESSION_APT_CODE"		,		session.getSessionAptCode());
				paramMap.put("SESSION_USER_HP"		,		session.getSessionHp());
				paramMap.put("SESSION_RET_YN"		,		session.getSessionRetYn());
				paramMap.put("SESSION_ADMIN_YN"		,		session.getSessionAdminYn());
				paramMap.put("SESSION_AUTH"			,		session.getSessionAuthCd());			//권한	
				
				
				String usrType = session.getSessionUserType();
				
				String[] arrAuth= ((String) paramMap.get("SESSION_AUTH")).split(",");
				List<String> INQ_BBS = new ArrayList<String>(arrAuth.length);			
				if(!"".equals(paramMap.get("SESSION_AUTH"))){
					for(int cnt=0; cnt < arrAuth.length; cnt++) {
						INQ_BBS.add(arrAuth[cnt]); 
					}
				}
				if(usrType != null && !usrType.equals("")){
					paramMap.put("SESSION_AUTH_CD",	INQ_BBS);
					paramMap.put("SESSION_REG_ID",	paramMap.get("SESSION_USER_ID"));  
				}else{
				}
			} else {
				List<String> INQ_BBS = new ArrayList<String>(0);
				paramMap.put("SESSION_YN", "N");
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		// 결과 반환
		return paramMap;
	}

	public static boolean isValidEmail(String email) {
		Pattern p = Pattern.compile("[a-zA-Z]*[0-9]*@[a-zA-Z]*.[a-zA-Z]*");
		Matcher m = p.matcher(email);

		return m.matches();
	}

	public static boolean isValidMobileNum(String mobile) {
		if (mobile == null || !StringUtils.isNumeric(mobile) || mobile.length() < 10)
			return false;

		return true;
	}

	public static String MapToUrl(HashMap pmap, String pExceptKey, String pPrefix) {
		String result = "";

		try {
			if (pmap != null) {
				pExceptKey = "," + PUtil.nullTrimReplace(pExceptKey).toLowerCase();
				pPrefix = PUtil.nullTrimReplace(pPrefix);

				Iterator v_keies = pmap.keySet().iterator();

				while (v_keies.hasNext()) {
					String v_key = (String) v_keies.next();
					String v_val = URLEncoder.encode(PUtil.nullTrimReplace(pmap.get(v_key)), "utf-8");
					String v_key_low = v_key.toLowerCase();

					if (pExceptKey.indexOf("," + v_key_low) == -1 && !"".equals(v_val)) {
						if (!result.equals("")) {
							result = result + "&" + v_key + "=" + v_val;
						} else {
							result = v_key + "=" + v_val;
						}
					}
				}

				if (!result.equals("")) {
					result = pPrefix + result;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		return result;
	}

	public static String MapToUrl(HttpServletRequest req, String pExceptKey, String pPrefix) {
		return MapToUrl(PUtil.getParameterMap(req, 3), pExceptKey, pPrefix);
	}

	public static String MapToUrlDest(HashMap pmap, String pDestKey, String pPrefix) {
		String result = "";

		try {
			if (pmap != null) {
				pDestKey = "," + PUtil.nullTrimReplace(pDestKey).toLowerCase();
				pPrefix = PUtil.nullTrimReplace(pPrefix);

				Iterator v_keies = pmap.keySet().iterator();

				while (v_keies.hasNext()) {
					String v_key = (String) v_keies.next();
					String v_val = URLEncoder.encode(PUtil.nullTrimReplace(pmap.get(v_key)), "utf-8");
					String v_key_low = v_key.toLowerCase();

					if (pDestKey.indexOf("," + v_key_low) > -1 && !"".equals(v_val)) {
						if (!result.equals("")) {
							result = result + "&" + v_key + "=" + v_val;
						} else {
							result = v_key + "=" + v_val;
						}
					}
				}

				if (!result.equals("")) {
					result = pPrefix + result;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		return result;
	}

	public static String MapToUrlDest(HttpServletRequest req, String pDestKey, String pPrefix) {
		return MapToUrlDest(PUtil.getParameterMap(req, 3), pDestKey, pPrefix);
	}

	public static DataMap getNullCheckDataMap(Object dataMap) {
		DataMap resultDataMap = null;
		try {
			resultDataMap = dataMap != null ? (DataMap) dataMap : new DataMap();
		} catch (Exception e) {
			resultDataMap = new DataMap();
		}

		return resultDataMap;
	}

	public static List getNullCheckList(Object list) {
		List resultList = null;
		try {
			resultList = list != null ? (List) list : new Vector();
		} catch (Exception e) {
			resultList = new Vector();
		}

		return resultList;
	}

	public static HashMap getNullCheckHashMap(Object hashMap) {
		HashMap resultHashMap = null;
		try {
			resultHashMap = hashMap != null ? (HashMap) hashMap : new HashMap();
		} catch (Exception e) {
			resultHashMap = new DataMap();
		}

		return resultHashMap;
	}

	public static String stripHtmlTag(String tagStr) {
		return tagStr != null ? tagStr.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "") : "";
	}

	public static String reNameFilePng(String fileName, int widthNum) {
		String tmpFileName = StringUtils.defaultIfEmpty(fileName, ".");
		String firstFileInfo = tmpFileName.substring(0, tmpFileName.lastIndexOf(".") - 1);

		return !StringUtils.defaultIfEmpty(firstFileInfo, "").equals("") ? firstFileInfo + "_" + widthNum + ".png" : null;
	}

	public static String bcToString(Object p_val) {
		BigDecimal v_bigNum = p_val == null ? new BigDecimal(0) : (BigDecimal) p_val;

		return v_bigNum.toString();
	}

	public static String parseContent(String content) {
		content = content.replaceAll("\n", "<br>");
		content = content.replaceAll(" ", "&nbsp;");

		return content;
	}

	public static void sysout(String p_title, Object p_print) {
		sysout(p_title, p_print == null ? "null" : p_print.toString());
	}

	public static void sysout(String p_title, String p_print) {
		String nowTime = PUtil.getFormatDate(new java.util.Date(), "yyyy-MM-dd HH:mm:ss");

		System.out.println("\n\n\n\n\n\n");
		System.out.println("================" + p_title + "================");
		System.out.println(nowTime);
		System.out.println(p_print);
		System.out.println("\n\n\n\n\n\n");
	}

	public static String getParameters(HttpServletRequest req) {
		StringBuffer sb = new StringBuffer();
		Enumeration parmNames = req.getParameterNames();
		while (parmNames.hasMoreElements()) {
			String tmpParamName = (String) parmNames.nextElement();
			sb.append("&");
			sb.append(tmpParamName + "=" + req.getParameter(tmpParamName));
		}

		return sb.toString().length() > 1 ? sb.toString().substring(1) : "";
	}

	public static String getConfirmNoGenerator(int unitcnt, String type) {
		Random rnd = new Random();
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < unitcnt; i++) {
			if ("1".equals(type)) {
				sb.append((rnd.nextInt(10)));
			} else if ("2".equals(type)) {
				sb.append((char) ((rnd.nextInt(26)) + 65));
			} else {
				if (rnd.nextBoolean()) {
					sb.append((char) ((rnd.nextInt(26)) + 97));
				} else {
					sb.append((rnd.nextInt(10)));
				}
			}
		}

		return sb.toString().toUpperCase();
	}

	public static String getShortDateString() {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMddHHmmss", java.util.Locale.KOREA);

		return formatter.format(new java.util.Date());
	}


	public static String getTransDate(String str) {
		String result = "";
		int point = 0;

		if (str.length() == 4) {
			// 첫번�? 문자?��?�� 0?��?���? ex: 0413
			point = str.indexOf("0");

			if (point == 0) {
				result = str.substring(0, 2) + "/" + str.substring(2, 4);
			} else {
				result = str.substring(0, 2) + "/" + str.substring(2, 4);
			}

		}

		return result;
	}

	public static String getTransDate2(String str) {
		String result = "";
		int point = 0;

		if (str.length() == 4) {
			// 첫번�? 문자?��?�� 0?��?���? ex: 0413
			point = str.indexOf("0");

			if (point == 0) {
				result = str.substring(0, 2) + ":" + str.substring(2, 4);
			} else {
				result = str.substring(0, 2) + ":" + str.substring(2, 4);
			}

		}

		return result;
	}

	public static void expListToXls(String excelTitle, int[] commonExcelCellSize, String[] commonExcelTitleList, List<DataMap> commonDbDataList, String[] commonDBFieldList,
			HttpServletRequest request, HttpServletResponse response, CommonFacade commonFacade, ModelAndView mav) throws Exception {
		mav.addObject("commonExcelTitle", excelTitle);
		mav.addObject("commonExcelCellSize", commonExcelCellSize);
		mav.addObject("commonExcelTitleList", commonExcelTitleList);
		mav.addObject("commonDBFieldList", commonDBFieldList);
		mav.addObject("commonExcelList", commonDbDataList);
		mav.setViewName("/_anfdma/common/CommonListExcel");
	}


	public static String nvl(Object obj) {
		return nvl(obj, "");
	}

	public static String nvl(Object obj, String ifNull) {
		return ( obj != null && obj != "" ) ? obj.toString() : ifNull;
	}

	public static int nvl(Object obj, int ifNull) {
		return ( obj != null ) ? Integer.parseInt(obj.toString()) : ifNull;
	}

	public static HashMap getPageValue(HashMap dataMap) {

		int CURR_PAGE = 0;
		int PAGE_SIZE  = 0;

		if ((String)dataMap.get("CURR_PAGE") == null || "".equals(dataMap.get("CURR_PAGE")))  {
			CURR_PAGE=1;
		} else {
			CURR_PAGE = Integer.parseInt((String)dataMap.get("CURR_PAGE"));
		}

		if ((String)dataMap.get("PAGE_SIZE") == null || "".equals(dataMap.get("PAGE_SIZE")))  {
			PAGE_SIZE=10;
		} else {
			PAGE_SIZE = Integer.parseInt((String)dataMap.get("PAGE_SIZE"));
		}

		dataMap.put("PAGE_SIZE", PAGE_SIZE);
		dataMap.put("CURR_PAGE", CURR_PAGE);

		return dataMap;
	}

	public static String lpad(String str, int limit, String str2) {
		int num = limit-str.length();
		for(int i = 0; i < num; i++) {
			str2 += str2;
		}
		str = str2.substring(0, num) + str;

		return str;
	}


	public static void sendMail(DataMap dataMap) {
		try{
			String subject = dataMap.getString("SUBJECT");
			String text = dataMap.getString("CONT");
			Properties props = new Properties();

			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.host", MessageUtil.getMessage("MAIL.IP"));
			props.put("mail.smtp.port", MessageUtil.getMessage("MAIL.PORT"));
			props.put("mail.smtp.auth","false");
			Session s = Session.getInstance(props, null);
			Multipart mp = new MimeMultipart();
			MimeMessage message = new MimeMessage(s);
			String fromAddress = MessageUtil.getMessage("MAIL.FROM");		//보내?��?��
			InternetAddress from = new InternetAddress(fromAddress);
			message.setFrom(from); //보내?��?�� ?��?��
			String toAddress = dataMap.getString("TO");
			InternetAddress to = new InternetAddress(toAddress); // 받는?�� ?��?��
			message.addRecipient(Message.RecipientType.TO, to);
			message.setSubject(subject); // ?���?

			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setContent(text, "text/html; charset=UTF-8"); // content Type ?��?��
			mbp1.setHeader("Content-Transfer-Encoding","base64");
			mp.addBodyPart(mbp1);

			message.setContent(mp); // 본문

			System.out.println("SUBJECT : " + subject );
			System.out.println("CONT : " + text );
			System.out.println("TO : " + toAddress );
			System.out.println("FROM : " + fromAddress );

			Transport.send(message); // 메일 발송 */

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String valid(String arg, boolean require, String lengthType, int length, String objNm) {
		String returnMsg = "";

		if (require && isEmpty(arg))returnMsg += "MANDATORY|";

		if ("max".equals(lengthType) && getLength(arg) > length)returnMsg += length+"length Over|";
		if ("eq".equals(lengthType) && getLength(arg) != length)returnMsg += length+"length Not Equal|";
		if ("min".equals(lengthType) && getLength(arg) < length)returnMsg += length+"lnegth Under|";

		//if (!"".equals(returnMsg))returnMsg = objNm+":"+returnMsg+"<br/>";
		if (!"".equals(returnMsg))returnMsg = objNm+":"+returnMsg;

		return returnMsg;
	}

	public static int getLength(String arg) {
		int length = 0;

		if (arg != null && !"".equals(arg)) {
			length = arg.getBytes().length;
		}

		return length;
	}

	private static boolean isEmpty(String arg) {
		if (arg == null || "".equals(arg.trim())) {
			return true;
		} else {
			return false;
		}
	}

	public static Calendar getWeekth(String sDate) {
		String resultMap = null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);

		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		cal.add(Calendar.DAY_OF_MONTH, (-(dayOfWeek - 1)));

		for ( int i = 0; i < 6; i++ ) {
			if (i == 1) {

			} else {
				System.out.println(sdf.format(cal.getTime()));
				cal.add(Calendar.DAY_OF_MONTH, 1);
			}
		}

		return cal;
	}
	
	public static DataMap mapXY(String args,HttpServletRequest request,String type)
	  {
	    String lat = "";
	    String lon = "";
	    String murl_oldAddr = "";
	    String murl = "";
	    String mapxml = "";
	    String key="";
	    DataMap map = new DataMap();
	    map.put("RESULT", "Y");
	    if((request.getRequestURL().toString()).indexOf("ilinc.inje.ac.kr") > -1){
	    	key="cdef19d4e3409a7e627f910ab0a18585";
	    }else{
	    	key="9f1694dfe353ae42a2cde9a166bc6359";
	    }
	    
	    String[] addr = { 
	    		args };
	    try
	    {
	      for (int i = 0; i < addr.length; i++) {
	        lat = "";
	        lon = "";
	        mapxml = "";
	        System.out.println("주소 ?��?��/경도 �??�� : "+addr[i]);
	        
	        if(type.equals("new")){
	        	murl = "http://openapi.map.naver.com/api/geocode?key="+key+"&encoding=EUC-KR&coord=latlng&query=" + URLEncoder.encode(addr[i],"utf-8");
	        }else{
	        	murl = "http://openapi.map.naver.com/api/geocode.php?key="+key+"&encoding=EUC-KR&coord=latlng&query=" + URLEncoder.encode(addr[i],"utf-8");
	        }
	        System.out.println("url : "+murl);
	        
	        URL mapXmlUrl = new URL(murl);
	        HttpURLConnection urlConn = (HttpURLConnection)mapXmlUrl.openConnection();
	        urlConn.setDoOutput(true);
	        urlConn.setRequestMethod("POST");            
	        int len = urlConn.getContentLength();
	        if (len > 0) {
	          BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "EUC-KR"));
	          String inputLine = "";
	          while ((inputLine = br.readLine()) != null) {
	            mapxml = mapxml + inputLine;
	          }

	          if (mapxml != null) {
	            if (mapxml.indexOf("</item>") > -1) {
	              int first = 1;
	              lon = mapxml.substring(mapxml.indexOf("<x>") + 3, mapxml.indexOf("</x>"));
	              lat = mapxml.substring(mapxml.indexOf("<y>") + 3, mapxml.indexOf("</y>"));
	              System.out.println("X : "+lon);
	              System.out.println("Y : "+lat);
	              map.put("X", lon);
	              map.put("Y", lat);
	            } else {
	            	map.put("RESULT", "N");
	            }
	          }

	          br.close();
	        }else{
	        	map.put("RESULT", "N");
	        }
	      }
	      
	    } catch (Exception e) {
	      System.out.println(e.toString());
	    }
	    return map;
	  }
	
	public static void pushSendProcess(List<String> tokkenList, String message) {
		String title = "?��?��???���? LINC+?��?��?��";
		try {
			if(tokkenList.size() > 0){
				sender = new Sender(API_KEY);							//?��?�� 보내?�� 객체 ?��?��
				com.google.android.gcm.server.Message.Builder builder = new com.google.android.gcm.server.Message.Builder();	//?��?�� 메시�? 만드?�� 객체
				builder.addData("title", URLEncoder.encode(title,"EUC-KR"));
				builder.addData("url", "http://www.naver.com");
				builder.addData("message", URLEncoder.encode(message,"EUC-KR"));
				builder.addData("sound", "1");
				
				msg = builder.build();
				MulticastResult result = sender.send(msg,tokkenList,5);//?��?�� ?��?��
				int sucess = result.getSuccess();					//?��?��코드
				int total = result.getTotal();						//?��?�� 메시�? id
				long MulticastId  = result.getMulticastId();						//?��?�� 메시�? id
				System.out.println("SUCESS = "+sucess);
				System.out.println("TOTAL ="+total);
				System.out.println("MulticastId="+ MulticastId);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	public static void pushSendProcess_iPhone(List tokkenList, String message) throws Exception {
		PushNotificationManager pushManager = null;
		try {
			if(tokkenList.size() > 0){
				PayLoad payLoad = new PayLoad();
				payLoad.addAlert(message);
				//payLoad.addCustomDictionary("url", url);
				payLoad.addBadge(1);
				payLoad.addSound("default");
				
				pushManager =  PushNotificationManager.getInstance();
				DataMap target = new DataMap();
				for(int i=0; i< tokkenList.size(); i++){
					target = (DataMap)tokkenList.get(i);
					pushManager.addDevice("iPhone", target.getString("TOKKEN_ID"));
					Device client = pushManager.getDevice("iPhone");
					//Connection to APNs
					String host = "gateway.push.apple.com";
					int port =2195;
					pushManager.initializeConnection(host, port, IP_PROF_PATH, IP_PROF_PASS, SSLConnectionHelper.KEYSTORE_TYPE_PKCS12);
					//Send Push
					pushManager.sendNotification(client, payLoad);
					pushManager.stopConnection();
					pushManager.removeDevice("iPhone");
				}			
			}
			
		} catch (IOException e) {
			pushManager.removeDevice("iPhone");
			e.printStackTrace();
		}finally{
			pushManager.removeDevice("iPhone");
		}
	}
	
	protected static String unscript(String name,String ret) {
		String tempRet = "";
    	if (ret == null || ret.trim().equals("")) {
            return "";
        }
    	
    	tempRet = ret.toLowerCase().trim().replace("\"","'");
    	if(
    			(tempRet.trim().indexOf("');") > -1 && tempRet.trim().indexOf("('") > -1) 
    			|| tempRet.trim().indexOf("alert(") > -1
    			|| tempRet.trim().indexOf("%2") > -1
    	){
    		
    		return "";
    	}
    	
        
        ret = ret.replaceAll("<(S|s)(C|c)(R|r)(I|i)(P|p)(T|t)", "&lt;script");
        ret = ret.replaceAll("</(S|s)(C|c)(R|r)(I|i)(P|p)(T|t)", "&lt;/script");
        
        ret = ret.replaceAll("<(O|o)(B|b)(J|j)(E|e)(C|c)(T|t)", "&lt;object");
        ret = ret.replaceAll("</(O|o)(B|b)(J|j)(E|e)(C|c)(T|t)", "&lt;/object");
        
        ret = ret.replaceAll("<(A|a)(P|p)(P|p)(L|l)(E|e)(T|t)", "&lt;applet");
        ret = ret.replaceAll("</(A|a)(P|p)(P|p)(L|l)(E|e)(T|t)", "&lt;/applet");
        
        ret = ret.replaceAll("<(F|f)(O|o)(R|r)(M|m)", "&lt;form");
        ret = ret.replaceAll("</(F|f)(O|o)(R|r)(M|m)", "&lt;/form");
        
        ret = ret.replaceAll("<(E|e)(M|m)(B|b)(E|e)(D|d)", "&lt;embed");
        ret = ret.replaceAll("</(E|e)(M|m)(B|b)(E|e)(D|d)", "&lt;/embed");
                
        ret = ret.replaceAll("<(I|i)(F|f)(R|r)(A|a)(M|m)(E|e)", "&lt;iframe");
        ret = ret.replaceAll("</(I|i)(F|f)(R|r)(A|a)(M|m)(E|e)", "&lt;/iframe");
        
        ret = ret.replaceAll("<(F|f)(R|r)(A|a)(M|m)(E|e)", "&lt;frame");
        ret = ret.replaceAll("</(F|f)(R|r)(A|a)(M|m)(E|e)", "&lt;/frame");
        
        ret = ret.replaceAll("<(F|f)(R|r)(A|a)(M|m)(E|e)(S|s)(E|e)(T|t)", "&lt;frameset");
        ret = ret.replaceAll("</(F|f)(R|r)(A|a)(M|m)(E|e)(S|s)(E|e)(T|t)", "&lt;/frameset");
        
        ret = ret.replaceAll("<(M|m)(E|e)(T|t)(A|a)", "&lt;meta");
        ret = ret.replaceAll("</(M|m)(E|e)(T|t)(A|a)", "&lt;/meta");
        
        ret = ret.replaceAll("(O|o)(N|n)(A|a)(B|b)(O|o)(R|r)(T|t)", "_onabort");
        
        ret = ret.replaceAll("(O|o)(N|n)(C|c)(L|l)(I|i)(C|c)(K|k)", "_onclick");

        ret = ret.replaceAll("(O|o)(N|n)(D|d)(B|b)(C|c)(L|l)(I|i)(C|c)(K|k)", "_ondbclick");

        ret = ret.replaceAll("(O|o)(N|n)(D|d)(R|r)(A|a)(G|g)", "_ondrag");
        
        ret = ret.replaceAll("(O|o)(N|n)(E|e)(R|r)(R|r)(O|o)(R|r)", "_onerror");
        
        ret = ret.replaceAll("(O|o)(N|n)(F|f)(O|o)(C|c)(U|u)(S|s)", "_onfocus");
        
        ret = ret.replaceAll("(O|o)(N|n)(K|k)(E|e)(Y|y)(D|d)(O|o)(W|w)(N|n)", "_onkeydown");
        
        ret = ret.replaceAll("(O|o)(N|n)(K|k)(E|e)(Y|y)(P|p)(R|r)(E|e)(S|s)(S|s)", "_onkeypress");
        
        ret = ret.replaceAll("(O|o)(N|n)(K|k)(E|e)(Y|y)(U|u)(P|p)", "_onkeyup");
        
        ret = ret.replaceAll("(O|o)(N|n)(L|l)(O|o)(A|a)(D|d)", "_onload");
        
        ret = ret.replaceAll("(O|o)(N|n)(M|m)(O|o)(U|u)(S|s)(E|e)(D|d)(O|o)(W|w)(N|n)", "_onmousedown");
        
        ret = ret.replaceAll("(O|o)(N|n)(M|m)(O|o)(U|u)(S|s)(E|e)(M|m)(O|o)(V|v)(E|e)", "_onmousemove");
        
        ret = ret.replaceAll("(O|o)(N|n)(M|m)(O|o)(U|u)(S|s)(E|e)(O|o)(U|u)(T|t)", "_onmouseout");
        
        ret = ret.replaceAll("(O|o)(N|n)(M|m)(O|o)(U|u)(S|s)(E|e)(O|o)(V|v)(E|e)(R|r)", "_onmouseover");
        
        ret = ret.replaceAll("(O|o)(N|n)(M|m)(O|o)(U|u)(S|s)(E|e)(U|u)(P|p)", "_onmouseup");
        
        ret = ret.replaceAll("(O|o)(N|n)(M|m)(O|o)(V|v)(E|e)", "_onmove");
        
        ret = ret.replaceAll("(O|o)(N|n)(R|r)(E|e)(S|s)(E|e)(T|t)", "_onreset");
        
        ret = ret.replaceAll("(O|o)(N|n)(R|r)(E|e)(S|s)(I|i)(Z|z)(E|e)", "_onresize");
        
        ret = ret.replaceAll("(O|o)(N|n)(S|s)(E|e)(L|l)(E|e)(C|c)(T|t)", "_onselect");
        
        ret = ret.replaceAll("(O|o)(N|n)(S|s)(U|u)(B|b)(M|m)(I|i)(T|t)", "_onsubmit");
        
        ret = ret.replaceAll("(O|o)(N|n)(U|u)(N|n)(L|l)(O|o)(A|a)(D|d)", "_onunload");
        
        ret = ret.replaceAll("(O|o)(N|n)(B|b)(L|l)(U|u)(E|e)", "_onblue");
        
        ret = ret.replaceAll("(O|o)(N|n)(C|c)(H|h)(A|a)(N|n)(G|g)(E|e)", "_onchange");
        
        return ret;
    }
	
	// 카카오 API를 이용하여 주소로 좌표 알아내기
	public static DataMap addrToLocation(String addr){
    	
        String url = "https://dapi.kakao.com/v2/local/search/address.json?query="+addr;
        DataMap json = new DataMap();
        try{
            json = getJSONData(url);
        }catch(Exception e){
            
            e.printStackTrace();
        }
        return json;
    }
	
	private static DataMap getJSONData(String apiUrl) throws Exception {
        String jsonString = new String();
        StringBuffer docJson = new StringBuffer();
        String buf;
        String apikey = "fb991cb5bfba1dcd5ca145cae1847cbe"; //apikey
        DataMap returnMap = new DataMap();
        
        URL url = new URL(apiUrl);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        String auth = "KakaoAK "+apikey;
        conn.setRequestMethod("GET");
        conn.setRequestProperty("X-Requested-With", "curl");
        conn.setRequestProperty("Authorization", auth);
        
        BufferedReader br = new BufferedReader(new InputStreamReader(
                conn.getInputStream(), "UTF-8"));
        while ((buf = br.readLine()) != null) {
            jsonString += buf;
            docJson.append(buf);
        }
        
        if(0<docJson.toString().length()){
//            System.out.println("docJson    :"+docJson.toString());
            
        }
        
        JSONObject jsonObject = new JSONObject(docJson.toString());
        JSONArray jsonArray= (JSONArray) jsonObject.get("documents");
        JSONObject tempObj = (JSONObject) jsonArray.get(0);
            
		returnMap.put("X_LOCATION", tempObj.getDouble("y"));
		returnMap.put("Y_LOCATION", tempObj.getDouble("x"));
        return returnMap;
    }
}
