package com.swbg.mlivestreaming.http;


import com.swbg.mlivestreaming.http.exception.HttpResultException;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import retrofit2.Response;


/**
 *
 * Created by HT on 2017/5/10.
 */

public class HttpBodyFun<T> implements Function<Response<T>, T> {


    @Override
    public T apply(@NonNull Response<T> tResponse) throws Exception {
        if (tResponse.code() != 200) {
            throw new HttpResultException(tResponse.code());
        }
        return tResponse.body();
    }

}
