package com.swbg.mlivestreaming.ui.home.play

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.matchWaterDropScreen
import com.swbg.mlivestreaming.provider.StoredUserSources
import com.swbg.mlivestreaming.setActivityImmersion
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.ui.home.HomeViewModel
import com.swbg.mlivestreaming.utils.ActivityUtils
import com.swbg.mlivestreaming.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_transfer_account.*
import kotlinx.android.synthetic.main.title_bar_simple.*

class TransferAccountActivity : MBaseActivity() {
    var mileWalletAmount : String? = "0"
    override val contentViewLayoutId: Int
        get() = R.layout.activity_transfer_account

    @SuppressLint("SetTextI18n")
    override fun initView() {
        matchWaterDropScreen(this)
        //设置沉浸式
        setActivityImmersion(this)

        titleBar.setBackgroundColor(ContextCompat.getColor(this, R.color.color00000000))
        Glide.with(this).load(R.mipmap.return_back_white).centerInside().into(ibReturn)
        ibReturn.singleClick {
            finishView()
        }
        iftTitle.apply {
            text = "转账"
            setTextColor(ContextCompat.getColor(this@TransferAccountActivity,R.color.white))
        }

        val settingData = StoredUserSources.getSettingData()
        tv_service_center.singleClick {
            settingData?.contact?.let {
                ActivityUtils.jumpToWebView(it, this)
            }
        }

        iv_btn_confirm.singleClick {
            if (et_input_money.text?.trim().toString().isEmpty()) return@singleClick
            homeViewModel.beginTransfer(hashMapOf("amount" to et_input_money.text.toString()
                .trim()))
        }
        et_input_money.addTextChangedListener {
            if (it.toString().isEmpty()) return@addTextChangedListener
            tv_money.text = "¥${it.toString().trim()}"
            if (it.toString().toDouble() < 1) {
                iv_btn_confirm.isEnabled = false
                iv_btn_confirm.setBackgroundResource(R.mipmap.transfer_confirm_btn)
                tv_money.setBackgroundResource(R.mipmap.transfer_confirm_btn_n)
            } else {
                iv_btn_confirm.isEnabled = true
                iv_btn_confirm.setBackgroundResource(R.mipmap.transfer_confirm_btn_select)
                tv_money.setBackgroundResource(R.mipmap.transfer_money_bg)
            }
        }
    /*    et_input_money.addTextChangedListener {
            if (it.toString().isNullOrEmpty()) return@addTextChangedListener
            if (it.toString().toDouble() < getMoney.toDouble() || it.toString().toDouble() > mileWalletAmount?.toDouble()!!) {
                iv_btn_confirm.isEnabled = false
                iv_btn_confirm.setBackgroundResource(R.mipmap.transfer_confirm_btn)
                tv_money.setBackgroundResource(R.mipmap.transfer_confirm_btn_n)
            } else {
                iv_btn_confirm.isEnabled = true
                iv_btn_confirm.setBackgroundResource(R.mipmap.transfer_confirm_btn_select)
                tv_money.setBackgroundResource(R.mipmap.transfer_money_bg)
            }
        }*/

//        iv_btn_confirm.isEnabled = false
//        iv_btn_confirm.setBackgroundResource(R.mipmap.transfer_confirm_btn)
//        tv_money.setBackgroundResource(R.mipmap.transfer_confirm_btn_n)

        tv_money.text = "￥${getMoney}"
        et_input_money.setText(getMoney)

        StoredUserSources.getUserInfo()?.apply {
            tv_mile_wallet.text = "￥${user?.balance}"
            tv_game_wallet.text = "￥${user?.game_balance}"
            mileWalletAmount = user?.balance
        }
    }

    private val homeViewModel by getViewModel(HomeViewModel::class.java) {
        _getTransferData.observe(it, Observer {
            it?.let {
                ToastUtils.showToast("转账成功", true)
                finishView()
            }
        })
    }
    private val getMoney by lazy {
        intent.getStringExtra(S_KEY) ?: "0"
    }

    companion object {
        const val S_KEY = "S_KEY"
        fun open(context: Context, currentChooseItem: String?=""): Intent {
            return Intent(context, TransferAccountActivity::class.java).putExtra(S_KEY,
                currentChooseItem)
        }
    }
}
