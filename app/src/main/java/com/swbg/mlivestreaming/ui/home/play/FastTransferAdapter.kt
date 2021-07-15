package com.swbg.mlivestreaming.ui.home.play

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.bean.ChargeCenterBean
import com.swbg.mlivestreaming.bean.ChooseMoneyNumBean
import com.swbg.mlivestreaming.singleClick
import kotlinx.android.synthetic.main.item_mipa_wallet.view.*

class FastTransferAdapter(val context: Context?) : CachedAutoRefreshAdapter<ChooseMoneyNumBean>() {
    private var mListener:OnSelectListener? =null
    var currentChooseItem = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_fast_transfer_money, parent, false)).apply {}
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            val get = get(position)
            tv_money.apply {
                text = get.content
                isSelected = get.isSelect
                singleClick {
                    setSelect(position)
                }
            }
        }
    }

    private fun setSelect(position: Int) {
        data.mapIndexed { index, chooseMoneyNumBean ->
            chooseMoneyNumBean.isSelect = (position == index)
        }
        notifyItemChanged(position)
        notifyItemChanged(currentChooseItem)
        currentChooseItem = position
        mListener?.onSelect(position)
    }
    fun setOnSelectListener(listener:OnSelectListener) {
        mListener = listener
    }
    interface OnSelectListener {
        fun onSelect(position: Int)
    }
}
