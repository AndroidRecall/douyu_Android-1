package com.mp.douyu.http.exception;


public class StatusException extends WithOutRetryException {

    /**
     * 用户归属发生变化，已经不是协作版用户，Android端自定义的错误码，并非规范错误码
     */
    public static final int ERROR_NOT_COLLABORATIVE = 10001;
    /**
     *
     * 用户未加入团队，或被踢出已有团队，Android端自定义的错误码，并非规范错误码
     */
    public static final int ERROR_NO_TEAM = 10002;
    /**
     * 组项目不可变
     */
    public static final int ERROR_BARRISTER_EDIT_DISABLE = 10003;


    public StatusException(int code) {
        super(code);
    }

    public StatusException(int code, String message) {
        super(code, message);
    }
}
