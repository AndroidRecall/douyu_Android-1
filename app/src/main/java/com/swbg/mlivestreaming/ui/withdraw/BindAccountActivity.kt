package com.swbg.mlivestreaming.ui.withdraw

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.swbg.mlivestreaming.BuildConfig
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.event.LiveEvent
import com.swbg.mlivestreaming.singleClick
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_bind_account.*

class BindAccountActivity : MBaseActivity() {
    var observable: Observable<LiveEvent>? = null
    var type: Int = 0
    override val contentViewLayoutId: Int
        get() = R.layout.activity_bind_account

    override fun initView() {
        type = intent.getIntExtra(INTENT_DATA,0)
        if (type == 0) {
            ll_bank.visibility = View.VISIBLE
            ll_alipay.visibility = View.GONE
        } else {
            ll_bank.visibility = View.GONE
            ll_alipay.visibility = View.VISIBLE
        }
        anchorViewModel.getAnchorInfo(hashMapOf())
        ibReturn.singleClick {
            finishView()
        }
        tv_bind.singleClick {
            if (type != 1) {
                val name = et_name.text.toString()
                val bank = et_bank.text.toString()
                val card = et_card.text.toString()
                val accountBank = et_account_bank.text.toString()
                if (name.isBlank()) {
                    ToastUtils.showShort("请输入姓名")
                    return@singleClick
                }
                if (bank.isBlank()) {
                    ToastUtils.showShort("请输入银行")
                    return@singleClick
                }
                if (card.isBlank()) {
                    ToastUtils.showShort("请输入卡号")
                    return@singleClick
                }
                if (accountBank.isBlank()) {
                    ToastUtils.showShort("请输入开户行")
                    return@singleClick
                }

                anchorViewModel.addAccount(hashMapOf("name" to "${name}",
                    "bank_card" to "${card}",
                    "bank_name" to "${bank}","type" to "${type}"))
            } else {
                val name = et_ali_name.text.toString()
                val alipay = et_alipay_account.text.toString()
                if (name.isBlank()) {
                    ToastUtils.showShort("请输入姓名")
                    return@singleClick
                }
                if (alipay.isBlank()) {
                    ToastUtils.showShort("请输入支付宝账号")
                    return@singleClick
                }
                anchorViewModel.addAccount(hashMapOf("alipay_account" to "${alipay}",
                    "alipay_name" to "${name}","type" to  "${type}"

                ))
            }

        }
    }


    companion object {
        const val INTENT_DATA = "data"
        const val INITIAL_POSITION = "position"
        fun open(context: Context?,type:Int?=0): Intent {
            return Intent(context, BindAccountActivity::class.java).apply {
            putExtra(INTENT_DATA,type)
            }
        }
    }

    private val anchorViewModel by getViewModel(AnchorViewModel::class.java) {
        _addAccountResult.observe(it, Observer {accountRes->
            accountRes?.let {
                finishView()
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()

    }

}