package com.mp.douyu.ui.mine

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.scwang.smart.refresh.header.ClassicsHeader
import com.mp.douyu.GlobeStatusViewHolder.isNotNeedLogin
import com.mp.douyu.R
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.base.VisibilityFragment
import com.mp.douyu.bean.UserInfo2Bean
import com.mp.douyu.bean.UserInfoBean
import com.mp.douyu.event.RefreshInfo
import com.mp.douyu.im.ImManager
import com.mp.douyu.provider.StoredUserSources
import com.mp.douyu.provider.TokenProvider
import com.mp.douyu.singleClick
import com.mp.douyu.ui.login_register.login.LoginViewModel
import com.mp.douyu.ui.mine.about.AboutUsActivity
import com.mp.douyu.ui.mine.apply.AnchorApplyActivity
import com.mp.douyu.ui.mine.feedback.FeedBackActivity
import com.mp.douyu.ui.mine.msg.MineMessageActivity
import com.mp.douyu.ui.mine.relation.RelationActivity
import com.mp.douyu.ui.mine.relation.RelationActivity.Companion.RELATION_TYPE_FANS
import com.mp.douyu.ui.mine.relation.RelationActivity.Companion.RELATION_TYPE_FOLLOW
import com.mp.douyu.ui.mine.self_info.SelfInfoActivity
import com.mp.douyu.ui.mine.space.UserSpaceActivity
import com.mp.douyu.ui.mine.system_setting.SystemSetActivity
import com.mp.douyu.ui.mine.task.TaskCenterActivity
import com.mp.douyu.ui.mine.vip.MineVipActivity
import com.mp.douyu.ui.mine.walnut.ChargeCenterActivity
import com.mp.douyu.ui.mine.walnut.MipaWalletActivity
import com.mp.douyu.ui.square.circle.CircleListActivity
import com.mp.douyu.ui.square.circle.CircleListActivity.Companion.CIRCLE_TYPE_USER
import com.mp.douyu.ui.withdraw.AnchorInfoActivity
import com.mp.douyu.ui.withdraw.AnchorViewModel
import com.mp.douyu.utils.ActivityUtils
import com.mp.douyu.utils.RxBus
import com.tencent.imsdk.v2.V2TIMCallback
import com.tencent.imsdk.v2.V2TIMUserFullInfo
import com.tencent.imsdk.v2.V2TIMValueCallback
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : VisibilityFragment() {
    var observable: Observable<RefreshInfo>? = null
    var userInfoBean: UserInfoBean = UserInfoBean()
        set(value) {
            activity?.let {
//                Glide.with(it).load(BuildConfig.IMAGE_BASE_URL+value.user?.avatar).error(R.mipmap.default_avatar).centerCrop()
//                    .placeholder(R.mipmap.default_avatar).into(iv_avatar)
//                tv_user_name_logined.text = value.user?.nickname
//                tv_circle.text = "${value.user?.node}"
//                tv_attention.text = "${value.user?.follow}"
//                tv_fans.text = "${value.user?.fans}"
//                iv_mine_walnet.text = "${value.user?.balance}"
//                tv_mine_bean.text = "${value.user?.points}"
                tv_game_walnut.text = "${value.user?.game_balance}"
//                tv_scroll.text = value.headlines
            }
            field = value
        }
    var userInfo2Bean: UserInfo2Bean = UserInfo2Bean()
        set(value) {
            activity?.let {
                Glide.with(it).load(value.avatar).error(R.mipmap.default_avatar).centerCrop()
                    .placeholder(R.mipmap.default_avatar).into(iv_avatar)
                tv_user_name_logined.text = value.nickname
                tv_circle.text = "${value.circle_count}"
                tv_attention.text = "${value.follow_count}"
                tv_fans.text = "${value.fans_count}"
                iv_mine_walnet.text = "${value.balance}"
                tv_mine_bean.text = "${value.points}"
//                tv_game_walnut.text = "${value.game_balance}"
            }
            field = value
        }
    private val mineViewModel by getViewModel(MineViewModel::class.java) {
        _getUserInfo.observe(it, Observer {
            refreshLayout.finishRefresh()
            it?.let {
                userInfoBean = it
                StoredUserSources.putUserInfo(it)
            }
        })
        _getUserInfo2.observe(it, Observer {
            refreshLayout.finishRefresh()
            it?.let { it1 ->
                if (it1.isNotEmpty()) {
                    userInfo2Bean = it[0]
                    //设置用户信息
                    StoredUserSources.putUserInfo2(it[0])
                    //
                    val imUserInfo = V2TIMUserFullInfo()
                    imUserInfo.gender = userInfo2Bean.gender!!
                    ImManager.instance.getUsersInfo(arrayListOf("${TokenProvider.get().userId}"),
                        object : V2TIMValueCallback<List<V2TIMUserFullInfo>> {
                            override fun onSuccess(p0: List<V2TIMUserFullInfo>?) {
                                if (p0 != null && p0.isNotEmpty()) {
                                    val v2TIMUserFullInfo = p0[0]
                                    v2TIMUserFullInfo.gender = userInfo2Bean.gender!!
                                    v2TIMUserFullInfo.setNickname(userInfo2Bean.nickname)
                                    v2TIMUserFullInfo.faceUrl = userInfo2Bean.avatar
                                    ImManager.instance.setSelfInfo(v2TIMUserFullInfo,
                                        object : V2TIMCallback {
                                            override fun onSuccess() {
                                                Log.e(TAG, "设置用户IM信息成功")
                                            }

                                            override fun onError(p0: Int, p1: String?) {
                                                Log.e(TAG, "设置用户IM信息失败 code=${p0},msg=${p1}")
                                            }
                                        })
                                }

                            }

                            override fun onError(p0: Int, p1: String?) {

                            }

                        })
                }
            }
        })
    }

    private val loginViewModel by getViewModel(LoginViewModel::class.java) {
        sysSettingResult.observe(it, Observer {
            it?.let {
                StoredUserSources.putSettingData(it)
            }
        })
    }

    private val anchorViewModel by getViewModel(AnchorViewModel::class.java) {
        _applyResult.observe(it, Observer {
            it?.let {
                if (it.equals("3")) {
                    //未申请，去申请
                    startActivityWithTransition(AnchorApplyActivity.open(context, it))

                } else if (it.equals("1")) {
                    //通过审核
                    startActivityWithTransition(AnchorInfoActivity.open(context))

                } else if (it.equals("2")) {
                    //未通过
                    startActivityWithTransition(AnchorApplyActivity.open(context, it))
                } else {
                    //审核中
                    startActivityWithTransition(AnchorApplyActivity.open(context, it))
                }
            }
        })
    }


    override val layoutId: Int
        get() = R.layout.fragment_mine

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
        initClickListener()
        initData()
        tv_scroll.post(Runnable {
            tv_scroll.text = StoredUserSources.getSettingData()?.headlines
        })
        if (StoredUserSources.getUserInfo2()?.is_anchor == 1) {
            tv_anchor.text = "主播信息"

        } else {
            tv_anchor.text = "申请主播"
        }
        observable = RxBus.getInstance().register(RefreshInfo::class.java)
        (observable as Observable<RefreshInfo>).subscribe {
            mineViewModel.getUserInfo2(hashMapOf())

        }
    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
        mineViewModel.getUserInfo2(hashMapOf())
    }

    private fun initClickListener() {
        refreshLayout.setRefreshHeader(ClassicsHeader(context))
        refreshLayout.setOnRefreshListener {
//            mineViewModel.getUserInfo()
            mineViewModel.getUserInfo2(hashMapOf())
        }
        ib_self_info.singleClick {
            if (isNotNeedLogin(activity as MBaseActivity)) {
                startActivityWithTransition(SelfInfoActivity.instance(context))
            }
        }
        //
        cl_mipa_walnut.singleClick {
            if (isNotNeedLogin(activity as MBaseActivity)) {
                startActivityWithTransition(MipaWalletActivity.open(context,
                    MipaWalletActivity.TYPE_MIPA_WALLET))
            }
        }
        cl_mi_bean.singleClick {
            if (isNotNeedLogin(activity as MBaseActivity)) {
                startActivityWithTransition(MipaWalletActivity.open(context,
                    MipaWalletActivity.TYPE_BEAN))
            }
        }

        cl_system_set.singleClick {
            if (isNotNeedLogin(activity as MBaseActivity)) {
                startActivityWithTransition(SystemSetActivity.open(context))
            }
        }
        //意见
        cl_feedback.singleClick {
            if (isNotNeedLogin(activity as MBaseActivity)) {
                startActivityWithTransition(FeedBackActivity.open(context))
            }
        }
        //关于我们
        cl_about_me.singleClick {
            startActivityWithTransition(AboutUsActivity.open(context))
        }
        cl_office_group.singleClick {
            val settingData = StoredUserSources.getSettingData()
            settingData?.group?.let {
                context?.let { it1 -> ActivityUtils.jumpToWebView(it, it1) }
            }
        }
        //充值中心
        cl_charge_center.singleClick {
            if (isNotNeedLogin(activity as MBaseActivity)) {
                startActivityWithTransition(ChargeCenterActivity.open(context))
            }
        }

        cl_mine_extension.singleClick {
            if (isNotNeedLogin(activity as MBaseActivity)) {
                startActivityWithTransition(IWantExtensionActivity.open(context))
            }
        }

        cl_task_center.singleClick {
            if (isNotNeedLogin(activity as MBaseActivity)) {
                startActivityWithTransition(TaskCenterActivity.open(context))
            }
        }
        cl_mine_vip.singleClick {
            if (isNotNeedLogin(activity as MBaseActivity)) {
                startActivityWithTransition(MineVipActivity.open(context))
            }
        }

        cl_service_center.singleClick {
            if (isNotNeedLogin(activity as MBaseActivity)) {
                val settingData = StoredUserSources.getSettingData()
                settingData?.contact?.let {
                    context?.let { it1 -> ActivityUtils.jumpToWebView(it, it1) }
                }
            }
        }
        val get = TokenProvider.get()
        val accessImToken = get.accessImToken
        val walut = " https://www.exing103.com:1023/app/wallet/myWallet?token=" + accessImToken
        iv_game_walnut.singleClick {
            Log.e("walut====", walut)
            if (isNotNeedLogin(activity as MBaseActivity)) {
//                userInfoBean.user?.wallet_url?.let {
                    activity?.let { it1 ->
                        ActivityUtils.jumpToWebView(walut,
                            it1,
                            true,
                            webViewTitle = context!!.getString(R.string.wallet))
                    }
//                }
            }
        }
        cl_mine_zone.singleClick {
            if (isNotNeedLogin(activity as MBaseActivity)) {
                startActivityWithTransition(UserSpaceActivity.open(context))
            }
        }
        cl_charge_message.singleClick {
            if (isNotNeedLogin(activity as MBaseActivity)) {
                startActivityWithTransition(MineMessageActivity.open(context))
            }

        }
        cl_task_apply_player.singleClick {
            if (isNotNeedLogin(activity as MBaseActivity)) {
                if (StoredUserSources.getUserInfo2()?.is_anchor == 1) {
                    startActivityWithTransition(AnchorInfoActivity.open(context))

                } else {
                    //不是主播，去检查申请状态
                    anchorViewModel.getApplyState(hashMapOf())
                }
            }


            /*  PermissionHelper.request(activity!!,
                  object : PermissionHelper.PermissionCallback {
                      override fun onSuccess() {

                          startActivityWithTransition(AnchorActivity.open(context))
                      }
                  },
                  Manifest.permission.READ_EXTERNAL_STORAGE,
                  Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
                  Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECORD_AUDIO)*/
        }
        ib_mine_message.singleClick {
            if (isNotNeedLogin(activity as MBaseActivity)) {
                startActivityWithTransition(MineMessageActivity.open(context))
            }

        }
        cl_circle.singleClick {
            if (isNotNeedLogin(activity as MBaseActivity)) {
                startActivityWithTransition(CircleListActivity.open(context, CIRCLE_TYPE_USER))
            }

        }
        cl_follow.singleClick {
            if (isNotNeedLogin(activity as MBaseActivity)) {
                startActivityWithTransition(RelationActivity.open(context, RELATION_TYPE_FOLLOW))
            }

        }
        cl_fans.singleClick {
            if (isNotNeedLogin(activity as MBaseActivity)) {
                startActivityWithTransition(RelationActivity.open(context, RELATION_TYPE_FANS))

            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (TokenProvider.get().hasToken()) {
            loginViewModel.getSysSetting()
            g_name.visibility = View.GONE
            tv_user_name_logined.visibility = View.VISIBLE
        } else {
            g_name.visibility = View.VISIBLE
            tv_user_name_logined.visibility = View.GONE
            userInfo2Bean = StoredUserSources.getUserInfo2()!!
        }
//        tv_scroll.text = StoredUserSources.getSettingData()?.headlines
        if (TokenProvider.get().hasToken() && userInfoBean.user?.username.isNullOrBlank()) {
            //一二期个人信息口一起调，因为二期有些字段没有有，一期的有
            mineViewModel.getUserInfo2(hashMapOf())
            mineViewModel.getUserInfo()
        }
    }

    private fun initData() {
        if (TokenProvider.get().hasToken()) {
//            mineViewModel.getUserInfo()
            mineViewModel.getUserInfo2(hashMapOf())
        }

//        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
////            textView.text = it
//        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        observable?.let {
            RxBus.getInstance().unregister(RefreshInfo::class.java, observable!!)
        }
    }
}