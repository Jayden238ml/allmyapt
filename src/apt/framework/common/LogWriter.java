package apt.framework.common;
import org.apache.log4j.Logger;

public class LogWriter {
	private static Logger logger;
	
	public static Logger getLogger(Class<? extends Object> Object){
		
		if (logger == null) {
			logger = Logger.getLogger(Object);
		}
		return logger;
	}
}
