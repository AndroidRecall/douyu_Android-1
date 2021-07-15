package com.swbg.mlivestreaming.utils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class RxUtils {


    public static Observable<Long> countDown(long period, final long time, TimeUnit unit){
        return Observable.interval(period, unit)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return time-aLong.intValue();
                    }
                })
                .take(time+1)
                .compose(SchedulerProvider.<Long>applySchedulers());
    }



    public static Observable<Long> delay(long time) {
        return Observable.timer(time, TimeUnit.MILLISECONDS)
                .compose(SchedulerProvider.<Long>applySchedulers());

    }

    public static Observable<Long> interval(long delay, long period) {
        return Observable.interval(delay, period, TimeUnit.SECONDS)
                .compose(SchedulerProvider.<Long>applySchedulers());

    }

    public static Observable<Long> interval(long delay, long period, TimeUnit unit) {
        return Observable.interval(delay, period, unit)
                .compose(SchedulerProvider.applySchedulers());
    }
}
