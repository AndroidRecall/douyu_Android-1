package com.mp.douyu.http;

import com.google.gson.JsonSyntaxException;
import com.mp.douyu.http.exception.WithOutRetryException;

import org.reactivestreams.Publisher;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 *
 * Created by HT on 2017/5/24.
 */

public class RetryFilterFun implements Function<Flowable<? extends Throwable>, Flowable<?>> {

    private final int maxRetryTime;
    private final int delay;
    private int retryCount;


    public RetryFilterFun() {
        this(3, 0);
    }


    public RetryFilterFun(int maxRetryTime, int delay) {
        this.maxRetryTime = maxRetryTime;
        this.delay = delay;
    }
    @Override
    public Flowable<?> apply(@NonNull final Flowable<? extends Throwable> flowable) {
        return flowable.flatMap((Function<Throwable, Publisher<?>>) throwable -> {
            if (++retryCount <= maxRetryTime && !(throwable instanceof WithOutRetryException)
                    && !(throwable instanceof SocketTimeoutException)
                    && !(throwable instanceof JsonSyntaxException)
                    && !(throwable instanceof UnknownHostException)
                    && !(throwable instanceof SocketException)
                    && !(throwable instanceof UnknownServiceException)) {
                return Flowable.timer(delay, TimeUnit.SECONDS);
            }
            return Flowable.error(throwable);
        });
    }
}
