package com.swbg.mlivestreaming.ui.withdraw

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.swbg.mlivestreaming.BuildConfig
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.event.LiveEvent
import com.swbg.mlivestreaming.singleClick
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_anchor_info.*
import kotlinx.android.synthetic.main.activity_anchor_info.iftActionRight
import kotlinx.android.synthetic.main.activity_anchor_info.tv_name

class AnchorInfoActivity : MBaseActivity() {
    var observable: Observable<LiveEvent>? = null
    override val contentViewLayoutId: Int
        get() = R.layout.activity_anchor_info

    override fun initView() {

        anchorViewModel.getAnchorInfo(hashMapOf())
        tv_withdraw.singleClick {
            startActivity(WithdrawActivity.open(this))
        }

        iftActionRight.singleClick {
            //规则
            startActivity(RuleActivity.open(this))
        }
        ibReturn.singleClick {
            finish()
        }
        tv_record.singleClick {
            //提现记录
            startActivity(WithdrawRecordActivity.open(this))
        }
    }


    companion object {
        const val INTENT_DATA = "data"
        const val INITIAL_POSITION = "position"
        fun open(context: Context?): Intent {
            return Intent(context, AnchorInfoActivity::class.java).apply {

            }
        }
    }

    private val anchorViewModel by getViewModel(AnchorViewModel::class.java) {
        _anchorInfoData.observe(it, Observer {anchorBean->
            if (anchorBean.user?.avatar?.contains("http") == false) {
                anchorBean.user?.avatar = BuildConfig.IMAGE_BASE_URL+anchorBean.user?.avatar
            }
            Glide.with(this@AnchorInfoActivity).load(anchorBean.user?.avatar).circleCrop()
                .placeholder(R.mipmap.default_avatar).into(iv_avatar)
            tv_name.text = anchorBean.user?.nickname
            tv_level.text = "${anchorBean.level?.name}"
            val total = anchorBean.level?.time?.plus(anchorBean.next_level_time!!)
            tv_time.text = "${anchorBean.level?.time}小时/${anchorBean.next_level_time}小时\n" + "${anchorBean.level?.experience}${getString(R.string.balance)}/${anchorBean.next_level_experience}${getString(R.string.balance)}"
            progressBar.max = total!!
            progressBar.progress = anchorBean.level?.time!!
            tv_tip.text = "还需${anchorBean.next_level_time?.minus(anchorBean.level?.time!!)}小时升级"
            tv_time_title.text = "${anchorBean.level?.time}小时"
            tv_fans_title.text = "${anchorBean.fans_count}人"
            tv_income_title.text = "${anchorBean.gift_count}${getString(R.string.balance)}"
            iv_level.setImageResource(getLevelIcon(anchorBean?.level?.level.toString()))
        })
    }


    override fun onDestroy() {
        super.onDestroy()

    }
    fun getLevelIcon(level:String):Int{
        return when(level){
            "1"->R.mipmap.ic_huangtong
            "2"->R.mipmap.ic_baiyin
            "3"->R.mipmap.ic_huangjin
            "4"->R.mipmap.ic_bojin
            "5"->R.mipmap.ic_zhuanshi
            "6"->R.mipmap.ic_wangzhe
            else ->R.mipmap.ic_huangtong
        }
    }
}