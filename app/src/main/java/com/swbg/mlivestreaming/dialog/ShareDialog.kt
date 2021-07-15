package com.swbg.mlivestreaming.dialog

import android.content.Context
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.hadcn.keyboard.ChatKeyboardLayout
import com.lxj.xpopup.util.XPopupUtils
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.adapter.CommentVideoAdapter
import com.swbg.mlivestreaming.ui.video.comment.CommentViewModel
import kotlinx.android.synthetic.main.fragment_video_comment2.view.*

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