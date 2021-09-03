package apt.framework.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.web.servlet.view.document.AbstractExcelView;


public class ExcelView extends AbstractExcelView{
	
	  protected void buildExcelDocument(Map map, HSSFWorkbook workbook, HttpServletRequest request ,HttpServletResponse response){
		//map  ?ïà?óê?Ñú Î¶¨Ïä§?ä∏?ì† ?ç∞?ù¥?Ñ∞?ùò Í∞ùÏ≤¥?ì†?ùÑ Í∞??†∏?ò§Í≤? ?êú?ã§
		//?ù¥?õÑ workbook?ùÑ ÎßåÎì†?ã§ Ï¶? Ï∂úÎ†•?ê† excel ?åå?ùº?ùÑ ÎßåÎì§Í∏∞Îßå ?ïòÎ©? ?êú?ã§
		        try {
		          Map resource = (Map) map.get("excel_Resource");
		          
		          String fileName = (String)resource.get("fileName")+".xls"; // ?óë?? ?åå?ùº?ù¥Î¶?
				  fileName = new String(fileName.getBytes("euc-kr"), "8859_1"); 
				  response.setHeader("Content-Disposition", "attachment; fileName=\"" + fileName + "\";");
				  response.setHeader("Content-Transfer-Encoding", "binary");
				  
		          workbook = setExcel(resource ,workbook); //?óë?? ?Éù?Ñ±
		                     //write the workbook to the output stream

		        } catch (Exception e) {
		         	 e.printStackTrace();            
		    }       
	  }
	  
