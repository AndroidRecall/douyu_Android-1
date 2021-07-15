package com.mp.douyu.http.exception;


public class Http400Exception extends HttpResultException {

    public static final int ERROR_PROJECT_NOT_EXIT = 90009;

    private String rawMessage;
    private String message;

    public Http400Exception(int code) {
        super(code);
    }

    public Http400Exception(int code, String message) {
        super(code, message);
    }

    public Http400Exception(int code, String message, String rawMessage) {
        super(code, message);
        this.message = message;
        this.rawMessage = rawMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    public String getErrorMessage() {
        return message;
    }
}
