package com.swbg.mlivestreaming.ui.home.search

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.GroupedRecyclerViewAdapter
import com.swbg.mlivestreaming.bean.CommendBean
import com.swbg.mlivestreaming.bean.SearchUserBean
import com.swbg.mlivestreaming.bean.VideoBean
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.ui.home.high_definition.HighDefinitionVideoAdapter
import com.swbg.mlivestreaming.ui.home.more_type.VideoListActivity
import com.swbg.mlivestreaming.utils.WindowUtils
import kotlinx.android.synthetic.main.item_header_search_0.view.*
import kotlinx.android.synthetic.main.item_header_search_1.view.*

class SearchAdapter(context: Context, val clickListener: (View) -> Unit) :
    GroupedRecyclerViewAdapter(context) {
    var listCommend = arrayListOf<CommendBean>(
//        CommendBean(),
//        CommendBean(),
//        CommendBean(),
//        CommendBean(),
//        CommendBean(),
//        CommendBean()
    )
    var listVideo = arrayListOf<VideoBean>()
    override fun onBindHeaderViewHolder(var1: CacheViewHolder?, var2: Int) {
        when (getHeaderViewType(var2)) {
            R.layout.item_header_search_0 -> {
                mRelateUserAdapter.refresh(listOf(), null)
            }
            R.layout.item_header_search_1 -> {
                videoAdapter.refresh(listVideo, null)
            }
        }
    }

    override fun getHeaderViewType(groupPosition: Int): Int {
        return when (groupPosition) {
            0 -> R.layout.item_header_search_0
            1 -> R.layout.item_header_search_1
            else -> 0
        }
    }

    override fun hasHeader(var1: Int): Boolean = when (var1) {
        0, 1 -> true
        else -> false
    }

    override fun getHeaderLayout(var1: Int): Int = var1

    override fun getChildrenCount(var1: Int): Int = when (var1) {
        2 -> listCommend.size
        else -> 0
    }

    override fun onBindChildViewHolder(holder: CacheViewHolder, groupPosition: Int, childPosition: Int) {
        when (getChildViewType(groupPosition, childPosition)) {
            R.layout.item_search_post -> {
            }
        }
    }

    override fun getChildViewType(groupPosition: Int, childPosition: Int): Int {
        return when (groupPosition) {
            2 -> R.layout.item_search_post
            else -> 0
        }
    }

    override fun onViewHolderCreated(viewHolder: CacheViewHolder, viewType: Int) {
        when (viewType) {
            R.layout.item_header_search_0 -> {
                viewHolder.itemView.apply {
                    item_recycler_view.apply {
                        adapter = mRelateUserAdapter
                        layoutManager = GridLayoutManager(context, 4)
                    }
                }
            }
            R.layout.item_header_search_1 -> {
                viewHolder.itemView.apply {
                    rv_video.apply {
                        adapter = videoAdapter
                        layoutManager = GridLayoutManager(context, 2).apply {
                            addItemDecoration(object : RecyclerView.ItemDecoration() {

                                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                                    val viewPosition =
                                        (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
                                    if (viewPosition != 0 && viewPosition % 2 != 0) {
                                        outRect.set(0, 0, WindowUtils.dip2Px(12f), 0)
                                    }
                                }
                            })
                        }
                    }
                    tv_more.apply {
                        singleClick {
                            clickListener.invoke(this)
                        }
                    }
                }
            }
            R.layout.item_search_post -> {
            }
        }
    }

    private val mRelateUserAdapter by lazy {
        RelateUserAdapter(context)
    }

    private val videoAdapter by lazy {
        HighDefinitionVideoAdapter({}, context)
    }

    override fun getFooterLayout(var1: Int): Int = var1

    override fun hasFooter(var1: Int): Boolean = false

    override fun getChildLayout(var1: Int): Int = var1

    override fun onBindFooterViewHolder(var1: CacheViewHolder?, var2: Int) {
    }

    override fun getGroupCount(): Int = 3

}
