package com.mp.douyu.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.DrawableRes
import cn.jzvd.JZDataSource
import cn.jzvd.Jzvd
import com.bumptech.glide.Glide
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.mp.douyu.BuildConfig
import com.mp.douyu.R
import com.mp.douyu.bean.ShortVideoBean
import com.mp.douyu.provider.StoredUserSources
import com.mp.douyu.singleClick
import kotlinx.android.synthetic.main.video_recycle_item_short_video.view.*
import kotlinx.android.synthetic.main.video_recycle_item_short_video.view.iv_avatar
import kotlinx.android.synthetic.main.video_recycle_item_short_video.view.tv_comment_num
import kotlinx.android.synthetic.main.video_recycle_item_short_video.view.tv_like_num

class ShortVideoAdapter(private val listener: () -> Unit, var context: Context?) :
    CachedAutoRefreshAdapter<ShortVideoBean>() {
    override var data: MutableList<ShortVideoBean>
        get() = super.data
        set(value) {}
    var shortVideoListener: OnShortVideoListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.video_recycle_item_short_video, parent, false)).apply {
            itemView.apply {}
        }
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            if (get(position).url?.contains("http") == false) {
                get(position).url = BuildConfig.IMAGE_BASE_URL + get(position).url
            }
            var url = "${get(position).url}"
            Log.e("ShortVideoAdapter", url)
            var jzDataSource = JZDataSource("${url}", get(position).title).apply {
                looping = true
            }
            if (get(position).image?.contains("http") == false) {
                get(position).image = BuildConfig.IMAGE_BASE_URL + get(position).image
            }
            if (get(position).user?.avatar?.contains("http") == false) {
                get(position).user?.avatar = BuildConfig.IMAGE_BASE_URL + get(position).user?.avatar
            }
            jz_player.setUp(jzDataSource, Jzvd.SCREEN_NORMAL/*, JZMediaIjk::class.java*/)

            Glide.with(context).load(get(position).image).into(jz_player.posterImageView)
            Glide.with(context).load(get(position).user?.avatar)
                .placeholder(R.mipmap.default_avatar).circleCrop().into(iv_avatar)
            tv_title.text = "@${get(position)?.user?.nickname}"
            tv_desc.text = "${get(position).title}"
            tv_comment_num.text = "${get(position).comment_count ?: 0}"
//            tv_repost_num.text = "${get(position).share_count ?: 0}"
            tv_like_num.text = "${get(position).like_count ?: 0}"

            tv_like_num.isSelected = get(position).is_like_short_video == 1
            fl_follow.visibility =
                if (StoredUserSources.getUserInfo2()?.id != get(position).user?.id) View.VISIBLE else View.GONE
            if (get(position).is_follow == 0) {
                iv_follow_status.setImageResource(R.mipmap.guanzhu_2)
            } else {
                iv_follow_status.setImageResource(R.mipmap.yiguanzhu_2)
            }
            iv_avatar.apply {
                singleClick {
                    shortVideoListener?.onHeaderClick(position)
                }
            }
            fl_follow.singleClick {
                shortVideoListener?.onFollow(position)
            }
            tv_comment_num.apply {
                singleClick {
                    shortVideoListener?.onCommentClick(get(position).id)
                }
            }

            tv_like_num.apply {
                singleClick {
                    shortVideoListener?.onLikeClick(position)
                }
            }
            tv_repost_num.apply {
                singleClick {
                    shortVideoListener?.onRepostClick(position)
                }
            }

            singleClick {
                listener.invoke()
            }
        }
    }

    fun setDrawableStart(@DrawableRes drawable: Int, textView: TextView) {
        context?.resources?.getDrawable(drawable)?.let { drawable ->
            drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight);
            textView.setCompoundDrawables(drawable, null, null, null);
        }
    }

    fun setListener(onShortVideoListener: OnShortVideoListener) {
        shortVideoListener = onShortVideoListener
    }

    interface OnShortVideoListener {
        fun onLikeClick(position: Int)
        fun onCommentClick(id: Int?)
        fun onHeaderClick(position: Int)
        fun onRepostClick(position: Int)
        fun onFollow(position: Int)
    }

}