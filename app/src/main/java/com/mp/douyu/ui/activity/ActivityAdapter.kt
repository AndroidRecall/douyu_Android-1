package com.mp.douyu.ui.activity

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.mp.douyu.R
import com.mp.douyu.bean.ActivityBean
import com.mp.douyu.singleClick
import com.mp.douyu.utils.ActivityUtils
import kotlinx.android.synthetic.main.item_activity.view.*

class ActivityAdapter(private val context: Context?) : CachedAutoRefreshAdapter<ActivityBean>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_activity, parent, false))
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            val get = get(position)
            tv_time.text = get.expire
            Glide.with(context).load(get.cover).into(iv_cover)
            iv_top_left.setBackgroundResource(when (get.tag) {
                "火爆" -> R.mipmap.activity_hot
                "日常" -> R.mipmap.activity_normal
                else -> R.mipmap.activity_most_new
            })
            singleClick {
                ActivityUtils.jumpToWebView(get.url, context)
            }

        }
    }

}
