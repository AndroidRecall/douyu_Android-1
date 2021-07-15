package com.swbg.mlivestreaming.http;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import retrofit2.Response;

public class HttpCodeBooleanFun implements Function<Response, Boolean> {
    @Override
    public Boolean apply(@NonNull Response response) {
        int code = response.code();
        return code >= 200 && code < 300;
    }
}
