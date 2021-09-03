package apt.framework.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.servlet.ModelAndView;
@SuppressWarnings("unchecked")
public class Utils {
	public static String writeXML(List list) {
		StringBuffer sb = new StringBuffer();
	
		sb.append("<rows>\r\n");
		Map  map;
		for(int i=0;i<list.size();i++){
			map = (Map)list.get(i);
			MapIterator mapit = new HashedMap(map).mapIterator();
			
			sb.append("<record>\r\n");
			try{
				while (mapit.hasNext()) {
					String key = mapit.next().toString();
					String value = "";
					if(mapit.getValue()!=null){
						value = mapit.getValue().toString();
					}
	
					sb.append("<"+key.toLowerCase()+">");
					sb.append(StringEscapeUtils.escapeXml(value)); 
					sb.append("</"+key.toLowerCase()+">");
				}
			}catch(Exception e){}
			sb.append("</record>\r\n");
		}
		sb.append("</rows>\r\n");
		
	    return sb.toString();
	}
	
	public static String writeJSON(List list){
		StringBuffer sb = new StringBuffer("[");
		String key = null;
		String value = null;
		try{
			Map  map;
			int size = list.size();
			for(int i=0;i<size;i++){
				sb.append("{");
				map = (Map)list.get(i);
				MapIterator mapit = new HashedMap(map).mapIterator();

				while (mapit.hasNext()) {
					key = mapit.next().toString().toLowerCase();
					value = "";
					if(mapit.getValue()!=null){
						value = mapit.getValue().toString() ;
					}
					
					sb.append(key).append(":");
					sb.append("\"").append(StringEscapeUtils.escapeJavaScript(value.replaceAll("\r\n", " "))).append("\"");
					
					if( mapit.hasNext() ){
						sb.append(",");
					}
				}						
				sb.append("},");
			}
			map = null;			
		}catch(Exception e){
			return "[]";
		}
		
		return StringUtils.removeEnd(sb.toString(),",")+"]" ;
	}
	
	
	public static int getErrcode(List list) {
		String value = "";
		Map  map;
		for(int i=0;i<list.size();i++){
			map = (Map)list.get(i);
			MapIterator mapit = new HashedMap(map).mapIterator();
			try{
				while (mapit.hasNext()) {
					String key = mapit.next().toString();

					if(key.equals("msgcode")){
						if(mapit.getValue()!=null){
							value = mapit.getValue().toString();
						}
					}
				}
			}catch(Exception e){}
		}
	    return new Integer(value);
	}

	public static int getIntReturnKey(List list, String inKey) {
		String value = "0";
		Map  map;
		for(int i=0;i<list.size();i++){
			map = (Map)list.get(i);
			MapIterator mapit = new HashedMap(map).mapIterator();
			try{
				while (mapit.hasNext()) {
					String key = mapit.next().toString();
					if(key.equals(inKey)){
						if(mapit.getValue()!=null){
							value = mapit.getValue().toString();
						}
					}
				}
			}catch(Exception e){}
		}
	    return new Integer(value);
	}

	public static String getStrReturnKey(List list, String inKey) {
		String value = "";
		Map  map;
		for(int i=0;i<list.size();i++){
			map = (Map)list.get(i);
			MapIterator mapit = new HashedMap(map).mapIterator();
			try{
				while (mapit.hasNext()) {
					String key = mapit.next().toString();
					if(key.equals(inKey)){
						if(mapit.getValue()!=null){
							value = mapit.getValue().toString();
						}
					}
				}
			}catch(Exception e){}
		}
	    return new String(value);
	}	
	
