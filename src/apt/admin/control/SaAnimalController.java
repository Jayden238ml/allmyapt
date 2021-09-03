package apt.admin.control;

import java.io.BufferedInputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import apt.framework.common.DataMap;
import apt.framework.common.control.LincActionController;
import apt.framework.core.CommonFacade;
import apt.framework.util.BrowserUtil;
import apt.framework.util.PUtil;

import com.ibatis.common.logging.Log;
import com.ibatis.common.logging.LogFactory;


@Controller
public class SaAnimalController extends LincActionController
{
  protected CommonFacade commonFacade;
  private PlatformTransactionManager transactionManager;
  Log log = LogFactory.getLog(getClass());

//  protected DataMap paramMap = null;

  @Autowired
  public void setTransactionManager(PlatformTransactionManager transactionManager)
  {
    this.transactionManager = transactionManager;
  }
  @Autowired
  @Qualifier("commonImpl")
  public void setCommonImpl(CommonFacade commonFacade) {
    this.commonFacade = commonFacade;
  }

  @ModelAttribute("requestParam")
  public DataMap requestParam(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception
  {
    showParameters(arg0);
    DataMap paramMap = new PUtil().getParameterDataMap(arg0);
    setSessionMenu(this.commonFacade, arg0, paramMap);
    return paramMap;
  }

  public void showParameters(HttpServletRequest request)
  {
    this.log.debug("###############################################################");
    this.log.debug("REQUEST  URL : " + request.getRequestURL());
    Enumeration paramNames = request.getParameterNames();
    try
    {
      while (paramNames.hasMoreElements()) {
        String name = ((String)paramNames.nextElement()).toString();
        String value = StringUtils.defaultIfEmpty(request.getParameter(name), "");

        this.log.debug("PARAM : " + name.toUpperCase() + "\t VALUE : " + value);
      }

      this.log.debug("###############################################################");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

	  /*
	   *게시?�� 리스?�� ?��?��
	  */
	@RequestMapping({"/Sa/animal/findapt.do"})
	public ModelAndView findAnimal(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response
			,@RequestParam(value="CURR_PAGE", required=false, defaultValue="1")String page
			,@RequestParam(value="PAGE_SIZE", required=false, defaultValue="10")String page_size) {
		List boardList = null;
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
	    def.setPropagationBehavior(0);
	    TransactionStatus status = this.transactionManager.getTransaction(def);
		
		try {
    	
			String type = dataMap.getString("TYPE");
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	        Calendar c1 = Calendar.getInstance();
	        String strToday = sdf.format(c1.getTime());
	        
	        Calendar c2 = Calendar.getInstance();
	        if("1".equals(type)){
	        	c2.add(Calendar.DATE, -15);
	        }else{
	        	c2.add(Calendar.DATE, -30);
	        }
	        
	        String beDate = sdf.format(c2.getTime());


			String uprcd = "";
			String orgcd = "";
			String stateCd = "";
			String upkindcd = dataMap.getString("UP_KIND_CD");
			String kindcd = "";
			if("".equals(upkindcd)){
				upkindcd = "417000"; 
			}
			
			if("1".equals(type)){
				stateCd = "notice";
			}else{
				stateCd = "protect";
			}
			
			StringBuilder urlBuilder = new StringBuilder("http://openapi.apt.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic");
			// ?��비스 KEY
			urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=FmRJggnPbuErC7S3g3D1K51bawXyTDd7hh%2FJZP%2Bdkyl5OdU79rlNJ%2BNZWXUfncUYfKzWtgUj8Ks6oxWvRQdPSg%3D%3D");
			// �??�� ?��?��?��
			urlBuilder.append("&" + URLEncoder.encode("bgnde","UTF-8") + "=" + URLEncoder.encode(beDate, "UTF-8")); 
			// �??�� 종료?��
			urlBuilder.append("&" + URLEncoder.encode("endde","UTF-8") + "=" + URLEncoder.encode(strToday, "UTF-8")); 
			// 축종코드 - �? : 417000 - 고양?�� : 422400 - 기�? : 429900
			urlBuilder.append("&" + URLEncoder.encode("upkind","UTF-8") + "=" + URLEncoder.encode(upkindcd, "UTF-8"));
			
			// ?��?��코드 (?��?�� 조회 OPEN API 참조)
			urlBuilder.append("&" + URLEncoder.encode("upr_cd","UTF-8") + "=" + URLEncoder.encode(uprcd, "UTF-8")); 
			
			// ?��군구코드 (?��군구 조회 OPEN API 참조)
			if(!"".equals(orgcd)){
				urlBuilder.append("&" + URLEncoder.encode("org_cd","UTF-8") + "=" + URLEncoder.encode(orgcd, "UTF-8"));
			}
			if(!"".equals(kindcd)){
				urlBuilder.append("&" + URLEncoder.encode("kind","UTF-8") + "=" + URLEncoder.encode(kindcd, "UTF-8"));
			}
			
			// ?��종코?�� (?���? 조회 OPEN API 참조)
//			urlBuilder.append("&" + URLEncoder.encode("kind","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); 
			// 보호?��번호 (보호?�� 조회 OPEN API 참조)
//	        urlBuilder.append("&" + URLEncoder.encode("care_reg_no","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); 
			// ?��?�� - ?���? : null(빈값) - 공고�? : notice - 보호�? : protect
	        urlBuilder.append("&" + URLEncoder.encode("state","UTF-8") + "=" + URLEncoder.encode(stateCd, "UTF-8")); 
			// ?��?���? 번호
	        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(page, "UTF-8")); 
			// ?��?���??�� 보여�? 개수
	        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("5000", "UTF-8")); 
	        // 중성?��?���?
//	        urlBuilder.append("&" + URLEncoder.encode("neuter_yn","UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8")); 
			
	        URL url = new URL(urlBuilder.toString());
	        System.out.println("URL==========" + urlBuilder.toString());
	        
	        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	        factory.setNamespaceAware(true);
	        XmlPullParser xpp = factory.newPullParser();
	        BufferedInputStream bis = new BufferedInputStream(url.openStream());
	        xpp.setInput(bis, "utf-8");
	        
	        String tag = null;
	        int event_type = xpp.getEventType();
	        
	        	        
	        ArrayList<DataMap> list = new ArrayList<DataMap>();
	        
	        String  bgnde			= ""; 	// ?��기날�?(�??�� ?��?��?��)     	
	        String  endde			= ""; 	// ?��기날�?(�??�� 종료?��)     
	        String  upkind			= ""; 	// 축종코드                
	        String  kind			= ""; 	// ?��종코?��                
	        String  upr_cd			= ""; 	// ?��?��코드                
	        String  org_cd			= ""; 	// ?��군구코드               
	        String  care_reg_no		= ""; 	// 보호?��번호               
	        String  state			= ""; 	// ?��?��                    
	        String  neuter_yn		= ""; 	// 중성?��?���?               
	        String  resultCode		= ""; 	// 결과코드                
	        String  resultMsg		= ""; 	// 결과메세�?               
	        String  desertionNo		= ""; 	// ?��기번?��                
	        String  filename		= ""; 	// Thumbnail Image        
	        String  happenDt		= ""; 	// ?��?��?��                  
	        String  happenPlace		= ""; 	// 발견?��?��                
	        String  kindCd			= ""; 	// ?���?                    
	        String  colorCd			= ""; 	// ?��?��                    
	        String  age				= ""; 	// ?��?��                    
	        String  weight			= ""; 	// 체중                    
	        String  noticeNo		= ""; 	// 공고번호                
	        String  noticeSdt		= ""; 	// 공고?��?��?��               
	        String  noticeEdt		= ""; 	// 공고종료?��               
	        String  popfile			= ""; 	// Image                  
	        String  processState	= ""; 	// ?��?��                    
	        String  sexCd			= ""; 	// ?���?                    
	        String  neuterYn		= ""; 	// 중성?��?���?               
	        String  specialMark		= ""; 	// ?���?                    
	        String  careNm			= ""; 	// 보호?��?���?               
	        String  careTel			= ""; 	// 보호?��?��?��번호           
	        String  careAddr		= ""; 	// 보호?��?��                
	        String  orgNm			= ""; 	// �??��기�?                
	        String  chargeNm		= ""; 	// ?��?��?��                  
	        String  officetel		= ""; 	// ?��?��?��?��?���?             
	        String  noticeComment	= ""; 	// ?��?��?��?��                
	        String  numOfRows		= ""; 	// ?�� ?��?���?결과 ?��         
	        String  pageNo			= ""; 	// ?��?���? 번호              
	        String  totalCount		= ""; 	// ?���? 결과 ?��   
	        
	        DataMap tempMap = tempMap = new DataMap();
	        
	        while (event_type != XmlPullParser.END_DOCUMENT) {
	            if (event_type == XmlPullParser.START_TAG) {
	                tag = xpp.getName();
	            } else if (event_type == XmlPullParser.TEXT) {
            		if("bgnde".equals(tag)){tempMap.put("bgnde", xpp.getText());}
	            	if("endde".equals(tag)){tempMap.put("endde", xpp.getText());}
	            	if("upkind".equals(tag)){tempMap.put("upkind", xpp.getText());}
	            	if("kind".equals(tag)){tempMap.put("kind", xpp.getText());}
	            	if("upr_cd".equals(tag)){tempMap.put("upr_cd", xpp.getText());}
	            	if("org_cd".equals(tag)){tempMap.put("org_cd", xpp.getText());}
	            	if("care_reg_no".equals(tag)){tempMap.put("care_reg_no", xpp.getText());}
	            	if("state".equals(tag)){tempMap.put("state", xpp.getText());}
	            	if("neuter_yn".equals(tag)){tempMap.put("neuter_yn", xpp.getText());}
	            	if("resultCode".equals(tag)){tempMap.put("resultCode", xpp.getText());}
	            	if("resultMsg".equals(tag)){tempMap.put("resultMsg", xpp.getText());}
	            	if("desertionNo".equals(tag)){tempMap.put("desertionNo", xpp.getText());}
	            	if("filename".equals(tag)){tempMap.put("filename", xpp.getText());}
	            	if("happenDt".equals(tag)){tempMap.put("happenDt", xpp.getText());}
	            	if("happenPlace".equals(tag)){tempMap.put("happenPlace", xpp.getText());}
	            	if("kindCd".equals(tag)){tempMap.put("kindCd", xpp.getText());}
	            	if("colorCd".equals(tag)){tempMap.put("colorCd", xpp.getText());}
	            	if("age".equals(tag)){tempMap.put("age", xpp.getText());}
	            	if("weight".equals(tag)){tempMap.put("weight", xpp.getText());}
	            	if("noticeNo".equals(tag)){tempMap.put("noticeNo", xpp.getText());}
	            	if("noticeSdt".equals(tag)){tempMap.put("noticeSdt", xpp.getText());}
	            	if("noticeEdt".equals(tag)){tempMap.put("noticeEdt", xpp.getText());}
	            	if("popfile".equals(tag)){tempMap.put("popfile", xpp.getText());}
	            	if("processState".equals(tag)){tempMap.put("processState", xpp.getText());}
	            	if("sexCd".equals(tag)){tempMap.put("sexCd", xpp.getText());}
	            	if("neuterYn".equals(tag)){tempMap.put("neuterYn", xpp.getText());}
	            	if("specialMark".equals(tag)){tempMap.put("specialMark", xpp.getText());}
	            	if("careNm".equals(tag)){tempMap.put("careNm", xpp.getText());}
	            	if("careTel".equals(tag)){tempMap.put("careTel", xpp.getText());}
	            	if("careAddr".equals(tag)){tempMap.put("careAddr", xpp.getText());}
	            	if("orgNm".equals(tag)){tempMap.put("orgNm", xpp.getText());}
	            	if("chargeNm".equals(tag)){tempMap.put("chargeNm", xpp.getText());}
	            	if("officetel".equals(tag)){tempMap.put("officetel", xpp.getText());}
	            	if("noticeComment".equals(tag)){tempMap.put("noticeComment", xpp.getText());}
	            	if("numOfRows".equals(tag)){dataMap.put("PAGE_SIZE", xpp.getText());}
	            	if("pageNo".equals(tag)){dataMap.put("CURR_PAGE", xpp.getText());}
	            	if("totalCount".equals(tag)){dataMap.put("TOTAL_CNT", xpp.getText());}
	            	
	            } else if (event_type == XmlPullParser.END_TAG) {
	                tag = xpp.getName();
	                if (tag.equals("item")) {
	                	list.add(tempMap);
 	                    tempMap = new DataMap();
	                }
	            }
	 
	            event_type = xpp.next();
	        }
	        
	        int cnt = 0;
	        dataMap.put("UPKIND"        , upkindcd );
	        if("1".equals(type)){
        		dataMap.put("procedureid", "System.FindAnimal_Delete");
        	}else{
        		dataMap.put("procedureid", "System.adoptAnimal_Delete");
        	}
	        this.commonFacade.processDelete(dataMap);
	        
	        for(DataMap temp : list){
	        	dataMap.put("BGNDE"			, temp.getString("bgnde"        ) );
	        	dataMap.put("ENDDE"         , temp.getString("endde"        ) );
	        	dataMap.put("KIND"          , temp.getString("kind"         ) );
	        	dataMap.put("UPR_CD"        , temp.getString("upr_cd"       ) );
	        	dataMap.put("ORG_CD"        , temp.getString("org_cd"       ) );
	        	dataMap.put("CARE_REG_NO"   , temp.getString("care_reg_no"  ) );
	        	dataMap.put("STATE"         , temp.getString("state"        ) );
	        	dataMap.put("NEUTER_YN"     , temp.getString("neuter_yn"    ) );
	        	dataMap.put("DESERTIONNO"   , temp.getString("desertionNo"  ) );
	        	dataMap.put("FILENAME"      , temp.getString("filename"     ) );
	        	dataMap.put("HAPPENDT"      , temp.getString("happenDt"     ) );
	        	dataMap.put("HAPPENPLACE"   , temp.getString("happenPlace"  ) );  
	        	dataMap.put("KINDCD"        , temp.getString("kindCd"       ) );  
	        	dataMap.put("COLORCD"       , temp.getString("colorCd"      ) );  
	        	dataMap.put("AGE"           , temp.getString("age"          ) );  
	        	dataMap.put("WEIGHT"        , temp.getString("weight"       ) );  
	        	dataMap.put("NOTICENO"      , temp.getString("noticeNo"     ) );  
	        	dataMap.put("NOTICESDT"     , temp.getString("noticeSdt"    ) );  
	        	dataMap.put("NOTICEEDT"     , temp.getString("noticeEdt"    ) );  
	        	dataMap.put("POPFILE"       , temp.getString("popfile"      ) );  
	        	dataMap.put("PROCESSSTATE"  , temp.getString("processState" ) );  
	        	dataMap.put("SEXCD"         , temp.getString("sexCd"        ) );  
	        	dataMap.put("NEUTERYN"      , temp.getString("neuterYn"     ) );  
	        	dataMap.put("SPECIALMARK"   , temp.getString("specialMark"  ) );  
	        	dataMap.put("CARENM"        , temp.getString("careNm"       ) );  
	        	dataMap.put("CARETEL"       , temp.getString("careTel"      ) );  
	        	dataMap.put("CAREADDR"      , temp.getString("careAddr"     ) );  
	        	dataMap.put("ORGNM"         , temp.getString("orgNm"        ) );  
	        	dataMap.put("CHARGENM"      , temp.getString("chargeNm"     ) );  
	        	dataMap.put("OFFICETEL"     , temp.getString("officetel"    ) );  
	        	dataMap.put("NOTICECOMMENT" , temp.getString("noticeComment") );

	        	
	        	if("1".equals(type)){
	        		dataMap.put("procedureid", "System.FindAnimal_Insert");
	        	}else{
	        		dataMap.put("procedureid", "System.adoptAnimal_Insert");
	        	}
	        	this.commonFacade.processInsert(dataMap);
	        	
	        	cnt ++;
	        }
	        
	        dataMap.put("CNT", cnt);
	        this.transactionManager.commit(status);
		}
		catch (Exception ex) {
			this.transactionManager.rollback(status);
			ex.printStackTrace();
			dataMap.put("ERROR_CD", "999");
		}finally {
		      if (!status.isCompleted()) this.transactionManager.rollback(status);
	    }
		return new ModelAndView("jsonView", dataMap);
  }
	
	
	
	
	
}