package com.swbg.mlivestreaming.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.lxj.xpopup.interfaces.XPopupImageLoader
import com.swbg.mlivestreaming.BuildConfig
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.bean.CommonUserBean
import com.swbg.mlivestreaming.bean.MineMessageBean
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.ui.mine.msg.MineMessageDetailFragment.Companion.TYPE_MINE_LIKED
import com.swbg.mlivestreaming.ui.mine.msg.MineMessageDetailFragment.Companion.TYPE_MINE_REPLY
import kotlinx.android.synthetic.main.mine_recycle_item_msg_reply.view.*
import kotlinx.android.synthetic.main.square_recycle_item_ad.view.*


import java.io.File

class MineMessageAdapter(private val listener: () -> Unit, var context: Context?, var type: Int? = TYPE_MINE_REPLY) :
    CachedAutoRefreshAdapter<MineMessageBean>() {
    companion object {
        const val TYPE_NOR_CONTENT = 0
        const val TYPE_VIDEO_CONTENT = 1
        const val TYPE_ADV_CONTENT = 2
    }

    override var data: MutableList<MineMessageBean>
        get() = super.data
        set(value) {}
    var mListener: OnDynamicListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return when (viewType) {
            TYPE_VIDEO_CONTENT -> {
                CacheViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.square_recycle_item_dynamic_video, parent, false)).apply {
                    itemView.apply {}
                }
            }
            TYPE_ADV_CONTENT -> {
                CacheViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.square_recycle_item_ad, parent, false)).apply {
                    itemView.apply {}
                }
            }
            TYPE_NOR_CONTENT -> {
                CacheViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.mine_recycle_item_msg_reply, parent, false)).apply {
                    itemView.apply {}
                }
            }

            else -> {
                CacheViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.mine_recycle_item_msg_reply, parent, false)).apply {
                    itemView.apply {}
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].getItemViewType()
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        var pos = holder.layoutPosition
        when (holder.itemViewType) {
            TYPE_ADV_CONTENT -> {
                setAdItemView(holder, position)
            }
            else -> {
                setItemView(holder, position)
            }
        }

    }

    private fun setAdItemView(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            Glide.with(context).load(BuildConfig.IMAGE_BASE_URL + get(position).advBean?.cover)
                .circleCrop().placeholder(R.mipmap.icon_place_holder_error).into(iv_ad_cover)
            singleClick {
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse("${get(position).advBean?.url}"));
                intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                context.startActivity(intent)
            }
        }
    }

    private fun setItemView(holder: CacheViewHolder, position: Int) {
        val bean = get(position)
        holder.itemView.apply {
            Log.e("onBindViewHolder", "is_like=${bean.is_like},position=${position}")
            Glide.with(context).load(BuildConfig.IMAGE_BASE_URL + bean.user?.avatar)
                .circleCrop().placeholder(R.mipmap.default_avatar).into(iv_avatar)
            tv_name.text = "${bean.user?.nickname}"
            tv_content.text = if (type==TYPE_MINE_LIKED) "点赞你！" else bean.content
            var contentStr = "[原文] ${bean.post?.content}"
                bean.images?.let {
                    if (it.size > 0) {
                        contentStr = "=[原文] [图片] ${bean.post?.content}"
                    }
                }
                bean.url?.let {
                    if (it.isNotEmpty()) {
                        contentStr = "[原文] [视频] ${bean.post?.content}"
                    }
                }
            tv_origin.text = contentStr

            singleClick {
                //                listener.invoke()
                mListener?.onCommentClick(position)
            }
        }
    }
    fun setNewData(data: MutableList<MineMessageBean>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun setListener(listener: OnDynamicListener) {
        mListener = listener
    }

    interface OnDynamicListener {
        fun onFollowClick(position: Int, isFollow: Int?)
        fun onLikeClick(position: Int, isLike: Int?)
        fun onCommentClick(position: Int)
        fun onHeaderClick(position: Int)
        fun onAdClick(position: Int)
        fun onCommentHeaderClick(user: CommonUserBean?)
    }

    class MyImageLoader : XPopupImageLoader {
        override fun loadImage(position: Int, url: Any, imageView: ImageView) {
            //必须指定Target.SIZE_ORIGINAL，否则无法拿到原图，就无法享用天衣无缝的动画
            Glide.with(imageView).load(BuildConfig.IMAGE_BASE_URL + url)
                .apply(RequestOptions().placeholder(R.mipmap.login_code_failed)
                    .override(Target.SIZE_ORIGINAL)).into(imageView)
        }

        override fun getImageFile(context: Context, uri: Any): File? {
            try {
                return Glide.with(context).downloadOnly().load(uri).submit().get()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }

}