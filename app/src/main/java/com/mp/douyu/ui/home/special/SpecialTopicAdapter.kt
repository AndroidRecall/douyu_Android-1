package com.mp.douyu.ui.home.special

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.mp.douyu.R
import com.mp.douyu.bean.SpecialBean
import com.mp.douyu.loadGlobleVideo
import com.mp.douyu.singleClick
import kotlinx.android.synthetic.main.item_special_topic.view.*

class SpecialTopicAdapter(var context: Context?,val listener : (View,Int) -> Unit) : CachedAutoRefreshAdapter<SpecialBean>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_special_topic, parent, false)).apply {
        }
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        val get = get(position)
        holder.itemView.apply {
            tv_title.text = get.title
            tv_detail.text = get.summary
            tv_video_number.text = "${get.videos}部"
            loadGlobleVideo(context,get.cover,iv_video_face)
//            Glide.with(context).load(get.cover).apply {
//                RequestOptions.bitmapTransform(RoundedCorners(5)).encodeQuality(100).centerCrop()
//                    .error(R.mipmap.icon_place_holder_error)
//                    .placeholder(R.mipmap.icon_place_holder_loading)
//            }.into(iv_video_face)
            iv_video_face.singleClick {
                listener.invoke(it,position)
            }
            tv_title.singleClick {
                listener.invoke(it,position)
            }
        }
    }

}
