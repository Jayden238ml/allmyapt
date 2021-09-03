package apt.framework.common;



import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import apt.framework.core.CommonFacade;
import apt.framework.util.MessageUtil;
import apt.framework.util.PUtil;
import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


public class CommonData {

	private CommonFacade commonFacade;
	private void setCommonFacade(CommonFacade commonFacade){
		if(this.commonFacade == null){
			this.commonFacade = commonFacade;
		}
	}
	public CommonData(){}

	public CommonData(CommonFacade commonFacade){
		setCommonFacade(commonFacade);
	}

	/**
	 * 파일 업로드
	 */
	public HashMap fileUpLoad( MultipartHttpServletRequest multipartRequest ,  String explainCd ) throws IllegalStateException, IOException{


		int flag  = 0; // 결과값 반환하는 변수
		Iterator fileNameIter = multipartRequest.getFileNames();

		File f	=	null;
		String finalFnm	= null;
		List fileList = new ArrayList();

		String  filePath	=null;
		String  extension	=null;
		String  fileName	=null;
		HashMap filelAddMap = new HashMap();
		while(fileNameIter.hasNext()) {

			MultipartFile uploadFile = multipartRequest.getFile((String)fileNameIter.next());

			if(uploadFile != null){


				String onriginFileName = uploadFile.getOriginalFilename().trim();

				String filePathTemp = "";		
				////////////// 파일 업로드 폴더 구조 -  adminUpload/ 게시판 코드에 따른 메뉴명 / 날짜..
				//해당 게시판 코드에 해당되는 업로드 폴더 구조명 리턴 받기
				String uploadFilePath = MessageUtil.getMessage("SYSTEM.FILE_FULL_PATH");

				if(onriginFileName != null && !onriginFileName.equals("")){
					 if("Y".equals(explainCd)){
						 uploadFilePath = MessageUtil.getMessage("STATIC.ROOT.PATH");
						 filePath  = uploadFilePath+ File.separator + "userUpload" + File.separator ;
					 }else{
						 filePath  = uploadFilePath+ File.separator  + File.separator + PUtil.getCurrentYear()+ File.separator + PUtil.getCurrentMonth()+ File.separator + PUtil.getCurrentDay();
					 }
					 filePathTemp = "/userUpload/"  + PUtil.getCurrentYear() + "/" + PUtil.getCurrentMonth() +"/" + PUtil.getCurrentDay() + "/";
					 extension = onriginFileName.substring(onriginFileName.lastIndexOf("."), onriginFileName.length());
					 fileName = String.valueOf(System.currentTimeMillis())+PUtil.getConfirmNoGenerator(5, "3");

					//지원하는 확장자 체크
					 if(".bmp,.csv,.docx,.gif,.hwp,.jpeg,.jpg,.pdf,.png,.pptx,.tif,.txt,.xlsx,.zip,.jrf,.eml,.avi".indexOf(extension.toLowerCase()) <= -1){
						 filelAddMap.put("EXT_ERROR", "Y");
						 return filelAddMap;
					 }
					 //지원하는 확장자 체크
					 if(".exe,.cab,.mis,.sh".indexOf(extension.toLowerCase()) > -1){
						 filelAddMap.put("EXT_ERROR", "Y");
						 return filelAddMap;
					 }
					// 파일을 지정한 위치에 upload
					f = new File(filePath);
					
					if(!f.exists()) {
						LogWriter.getLogger(getClass()).debug("writeBoardProc : 디렉토리 생성");
						f.mkdirs();         // 디렉토리 생성
						Runtime.getRuntime().exec("chmod -R 777 " + PUtil.getCurrentYear()); 
						Runtime.getRuntime().exec("chmod -R 777 " + PUtil.getCurrentMonth()); 
						Runtime.getRuntime().exec("chmod -R 777 " + PUtil.getCurrentDay());
					}

					if("Y".equals(explainCd)){
						finalFnm = filePath+File.separator+onriginFileName;
					}else{
						finalFnm = filePath+File.separator+fileName+extension;
					}
					uploadFile.transferTo(new File(finalFnm));


					filelAddMap.put("TRANS_FILE_NM", fileName+extension);
					filelAddMap.put("REAL_FILE_NM", onriginFileName);
					filelAddMap.put("FILE_SIZE",  Long.toString(uploadFile.getSize()));

					if("Y".equals(explainCd)){
						filelAddMap.put("FILE_PATH", "/userUpload/");
					}else{
						filelAddMap.put("FILE_PATH", filePathTemp);
					}
					filelAddMap.put("FILE_TYPE",  extension.replace(".", ""));
					
					//이미지파일일경우 썸네일 이미지 등록
					if(".bmp,.gif,.jpeg,.jpg,.png".indexOf(extension.toLowerCase()) > -1){
						 Thumbnails.of(filePath+File.separator+fileName+extension).size(224, 172).toFile(new File(filePath+File.separator+"s_"+fileName+extension));
					}
					
				}
			}
		}

		return filelAddMap;
	}
	
public List<HashMap> fileUpLoadMulti( MultipartHttpServletRequest multipartRequest ,  String explainCd ) throws IllegalStateException, IOException{

		
		int flag  = 0; // 결과�? 반환?��?�� �??��
		Iterator fileNameIter = multipartRequest.getFileNames();

		File f	=	null;
		String finalFnm	= null;
		List fileList = new ArrayList();

		String  filePath	=null;
		String  extension	=null;
		String  fileName	=null;
		DataMap filelAddMap = new DataMap();
		List<HashMap> fileAddMapList = new ArrayList<HashMap>();	//리턴?�� 리스?��
		
		
		while(fileNameIter.hasNext()) {

			MultipartFile uploadFile = multipartRequest.getFile((String)fileNameIter.next());

			if(uploadFile != null){

				long fileSize = uploadFile.getSize();				
				String onriginFileName = uploadFile.getOriginalFilename().trim();

				String filePathTemp = "";

				////////////// ?��?�� ?��로드 ?��?�� 구조 -  adminUpload/ 게시?�� 코드?�� ?���? 메뉴�? / ?���?..
				//?��?�� 게시?�� 코드?�� ?��?��?��?�� ?��로드 ?��?�� 구조�? 리턴 받기
				String uploadFilePath = MessageUtil.getMessage("SYSTEM.FILE_FULL_PATH");

				if(onriginFileName != null && !onriginFileName.equals("")){
					 if("Y".equals(explainCd)){
						 uploadFilePath = MessageUtil.getMessage("STATIC.ROOT.PATH");
						 filePath  = uploadFilePath+ File.separator + "userUpload" + File.separator ;
					 }else{
						 filePath  = uploadFilePath+ File.separator  + File.separator + PUtil.getCurrentYear()+ File.separator + PUtil.getCurrentMonth()+ File.separator + PUtil.getCurrentDay();
					 }
					 filePathTemp = "/userUpload/"  + PUtil.getCurrentYear() + "/" + PUtil.getCurrentMonth() +"/" + PUtil.getCurrentDay() + "/";
					 extension = onriginFileName.substring(onriginFileName.lastIndexOf("."), onriginFileName.length());
					 fileName = String.valueOf(System.currentTimeMillis())+PUtil.getConfirmNoGenerator(5, "3");

					 //�??��?��?�� ?��?��?�� 체크
					 /*if(".bmp,.csv,.docx,.gif,.hwp,.jpeg,.jpg,.pdf,.png,.pptx,.tif,.txt,.xlsx,.zip,.jrf,.eml,.avi".indexOf(extension.toLowerCase()) <= -1){
						 filelAddMap.put("EXT_ERROR", "Y");
						 return filelAddMap;
					 }*/
					 //�??��?��?�� ?��?��?�� 체크
					 if(".exe,.cab,.mis,.sh".indexOf(extension.toLowerCase()) > -1){
						 filelAddMap.put("EXT_ERROR", "Y");
						 return fileAddMapList;
					 }
					// ?��?��?�� �??��?�� ?��치에 upload
					f = new File(filePath);
					if(!f.exists()) {
						LogWriter.getLogger(getClass()).debug("writeBoardProc : ?��?��?���? ?��?��");
						f.mkdirs();         // ?��?��?���? ?��?��
					}

					if("Y".equals(explainCd)){
						finalFnm = filePath+File.separator+onriginFileName;
					}else{
						finalFnm = filePath+File.separator+fileName+extension;
					}
					uploadFile.transferTo(new File(finalFnm));


					filelAddMap.put("TRANS_FILE_NM", fileName+extension);
					filelAddMap.put("REAL_FILE_NM", onriginFileName);
					filelAddMap.put("FILE_SIZE", fileSize);
					if("Y".equals(explainCd)){
						filelAddMap.put("FILE_PATH", "/userUpload/");
					}else{
						filelAddMap.put("FILE_PATH", filePathTemp);
					}
					filelAddMap.put("FILE_TYPE",  extension.replace(".", ""));
					
					//리스?��?�� ?��?���?
					fileAddMapList.add(filelAddMap);
					
				}
			}
		}


		return fileAddMapList;
	}

