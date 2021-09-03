package apt.framework.common.datagokr;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonRootName;




@JsonRootName(value="response")
public class DataGoKrResponse {
	
	private Header header;
	private Body body;
	
	public ArrayList<Map<String, String>> getItems() {
		try {
			return this.body.items.item;
		} catch (Exception e) {
			return null;
		}
	}
	
	public Set<String> getColumnNames(){
		Map<String, String> map = this.getItems() != null ? this.getItems().get(0) : null;
		return this.getItems() != null? map.keySet() : null;
	}
	
	public DataGoKrResponse() {
		// TODO Auto-generated constructor stub
	}
	
	class Header {
		private String resultCode;
		private String resultMsg;
		
		public String getResultCode() {
			return resultCode;
		}
		public void setResultCode(String resultCode) {
			this.resultCode = resultCode;
		}
		public String getResultMsg() {
			return resultMsg;
		}
		public void setResultMsg(String resultMsg) {
			this.resultMsg = resultMsg;
		}
		
		public Header() {
			// TODO Auto-generated constructor stub
		}
	}
	
    @JsonIgnoreProperties(ignoreUnknown=true)
	class Body {
		
		private Items items = null;
		private String numOfRows = "100";
		private String pageNo = "1";
		private String totalCount = "0";
		
		

		class Items {
			
			private ArrayList<Map<String, String>> item;

			public ArrayList<Map<String, String>> getItem() {
				return item;
			}

			public void setItem(ArrayList<Map<String, String>> item) {
				this.item = item;
			}
			
			public Items() {
				// TODO Auto-generated constructor stub
			}
			
			
		}
		
		
		
		public Body() {
			// TODO Auto-generated constructor stub
		}

		public Items getItems() {
			return items;
		}

		public void setItems(Items items) {
			this.items = items;
		}

		public String getNumOfRows() {
			return numOfRows;
		}

		public void setNumOfRows(String numOfRows) {
			this.numOfRows = numOfRows;
		}

		public String getPageNo() {
			return pageNo;
		}

		public void setPageNo(String pageNo) {
			this.pageNo = pageNo;
		}

		public String getTotalCount() {
			return totalCount;
		}

		public void setTotalCount(String totalCount) {
			this.totalCount = totalCount;
		}			
		
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}
	
	
}
