package apt.framework.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.time.DateUtils;
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



public class Utils {
	
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
		mav.setViewName("/shop/common/Error");
		mav.addObject("errCode",errCode);
		mav.addObject("errMsg",errMsg.replace("\n", "<br>"));
		return mav;
	}

	public static String convertHtmlcharsIn(String htmlstr)
	{
		String convert = new String();
		convert = replace2(htmlstr, "<script>", "");
		convert = replace2(convert, "</script>", "");
		//convert = replace2(convert, "<p>", "");
		//convert = replace2(convert, "</p>", "");
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
		convert = replace2(htmlstr, "&lt;", "<");
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
	
	
	/**
	 *  <br> 태그로 변환 
	 * Auth : yumi
	 * Date : 2011. 07. 29
	 * @param 
	 * @param 
	 * @return  String
	 * @TODO : TODO
	 */
	public static String br(String content){
	    if (content == null)
	      return "";
	    return content.replaceAll("\n", "<br/>");
	}
	
	public static String nullToString(String str)
	{		
		if(str == null)
		{
			str = "";
		}			
		else
		{				
			str = str;
		}		
		return str;
	}
	
	
	
	/**
	 * 게시물 제목 size만큼 자르기
	 * Auth : Lucy
	 * Date : 2011. 08. 04
	 * @param 
	 * @param 
	 * @return  String
	 * @TODO : TODO
	 */
	public static String TitleCut(String Title, int size ){
		String Titl="";
		if(Title.length()>size ){
			Titl=Title.substring(0,size)+"...";
			}
		else{
			Titl=Title;
			}
		
		return Titl;
		
	}

	/**
	 * ZIP 파일 만들기
	 */
	public static String getZipFile(List fileInfo, String fileName) {
		try {
			byte[] buffer = new byte[1024];
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName));
			ZipOutputStream zipOs = new ZipOutputStream(bos);
			zipOs.setLevel(0);
			for(int i = 0 ; i < fileInfo.size() ; i++){
				Map fileData = (Map)fileInfo.get(i);
				FileInputStream in = new FileInputStream((String)fileData.get("FILNM"));
				BufferedInputStream bis = new BufferedInputStream(in);

				zipOs.putNextEntry(new ZipEntry((String)fileData.get("FILNM")));
				int len;
				while((len = bis.read(buffer)) > 0){
					zipOs.write(buffer, 0, len);
				}
				zipOs.closeEntry();
				bis.close();
			}
			zipOs.close();
			bos.close();
		} catch(Exception e){
			e.printStackTrace();
		}

		return fileName;
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
	 * 엑셀파일 데이터를 Table형식으로 반환
	 * @param excelFilePath 엑셀파일 경로
	 * @return
	 */
	public static Hashtable getExcelTable(String excelFilePath, int cellCnt) throws Exception {
		Hashtable excelTable = new Hashtable();		
		String ext = excelFilePath.substring(excelFilePath.lastIndexOf(".") + 1);
		
		if ( "xls".equals(ext.toLowerCase()) ) {
			excelTable = getXLSExcelTable(excelFilePath, cellCnt);
		} else if ( "xlsx".equals(ext.toLowerCase()) ) {
			excelTable = getXLSXExcelTable(excelFilePath, cellCnt);
		}
		
		return excelTable;
	}
	
	/**
	 * 엑셀파일 데이터를 Table형식으로 반환
	 * @param excelFilePath 엑셀파일 경로
	 * @return
	 */
	public static Hashtable getExcelTableTest(String excelFilePath, int cellCnt, int rowCnt) throws Exception {
		Hashtable excelTable = new Hashtable();	
		String ext = excelFilePath.substring(excelFilePath.lastIndexOf(".") + 1);
		
		if ( "xls".equals(ext.toLowerCase()) ) {
			excelTable = getXLSExcelTableTest(excelFilePath, cellCnt, rowCnt);
		} else if ( "xlsx".equals(ext.toLowerCase()) ) {
			excelTable = getXLSXExcelTableTest(excelFilePath, cellCnt, rowCnt);
		}
		
		return excelTable;
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
	 * 엑셀파일(XLS)의 데이터를 Table형식으로 반환
	 * @param excelFilePath 엑셀파일 경로
	 * @return
	 */
	public static Hashtable getXLSXExcelTable(String excelFilePath, int cellCnt) throws Exception {
		Hashtable excelTable = new Hashtable();				
		List<List<String>> excelList = new ArrayList<List<String>>();
		
		//파일을 읽기위해 엑셀파일을 가져온다 
		FileInputStream fis = new FileInputStream(excelFilePath);
		System.out.println(excelFilePath);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		int rowindex = 0;
		int columnindex = 0;
		
		//시트 수 (첫번째에만 존재하므로 0을 준다)
		//만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
		XSSFSheet sheet = workbook.getSheetAt(0);
		String sheetName = sheet.getSheetName();		
		
		//행의 수
		int rows = sheet.getPhysicalNumberOfRows();
		//셀의 수
		int cells = 0;
		
		XSSFRow row = null;
		List<String> rowList = null;
		XSSFCell cell = null;
		String value = ""; 
		for ( rowindex = 0; rowindex < rows; rowindex++ ) {
		    //행을 읽는다
		    row = sheet.getRow(rowindex);
		    if ( row != null ) {
		    	rowList = new ArrayList<String>();
		    	
		    	if ( rowindex == 0 ) {  // 첫번째줄의 셀개수로 전체줄 데이터 가져오기.
			        //셀의 수
		    		if(cellCnt == 0){
			        	cells = row.getPhysicalNumberOfCells();
			        }else{
			        	cells = cellCnt;
			        }
		    	}
		    	
		        for ( columnindex = 0; columnindex < cells; columnindex++ ) {
		            //셀값을 읽는다
		            cell = row.getCell(columnindex);
		            value = "";
		            //셀이 빈값일경우를 위한 널체크
		            if ( cell != null ) {
		            	cell.setCellType(XSSFCell.CELL_TYPE_STRING);  // 셀을 String 타입으로 변경.
			            value = cell.getStringCellValue() + "";
		            }
		            rowList.add(value);
	            }
		        excelList.add(rowList);
	        }
		}		
		excelTable.put("sheetName", sheetName);
		excelTable.put("excelList" , excelList);
		
		return excelTable;
	}
	
	/**
	 * 엑셀파일(XLS)의 데이터를 Table형식으로 반환
	 * @param excelFilePath 엑셀파일 경로
	 * @return
	 */
	public static Hashtable getXLSExcelTable(String excelFilePath, int cellCnt) throws Exception {
		Hashtable excelTable = new Hashtable();		
		List<List<String>> excelList = new ArrayList<List<String>>();
		
		//파일을 읽기위해 엑셀파일을 가져온다 
		FileInputStream fis = new FileInputStream(excelFilePath);
		HSSFWorkbook workbook = new HSSFWorkbook(fis);
		int rowindex = 0;
		int columnindex = 0;
		
		//시트 수 (첫번째에만 존재하므로 0을 준다)
		//만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
		HSSFSheet sheet = workbook.getSheetAt(0);
		String sheetName = sheet.getSheetName();		
		
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
		    		if(cellCnt == 0){
			        	cells = row.getPhysicalNumberOfCells();
			        }else{
			        	cells = cellCnt;
			        }
		    	}
		    	
		        for ( columnindex = 0; columnindex < cells; columnindex++ ) {
		            //셀값을 읽는다
		            HSSFCell cell = row.getCell(columnindex);
		            String value = "";
		            //셀이 빈값일경우를 위한 널체크
		            if ( cell != null ) {
		            	cell.setCellType(HSSFCell.CELL_TYPE_STRING);  // 셀을 String 타입으로 변경.
			            value = cell.getStringCellValue() + "";
		            }
		            rowList.add(value);
	            }
		        excelList.add(rowList);
	        }
		}		
		excelTable.put("sheetName", sheetName);
		excelTable.put("excelList" , excelList);
		
		return excelTable;
	}
	
	/* ------------------------------------------------------------------------------------------------------------------------------------------------------ */
	
	/**
	 * 엑셀파일(XLS)의 데이터를 Table형식으로 반환
	 * @param excelFilePath 엑셀파일 경로
	 * @return
	 */
	public static Hashtable getXLSXExcelTableTest(String excelFilePath, int cellCnt, int rowCnt) throws Exception {
		Hashtable excelTable = new Hashtable();				
		List<List<String>> excelList = new ArrayList<List<String>>();
		
		//파일을 읽기위해 엑셀파일을 가져온다 
		FileInputStream fis = new FileInputStream(excelFilePath);
		System.out.println(excelFilePath);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		int rowindex = 0;
		int columnindex = 0;
		
		//시트 수 (첫번째에만 존재하므로 0을 준다)
		//만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
		XSSFSheet sheet = workbook.getSheetAt(0);
		String sheetName = sheet.getSheetName();		
		
		//행의 수
		int rows = rowCnt;
		//셀의 수
		int cells = 0;
		
		XSSFRow row = null;
		List<String> rowList = null;
		XSSFCell cell = null;
		String value = ""; 
		for ( rowindex = 0; rowindex < rows; rowindex++ ) {
		    //행을 읽는다
		    row = sheet.getRow(rowindex);
		    if ( row != null ) {
		    	rowList = new ArrayList<String>();
		    	
		    	if ( rowindex == 0 ) {  // 첫번째줄의 셀개수로 전체줄 데이터 가져오기.
			        //셀의 수
		    		if(cellCnt == 0){
			        	cells = row.getPhysicalNumberOfCells();
			        }else{
			        	cells = cellCnt;
			        }
		    	}
		    	
		        for ( columnindex = 0; columnindex < cells; columnindex++ ) {
		            //셀값을 읽는다
		            cell = row.getCell(columnindex);
		            value = "";
		            //셀이 빈값일경우를 위한 널체크
		            if ( cell != null ) {
		            	cell.setCellType(XSSFCell.CELL_TYPE_STRING);  // 셀을 String 타입으로 변경.
			            value = cell.getStringCellValue() + "";
		            }
		            rowList.add(value);
	            }
		        excelList.add(rowList);
	        }
		}		
		excelTable.put("sheetName", sheetName);
		excelTable.put("excelList" , excelList);
		
		return excelTable;
	}
	
	/**
	 * 엑셀파일(XLS)의 데이터를 Table형식으로 반환
	 * @param excelFilePath 엑셀파일 경로
	 * @return
	 */
	public static Hashtable getXLSExcelTableTest(String excelFilePath, int cellCnt, int rowCnt) throws Exception {
		Hashtable excelTable = new Hashtable();		
		List<List<String>> excelList = new ArrayList<List<String>>();
		
		//파일을 읽기위해 엑셀파일을 가져온다 
		FileInputStream fis = new FileInputStream(excelFilePath);
		HSSFWorkbook workbook = new HSSFWorkbook(fis);
		int rowindex = 0;
		int columnindex = 0;
		
		//시트 수 (첫번째에만 존재하므로 0을 준다)
		//만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
		HSSFSheet sheet = workbook.getSheetAt(0);
		String sheetName = sheet.getSheetName();		
		
		//행의 수
		int rows = rowCnt;
		//셀의 수
		int cells = 0;
		
		for ( rowindex = 0; rowindex < rows; rowindex++ ) {
		    //행을 읽는다
		    HSSFRow row = sheet.getRow(rowindex);
		    if ( row != null ) {
		    	List<String> rowList = new ArrayList<String>();
		    	
		    	if ( rowindex == 0 ) {  // 첫번째줄의 셀개수로 전체줄 데이터 가져오기.
			        //셀의 수
		    		if(cellCnt == 0){
			        	cells = row.getPhysicalNumberOfCells();
			        }else{
			        	cells = cellCnt;
			        }
		    	}
		    	
		        for ( columnindex = 0; columnindex < cells; columnindex++ ) {
		            //셀값을 읽는다
		            HSSFCell cell = row.getCell(columnindex);
		            String value = "";
		            //셀이 빈값일경우를 위한 널체크
		            if ( cell != null ) {
		            	cell.setCellType(HSSFCell.CELL_TYPE_STRING);  // 셀을 String 타입으로 변경.
			            value = cell.getStringCellValue() + "";
		            }
		            rowList.add(value);
	            }
		        excelList.add(rowList);
	        }
		}		
		excelTable.put("sheetName", sheetName);
		excelTable.put("excelList" , excelList);
		
		return excelTable;
	}
}