	/**
	 * 그룹코드�? 공통코드�? 조회
	 * @param grpCd
	 * @return
	 */
	public List getComCodeList(String grpCd){
		DataMap dataMap = new DataMap();
		dataMap.put("GRP_CODE", grpCd);
		dataMap.put("procedureid", "Common.getComCd_List");
		return this.commonFacade.list(dataMap);
	}

	/**
	 * CBM차트�?�? ?��로그?�� target�? 조회
	 * @param tagSeq
	 * @return
	 */
	public List getJobCbmTarList(){
		DataMap dataMap = new DataMap();
		dataMap.put("procedureid", "Common.getJobCbmTar_List");
		return this.commonFacade.list(dataMap);
	}
	
	/**
	 * 첨�? ?��?�� 리스?�� 
	 * @param dataMap
	 * @return
	 */
	public List FileList(DataMap dataMap){
		
		List boardFileList = null;
		dataMap.put("procedureid", "Common.getboardFile_List");
		boardFileList = this.commonFacade.list(dataMap);
		return boardFileList; 
	}
	
	/**
	 * 첨�? ?��?�� 리스?�� 
	 * @param dataMap
	 * @return
	 */
	public List FileListPort(DataMap dataMap){
		
		List boardFileList = null;
		dataMap.put("procedureid", "Common.getPortFile_List");
		boardFileList = this.commonFacade.list(dataMap);
		
		return boardFileList; 
	}
	
	
	/**
	 * MultiCode �?�?
	 * @param tagSeq
	 * @return
	 */
	public DataMap recMultiMap(String multiCd, String[] multiVal, DataMap dataMap){
		
		System.out.println("######################### MultiCode ###########################");
		System.out.println("######################### 그룹코드: "+multiCd);
		System.out.println("###############################################################");
		System.out.println("######################### 그룹코드?�� ?��?�� 코드: "+Arrays.toString(multiVal));
		
		if(multiVal != null && multiVal.length > 0){
			dataMap.put("GB_CODE", multiCd);				
			for(int i=0; i<multiVal.length; i++){
    			dataMap.put("GB_VAL", multiVal[i]);
    			this.commonFacade.processInsertInt(dataMap);
    		}
    	}
		return dataMap;
	}
	
