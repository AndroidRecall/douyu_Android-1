package com.mp.douyu.ui.video

import android.content.Context
import android.content.Intent
import com.mp.douyu.R
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.bean.ShortVideoBean
import com.mp.douyu.inTransaction

class ShortVideoActivity : MBaseActivity() {
    var fragment: VideoFragment? = null
    override val contentViewLayoutId: Int
        get() = R.layout.activity_user_sapce

    override fun initView() {
        val data = intent.getParcelableArrayListExtra<ShortVideoBean>(VIDEO_DATA)
        val position = intent.getIntExtra(INITIAL_POSITION, 0)
        fragment = VideoFragment()
        supportFragmentManager.inTransaction {
            add(R.id.container, fragment!!)
        }
    }

    companion object {
        const val VIDEO_DATA = "data"
        const val INITIAL_POSITION = "position"
        fun open(context: Context?): Intent {
            return Intent(context,
                ShortVideoActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).apply {

            }
        }
    }

}