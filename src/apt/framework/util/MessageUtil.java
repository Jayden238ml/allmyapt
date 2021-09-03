package apt.framework.util;

import java.util.Locale;

import org.springframework.context.support.MessageSourceAccessor;

public class MessageUtil {
	/**
     * MessageSourceAccessor
     */
    private static MessageSourceAccessor msAcc = null;
    
    public void setMessageSourceAccessor(MessageSourceAccessor msAcc) {
    	MessageUtil.msAcc = msAcc;
    }
    
    /**
     * KEY?— ?•´?‹¹?•˜?Š” ë©”ì„¸ì§? ë°˜í™˜
     * @param key
     * @return
     */
    public static String getMessage(String key) {
        return msAcc.getMessage(key, Locale.getDefault());
    }
    
    /**
     * KEY?— ?•´?‹¹?•˜?Š” ë©”ì„¸ì§? ë°˜í™˜
     * @param key
     * @param objs
     * @return
     */
    public static String getMessage(String key, Object[] objs) {
        return msAcc.getMessage(key, objs, Locale.getDefault());
    }
}
