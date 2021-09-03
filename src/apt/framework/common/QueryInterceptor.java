package apt.framework.common;

import java.util.HashMap;
import java.util.Properties;

import apt.framework.util.PUtil;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


@Intercepts(
    {
    		@Signature(type = Executor.class, method = "query",  args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
       		,@Signature(type = Executor.class, method = "query",  args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
    }
)

public class QueryInterceptor implements Interceptor 
{
    private Logger log = LogManager.getLogger(getClass());
    
    @Override
    public Object intercept(Invocation invocation) throws Throwable
    {
        Object[]        args     = invocation.getArgs();
        MappedStatement ms       = (MappedStatement)args[0];
        String userID = "";
        if((HashMap)args[1] != null && ((HashMap)args[1]).get("SESSION_USR_ID") != null){
        	userID = (String)((HashMap)args[1]).get("SESSION_USR_ID");
        }
        if(!"".equals( userID )){
        	log.debug(PUtil.getCurrentDate("yyyy.MM.dd HH:mm:ss") +","+ userID + "," + ms.getId());
        }
       
        return invocation.proceed();
    }


    @Override
    public Object plugin(Object target)
    {
        return Plugin.wrap(target, this);
    }


    @Override
    public void setProperties(Properties properties)
    {
    }
}