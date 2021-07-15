package com.swbg.mlivestreaming.ui.home.nv_info

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.bean.NvDetailBean
import com.swbg.mlivestreaming.loadGlobleVideo
import com.swbg.mlivestreaming.singleClick
import kotlinx.android.synthetic.main.item_high_definition_video.view.*
import kotlinx.android.synthetic.main.item_nv_info.view.*
import kotlinx.android.synthetic.main.item_nv_info_header.view.*

class NvInfoAdapter(var context: Context?,val listener : (Int)->Unit ) : CachedAutoRefreshAdapter<NvDetailBean>() {
    val infoList: ArrayList<NvDetailBean> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        when (viewType) {
            0 -> return CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_nv_info_header, parent, false)).apply {

            }
            1 -> return CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_nv_info, parent, false)).apply {

            }
            else -> return CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_nv_info, parent, false)).apply {
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (infoList[position].isHeader) {
            false -> 1
            else -> 0
        }
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> {
                holder.itemView.apply {
                    val nvInfoBean = infoList[position]
                    tv_header.text = nvInfoBean.name
                }
            }
            1 -> {
                holder.itemView.apply {
                    val nvInfoBean = infoList[position]
                    tv_name.text = nvInfoBean.name
                    loadGlobleVideo(context,nvInfoBean.cover,iv_face)

//                    Glide.with(context).load(nvInfoBean.cover).into(iv_face)
                    tv_height.text = "${nvInfoBean.height}CM"
                    tv_cup.text = "${nvInfoBean.cup}罩杯"
                    tv_three.text = "${nvInfoBean.measurements}"
                    singleClick {
                        listener.invoke(position)
                    }
                }
            }
        }
    }

}
