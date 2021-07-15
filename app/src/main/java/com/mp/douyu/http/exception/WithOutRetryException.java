package com.mp.douyu.http.exception;


public class WithOutRetryException extends MException {


    protected WithOutRetryException(int code) {
        super(code);
    }

    protected WithOutRetryException(int code, String message) {
        super(code, message);
    }
}
