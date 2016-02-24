/** ******ResourceBundleUtil.java*****/
/**
 *Copyright
 *
 **/
package com.saturn.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.slf4j.Logger;
import com.saturn.util.log.Log;

/**
 * @describe: <pre>
 * 处理资源文件加载工具类
 * </pre>
 * @date :2014年2月27日 下午3:30:42
 * @author : ericcoderr@gmail.com
 */
public class ResourceBundleUtil {

    private static final Logger log = Log.getLogger();

    /**
     * 不缓存资源
     */
    private static class NoCacheResourceBundleControl extends ResourceBundle.Control {

        @Override
        public long getTimeToLive(String baseName, Locale locale) {
            return ResourceBundle.Control.TTL_DONT_CACHE;
        }
    }

    private static final NoCacheResourceBundleControl noCacheResourceBundleControl = new NoCacheResourceBundleControl();

    public static ResourceBundle reloadNoCache(String fileName) {
        try {
            return ResourceBundle.getBundle(fileName, Locale.ENGLISH, noCacheResourceBundleControl);
        } catch (MissingResourceException e) {
            log.error("Load fileName resource error,", e);
            return null;
        }
    }

    public static ResourceBundle reload(String fileName) {
        try {
            return ResourceBundle.getBundle(fileName, Locale.ENGLISH);
        } catch (MissingResourceException e) {
            log.error("Load fileName resource error,", e);
            return null;
        }
    }

    public static Object getObject(ResourceBundle rb, String key) {
        try {
            Object value = rb.getObject(key);
            return value;
        } catch (Exception e) {
        }
        return null;
    }

    public static String getString(ResourceBundle rb, String key) {
        try {
            String value = rb.getString(key);
            return value;
        } catch (Exception e) {
        }
        return null;
    }
}
