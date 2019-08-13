package com.xiaoliu.center.common.result;

/**
 * 返回对象实现
 *
 * @author xiaoliu
 */
public class Result implements IResult {

    /**
     * 默认编码
     */
    private static final int CODE_OK = 1;

    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 返回消息
     */
    private String message;
    /**
     * 返回数据
     */
    private Object data;
    /**
     * 返回编码
     */
    private int code;

    /**
     * 私有带参构造器
     *
     * @param success 是否成功
     * @param message 消息
     * @param code    编码
     */
    private Result(boolean success, String message, int code) {
        this.success = success;
        this.message = message;
        this.data = null;
        this.code = code;
    }

    /**
     * 私有带参构造器
     *
     * @param success 是否成功
     * @param message 消息
     * @param data    数据
     * @param code    编码
     */
    private Result(boolean success, String message, Object data, int code) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.code = code;
    }

    /**
     * 成功
     *
     * @param message 成功消息
     * @return 成功Result
     */
    public static Result success(String message) {
        return new Result(true, message, CODE_OK);
    }

    /**
     * 成功
     *
     * @param message 成功消息
     * @param data    成功数据
     * @return 成功Result
     */
    public static Result success(String message, Object data) {
        return new Result(true, message, data, CODE_OK);
    }

    /**
     * 成功
     *
     * @param message 成功消息
     * @param code    成功编码
     * @return 成功Result
     */
    public static Result success(String message, int code) {
        return new Result(true, message, code);
    }

    /**
     * 成功
     *
     * @param message 成功消息
     * @param data    成功数据
     * @param code    成功编码
     * @return 成功Result
     */
    public static Result success(String message, Object data, int code) {
        return new Result(true, message, data, code);
    }

    /**
     * 失败
     *
     * @param message 失败消息消息
     * @return 失败Result
     */
    public static Result failed(String message) {
        return new Result(false, message, CODE_OK);
    }

    /**
     * 失败
     *
     * @param message 失败消息消息
     * @param data    成功数据
     * @return 失败Result
     */
    public static Result failed(String message, Object data) {
        return new Result(false, message, data, CODE_OK);
    }

    /**
     * 失败
     *
     * @param message 失败消息消息
     * @param code    成功编码
     * @return 失败Result
     */
    public static Result failed(String message, int code) {
        return new Result(false, message, code);
    }

    /**
     * 失败
     *
     * @param message 失败消息消息
     * @param data    成功数据
     * @param code    成功编码
     * @return 失败Result
     */
    public static Result failed(String message, Object data, int code) {
        return new Result(false, message, data, code);
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getData() {
        return (T) data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
