/** ******XmlUtil.java*****/
/**
 *Copyright
 *
 **/
package com.saturn.util.xml;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @describe: <pre>
 * </pre>
 * @date :2014年7月9日 下午12:00:53
 * @author : ericcoderr@gmail.com
 */
public class XmlUtil {

    public static final XmlMapper DEFAULT_XML_MAPPER = new XmlMapper();

    public static String getNodeText(String xml, String fieldName) throws JsonProcessingException, IOException {
        JsonNode node = DEFAULT_XML_MAPPER.readTree(xml);
        String value = node.get(fieldName).textValue();
        return value == null ? "" : value;
    }

    //TODO 读取数组有问题，类似<values><value>1</value><value>2</value><values>,只能生成数组最后一个元素
    public static Map<String, String> xml2Map(String xmlString) throws Exception {
        JsonNode node = XmlUtil.DEFAULT_XML_MAPPER.readTree(xmlString);
        Iterator<Entry<String, JsonNode>> iter = node.fields();
        Map<String, String> map = new HashMap<String, String>();
        while (iter.hasNext()) {
            Entry<String, JsonNode> entry = iter.next();
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue().textValue();
            map.put(fieldName, fieldValue);
            map.put(fieldName, node.asText());
        }
        return map;
    }
}
