package com.xiaoliu.centerbiz.common.exception;

/**
 * 程序业务逻辑异常, 主要用于Service和DAO层
 *
 * @author xiaoliu
 */
public class LogicException extends Exception {

    public LogicException(String message) {
        super(message);
    }

}
