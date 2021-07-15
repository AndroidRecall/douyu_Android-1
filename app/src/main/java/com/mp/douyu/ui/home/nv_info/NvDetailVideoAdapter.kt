package com.mp.douyu.ui.home.nv_info

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
import kotlinx.android.synthetic.main.item_nv_info_product.view.*
import kotlinx.android.synthetic.main.item_nv_info_product.view.tv_video_title

class NvDetailVideoAdapter(var context: Context?,val listener : (Int)->Unit ) : CachedAutoRefreshAdapter<VideoBean>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
       return CacheViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_nv_info_product, parent, false))
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            get(position).let {
                loadGlobleVideo(context,it.faceImageUrl,iv_item_face)

//                Glide.with(context).load(it.faceImageUrl)
//                    .into(iv_item_face)
                tv_time.text = it.duration
                tv_video_title.text = it.videoTitle
            }
            iv_item_face.singleClick {
                openDetailActivity(get(position))
            }
            tv_video_title.singleClick {
                openDetailActivity(get(position))
            }

        }
    }

    private fun openDetailActivity(bean: VideoBean) {
        context?.startActivity(VideoPlayActivity.open(context, bean.id))
    }

}
