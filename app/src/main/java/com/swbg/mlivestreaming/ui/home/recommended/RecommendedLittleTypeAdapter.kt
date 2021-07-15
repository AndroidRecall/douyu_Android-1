package com.swbg.mlivestreaming.ui.home.recommended

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.iyao.recyclerviewhelper.adapter.takeInstance
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.GroupedRecyclerViewAdapter
import com.swbg.mlivestreaming.bean.RecommendedBean
import com.swbg.mlivestreaming.bean.VideoBean
import kotlinx.android.synthetic.main.item_header_recommend_little_type_0.view.*

class RecommendedLittleTypeAdapter(context: Context?, val recommendedBean: RecommendedBean?) :
    GroupedRecyclerViewAdapter(context) {
    override fun onBindHeaderViewHolder(holder: CacheViewHolder, groupPosition: Int) {
        when (getHeaderViewType(groupPosition)) {

            R.layout.item_header_recommend_little_type_0 -> {
                holder.itemView.apply {
                    rv_recommend_little_type.let {
                        it.adapter = videoAdapter
                        it.layoutManager = GridLayoutManager(context, 3)
                    }
                    videoAdapter.takeInstance<CachedAutoRefreshAdapter<VideoBean>>()
                        .refresh(recommendedBean?.videoList!!, null)
                }
            }
        }

    }

    override fun getHeaderViewType(groupPosition: Int): Int = when (groupPosition) {
        0 -> R.layout.item_header_recommend_little_type_0
        else -> R.layout.item_header_recommend_little_type_0
    }

    override fun hasHeader(var1: Int): Boolean = true

    override fun getHeaderLayout(var1: Int): Int = var1

    override fun getChildrenCount(var1: Int): Int = 0

    override fun onBindChildViewHolder(var1: CacheViewHolder?, var2: Int, var3: Int) {
    }

    override fun onViewHolderCreated(viewHolder: CacheViewHolder?, viewType: Int) {
        when (viewType) {
            R.layout.item_header_recommend_little_type_0 -> {
            }
        }
    }

    override fun getFooterLayout(var1: Int): Int = var1

    override fun hasFooter(var1: Int): Boolean = false


    private val videoAdapter by lazy {
        VideoAdapter({

        }, context)
    }

    override fun getChildLayout(var1: Int): Int = var1

    override fun onBindFooterViewHolder(var1: CacheViewHolder?, var2: Int) {
    }

    override fun getGroupCount(): Int = 1
}