	private HSSFWorkbook setExcel(Map resource ,HSSFWorkbook workbook) throws Exception {
		
		String sheetName= (String)resource.get("sheetName"); //?ãú?ä∏?ù¥Î¶?;
		String tbName= (String)resource.get("tbName"); //?Öå?ù¥Î∏îÏ†úÎ™?;
		String tbName2= (String)resource.get("tbName2"); //?Öå?ù¥Î∏îÏ†úÎ™?;
		String tbName3= (String)resource.get("tbName3"); //?Öå?ù¥Î∏îÏ†úÎ™?;
		int row_num = 0;	//?ùº?ù∏ Î≤àÌò∏
		
		//?óë?? ?åå?ùº ?Éù?Ñ±
		HSSFWorkbook wb = workbook;
		//?â¨?ä∏ Î∞? ?è∞?ä∏ Ïß??†ï
		HSSFSheet sht = wb.createSheet(sheetName);
		sht.setGridsPrinted(true);
		sht.setFitToPage(true);
		sht.setDisplayGuts(true);
		HSSFRow row = null;
		HSSFCell cell = null;
 
		//?â¨?ä∏ ?ù¥Î¶? Ï£ºÍ∏∞
		wb.setSheetName(0, sheetName);
	 
		//?†úÎ™? Ï§? ?Éù?Ñ±
		String[] col_nm	 = (String[]) resource.get("col_nm"); //Ïª¨ÎüºÎ™?
		String[] key_nm	 = (String[]) resource.get("key_nm"); //keyÎ™?
		List contents = (List) resource.get("excelList"); //?ç∞?ù¥??
		
		//row 1 table start
		row = sht.createRow((short)0);
		row.setHeight((short)500);    //ÏπºÎüº ?Üí?ù¥
		short width = 5000;
	 
	  //========== title1 - first row  ========================
	 
	   //sht.setColumnWidth(1, (cellwidth[2] * width));    // Column ?Ñì?ù¥ ?Ñ§?†ï
	   
	   cell = row.createCell(row_num);
	   row_num++;
	    //====== Cell ?ï©Î≥? ==================  
	 
		sht.addMergedRegion(new CellRangeAddress(0,0,0,col_nm.length-1));
	 
	   //==================================
		 cell.setCellValue(new HSSFRichTextString(tbName));
		 cell.setCellStyle(getTitleStyle(wb));

		HSSFCellStyle textStyle = getTextStyle(wb);
		HSSFCellStyle titleStyle = getTitleStyle(wb);
			
		if(resource.containsKey("sub_excel_info")){
		
			HashMap sub_excel_info =  (HashMap) resource.get("sub_excel_info");
			int[] sub_excel_range = (int[])sub_excel_info.get("sub_excel_range");
			String[] sub_excel_col = (String[])sub_excel_info.get("sub_excel_col");
			String[] sub_excel_key = (String[])sub_excel_info.get("sub_excel_key");
			List<HashMap> sub_excel_list = (List<HashMap>)sub_excel_info.get("sub_excel_list");
			
			// ?ÑúÎ∏? ?óë?? ???ù¥?? ?Ñ§?†ï
		 	row = sht.createRow(row_num);
			row.setHeight((short)500);    //ÏπºÎüº ?Üí?ù¥
	
			int start_cell_num = 0;
			for(int i = 0 ; i < sub_excel_range.length ; i++){
				
				cell = row.createCell(start_cell_num);
				cell.setCellStyle(titleStyle);
				cell.setCellValue(new HSSFRichTextString(sub_excel_col[i]));
				
				//??Î≥ëÌï©?ù¥ ?ûà?äîÍ≤ΩÏö∞
				if(sub_excel_range[i] > 1){
					
					for(int y = 0; y < sub_excel_range.length ; y++){
						cell = row.createCell(start_cell_num+1);
						cell.setCellStyle(titleStyle);
					}
					
					sht.addMergedRegion(new CellRangeAddress(row_num,row_num,start_cell_num,(sub_excel_range[i] - 1) + start_cell_num));
					
				}
				
				start_cell_num = sub_excel_range[i] + start_cell_num;
			
			}
			row_num++;
			// ?ÑúÎ∏? ?óë?? ???ù¥?? ?Ñ§?†ï ?Åù

			// ?ÑúÎ∏? ?óò?? Î¶¨Ïä§?ä∏ ?Ñ§?†ï
		 	for(int y = 0 ; y < sub_excel_list.size() ; y++){
		 		
		 		HashMap sub_excel_val = sub_excel_list.get(y);
		 		row = sht.createRow(row_num);
		 		row.setHeight((short)500);    //ÏπºÎüº ?Üí?ù¥
		 		start_cell_num = 0;
		 		
				for(int i = 0 ; i < sub_excel_range.length ; i++){
					
					cell = row.createCell(start_cell_num);
					
					//??Î≥ëÌï©?ù¥ ?ûà?äîÍ≤ΩÏö∞
					if(sub_excel_range[i] > 1){
						sht.addMergedRegion(new CellRangeAddress(row_num,row_num,start_cell_num,(sub_excel_range[i] - 1) + start_cell_num));
					}
					cell.setCellValue(new HSSFRichTextString((String)sub_excel_val.get(sub_excel_key[i])));
					cell.setCellStyle(textStyle);
					
					start_cell_num = sub_excel_range[i] + start_cell_num;
				
				}
		 	}
		 	row_num++;
		 	
		}
		
	 // ===========title2 - Second row  ====================
	 
	row = sht.createRow(row_num);
	row_num++;
	row.setHeight((short)500);    //ÏπºÎüº ?Üí?ù¥
	
	 for(int i = 0;	 i < col_nm.length;	 i++){
		 sht.setColumnWidth(i, width);		
		 cell = row.createCell((i));
		 cell.setCellValue(new HSSFRichTextString(col_nm[i]));
		 cell.setCellStyle(titleStyle);
	 }
	  // ======================================================
	 
	// ===========title3 - Second row  ====================
	 
		  // ======================================================
	        
	 //===========  Table Contents   ===================
	 
	         DataMap resultMap = null;
	         if(contents != null && contents.size() >0){
	        	 for(int  i=0; i <contents.size(); i++ ){
           		  	row = sht.createRow(row_num+i);
           		  	row.setHeight((short)500);    //ÏπºÎüº ?Üí?ù¥
           		  	
	        		 resultMap = (DataMap)contents.get(i);
		               //sht.setColumnWidth(i, (cellwidth[i] * width)); //?? Í∞?Î°úÍ∏∏?ù¥
		               
		            	  for(int k=0; k<key_nm.length; k++){
		            		  cell = row.createCell((k));
		            		  cell.setCellValue(new HSSFRichTextString(resultMap.getString(key_nm[k])));
		            		  cell.setCellStyle(textStyle);
		              }
	        	 }
	         }
	
  //=====================================================  
       //Ï∂úÎ†•?Ñ§?†ï
	 
	       HSSFPrintSetup hps = sht.getPrintSetup();
	 
	       //?ö©Ïß??Ñ§?†ï
	 
	       hps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
	 
	       //Ï∂úÎ†•Î∞©Ìñ•?Ñ§?†ï
	 
	       hps.setLandscape(false);
	 
	       //Ï∂úÎ†•ÎπÑÏú®?Ñ§?†ï
	 
	       hps.setScale((short)100);
	 
	       //footer?óê ?éò?ù¥Ïß?Î≤àÌò∏ ?Ñ§?†ï
	 
	       HSSFFooter footer = sht.getFooter();
	 
	       footer.setCenter(HSSFFooter.page() + "/" + HSSFFooter.numPages() );

	
	
	        //?â¨?ä∏ ?ó¨Î∞? ?Ñ§?†ï
	 
	        sht.setMargin(HSSFSheet.TopMargin, 0.6);
	 
	        sht.setMargin(HSSFSheet.BottomMargin, 0.6);
	 
	        sht.setMargin(HSSFSheet.LeftMargin, 0.6);
	 
	        sht.setMargin(HSSFSheet.RightMargin, 0.6);
	
	        //Î∞òÎ≥µ?ñâ ?Ñ§?†ï
	 
	       wb.setRepeatingRowsAndColumns(0, 0, 3, 0, 0);
	       return wb;
	 
	    }
	 

