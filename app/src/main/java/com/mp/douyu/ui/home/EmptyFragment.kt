package com.mp.douyu.ui.home

import android.util.Log
import com.mp.douyu.R
import com.mp.douyu.base.VisibilityFragment

class EmptyFragment: VisibilityFragment() {
    override val layoutId: Int
        get() = R.layout.empty_fragmnet

    override fun initView() {

    }

    override fun onVisible() {
        super.onVisible()
        Log.e(TAG,"EmptyFragment onVisible")
//        startActivityWithTransition(ShortVideoActivity.open(context))
//        RxBus.getInstance().post(TabShortVideoEvent())
    }

    override fun onInvisible() {
        super.onInvisible()
//        RxBus.getInstance().post(TabShortVideoEvent())
    }
}