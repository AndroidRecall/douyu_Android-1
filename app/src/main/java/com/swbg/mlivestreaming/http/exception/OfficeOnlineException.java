package com.swbg.mlivestreaming.http.exception;


/**
 * Office Online相关异常
 */
public class OfficeOnlineException extends MException {

    public static final int ERROR_OFFICE_ONLINE_LOGIN = 10001;
    public static final int ERROR_OFFICE_ONLINE_CREATE = 10002;

    public OfficeOnlineException(int code) {
        super(code);
    }

    public OfficeOnlineException(int code, String message) {
        super(code, message);
    }
}
