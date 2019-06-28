package com.xiaoliu.centerbiz.common.annotation;

import java.lang.annotation.*;

/**
 * 判断字符串的的长度
 *
 * @author xiaoliu
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Length {

    /**
     * 需要判断的参数名称
     *
     * @return
     */
    String[] name() default {};

    /**
     * 如果判断的条件为真后，对应每个字段的消息提示。
     * <p>Note：消息的顺序和value中的字段名顺序与之对应</p>
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

    /**
     * @return 设置当前字符串的最小长度，必须小于max
     */
    int[] min() default 0;

    /**
     * @return 当前字符串的最大长度，必须大于min
     */
    int[] max() default Integer.MAX_VALUE;

}
