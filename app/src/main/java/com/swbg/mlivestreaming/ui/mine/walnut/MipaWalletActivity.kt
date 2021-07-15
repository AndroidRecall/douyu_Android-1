package com.swbg.mlivestreaming.ui.mine.walnut

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.swbg.mlivestreaming.*
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.bean.MipaWalletBean
import com.swbg.mlivestreaming.event.LiveHomeEvent
import com.swbg.mlivestreaming.provider.StoredUserSources
import com.swbg.mlivestreaming.ui.home.play.TransferAccountActivity
import com.swbg.mlivestreaming.ui.mine.MineViewModel
import com.swbg.mlivestreaming.ui.mine.task.TaskCenterActivity
import com.swbg.mlivestreaming.utils.ActivityUtils.jumpToWebView
import com.swbg.mlivestreaming.utils.RxBus
import com.swbg.mlivestreaming.view.popupwindow.BaseFragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_mipa_wallet.*
import kotlinx.android.synthetic.main.title_bar_simple.*

class MipaWalletActivity : MBaseActivity() {
    lateinit var titles: ArrayList<String>
    lateinit var fragments: List<Fragment>
    val getType by lazy {
        intent.getStringExtra(TYPE)
    }

    override val contentViewLayoutId: Int
        get() = R.layout.activity_mipa_wallet

    override fun initView() {
        matchWaterDropScreen(this@MipaWalletActivity)
        //设置沉浸式
        setActivityImmersion(this@MipaWalletActivity)

        titleBar.setBackgroundColor(ContextCompat.getColor(this, R.color.color00000000))
        Glide.with(this).load(R.mipmap.return_back_white).centerInside().into(ibReturn)
        ibReturn.singleClick {
            finishView()
        }

        //获取系统设置数据
        val settingData = StoredUserSources.getSettingData()

        tv_account_remain.text =  "${getAmount(getType)}"
        //隱藏
        iv_hint.singleClick {
            tv_account_remain.text = if (tv_account_remain.text.toString() == "***") "${getAmount(getType)}" else "***"
        }
        tv_account_bean.singleClick {
            tv_account_remain.text = if (tv_account_remain.text.toString() == "***") "${getAmount(getType)}" else "***"
        }

        iftTitle.setTextColor(ContextCompat.getColor(this, R.color.white))
        when (getType) {
            TYPE_BEAN -> {
                tv_mile_wallet_used.visibility = View.GONE
                iftTitle.text = "${BuildConfig.APP_BLANCE_NAME}"
                titles = arrayListOf("获得", "消费")
                fragments = listOf<Fragment>(MipaWalletFragment.newInstance(MipaWalletBean("获得",
                    TYPE_BEAN)), MipaWalletFragment.newInstance(MipaWalletBean("消费", TYPE_BEAN)))
                //获取咪豆
                tv_get_mi_bean.singleClick {
                    startActivityWithTransition(TaskCenterActivity.open(this))
                }
            }
            TYPE_MIPA_WALLET -> {
                iftTitle.text = "${BuildConfig.APP_NAME_}钱包"
                tv_account_bean.text = "账户余额（元）"
                Glide.with(this).load(R.mipmap.wallet_c).centerInside().into(iv1)
                tv_get_mi_bean.visibility = View.GONE
                tv_transfer_account.visibility = View.VISIBLE

                //转账
                tv_transfer_account.singleClick {
                    startActivityWithTransition(TransferAccountActivity.open(this, "0"))
                }
                tv_transfer_account.visibility = View.VISIBLE
                tv_mile_wallet_used.singleClick {
                    settingData?.wallet_introduce?.apply {
                        jumpToWebView(this, this@MipaWalletActivity, true, webViewTitle = "${BuildConfig.APP_NAME_}钱包用途")
                    }
                }
                titles = arrayListOf("存款", "转账", "消费")
                fragments = listOf<Fragment>(MipaWalletFragment.newInstance(MipaWalletBean("存款",
                    TYPE_MIPA_WALLET)),
                    MipaWalletFragment.newInstance(MipaWalletBean("转账", TYPE_MIPA_WALLET)),
                    MipaWalletFragment.newInstance(MipaWalletBean("消费", TYPE_MIPA_WALLET)))
            }
        }

        //充值中心
        tv_charge.singleClick {
            startActivityWithTransition(ChargeCenterActivity.open(this))
        }

        wallet_view_pager.apply {
            adapter = BaseFragmentPagerAdapter(supportFragmentManager, titles, fragments)
        }
        wallet_tabLayout.setupWithViewPager(wallet_view_pager)

        tv_service_center.singleClick {
            settingData?.contact?.let {
                jumpToWebView(it, this)
            }
        }

        initEvent()
    }

    private fun initEvent() {
        RxBus.getInstance().register(LiveHomeEvent::class.java).subscribe {
            when (it.type) {
                0->{
                   finishView()
                }
            }

        }
    }

    private fun getAmount(type: String?): String {
        when (type) {
            TYPE_BEAN-> return "${StoredUserSources.getUserInfo2()?.points}"
            TYPE_MIPA_WALLET-> return "${StoredUserSources.getUserInfo2()?.balance}"
        }
        return "0"
    }


    private val mineViewModel by getViewModel(MineViewModel::class.java) {
        _setUserAvatar.observe(it, Observer {
            it?.let {
                finishView()
            }
        })
    }


    companion object {
        const val TYPE = "WALLET_TYPE"
        const val TYPE_BEAN = "TYPE_BEAN"
        const val TYPE_MIPA_WALLET = "TYPE_MIPA_WALLET"
        fun open(context: Context?, type: String): Intent {
            return Intent(context, MipaWalletActivity::class.java).putExtra(TYPE, type)
        }
    }

}
