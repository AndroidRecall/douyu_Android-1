package com.mp.douyu.http.exception;

public class HttpResultException extends WithOutRetryException {

    public HttpResultException(int code) {
        super(code);
    }

    public HttpResultException(int code, String message) {
        super(code, message);
    }
}