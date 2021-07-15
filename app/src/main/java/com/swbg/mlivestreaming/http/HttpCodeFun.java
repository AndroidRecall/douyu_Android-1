package com.swbg.mlivestreaming.http;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import retrofit2.Response;

/**
 * Created by HT on 2017/5/10.
 */

public class HttpCodeFun implements Function<Response, Integer> {
    @Override
    public Integer apply(@NonNull Response response) throws Exception {
        return response.code();
    }
}
