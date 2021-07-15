package com.swbg.mlivestreaming.ui.withdraw

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.lxj.xpopup.XPopup
import com.swbg.mlivestreaming.BuildConfig
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.bean.AccountRes
import com.swbg.mlivestreaming.bean.AnchorBean
import com.swbg.mlivestreaming.dialog.SelectAccountDialog
import com.swbg.mlivestreaming.dialog.SelectAccountTypeDialog
import com.swbg.mlivestreaming.event.LiveEvent
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.utils.HideDataUtil
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_apply_withdraw.*
import kotlinx.android.synthetic.main.activity_apply_withdraw.ibReturn
import kotlinx.android.synthetic.main.activity_apply_withdraw.iv_avatar
import kotlinx.android.synthetic.main.activity_apply_withdraw.tv_withdraw

class WithdrawActivity : MBaseActivity() {
    var observable: Observable<LiveEvent>? = null
    var anchorBean: AnchorBean? = null
    var accountRes: AccountRes? = null
    var list: List<AccountRes> = arrayListOf()
    override val contentViewLayoutId: Int
        get() = R.layout.activity_apply_withdraw

    override fun initView() {

        anchorViewModel.getAnchorInfo(hashMapOf())
        ibReturn.singleClick {
            finish()
        }
        tv_withdraw.singleClick {
            //确认提现
            val amount = et_withdraw_title.text.toString()
            if (amount.isBlank()) {
                showToast("请输入正确金额")
                return@singleClick
            }

            anchorViewModel.withdraw(hashMapOf("amount" to "${amount}",
                "account_id" to "${accountRes?.id}",
                "name" to "${anchorBean?.name}"))
        }
        val accountDialog by lazy {
            XPopup.Builder(this@WithdrawActivity)
                .asCustom(SelectAccountDialog(this@WithdrawActivity,
                    list,
                    object : SelectAccountDialog.OnAccountListener {
                        override fun selectPos(position: Int) {
                            accountRes = list[position]
                            if (accountRes?.type == 1) {
                                tv_account_title.text =
                                    "支付宝(${HideDataUtil.hidePhoneNo(accountRes?.alipay_account)})"
                            } else {
                                tv_account_title.text =
                                    "银行卡号(${accountRes?.bank_name}${HideDataUtil.hideCardNo(
                                        accountRes?.bank_card)})"
                            }
                        }

                        override fun addAccount() {
                            typeDialog.show()

                        }

                    }))
        }
        cl_account.singleClick {
            //选择账户
            accountDialog.show()
        }
        tv_account_title.singleClick {
            //选择账户
            accountDialog.show()
        }
        anchorViewModel.getAccount(hashMapOf())
    }

    val typeDialog by lazy {
        XPopup.Builder(this@WithdrawActivity)
            .asCustom(SelectAccountTypeDialog(this@WithdrawActivity,
                object : SelectAccountTypeDialog.OnAccountTypeListener {
                    override fun selectPos(type: Int) {
                        startActivity(BindAccountActivity.open(this@WithdrawActivity, type))
                    }
                }))
    }

    private fun showSelectType() {

    }


    companion object {
        const val LIVE_DATA = "data"
        const val INITIAL_POSITION = "position"
        fun open(context: Context?): Intent {
            return Intent(context, WithdrawActivity::class.java).apply {

            }
        }
    }

    private val anchorViewModel by getViewModel(AnchorViewModel::class.java) {
        _anchorInfoData.observe(it, Observer { anchorBean ->
            this@WithdrawActivity.anchorBean = anchorBean
            if (anchorBean.user?.avatar?.contains("http") == false) {
                anchorBean.user?.avatar = BuildConfig.IMAGE_BASE_URL + anchorBean.user?.avatar
            }
            Glide.with(this@WithdrawActivity).load(anchorBean.user?.avatar).circleCrop()
                .placeholder(R.mipmap.default_avatar).into(iv_avatar)
            tv_nickname.text = anchorBean.user?.nickname
            tv_balance.text = "账户余豆： ${anchorBean.user?.balance}${getString(R.string.balance)}"
            tv_rate.text = "兑换比例： ${anchorBean.level?.rate}:${anchorBean.level?.rmb_rate}"
            tv_name_title.text = "${anchorBean.name}"
        })
        _withdrawResult.observe(it, Observer {
            //提现回调
            it?.let {
                startActivity(WithdrawResultActivity.open(this@WithdrawActivity, it, accountRes))
                finishView()
            }
        })
        _accountList.observe(it, Observer {
            it?.let {
                //显示dialog
                list = it
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()

    }

}