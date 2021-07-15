package com.mp.douyu.ui.square.comment

import android.content.Context
import android.content.Intent
import com.mp.douyu.R
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.inTransaction
import com.mp.douyu.ui.square.comment.PublishImageFragment.Companion.TYPE_PUBLISH_POST

class PublishActivity:MBaseActivity() {
    var fragment:PublishImageFragment? =null
    override val contentViewLayoutId: Int
        get() = R.layout.activity_user_sapce

    override fun initView() {
        val type = intent.getIntExtra(PUBLISH_TYPE, TYPE_PUBLISH_POST)
        val circleId = intent.getIntExtra(PUBLISH_CIRCLE, -1)
        fragment = PublishImageFragment.newInstance(type,circleId)
        supportFragmentManager.inTransaction {
            add(R.id.container, fragment!!)
        }
    }
    companion object {
        const val PUBLISH_TYPE = "type"
        const val PUBLISH_CIRCLE = "circle_id"
        fun open(context:Context?,type: Int = TYPE_PUBLISH_POST,circleId:Int? = -1):Intent{
            return Intent(context,PublishActivity::class.java)
                .putExtra(PUBLISH_TYPE,type)
                .putExtra(PUBLISH_CIRCLE,circleId)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            // 结果回调
            fragment?.chooseCallback(data)
        }
    }
}