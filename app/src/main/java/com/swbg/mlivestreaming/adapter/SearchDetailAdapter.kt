package com.swbg.mlivestreaming.adapter

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.jzvd.Jzvd
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.lxj.xpopup.XPopup
import com.swbg.mlivestreaming.BuildConfig
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.bean.CommonUserBean
import com.swbg.mlivestreaming.bean.LiveBean
import com.swbg.mlivestreaming.bean.SearchDetailBean
import com.swbg.mlivestreaming.bean.SquareCircleBean
import com.swbg.mlivestreaming.provider.StoredUserSources
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.ui.home.high_definition.HighDefinitionVideoAdapter
import com.swbg.mlivestreaming.ui.square.me.SquareDynamicFragment
import com.swbg.mlivestreaming.utils.DrawableUtils
import com.swbg.mlivestreaming.utils.WindowUtils
import com.swbg.mlivestreaming.view.gridview.NineImageAdapter
import kotlinx.android.synthetic.main.live_recycle_item_live.view.tv_name
import kotlinx.android.synthetic.main.live_recycle_item_live_header.view.*
import kotlinx.android.synthetic.main.search_recycle_item_anchor.view.*
import kotlinx.android.synthetic.main.search_recycle_item_header.view.*
import kotlinx.android.synthetic.main.search_recycle_item_header.view.tv_more
import kotlinx.android.synthetic.main.search_recycle_item_video.view.rv_video
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_img.view.*
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_img.view.iv_avatar
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_img.view.recycle_comment
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_img.view.tv_comment_num
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_img.view.tv_content
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_img.view.tv_follow_num
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_img.view.tv_like_num
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_img.view.tv_time
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_video.view.*
import org.jzvd.jzvideo.TAG
import java.util.ArrayList

