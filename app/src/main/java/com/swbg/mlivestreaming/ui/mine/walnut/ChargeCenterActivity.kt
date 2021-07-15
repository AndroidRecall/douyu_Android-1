package com.swbg.mlivestreaming.ui.mine.walnut

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jzvd.Jzvd
import com.swbg.mlivestreaming.BuildConfig
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.bean.ChargeCenterBean
import com.swbg.mlivestreaming.provider.StoredUserSources
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.ui.mine.MineViewModel
import com.swbg.mlivestreaming.utils.ActivityUtils
import com.swbg.mlivestreaming.view.popupwindow.ChargeAlarmDialog
import kotlinx.android.synthetic.main.activity_charge_center.*
import kotlinx.android.synthetic.main.title_bar_confirm_btn.*

class ChargeCenterActivity : MBaseActivity() {
    override val contentViewLayoutId: Int
        get() = R.layout.activity_charge_center

    override fun initView() {
        iftTitle.text = "充值中心"
        val settingData = StoredUserSources.getSettingData()
        tv_service_center.apply {
            visibility = View.VISIBLE
            singleClick {
                //service
                settingData?.contact?.let {
                    ActivityUtils.jumpToWebView(it, this@ChargeCenterActivity)
                }
            }
        }
        ibReturn.singleClick {
            finishView()
        }
        initRecycler()

        mineViewModel.getPayList()
    }

    private fun initRecycler() {
        recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@ChargeCenterActivity)
        }
        mAdapter.refresh(listOf(ChargeCenterBean()), null)
    }

    private val mineViewModel by getViewModel(MineViewModel::class.java) {
        _payList.observe(it, Observer {
            it?.let {
                mAdapter.payListBean = it
                mAdapter.refresh(listOf(ChargeCenterBean()), null)
            }
        })
        _beginPay.observe(it, Observer {
            it?.let {
                it.pay_url?.apply {
                    if (this.isNotEmpty()) {
                        ChargeAlarmDialog.newInstance("充值完成后，请根据系统消息提示查收充值余额，祝您玩得愉快",
                            "",
                            "确认",
                            "取消").apply {
                            setOnClickListener(DialogInterface.OnClickListener { dialog, which ->
                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    getPayResult(hashMapOf("id" to "${it.order_no}"))
                                }
                            })
                        }.show(supportFragmentManager, null)
                        ActivityUtils.jumpToWebView(it.pay_url, this@ChargeCenterActivity)
                    }
                }
            }
        })

        _getChargeResult.observe(it, Observer {
            it?.let {
                if (it.status == 1) {
                    BuildConfig.IMAGE_BASE_URL
                    ChargeAlarmDialog.newInstance("${BuildConfig.APP_NAME_}钱包充值成功",
                        "恭喜您，本次充值${it.amount}元成功，并获赠${it.points}${BuildConfig.APP_BLANCE_NAME}",
                        "我知道了",
                        "").apply {
                        setOnClickListener(DialogInterface.OnClickListener { dialog, which ->
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                setResult(Activity.RESULT_OK, Intent())
                                finishView()

                            }
                        })
                    }.show(supportFragmentManager, null)
                }
            }
        })
    }


    private val mAdapter by lazy {
        ChargeCenterAdapter(this) { view: View ->
            when (view.id) {
                R.id.tv_confirm_charge -> {//confirm charge
                    fBeginPay()
                }
            }
        }
    }


    override fun onBackPressed() {
        if (Jzvd.backPress()) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        Jzvd.goOnPlayOnPause()
    }

    override fun onResume() {
        super.onResume()
        Jzvd.goOnPlayOnResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        Jzvd.releaseAllVideos()
    }

    private fun fBeginPay() {
        mineViewModel.beginPay(hashMapOf("pay_id" to "${mAdapter.currentId}",
            "name" to "${mAdapter.currentName}",
            "amount" to "${mAdapter.currentMoney}"))
    }

    companion object {
        fun open(context: Context?): Intent {
            return Intent(context,
                ChargeCenterActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}
