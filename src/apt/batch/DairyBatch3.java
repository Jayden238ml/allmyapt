package apt.batch;

import apt.framework.common.DataMap;
import apt.framework.core.CommonFacade;
import com.ibatis.common.logging.Log;
import com.ibatis.common.logging.LogFactory;
import java.io.BufferedInputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class DairyBatch3 extends QuartzJobBean
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

  protected void executeInternal(JobExecutionContext context) throws JobExecutionException
  {
    runBatch();
  }

  @Transactional(propagation=Propagation.REQUIRED, rollbackFor={Exception.class}, readOnly=false)
  public void runBatch()
  {
    boolean bol2 = false;
    boolean bol3 = false;
    boolean bol1 = InsertData("2", "417000");
    if (bol1) {
      bol2 = InsertData("2", "422400");
    }
    if (bol2)
      bol3 = InsertData("2", "429900");
  }

  public boolean InsertData(String type, String upkindcd)
  {
    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
    def.setPropagationBehavior(0);
    TransactionStatus status = this.transactionManager.getTransaction(def);

    boolean rtn = false;
    try
    {
      DataMap dataMap = new DataMap();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      Calendar c1 = Calendar.getInstance();
      String strToday = sdf.format(c1.getTime());

      Calendar c2 = Calendar.getInstance();
      if ("1".equals(type))
        c2.add(5, -15);
      else {
        c2.add(5, -30);
      }

      String beDate = sdf.format(c2.getTime());

      String uprcd = "";
      String orgcd = "";
      String stateCd = "";
      String kindcd = "";

      if ("1".equals(type))
        stateCd = "notice";
      else {
        stateCd = "protect";
      }

      StringBuilder urlBuilder = new StringBuilder("http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic");

      urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=FmRJggnPbuErC7S3g3D1K51bawXyTDd7hh%2FJZP%2Bdkyl5OdU79rlNJ%2BNZWXUfncUYfKzWtgUj8Ks6oxWvRQdPSg%3D%3D");

      urlBuilder.append("&" + URLEncoder.encode("bgnde", "UTF-8") + "=" + URLEncoder.encode(beDate, "UTF-8"));

      urlBuilder.append("&" + URLEncoder.encode("endde", "UTF-8") + "=" + URLEncoder.encode(strToday, "UTF-8"));

      urlBuilder.append("&" + URLEncoder.encode("upkind", "UTF-8") + "=" + URLEncoder.encode(upkindcd, "UTF-8"));

      urlBuilder.append("&" + URLEncoder.encode("upr_cd", "UTF-8") + "=" + URLEncoder.encode(uprcd, "UTF-8"));

      if (!"".equals(orgcd)) {
        urlBuilder.append("&" + URLEncoder.encode("org_cd", "UTF-8") + "=" + URLEncoder.encode(orgcd, "UTF-8"));
      }
      if (!"".equals(kindcd)) {
        urlBuilder.append("&" + URLEncoder.encode("kind", "UTF-8") + "=" + URLEncoder.encode(kindcd, "UTF-8"));
      }

      urlBuilder.append("&" + URLEncoder.encode("state", "UTF-8") + "=" + URLEncoder.encode(stateCd, "UTF-8"));

      urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));

      urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("5000", "UTF-8"));

      URL url = new URL(urlBuilder.toString());
      System.out.println("URL==========" + urlBuilder.toString());

      XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
      factory.setNamespaceAware(true);
      XmlPullParser xpp = factory.newPullParser();
      BufferedInputStream bis = new BufferedInputStream(url.openStream());
      xpp.setInput(bis, "utf-8");

      String tag = null;
      int event_type = xpp.getEventType();

      ArrayList<DataMap> list = new ArrayList();

      String bgnde = "";
      String endde = "";
      String upkind = "";
      String kind = "";
      String upr_cd = "";
      String org_cd = "";
      String care_reg_no = "";
      String state = "";
      String neuter_yn = "";
      String resultCode = "";
      String resultMsg = "";
      String desertionNo = "";
      String filename = "";
      String happenDt = "";
      String happenPlace = "";
      String kindCd = "";
      String colorCd = "";
      String age = "";
      String weight = "";
      String noticeNo = "";
      String noticeSdt = "";
      String noticeEdt = "";
      String popfile = "";
      String processState = "";
      String sexCd = "";
      String neuterYn = "";
      String specialMark = "";
      String careNm = "";
      String careTel = "";
      String careAddr = "";
      String orgNm = "";
      String chargeNm = "";
      String officetel = "";
      String noticeComment = "";
      String numOfRows = "";
      String pageNo = "";
      String totalCount = "";

      DataMap tempMap = tempMap = new DataMap();

      while (event_type != 1) {
        if (event_type == 2) {
          tag = xpp.getName();
        } else if (event_type == 4) {
          if ("bgnde".equals(tag)) tempMap.put("bgnde", xpp.getText());
          if ("endde".equals(tag)) tempMap.put("endde", xpp.getText());
          if ("upkind".equals(tag)) tempMap.put("upkind", xpp.getText());
          if ("kind".equals(tag)) tempMap.put("kind", xpp.getText());
          if ("upr_cd".equals(tag)) tempMap.put("upr_cd", xpp.getText());
          if ("org_cd".equals(tag)) tempMap.put("org_cd", xpp.getText());
          if ("care_reg_no".equals(tag)) tempMap.put("care_reg_no", xpp.getText());
          if ("state".equals(tag)) tempMap.put("state", xpp.getText());
          if ("neuter_yn".equals(tag)) tempMap.put("neuter_yn", xpp.getText());
          if ("resultCode".equals(tag)) tempMap.put("resultCode", xpp.getText());
          if ("resultMsg".equals(tag)) tempMap.put("resultMsg", xpp.getText());
          if ("desertionNo".equals(tag)) tempMap.put("desertionNo", xpp.getText());
          if ("filename".equals(tag)) tempMap.put("filename", xpp.getText());
          if ("happenDt".equals(tag)) tempMap.put("happenDt", xpp.getText());
          if ("happenPlace".equals(tag)) tempMap.put("happenPlace", xpp.getText());
          if ("kindCd".equals(tag)) tempMap.put("kindCd", xpp.getText());
          if ("colorCd".equals(tag)) tempMap.put("colorCd", xpp.getText());
          if ("age".equals(tag)) tempMap.put("age", xpp.getText());
          if ("weight".equals(tag)) tempMap.put("weight", xpp.getText());
          if ("noticeNo".equals(tag)) tempMap.put("noticeNo", xpp.getText());
          if ("noticeSdt".equals(tag)) tempMap.put("noticeSdt", xpp.getText());
          if ("noticeEdt".equals(tag)) tempMap.put("noticeEdt", xpp.getText());
          if ("popfile".equals(tag)) tempMap.put("popfile", xpp.getText());
          if ("processState".equals(tag)) tempMap.put("processState", xpp.getText());
          if ("sexCd".equals(tag)) tempMap.put("sexCd", xpp.getText());
          if ("neuterYn".equals(tag)) tempMap.put("neuterYn", xpp.getText());
          if ("specialMark".equals(tag)) tempMap.put("specialMark", xpp.getText());
          if ("careNm".equals(tag)) tempMap.put("careNm", xpp.getText());
          if ("careTel".equals(tag)) tempMap.put("careTel", xpp.getText());
          if ("careAddr".equals(tag)) tempMap.put("careAddr", xpp.getText());
          if ("orgNm".equals(tag)) tempMap.put("orgNm", xpp.getText());
          if ("chargeNm".equals(tag)) tempMap.put("chargeNm", xpp.getText());
          if ("officetel".equals(tag)) tempMap.put("officetel", xpp.getText());
          if ("noticeComment".equals(tag)) tempMap.put("noticeComment", xpp.getText());
          if ("numOfRows".equals(tag)) dataMap.put("PAGE_SIZE", xpp.getText());
          if ("pageNo".equals(tag)) dataMap.put("CURR_PAGE", xpp.getText());
          if ("totalCount".equals(tag)) dataMap.put("TOTAL_CNT", xpp.getText());
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

      int cnt = 0;
      dataMap.put("UPKIND", upkindcd);
      if ("1".equals(type))
        dataMap.put("procedureid", "System.FindAnimal_Delete");
      else {
        dataMap.put("procedureid", "System.adoptAnimal_Delete");
      }
      this.commonFacade.processDelete(dataMap);

      for (DataMap temp : list) {
        dataMap.put("BGNDE", temp.getString("bgnde"));
        dataMap.put("ENDDE", temp.getString("endde"));
        dataMap.put("KIND", temp.getString("kind"));
        dataMap.put("UPR_CD", temp.getString("upr_cd"));
        dataMap.put("ORG_CD", temp.getString("org_cd"));
        dataMap.put("CARE_REG_NO", temp.getString("care_reg_no"));
        dataMap.put("STATE", temp.getString("state"));
        dataMap.put("NEUTER_YN", temp.getString("neuter_yn"));
        dataMap.put("DESERTIONNO", temp.getString("desertionNo"));
        dataMap.put("FILENAME", temp.getString("filename"));
        dataMap.put("HAPPENDT", temp.getString("happenDt"));
        dataMap.put("HAPPENPLACE", temp.getString("happenPlace"));
        dataMap.put("KINDCD", temp.getString("kindCd"));
        dataMap.put("COLORCD", temp.getString("colorCd"));
        dataMap.put("AGE", temp.getString("age"));
        dataMap.put("WEIGHT", temp.getString("weight"));
        dataMap.put("NOTICENO", temp.getString("noticeNo"));
        dataMap.put("NOTICESDT", temp.getString("noticeSdt"));
        dataMap.put("NOTICEEDT", temp.getString("noticeEdt"));
        dataMap.put("POPFILE", temp.getString("popfile"));
        dataMap.put("PROCESSSTATE", temp.getString("processState"));
        dataMap.put("SEXCD", temp.getString("sexCd"));
        dataMap.put("NEUTERYN", temp.getString("neuterYn"));
        dataMap.put("SPECIALMARK", temp.getString("specialMark"));
        dataMap.put("CARENM", temp.getString("careNm"));
        dataMap.put("CARETEL", temp.getString("careTel"));
        dataMap.put("CAREADDR", temp.getString("careAddr"));
        dataMap.put("ORGNM", temp.getString("orgNm"));
        dataMap.put("CHARGENM", temp.getString("chargeNm"));
        dataMap.put("OFFICETEL", temp.getString("officetel"));
        dataMap.put("NOTICECOMMENT", temp.getString("noticeComment"));

        if ("1".equals(type))
          dataMap.put("procedureid", "System.FindAnimal_Insert");
        else {
          dataMap.put("procedureid", "System.adoptAnimal_Insert");
        }
        this.commonFacade.processInsert(dataMap);

        cnt++;
      }

      dataMap.put("CNT", Integer.valueOf(cnt));
      this.transactionManager.commit(status);

      String nm = "";
      if ("417000".equals(upkindcd)) nm = "�?";
      if ("422400".equals(upkindcd)) nm = "고양?��";
      if ("429900".equals(upkindcd)) nm = "기�?";
      if ("1".equals(type))
        this.log.debug("######################## 공고�? " + nm + "( " + cnt + " )�? ???��?��#########################");
      else {
        this.log.debug("######################## 보호�? " + nm + "( " + cnt + " )�? ???��?��#########################");
      }
      rtn = true;
    }
    catch (Exception ex) {
      this.transactionManager.rollback(status);
      ex.printStackTrace();
    } finally {
      if (!status.isCompleted()) this.transactionManager.rollback(status);
    }
    return rtn;
  }
}