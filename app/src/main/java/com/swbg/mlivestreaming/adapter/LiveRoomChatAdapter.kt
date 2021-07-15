package com.swbg.mlivestreaming.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.bean.ChatMsgBean
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.utils.SpanUtils
import kotlinx.android.synthetic.main.live_recycle_item_chat_room.view.*

class LiveRoomChatAdapter(private val listener: (Int) -> Unit, var context: Context?) :
    CachedAutoRefreshAdapter<ChatMsgBean>() {
    companion object {
        const val TYPE_CONTENT = 0
        const val TYPE_TIP = 1
    }

    private val TAG: String = javaClass.simpleName
    var betListener: OnBetListener? = null
    override var data: MutableList<ChatMsgBean>
        get() = super.data
        set(value) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {

        return when (viewType) {
            TYPE_TIP -> CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.live_recycle_item_chat_tip, parent, false)).apply {
                itemView.apply {}
            }
            else -> CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.live_recycle_item_chat_room, parent, false)).apply {
                itemView.apply {}
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (data.size > 0) data[position].itemViewType else super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        when (get(position).itemViewType) {
            TYPE_TIP -> setTipItemView(holder,position)
            else -> setContentItemView(holder,position)
        }
    }

    private fun setContentItemView(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply{
        var keywordColor = Color.parseColor("#FFFFD400");
            val matcherTitle =
                SpanUtils.matcherTitle("${get(position).nickname}:${get(position).content}",
                    "${get(position).nickname}",
                    keywordColor)
//            tv_content.text = "${get(position).nickname}:${get(position).content}"
            tv_content.text = matcherTitle

            singleClick {
                listener.invoke(position)
            }
        }
    }

    private fun setTipItemView(holder: CacheViewHolder, position: Int) {

    }

    fun setListener(onBetListener: OnBetListener) {
        this.betListener = onBetListener
    }

    interface OnBetListener {
        fun onBet()
    }

}