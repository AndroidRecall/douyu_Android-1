package com.swbg.mlivestreaming.ui.home.more_type

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.iyao.recyclerviewhelper.adapter.takeInstance
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.GroupedRecyclerViewAdapter
import com.swbg.mlivestreaming.bean.SpecialBean
import com.swbg.mlivestreaming.bean.TypeBean
import com.swbg.mlivestreaming.loadGlobleVideo
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.ui.home.special.SpecialTopicDetailActivity
import kotlinx.android.synthetic.main.item_high_definition_video.view.*
import kotlinx.android.synthetic.main.item_more_type_header_0.view.*
import kotlinx.android.synthetic.main.item_special_topic.view.*

class MoreTypeAdapter(context: Context?) : GroupedRecyclerViewAdapter(context) {
    var viewTypeTest = listOf(TypeBean())
    var videoBeanList = listOf(SpecialBean())

    override fun onBindHeaderViewHolder(holder: CacheViewHolder, groupPosition: Int) {
        when (getHeaderViewType(groupPosition)) {
            R.layout.item_more_type_header_0 -> {
                holder.itemView.apply {
                    mTypeAdapter.takeInstance<CachedAutoRefreshAdapter<TypeBean>>().refresh(viewTypeTest,null)
                }
            }
        }
    }

    override fun getHeaderViewType(groupPosition: Int): Int {
        return when (groupPosition) {
            0 -> R.layout.item_more_type_header_0
            else -> 0
        }
    }

    override fun hasHeader(var1: Int): Boolean = var1 == 0

    override fun getHeaderLayout(var1: Int): Int = var1

    override fun getChildrenCount(var1: Int): Int = when (var1) {
        1 -> videoBeanList.size
        else -> 0
    }

    override fun onBindChildViewHolder(holder: CacheViewHolder, groupPosition: Int, childPosition: Int) {
        when (getChildViewType(groupPosition, childPosition)) {
            R.layout.item_special_topic -> {
                holder.itemView.apply {
                    val specialBean = videoBeanList[childPosition]
                    tv_title.text = specialBean.title
                    tv_detail.text = specialBean.summary
                    tv_video_number.text = "${specialBean.videos}部"
                    loadGlobleVideo(context,specialBean.cover,iv_video_face)

//                    Glide.with(context).load(specialBean.cover).into(iv_video_face)
                    singleClick {
                        context.startActivity(SpecialTopicDetailActivity.open(context,specialBean))
                    }
                }
            }
        }
    }

    override fun getChildViewType(groupPosition: Int, childPosition: Int): Int {
        return when(groupPosition){
            1 -> R.layout.item_special_topic
            else -> 0
        }
    }
    override fun onViewHolderCreated(viewHolder: CacheViewHolder, viewType: Int) {
        when(viewType){
            R.layout.item_more_type_header_0 -> {
                viewHolder.itemView.apply {
                    rv_more_type.apply {
                        adapter = mTypeAdapter
                        layoutManager = GridLayoutManager(context,4)
                    }
                }
            }
            R.layout.item_more_type -> {
            }
        }
    }

    val mTypeAdapter by lazy {
        TypeMoreAdapter(context){}
    }
    override fun getFooterLayout(var1: Int): Int = var1

    override fun hasFooter(var1: Int): Boolean = false

    override fun getChildLayout(var1: Int): Int =  var1

    override fun onBindFooterViewHolder(var1: CacheViewHolder?, var2: Int) {
    }

    override fun getGroupCount(): Int = 2

}
