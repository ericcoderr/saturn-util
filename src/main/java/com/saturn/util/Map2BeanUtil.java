/** ******Map2BeanUtil.java*****/
/**
 *Copyright
 *
 **/
package com.saturn.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @describe: <pre>
 * </pre>
 * @date :2014年6月19日 下午4:10:00
 * @author : huangth
 */
public class Map2BeanUtil {

    @SuppressWarnings("rawtypes")
    public static <T> T convertMap(Class<T> type, Map map) throws IntrospectionException, IllegalAccessException, InstantiationException, InvocationTargetException {
        BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
        T obj = type.newInstance(); // 创建 JavaBean 对象

        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();

            if (map.containsKey(propertyName)) {
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                try {
                    Object value = map.get(propertyName);
                    if (value == null) {
                        continue;
                    }
                    Object[] args = new Object[1];
                    args[0] = value;
                    descriptor.getWriteMethod().invoke(obj, args);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }
}
