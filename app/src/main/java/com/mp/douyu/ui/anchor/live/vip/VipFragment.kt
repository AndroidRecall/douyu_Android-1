package com.mp.douyu.ui.anchor.live.vip

import android.content.DialogInterface
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.mp.douyu.BuildConfig
import com.mp.douyu.R
import com.mp.douyu.adapter.LiveVipPowerAdapter
import com.mp.douyu.base.VisibilityFragment
import com.mp.douyu.bean.MyVipBean
import com.mp.douyu.provider.StoredUserSources
import com.mp.douyu.singleClick
import com.mp.douyu.ui.mine.MineViewModel
import com.mp.douyu.ui.mine.task.TaskCenterActivity
import com.mp.douyu.utils.ToastUtils
import com.mp.douyu.view.popupwindow.GetMiBeanDialog
import com.mp.douyu.view.popupwindow.TitleAndMessageDialog
import kotlinx.android.synthetic.main.fragment_my_vip.*
import kotlinx.android.synthetic.main.live_fragment_vip.*

class VipFragment : VisibilityFragment() {
    var vipBean: MyVipBean? = null
    override val layoutId: Int
        get() = R.layout.live_fragment_vip

    companion object {
        const val VIP_TYPE = "type"
        const val EXTRA_DATA = "data"
        const val VIP_TYPE_DF = 0
        const val VIP_TYPE_GJ = 1
        const val VIP_TYPE_HJ = 2
        const val VIP_TYPE_ZJ = 3
        const val VIP_TYPE_QS = 4
        fun newInstance(type: Int = VIP_TYPE_DF): VipFragment {
            val fragment = VipFragment()
            val bundle = Bundle()
            bundle.putInt(VIP_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }

        fun newInstance(bean: MyVipBean): VipFragment {
            val fragment = VipFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_DATA, bean)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {
    }

    override fun onVisible() {
        super.onVisible()
    }

    override fun onInvisible() {
        super.onInvisible()
    }

    override fun onVisibleFirst() {
        super.onVisibleFirst()
        vipBean = arguments?.getParcelable<MyVipBean>(EXTRA_DATA)?.apply {
            tv_status.text = title
            activity?.let {
                Glide.with(it).load(BuildConfig.IMAGE_BASE_URL + cover)
                    .error(R.mipmap.icon_vip_huang_small).into(iv_status)
            }
            tv_open_desc.text = "开通${title}首月:${points}${context?.getString(R.string.balance)}"
            tv_renew_desc.text = "续费只需${points}${context?.getString(R.string.balance)}/月"
            tv_residue.text = "${context?.getString(R.string.balance)}余额:${StoredUserSources.getUserInfo()?.user?.game_balance}"
            tv_open_person.text
        }

        recycle_power?.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(context, 3)
        }
        initOnclickListener()
    }

    private fun initOnclickListener() {
        tv_open.singleClick {
            TitleAndMessageDialog.newInstance("提示", "确认支付开通VIP", "确定", "取消")
                .setOnClickListener(DialogInterface.OnClickListener { dialog, which ->
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        mineViewModel.chargeVip(hashMapOf("vip_id" to "${vipBean?.id}"))
//                        mineViewModel.
                    }
                }).show(childFragmentManager, null)
        }

    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
    }

    val mAdapter by lazy {
        LiveVipPowerAdapter({}, context).apply {
            add("")
            add("")
            add("")
            add("")
            add("")
            add("")
            add("")
        }
    }

    private val mineViewModel by getViewModel(MineViewModel::class.java) {
        _chargeVipList.observe(it, Observer {
            if (it == null) {
            }
            it?.let {
                if (it is String) {
                    when (it) {
                        "${BuildConfig.APP_BLANCE_NAME}不足" -> {
                            GetMiBeanDialog.newInstance("${BuildConfig.APP_BLANCE_NAME}不足请获取", "", "获取", "取消")
                                .setOnClickListener(DialogInterface.OnClickListener { dialog1, which1 ->
                                    if (which1 == DialogInterface.BUTTON_POSITIVE) {
                                        startActivityWithTransition(TaskCenterActivity.open(context))
                                    }
                                }).show(childFragmentManager, null)
                        }
                        else -> {
                            ToastUtils.showToast("$it", false)
                        }
                    }
                } else {
                    ToastUtils.showToast("成功开通VIP", true)
                    tv_open_now.text = "已开通"

                    this@VipFragment.showLoadingView(true)
                    getUserInfo()
                }
            }
        })
        _getUserInfo.observe(it, Observer {
            it?.let {
//                userInfoBean = it
//                tv_content.text = "VIP到期时间：${it.user?.vip_time}\n续费只需 ${getVipBean?.points} 眯豆/月\n眯豆余额：${userInfoBean.user?.points}"
                StoredUserSources.putUserInfo(it)
            }
        })
    }
}