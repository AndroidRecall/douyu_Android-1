package com.mp.douyu.ui.withdraw

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mp.douyu.R
import com.mp.douyu.adapter.RuleAdapter
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.event.LiveEvent
import com.mp.douyu.singleClick
import io.reactivex.Observable
import kotlinx.android.synthetic.main.title_bar_simple.*
import kotlinx.android.synthetic.main.withdraw_activity_rule.*
import kotlinx.android.synthetic.main.withdraw_activity_rule.warningView

class RuleActivity : MBaseActivity() {
    var observable: Observable<LiveEvent>? = null
    override val contentViewLayoutId: Int
        get() = R.layout.withdraw_activity_rule

    override fun initView() {
        iftTitle.text ="规则"
        anchorViewModel.getRule(hashMapOf())
        recycler_view?.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
        ibReturn.singleClick {
            finish()
        }
        warningView.addOnRetryListener {
            warningView.hideWarning()
            anchorViewModel.getRule(hashMapOf())
        }
        anchorViewModel.getRule(hashMapOf())
    }
    val mAdapter by lazy {
        RuleAdapter({}, this).apply {

        }
    }

    companion object {
        const val LIVE_DATA = "data"
        const val INITIAL_POSITION = "position"
        fun open(context: Context?): Intent {
            return Intent(context, RuleActivity::class.java).apply {

            }
        }
    }

    private val anchorViewModel by getViewModel(AnchorViewModel::class.java) {
        _ruleList.observe(it, Observer {
            it?.let {ruleList->
                mAdapter.refresh(ruleList,null)
            }
            if (mAdapter.size == 0) {
                if (it == null) {
                    warningView.showNoNetWorkWarning()
                } else {
                    warningView.showOtherWarning(R.mipmap.ic_no_record, R.string.empty)
                }
            } else {
                warningView.hideWarning()
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()

    }

}