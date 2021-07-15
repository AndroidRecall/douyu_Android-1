package com.swbg.mlivestreaming.ui.home.nv_info

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.bean.NvInfoScrollBean
import com.swbg.mlivestreaming.singleClick
import kotlinx.android.synthetic.main.item_nv_info_scroll.view.*

class NvInfoScrollAdapter(var context: Context?, val listener: (Int) -> Unit) :
    CachedAutoRefreshAdapter<NvInfoScrollBean>() {
    val infoScrollList: ArrayList<NvInfoScrollBean> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_nv_info_scroll, parent, false)).apply {}
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        holder.itemView.let {
            it.tv_key_value.apply {
                text = get(position).key
                setTextColor(if (get(position).isSelect) ContextCompat.getColor(context,
                    R.color.colorScrollText) else ContextCompat.getColor(context,
                    R.color.colorCharacterBlack))

                singleClick {
                    setSelect(position, data)
                }
            }
        }
    }

    private fun setSelect(position: Int, data: MutableList<NvInfoScrollBean>) {
        var selectPosition = position
        for (i in data.indices) {
            if (data[i].isSelect && selectPosition != position) selectPosition = i


            data[i].isSelect = (i == position)
        }

        notifyItemChanged(selectPosition)
        notifyItemChanged(position)
    }


}
