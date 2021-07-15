package com.swbg.mlivestreaming.dialog

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lxj.xpopup.util.XPopupUtils
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.adapter.GiftNumberAdapter
import com.swbg.mlivestreaming.adapter.TabNavigatorAdapter
import com.swbg.mlivestreaming.adapter.VP2FragmentPagerAdapter
import com.swbg.mlivestreaming.bean.*
import com.swbg.mlivestreaming.http.DisposableSubscriberAdapter
import com.swbg.mlivestreaming.provider.StoredUserSources
import com.swbg.mlivestreaming.provider.TokenProvider
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.ui.anchor.live.LiveViewModel
import com.swbg.mlivestreaming.ui.anchor.live.gift.GiftDetailFragment
import com.swbg.mlivestreaming.ui.login_register.LoginActivity
import com.swbg.mlivestreaming.ui.mine.MineViewModel
import com.swbg.mlivestreaming.ui.mine.walnut.ChargeCenterActivity
import com.swbg.mlivestreaming.utils.ToastUtils
import com.swbg.mlivestreaming.view.ViewPager2Helper
import kotlinx.android.synthetic.main.live_dialog_gift.view.*
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * 礼物Dialog
 */
class GiftListDialog(context: Context, var liveBean: LiveBean? = null, var bitmap: Bitmap? = null, var listener: OnGiftListener) :
    BaseBottomPopupView(context) {
    private val tabBeans: List<MiniTabBean> by lazy {
        val norTabBean = MiniTabBean("普通礼物")
        val interplayTabBean = MiniTabBean("互动礼物")
        val highTabBean = MiniTabBean("高级礼物")
//        val packageTabBean = MiniTabBean("包裹")
        listOf(norTabBean, interplayTabBean, highTabBean/*, packageTabBean*/)
    }
    private val fragments: Array<GiftDetailFragment> by lazy {
        arrayOf(GiftDetailFragment.newInstance(GiftDetailFragment.GIFT_TYPE_NOR),
            GiftDetailFragment.newInstance(GiftDetailFragment.GIFT_TYPE_INTERPLAY),
            GiftDetailFragment.newInstance(GiftDetailFragment.GIFT_TYPE_HIGH)/*,
            GiftDetailFragment.newInstance(GiftDetailFragment.GIFT_TYPE_PACKAGE)*/)
    }

    override fun getImplLayoutId(): Int {
        return R.layout.live_dialog_gift
    }

    override fun getMaxHeight(): Int {
        return (XPopupUtils.getScreenHeight(context) * .6f).toInt()
    }

    override fun onCreate() {
        super.onCreate()
        recycler_number.apply {
            adapter = mNumberAdapter
            layoutManager = LinearLayoutManager(context)
        }
        view_pager2?.apply {
            isUserInputEnabled = true
            adapter = VP2FragmentPagerAdapter(context as FragmentActivity,
                fragments.toMutableList() as List<Fragment>?)
        }
        mCommonNavigatorAdapter?.apply {
            selectedColor = resources.getColor(R.color.colorBtnBlue)
            normalColor = resources.getColor(R.color.black)
            selectedSize = 12
            normalSize = 12
            val commonNavigator = CommonNavigator(context)
            commonNavigator.isAdjustMode = false
            commonNavigator.adapter = this
            indicator.navigator = commonNavigator
            ViewPager2Helper.bind(indicator, view_pager2)
        }
        val userInfo = StoredUserSources.getUserInfo()
        tv_joy_beans_num.text = "${userInfo?.user?.points}"
        tv_wallet_num.text = "${userInfo?.user?.balance}"
        initOnclick()
        iv_bg.setImageBitmap(bitmap)

        mineViewModel.getUserInfo(object : DisposableSubscriberAdapter<UserInfoBean?>(this) {
            override fun onNext(t: UserInfoBean?) {
                StoredUserSources.putUserInfo(t)
                tv_joy_beans_num.text = "${t?.user?.points}"
                tv_wallet_num.text = "${t?.user?.balance}"
            }

            override fun onError(t: Throwable?) {
                super.onError(t)
                ToastUtils.showToast("${t?.localizedMessage}")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    private val mNumberAdapter by lazy {
        GiftNumberAdapter({ view, position ->
            selectNumber(position)
            recycler_number.visibility = View.GONE
        }, context).apply {
            addAll(arrayListOf(GiftNumberBean(1314, "一生一世"),
                GiftNumberBean(520, "我爱你"),
                GiftNumberBean(188, "要抱抱"),
                GiftNumberBean(66, "一切顺利"),
                GiftNumberBean(30, "想你"),
                GiftNumberBean(10, "十全十美"),
                GiftNumberBean(1, "一心一意")))
        }

    }

    private fun selectNumber(position: Int) {
        tv_give_num.text = "${mNumberAdapter[position].number}"
    }

    private fun initOnclick() {
        tv_recharge.singleClick {
            //充值
            if (!TokenProvider.get().hasToken()) {
                context.startActivity(LoginActivity.open(context))
            } else {
                context.startActivity(ChargeCenterActivity.open(context))
            }
        }
        iv_dismiss.singleClick {
            dismiss()
        }
        tv_give_num.singleClick {
            //选择礼物数量
            if (recycler_number.visibility == View.GONE) {

                recycler_number.visibility = View.VISIBLE
            } else {
                recycler_number.visibility = View.GONE
            }
        }
        tv_submit.singleClick {
            //赠送礼物
            var num = tv_give_num.text.toString().toInt()
            fragments[view_pager2.currentItem].getCurSelectGift()?.let { it ->
                liveViewModel.sendGift2(hashMapOf(
                    "gift_id" to "${it.id}",
                    "number" to "${num}",
                    "anchor_id" to "${liveBean?.anchor?.id}",
                    "live_id" to "${liveBean?.id}"), object : DisposableSubscriberAdapter<String>(this) {
                    override fun onNext(t: String) {
                        listener.sendGift(SendGiftBean("$num", it))
                        ToastUtils.showToast("赠送成功")
                        dismiss()
                    }

                    override fun onError(t: Throwable?) {
                        super.onError(t)
                        ToastUtils.showToast("${t?.localizedMessage}")
                    }
                })
            }
        }
        tv_bursts.singleClick {
            //连续赠送礼物
        }
    }

    private val mCommonNavigatorAdapter: TabNavigatorAdapter by lazy {
        TabNavigatorAdapter(tabBeans, object : TabNavigatorAdapter.OnTabItemClickListener {
            override fun onTabItemClick(position: Int) {
                view_pager2?.currentItem = position
            }
        })
    }
    private val liveViewModel by getViewModel(LiveViewModel::class.java,
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return LiveViewModel() as T
            }
        }) {
        _sendGiftsResult.observe(it, Observer {
            it?.let {
                //送礼成功，更新金额及送礼动画

            }
        })
    }
    private val mineViewModel by getViewModel(MineViewModel::class.java) {
        _getUserInfo.observe(it, Observer {
            it?.let {
                StoredUserSources.putUserInfo(it)
            }
        })
    }

    interface OnGiftListener {
        fun sendGift(bean: SendGiftBean)
    }
}