	/**
	 * ?��발시 ?���? ?��?��블에 ?��?��?��?�� 만큼 로우 ?��?��
	 * @param request
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public void insertRecode( DataMap dataMap ) throws IllegalStateException, IOException{
		
		//?��?��?��?�� ?���? 조회
		dataMap.put("procedureid", "Comp.getPlaceExeAppInfo");
		DataMap exeView = (DataMap)this.commonFacade.getObject(dataMap);
		
		exeView.put("ONE_ID", dataMap.getString("ONE_ID"));
		
		//?��?��기간?�� ?��?��?��?�� 만큼 로우 ?��?��
		int year1 = 0;
		int year2 = 0;
		int month1 = 0;
		int month2 = 0;
		int day1 = 0;
		int day2 = 0;
		String stDt = exeView.getString("EXE_ST_DT");
		String endDt = exeView.getString("EXE_END_DT");
		
		year1 = Integer.parseInt(stDt.substring(0, 4));
		month1 = Integer.parseInt(stDt.substring(4, 6)) - 1;
		day1 = Integer.parseInt(stDt.substring(6, 8));
		
		year2 = Integer.parseInt(endDt.substring(0, 4));
		month2 = Integer.parseInt(endDt.substring(4, 6)) - 1;
		day2 = Integer.parseInt(endDt.substring(6, 8));
		
		Calendar cal = Calendar.getInstance ( );
		cal.set ( year2,month2,day2 );// ?��?���? ?��?��. 
		Calendar cal2 = Calendar.getInstance ( );
		cal2.set ( year1, month1, day1 ); // 기�??���? ?��?��. month?�� 경우 ?��?��?��?��-1?�� ?��줍니?��.
		
		int count = 0;
		String m_week = "";
		while ( !cal2.after ( cal ) ){			
			SimpleDateFormat dFormat = new SimpleDateFormat ( "yyyy.MM.dd" );
			
			int day_of_week = cal2.get ( Calendar.DAY_OF_WEEK );
			if ( day_of_week == 1 ){m_week="001";exeView.put("WORK_YN", "0");}
			else if ( day_of_week == 2 ){m_week="002";exeView.put("WORK_YN", "");}
			else if ( day_of_week == 3 ){m_week="003";exeView.put("WORK_YN", "");}
			else if ( day_of_week == 4 ){m_week="004";exeView.put("WORK_YN", "");}
			else if ( day_of_week == 5 ){m_week="005";exeView.put("WORK_YN", "");}
			else if ( day_of_week == 6 ){m_week="006";exeView.put("WORK_YN", "");}
			else if ( day_of_week == 7 ){m_week="007";exeView.put("WORK_YN", "0");}

			exeView.put("RECORD_DT", dFormat.format(cal2.getTime()));
			exeView.put("RECORD_DT_WEEK", m_week);
			exeView.put("procedureid", "Comp.insertRecode");			
			this.commonFacade.processInsert(exeView);
			
			count++;cal2.add ( Calendar.DATE, 1 ); // ?��?��?���? 바�??
		}
	}	
	
	/**
	 * ?��발시 ?���? ?��?��블에 추�??�� ?��?��?��?�� 만큼 로우 ?��?��
	 * @param request
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public void insertAddRecode( DataMap dataMap ) throws IllegalStateException, IOException{
		
		//?��?��기간?�� ?��?��?��?�� 만큼 로우 ?��?��
		int year1 = 0;
		int year2 = 0;
		int month1 = 0;
		int month2 = 0;
		int day1 = 0;
		int day2 = 0;
		String stDt = dataMap.getString("EXE_ST_DT");
		String endDt = dataMap.getString("EXE_END_DT");
		
		year1 = Integer.parseInt(stDt.substring(0, 4));
		month1 = Integer.parseInt(stDt.substring(5, 7)) - 1;
		day1 = Integer.parseInt(stDt.substring(8, 10));
		
		year2 = Integer.parseInt(endDt.substring(0, 4));
		month2 = Integer.parseInt(endDt.substring(5, 7)) - 1;
		day2 = Integer.parseInt(endDt.substring(8, 10));
		
		Calendar cal = Calendar.getInstance ( );
		cal.set ( year2,month2,day2 );// 
		Calendar cal2 = Calendar.getInstance ( );
		cal2.set ( year1, month1, day1 ); 
	//	cal2.add(Calendar.DATE, 1); //?��?��?���??�� ?��록되�? ?��?��
		
		int count = 0;
		String m_week = "";
		while ( !cal2.after ( cal ) ){			
			SimpleDateFormat dFormat = new SimpleDateFormat ( "yyyy.MM.dd" );
			
			int day_of_week = cal2.get ( Calendar.DAY_OF_WEEK );
			if ( day_of_week == 1 ){m_week="001";dataMap.put("WORK_YN", "0");}
			else if ( day_of_week == 2 ){m_week="002";dataMap.put("WORK_YN", "");}
			else if ( day_of_week == 3 ){m_week="003";dataMap.put("WORK_YN", "");}
			else if ( day_of_week == 4 ){m_week="004";dataMap.put("WORK_YN", "");}
			else if ( day_of_week == 5 ){m_week="005";dataMap.put("WORK_YN", "");}
			else if ( day_of_week == 6 ){m_week="006";dataMap.put("WORK_YN", "");}
			else if ( day_of_week == 7 ){m_week="007";dataMap.put("WORK_YN", "0");}

			dataMap.put("RECORD_DT", dFormat.format(cal2.getTime()));
			dataMap.put("RECORD_DT_WEEK", m_week);
			dataMap.put("procedureid", "Comp.insertRecode");			
			this.commonFacade.processInsert(dataMap);
			
			
			
			
			count++;cal2.add ( Calendar.DATE, 1 ); // ?��?��?���? 바�??
			
			
			
		}
	}	
	
	/**
	 * ?��발시 ?���? ?��?��블에 추�??�� ?��?��?��?�� 만큼 로우 ?��?��
	 * @param request
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public void insertAddWeek( DataMap dataMap ) throws IllegalStateException, IOException{
		//?��?��?��?�� ?���? 조회
		dataMap.put("procedureid", "Comp.getPlaceExeAppInfo");
		DataMap exeView = new DataMap();
		
		if(dataMap.getString("PLACE_EXE_APP_SEQ").equals("")){
			exeView.put("EXE_ID", dataMap.getString("EXE_ID"));
			exeView.put("EXE_ST_DT", dataMap.getString("EXE_ST_DT").replace(".", ""));
			exeView.put("EXE_END_DT", dataMap.getString("EXE_END_DT").replace(".", ""));
			exeView.put("PLACE_EXE_SEQ", dataMap.getString("PLACE_EXE_SEQ"));
		}else{				
			exeView = (DataMap)this.commonFacade.getObject(dataMap);
		}
		
		exeView.put("ONE_ID", dataMap.getString("ONE_ID"));
		
		//?��?��기간?�� ?��?��?��?�� 만큼 로우 ?��?��
		int year1 = 0;
		int year2 = 0;
		int month1 = 0;
		int month2 = 0;
		int day1 = 0;
		int day2 = 0;
		String stDt = exeView.getString("EXE_ST_DT");
		String endDt = exeView.getString("EXE_END_DT");
		year1 = Integer.parseInt(stDt.substring(0, 4));
		month1 = Integer.parseInt(stDt.substring(4, 6)) - 1;
		day1 = Integer.parseInt(stDt.substring(6, 8));
		
		year2 = Integer.parseInt(endDt.substring(0, 4));
		month2 = Integer.parseInt(endDt.substring(4, 6)) - 1;
		day2 = Integer.parseInt(endDt.substring(6, 8));
		
		Calendar cal = Calendar.getInstance ( );
		cal.set ( year2,month2,day2 );// ?��?���? ?��?��. 
		Calendar cal2 = Calendar.getInstance ( );
		cal2.set ( year1, month1, day1 ); // 기�??���? ?��?��. month?�� 경우 ?��?��?��?��-1?�� ?��줍니?��.
		Calendar cal3 = Calendar.getInstance ( );
		cal3.set ( year2,month2,day2 );// ?��?���? ?��?��.
		
		int count = 0;
		int weekcnt = 0;
		String m_week = "";
		String week_st_dt = stDt;
		String week_ed_dt = "";
		while ( !cal2.after ( cal ) ){			
			SimpleDateFormat dFormat = new SimpleDateFormat ( "yyyy.MM.dd" );
			
			int day_of_week = cal2.get ( Calendar.DAY_OF_WEEK );
			if ( day_of_week == 1 )m_week="001";
			else if ( day_of_week == 2 ){m_week="002"; week_st_dt=dFormat.format(cal2.getTime());}
			else if ( day_of_week == 3 )m_week="003";
			else if ( day_of_week == 4 )m_week="004";
			else if ( day_of_week == 5 )m_week="005";
			else if ( day_of_week == 6 ){m_week="006";week_ed_dt=dFormat.format(cal2.getTime());}
			else if ( day_of_week == 7 )m_week="007";

			//금요?��?��경우 주간?��?���? ?���?
			if(day_of_week == 6){
				weekcnt++;
				exeView.put("WEEK_SEQ", weekcnt);	//주별?��컨스
				exeView.put("WEEK_ST_DT", week_st_dt);	//주간?��?��?��
				exeView.put("WEEK_ED_DT", week_ed_dt);	//주간종료?��
				exeView.put("procedureid", "Comp.insertWeek");			
				this.commonFacade.processInsert(exeView);
			}else{
				if(dFormat.format(cal3.getTime()).equals(dFormat.format(cal2.getTime()))){
					weekcnt++;
					exeView.put("WEEK_SEQ", weekcnt);	//주별?��컨스
					exeView.put("WEEK_ST_DT", week_st_dt);	//주간?��?��?��
					exeView.put("WEEK_ED_DT", dFormat.format(cal2.getTime()));	//주간종료?��
					exeView.put("procedureid", "Comp.insertWeek");			
					this.commonFacade.processInsert(exeView);
				}	
			}
			
			
			
			count++;cal2.add ( Calendar.DATE, 1 ); // ?��?��?���? 바�??
			
			
			
		}
	}	
	/**
	 * 미선발시 ?���?,주�? ?��?���? ?��?��
	 * @param request
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public void deleteRecode( DataMap dataMap ) throws IllegalStateException, IOException{
		
		//?��?��?��?�� ?���? 조회
		dataMap.put("procedureid", "Comp.getPlaceExeAppInfo");
		DataMap exeView = (DataMap)this.commonFacade.getObject(dataMap);

		exeView.put("procedureid", "Comp.deleteRecode");
		this.commonFacade.processDelete(exeView);			
		
		exeView.put("procedureid", "Comp.deleteWeek");
		this.commonFacade.processDelete(exeView);
	}
	
	//?��?��?�� ?��진올리기
		public HashMap getResumeFileUpLoad( MultipartHttpServletRequest multipartRequest , String destinationDir ) throws IllegalStateException, IOException{

			
			int flag  = 0; // 결과�? 반환?��?�� �??�� 
			Iterator fileNameIter = multipartRequest.getFileNames();
			
			File f	=	null;
			String finalFnm	= null;			
			List fileList = new ArrayList();
			
			String  filePath	=null;
			String  extension	=null;
			String  fileName	=null;
			HashMap filelAddMap = new HashMap();		
			
			while(fileNameIter.hasNext()) {
			
				MultipartFile uploadFile = multipartRequest.getFile((String)fileNameIter.next());			
				
				if(uploadFile != null){
					

					String onriginFileName = uploadFile.getOriginalFilename().trim();			
					System.out.println("?��?��?��로드 ?���? ="+onriginFileName);
					System.out.println("?��?��미터 ?���? ="+uploadFile.getName());
					String filePathTemp = "";

					////////////// ?��?�� ?��로드 ?��?�� 구조 -  adminUpload/ 게시?�� 코드?�� ?���? 메뉴�? / ?���?..
					//?��?�� 게시?�� 코드?�� ?��?��?��?�� ?��로드 ?��?�� 구조�? 리턴 받기 
					String uploadFilePath = MessageUtil.getMessage("SYSTEM.FILE_FULL_PATH");		
				
					if(onriginFileName != null && !onriginFileName.equals("")){
						 filePath  = uploadFilePath+ File.separator + destinationDir + File.separator + StringUtils.substring(PUtil.getShortDateString(),0,8);	
						 filePathTemp = "/userUpload/" + destinationDir + "/" + StringUtils.substring(PUtil.getShortDateString(),0,8) + "/";	
						 extension = onriginFileName.substring(onriginFileName.lastIndexOf("."), onriginFileName.length());
						 fileName = String.valueOf(System.currentTimeMillis())+PUtil.getConfirmNoGenerator(5, "3");
						
						// ?��?��?�� �??��?�� ?��치에 upload
						f = new File(filePath);		
						if(!f.exists()) {
							LogWriter.getLogger(getClass()).debug("writeBoardProc : ?��?��?���? ?��?��");
							f.mkdirs();         // ?��?��?���? ?��?��
						}
													
						finalFnm = filePath+File.separator+fileName+extension;
						uploadFile.transferTo(new File(finalFnm));
						
						filelAddMap.put("TRANS_FILE_NM", fileName+extension);
						filelAddMap.put("REAL_FILE_NM", onriginFileName);
						filelAddMap.put("FILE_PATH", filePathTemp);
						filelAddMap.put("FILE_TYPE",  extension.replace(".", ""));
						filelAddMap.put("FILE_SIZE",  Long.toString(uploadFile.getSize()));

					}
				}
			} 
			
			return filelAddMap;
		}
	
		//종합보고?�� 첨�??��?�� ?��?�� 2�? ?��리기
		public HashMap getReportFileUpLoad( MultipartHttpServletRequest multipartRequest , String destinationDir ) throws IllegalStateException, IOException{

			
			int flag  = 0; // 결과�? 반환?��?�� �??�� 
			Iterator fileNameIter = multipartRequest.getFileNames();
			
			File f	=	null;
			String finalFnm	= null;			
			List fileList = new ArrayList();
			
			String  filePath	=null;
			String  extension	=null;
			String  fileName	=null;
			HashMap filelAddMap = new HashMap();		
			
			while(fileNameIter.hasNext()) {
			
				MultipartFile uploadFile = multipartRequest.getFile((String)fileNameIter.next());			
				
				if(uploadFile != null){
					

					String onriginFileName = uploadFile.getOriginalFilename().trim();			
					System.out.println("?��?��?��로드 ?���? ="+onriginFileName);
					System.out.println("?��?��미터 ?���? ="+uploadFile.getName());
					String filePathTemp = "";

					////////////// ?��?�� ?��로드 ?��?�� 구조 -  adminUpload/ 게시?�� 코드?�� ?���? 메뉴�? / ?���?..
					//?��?�� 게시?�� 코드?�� ?��?��?��?�� ?��로드 ?��?�� 구조�? 리턴 받기 
					String uploadFilePath = MessageUtil.getMessage("SYSTEM.FILE_FULL_PATH");		
				
					if(onriginFileName != null && !onriginFileName.equals("")){
						 filePath  = uploadFilePath+ File.separator + destinationDir + File.separator + StringUtils.substring(PUtil.getShortDateString(),0,8);	
						 filePathTemp = "/userUpload/" + destinationDir + "/" + StringUtils.substring(PUtil.getShortDateString(),0,8) + "/";
						 extension = onriginFileName.substring(onriginFileName.lastIndexOf("."), onriginFileName.length());
						 fileName = String.valueOf(System.currentTimeMillis())+PUtil.getConfirmNoGenerator(5, "3");
						
						// ?��?��?�� �??��?�� ?��치에 upload
						f = new File(filePath);		
						if(!f.exists()) {
							LogWriter.getLogger(getClass()).debug("writeBoardProc : ?��?��?���? ?��?��");
							f.mkdirs();         // ?��?��?���? ?��?��
						}
													
						finalFnm = filePath+File.separator+fileName+extension;
						uploadFile.transferTo(new File(finalFnm));
						
						if(uploadFile.getName().equals("file1")){
						filelAddMap.put("TRANS_FILE_NM1", fileName+extension);
						filelAddMap.put("REAL_FILE_NM1", onriginFileName);
						filelAddMap.put("FILE_PATH1", filePathTemp);
						filelAddMap.put("FILE_TYPE1",  extension.replace(".", ""));
						}else{
						filelAddMap.put("TRANS_FILE_NM2", fileName+extension);
						filelAddMap.put("REAL_FILE_NM2", onriginFileName);
						filelAddMap.put("FILE_PATH2", filePathTemp);
						filelAddMap.put("FILE_TYPE2",  extension.replace(".", ""));						
						}
					}
				}
			}
			
			
			
			return filelAddMap;
		}		
		
		/**
		 * ?��?��?�� ?��?�� 
		 * @param DELETEID
		 * @param TableNm
		 * @param columnm
		 */
		public void delteChk( String NO ,String TableNm , String columnm ){
			LogWriter.getLogger(getClass()).debug("******************* CommonData    ?��?��?�� ?��?��  ************************* " );
			LogWriter.getLogger(getClass()).debug("***** �??��?��?�� 컬럼 번호  ***** " +NO);
			LogWriter.getLogger(getClass()).debug("*******�??��?��?�� ?��?��블명 ***** " +TableNm);
			LogWriter.getLogger(getClass()).debug("*******�??��?��?�� 컬럼�? ***** " +columnm);
			
			
			
			HashMap hashMap = new HashMap();
			
			hashMap.put("DEL_NO", NO);
			hashMap.put("TABLE_NM", TableNm);
			hashMap.put("COLUM_NM", columnm);		
			hashMap.put("procedureid", "Board.delete");
			
			this.commonFacade.processUpdate(hashMap);
			
		}
		
		
		/**
		 * ?��?���? ?���? ?��?��
		 * @param dataMap
		 * @return DataMap
		 */
		public DataMap setPageValue(DataMap dataMap){
			
			int totalCnt = dataMap.getInt("TOTAL_CNT");												//총갯?��
			int currPage = dataMap.getInt("CURR_PAGE") == 0 ? 1 : dataMap.getInt("CURR_PAGE");		//?��?��?��?���?
			int pageSize = dataMap.getInt("PAGE_SIZE") == 0 ? 10 : dataMap.getInt("PAGE_SIZE");		//?��?��?���??�� �??��
			
			if(totalCnt/pageSize < currPage && totalCnt%pageSize == 0){
				currPage = totalCnt/pageSize;
			}else if(totalCnt/pageSize + 1 < currPage){
				currPage = totalCnt/pageSize + 1;
			}
			
			dataMap.put("CURR_PAGE", Integer.toString(currPage));
			dataMap.put("PAGE_SIZE", Integer.toString(pageSize));
			return dataMap;
		}
		
		public static String unescape(String src) {
			StringBuffer tmp = new StringBuffer();
			tmp.ensureCapacity(src.length());
			int lastPos = 0, pos = 0;
			char ch;
			while (lastPos < src.length()) {
				pos = src.indexOf("%", lastPos);
				if (pos == lastPos) {
					if (src.charAt(pos + 1) == 'u') {
						ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
						tmp.append(ch);
						lastPos = pos + 6;
					} else {
						ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
						tmp.append(ch);
						lastPos = pos + 3;
					}
				} else {
					if (pos == -1) {
						tmp.append(src.substring(lastPos));
						lastPos = src.length();
					} else {
						tmp.append(src.substring(lastPos, pos));
						lastPos = pos;
					}
				}
			}
			return tmp.toString();
		}
}
