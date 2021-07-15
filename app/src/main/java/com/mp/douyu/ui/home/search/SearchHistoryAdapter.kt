package com.mp.douyu.ui.home.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.mp.douyu.R
import com.mp.douyu.bean.SearchHistoryBean
import com.mp.douyu.singleClick
import kotlinx.android.synthetic.main.item_search_history.view.*

class SearchHistoryAdapter(private val context: Context, val listener: (View,String) -> Unit) :
    CachedAutoRefreshAdapter<SearchHistoryBean>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_search_history, parent, false))
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            rv_history_content.text = get(position).searchKey
            rv_history_content.apply {
                singleClick{
                    listener.invoke(it,rv_history_content.text.toString().trim())
                }
            }
        }
    }

}
