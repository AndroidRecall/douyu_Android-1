package com.swbg.mlivestreaming.ui.mine.msg

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.blankj.utilcode.util.BarUtils
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.adapter.MineMessageAdapter
import com.swbg.mlivestreaming.base.VisibilityFragment
import com.swbg.mlivestreaming.bean.*
import com.swbg.mlivestreaming.provider.StoredUserSources
import com.swbg.mlivestreaming.relation.RelationManager
import com.swbg.mlivestreaming.ui.mine.MineViewModel
import com.swbg.mlivestreaming.ui.mine.space.UserSpaceActivity
import com.swbg.mlivestreaming.ui.square.circle.DynamicViewModel
import com.swbg.mlivestreaming.ui.square.comment.DynamicCommentActivity
import kotlinx.android.synthetic.main.fragment_activity.refreshLayout
import kotlinx.android.synthetic.main.square_activity_dymaic.*


class MineMessageDetailFragment(var type: Int = TYPE_MINE_LIKED, var uid: Int = 0) :
    VisibilityFragment() {
    private var currentPage: Int = 1
    private var pageSize: Int = 20
    private var currentPosition = -1;

    companion object {
        const val TYPE_MINE_REPLY = 1//回复
        const val TYPE_MINE_LIKED = 2   //点赞

        const val TYPE_MSG_SYSTEM = 3//系统消息
        const val DYNAMIC_TYPE = "dynamic_type"
        const val EXTRA_UID = "uid"
        fun newInstance(type: Int = TYPE_MINE_LIKED, uid: Int = 0): MineMessageDetailFragment {
            val fragment = MineMessageDetailFragment()
            val bundle = Bundle()
            bundle.putInt(DYNAMIC_TYPE, type)
            bundle.putInt(EXTRA_UID, uid)
            fragment.arguments = bundle
            return fragment
        }
    }

    override val layoutId: Int
        get() = R.layout.square_activity_dymaic
    val mAdapter by lazy {
        MineMessageAdapter({}, context, type).apply {
            setListener(object : MineMessageAdapter.OnDynamicListener {
                override fun onFollowClick(position: Int, isFollow: Int?) {


                }

                override fun onLikeClick(position: Int, isLike: Int?) {

                }

                override fun onCommentClick(position: Int) {
                    comment(position)

                }

                override fun onHeaderClick(position: Int) {
                    startActivityWithTransition(UserSpaceActivity.open(activity!!,
                        get(position).user?.id))
                }

                override fun onCommentHeaderClick(user: CommonUserBean?) {
                    startActivityWithTransition(UserSpaceActivity.open(context, user?.id))
                }

                override fun onAdClick(position: Int) {

                }
            })
        }
    }

    private fun comment(position: Int) {

        mAdapter.data[position].post?.user = CommonUserBean(avatar = StoredUserSources.getUserInfo2()?.avatar,
            nickname = StoredUserSources.getUserInfo2()?.nickname,
            id = StoredUserSources.getUserInfo2()?.id)
        startActivity(DynamicCommentActivity.open(activity, mAdapter.data[position].post!!))
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
        BarUtils.setNavBarColor(activity!!, resources.getColor(R.color.color00000000))
        BarUtils.setStatusBarLightMode(activity!!, false)
        type = arguments?.getInt(DYNAMIC_TYPE, TYPE_MINE_LIKED)!!
        uid = arguments?.getInt(EXTRA_UID)!!
        recyclerView.apply {
            adapter = mAdapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            layoutManager = LinearLayoutManager(context)

        }
        refreshLayout.setRefreshHeader(ClassicsHeader(context))
        refreshLayout.setRefreshFooter(ClassicsFooter(context))
        refreshLayout.setOnRefreshListener {
            currentPage = 1
            loadData(type)
        }
        refreshLayout.setOnLoadMoreListener { refreshlayout ->
            currentPage++
            loadData(type)
        }
        loadData(type)
        warningView.addOnRetryListener {
            warningView.hideWarning()
            currentPage = 1
            loadData(type)
        }
    }

    private fun loadData(type: Int) {
        when (type) {
            TYPE_MINE_REPLY -> mineViewModel.getUserReplyData(hashMapOf("list_rows" to "${pageSize}",
                "page" to "${currentPage}"))
            TYPE_MINE_LIKED -> mineViewModel.getUserLikeData(hashMapOf("list_rows" to "${pageSize}",
                "page" to "${currentPage}"))
            TYPE_MSG_SYSTEM -> {
                warningView.showOtherWarning(R.mipmap.icon_empty_issue, R.string.empty)
            }
        }

    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
    }

    private val mineViewModel by getViewModel(MineViewModel::class.java) {
        userReplyData.observe(it, Observer { it1 ->
            setData(it1)
        })
        userLikeData.observe(it, Observer { it1 ->
            setData(it1)
        })
    }

    private fun setData(it: List<MineMessageBean>?) {
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadMore()
        it?.let { it1 ->
            if (it1.size < pageSize) refreshLayout.finishLoadMoreWithNoMoreData()

            if (currentPage == 1) mAdapter.data.clear()
            mAdapter.data.addAll(it1)
            mAdapter.notifyDataSetChanged()
        }
        if (mAdapter.size == 0) {
            if (it == null) {
                warningView.showNoNetWorkWarning()
            } else {
                warningView.showOtherWarning(R.mipmap.icon_empty_issue, R.string.empty)
            }
        } else {
            warningView.hideWarning()
        }
    }


    fun refreshData() {
        currentPage = 1
        loadData(type)
    }
}