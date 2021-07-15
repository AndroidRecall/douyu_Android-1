package com.mp.douyu.ui.home.high_definition

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.mp.douyu.R
import com.mp.douyu.bean.VideoBean
import com.mp.douyu.loadGlobleVideo
import com.mp.douyu.singleClick
import com.mp.douyu.ui.home.play.VideoPlayActivity
import kotlinx.android.synthetic.main.item_high_definition_video.view.iv_video
import kotlinx.android.synthetic.main.item_high_definition_video.view.tv_video_title

class HighDefinitionVideoAdapter(private val listener : () -> Unit,val context: Context?) : CachedAutoRefreshAdapter<VideoBean>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_high_definition_video, parent, false)).apply {
            itemView.apply {}
        }
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        val videoBean = get(position)
        holder.itemView.apply {
            loadGlobleVideo(context,videoBean.faceImageUrl,iv_video)

//            Glide.with(context).load(videoBean.faceImageUrl).apply {
//                RequestOptions.bitmapTransform(RoundedCorners(5)).encodeQuality(100).centerCrop()
//                    .error(R.mipmap.icon_place_holder_error)
//                    .placeholder(R.mipmap.icon_place_holder_loading)
//            }.into(iv_video)
            tv_video_title.text = videoBean.videoTitle ?: ""
            singleClick {
                context.startActivity(VideoPlayActivity.open(context,get(position).id))
//                listener.invoke()
            }
        }
    }

}
