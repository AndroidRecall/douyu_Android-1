package com.swbg.mlivestreaming.ui.mine.apply

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.ui.mine.MineViewModel
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