	public static ModelAndView errorHandler(HttpServletRequest request, String errMsg, String errCode) {
		
		ModelAndView mav = new ModelAndView("");				
		mav.setViewName("/common/Error");
		mav.addObject("errCode",errCode);
		mav.addObject("errMsg",errMsg.replace("\n", "<br>"));
		return mav;
	}
	
	
	public static String convertHtmlcharsIn(String htmlstr)
	{
		String convert = new String();
		convert = replace2(htmlstr, "<script>", "");
		convert = replace2(convert, "</script>", "");
		//convert = replace2(convert, "&nbsp;", " ");
		//convert = replace2(convert, "<", "&lt;");
		//convert = replace2(convert, ">", "&gt;");
		//convert = replace2(convert, "\"", "&quot;");
		//convert = replace2(convert, "&nbsp;", "&amp;nbsp;");
		return convert;
	}

	public static String convertHtmlcharsOut(String htmlstr)
	{
		String convert = new String();
		convert = replace2(htmlstr, "<p>", "");
		convert = replace2(convert, "</p>", "");
		convert = replace2(convert, "&nbsp;", " ");
		convert = replace2(convert, "&lt;", "<");
		convert = replace2(convert, "&gt;", ">");
		convert = replace2(convert, "&quot;", "\"");
		convert = replace2(convert, "&amp;nbsp;", "&nbsp;");
		return convert;
	}

	public static String replace2(String original, String oldstr, String newstr)
	{
		String convert = new String();
		int pos = 0;
		int begin = 0;
		pos = original.indexOf(oldstr);

		if(pos == -1)
			return original;

		while(pos != -1)
		{
			convert = convert + original.substring(begin, pos) + newstr;
			begin = pos + oldstr.length();
			pos = original.indexOf(oldstr, begin);
		}
		convert = convert + original.substring(begin);

		return convert;
	}

	public static int Dc_Rate(int dc_rate)
	{
		int dcRate = 0;
		
		if ( dc_rate == 0  ) dcRate = 0;
		else if ( dc_rate > 0 && dc_rate <= 5 ) dcRate = 5;
		else if ( dc_rate > 5 && dc_rate <= 10 ) dcRate = 10;
		else if ( dc_rate > 10 && dc_rate <= 15 ) dcRate = 15;
		else if ( dc_rate > 15 && dc_rate <= 20 ) dcRate = 20;
		else if ( dc_rate > 20 && dc_rate <= 25 ) dcRate = 25;
		else if ( dc_rate > 25 && dc_rate <= 30 ) dcRate = 30;
		else if ( dc_rate > 30 && dc_rate <= 35 ) dcRate = 35;
		else if ( dc_rate > 35 && dc_rate <= 40 ) dcRate = 40;
		else if ( dc_rate > 40 && dc_rate <= 45 ) dcRate = 45;
		else if ( dc_rate > 45 && dc_rate <= 50 ) dcRate = 50;
		else if ( dc_rate > 50 && dc_rate <= 55 ) dcRate = 55;
		else if ( dc_rate > 55 && dc_rate <= 60 ) dcRate = 60;
		else if ( dc_rate > 60 && dc_rate <= 65 ) dcRate = 65;
		else if ( dc_rate > 65 && dc_rate <= 70 ) dcRate = 70;
		else if ( dc_rate > 70 && dc_rate <= 75 ) dcRate = 75;
		else if ( dc_rate > 75 && dc_rate <= 80 ) dcRate = 80;
		else if ( dc_rate > 80 && dc_rate <= 85 ) dcRate = 85;
		else if ( dc_rate > 85 && dc_rate <= 90 ) dcRate = 90;
		else if ( dc_rate > 90 && dc_rate <= 95 ) dcRate = 95;
		else if ( dc_rate > 95 && dc_rate <= 100 ) dcRate = 100;

		return dcRate;
	}

	/**
	 * 엑셀파일 데이터를 List형식으로 반환
	 * @param excelFilePath 엑셀파일 경로
	 * @return
	 */
	public static List<List<String>> getExcelData(String excelFilePath) throws Exception {
		List<List<String>> excelList = new ArrayList<List<String>>();
		
		String ext = excelFilePath.substring(excelFilePath.lastIndexOf(".") + 1);
		
		if ( "xls".equals(ext.toLowerCase()) ) {
			excelList = getXLSExcelData(excelFilePath);
		} else if ( "xlsx".equals(ext.toLowerCase()) ) {
			excelList = getXLSXExcelData(excelFilePath);
		}
		
		return excelList;
	}
	
