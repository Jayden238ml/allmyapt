package apt.framework.common.datagokr;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DataGoKrService  {
	
    private JdbcTemplate jdbcTemplate;
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private Integer newVersion;

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
    public DataGoKrService() {
    	StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
    	List list = Arrays.asList(stringHttpMessageConverter);
    	RestTemplate restTemplate = new RestTemplate();
    	restTemplate.setMessageConverters(list);
    	this.restTemplate = restTemplate;
    	
    	ObjectMapper objectMapper = new ObjectMapper();
    	System.out.println(objectMapper);
    	
    	objectMapper.setAnnotationIntrospector( new JacksonAnnotationIntrospector());
		objectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, true);
    	objectMapper.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, true);
    	objectMapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    	objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, true);
    	objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    	this.objectMapper = objectMapper;
	}

    
    public int getDgkNewVersion() throws Exception {
    	int version = this.jdbcTemplate.queryForObject("SELECT TOP 1 VERSION FROM DGK_VERSION ORDER BY VERSION_DATE DESC", Integer.class);
    	System.out.println("current Version : " + version);
    	
    	
    	version = version + 1;
    	int update = this.jdbcTemplate.update("INSERT INTO DGK_VERSION (VERSION) VALUES (?)", version);
    	System.out.println("update new version result : " +  update);
    	newVersion = version;
    	return newVersion;
    	
//    	newVersion = 31;
//    	return newVersion;
    }
    
    public void setNewVersion(Integer newVersion) {
		this.newVersion = newVersion;
	}
    


	@Async
	public Future<String> getApiResponse(String tableName, List<String> cols, String url, Map<String, String> paraMap) throws Exception {
		
		System.out.println(url);
		System.out.println(paraMap);
		
    	String results = restTemplate.getForObject(url, String.class, paraMap);
		DataGoKrResponse dgk = objectMapper.readValue(results, DataGoKrResponse.class);
		ArrayList<Map<String, String>> items = dgk.getItems();
		if (items == null){
			return new AsyncResult<>("done");
		}
		
		String[] sqls = new String[items.size()];
		for (int j = 0; j < items.size(); j++) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("INSERT INTO ");
			stringBuilder.append(tableName);
			stringBuilder.append(" (");
			Map<String, String> map = items.get(j);
			String[] keys = map.keySet().toArray(new String[map.size()]);
			for (int i = 0; i < keys.length; i++) {
				String key = keys[i];
				if (cols.contains(key)){
					stringBuilder.append(key.toUpperCase());
					stringBuilder.append(",");
				}
			}
			stringBuilder.append("VERSION) VALUES (");
			for (int i = 0; i < keys.length; i++) {
				String key = keys[i];
				String val = map.get(key);
				if (cols.contains(key)){
					stringBuilder.append("'");
					stringBuilder.append(val);
					stringBuilder.append("'");
					stringBuilder.append(",");
				}
			}
			stringBuilder.append(newVersion);
			stringBuilder.append(")");
			sqls[j] = stringBuilder.toString();
		}
		
		int[] batchInsertResult = jdbcTemplate.batchUpdate(sqls);
		System.out.println("batchInsertResult : " + batchInsertResult);
		
		Thread.sleep(10L);
		return requestNextPage(tableName, cols, url, paraMap, dgk);
		
	}
	
	
	private Future<String> requestNextPage(String tableName, List<String> cols, String url, Map<String, String> paraMap, DataGoKrResponse dgk)
			throws Exception {
		String numOfRows = dgk.getBody().getNumOfRows();
		String pageNo = dgk.getBody().getPageNo();
		String totalCount = dgk.getBody().getTotalCount();
		
		System.out.println("numOfRows :" + numOfRows);
		System.out.println("pageNo : " + pageNo);
		System.out.println("totalCount : " + totalCount);
		
//		?óÜ?äî ?éò?ù¥Ïß?Î•? ?ò∏Ï∂úÌïòÎ©?  numOfRows, pageNo, totalCount Í∞? ?óÜ?ã§.
		if(Integer.valueOf(numOfRows) * Integer.valueOf(pageNo) < Integer.valueOf(totalCount)){
			// page + 1
			paraMap.put("pageNo", String.valueOf(Integer.valueOf(pageNo) + 1));
			return getApiResponse(tableName, cols, url, paraMap);
		}else{
			return new AsyncResult<>("done");
		}
	}
	


	public void createTable(List<String> allKeys, String tableName) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("CREATE TABLE ");
		stringBuilder.append(tableName.toUpperCase());
		stringBuilder.append(" ");
		stringBuilder.append("(");
		for (String col : allKeys) {
			stringBuilder.append(col);
			stringBuilder.append(" ");
			stringBuilder.append("VARCHAR(125)");
			stringBuilder.append(",");
		}
		stringBuilder.append("VERSION INT");
		stringBuilder.append(")");
		
		String queryForCreateTable = stringBuilder.toString();
		System.out.println(queryForCreateTable);
		
		jdbcTemplate.execute(queryForCreateTable);
		
	}


	public boolean checkTableExists(String tableName) {
		List<Map<String, Object>> queryForList = this.jdbcTemplate.queryForList("select 1 from sysobjects where name=? and xtype='U'", tableName);
		return queryForList.size() ==1;
	}


	@Autowired
	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

	}
	
	
	public List<String> getSchlIds(String svyyr, Integer version){
		String query = "SELECT SCHLID FROM DGK_CS_UNIVERSITYCODE WHERE SVYYR = ? AND VERSION = ? GROUP BY SCHLID";
		List<String> queryForList = this.jdbcTemplate.queryForList(query, new Object[]{svyyr, version}, String.class);
		return queryForList;
	}
	
	public List<String> getIndctId(String svyyr, Integer version){
		String query = "SELECT CDID FROM DGK_CS_KEYINDICATORCODE WHERE RMK = ? AND VERSION = ? GROUP BY CDID";
		List<String> queryForList = this.jdbcTemplate.queryForList(query, new Object[]{svyyr, version}, String.class);
		return queryForList;
	}
	
	public List<String> getIndctId(Integer version){
		String query = "SELECT CDID FROM DGK_CS_KEYINDICATORCODE WHERE VERSION = ? GROUP BY CDID";
		List<String> queryForList = this.jdbcTemplate.queryForList(query, new Object[]{version}, String.class);
		return queryForList;
	}
	
	public List<String> getSchlDivCd(Integer version){
		String query =  "SELECT CDID FROM DGK_CS_CODEBYTYPE  WHERE VERSION = ? GROUP BY CDID";
		List<String> queryForList = this.jdbcTemplate.queryForList(query, new Object[]{version}, String.class);
		return queryForList;
	}

	
}
