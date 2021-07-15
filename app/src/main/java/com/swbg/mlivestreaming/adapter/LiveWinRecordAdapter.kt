package com.swbg.mlivestreaming.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.bean.GameBean
import com.swbg.mlivestreaming.bean.GameTypeBean
import com.swbg.mlivestreaming.bean.WinRecordBean
import com.swbg.mlivestreaming.utils.SpanUtils
import kotlinx.android.synthetic.main.live_recycle_item_win_record.view.*

class LiveWinRecordAdapter(val context: Context?) :
    RecyclerView.Adapter<LiveWinRecordAdapter.ViewHolder>() {
    var mListener: OnBetListener? = null
    var mData: MutableList<WinRecordBean>? =
        arrayListOf(WinRecordBean(randomName(), randomGame(), randomMoney()),
            WinRecordBean(randomName(), randomGame(), randomMoney()),
            WinRecordBean(randomName(), randomGame(), randomMoney()),
            WinRecordBean(randomName(), randomGame(), randomMoney()),
            WinRecordBean(randomName(), randomGame(), randomMoney()),
            WinRecordBean(randomName(), randomGame(), randomMoney()))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveWinRecordAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.live_recycle_item_win_record, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mData?.size ?: 0
    }

    override fun onBindViewHolder(holder: LiveWinRecordAdapter.ViewHolder, position: Int) {
        holder.itemView.apply {
            SpanUtils.makeWinResultContent(tv_title,"${mData?.get(position)?.name!!}…",mData?.get(position)?.gameBean?.gameName!!,mData?.get(position)?.monery!!)
//            tv_title.text ="恭喜 ${mData?.get(position)?.name}*** 在 ${mData?.get(position)?.gameType} 中得 ${mData?.get(position)?.monery}元"
            holder.itemView.setOnClickListener {
                mListener?.onBet(position)
            }
        }
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    }

    fun setOnLiveListener(listener: OnBetListener) {
        mListener = listener
    }

    interface OnBetListener {
        fun onBet(position: Int)
    }

    fun randomName(): String {
        var randomcode = ""
        val model = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        val m = model.toCharArray()
        for (index in 0..6) {
            val c = m[(Math.random() * 52).toInt()]
            randomcode += c
        }
        return randomcode
    }

    fun randomGame(): GameTypeBean {
        val gameTypeList = arrayListOf<String>("彩票","棋牌", "真人", "电竞", "体育", "电子", "捕鱼")
        val index = (Math.random() * 4).toInt()
        return GameTypeBean(index,gameTypeList[index])
    }

    fun randomMoney(): String {
        return String.format("%1$.2f",(Math.random() * 2000) + 100)
    }
}