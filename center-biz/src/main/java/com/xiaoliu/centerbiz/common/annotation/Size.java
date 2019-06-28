package com.xiaoliu.centerbiz.common.annotation;

import java.lang.annotation.*;

/**
 * 允许数值的范围
 *
 * @author xiaoliu
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Size {

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
     * @return 设置数字的最小值
     */
    float[] min() default 0.0f;

    /**
     * @return 设置当前数字的最大值
     */
    float[] max() default Float.MAX_VALUE;

    /**
     * 消息体中的变量值
     *
     * @return
     */
    String[] messageArgs() default {};

}
