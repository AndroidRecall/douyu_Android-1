package com.mp.douyu.ui.anchor.invite

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.lxj.xpopup.XPopup
import com.mp.douyu.BuildConfig
import com.mp.douyu.GlobeStatusViewHolder
import com.mp.douyu.R
import com.mp.douyu.adapter.TabNavigatorAdapter
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.base.VisibilityFragment
import com.mp.douyu.bean.InviteCityBean
import com.mp.douyu.bean.MiniTabBean
import com.mp.douyu.dialog.JoinHookDialog
import com.mp.douyu.singleClick
import com.mp.douyu.ui.anchor.AnchorViewModel
import com.mp.douyu.ui.anchor.live.LiveViewModel
import com.mp.douyu.ui.home.HomeViewModel
import com.mp.douyu.ui.mine.task.TaskCenterActivity
import com.mp.douyu.view.popupwindow.BaseFragmentPagerAdapter
import kotlinx.android.synthetic.main.invite_fragment_detail.*
import kotlinx.android.synthetic.main.invite_fragment_detail.ibReturn
import kotlinx.android.synthetic.main.invite_fragment_detail.indicator
import kotlinx.android.synthetic.main.invite_fragment_detail.iv_top_bg
import kotlinx.android.synthetic.main.invite_fragment_detail.refreshLayout
import kotlinx.android.synthetic.main.invite_fragment_detail.statusBarView
import kotlinx.android.synthetic.main.invite_fragment_detail.view_pager
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

class InviteCityDetailFragment(var hookId: Int? = 0) : VisibilityFragment() {
    var inviteCityBean: InviteCityBean? = null
    private val fragments: Array<VisibilityFragment> by lazy {
        arrayOf(InviteCityDescriptionFragment(hookId!!),
            InviteCityRecordFragment.newInstance(hookId!!))
    }
    private val tabBeans: List<MiniTabBean> by lazy {
        val descTabBean = MiniTabBean("同城约炮说明")
        val recordTabBean = MiniTabBean("同城约炮记录")
        listOf(descTabBean, recordTabBean)
    }
    private val mCommonNavigatorAdapter: TabNavigatorAdapter by lazy {
        TabNavigatorAdapter(tabBeans, object : TabNavigatorAdapter.OnTabItemClickListener {
            override fun onTabItemClick(position: Int) {
                view_pager?.currentItem = position
            }
        })
    }

