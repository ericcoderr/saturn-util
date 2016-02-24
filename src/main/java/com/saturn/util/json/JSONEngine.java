package com.saturn.util.json;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.slf4j.Logger;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.saturn.util.ReflectConventionUtil;
import com.saturn.util.log.Log;

/**
 * 配置JSONUtil工作时所使用的两种引擎，JACKSON Engineer优先考虑，如果加载失败就是用自己写的引擎
 */
public class JSONEngine {

    private static final Logger log = Log.getLogger();
    public static final ObjectMapper DEFAULT_JACKSON_MAPPER;
    public static final ObjectMapper PRETTY_JACKSON_MAPPER;// 打印人更易阅读的Json串

    /**
     * 标记JACKSON的类库是否加载成功
     */
    public static final boolean JACKSON_ENABLE = ReflectConventionUtil.isClassFound("com.fasterxml.jackson.databind.ObjectMapper", "com.fasterxml.jackson.core.JsonGenerator");
    static {
        if (JACKSON_ENABLE) {
            log.error("JSONUtil Using Jackson Engine...");
            JsonFactory jf = new JsonFactory();
            jf.enable(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER);// 放宽jsonParser的限制
            jf.enable(JsonParser.Feature.ALLOW_COMMENTS);
            jf.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
            jf.enable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
            jf.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);

            DateFormat defaultDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DEFAULT_JACKSON_MAPPER = new ObjectMapper(jf);
            DEFAULT_JACKSON_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            DEFAULT_JACKSON_MAPPER.setDateFormat(defaultDF);

            // 忽略JSON字符串中存在而Java对象实际没有的属性
            DEFAULT_JACKSON_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            PRETTY_JACKSON_MAPPER = new ObjectMapper(jf);
            //
            PRETTY_JACKSON_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            PRETTY_JACKSON_MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true);
            PRETTY_JACKSON_MAPPER.setDateFormat(defaultDF);
            //
            // // 忽略JSON字符串中存在而Java对象实际没有的属性
            PRETTY_JACKSON_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        } else {
            DEFAULT_JACKSON_MAPPER = PRETTY_JACKSON_MAPPER = null;
            log.error("JSONUtil Using InternalJSON Engine...");
        }
    }
}