	/**
	 * 엑셀파일(XLS)의 데이터를 List형식으로 반환
	 * @param excelFilePath 엑셀파일 경로
	 * @return
	 */
	public static List<List<String>> getXLSExcelData(String excelFilePath) throws Exception {
		List<List<String>> excelList = new ArrayList<List<String>>();
		
		//파일을 읽기위해 엑셀파일을 가져온다 
		FileInputStream fis = new FileInputStream(excelFilePath);
		HSSFWorkbook workbook = new HSSFWorkbook(fis);
		int rowindex = 0;
		int columnindex = 0;
		//시트 수 (첫번째에만 존재하므로 0을 준다)
		//만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
		HSSFSheet sheet = workbook.getSheetAt(0);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		//행의 수
		int rows = sheet.getPhysicalNumberOfRows();
		//셀의 수
		int cells = 0;
		
		for ( rowindex = 0; rowindex < rows; rowindex++ ) {
		    //행을 읽는다
		    HSSFRow row = sheet.getRow(rowindex);
		    if ( row != null ) {
		    	List<String> rowList = new ArrayList<String>();
		    	
		    	if ( rowindex == 0 ) {  // 첫번째줄의 셀개수로 전체줄 데이터 가져오기.
			        //셀의 수
			        cells = row.getPhysicalNumberOfCells();
		    	}
		    	
		        for ( columnindex = 0; columnindex < cells; columnindex++ ) {
		            //셀값을 읽는다
		            HSSFCell cell = row.getCell(columnindex);
		            String value = "";
		            //셀이 빈값일경우를 위한 널체크
		            if ( cell != null ) {
			            if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
		            		Double numericCellValue = cell.getNumericCellValue();
							if(Math.floor(numericCellValue) == numericCellValue){ // 소수점 이하를 버린 값이 원래의 값과 같다면,,
								value = numericCellValue.intValue() + ""; // int형으로 소수점 이하 버리고 String으로 데이터 담는다.
							}else{
								value = numericCellValue + "";
							}
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								value = formatter.format(cell.getDateCellValue());
							}
		            	}else{
			            	cell.setCellType(XSSFCell.CELL_TYPE_STRING);  // 셀을 String 타입으로 변경.
				            value = cell.getStringCellValue() + "";
		            	}
			            
		            }
		            rowList.add(value);
	            }
		        excelList.add(rowList);
	        }
		}
		
		return excelList;
	}
	
	/**
	 * 엑셀파일(XLSX)의 데이터를 List형식으로 반환
	 * @param excelFilePath 엑셀파일 경로
	 * @param cellCnt 전체셀을 읽을 경우 0
	 * @return
	 */
	public static List<List<String>> getXLSXExcelData(String excelFilePath) throws Exception {
		List<List<String>> excelList = new ArrayList<List<String>>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		//파일을 읽기위해 엑셀파일을 가져온다 
		FileInputStream fis = new FileInputStream(excelFilePath);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		int rowindex = 0;
		int columnindex = 0;
		//시트 수 (첫번째에만 존재하므로 0을 준다)
		//만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
		XSSFSheet sheet = workbook.getSheetAt(0);
		//행의 수
		int rows = sheet.getPhysicalNumberOfRows();
		//셀의 수
		int cells = 0;
		
		for ( rowindex = 0; rowindex < rows; rowindex++ ) {
		    //행을 읽는다
			XSSFRow row = sheet.getRow(rowindex);
		    if ( row != null ) {
		    	List<String> rowList = new ArrayList<String>();
		    	
		    	if ( rowindex == 0 ) {  // 첫번째줄의 셀개수로 전체줄 데이터 가져오기.
			        //셀의 수
			        cells = row.getPhysicalNumberOfCells();
		    	}
		    	
		        for ( columnindex = 0; columnindex < cells; columnindex++ ) {
		            //셀값을 읽는다
		        	XSSFCell cell = row.getCell(columnindex);
		            String value = "";
		            //셀이 빈값일경우를 위한 널체크
		            if ( cell != null ) {
		            	if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
		            		Double numericCellValue = cell.getNumericCellValue();
							if(Math.floor(numericCellValue) == numericCellValue){ // 소수점 이하를 버린 값이 원래의 값과 같다면,,
								value = numericCellValue.intValue() + ""; // int형으로 소수점 이하 버리고 String으로 데이터 담는다.
							}else{
								value = numericCellValue + "";
							}
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								value = formatter.format(cell.getDateCellValue());
							}
		            	}else{
			            	cell.setCellType(XSSFCell.CELL_TYPE_STRING);  // 셀을 String 타입으로 변경.
				            value = cell.getStringCellValue() + "";
		            	}
		            }
		            rowList.add(value);
	            }
		        excelList.add(rowList);
	        }
		}
		
		return excelList;
	}
	
	
	public static String RandomVal(){
		
		Random random = new Random();
	
        int max_length = 1; 
        String random_value = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789�??��?��?��마바?��?��?��차카???��?��"; 

        StringBuffer strbuff = new StringBuffer(); 

        int rand = 0; 
        for (int j = 0; j < max_length; j++) { 
            rand = random.nextInt(40); 
            strbuff.append(random_value.charAt(rand)); 
        } 

        String text = strbuff.toString();		
		return text;
    } 

	
	public static void makeDir(String path ) {
		try { 
			org.apache.commons.io.FileUtils.forceMkdir(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void makeDir(File f ) {
		try {
			org.apache.commons.io.FileUtils.forceMkdir(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void deleteOldFiles(String saveDir ){
	    Iterator it = FileUtils.iterateFiles(new File(saveDir), null, false);
	    String strFilename;
	    while (it.hasNext()){
	        strFilename = it.next().toString();
	        boolean isOld = FileUtils.isFileOlder(new File(strFilename),System.currentTimeMillis()-3600000);
	        if(isOld){
	        	try {
					FileUtils.forceDelete(new File(strFilename));
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
	    }
	}
	public static HashMap<String, String> getParameterMap(HttpServletRequest request, int capiatlOption) { 
        // ?��?��미터 ?���? 
        Enumeration<String> paramNames = request.getParameterNames(); 

        // ???��?�� �? 
        HashMap<String, String> paramMap = new HashMap<String, String>(); 

       // 맵에 ???�� 
       while (paramNames.hasMoreElements()) { 
           String name = paramNames.nextElement().toString(); 
           String value = StringUtils.defaultIfEmpty(request.getParameter(name), ""); 
  
           if (capiatlOption == 1) paramMap.put(name.toUpperCase(), value); 
           if (capiatlOption == 2) paramMap.put(name.toLowerCase(), value); 
           if (capiatlOption == 3) paramMap.put(name, value); 
       } 

       // 결과 반환 
       return paramMap; 
   }
	
	/**
	 * CLOB ???�� �??��?���? 
	 * Auth : yumi
	 * Date : 2011. 08. 18
	 * @param 
	 * @param 
	 * @return  String
	 * @TODO : TODO
	 */
	 public static String getStringFromCLOB(java.sql.Clob clob) { 
	        StringBuffer sbf = new StringBuffer(); 
	        java.io.Reader br = null; 
	        char[] buf = new char[1024]; 
	        int readcnt; 
	  
	        try { 
	            br = clob.getCharacterStream(); 
	            while ((readcnt=br.read(buf,0,1024))!=-1) { 
	                sbf.append(buf,0,readcnt); 
	            } 
	        } catch (Exception e) { 
	            //logger.error("Failed to create String object from CLOB", e); 
	        }finally{ 
	            if(br!=null) 
	                try { 
	                    br.close(); 
	                } catch (IOException e) { 
	                    //logger.error("Failed to close BufferedReader object", e); 
	               } 
	        } 
	        return sbf.toString(); 
	    } 
	


}
