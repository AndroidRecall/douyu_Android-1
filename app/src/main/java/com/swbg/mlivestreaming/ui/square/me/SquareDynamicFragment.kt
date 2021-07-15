package com.swbg.mlivestreaming.ui.square.me

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import cn.jzvd.Jzvd
import com.blankj.utilcode.util.BarUtils
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.adapter.DynamicAdapter
import com.swbg.mlivestreaming.adapter.DynamicAdapter.Companion.TYPE_ADV_CONTENT
import com.swbg.mlivestreaming.base.VisibilityFragment
import com.swbg.mlivestreaming.bean.*
import com.swbg.mlivestreaming.relation.RelationManager
import com.swbg.mlivestreaming.ui.mine.space.UserSpaceActivity
import com.swbg.mlivestreaming.ui.square.RelationViewModel
import com.swbg.mlivestreaming.ui.square.circle.DynamicViewModel
import com.swbg.mlivestreaming.ui.square.comment.DynamicCommentActivity
import kotlinx.android.synthetic.main.fragment_activity.refreshLayout
import kotlinx.android.synthetic.main.square_activity_dymaic.*
import kotlinx.android.synthetic.main.square_activity_dymaic.recyclerView


class SquareDynamicFragment(var type: Int = TYPE_MINE_FOLLOWED, var id: Int? = 0) :
    VisibilityFragment() {
    private var currentPage: Int = 1
    private var pageSize: Int = 20
    private var currentPosition = -1;

    companion object {
        //“我的”
        const val TYPE_MINE_FOLLOWED = 0//关注
        const val TYPE_MINE_LIKED = 1   //点赞
        const val TYPE_MINE_COMMENTED = 2//评论
        const val TYPE_MINE_PUBLISHED = 3//发布

        //推荐
        const val TYPE_MINE_RECOMMEND = 4//推荐

        //圈子
        const val TYPE_CIRCLE_ALL = 5//全部
        const val TYPE_CIRCLE_ELITE = 6//精华

        //个人空间
        const val TYPE_SPACE_PUBLISH = 7//发布
        const val TYPE_SPACE_REPLY = 8//回复
        const val TYPE_SPACE_COLLECT = 9//收藏
        const val TYPE_SPACE_RECORD = 10//足迹

        const val TYPE_MSG_SYSTEM = 11//系统消息
        const val DYNAMIC_TYPE = "dynamic_type"
        const val EXTRA_ID = "id"
        fun newInstance(type: Int = TYPE_MINE_FOLLOWED, id: Int? = 0): SquareDynamicFragment {
            val fragment = SquareDynamicFragment()
            val bundle = Bundle()
            bundle.putInt(DYNAMIC_TYPE, type)
            id?.let { bundle.putInt(EXTRA_ID, it) }
            fragment.arguments = bundle
            return fragment
        }
    }

    override val layoutId: Int
        get() = R.layout.square_activity_dymaic
    val mAdapter by lazy {
        DynamicAdapter({}, context, type).apply {
            //数据刷新混乱及解决方案
//            setHasStableIds(false)
            setListener(object : DynamicAdapter.OnDynamicListener {
                override fun onFollowClick(position: Int, isFollow: Int?) {
                    when (isFollow) {
                        0 -> follow(position)
                        else -> unFollow(position)
                    }

                }

                override fun onLikeClick(position: Int, isLike: Int?) {
                    if (type == TYPE_SPACE_COLLECT) return
                    when (isLike) {
                        0 -> likePosts(position)
                        else -> unLikePosts(position)
                    }
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
        startActivity(DynamicCommentActivity.open(activity, mAdapter.data[position]))
    }

    private fun follow(position: Int) {
        currentPosition = position
        dynamicViewModel.follow(hashMapOf("to_id" to "${mAdapter.data[position].user?.id}"))
    }

    private fun unFollow(position: Int) {
        currentPosition = position
        dynamicViewModel.cancelFollow(hashMapOf("to_id" to "${mAdapter.data[position].user?.id}"))
    }

    private fun likePosts(position: Int) {
        currentPosition = position
//        ToastUtils.showShort("点赞post_id=${mAdapter.data[position].id}")
        dynamicViewModel.likePosts(hashMapOf("post_id" to "${mAdapter.data[position].id}"))
    }

    private fun unLikePosts(position: Int) {
        currentPosition = position
//        ToastUtils.showShort("取消点赞post_id=${mAdapter.data[position].id}")
        dynamicViewModel.unlikePost(hashMapOf("post_id" to "${mAdapter.data[position].id}"))
    }

    override fun initView() {
        Log.e(TAG, "SquareDynamicFragment  initView")
    }

    override fun onVisible() {
        super.onVisible()
        Log.e(TAG, "SquareDynamicFragment  onVisible")
    }

    override fun onInvisible() {
        super.onInvisible()
        Log.e(TAG, "SquareDynamicFragment  onInvisible")
    }

    override fun onVisibleFirst() {
        Log.e(TAG, "SquareDynamicFragment  onVisibleFirst")
        BarUtils.setNavBarColor(activity!!, resources.getColor(R.color.color00000000))
        BarUtils.setStatusBarLightMode(activity!!, false)
        type = arguments?.getInt(DYNAMIC_TYPE, TYPE_MINE_FOLLOWED)!!
        id = arguments?.getInt(EXTRA_ID)!!
        recyclerView.apply {
            adapter = mAdapter
            //禁止局部刷新动画
            setItemViewCacheSize(0)
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (Jzvd.CURRENT_JZVD == null) return
                    val currentPlayPosition = Jzvd.CURRENT_JZVD.positionInList
                    val firstPosition =
                        (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    val lastPosition =
                        (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    if (currentPlayPosition >= 0) {
                        if (currentPlayPosition < firstPosition || currentPlayPosition > lastPosition) {
                            if (Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                                Jzvd.releaseAllVideos() //为什么最后一个视频横屏会调用这个，其他地方不会
                            }
                        }
                    }
                }
            })
        }
        refreshLayout.setRefreshFooter(ClassicsFooter(context))

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
            TYPE_MINE_FOLLOWED -> dynamicViewModel.getAllFollowDynamicData(hashMapOf("list_rows" to "${pageSize}",
                "page" to "${currentPage}",
                "uid" to "$id"))
            TYPE_MINE_LIKED -> dynamicViewModel.getAllLikeDynamicData(hashMapOf("list_rows" to "${pageSize}",
                "page" to "${currentPage}",
                "uid" to "$id"))
            TYPE_MINE_COMMENTED -> dynamicViewModel.getAllCommentDynamicData(hashMapOf("list_rows" to "${pageSize}",
                "page" to "${currentPage}",
                "uid" to "$id"))
            TYPE_MINE_PUBLISHED -> dynamicViewModel.getAllPostDynamicData(hashMapOf("list_rows" to "${pageSize}",
                "page" to "${currentPage}",
                "uid" to "$id"))
            TYPE_MINE_RECOMMEND -> dynamicViewModel.getAllRecommendPostsData(hashMapOf("list_rows" to "${pageSize}",
                "page" to "${currentPage}",
                "uid" to "$id"))
            TYPE_CIRCLE_ALL ->/* dynamicViewModel.getAllRecommendPostsData(hashMapOf("list_rows" to "${pageSize}",
                "page" to "${currentPage}",
                "uid" to "$uid"))*/
                dynamicViewModel.getSearchPost(hashMapOf("circle_id" to "${id}",
                    "list_rows" to "${100}",
                    "page" to "${currentPage}"))
            TYPE_CIRCLE_ELITE -> /*dynamicViewModel.getAllRecommendPostsData(hashMapOf("list_rows" to "${pageSize}",
                "page" to "${currentPage}",
                "uid" to "$id"))*/
                dynamicViewModel.getSearchPost(hashMapOf("circle_id" to "${id}",
                    "list_rows" to "${pageSize}",
                    "page" to "${currentPage}"))
            TYPE_SPACE_PUBLISH -> dynamicViewModel.getUserDynamicData(hashMapOf("list_rows" to "${pageSize}",
                "page" to "${currentPage}",
                "uid" to "$id"))
            TYPE_SPACE_REPLY -> dynamicViewModel.getAllReplyPostData(hashMapOf("list_rows" to "${pageSize}",
                "page" to "${currentPage}",
                "uid" to "$id"))
            TYPE_SPACE_COLLECT -> dynamicViewModel.getAllPostLikesData(hashMapOf("list_rows" to "${pageSize}",
                "page" to "${currentPage}",
                "uid" to "$id"))
            TYPE_SPACE_RECORD -> dynamicViewModel.getAllRecordPostData(hashMapOf("list_rows" to "${pageSize}",
                "page" to "${currentPage}",
                "uid" to "$id"))
            TYPE_MSG_SYSTEM -> {
                warningView.showOtherWarning(R.mipmap.icon_empty_issue, R.string.empty)
            }
        }

    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
        Log.e(TAG, "SquareDynamicFragment  onVisibleExceptFirst")
        relationViewModel.getAllFollowsData(hashMapOf())
        relationViewModel.getAllPostLikesData(hashMapOf())
    }

    private val dynamicViewModel by getViewModel(DynamicViewModel::class.java) {
        //我关注过的
        followDynamicData.observe(it, Observer {
            setData(it)
        })
        //点赞过的文章
        likeDynamicData.observe(it, Observer {
            setData(it)
        })
        //我评论过的
        commentDynamicData.observe(it, Observer {
            setData(it)

        })
        //我发布过的
        postDynamicData.observe(it, Observer {
            setData(it)
        })
        //我回复过的
        replyPostData.observe(it, Observer {
            setData(it)
        })
        //我回复过的
        recordPostsData.observe(it, Observer {
            setData(it)
        })
        //推荐文章列表
        recommendPostsData.observe(it, Observer {
            setData(it)
        })
        //帖子发布列表(个人空间)
        _userDynamicData.observe(it, Observer {
            setData(it)
        })
        //我喜欢的文章
        postLikesData.observe(it, Observer {
            setData(it)

        })
        _searchResult.observe(it, Observer {
            refreshLayout.finishLoadMore()
            it?.data?.let { it1 ->
                var list: MutableList<DynamicBean> = arrayListOf()
                if (type == TYPE_CIRCLE_ELITE) {
                    it1.forEach {
                        if (it.is_elite == 1) {
                            list.add(it)
                        }
                    }
                } else {
                    list.addAll(it1)
                }
                if (list.size < pageSize) refreshLayout.finishLoadMoreWithNoMoreData()

                list.forEach { dynamicBean ->
                    setActionRelationData(dynamicBean)

                }
                if (currentPage == 1) mAdapter.data.clear()
                mAdapter.data.addAll(list)
                mAdapter.notifyDataSetChanged()
                //                loadComment(mAdapter.data)
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
        })
        recommendComments.observe(it, Observer {
            it?.let { it1 ->
                Log.e(TAG, "recommendComments: observe")
                addRecommendCommentData(it1)
                mAdapter.notifyDataSetChanged()
            }
        })
        _followResult.observe(it, Observer {
            it?.let {
//                mAdapter[currentPosition].is_follow = 1
//                mAdapter.notifyItemChanged(currentPosition)
                relationViewModel.getAllFollowsData(hashMapOf())
//                if (it) {
//                mAdapter.data.get(curSelCircle).is_join = 1
//                mAdapter.notifyDataSetChanged()
//                }
            }
        })
        _cancelFollowResult.observe(it, Observer {
            it?.let {
//                mAdapter[currentPosition].is_follow = 0
//                mAdapter.notifyItemChanged(currentPosition)
                relationViewModel.getAllFollowsData(hashMapOf())
//                if (it) {
//                mAdapter.list.get(curSelCircle).is_join = 1
//                mAdapter.notifyDataSetChanged()
//                }
            }
        })
        _likePostsResult.observe(it, Observer {
            it?.let {
                mAdapter[currentPosition].like_count = mAdapter[currentPosition].like_count?.plus(1)
//                mAdapter[currentPosition].is_like = 1
//                mAdapter.notifyItemChanged(currentPosition)
                relationViewModel.getAllPostLikesData(hashMapOf())
//                if (it) {
//                mAdapter.data.get(curSelCircle).is_join = 1
//                mAdapter.notifyDataSetChanged()
//                }
            }
        })
        _unlikePostResult.observe(it, Observer {
            it?.let {
                mAdapter[currentPosition].like_count =
                    mAdapter[currentPosition].like_count?.minus(1)
//                mAdapter[currentPosition].is_like = 0
//                mAdapter.notifyItemChanged(currentPosition)
                relationViewModel.getAllPostLikesData(hashMapOf())
//                if (it) {
//                mAdapter.list.get(curSelCircle).is_join = 1
//                mAdapter.notifyDataSetChanged()
//                }
            }
        })
    }

    private fun setData(it: HttpDataListBean<DynamicBean>?) {
        refreshLayout.finishLoadMore()
        it?.data?.toMutableList().let { it1 ->
            if (it1 == null) {
                return
            }
            if (it1.size < pageSize) refreshLayout.finishLoadMoreWithNoMoreData()
            addAdv(it1)
            it1.forEach { dynamicBean ->
                setActionRelationData(dynamicBean)
            }
            if (currentPage == 1) mAdapter.data.clear()
            mAdapter.data.addAll(it1)
            mAdapter.notifyDataSetChanged()
            //                loadComment(mAdapter.data)
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

    private fun addAdv(it1: MutableList<DynamicBean>) {
        if (type == TYPE_MINE_RECOMMEND || type == TYPE_CIRCLE_ALL || type == TYPE_CIRCLE_ELITE) {
            //推荐需要展示广告，这里随机去拿广告
            //需要展示几条
            var totalNum = 4
            var curNum = 0
            totalNum = when (it1.size) {
                pageSize -> 4
                in 10..15 -> 3
                in 5..10 -> 2
                in 1..5 -> 1
                else -> 0
            }
            if (curNum < totalNum) {
                //去拿广告
                //广告不能连在一起
                if (RelationManager.instance.advBeans.size > 0 && it1.size > 0) {

                    //随机插入位置
                    for (index in 0 until totalNum) {
                        var adPos = (Math.random() * RelationManager.instance.advBeans.size).toInt()
                        val advBean = RelationManager.instance.advBeans[adPos]
                        //获取随机位置
                        var pos = getRandomPos(it1)
                        Log.e(TAG, "pos = ${pos} ")
                        it1.add(pos, DynamicBean(type = TYPE_ADV_CONTENT, advBean = advBean))
                        it1.forEachIndexed { index, dynamicBean ->
                            Log.e(TAG, "index=${index} dynamicBean =${dynamicBean.type}")
                        }
                    }
                }
            }
            if (RelationManager.instance.advBeans.size > 0 && it1.size > 0) {
                var adPos = (Math.random() * RelationManager.instance.advBeans.size).toInt()
                val advBean = RelationManager.instance.advBeans[adPos]
                it1.add((Math.random() * it1.size).toInt(),
                    DynamicBean(type = DynamicAdapter.TYPE_ADV_CONTENT, advBean = advBean))
            }

        }
    }

    private fun getRandomPos(it1: MutableList<DynamicBean>): Int {
        var pos = (Math.random() * it1.size).toInt()
        if (pos == 0) {
            //判断当前这条是否是广告
            if (it1[pos].type == TYPE_ADV_CONTENT) {
                return getRandomPos(it1)
            }
            /* } else if (pos + 1 < it1.size) {
                 //随机在中间，那么要判断上一条是否也是广告
                 if (it1[pos - 1].type == TYPE_ADV_CONTENT || it1[pos].type == TYPE_ADV_CONTENT||it1[pos +1].type == TYPE_ADV_CONTENT) {
                     pos = getRandomPos(it1)
                 }
                 Log.e(TAG,
                     "pos = ${pos} it1[pos - 1].type = ${it1[pos - 1].type} it1[pos].type=${it1[pos].type} ")*/
        } else {
            if (it1[pos - 1].type == TYPE_ADV_CONTENT || it1[pos].type == TYPE_ADV_CONTENT) {
                return getRandomPos(it1)
            }
        }

        return pos
    }

    private val relationViewModel by getViewModel(RelationViewModel::class.java) {
        followsData.observe(it, Observer {

            it?.let {
                RelationManager.instance.follows = it.toMutableList()
                for ((index, dynamicBean) in mAdapter.data.withIndex()) {
                    val initialIsLike = dynamicBean.is_like
                    val initialIsFollow = dynamicBean.is_follow
                    if (it.isEmpty()) {
                        dynamicBean.is_follow = 0
                    } else {
                        setActionRelationData(dynamicBean)
                    }
                    if (initialIsLike != dynamicBean.is_like || initialIsFollow != dynamicBean.is_follow) {
                        mAdapter.notifyItemChanged(index)
                    }
                }

            }
        })
        postLikesData.observe(it, Observer {
            it?.data?.let { it ->
                RelationManager.instance.postLikes = it.toMutableList()
                for ((index, dynamicBean) in mAdapter.data.withIndex()) {
                    val initialIsLike = dynamicBean.is_like
                    val initialIsFollow = dynamicBean.is_follow
                    if (it.isEmpty()) {
                        //这边应该走不进来
                        dynamicBean.is_like = 0
                    } else {
                        setActionRelationData(dynamicBean)
                    }
                    if (initialIsLike != dynamicBean.is_like || initialIsFollow != dynamicBean.is_follow) {
                        mAdapter.notifyItemChanged(index)
                    }
                }
            }
        })
    }

    private fun setActionRelationData(dynamicBean: DynamicBean) {
        if (dynamicBean.type == TYPE_ADV_CONTENT) {
            Log.e(TAG, "item is ad")
            return
        }
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
//        RelationManager.instance.follows.forEach { userBean ->
//            if (userBean.id == followBean.user?.id) {
//                followBean.is_follow = 1
//            }
//        }
//        //设置点赞的文章状态
//        RelationManager.instance.postLikes.forEach { postBean ->
//            if (postBean.pivot?.post_id == followBean.id) {
//                followBean.is_like = 1
//            }
//        }
    }

    private fun addRecommendCommentData(comments: List<CommentBean>) {
        comments?.apply {
            if (size > 0) {
                Log.e(TAG, "addRecommendCommentData")
                val postId = comments[0].post_id
                mAdapter.data.forEachIndexed { index, dynamicBean ->
                    if (dynamicBean.id == postId) {
                        mAdapter.data[index].comments = this
                    }
                }
            }
            Log.e(TAG, "addRecommendCommentData---data:${mAdapter.data.hashCode()}")
        }
    }

    private fun loadComment(list: MutableList<DynamicBean>) {
        for (bean in list) {
            dynamicViewModel.getRecommendComment(hashMapOf("post_id" to "${bean.id}"))
        }
    }

    fun refreshData() {
        currentPage = 1
        loadData(type)
    }
}