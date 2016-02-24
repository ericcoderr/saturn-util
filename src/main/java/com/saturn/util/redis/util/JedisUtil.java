/** filename:JedisUtil.java */
package com.saturn.util.redis.util;

import com.saturn.util.StringTools;
import com.saturn.util.redis.core.JedisTemplate;


/**
 * <pre>
 * jedis 工具类
 * </pre>
 * 
 * @author : ericcoderr@gmail.com
 * @date : 2012-9-6
 */

public class JedisUtil {

    public static final String[] strArray = new String[0];

    public static final String DEFAULT_SEPARATOR = "_";

    public static String concatKey(String prefix, Object... values) {
        StringBuilder sb = new StringBuilder(prefix);
        for (Object o : values) {
            if (StringTools.isEmpty(o.toString())) {
                continue;
            }
            sb.append(":").append(o);
        }
        return sb.toString();
    }

    public static void rnameAndExpireRedisKey(JedisTemplate jedisTemplate, String oldKey, String newKey, int redisKeyExpire) {
        // rnamekey 如果oldKey不存在会报错
        synchronized (oldKey.intern()) {
            if (jedisTemplate.exists(oldKey)) {
                jedisTemplate.rename(oldKey, newKey);
            }
            if (jedisTemplate.exists(newKey)) {
                jedisTemplate.expire(newKey, redisKeyExpire);
            }
        }
    }

    public static String cutChannelPrefix(String channel, String separator) {
        return channel.substring(0, channel.indexOf(separator));
    }

    public static String cutChannelPrefix(String channel) {
        return cutChannelPrefix(channel, DEFAULT_SEPARATOR);
    }

    public static String cutChannelSuffix(String channel, String separator) {
        return channel.substring(channel.indexOf(separator) + 1, channel.length());
    }

    public static String cutChannelSuffix(String channel) {
        return cutChannelSuffix(channel, DEFAULT_SEPARATOR);
    }

}
