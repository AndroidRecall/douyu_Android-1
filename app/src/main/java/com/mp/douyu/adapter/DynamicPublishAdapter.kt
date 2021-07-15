package com.mp.douyu.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.mp.douyu.R
import com.mp.douyu.bean.PublishItemBean
import com.mp.douyu.singleClick
import com.mp.douyu.ui.square.comment.PublishImageFragment.Companion.TYPE_PUBLISH_POST
import com.mp.douyu.ui.square.comment.PublishImageFragment.Companion.TYPE_PUBLISH_VIDEO
import kotlinx.android.synthetic.main.dynamic_recycle_item_img.view.*

class DynamicPublishAdapter(private val listener: () -> Unit, var context: Context?) :
    CachedAutoRefreshAdapter<PublishItemBean>() {
    companion object {
        const val TYPE_CONTENT = 0
        const val TYPE_FOOT = 1
    }

    override var data: MutableList<PublishItemBean>
        get() = super.data
        set(value) {}
    var mListener: OnSelectListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return when (viewType) {
            TYPE_FOOT -> CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.dynamic_recycle_item_publish, parent, false)).apply {
                itemView.apply {}
            }
            else -> CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.dynamic_recycle_item_img, parent, false)).apply {
                itemView.apply {}
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (data.size > 0) data[position].itemType else super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_FOOT -> setFootItemView(holder, position)
            else -> setContentItemView(holder, position)
        }

    }

    private fun setFootItemView(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            singleClick {
                mListener?.onAdd(position)
            }
        }

    }

    private fun setContentItemView(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            var url = ""

            get(position).localMedia?.run {

                url = if (PictureMimeType.getMimeType(getMimeType()) === PictureConfig.TYPE_VIDEO) {
                    //            url = item.localMedia.getPath();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        androidQToPath
                    } else {
                        path
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        androidQToPath
                    } else {
                        compressPath
                    }
                }
                url
            }

            Glide.with(context).load(url).placeholder(R.mipmap.default_avatar).into(iv_content)
            iv_del.apply {
                singleClick {
                    mListener?.onDel(holder.adapterPosition)
                }

            }
            iv_content.apply {
                singleClick {

                }
            }

            singleClick {
                listener.invoke()
            }
        }
    }

    fun setListener(listener: OnSelectListener) {
        mListener = listener
    }

    fun getFootCount(): Int {
        var count = 0
        data.let {
            for (bean in data) {
                if (bean.itemType == TYPE_FOOT) {
                    count++
                }
            }
        }
        return count
    }

    fun getDataList(): MutableList<PublishItemBean> {
        var list = arrayListOf<PublishItemBean>()
        data.let {
            for (bean in data) {
                if (bean.itemType != TYPE_FOOT) {
                    list.add(bean)
                }
            }
        }
        return list
    }

    fun removeFootView() {
        val iterator = data.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().itemType == TYPE_FOOT) {
                iterator.remove()
            }
        }
        notifyDataSetChanged()
    }

    fun addFootView(type: Int) {
        when (type) {

            TYPE_PUBLISH_POST -> {
                //发布文章，需要判断当前已选择的内容类型,图片 9张 ，视频一张
                if (data.size > 0) {
                    //取第一个内容来判断选择的是什么类型的内容
                    if (PictureMimeType.getMimeType(data[0].localMedia?.mimeType) === PictureConfig.TYPE_IMAGE
                        &&data.size < 9
                        && getFootCount() == 0) {
                        //当前是图片
                        add(PublishItemBean(itemType = TYPE_FOOT))
                    }

                } else {
                    if (getFootCount() == 0) {
                        add(PublishItemBean(itemType = TYPE_FOOT))
                    }
                }
            }
            TYPE_PUBLISH_VIDEO -> {
                //发布视频
                if (data.size < 1 && getFootCount() == 0) {
                    add(PublishItemBean(itemType = TYPE_FOOT))
                }
            }
        }
    }

    interface OnSelectListener {
        fun onDel(position: Int)
        fun onAdd(position: Int)
    }

}