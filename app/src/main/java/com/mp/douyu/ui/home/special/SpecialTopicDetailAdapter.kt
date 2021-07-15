package com.mp.douyu.ui.home.special

import android.content.Context
import android.view.View
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.mp.douyu.R
import com.mp.douyu.base.GroupedRecyclerViewAdapter
import com.mp.douyu.bean.SpecialBean
import com.mp.douyu.bean.VideoBean
import com.mp.douyu.loadGlobleVideo
import com.mp.douyu.singleClick
import kotlinx.android.synthetic.main.item_header_special_detail_0.view.*
import kotlinx.android.synthetic.main.item_video.view.iv_video
import kotlinx.android.synthetic.main.item_video.view.tv_video_title

class SpecialTopicDetailAdapter(val context: Context,  val listener: (View,VideoBean) -> Unit) :
    GroupedRecyclerViewAdapter(context) {
    var videoList : ArrayList<VideoBean> = arrayListOf()
    var sb : SpecialBean = SpecialBean()
    override fun onBindHeaderViewHolder(viewHolder: CacheViewHolder, groupPosition: Int) {
        when (getHeaderViewType(groupPosition)) {
            R.layout.item_header_special_detail_0 -> {
                viewHolder.itemView.apply {
                    loadGlobleVideo(context,sb.cover,iv_top_image)
//                    Glide.with(context).load(sb.cover).apply {
//                        RequestOptions.bitmapTransform(RoundedCorners(5)).encodeQuality(100)
//                            .centerCrop().error(R.mipmap.icon_place_holder_error)
//                            .placeholder(R.mipmap.icon_place_holder_loading)
//                    }.into(iv_top_image)
                    tv_title.text = sb.title
                    tv_content.text = sb.summary
                }
            }
        }
    }

    override fun getHeaderViewType(groupPosition: Int): Int = when (groupPosition) {
        0 -> R.layout.item_header_special_detail_0
        else -> 0
    }

    override fun hasHeader(var1: Int): Boolean = var1 == 0

    override fun getHeaderLayout(var1: Int): Int = var1

    override fun getChildrenCount(var1: Int): Int = when (var1) {
        1 -> videoList.size
        else -> 0
    }

    override fun onBindChildViewHolder(holder: CacheViewHolder, groupPosition: Int, childPosition: Int) {
        when (getChildViewType(groupPosition, childPosition)) {
            R.layout.item_special_topic_detail -> {
                holder.itemView.apply {
                    val videoBean = videoList[childPosition]
                    loadGlobleVideo(context,videoBean.faceImageUrl,iv_video)

//                    Glide.with(context).load(videoBean.faceImageUrl).apply {
//                        RequestOptions.bitmapTransform(RoundedCorners(5)).encodeQuality(100)
//                            .centerCrop().error(R.mipmap.icon_place_holder_error)
//                            .placeholder(R.mipmap.icon_place_holder_loading)
//                    }.into(iv_video)
                    tv_video_title.text = videoBean.videoTitle ?: ""
                    iv_video.singleClick {
                        listener.invoke(it,videoBean)
                    }
                    tv_video_title.singleClick {
                        listener.invoke(it,videoBean)
                    }
                }
            }
        }
    }

    override fun getChildViewType(groupPosition: Int, childPosition: Int): Int {
        return when (groupPosition) {
            1 -> R.layout.item_special_topic_detail
            else -> 0
        }
    }

    override fun onViewHolderCreated(viewHolder: CacheViewHolder?, viewType: Int) {
        when (viewType) {
            R.layout.item_header_special_detail_0 -> {
                viewHolder.apply {}
            }
            R.layout.item_special_topic_detail -> {
            }
        }
    }

    override fun getFooterLayout(var1: Int): Int = var1

    override fun hasFooter(var1: Int): Boolean = false

    override fun getChildLayout(var1: Int): Int = var1

    override fun onBindFooterViewHolder(var1: CacheViewHolder?, var2: Int) {
    }

    override fun getGroupCount(): Int = 2

}
