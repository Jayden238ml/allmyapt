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
     * KEY? ?΄?Ή?? λ©μΈμ§? λ°ν
     * @param key
     * @return
     */
    public static String getMessage(String key) {
        return msAcc.getMessage(key, Locale.getDefault());
    }
    
    /**
     * KEY? ?΄?Ή?? λ©μΈμ§? λ°ν
     * @param key
     * @param objs
     * @return
     */
    public static String getMessage(String key, Object[] objs) {
        return msAcc.getMessage(key, objs, Locale.getDefault());
    }
}
