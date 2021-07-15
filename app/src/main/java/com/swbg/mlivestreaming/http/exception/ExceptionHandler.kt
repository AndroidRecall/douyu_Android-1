package com.swbg.mlivestreaming.http.exception

import android.app.Activity


object ExceptionHandler : IExceptionHandler {
    private var handlers = arrayListOf(
            PermissionExceptionHandler(),
            ProjectDeleteExceptionHandler(),
            SocketExceptionHandler(),
            DefaultExceptionHandler())

    override fun handle(activity: Activity, t: Throwable?): Boolean {
        return handlers.any { it.handle(activity, t) }
    }
}