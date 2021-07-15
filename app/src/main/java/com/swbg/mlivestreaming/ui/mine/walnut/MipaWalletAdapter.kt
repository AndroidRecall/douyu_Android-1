package com.swbg.mlivestreaming.ui.mine.walnut

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.bean.TradeRecordItemBean
import kotlinx.android.synthetic.main.item_mipa_wallet.view.*

class MipaWalletAdapter(private val activity: Activity?, val typeName: String) :
    CachedAutoRefreshAdapter<TradeRecordItemBean>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(activity)
            .inflate(R.layout.item_mipa_wallet, parent, false))
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            val get = get(position)
            tv_content.text = get.remarks
            tv_time_detail.text = get.create_time
            tv_money.text = "${when (typeName) {
                "存款" -> "+"
                "转账" -> "-"
                "消费" -> "-"
                "获得" -> "+"
                "消费" -> "-"
                else -> "+"
            }} ${get.amount}"
            tv_time.text = get.create_time?.run {
                if (length > 6) substring(0, 7)
                else this
            }

        }
    }

}
