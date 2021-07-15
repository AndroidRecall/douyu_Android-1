package com.mp.douyu.ui.square.circle

import android.content.Context
import android.content.Intent
import com.mp.douyu.R
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.singleClick
import kotlinx.android.synthetic.main.square_activity_circle_explain.*
import kotlinx.android.synthetic.main.title_bar_simple.*

/**
 * 圈子描述
 */
class CircleExplainActivity : MBaseActivity() {
    override val contentViewLayoutId: Int
        get() = R.layout.square_activity_circle_explain

    override fun initView() {
        var explain = intent.getStringExtra(CIRCLE_DATA)
        tv_content.text = explain
        iftTitle.text="圈规说明"
        ibReturn.singleClick {
            finish()
        }
    }

    companion object {
        const val CIRCLE_DATA = "circle_explain_data"
        fun open(context: Context?, data: String): Intent {
            return Intent(context, CircleExplainActivity::class.java).apply {
                putExtra(CIRCLE_DATA, data)

            }
        }
    }
}