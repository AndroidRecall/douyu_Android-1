package com.swbg.mlivestreaming.http;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;


public class SchedulersTransformer<T> implements FlowableTransformer<T, T> {
    @Override
    public Publisher<T> apply(@NonNull Flowable<T> upstream) {
        return  upstream.
                subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
