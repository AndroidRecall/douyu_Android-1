package com.swbg.mlivestreaming.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.bean.WebViewBtnBean
import com.swbg.mlivestreaming.singleClick
import kotlinx.android.synthetic.main.item_webview_btn.view.*

class WebViewBtnAdapter(val context: Context, val listener: (Int) -> Unit) :
    CachedAutoRefreshAdapter<WebViewBtnBean>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_webview_btn, parent, false))
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        holder.itemView.iv_image.apply {
            visibility = when (get(position).isShow) {
                true -> View.VISIBLE
                false -> View.GONE
            }
            Glide.with(context).load(get(position).imageUrl).into(this)
            singleClick {
                listener.invoke(position)
            }
        }
    }


}