	    	 /*@@@@@     Font ?Ñ§?†ï Method    @@@@@*/
	    
	 
	    private HSSFCellStyle getTitleStyle(HSSFWorkbook wb) {
	 
	    	 //?†úÎ™? ?è∞?ä∏
	 
	        HSSFFont hf = wb.createFont();
	 
	        hf.setFontHeightInPoints((short) 8);
	 
	        hf.setColor((short) HSSFColor.BLACK.index);
	 
	        hf.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	 
	
	
	
	        //Header style setting
	 
	        HSSFCellStyle hcs = wb.createCellStyle();
	 
	        hcs.setFont(hf);
	 
	        hcs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	 
	
	
	        //set border style
	 
	        hcs.setBorderBottom(HSSFCellStyle.BORDER_THICK);
	 
	        hcs.setBorderRight(HSSFCellStyle.BORDER_THIN);
	 
	        hcs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	 
	        hcs.setBorderTop(HSSFCellStyle.BORDER_THIN);
	 
	        hcs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	 
	
	        //set color
	 
	        hcs.setFillBackgroundColor((short) HSSFColor.WHITE.index );
	 
	        hcs.setFillForegroundColor((short) HSSFColor.GREY_25_PERCENT.index );
	 
	        hcs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	 
	        hcs.setLocked(true);
	 
	        hcs.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	
	        return hcs;
	 
	    }
	 
	    
	 
		/*@@@@@     Font ?Ñ§?†ï Method    @@@@@*/
	 
	    
	 
	    private HSSFCellStyle getTextStyle(HSSFWorkbook wb) {
	 
		   HSSFFont hf = wb.createFont();
	 
		   hf.setFontHeightInPoints((short) 8);
	 
		   hf.setColor((short) HSSFColor.BLACK.index);
	 
		   
	
	
		   HSSFCellStyle hcs = wb.createCellStyle();
	 
		   hcs.setFont(hf);
	 
		   hcs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	 
	
	
	
	            //set border style
	 
	       hcs.setBorderBottom(HSSFCellStyle.BORDER_THICK);
	 
		   hcs.setBorderRight(HSSFCellStyle.BORDER_THIN);
	 
		   hcs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	 
		   hcs.setBorderTop(HSSFCellStyle.BORDER_THIN);
	 
		   hcs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	 
		
	
		   //set color
	 
		   hcs.setFillBackgroundColor((short) HSSFColor.WHITE.index );
	 
		   hcs.setFillForegroundColor((short) HSSFColor.WHITE.index );
	 
		   hcs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	 
		
	
           hcs.setLocked(true);
	 
		   hcs.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	 
		   hcs.setWrapText(true); //Ï§ÑÎ∞îÍøàÏùÑ ?úÑ?ï¥ Ï∂îÍ? 
	 
		   return hcs;
	 
	    }
	  }		  
