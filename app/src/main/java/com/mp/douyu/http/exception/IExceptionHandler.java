package com.mp.douyu.http.exception;

import android.app.Activity;

import androidx.annotation.MainThread;


public interface IExceptionHandler {
    @MainThread
    boolean handle(final Activity activity, Throwable t);
}
