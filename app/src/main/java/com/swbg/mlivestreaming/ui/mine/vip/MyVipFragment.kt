package com.swbg.mlivestreaming.ui.mine.vip

import android.content.DialogInterface
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.swbg.mlivestreaming.BuildConfig
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.MBaseFragment
import com.swbg.mlivestreaming.bean.MyVipBean
import com.swbg.mlivestreaming.bean.SelfInfoBean
import com.swbg.mlivestreaming.bean.UserInfoBean
import com.swbg.mlivestreaming.provider.StoredUserSources
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.ui.mine.MineViewModel
import com.swbg.mlivestreaming.ui.mine.task.TaskCenterActivity
import com.swbg.mlivestreaming.utils.ToastUtils
import com.swbg.mlivestreaming.view.popupwindow.GetMiBeanDialog
import com.swbg.mlivestreaming.view.popupwindow.TitleAndMessageDialog
import kotlinx.android.synthetic.main.fragment_my_vip.*

class MyVipFragment : MBaseFragment() {
    var currenListBean : ArrayList<MyVipBean> =ArrayList()
    var vipString : StringBuilder = java.lang.StringBuilder()
    var userInfoBean: UserInfoBean = UserInfoBean()
        set(value) {
            this.let {
                value.user?.let {
                    if ((getType?.plus(1)).toString() == it.vip) {
                        vipString.clear()
                        tv_open_now.text = "已开通"
                        vipString.append("VIP到期时间：${it.vip_time}\n")
                    }
                }
            }
            field = value
        }
    override val layoutId: Int
        get() = R.layout.fragment_my_vip

    override fun initView() {
        initData()
        recyclerView.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(context, 3).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (position == 0) {
                            3
                        } else {
                            1
                        }
                    }
                }
                addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//                        if ((view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition % 4 == 0 && (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition == 1) {
//                            outRect.set(34, 50, 0, 0)
//                        } else {
//                            outRect.set(12, 50, 0, 0)
//                        }
                    }
                })
            }
        }

        mAdapter?.type = getType
        val listBean = arrayListOf<MyVipBean>()
        when (getType) {
            0 -> {
                setBeanData(0, listBean)
            }
            1 -> {
                setBeanData(1, listBean)
            }
            2 -> {
                setBeanData(2, listBean)
            }
            3 -> {
                setBeanData(3, listBean)
            }
            4 -> {
                setBeanData(4, listBean)
            }
            5 -> {
                setBeanData(5, listBean)
            }
        }
        mAdapter?.vipBeans = listBean
        mAdapter?.changeDataSet()

        getVipBean?.let {
            mAdapter?.vipBean = it
            mAdapter?.changeHeader(0)
        }

        tv_open_now.singleClick {
            if (tv_open_now.text.toString() == "已开通") return@singleClick
            TitleAndMessageDialog.newInstance("提示", "确认支付开通VIP", "确定", "取消")
                .setOnClickListener(DialogInterface.OnClickListener { dialog, which ->
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        mineViewModel.chargeVip(hashMapOf("vip_id" to "${getVipBean?.id}"))
//                        mineViewModel.
                    }
                }).show(childFragmentManager, null)
        }


    }

    override fun showLoadingView(show: Boolean) {
        super.showLoadingView(show)
    }

    private fun initData() {
        userInfoBean = StoredUserSources.getUserInfo().takeIf { it != null } ?: UserInfoBean()
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

                    this@MyVipFragment.showLoadingView(true)
                    getUserInfo()
                }
            }
        })
        _getUserInfo.observe(it, Observer {
            it?.let {
                userInfoBean = it
                tv_content.text = "VIP到期时间：${it.user?.vip_time}\n续费只需 ${getVipBean?.points} ${context?.getString(R.string.balance)}/月\n${context?.getString(R.string.balance)}余额：${userInfoBean.user?.points}"
                StoredUserSources.putUserInfo(it)
            }
        })
    }

    private fun setBeanData(type: Int, listBean: ArrayList<MyVipBean>) {
        currenListBean = listBean
        listBean.addAll(arrayListOf(
//            MyVipBean(mount_icon = getVipBean?.mount_icon,
//            mount_text = getVipBean?.mount_text,
//            title = "专属坐骑"),
            MyVipBean(localImage = R.mipmap.vip_item_two,
                mount_text = "首开${getVipBean?.points}/月\n续开${getVipBean?.points}/月",
                title = "超值优惠"),
            MyVipBean(localImage = R.mipmap.vip_item_three,
                title = "VIP标识",
                mount_text = "${getVipBean?.title}专属身份徽章"),
//            MyVipBean(localImage = R.mipmap.vip_item_four,
//                title = "发言特权",
//                mount_text = "尊享${getVipBean?.title}气泡\n解锁${getVipBean?.danmaku_level}弹幕"),
            MyVipBean(localImage = R.mipmap.vip_item_five,
                title = "看片特权",
                mount_text = "每日超清观影${getVipBean?.play}次\n每日超清缓存${getVipBean?.download}次")
//            ,
//            MyVipBean(localImage = R.mipmap.vip_item_six,
//                title = "开通通知",
//                mount_text = "开通续费时\n房间内广播")
        ))
//        getVipBean?.let {
//            if (it.horn != 0) {
//                listBean.add(MyVipBean(localImage = R.mipmap.vip_item_seven,
//                    title = "喇叭",
//                    mount_text = "开通即送${getVipBean?.horn}个喇叭\n每日免费送${getVipBean?.horn}个喇叭\n范围:所有直播间"))
//            }
//        }
        tv_content.text = when(userInfoBean.user?.vip == getType?.plus(1).toString()){
            true->{
                "${vipString}续费只需 ${getVipBean?.points} ${context?.getString(R.string.balance)}/月\n${context?.getString(R.string.balance)}余额：${StoredUserSources.getUserInfo2()?.points}"
            }
            false->{
                "开通${getVipBean?.title}首月：${getVipBean?.points} ${context?.getString(R.string.balance)}\n续费只需 ${getVipBean?.points} 眯豆/月\n${context?.getString(R.string.balance)}余额：${StoredUserSources.getUserInfo2()?.points}"
            }
        }
    }

    private val mAdapter by lazy {
        context?.let { MyVipAdapter(it) }
    }

    private val getType by lazy {
        arguments?.getInt(TYPE)
    }
    private val getVipBean by lazy {
        arguments?.getParcelable<MyVipBean>(MY_VIP_BEAN)
    }

    companion object {
        const val TYPE = "type"
        const val MY_VIP_BEAN = "MY_VIP_BEAN"
        fun newInstance(datas: Int, myVipBean: MyVipBean?): MyVipFragment {
            val fragment = MyVipFragment()
            val bundle = Bundle()
            bundle.putInt(TYPE, datas)
            bundle.putParcelable(MY_VIP_BEAN, myVipBean)
            fragment.arguments = bundle
            return fragment
        }
    }
}
