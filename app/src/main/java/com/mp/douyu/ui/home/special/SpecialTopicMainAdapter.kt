package com.mp.douyu.ui.home.special

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.iyao.recyclerviewhelper.adapter.takeInstance
import com.mp.douyu.R
import com.mp.douyu.base.GroupedRecyclerViewAdapter
import com.mp.douyu.bean.BannerImageBean
import com.mp.douyu.bean.SpecialBean
import com.mp.douyu.bean.SpecialTopicBean
import com.mp.douyu.loadGlobleVideo
import com.mp.douyu.utils.ActivityUtils
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.item_header_special_topic_0.view.*
import kotlinx.android.synthetic.main.item_header_special_topic_1.view.*

class SpecialTopicMainAdapter(context: Context?, val listener: (View, Int) -> Unit) :
    GroupedRecyclerViewAdapter(context) {

    var sb : SpecialTopicBean? = SpecialTopicBean()

    override fun onBindHeaderViewHolder(holder: CacheViewHolder, groupPosition: Int) {
        when (getHeaderViewType(groupPosition)) {

            R.layout.item_header_special_topic_0 -> {
                //轮播
                holder.itemView.home_banner.apply {
                    adapter = object :
                        BannerImageAdapter<BannerImageBean>(sb?.bannersList) {
                        override fun onBindView(holder: BannerImageHolder, data: BannerImageBean, position: Int, size: Int) {
                            loadGlobleVideo(context,data.imageUrl,holder.imageView)
//                            Glide.with(context).load(data.imageUrl)
//                                .apply(RequestOptions.bitmapTransform(RoundedCorners(5))
//                                    .centerCrop()).into(holder.imageView)
                        }
                    }
                    setOnBannerListener { data, position ->
                        ActivityUtils.jumpToWebView(sb?.bannersList?.get(position)?.url,context)
                    }
                    setIndicator(CircleIndicator(context))
                }
            }
            R.layout.item_header_special_topic_1 -> {
                holder.itemView.apply {
                    rv_special_topic.apply {
                        adapter = mAdapter
                        layoutManager = LinearLayoutManager(context)
                    }
                    sb?.special?.apply {
                        mAdapter.takeInstance<CachedAutoRefreshAdapter<SpecialBean>>()
                            .refresh(this, null)
                    }

                }
            }
        }

    }

    override fun getHeaderViewType(groupPosition: Int): Int = when (groupPosition) {
        0 -> R.layout.item_header_special_topic_0
        1 -> R.layout.item_header_special_topic_1
        else -> R.layout.item_header_special_topic_0
    }

    override fun hasHeader(var1: Int): Boolean = true

    override fun getHeaderLayout(var1: Int): Int = var1

    override fun getChildrenCount(var1: Int): Int = 0

    override fun onBindChildViewHolder(var1: CacheViewHolder?, var2: Int, var3: Int) {
    }

    override fun onViewHolderCreated(viewHolder: CacheViewHolder?, viewType: Int) {
        when (viewType) {
            R.layout.item_header_special_topic_0 -> {
            }
            R.layout.item_header_special_topic_1 -> {
            }
        }
    }

    override fun getFooterLayout(var1: Int): Int = var1

    override fun hasFooter(var1: Int): Boolean = false


    private val mAdapter by lazy {
        SpecialTopicAdapter(context) { view: View, i: Int ->
            listener.invoke(view, i)
        }
    }

    override fun getChildLayout(var1: Int): Int = var1

    override fun onBindFooterViewHolder(var1: CacheViewHolder?, var2: Int) {
    }

    override fun getGroupCount(): Int = 2
}
