package com.xiaoliu.centerbiz.common.annotation;

import java.lang.annotation.*;

/**
 * 判断集合是否为非空值
 *
 * @author xiaoliu
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface NotEmpty {

    /**
     * 需要判断的参数名称
     *
     * @return
     */
    String[] name() default {};

    /**
     * 如果判断的条件为真后，对应每个字段的消息提示。
     * <b>注：消息的顺序和value中的字段名顺序与之对应</b>
     *
     * @return
     */
    String[] messages() default {};

    /**
     * 消息体中的变量值
     *
     * @return
     */
    String[] messageArgs() default {};

}
