package com.itmayiedu.common.util;

/**
 * @Auther: Administrator
 * @Date: 2018/10/24 09:33
 * @Description:
 */
public class JsonMessage {
    private boolean success;
    private String message;
    private Object object;

    public JsonMessage() {
    }

    public JsonMessage(boolean success, String message, Object object) {
        this.success = success;
        this.message = message;
        this.object = object;
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

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "JsonMessage{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", object=" + object +
                '}';
    }
}
