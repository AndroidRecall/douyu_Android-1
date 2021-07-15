package com.swbg.mlivestreaming.ui.home.more_type

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.bean.TypeBean
import com.swbg.mlivestreaming.singleClick
import kotlinx.android.synthetic.main.item_type.view.*

class TypeMoreAdapter(val context: Context?, val function: (View) -> Unit) :
    CachedAutoRefreshAdapter<TypeBean>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_type, parent, false))
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            tv_content.let { view ->
                view.text = get(position).name
                view.singleClick {
                    function.invoke(it)
                    context.startActivity(VideoListActivity.open(context,get(position)))
                }
            }
        }
    }

}
