package com.xiaoliu.centerbiz.common.result;

import com.xiaoliu.centerbiz.common.model.BaseBean;

/**
 * 返回实现
 *
 * @author xiaoliu
 */
public class Result extends BaseBean implements IResult {

    public static final int CODE_OK = 1;

    private boolean success;
    private String message;
    private Object data;
    private int code;

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.code = CODE_OK;
    }

    public Result(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.code = CODE_OK;
    }

    public Result(boolean success, String message, int code) {
        this(success, message, null, code);
    }

    public Result(boolean success, String message, Object data, int code) {
        this.success = success;
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SuppressWarnings("unchecked")
    public <T> T getData() {
        return (T) data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
