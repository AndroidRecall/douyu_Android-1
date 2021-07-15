package com.mp.douyu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.mp.douyu.R
import com.mp.douyu.bean.GiftNumberBean
import com.mp.douyu.singleClick
import kotlinx.android.synthetic.main.live_recycle_item_gift_number.view.*

class GiftNumberAdapter(private val listener: (view:View,positon:Int) -> Unit, var context: Context?) :
    CachedAutoRefreshAdapter<GiftNumberBean>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.live_recycle_item_gift_number, parent, false)).apply {
            itemView.apply {}
        }
    }

    override var data: MutableList<GiftNumberBean>
        get() = super.data
        set(value) {}

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            tv_content.text = "${get(position).number} ${get(position).content}"

            singleClick {
                listener.invoke(it,position)
            }
        }
    }

}