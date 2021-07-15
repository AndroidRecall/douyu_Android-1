package com.swbg.mlivestreaming.ui.home

import android.util.Log
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.VisibilityFragment
import com.swbg.mlivestreaming.event.TabShortVideoEvent
import com.swbg.mlivestreaming.ui.video.ShortVideoActivity
import com.swbg.mlivestreaming.utils.RxBus

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