package com.swbg.mlivestreaming.http;

import com.swbg.mlivestreaming.bean.HttpWrapBean;
import com.swbg.mlivestreaming.http.exception.Http400Exception;
import com.swbg.mlivestreaming.http.exception.Http401Exception;
import com.swbg.mlivestreaming.http.exception.HttpResultException;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class ResponseFunction<T> implements Function<HttpWrapBean<T>, T> {
    @NonNull
    @Override
    public T apply(@NonNull HttpWrapBean<T> tResponse) throws Exception {
        if (tResponse != null) {
            int code = tResponse.getCode();
            String message = tResponse.getMessage();
            if (code == 200) {
                return tResponse.getData();
            } else if (code == 400 || (code >= 402 && code < 500)) {

                throw new Http400Exception(code, message);
            } else if (code == 401) {
                throw new Http401Exception(code);
            } else {
                throw new HttpResultException(code, message);
            }
        }
        throw new HttpResultException(100001);
    }
}
