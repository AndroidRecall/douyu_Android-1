package com.mp.douyu.ui.home.search

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.text.TextUtils
import android.util.Log
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
import com.mp.douyu.adapter.SearchShortVideoAdapter
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.bean.*
import com.mp.douyu.closeInputMethod
import com.mp.douyu.relation.RelationManager
import com.mp.douyu.singleClick
import com.mp.douyu.ui.home.HomeViewModel
import com.mp.douyu.ui.mine.space.UserSpaceActivity
import com.mp.douyu.ui.square.circle.DynamicViewModel
import com.mp.douyu.ui.square.comment.DynamicCommentActivity
import com.mp.douyu.ui.video.SearchShortVideoActivity
import com.mp.douyu.ui.video.ShortVideoViewModel
import com.mp.douyu.utils.ToastUtils
import com.mp.douyu.utils.WindowUtils
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.recyclerView
import kotlinx.android.synthetic.main.et_search_shape.*
import kotlin.collections.ArrayList

class SearchActivity : MBaseActivity() {
    private var currentPage: Int = 1
    private var pageSize: Int = 10
    private var currentPosition = -1;
    private var listHistory: ArrayList<SearchHistoryBean> = arrayListOf()
    private var searchKey: String? = ""
    private var type: Int = SEARCH_TYPE_VIDEO
    private val gestureDetectorHolder by lazy {
        GestureDetectorHolder(this)
    }

    override val contentViewLayoutId: Int
        get() = R.layout.activity_search

