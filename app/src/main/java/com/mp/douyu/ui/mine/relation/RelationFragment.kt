package com.mp.douyu.ui.mine.relation

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smart.refresh.header.ClassicsHeader
import com.mp.douyu.R
import com.mp.douyu.adapter.RelationAdapter
import com.mp.douyu.base.VisibilityFragment
import com.mp.douyu.provider.StoredUserSources
import com.mp.douyu.relation.RelationManager
import com.mp.douyu.ui.square.RelationViewModel
import com.mp.douyu.ui.square.circle.DynamicViewModel
import kotlinx.android.synthetic.main.square_circle_list.recyclerView
import kotlinx.android.synthetic.main.square_circle_list.refreshLayout
import kotlinx.android.synthetic.main.square_circle_list.warningView
import kotlinx.android.synthetic.main.title_bar_simple.*

class RelationFragment : VisibilityFragment() {
    private var currentPage: Int = 1
    private var pageSize = 10;
    protected var curSelPos: Int = -1;
    private var type: Int? = RELATION_TYPE_FOLLOW
    private var uid: Int? = 0
    override val layoutId: Int
        get() = R.layout.mine_fragment_relation

    companion object {
        const val EXTRA_RELATION_TYPE = "type"
        const val EXTRA_UID = "uid"
        const val RELATION_TYPE_FOLLOW = 0
        const val RELATION_TYPE_FANS = 1
        fun newInstance(type: Int = RELATION_TYPE_FOLLOW, uid: Int = 0): RelationFragment {
            val fragment = RelationFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_RELATION_TYPE, type)
            bundle.putInt(EXTRA_UID, uid)
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
        type = arguments?.getInt(EXTRA_RELATION_TYPE)
        uid = arguments?.getInt(EXTRA_UID)
        if (uid == 0||uid==StoredUserSources.getUserInfo2()?.id) {
            iftTitle.text = if (type == RELATION_TYPE_FOLLOW) "我关注的人" else "关注我的人"
        } else {
            iftTitle.text = if (type == RELATION_TYPE_FOLLOW) "TA的关注人" else "关注TA的人"
        }
        ibReturn.setOnClickListener {
            activity?.finish()
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        refreshLayout.setRefreshHeader(ClassicsHeader(activity))
        refreshLayout.setOnRefreshListener {
            currentPage = 1
            loadData(type)
        }
//        refreshLayout.setOnLoadMoreListener { refreshlayout ->
//            currentPage++
//            loadData()
//        }
        warningView.addOnRetryListener {
            warningView.hideWarning()
            refreshLayout.autoRefresh()
        }
        loadData(type)
    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
    }

    private fun loadData(type: Int?) {
        when (type) {
            RELATION_TYPE_FANS -> relationViewModel.getFansData(hashMapOf("uid" to "${uid}"))
            else -> relationViewModel.getAllFollowsData(hashMapOf("uid" to "${uid}"))
        }
    }

    val mAdapter by lazy {
        RelationAdapter({ position ->
            //点击"用户"头像
            onUserClick(position)
        }, activity).apply {
            setListener(object : RelationAdapter.OnRelationListener {
                override fun onItemViewClick(position: Int) {

                }

                override fun onFollow(position: Int) {
                    //关注/取消关注好友
                    when (get(position).is_follow) {
                        0 -> follow(position)
                        else -> unFollow(position)
                    }


                }
            })
        }
    }

    private fun onUserClick(position: Int) {

    }

    private fun follow(position: Int) {
        curSelPos = position
        dynamicViewModel.follow(hashMapOf("to_id" to "${mAdapter[position].id}"))
    }

    private fun unFollow(position: Int) {
        curSelPos = position
        dynamicViewModel.cancelFollow(hashMapOf("to_id" to "${mAdapter[position].id}"))
    }

    private val relationViewModel by getViewModel(RelationViewModel::class.java) {
        followsData.observe(it, Observer {
            it?.let {
                it.forEach { it1 ->
                    if (uid == 0||uid==StoredUserSources.getUserInfo2()?.id) {
                        it1.is_follow = 1
                    } else {
                        for (userBean in RelationManager.instance.follows) {
                            if (userBean.id == it1.id) {
                                it1.is_follow = 1
                                break
                            } else {
                                it1.is_follow = 0
                            }
                        }
                    }
                }
                mAdapter.addAll(it)
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
        })
        fansData.observe(it, Observer {
            it?.let { list ->
                list.forEach { it1 ->
                    for (userBean in RelationManager.instance.follows) {
                        if (userBean.id == it1.id) {
                            it1.is_follow = 1
                            break
                        } else {
                            it1.is_follow = 0
                        }
                    }
                }
                mAdapter.addAll(list.toMutableList())
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
        })
    }
    private val dynamicViewModel by getViewModel(DynamicViewModel::class.java) {
        _followResult.observe(it, Observer {
            it?.let {
                mAdapter[curSelPos].is_follow = 1
                mAdapter.notifyItemChanged(curSelPos)
//                when (type) {
//                    RELATION_TYPE_FOLLOW -> {
//                        mAdapter[curSelPos].is_follow = 0
//                        mAdapter.notifyItemChanged(curSelPos)
//                    }
//                    else -> relationViewModel.getAllFollowsData(hashMapOf())
//                }

            }
        })
        _cancelFollowResult.observe(it, Observer {
            it?.let {
                mAdapter[curSelPos].is_follow = 0
                mAdapter.notifyItemChanged(curSelPos)
//                when (type) {
//
//                }
//                relationViewModel.getAllFollowsData(hashMapOf())

            }
        })
    }
}