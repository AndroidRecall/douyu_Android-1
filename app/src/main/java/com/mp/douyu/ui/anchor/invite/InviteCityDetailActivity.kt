package com.mp.douyu.ui.anchor.invite

import android.content.Context
import android.content.Intent
import com.mp.douyu.R
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.event.LiveHomeEvent
import com.mp.douyu.inTransaction
import com.mp.douyu.utils.RxBus

class InviteCityDetailActivity : MBaseActivity() {
    override val contentViewLayoutId: Int
        get() = R.layout.activity_user_sapce

    override fun initView() {
        supportFragmentManager.inTransaction {

            add(R.id.container,
                InviteCityDetailFragment.newInstance(intent.getIntExtra(EXTRA_DATA, 0)))
        }
//    ActivityUtils.add(supportFragmentManager,R.id.container,SpaceFragment(),"user_space")
        RxBus.getInstance().register(LiveHomeEvent::class.java).subscribe {
            when (it.type) {
                0 -> finish()
            }
        }
    }

    companion object {
        const val EXTRA_DATA = "data"

        fun open(context: Context?, id: Int): Intent {
            return Intent(context, InviteCityDetailActivity::class.java).apply {
                putExtra(EXTRA_DATA, id)
            }
        }
    }

}