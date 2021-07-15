package com.mp.douyu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.mp.douyu.BuildConfig
import com.mp.douyu.R
import com.mp.douyu.bean.ShortVideoBean
import com.mp.douyu.singleClick
import kotlinx.android.synthetic.main.video_recycle_item_search.view.*

class SearchShortVideoAdapter(private val listener: (position:Int) -> Unit, var context: Context?) :
    CachedAutoRefreshAdapter<ShortVideoBean>() {
    override var data: MutableList<ShortVideoBean>
        get() = super.data
        set(value) {}
    var commentVideoListener: OnShortVideoListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.video_recycle_item_search, parent, false)).apply {
            itemView.apply {}
        }
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            if (get(position).image?.contains("http") == false) {
                get(position).image = BuildConfig.IMAGE_BASE_URL+get(position).image
            }

            Glide.with(context).load(get(position).image)
                .placeholder(R.mipmap.login_code_failed).into(iv_bg)
            tv_like_num.text = "${get(position).like_count ?: 0}"
            tv_title.text = get(position).title
            iv_like.setImageResource(if (get(position).is_like_short_video==0)R.drawable.tiktok_star_normal else R.drawable.tiktok_star_selected)
            singleClick {
                listener.invoke(position)
            }
        }
    }

    fun setListener(onShortVideoListener: OnShortVideoListener) {
        commentVideoListener = onShortVideoListener
    }

    interface OnShortVideoListener {

    }

}