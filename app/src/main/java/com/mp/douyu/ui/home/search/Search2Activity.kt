package com.mp.douyu.ui.home.search

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.mp.douyu.GestureDetectorHolder
import com.mp.douyu.R
import com.mp.douyu.adapter.DynamicAdapter
import com.mp.douyu.adapter.SearchDetailAdapter
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.bean.*
import com.mp.douyu.closeInputMethod
import com.mp.douyu.relation.RelationManager
import com.mp.douyu.singleClick
import com.mp.douyu.ui.anchor.live.LiveViewModel
import com.mp.douyu.ui.home.HomeViewModel
import com.mp.douyu.ui.mine.space.UserSpaceActivity
import com.mp.douyu.ui.square.RelationViewModel
import com.mp.douyu.ui.square.circle.DynamicViewModel
import com.mp.douyu.ui.square.comment.DynamicCommentActivity
import com.mp.douyu.utils.ToastUtils
import com.mp.douyu.utils.WindowUtils
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.recyclerView
import kotlinx.android.synthetic.main.et_search_shape.*
import kotlin.collections.ArrayList

class Search2Activity : MBaseActivity() {
    private var currentPage: Int = 1
    private var pageSize: Int = 100
    private var currentPosition = -1;
    private var listHistory: ArrayList<SearchHistoryBean> = arrayListOf()
    private var searchKey: String? = ""
    private var type: Int = SEARCH_TYPE_VIDEO
    private var hasLoadLives = false
    private var hasLoadVideo = false
    private var hasLoadDynamic = false
    private val gestureDetectorHolder by lazy {
        GestureDetectorHolder(this)
    }

    override val contentViewLayoutId: Int
        get() = R.layout.activity_search

