package com.xiaoliu.centerbiz.common.result;

import java.io.Serializable;

/**
 * 返回对象接口
 *
 * @author xiaoliu
 */
public interface IResult extends Serializable {

    /**
     * 是否成功
     *
     * @return 是否成功
     */
    boolean isSuccess();

    /**
     * 提示消息
     *
     * @return 提示消息
     */
    String getMessage();

    /**
     * 返回数据
     *
     * @param <T> 返回数据类型
     *
     * @return 数据
     */
    <T> T getData();

    /**
     * 返回代码
     *
     * @return 代码
     */
    int getCode();

}
