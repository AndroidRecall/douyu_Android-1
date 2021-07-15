package com.mp.douyu.ui.mine.apply

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.mp.douyu.R
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.event.RefreshInfo
import com.mp.douyu.singleClick
import com.mp.douyu.ui.mine.MineViewModel
import com.mp.douyu.utils.RxBus
import kotlinx.android.synthetic.main.mine_activity_apply_anchor.*
import kotlinx.android.synthetic.main.title_bar_simple.*

class AnchorApplyActivity : MBaseActivity() {
    override val contentViewLayoutId: Int
        get() = R.layout.mine_activity_apply_anchor

    override fun initView() {
        var type  = intent.getStringExtra(INTENT_DATA)
        iftTitle.text ="申请成为主播"
        if (type.equals("3")) {
            //未申请
            ll_apply.visibility = View.VISIBLE
            ll_check.visibility = View.GONE
        }else if (type.equals("1")) {
            //审核失败
            ll_apply.visibility = View.GONE
            ll_check.visibility = View.VISIBLE
            iv_state.setImageResource(R.mipmap.ic_check_fail)
            tv_state.text = "审核失败"
            tv_state.setTextColor(resources.getColor(R.color.colorF60F4B))
        }else {
            //审核中
            ll_apply.visibility = View.GONE
            ll_check.visibility = View.VISIBLE
            iv_state.setImageResource(R.mipmap.ic_checking)
            tv_state.text = "审核中..."
            tv_state.setTextColor(resources.getColor(R.color.text_all_btn_color))
        }

        et_remark.addTextChangedListener {
            tv_remark_num.text = "${it?.length}/300"
        }
        initClickListener()

    }

    private fun initClickListener() {
        tv_submit.singleClick {
            if (et_name.text.isNullOrEmpty()) {
                ToastUtils.showShort("姓名不能为空")
                return@singleClick
            }
            if (et_phone.text.isNullOrEmpty()) {
                ToastUtils.showShort("手机号不能为空")
                return@singleClick
            }
            if (et_wx.text.isNullOrEmpty()) {
                ToastUtils.showShort("qq不能为空")
                return@singleClick
            }
//            if (et_email.text.isNullOrEmpty()) {
//                ToastUtils.showShort("邮箱不能为空")
//                return@singleClick
//            }
//            if (et_remark.text.isNullOrEmpty()) {
//                ToastUtils.showShort("备注不能为空")
//                return@singleClick
//            }
            mineViewModel.applyAnchor(hashMapOf(
                "name" to "${et_name.text}",
                "mobile" to "${et_phone.text}",
                "qq" to "${et_wx.text}",
                "email" to "${et_email.text}",
                "comment" to "${et_remark.text}"))
        }
        ibReturn.singleClick {
            finish()
        }

    }

    companion object {
        const val INTENT_DATA = "type"

        fun open(context: Context?, state: String): Intent {
            return Intent(context, AnchorApplyActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(INTENT_DATA,state)
        }
    }
    private val mineViewModel by   getViewModel(MineViewModel::class.java){
        _applyAnchorResult.observe(it, Observer {it
            it?.let {
                RxBus.getInstance().post(RefreshInfo())
               startActivity(AnchorApplyNextActivity.open(this@AnchorApplyActivity))
                finishView()
            }
        })
    }
}