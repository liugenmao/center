package com.xiaoliu.center.common.model;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础bean
 *
 * @author xiaoliu
 */
public class BaseBean implements Serializable {

    /**
     * 将bean转换为key value 表示的map
     *
     * @return Map
     */
    public Map<String, Object> toMap() {
        Class<?> type = this.getClass();
        Map<String, Object> returnMap = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    returnMap.put(propertyName, readMethod.invoke(this, new Object[0]));
                }
            }
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return returnMap;
    }
}
