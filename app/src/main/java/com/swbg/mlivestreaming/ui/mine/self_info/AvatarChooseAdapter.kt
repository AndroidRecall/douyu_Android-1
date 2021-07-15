package com.swbg.mlivestreaming.ui.mine.self_info

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.bean.ManBean
import com.swbg.mlivestreaming.singleClick
import kotlinx.android.synthetic.main.item_avatar_choose.view.*
import kotlinx.android.synthetic.main.item_avatar_choose_header.view.*

class AvatarChooseAdapter(var activity: Activity, var listAvatars: List<ManBean>,val listener : (View,Int) -> Unit) :
    CachedAutoRefreshAdapter<ManBean>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        when (viewType) {
            0 -> {
                return CacheViewHolder(LayoutInflater.from(activity)
                    .inflate(R.layout.item_avatar_choose_header, parent, false))
            }
            else -> return CacheViewHolder(LayoutInflater.from(activity)
                .inflate(R.layout.item_avatar_choose, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> 0
            else -> if (listAvatars[position].isTitle) {
                0
            } else {
                1
            }
        }
    }


    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> {
                holder.itemView.apply {
                    tv_title.text = if (position == 0) "男生" else "女生"
                }
            }
            else -> {
                holder.itemView.apply {
                    Glide.with(context)
//                        .load("https://img.zcool.cn/community/01233056fb62fe32f875a9447400e1.jpg")
                        .load(get(position).imageUrl)
                        .apply {
                            RequestOptions.circleCropTransform().placeholder(R.mipmap.default_avatar)
                                .error(R.mipmap.default_avatar).centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                        }.into(iv_avatar)
                    iv_choose.visibility = if (listAvatars[position].isSelect!!) View.VISIBLE else View.GONE
                    singleClick {
                        listener.invoke(it,position)
                        setSelect(position)
                    }
                }
            }
        }
    }

    private fun setSelect(position: Int) {
        var selectPosition = position
        for (i in listAvatars.indices) {
            if (listAvatars[i]?.isSelect!! && selectPosition == position) {
                selectPosition = i
            }
            listAvatars[i].isSelect = (i == position)
        }
        notifyItemChanged(selectPosition)
        notifyItemChanged(position)
//        refresh(listAvatars,null)
    }
}
