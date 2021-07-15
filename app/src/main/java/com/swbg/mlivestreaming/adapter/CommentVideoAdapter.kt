package com.swbg.mlivestreaming.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.swbg.mlivestreaming.BuildConfig
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.bean.CommentBean
import com.swbg.mlivestreaming.singleClick
import kotlinx.android.synthetic.main.video_recycle_item_comment.view.*

class CommentVideoAdapter(private val listener: () -> Unit, var context: Context?) :
    CachedAutoRefreshAdapter<CommentBean>() {
    var commentVideoListener: OnCommentVideoListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.video_recycle_item_comment, parent, false)).apply {
            itemView.apply {}
        }
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            if (get(position).user?.avatar?.contains("http") == false) {
                get(position).user?.avatar = BuildConfig.IMAGE_BASE_URL+get(position).user?.avatar
            }
            Glide.with(context).load(get(position).user?.avatar).placeholder(R.mipmap.default_avatar).circleCrop().into(iv_avatar)
            tv_like_num.text= "${get(position).like_count?:0}"
            tv_comment_content.text= get(position).content?:""
            tv_time.text= "${get(position).user?.create_time}"
            tv_user_name.text= "${get(position).user?.nickname}"
//            tv_anchor_gender.text = if (get(position).user?.gender==1)"女" else "男"
            when (get(position).user?.gender) {
                0->{iv_anchor_gender.visibility = View.INVISIBLE}
                1->{iv_anchor_gender.setImageResource(R.mipmap.icon_gender_man)}
                2->{iv_anchor_gender.setImageResource(R.mipmap.icon_gender_woman)}
            }
            tv_reply_num.text= "${get(position).data_i.size}"
            iv_avatar.apply {
                singleClick {

                }
            }

            tv_like_num.apply { singleClick {
                commentVideoListener?.onLikeClick()
            } }


            singleClick {
//                listener.invoke()
                commentVideoListener?.onCommentClick(position)
            }
        }
    }

    fun setListener(onShortVideoListener: OnCommentVideoListener) {
        commentVideoListener = onShortVideoListener
    }

    interface OnCommentVideoListener {
        fun onLikeClick()
        fun onCommentClick(position: Int)
    }

}