    override fun initView() {
        type = intent.getIntExtra(EXTRA_DATA, SEARCH_TYPE_VIDEO)
        searchKey = intent.getStringExtra(EXTRA_KEYWORD)
        et_search.setText(searchKey)
        if (!et_search.text.toString().trim().isEmpty()) {
            researchContent(et_search.text.toString())
        }
        click_btn.visibility = View.GONE
        recyclerView.apply {
            adapter = when (type) {
                SEARCH_TYPE_SHORT_VIDEO -> mShortVideoAdapter
                SEARCH_TYPE_POST -> mPostAdapter
                else -> mAdapter
            }
            layoutManager =
                if (type == SEARCH_TYPE_SHORT_VIDEO) GridLayoutManager(this@SearchActivity,
                    3) else LinearLayoutManager(this@SearchActivity)
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
        searchKey = key
        when (type) {
            SEARCH_TYPE_SHORT_VIDEO -> shortVideoViewModel.getSearchShortData(hashMapOf("title" to searchKey,
                "list_rows" to "${pageSize}",
                "page" to "${currentPage}"))
            SEARCH_TYPE_POST -> dynamicViewModel.getSearchPost(hashMapOf("title" to searchKey,
                "list_rows" to "${pageSize}",
                "page" to "${currentPage}"))
            else -> homeViewModel.getSearchContent(hashMapOf("type" to "1",
                "kw" to key,
                "size" to "6",
                "page" to "1"))
        }

    }

    private val mAdapter by lazy {
        SearchAdapter(this) {
            when (it.id) {
                R.id.tv_more -> {
                    startActivityWithTransition(SearchVideoListActivity.open(this,
                        TypeBean("相关视频", searchKey)))
                }
            }
        }
    }
    private val mShortVideoAdapter by lazy {
        SearchShortVideoAdapter({ position ->
            toShortPage(position)
        }, this).apply {

        }

    }

    private fun toShortPage(position: Int) {
        startActivityWithTransition(SearchShortVideoActivity.open(this,
            mShortVideoAdapter.data.toList() as ArrayList<ShortVideoBean>,
            position))
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
                    startActivityWithTransition(UserSpaceActivity.open(context,
                        user?.id))
                }

                override fun onAdClick(position: Int) {

                }
            })
        }

    }
    private val homeViewModel by getViewModel(HomeViewModel::class.java) {
        _getSearchData.observe(it, Observer {
            it?.let {
                mAdapter.listVideo.clear()
                it.data?.let { it1 ->
                    mAdapter.listVideo.addAll(it1)
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
                mAdapter.changeHeader(1)

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
        startActivity(DynamicCommentActivity.open(this, mPostAdapter[position]))
    }

    private fun follow(position: Int) {
        currentPosition = position
        dynamicViewModel.follow(hashMapOf("to_id" to "${mPostAdapter[position].user?.id}"))
    }

    private fun unFollow(position: Int) {
        currentPosition = position
        dynamicViewModel.cancelFollow(hashMapOf("to_id" to "${mPostAdapter[position].user?.id}"))
    }

    private fun likePosts(position: Int) {
        currentPosition = position
        dynamicViewModel.likePosts(hashMapOf("post_id" to "${mPostAdapter[position].id}"))
    }

    private fun unLikePosts(position: Int) {
        currentPosition = position
        dynamicViewModel.unlikePost(hashMapOf("post_id" to "${mPostAdapter[position].id}"))
    }

    private val shortVideoViewModel by getViewModel(ShortVideoViewModel::class.java) {
        _searchShortData.observe(it, Observer {
            refreshLayout.finishLoadMore()
            it?.data?.let { it1 ->
                if (it1.size < pageSize) refreshLayout.finishLoadMoreWithNoMoreData()
                if (currentPage == 1) mShortVideoAdapter.clear()
                it1.forEach { shortVideoBean ->
                    setActionRelationData(shortVideoBean)
                }
                mShortVideoAdapter.addAll(it1)
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
            refreshLayout.finishLoadMore()
            it?.data?.let { it1 ->
                if (it1.size < pageSize) refreshLayout.finishLoadMoreWithNoMoreData()
                if (currentPage == 1) mPostAdapter.clear()
                it1.forEach { dynamicBean ->
                    setActionRelationData(dynamicBean)

                }
                mPostAdapter.addAll(it1)
                loadComment(mPostAdapter.data)
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
        recommendComments.observe(it, Observer {
            it?.let { it1 ->
                Log.e(TAG, "recommendComments: observe")
                addRecommendCommentData(it1)
                mPostAdapter.notifyDataSetChanged()
            }
        })

        _followResult.observe(it, Observer {
            it?.let {
                mPostAdapter[currentPosition].is_follow = 1
                mPostAdapter.notifyItemChanged(currentPosition)
//                if (it) {
//                mAdapter.data.get(curSelCircle).is_join = 1
//                mAdapter.notifyDataSetChanged()
//                }
            }
        })
        _cancelFollowResult.observe(it, Observer {
            it?.let {
                mPostAdapter[currentPosition].is_follow = 0
                mPostAdapter.notifyItemChanged(currentPosition)
//                if (it) {
//                mAdapter.list.get(curSelCircle).is_join = 1
//                mAdapter.notifyDataSetChanged()
//                }
            }
        })
        _likePostsResult.observe(it, Observer {
            it?.let {
                mPostAdapter[currentPosition].is_like = 1
                mPostAdapter.notifyItemChanged(currentPosition)
//                if (it) {
//                mAdapter.data.get(curSelCircle).is_join = 1
//                mAdapter.notifyDataSetChanged()
//                }
            }
        })
        _unlikePostResult.observe(it, Observer {
            it?.let {
                mPostAdapter[currentPosition].is_like = 0
                mPostAdapter.notifyItemChanged(currentPosition)
//                if (it) {
//                mAdapter.list.get(curSelCircle).is_join = 1
//                mAdapter.notifyDataSetChanged()
//                }
            }
        })
    }

    private fun setActionRelationData(followBean: DynamicBean) {
        //设置关注的人状态
        RelationManager.instance.follows.forEach { userBean ->
            if (userBean.id == followBean.user?.id) {
                followBean.is_follow = 1
            }
        }
        //设置点赞的文章状态
        if (RelationManager.instance.postLikes.size > 0) {
            RelationManager.instance.postLikes.forEach { postBean ->
                if (postBean.pivot?.post_id == followBean.id) {
                    followBean.is_like = 1
                }
            }
        }
    }
    private fun setActionRelationData(shortVideoBean: ShortVideoBean) {
        //设置关注的人状态
        for (userBean in RelationManager.instance.follows) {
            if (userBean.id == shortVideoBean.user?.id) {
                shortVideoBean.is_follow = 1
                break
            } else {
                shortVideoBean.is_follow = 0
            }
        }
        //设置点赞的短视频状态
        for (likeShortVideoBean in RelationManager.instance.shortVideoLikes) {
            if (likeShortVideoBean.pivot?.video_id == shortVideoBean.id) {
                shortVideoBean.is_like_short_video = 1
                break
            } else {
                shortVideoBean.is_like_short_video = 0
            }
        }
    }
    private fun addRecommendCommentData(comments: List<CommentBean>) {
        comments?.apply {
            if (size > 0) {
                val postId = comments[0].post_id
                mPostAdapter.data.forEachIndexed { index, dynamicBean ->
                    if (dynamicBean.id == postId) {
                        mPostAdapter.data[index].comments = this
                    }
                }
            }
        }
    }

    private fun loadComment(list: MutableList<DynamicBean>) {
        for (bean in list) {
            dynamicViewModel.getRecommendComment(hashMapOf("post_id" to "${bean.id}"))
        }
    }
//    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        return gestureDetectorHolder.dispatchMotionEvent(ev, tv_cancel, click_search,et_search) && super.dispatchTouchEvent(ev)
//    }


    companion object {
        private val EXTRA_DATA = "type"
        private val EXTRA_KEYWORD = "keyword"
        const val SEARCH_TYPE_VIDEO = 0
        const val SEARCH_TYPE_SHORT_VIDEO = 1
        const val SEARCH_TYPE_POST = 2
        fun open(context: Context?, type: Int = SEARCH_TYPE_VIDEO,keyword:String?=null): Intent {
            return Intent(context, SearchActivity::class.java)
                .putExtra(EXTRA_DATA, type)
                .putExtra(EXTRA_KEYWORD,keyword)
        }
    }
}
