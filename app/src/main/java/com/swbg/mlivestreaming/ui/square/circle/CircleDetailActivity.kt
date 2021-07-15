package com.swbg.mlivestreaming.ui.square.circle

import android.content.Context
import android.content.Intent
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.bean.SquareCircleBean
import com.swbg.mlivestreaming.inTransaction

/**
 * 圈子详情
 */
class CircleDetailActivity:MBaseActivity() {
    override val contentViewLayoutId: Int
        get() = R.layout.activity_user_sapce

    override fun initView() {
        val data = intent.getParcelableExtra<SquareCircleBean>(CIRCLE_DATA)
        supportFragmentManager.inTransaction {
            add(R.id.container, CircleDetailFragment.newInstance(data))
        }
//    ActivityUtils.add(supportFragmentManager,R.id.container,SpaceFragment(),"user_space")
    }
    companion object {
        const val CIRCLE_DATA = "circle_data"
        fun open(context:Context?, bean: SquareCircleBean):Intent{
            return Intent(context,CircleDetailActivity::class.java).apply {
                putExtra(CIRCLE_DATA,bean)

            }
        }
    }
}