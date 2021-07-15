package com.swbg.mlivestreaming.http.exception;

public class MException extends RuntimeException {

    public static final int CODE_NONE = -100;
    public static final int CODE_SKIP = -101;
    public static final String MESSAGE_NONE = "";

    private int code;
    private String error_description;

    protected MException(int code) {
        this(code, MESSAGE_NONE);
    }

    protected MException(int code, String message) {
        super(message);
        this.code = code;
        this.error_description = message;
    }

    public int getCode() {
        return code;
    }
    public String getMessage(){
        return error_description;
    }
}
