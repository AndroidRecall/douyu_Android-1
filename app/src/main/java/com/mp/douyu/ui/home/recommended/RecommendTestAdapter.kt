package com.mp.douyu.ui.home.recommended

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.iyao.recyclerviewhelper.adapter.takeInstance
import com.mp.douyu.R
import com.mp.douyu.bean.RecommendCateBean
import com.mp.douyu.bean.TypeBean
import com.mp.douyu.bean.VideoBean
import com.mp.douyu.singleClick
import com.mp.douyu.ui.home.more_type.VideoListActivity
import com.mp.douyu.utils.DisplayUtils
import kotlinx.android.synthetic.main.item_header_recommend_little_type_0.view.*

class RecommendTestAdapter(private val listener: (View) -> Unit,var context: Context?) : CachedAutoRefreshAdapter<RecommendCateBean>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_recommended_little_type, parent, false)).apply {
            itemView.apply {
                rv_recommend_little_type.let {
                    it.adapter =         VideoAdapter({
                        listener.invoke(it)
                    }, context)
                    it.layoutManager = GridLayoutManager(context, 3)
                }

            }
        }
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            get(position).videos?.let {
//            recommendedBean?.cate?.get(position)?.videos?.let {
                rv_recommend_little_type.adapter?.takeInstance<CachedAutoRefreshAdapter<VideoBean>>()
                    ?.refresh(it, null)
            }
            if (position == itemCount - 1) {
                cl_bottom?.setPadding(0,0,0,DisplayUtils.dp2px(30f))
               /* tv_watch_more.visibility = View.INVISIBLE
                iv_watch_more?.visibility = View.INVISIBLE*/
            } else {
                cl_bottom?.setPadding(0,0,0,0)
              /*  tv_watch_more.visibility = View.VISIBLE
                iv_watch_more?.visibility = View.VISIBLE*/
            }
            tv_title.text = get(position).title ?: "最新更新"
            tv_watch_more.singleClick {
                context.startActivity(VideoListActivity.open(context, TypeBean("${get(position).title}"
                    ,"${get(position).id}","")))
            }
        }
    }

/*    private val videoAdapter by lazy {
        VideoAdapter({
            listener.invoke(it)
        }, context)
    }*/

}
