package com.swbg.mlivestreaming.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.swbg.mlivestreaming.BuildConfig
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.bean.CommonUserBean
import com.swbg.mlivestreaming.provider.StoredUserSources
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.utils.DrawableUtils
import kotlinx.android.synthetic.main.mine_recycle_item_relation.view.*

class RelationAdapter(private val listener: (Int) -> Unit, var context: Context?) :
    CachedAutoRefreshAdapter<CommonUserBean>() {
    private val TAG: String = javaClass.simpleName
    override var data: MutableList<CommonUserBean>
        get() = super.data
        set(value) {}
    var onCircleListener: OnRelationListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.mine_recycle_item_relation, parent, false)).apply {
            itemView.apply {}

        }
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            Glide.with(context).load(BuildConfig.IMAGE_BASE_URL+get(position).avatar).circleCrop()
                .placeholder(R.mipmap.default_avatar).into(iv_avatar)
            tv_name.text = "${get(position).nickname ?: "名称"}"
//            tv_people_num.text = "${get(position).user_count ?: "0"}"
//            tv_post_num.text = "${get(position).post_count ?: "0"}"
            if (get(position).id == StoredUserSources.getUserInfo2()?.id) {
                cl_join.visibility = View.INVISIBLE
            }
            tv_follow_num.text = "${if (get(position).is_follow == 0) "关注" else "已关注"}"
            if (get(position).is_follow == 0) {
                DrawableUtils.setDrawable(drawable = R.mipmap.ic_post_views,textView = tv_follow_num)
            } else {
                tv_follow_num.setCompoundDrawables(null, null, null, null)
            }
            cl_join.setBackgroundResource(if (get(position).is_follow == 0) R.drawable.btn_join_circle_bg1 else R.drawable.btn_gray_c30_bg)
//            tv_join.setTextColor(if (get(position).is_join == 0) resources.getColor(R.color.colorBtnBlue)
//            else resources.getColor(R.color.colorC8LightGray))
//            cl_join.isSelected = get(position).is_join == 0
            cl_join.setOnClickListener {
                onCircleListener?.apply {
                    onFollow(position)
                }
            }
            singleClick {
                listener.invoke(position)
                onCircleListener?.apply {
                    onItemViewClick(position)
                }
            }
        }
    }

    fun setListener(onCircleListener: OnRelationListener) {
        this.onCircleListener = onCircleListener
    }

    interface OnRelationListener {
        fun onItemViewClick(position: Int)
        fun onFollow(position: Int)
    }

}