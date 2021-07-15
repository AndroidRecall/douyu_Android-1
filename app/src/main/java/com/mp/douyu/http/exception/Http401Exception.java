package com.mp.douyu.http.exception;


public class Http401Exception extends HttpResultException {

    /**
     * 客户端信息无效，重新登录
     */
    public static final int ERROR_CLIENT = 90000;


    public Http401Exception(int code) {
        super(code);
    }

    private String message = "";
    private String responseBody;

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getErrorMessage() {
        return message;
    }

    public Http401Exception(int code, String message,String responseBody) {
        super(code, message);
        this.message = message;
        this.responseBody = responseBody;
    }
}