class SearchDetailAdapter(private val listener: () -> Unit, var context: Context?) :
    CachedAutoRefreshAdapter<SearchDetailBean>() {
    companion object {
        const val ITEM_VIEW_HEADER = 10 //类型标题
        const val ITEM_VIEW_DYNAMIC = 1 //文章
        const val ITEM_VIEW_ANCHOR = 2  //主播
        const val ITEM_VIEW_VIDEO = 3   //视频
    }

    var mListener: OnSearchListener? = null
    override var data: MutableList<SearchDetailBean>
        get() = super.data
        set(value) {}

    override fun getItemViewType(position: Int): Int {
        return data[position].getItemViewType()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        val view: View
        return when (viewType) {
            ITEM_VIEW_HEADER -> CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.search_recycle_item_header, parent, false)).apply {
                itemView.apply {}
            }
            ITEM_VIEW_ANCHOR -> CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.search_recycle_item_anchor, parent, false)).apply {
                itemView.apply {}
            }
            ITEM_VIEW_VIDEO -> CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.search_recycle_item_video, parent, false)).apply {
                itemView.apply {}
            }
            else -> CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.search_recycle_item_dynamic, parent, false)).apply {
                itemView.apply {}
            }

        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        when (holder.itemViewType) {
            ITEM_VIEW_HEADER -> setHeaderItemView(holder, position)
            ITEM_VIEW_ANCHOR -> setAnchorItemView(holder, position)
            ITEM_VIEW_VIDEO -> setVideoItemView(holder, position)
            ITEM_VIEW_DYNAMIC -> setDynamicItemView(holder, position)
            else -> setDynamicItemView(holder, position)

        }

    }

    private fun setVideoItemView(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            rv_video.apply {
                adapter = HighDefinitionVideoAdapter({}, context).apply {
                    addAll(this@SearchDetailAdapter.get(position).videos)
                }
                layoutManager = GridLayoutManager(context, 2).apply {
//                    addItemDecoration(object : RecyclerView.ItemDecoration() {
//
//                        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//                            val viewPosition =
//                                (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
//                            if (viewPosition != 0 && viewPosition % 2 != 0) {
//                                outRect.set(0, 0, WindowUtils.dip2Px(12f), 0)
//                            }
//                        }
//                    })
                }
            }
        }
    }

    private fun setAnchorItemView(holder: CacheViewHolder, position: Int) {
        Log.e(TAG, "setAnchorItemView position=${position}")
        holder.itemView.apply {
            recycler_anchor.apply {
                layoutManager = GridLayoutManager(context, 5)
                adapter = SearchAnchorDetailAdapter({ pos ->
                    onHeaderClick(position, pos)
                }, context).apply {
                    setListener(object : SearchAnchorDetailAdapter.OnAnchorListener {
                        override fun onAnchorList() {
                            this@SearchDetailAdapter.mListener?.onSearchMoreAnchorClick()
                        }

                        override fun onHeaderClick(userId: Int?) {
                            userId?.let { this@SearchDetailAdapter.mListener?.onHeaderClick(it) }
                        }

                        override fun onFollow(position: Int) {

                        }

                        override fun onItemViewClick(position: Int) {

                        }
                    })
                    if (this@SearchDetailAdapter.get(position).lives.size > 4) {
                        data.addAll(this@SearchDetailAdapter.get(position).lives.subList(0, 4))
                        data.add(4, LiveBean(title = "全部"))

                    } else {
                        addAll(this@SearchDetailAdapter.get(position).lives)
                    }
                }
            }
        }
    }

    private fun onHeaderClick(position: Int, childPos: Int) {
        get(position).lives.apply {
            get(childPos).user_id?.let { mListener?.onHeaderClick(it) }
        }
    }

    private fun setDynamicItemView(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            get(position).dynamicBean?.apply {
                if (user?.avatar?.contains("http") == false) {
                    user?.avatar = BuildConfig.IMAGE_BASE_URL + user?.avatar
                }
                Log.e("onBindViewHolder", "is_like=${is_like},position=${position}")
                Glide.with(context).load(user?.avatar).circleCrop()
                    .placeholder(R.mipmap.default_avatar).into(iv_avatar)
                tv_name.text = "${user?.nickname}"
                var contentStr = "${content}"
                if (type == SquareDynamicFragment.TYPE_SPACE_REPLY) {
                    images?.let {
                        if (it.size > 0) {
                            contentStr = "原文 [图片] ${content}"
                        }
                    }
                    url?.let {
                        if (it.isNotEmpty()) {
                            contentStr = "原文 [视频] ${content}"
                        }
                    }
                }
                tv_content.text = contentStr
                tv_comment_num.text = "${comm_count ?: 0}"
                tv_time.text = update_time
                if (is_follow == 0) {
                    tv_follow_num.text = "关注"
                    tv_follow_num.setTextColor(resources.getColor(R.color.color_4688FF))
                    tv_follow_num.setBackgroundResource(R.drawable.btn_gradient_201f1f_border_white_bg_layer)
                } else {
                    tv_follow_num.text = "已关注"
                    tv_follow_num.setTextColor(resources.getColor(R.color.colorMiddleGray))
                    tv_follow_num.setBackgroundResource(R.drawable.btn_gray_border_bg)

                }
                /*当该动态是本人发的，或者是“我的空间”里面的，则隐藏关注按钮*/
                if (user?.id == StoredUserSources.getUserInfo2()?.id || type == SquareDynamicFragment.TYPE_SPACE_PUBLISH || type == SquareDynamicFragment.TYPE_SPACE_REPLY || type == SquareDynamicFragment.TYPE_SPACE_COLLECT || type == SquareDynamicFragment.TYPE_SPACE_RECORD) {
                    tv_follow_num.visibility = View.GONE
                }
                tv_like_num.text = "${like_count ?: 0}"
                DrawableUtils.setDrawable(drawable = if (is_like == 0) R.mipmap.ic_box_like_false else R.mipmap.icon_dianzhan_true,
                    textView = tv_like_num)
                iv_avatar.apply {
                    singleClick {
                        uid?.let { it1 -> mListener?.onHeaderClick(it1) }
                    }
                }

                tv_like_num.apply {
                    singleClick {
                        mListener?.onLikeClick(position, is_like)
                    }
                }
                tv_follow_num.apply {
                    singleClick {
                        mListener?.onFollowClick(position, is_follow)
                    }
                }

                if (type != SquareDynamicFragment.TYPE_SPACE_REPLY) {
                    if (getItemViewType() == DynamicAdapter.TYPE_VIDEO_CONTENT) {
                        Log.e(TAG, "设置视频 position=${position},url=${url}")
                        setVideoPlay(position)
                    } else if (getItemViewType() == DynamicAdapter.TYPE_NOR_CONTENT) {
                        if (images?.isNotEmpty() == true) {
                            Log.e(TAG, "设置九宫格图片")
                            setNineImgView(position)
                        }
                    }
                }
                recycle_comment.apply {
                    adapter = SquareDynamicCommentAdapter({}, context).apply {
                        setListener(object : SquareDynamicCommentAdapter.OnCommentListener {
                            override fun onHeaderClick(pos: Int) {
                                this@SearchDetailAdapter.mListener?.onCommentHeaderClick(this@SearchDetailAdapter.get(
                                    position).dynamicBean?.comments?.get(pos)?.user)
                            }

                            override fun onFollowClick(position: Int) {
                            }

                            override fun onLikePostClick(position: Int, isLike: Int?) {
                            }

                            override fun onCommentClick(position: Int) {
                            }
                        })
                    }
                    layoutManager = LinearLayoutManager(context)
                    comments?.apply {
                        if (size < 3) {
                            (adapter as SquareDynamicCommentAdapter).refresh(this, null)
                        } else (adapter as SquareDynamicCommentAdapter).refresh(subList(0, 3), null)
                    }

                }

                singleClick {
                    //                listener.invoke()
                    mListener?.onCommentClick(position)
                }
            }
        }

    }

    private fun setHeaderItemView(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            tv_type_title.text = get(position).title
            if (get(position).isMore == true) {
                tv_more.visibility = View.VISIBLE
                tv_more.singleClick {
                    //查看更多
                    mListener?.onSearchMoreClick()
                }
            } else {
                tv_more.visibility = View.GONE
            }

        }
    }

    private fun View.setNineImgView(position: Int) {
        val requestOptions = RequestOptions().centerCrop()
        val drawableTransitionOptions = DrawableTransitionOptions.withCrossFade()
        grid_view.setAdapter(NineImageAdapter(context,
            requestOptions,
            drawableTransitionOptions,
            get(position).dynamicBean?.images))
        grid_view.setOnImageClickListener { pos, view ->
            val list: MutableList<Any> = ArrayList()
            get(position).dynamicBean?.images?.forEach {
                list.add("${it.url}")
            }
            XPopup.Builder(context)
                .asImageViewer(view as ImageView, pos, list, { popupView, position ->
                    popupView.updateSrcView(grid_view.getChildAt(position) as ImageView)
                }, DynamicAdapter.MyImageLoader()).show()
        }
    }

    private fun View.setVideoPlay(position: Int) {
        videoPlayer.setUp("${BuildConfig.IMAGE_BASE_URL}${get(position).dynamicBean?.url}",
            "",
            Jzvd.SCREEN_NORMAL)
        videoPlayer.positionInList = position
    }

    fun setOnSearchListener(listener: OnSearchListener) {
        mListener = listener
    }

    interface OnSearchListener {
        fun onFollowClick(position: Int, isFollow: Int?)
        fun onLikeClick(position: Int, isLike: Int?)
        fun onCommentClick(position: Int)
        fun onHeaderClick(uid: Int)
        fun onAdClick(position: Int)
        fun onSearchMoreClick()
        fun onSearchMoreAnchorClick()
        fun onCommentHeaderClick(user: CommonUserBean?)
    }
}

