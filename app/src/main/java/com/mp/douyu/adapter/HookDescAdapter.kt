package com.mp.douyu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.mp.douyu.R
import com.mp.douyu.bean.HookDescBean
import com.mp.douyu.singleClick
import kotlinx.android.synthetic.main.hook_recycle_item_desc.view.*
import kotlinx.android.synthetic.main.hook_recycle_item_desc_foot.view.*

class HookDescAdapter(private val listener: (Int) -> Unit, var context: Context?) :
    CachedAutoRefreshAdapter<HookDescBean>() {
    companion object {
        const val TYPE_CONTENT = 0
        const val TYPE_FOOT = 1
    }

    private val TAG: String = javaClass.simpleName
    var betListener: OnBetListener? = null
    override var data: MutableList<HookDescBean>
        get() = super.data
        set(value) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {

        return when (viewType) {
            TYPE_FOOT -> CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.hook_recycle_item_desc_foot, parent, false)).apply {
                itemView.apply {}
            }
            else -> CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.hook_recycle_item_desc, parent, false)).apply {
                itemView.apply {}
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (data.size > 0) data[position].itemViewType else super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        when (get(position).itemViewType) {
            TYPE_FOOT -> setFootItemView(holder,position)
            else -> setContentItemView(holder,position)
        }
    }

    private fun setContentItemView(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            if (position == 0) {
                line_top.visibility = View.INVISIBLE
            }
            if (position == data.size - 2) {
                line_bottom.visibility = View.INVISIBLE
            }
            tv_step.text = "第${position + 1}步"
            tv_content.text = "${get(position).tip}"

            singleClick {
                listener.invoke(position)
            }
        }
    }

    private fun setFootItemView(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            tv_desc_foot_content.text = get(position).tip
        }
    }

    fun setListener(onBetListener: OnBetListener) {
        this.betListener = onBetListener
    }

    interface OnBetListener {
        fun onBet()
    }

}