package com.swbg.mlivestreaming.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.swbg.mlivestreaming.BuildConfig
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.bean.CommonUserBean
import com.swbg.mlivestreaming.singleClick
import kotlinx.android.synthetic.main.live_recycle_item_audience.view.*

class LiveAudienceInfoAdapter(private val listener: () -> Unit, var context: Context?) :
    CachedAutoRefreshAdapter<CommonUserBean>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.live_recycle_item_audience, parent, false)).apply {
            itemView.apply {}
        }
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            Glide.with(context).load(get(position).avatar).circleCrop().placeholder(R.mipmap.default_avatar).into(iv_avatar)
            singleClick {
                listener.invoke()
            }
        }
    }

}