package com.mp.douyu.ui.withdraw

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import com.mp.douyu.GlobeStatusViewHolder
import com.mp.douyu.R
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.bean.WithdrawRecordBean
import com.mp.douyu.event.LiveEvent
import com.mp.douyu.provider.StoredUserSources
import com.mp.douyu.singleClick
import com.mp.douyu.utils.ActivityUtils
import com.mp.douyu.utils.HideDataUtil
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_withdraw_detail.*

class WithdrawRecordDetailActivity : MBaseActivity() {
    var recordBean:WithdrawRecordBean?=null
    var observable: Observable<LiveEvent>? = null
    override val contentViewLayoutId: Int
        get() = R.layout.activity_withdraw_detail

    override fun initView() {
        recordBean = intent.getParcelableExtra<WithdrawRecordBean>(INTENT_DATA)

        anchorViewModel.getRecordDetail(hashMapOf("id" to "${recordBean?.id}"))

        ibReturn.singleClick {
            finish()
        }
        tv_server.singleClick {
            //客服
            if (GlobeStatusViewHolder.isNotNeedLogin(this)) {
                val settingData = StoredUserSources.getSettingData()
                settingData?.contact?.let {
                    ActivityUtils.jumpToWebView(it, this)
                }
            }
        }
    }


    companion object {
        const val INTENT_DATA = "data"
        const val INITIAL_POSITION = "position"
        fun open(context: Context?, recordBean: WithdrawRecordBean): Intent {
            return Intent(context, WithdrawRecordDetailActivity::class.java).apply {
                putExtra(INTENT_DATA,recordBean)
            }
        }
    }

    private val anchorViewModel by getViewModel(AnchorViewModel::class.java) {
        _recordDetail.observe(it, Observer {recordDetail->

            tv_amount.text = "+${recordDetail?.amount}"
            if (recordDetail?.status==0) {
                tv_state.text="审核中"
                tv_state.setTextColor(resources.getColor(R.color.colorTextG2))
            }else if (recordDetail?.status == 1) {
                tv_state.text="成功"
                tv_state.setTextColor(resources.getColor(R.color.picture_color_20c064))
            }else if (recordDetail?.status == 2) {
                tv_state.text="失败"
                tv_state.setTextColor(resources.getColor(R.color.color_C52122))
            }
            tv_way_title.text = if (recordDetail?.account?.type==0)  "银行卡号(${HideDataUtil.hideCardNo(recordDetail?.account?.bank_card)})" else "支付宝(${HideDataUtil.hidePhoneNo(recordDetail?.account?.alipay_account)})"
            tv_time_title.text = recordDetail?.create_time
            tv_order_title.text = recordDetail?.order_id


        })
    }


    override fun onDestroy() {
        super.onDestroy()

    }

}