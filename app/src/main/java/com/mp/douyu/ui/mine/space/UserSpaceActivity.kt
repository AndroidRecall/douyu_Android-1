package com.mp.douyu.ui.mine.space

import android.content.Context
import android.content.Intent
import com.mp.douyu.R
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.inTransaction

class UserSpaceActivity : MBaseActivity() {
    override val contentViewLayoutId: Int
        get() = R.layout.activity_user_sapce

    override fun initView() {
        val data = intent.getIntExtra(EXTRA_DATA,0)
        supportFragmentManager.inTransaction {
            add(R.id.container, SpaceFragment.newInstance(data))
        }
//    ActivityUtils.add(supportFragmentManager,R.id.container,SpaceFragment(),"user_space")
    }

    companion object {
        private val EXTRA_DATA = "uid"

        fun open(context: Context?, uid: Int? = 0): Intent {
            return Intent(context, UserSpaceActivity::class.java).putExtra(EXTRA_DATA, uid)
        }
    }
}