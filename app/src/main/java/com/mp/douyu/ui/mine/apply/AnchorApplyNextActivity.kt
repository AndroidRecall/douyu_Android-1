package com.mp.douyu.ui.mine.apply

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import com.mp.douyu.R
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.singleClick
import com.mp.douyu.ui.mine.MineViewModel
import kotlinx.android.synthetic.main.mine_activity_apply_anchor_next.*

class AnchorApplyNextActivity : MBaseActivity() {
    override val contentViewLayoutId: Int
        get() = R.layout.mine_activity_apply_anchor_next

    override fun initView() {

        initClickListener()

    }

    private fun initClickListener() {

        ibReturn.singleClick {
            finish()
        }

    }

    companion object {
        const val INTENT_DATA = "type"

        fun open(context: Context?): Intent {
            return Intent(context, AnchorApplyNextActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                .putExtra(INTENT_DATA,state)
        }
    }
    private val mineViewModel by   getViewModel(MineViewModel::class.java){
        _applyAnchorResult.observe(it, Observer {it
            it?.let {
                finish()
            }
        })
    }
}