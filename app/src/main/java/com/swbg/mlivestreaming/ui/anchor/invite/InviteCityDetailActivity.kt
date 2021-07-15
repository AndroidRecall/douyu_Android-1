package com.swbg.mlivestreaming.ui.anchor.invite

import android.content.Context
import android.content.Intent
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.event.LiveHomeEvent
import com.swbg.mlivestreaming.inTransaction
import com.swbg.mlivestreaming.utils.RxBus

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