    companion object {
        private val EXTRA_DATA: String? = "data"

        fun newInstance(hookId: Int): InviteCityDetailFragment {
            val fragment = InviteCityDetailFragment(hookId)
            val bundle = Bundle()
            bundle.putInt(EXTRA_DATA, hookId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override val layoutId: Int
        get() = R.layout.invite_fragment_detail


    override fun initView() {

    }

    override fun onVisible() {
        super.onVisible()
    }

    override fun onInvisible() {
        super.onInvisible()
    }

    override fun onVisibleFirst() {
        BarUtils.setStatusBarVisibility(activity!!, false)
        BarUtils.setNavBarColor(activity!!, resources.getColor(R.color.color00000000))
        BarUtils.setStatusBarLightMode(activity!!, false)
        statusBarView.layoutParams.height = BarUtils.getStatusBarHeight()
        iv_header.pivotX = (iv_header.width / 2).toFloat()
        iv_header.pivotY = (iv_header.height / 2).toFloat()
        iv_header.rotation = -40f
        hookId = arguments?.getInt(EXTRA_DATA)
        view_pager?.apply {
            adapter = BaseFragmentPagerAdapter(childFragmentManager, fragments.toList())
            offscreenPageLimit = 2
            currentItem = 0
        }
        mCommonNavigatorAdapter?.apply {
            selectedColor = resources.getColor(R.color.TabSelect)
            normalColor = resources.getColor(R.color.tab_normal_color)
            val commonNavigator = CommonNavigator(context)
            selectedSize = 16
            normalSize = 14
            commonNavigator.adapter = this
            indicator.navigator = commonNavigator
            ViewPagerHelper.bind(indicator, view_pager)
        }
        refreshLayout.setOnRefreshListener {
            loadData()
        }
        loadData()
        tv_join.singleClick {
            if (inviteCityBean?.rest == 0){
                ToastUtils.showShort("本期目标达成")
                return@singleClick
            }
            inviteCityBean?.let {
                XPopup.Builder(activity).asCustom(JoinHookDialog(activity!!,
                    inviteCityBean!!,
                    object : JoinHookDialog.OnDialogListener {
                        override fun onObtain() {
                            if (GlobeStatusViewHolder.isNotNeedLogin(activity as MBaseActivity)) {
                                startActivityWithTransition(TaskCenterActivity.open(context))
                            }
                        }

                        override fun onJoinHook(number: Int) {
                            if (inviteCityBean?.rest == 0){
                                ToastUtils.showShort("本期目标达成")
                                return
                            }
                            liveViewModel.joinHook(hashMapOf("hook_id" to "${hookId}",
                                "issue" to "${inviteCityBean?.issue}",
                                "number" to "${number}"))
                        }
                    })).show()
            }
//            (activity as MBaseActivity).showLoadingView(true)
//            if (GlobeStatusViewHolder.isNotNeedLogin(activity as MBaseActivity)) homeViewModel.getGameLink(
//                hashMapOf("game_id" to "0"))
        }
        initOnClickListener()
    }

    private fun loadData() {
        anchorViewModel.getAllCityInviteDetailData(hashMapOf("hook_id" to "$hookId"))

        anchorViewModel.getAllHookRecordData(hashMapOf("list_rows" to "${1}",
            "page" to "${1}",
            "hook_id" to "${hookId}"))
    }

    private fun initOnClickListener() {

        ibReturn.singleClick {
            finishView()
        }
    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
    }

    val anchorViewModel by getViewModel(AnchorViewModel::class.java) {
        cityInviteDetailData.observe(it, Observer {
            refreshLayout.finishRefresh()
            refreshLayout?.finishLoadMore()
            it?.let {
                //详情
                inviteCityBean = it
                tv_title.text = "同城约炮*第${it.issue}期"
                tv_task_title.text = it.title

                tv_remain.text = if (it.rest == 0) "目标达成!" else "剩余${it.rest}份"
                tv_task_num.text = "需要${it.total}份"
                if (it.image?.contains("http") != true) {
                    it.image = BuildConfig.IMAGE_BASE_URL + it.image
                }
                context?.let { it1 ->
                    Glide.with(it1).load(it.image)
                        .placeholder(R.mipmap.login_code_failed).centerCrop().into(iv_top_bg)
                }

                tv_rule_title.text = it.n_title
                tv_rule_content.text = it.n_content
                progressBar.max = it.total!!
                progressBar.progress = it.total!! - it.rest!!

                (fragments[0] as InviteCityDescriptionFragment).updateData(it)
            }
        })
        hookRecordData.observe(it, Observer {
            it?.let {
                it.data.let { it1 ->
                    if (it1.isNotEmpty()) {
                        cl_last_issue.visibility = View.VISIBLE
                        if (it1[0].user?.avatar?.contains("http") != true) {
                            it1[0].user?.avatar = BuildConfig.IMAGE_BASE_URL + it1[0].user?.avatar
                        }
                        context?.let { context ->
                            Glide.with(context).load(it1[0].user?.avatar).circleCrop()
                                .placeholder(R.mipmap.default_avatar).into(iv_avatar)
                        }
                        tv_luck_num.text = "幸运号码:${it1[0].luck_number}"
                        tv_issue.text = "中奖期数:第${it1[0].issue}期"
                        tv_time.text = "开奖时间:${it1[0].update_time}"
                        tv_nickname.text = "用户昵称:${it1[0].nickname}"
                    } else {
                        cl_last_issue.visibility = View.GONE
                    }

                }
            }
        })
    }
    val homeViewModel by getViewModel(HomeViewModel::class.java) {

    }
    val liveViewModel by getViewModel(LiveViewModel::class.java) {
        _joinHookResult.observe(it, Observer {
            loadData()
        })
    }
}