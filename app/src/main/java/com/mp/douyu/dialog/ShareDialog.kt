package com.mp.douyu.dialog

import android.content.Context
import com.lxj.xpopup.util.XPopupUtils
import com.mp.douyu.R

/**
 * 分享
 */
class ShareDialog(context: Context,var videoId: Int? = 0):BaseBottomPopupView(context) {
    private val pageSize = 10
    private var currentPage = 1

    override fun onCreate() {
        super.onCreate()
        initView()
    }

    private fun initView() {

    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_video_share
    }

    override fun getMaxHeight(): Int {
        return (XPopupUtils.getScreenHeight(context) * .45f).toInt()
    }

}