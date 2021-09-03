package apt.batch;

import apt.framework.common.DataMap;
import apt.framework.core.CommonFacade;
import com.ibatis.common.logging.Log;
import com.ibatis.common.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class DairyBatch extends QuartzJobBean
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
    DataMap dataMap = new DataMap();
    try
    {
      dataMap.put("procedureid", "Common.SelectAs");
      DataMap detail = this.commonFacade.getObject(dataMap);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}