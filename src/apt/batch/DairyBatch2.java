package apt.batch;

import apt.framework.common.DataMap;
import apt.framework.core.CommonFacade;
import apt.framework.util.PUtil;

import com.ibatis.common.logging.Log;
import com.ibatis.common.logging.LogFactory;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class DairyBatch2 extends QuartzJobBean
{
	private CommonFacade commonFacade;
  	private PlatformTransactionManager transactionManager;
  	Log log = LogFactory.getLog(getClass());
  	

  	@Autowired
  	@Qualifier("transactionManager")
  	public void setTransactionManager(PlatformTransactionManager transactionManager) { this.transactionManager = transactionManager; }

  	@Autowired
  	@Qualifier("commonImpl")
  	public void setCommonImpl(CommonFacade commonFacade) {
  		this.commonFacade = commonFacade;
  	}

  	protected void executeInternal(JobExecutionContext context) throws JobExecutionException{
  		runBatch();
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor={Exception.class}, readOnly=false)
	public void runBatch(){
		DataMap dataMap = new DataMap();
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		Calendar cal = Calendar.getInstance();							
		Calendar b_cal = Calendar.getInstance();							
		DateFormat sdFormat = new SimpleDateFormat("yyyyMM");
		dataMap.put("TODAY",sdFormat.format(cal.getTime()));
		
		b_cal.add(Calendar.MONTH, -1);
		dataMap.put("B_TODAY",sdFormat.format(b_cal.getTime()));
		
		dataMap.put("procedureid", "Common.getAreaSiGunGu_AllList");
		List<DataMap> SiGunGuList = commonFacade.list(dataMap);
		
		PUtil pu = new PUtil();
		
		try {
			
			log.debug("######################## "+ dataMap.getString("TODAY") +" 분양권 배치 시작(START) #########################");
			
			// 2개월치 삭제
			dataMap.put("DEAL_YEAR", dataMap.getString("TODAY").substring(0,4));
			String month = dataMap.getString("TODAY").substring(4,6);
			if("0".equals(month.substring(0,1))) {
				month = dataMap.getString("TODAY").substring(5,6);
			}
			dataMap.put("DEAL_MONTH", month);
			dataMap.put("procedureid", "Api.ParcelOutInfo_Delete");
			commonFacade.processDelete(dataMap);
			
			dataMap.put("DEAL_YEAR", dataMap.getString("B_TODAY").substring(0,4));
			String bmonth = dataMap.getString("B_TODAY").substring(4,6);
			if("0".equals(bmonth.substring(0,1))) {
				bmonth = dataMap.getString("B_TODAY").substring(5,6);
			}
			dataMap.put("DEAL_MONTH", bmonth);
			dataMap.put("procedureid", "Api.ParcelOutInfo_Delete");
			commonFacade.processDelete(dataMap);
			
			for(int m = 1 ; m <= 2; m ++) {
				String deal_ymd = "";
				if(m == 1) {
					deal_ymd = dataMap.getString("B_TODAY");
				}else {
					deal_ymd = dataMap.getString("TODAY");
				}
				
				for(DataMap tmp : SiGunGuList) {
	    			StringBuilder urlBuilder = new StringBuilder("http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcSilvTrade");
	    	        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=FmRJggnPbuErC7S3g3D1K51bawXyTDd7hh%2FJZP%2Bdkyl5OdU79rlNJ%2BNZWXUfncUYfKzWtgUj8Ks6oxWvRQdPSg%3D%3D");
	    	        urlBuilder.append("&" + URLEncoder.encode("DEAL_YMD", "UTF-8") + "=" + URLEncoder.encode(deal_ymd, "UTF-8"));
					urlBuilder.append("&" + URLEncoder.encode("LAWD_CD", "UTF-8") + "=" + URLEncoder.encode(tmp.getString("CODE"), "UTF-8"));
					URL url = new URL(urlBuilder.toString());
//					System.out.println("년월==========" + deal_ymd);
//					System.out.println("시군구명==========" + tmp.getString("CODENM"));
					
					XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			        factory.setNamespaceAware(true);
			        XmlPullParser xpp = factory.newPullParser();
			        BufferedInputStream bis = new BufferedInputStream(url.openStream());
			        xpp.setInput(bis, "utf-8");

			        String tag = null;
			        int event_type = xpp.getEventType();

			        ArrayList list = new ArrayList();
			        DataMap tempMap = tempMap = new DataMap();

			        while (event_type != 1) {
			          if (event_type == 2) {
			            tag = xpp.getName();
			          } else if (event_type == 4)
			          {
			        	  
			            if ("거래금액".equals(tag)) tempMap.put("DEAL_AMOUNT", xpp.getText());
			            if ("년".equals(tag)) tempMap.put("DEAL_YEAR", xpp.getText());
			            if ("단지".equals(tag)) tempMap.put("APARTMENT_NAME", xpp.getText());
			            if ("법정동".equals(tag)) tempMap.put("DONG", xpp.getText());
			            if ("시군구".equals(tag)) tempMap.put("SIGUNGU", xpp.getText());
			            if ("월".equals(tag)) tempMap.put("DEAL_MONTH", xpp.getText());
			            if ("일".equals(tag)) tempMap.put("DEAL_DAY", xpp.getText());
			            if ("전용면적".equals(tag)) tempMap.put("AREA_EXCLUSIVE_USE", xpp.getText());
			            if ("지번".equals(tag)) tempMap.put("JIBUN", xpp.getText());
			            if ("지역코드".equals(tag)) tempMap.put("REGIONAL_CODE", xpp.getText());
			            if ("층".equals(tag)) tempMap.put("FLOOR", xpp.getText());

			          }
			          else if (event_type == 3) {
			            tag = xpp.getName();
			            if (tag.equals("item")) {
			              list.add(tempMap);
			              tempMap = new DataMap();
			            }
			          }

			          event_type = xpp.next();
			        }
			        for(int i =0; i < list.size(); i ++) {
			        	DataMap insertMap = new DataMap();
			        	insertMap = (DataMap) list.get(i);
			        	
			        	insertMap.put("procedureid", "Api.ParcelOutInfo_Insert");
						commonFacade.processInsert(insertMap);
			        }
				}
			}
			
			
			log.debug("######################## "+ dataMap.getString("TODAY") +" 분양권 배치 종료(END) #########################");
			
			log.debug("######################## "+ dataMap.getString("TODAY") +" 실거래가 배치 시작(START) #########################");
			
			
			// 2개월치 삭제
			dataMap.put("DEAL_YEAR", dataMap.getString("TODAY").substring(0,4));
			dataMap.put("DEAL_MONTH", month);
			dataMap.put("procedureid", "Api.DealAptInfo_Delete");
			commonFacade.processDelete(dataMap);
			
			dataMap.put("DEAL_YEAR", dataMap.getString("B_TODAY").substring(0,4));
			dataMap.put("DEAL_MONTH", bmonth);
			dataMap.put("procedureid", "Api.DealAptInfo_Delete");
			commonFacade.processDelete(dataMap);
			
			for(int m = 1 ; m <= 2; m ++) {
				String deal_ymd = "";
				if(m == 1) {
					deal_ymd = dataMap.getString("B_TODAY");
				}else {
					deal_ymd = dataMap.getString("TODAY");
				}
				
				for(DataMap tmp : SiGunGuList) {
					StringBuilder urlBuilder = new StringBuilder("http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTradeDev");
					urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=FmRJggnPbuErC7S3g3D1K51bawXyTDd7hh%2FJZP%2Bdkyl5OdU79rlNJ%2BNZWXUfncUYfKzWtgUj8Ks6oxWvRQdPSg%3D%3D");
					
					urlBuilder.append("&" + URLEncoder.encode("DEAL_YMD", "UTF-8") + "=" + URLEncoder.encode(deal_ymd, "UTF-8"));
					urlBuilder.append("&" + URLEncoder.encode("LAWD_CD", "UTF-8") + "=" + URLEncoder.encode(tmp.getString("CODE"), "UTF-8"));
			        urlBuilder.append("&numOfRows=100000"); 
			        urlBuilder.append("&pageNo=1"); 
					
					URL url = new URL(urlBuilder.toString());
//					System.out.println("년월==========" + deal_ymd);
//					System.out.println("시군구명==========" + tmp.getString("CODENM"));
					
					XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			        factory.setNamespaceAware(true);
			        XmlPullParser xpp = factory.newPullParser();
			        BufferedInputStream bis = new BufferedInputStream(url.openStream());
			        xpp.setInput(bis, "utf-8");

			        String tag = null;
			        int event_type = xpp.getEventType();

			        ArrayList list = new ArrayList();
			        DataMap tempMap = tempMap = new DataMap();

			        while (event_type != 1) {
			          if (event_type == 2) {
			            tag = xpp.getName();
			          } else if (event_type == 4)
			          {
			        	  
			        	  if ("거래금액".equals(tag)) tempMap.put("DEAL_AMOUNT", xpp.getText());
			        	  if ("년".equals(tag)) tempMap.put("DEAL_YEAR", xpp.getText());
			        	  if ("아파트".equals(tag)) tempMap.put("APARTMENT_NAME", xpp.getText());
			        	  if ("법정동".equals(tag)) tempMap.put("DONG", xpp.getText());
			        	  if ("시군구".equals(tag)) tempMap.put("SIGUNGU_CODE", xpp.getText());
			        	  if ("월".equals(tag)) tempMap.put("DEAL_MONTH", xpp.getText());
			        	  if ("일".equals(tag)) tempMap.put("DEAL_DAY", xpp.getText());
			        	  if ("전용면적".equals(tag)) tempMap.put("AREA_EXCLUSIVE_USE", xpp.getText());
			        	  if ("지번".equals(tag)) tempMap.put("JIBUN", xpp.getText());
			        	  if ("지역코드".equals(tag)) tempMap.put("REGIONAL_CODE", xpp.getText());
			        	  if ("층".equals(tag)) tempMap.put("FLOOR", xpp.getText());
			        	  if ("건축년도".equals(tag)) tempMap.put("BUILD_YEAR", xpp.getText());
			        	  if ("도로명".equals(tag)) tempMap.put("ROAD_NAME", xpp.getText());
			        	  if ("도로명건물본번호코드".equals(tag)) tempMap.put("ROAD_NAME_BONBUN", xpp.getText());
			        	  if ("도로명건물부번호코드".equals(tag)) tempMap.put("ROAD_NAME_BUBUN", xpp.getText());
			        	  if ("도로명시군구코드".equals(tag)) tempMap.put("ROAD_NAME_SIGUNGU_CODE", xpp.getText());
			        	  if ("도로명일련번호코드".equals(tag)) tempMap.put("ROAD_NAME_SEQ", xpp.getText());
			        	  if ("도로명코드".equals(tag)) tempMap.put("ROAD_NAME_CODE", xpp.getText());

			          }
			          else if (event_type == 3) {
			            tag = xpp.getName();
			            if (tag.equals("item")) {
			              list.add(tempMap);
			              tempMap = new DataMap();
			            }
			          }

			          event_type = xpp.next();
			        }
			        for(int i =0; i < list.size(); i ++) {
			        	DataMap insertMap = new DataMap();
			        	insertMap = (DataMap) list.get(i);
			        	
			        	String sido_code = insertMap.getString("REGIONAL_CODE").substring(0,2);
						insertMap.put("SIDO_CODE", sido_code);
						
						insertMap.put("procedureid", "Api.getSiGunGuNmData");
					    DataMap addrMap = this.commonFacade.getObject(insertMap);
						
						int bonbun = insertMap.getInt("ROAD_NAME_BONBUN");
						int bubun = insertMap.getInt("ROAD_NAME_BUBUN");
						
						String doro_jibun = "";
						if(bonbun > 0) {
							doro_jibun = String.valueOf(bonbun);
						}
						if(bubun > 0) {
							doro_jibun = doro_jibun + "-" + String.valueOf(bubun);
						}
						
						String rn = insertMap.getString("ROAD_NAME");
						if("".equals(rn)) {
							rn = insertMap.getString("DONG");
						}
						String address = addrMap.getString("UP_NM") + " " + addrMap.getString("CODENM") + " " + rn + " " +  doro_jibun;
						
						DataMap xyMap = new DataMap();
						if(!"".equals(address) && !"".equals(rn)){
							  xyMap = pu.addrToLocation(URLEncoder.encode(address,"UTF-8"));
							  insertMap.put("ADDRESS", address);
						  }
						  if(!"".equals(xyMap)){
							  insertMap.put("X_LOCATION", xyMap.getString("X_LOCATION"));
							  insertMap.put("Y_LOCATION", xyMap.getString("Y_LOCATION"));
						  }
						
			        	insertMap.put("procedureid", "Api.RealAptInfo_Insert");
						commonFacade.processInsert(insertMap);
			        }
				}
			}
			log.debug("######################## "+ dataMap.getString("TODAY") +" 실거래가 배치 종료(END) #########################");
			
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
          	e.printStackTrace();
		}finally {
			if (!status.isCompleted()) transactionManager.rollback(status);
		}
	}
	

}