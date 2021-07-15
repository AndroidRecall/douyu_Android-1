package com.swbg.mlivestreaming.base

import github.leavesc.reactivehttp.base.BaseReactiveViewModel

open class MBaseViewModel : BaseReactiveViewModel() {
    protected open val remoteDataSource by lazy {
//        SelfRemoteDataSource(this)
    }

}