    override fun initView() {
        type = intent.getIntExtra(EXTRA_DATA, SEARCH_TYPE_VIDEO)
        click_btn.visibility = View.GONE
        recyclerView.apply {
            adapter = when (type) {
                else -> mAdapter
            }
            layoutManager = LinearLayoutManager(this@Search2Activity)
        }
        rv_history.apply {
            adapter = historyAdapter
            layoutManager = GridLayoutManager(context, 5).apply {
                addItemDecoration(object : RecyclerView.ItemDecoration() {

                    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                        val viewPosition =
                            (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
                        outRect.set(WindowUtils.dip2Px(12f), WindowUtils.dip2Px(10f), 0, 0)
                    }
                })
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (listHistory[position].searchKey?.length!! > 4) {
                            2
                        } else if (listHistory[position].searchKey?.length!! > 6) {
                            3
                        } else {
                            1
                        }
                    }
                }
            }
        }
        tv_cancel.singleClick {
            finishView()
        }
        click_search.singleClick {
            val content = et_search.text.toString().trim()

            researchContent(content)
        }

        tv_clear.singleClick {
            homeViewModel.putSearchHistory(listHistory.apply {
                clear()
            })
            historyAdapter.refresh(listHistory, null)
        }
        et_search.apply {
            addTextChangedListener {
                if (et_search.text.toString().trim().isEmpty()) {
                    selectRv(false)
                }
            }
            setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val keyword = v.text.toString().trim()
                    researchContent(keyword)
                    this.closeInputMethod()
                    return@setOnEditorActionListener true
                }

                return@setOnEditorActionListener false
            }
        }
        refreshLayout.setRefreshHeader(ClassicsHeader(this))
        refreshLayout.setRefreshFooter(ClassicsFooter(this))
        refreshLayout.setOnRefreshListener {
            currentPage = 1
            searchKey?.let { researchContent(it) }

        }
        refreshLayout.setOnLoadMoreListener { refreshlayout ->
            currentPage += 1
            searchKey?.let { researchContent(it) }
        }
        selectRv(false)
    }

    private val historyAdapter by lazy {
        SearchHistoryAdapter(this) { view: View, key: String ->
//            et_search.openInputMethod()
            et_search.setText(key)
            researchContent(key)
        }
    }

    private fun researchContent(key: String) {
        if (TextUtils.isEmpty(key)) {
            ToastUtils.showToast(getString(R.string.please_input_content))
            return
        }
        mAdapter.data.clear()
        searchKey = key
        serch(key)

    }

    private fun serch(key: String) {
        hasLoadLives = false
        hasLoadVideo = false
        hasLoadDynamic = false
        liveViewModel.getSearchLivesData(hashMapOf("keywords" to searchKey))
        homeViewModel.getSearchContent(hashMapOf("type" to "1",
            "kw" to key,
            "size" to "6",
            "page" to "1"))
        dynamicViewModel.getSearchPost(hashMapOf("title" to searchKey,
            "list_rows" to "${pageSize}",
            "page" to "${currentPage}"))
//        when (type) {
//    //            SEARCH_TYPE_SHORT_VIDEO -> shortVideoViewModel.getSearchShortData(hashMapOf("title" to searchKey,
//    //                "list_rows" to "${pageSize}",
//    //                "page" to "${currentPage}"))
//            SEARCH_TYPE_POST -> dynamicViewModel.getSearchPost(hashMapOf("title" to searchKey,
//                "list_rows" to "${pageSize}",
//                "page" to "${currentPage}"))
//            SEARCH_TYPE_ANCHOR -> {
//                liveViewModel.getSearchLivesData(hashMapOf("keywords" to searchKey))
//            }
//            else -> homeViewModel.getSearchContent(hashMapOf("type" to "1",
//                "kw" to key,
//                "size" to "6",
//                "page" to "1"))
//        }
    }

    private val mAdapter by lazy {
        SearchDetailAdapter({}, this).apply {
            setOnSearchListener(object : SearchDetailAdapter.OnSearchListener {
                override fun onFollowClick(position: Int, isFollow: Int?) {
                    when (isFollow) {
                        0 -> follow(position)
                        else -> unFollow(position)
                    }
                }

                override fun onLikeClick(position: Int, isLike: Int?) {
                    when (isLike) {
                        0 -> likePosts(position)
                        else -> unLikePosts(position)
                    }
                }

                override fun onCommentClick(position: Int) {
                    comment(position)
                }

                override fun onHeaderClick(uid: Int) {
                    startActivityWithTransition(UserSpaceActivity.open(this@Search2Activity, uid))
                }

                override fun onAdClick(position: Int) {

                }

                override fun onCommentHeaderClick(user: CommonUserBean?) {
                    startActivityWithTransition(UserSpaceActivity.open(context, user?.id))
                }

                override fun onSearchMoreClick() {
                    startActivityWithTransition(SearchVideoListActivity.open(this@Search2Activity,
                        TypeBean("相关视频", searchKey)))
                }

                override fun onSearchMoreAnchorClick() {
                    //全部主播
                    startActivityWithTransition(SearchAnchorActivity.open(context, searchKey))
                }
            })
        }
//        SearchAdapter(this) {
//            when (it.id) {
//                R.id.tv_more -> {
//                    startActivityWithTransition(SearchVideoListActivity.open(this,
//                        TypeBean("相关视频", searchKey)))
//                }
//            }
//        }
    }


    private val mPostAdapter by lazy {
        DynamicAdapter({}, this).apply {
            setListener(object : DynamicAdapter.OnDynamicListener {
                override fun onFollowClick(position: Int, isFollow: Int?) {
                    when (isFollow) {
                        0 -> follow(position)
                        else -> unFollow(position)
                    }
                }

                override fun onLikeClick(position: Int, isLike: Int?) {
                    when (isLike) {
                        0 -> likePosts(position)
                        else -> unLikePosts(position)
                    }
                }

                override fun onCommentClick(position: Int) {
                    comment(position)
                }

                override fun onHeaderClick(position: Int) {
                    startActivityWithTransition(UserSpaceActivity.open(context,
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
    private val homeViewModel by getViewModel(HomeViewModel::class.java) {
        _getSearchData.observe(it, Observer {
            refreshLayout.finishRefresh()
            refreshLayout.finishLoadMoreWithNoMoreData()
//                mAdapter.listVideo.clear()
            it?.data?.let { it1 ->
                var start = 0
                if (hasLoadLives) {
                    start = 2
                }
                mAdapter.data.add(start, SearchDetailBean(title = "相关视频", isMore = true))
                mAdapter.data.add(start + 1, SearchDetailBean(videos = it1))
                mAdapter.notifyItemRangeChanged(start, 2)
                mAdapter.notifyDataSetChanged()
                hasLoadVideo = true
                selectRv(true)
                val searchHistoryBean = SearchHistoryBean(et_search.text.toString().trim())
                if (it1.isNotEmpty()) {
                    listHistory.map {
                        if (searchHistoryBean.searchKey == it.searchKey) return@let
                    }
                    putSearchHistory(listHistory.apply {
                        add(searchHistoryBean)
                    })
                }
            }

        })
        _putSearchHistory.observe(it, Observer {
            it?.let {

            }
        })
        _getSearchHistoryData.observe(it, Observer {
            it?.let {
                listHistory.clear()
                listHistory.addAll(it)
                historyAdapter.refresh(listHistory, null)
            }
        })
    }

    private fun selectRv(b: Boolean) {
        g_history.visibility = if (b) View.GONE else View.VISIBLE
        recyclerView.visibility = if (!b) View.GONE else View.VISIBLE
        if (!b) {
            homeViewModel.getSearchHistory()
        }
    }

    private fun comment(position: Int) {
        startActivity(DynamicCommentActivity.open(this, mAdapter[position].dynamicBean!!))
    }

    private fun follow(position: Int) {
        currentPosition = position
        dynamicViewModel.follow(hashMapOf("to_id" to "${mAdapter[position].dynamicBean?.user?.id}"))
    }

    private fun unFollow(position: Int) {
        currentPosition = position
        dynamicViewModel.cancelFollow(hashMapOf("to_id" to "${mAdapter[position].dynamicBean?.user?.id}"))
    }

    private fun likePosts(position: Int) {
        currentPosition = position
        dynamicViewModel.likePosts(hashMapOf("post_id" to "${mAdapter[position].id}"))
    }

    private fun unLikePosts(position: Int) {
        currentPosition = position
        dynamicViewModel.unlikePost(hashMapOf("post_id" to "${mAdapter[position].id}"))
    }

    //    private val shortVideoViewModel by getViewModel(ShortVideoViewModel::class.java) {
//        _searchShortData.observe(it, Observer {
//            refreshLayout.finishLoadMore()
//            it?.data?.let { it1 ->
//                if (it1.size < pageSize) refreshLayout.finishLoadMoreWithNoMoreData()
//                if (currentPage == 1) mShortVideoAdapter.clear()
//                mShortVideoAdapter.addAll(it1)
//                selectRv(true)
//                val searchHistoryBean = SearchHistoryBean(et_search.text.toString().trim())
//                if (it1.isNotEmpty()) {
//                    listHistory.map {
//                        if (searchHistoryBean.searchKey == it.searchKey) return@let
//                    }
//                    homeViewModel.putSearchHistory(listHistory.apply {
//                        add(searchHistoryBean)
//                    })
//                }
//            }
//        })
//    }
    private val liveViewModel by getViewModel(LiveViewModel::class.java) {
        _searchLivesData.observe(it, Observer {
            refreshLayout.finishRefresh()
            refreshLayout.finishLoadMoreWithNoMoreData()
            it?.let { it1 ->
                if (it1.isNotEmpty()) {
                    mAdapter.data.add(0, SearchDetailBean(title = "相关主播"))
                    mAdapter.data.add(1, SearchDetailBean(lives = it1.toMutableList()))
                    hasLoadLives = true
                    var start = 0
                    mAdapter.notifyItemRangeChanged(start, 2)
                    mAdapter.notifyDataSetChanged()
                }
                selectRv(true)
                val searchHistoryBean = SearchHistoryBean(et_search.text.toString().trim())
                if (it1.isNotEmpty()) {
                    listHistory.map {
                        if (searchHistoryBean.searchKey == it.searchKey) return@let
                    }
                    homeViewModel.putSearchHistory(listHistory.apply {
                        add(searchHistoryBean)
                    })
                }
            }
        })
    }
    private val dynamicViewModel by getViewModel(DynamicViewModel::class.java) {
        _searchResult.observe(it, Observer {
            refreshLayout.finishRefresh()
            refreshLayout.finishLoadMoreWithNoMoreData()
            it?.data?.let { it1 ->
                if (it1.isNotEmpty()) {
                    if (it1.size < pageSize) refreshLayout.finishLoadMoreWithNoMoreData()
//                if (currentPage == 1) mPostAdapter.clear()
                    mAdapter.data.add(SearchDetailBean(title = "相关帖子"))
                    it1.forEach { dynamicBean ->
                        setActionRelationData(dynamicBean)
                        mAdapter.data.add(SearchDetailBean(dynamicBean = dynamicBean))
                    }
                    var start = 0
                    if (hasLoadLives) {
                        start += 2
                    }
                    if (hasLoadVideo) {
                        start += 2
                    }
                    mAdapter.notifyItemRangeChanged(start, it1.size)
                    hasLoadDynamic = true
                }
//                loadComment(mPostAdapter.data)
                selectRv(true)
                val searchHistoryBean = SearchHistoryBean(et_search.text.toString().trim())
                if (it1.isNotEmpty()) {
                    listHistory.map {
                        if (searchHistoryBean.searchKey == it.searchKey) return@let
                    }
                    homeViewModel.putSearchHistory(listHistory.apply {
                        add(searchHistoryBean)
                    })
                }
            }
        })

        _followResult.observe(it, Observer {
            it?.let {
                relationViewModel.getAllFollowsData(hashMapOf())

            }
        })
        _cancelFollowResult.observe(it, Observer {
            it?.let {
                relationViewModel.getAllFollowsData(hashMapOf())
            }
        })
        _likePostsResult.observe(it, Observer {
            it?.let {
                relationViewModel.getAllPostLikesData(hashMapOf())

            }
        })
        _unlikePostResult.observe(it, Observer {
            it?.let {
                relationViewModel.getAllPostLikesData(hashMapOf())

            }
        })
    }


    private fun loadComment(list: MutableList<DynamicBean>) {
        for (bean in list) {
            dynamicViewModel.getRecommendComment(hashMapOf("post_id" to "${bean.id}"))
        }
    }

    private val relationViewModel by getViewModel(RelationViewModel::class.java) {
        followsData.observe(it, Observer {

            it?.let {
                RelationManager.instance.follows = it.toMutableList()
                for ((index, searchData) in mAdapter.data.withIndex()) {
                    val initialIsLike = searchData.dynamicBean?.is_like
                    val initialIsFollow = searchData.dynamicBean?.is_follow
                    if (it.isEmpty()) searchData.dynamicBean?.is_follow = 0
                    else {
                        searchData.dynamicBean?.let {
                            setActionRelationData(searchData.dynamicBean!!)
                        }
                    }
                    if (initialIsLike != searchData.dynamicBean?.is_like || initialIsFollow != searchData.dynamicBean?.is_follow) {
                        mAdapter.notifyItemChanged(index)
                    }
                }

            }
        })
        postLikesData.observe(it, Observer {
            it?.data?.let { it ->
                RelationManager.instance.postLikes = it.toMutableList()
                for ((index, searchData) in mAdapter.data.withIndex()) {
                    val initialIsLike = searchData.dynamicBean?.is_like
                    val initialIsFollow = searchData.dynamicBean?.is_follow
                    if (it.isEmpty()) searchData.dynamicBean?.is_like = 0
                    else {
                        searchData.dynamicBean?.let {
                            setActionRelationData(searchData.dynamicBean!!)
                        }
                    }
                    if (initialIsLike != searchData.dynamicBean?.is_like || initialIsFollow != searchData.dynamicBean?.is_follow) {
                        mAdapter.notifyItemChanged(index)
                    }
                }
            }
        })
    }

    private fun setActionRelationData(dynamicBean: DynamicBean) {
        //设置关注的人状态
        for (userBean in RelationManager.instance.follows) {
            if (userBean.id == dynamicBean.user?.id) {
                dynamicBean.is_follow = 1
                break
            } else {
                dynamicBean.is_follow = 0
            }
        }
        if (RelationManager.instance.postLikes.size > 0) {
            for (postBean in RelationManager.instance.postLikes) {
                if (postBean.pivot?.post_id == dynamicBean.id) {
                    dynamicBean.is_like = 1
                    break
                } else {
                    dynamicBean.is_like = 0

                }
            }
        }

    }

    companion object {
        private val EXTRA_DATA = "type"
        const val SEARCH_TYPE_VIDEO = 0

        fun open(context: Context?, type: Int = SEARCH_TYPE_VIDEO): Intent {
            return Intent(context, Search2Activity::class.java).putExtra(EXTRA_DATA, type)
        }
    }
}
