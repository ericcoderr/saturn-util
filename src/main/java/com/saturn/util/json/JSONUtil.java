package com.saturn.util.json;

import java.io.IOException;
import org.slf4j.Logger;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saturn.util.StringHelper;
import com.saturn.util.log.Log;

/**
 * 将对象转换成JSON格式的工具类，如果JACKSON Engineer加载成功就用其直接转换，否则使用JSONEncoder转换
 */
public class JSONUtil {

    private static final Logger log = Log.getLogger();

    /**
     * 将Object类型的对象转换成JSON
     */
    public static String fromObject(Object obj) {
        return fromObject(obj, JSONEngine.JACKSON_ENABLE);
    }

    /**
     * 将Object类型的对象转换成JSON，可是设置所使用的引擎
     * 
     * @param obj 待转换的对象
     * @param usingJackson 是否使用JACKSON引擎
     * @return
     */
    public static String fromObject(Object obj, boolean usingJackson) {
        if (usingJackson) {
            return fromObject(obj, JSONEngine.DEFAULT_JACKSON_MAPPER);
        }
        return JSONEncoder.encode(obj);
    }

    public static String fromObject(Object obj, ObjectMapper objectMapper) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            StringBuilder stack = StringHelper.printStackTraceSimple();
            log.error("parse type {} failed, stack {}", obj.getClass(), stack, e);
        }
        return "{}";
    }

    public static String fromObjectPretty(Object obj) {
        return fromObject(obj, JSONEngine.PRETTY_JACKSON_MAPPER);
    }

    /**
     * 字符串转对象
     * 
     * @param str
     * @param t
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static <T> T parseString2Obj(String str, Class<T> t) throws JsonParseException, JsonMappingException, IOException {
        return JSONEngine.DEFAULT_JACKSON_MAPPER.readValue(str, t);
    }

    public static <T> T parseString2Obj(String str, TypeReference<T> t) throws JsonParseException, JsonMappingException, IOException {
        return JSONEngine.DEFAULT_JACKSON_MAPPER.readValue(str, t);
    }
}
