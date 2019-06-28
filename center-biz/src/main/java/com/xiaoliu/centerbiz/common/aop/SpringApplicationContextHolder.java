/**
 *
 */
package com.xiaoliu.centerbiz.common.aop;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * @author xiaoliu
 */
public class SpringApplicationContextHolder {

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringApplicationContextHolder.applicationContext = applicationContext;
    }

    /**
     * 根据Bean定义的name获取相应的Bean实例
     *
     * @param name 定义的Bean名称
     * @return
     * @change 对ApplicationContext进行了空校验
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        if (applicationContext == null) {
            return null;
        }
        return (T) applicationContext.getBean(name);
    }

    /**
     * {@link #getBean(Class, Object...)}
     */
    public static <T> T getBean(Class<T> clazz) {
        return getBean(clazz, (Object[]) null);
    }

    /**
     * 根据Bean的类型获取相应的Bean实例
     *
     * @param clazz Bean类型
     * @param args  构造参数
     * @return
     * @change 对ApplicationContext进行了空校验
     */
    public static <T> T getBean(Class<T> clazz, Object... args) {
        if (applicationContext == null) {
            return null;
        }
        return (T) applicationContext.getBean(clazz, args);
    }

}
