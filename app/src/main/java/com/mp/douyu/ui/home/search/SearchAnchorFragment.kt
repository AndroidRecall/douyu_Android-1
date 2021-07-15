package com.mp.douyu.ui.home.search

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smart.refresh.header.ClassicsHeader
import com.mp.douyu.R
import com.mp.douyu.adapter.SearchAnchorDetailAdapter
import com.mp.douyu.base.VisibilityFragment
import com.mp.douyu.relation.RelationManager
import com.mp.douyu.ui.anchor.live.LiveViewModel
import com.mp.douyu.ui.square.RelationViewModel
import com.mp.douyu.ui.square.circle.DynamicViewModel
import kotlinx.android.synthetic.main.square_circle_list.recyclerView
import kotlinx.android.synthetic.main.square_circle_list.refreshLayout
import kotlinx.android.synthetic.main.title_bar_simple.*

class SearchAnchorFragment : VisibilityFragment() {
    private var currentPage: Int = 1
    private var pageSize = 100;
    protected var curSelPos: Int = -1;
    private var keyword: String? = null
    override val layoutId: Int
        get() = R.layout.search_fragment_anchor

    companion object {
        const val EXTRA_KEYWORD = "keyword"
        const val RELATION_TYPE_FOLLOW = 0
        const val RELATION_TYPE_FANS = 1
        fun newInstance(keyword: String? = null): SearchAnchorFragment {
            val fragment = SearchAnchorFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_KEYWORD, keyword)
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
        keyword = arguments?.getString(EXTRA_KEYWORD)!!

        iftTitle.text = "相关用户"
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
            loadData(keyword)
        }
//        refreshLayout.setOnLoadMoreListener { refreshlayout ->
//            currentPage++
//            loadData()
//        }
        loadData(keyword)
    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
    }

    private fun loadData(keyword: String?) {
       liveViewModel.getSearchLivesData(hashMapOf("keywords" to keyword,
        "list_rows" to "${pageSize}",
        "page" to "${currentPage}"))

    }

    val mAdapter by lazy {
        SearchAnchorDetailAdapter({ position ->
            //点击"用户"头像
            onUserClick(position)
        }, activity).apply {
            setListener(object : SearchAnchorDetailAdapter.OnAnchorListener {
                override fun onAnchorList() {

                }

                override fun onHeaderClick(userId: Int?) {
                }

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
                RelationManager.instance.follows = it.toMutableList()
                it.forEach { it1 ->
                    it1.is_follow = 1
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

            }
        })
    }
    private val liveViewModel by getViewModel(LiveViewModel::class.java) {
        _searchLivesData.observe(it, Observer {
            refreshLayout.finishRefresh()
            refreshLayout.finishLoadMoreWithNoMoreData()
            it?.let { list ->
                list.forEach {it1 ->
                    for (userBean in RelationManager.instance.follows) {
                        if (userBean.id == it1.anchor?.user_id) {
                            it1.is_follow = 1
                            break
                        } else {
                            it1.is_follow = 0
                        }
                    }
                }
                mAdapter.addAll(list)
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