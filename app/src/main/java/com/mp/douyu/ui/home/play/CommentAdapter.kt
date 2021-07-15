package com.mp.douyu.ui.home.play

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.mp.douyu.BuildConfig
import com.mp.douyu.R
import com.mp.douyu.bean.CommentBean
import com.mp.douyu.bean.CommonUserBean
import com.mp.douyu.singleClick
import kotlinx.android.synthetic.main.item_video_player_comment.view.*

class CommentAdapter(private val listener: (View, Int) -> Unit, var context: Context?) :
    CachedAutoRefreshAdapter<CommentBean>() {
    var mListener: VideoPlayAdapter.OnCommentListener?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_video_player_comment, parent, false)).apply {
            itemView.apply {}
        }
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        val commentBean = get(position)
        holder.itemView.apply {
            if (commentBean.user?.avatar?.contains("http")==false) {
                commentBean.user?.avatar = BuildConfig.IMAGE_BASE_URL+commentBean.user?.avatar
            }
            Glide.with(context).load( commentBean.user?.avatar)
                .error(R.mipmap.default_avatar).into(iv_comment_avatar)
            tv_name.text = commentBean.user?.nickname
            tv_content.text = commentBean.content
            tv_click_like_num_small.text = "${commentBean.like_count}"
            tv_comment_num_small.text = "${commentBean.data_i.size}"
            tv_comment_time.text = commentBean.create_time
            iv_gender.setBackgroundResource(R.mipmap.player_man)

            cl_comment.singleClick {
                mListener?.onCommentClick(commentBean)
            }
            iv_comment_avatar.singleClick {
                mListener?.onCommentHeaderClick(commentBean.user)
            }
        }
    }
    fun setListener(listener: VideoPlayAdapter.OnCommentListener) {
        mListener = listener
    }
    interface OnCommentListener {
        fun onCommentClick(position: CommentBean)
        fun onCommentHeaderClick(user: CommonUserBean?)
    }
}
