package com.swbg.mlivestreaming.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import cn.jzvd.Jzvd
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.lxj.xpopup.XPopup
import com.swbg.mlivestreaming.BuildConfig
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.bean.CommentBean
import com.swbg.mlivestreaming.provider.StoredUserSources
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.view.gridview.NineImageAdapter
import kotlinx.android.synthetic.main.search_recycle_item_dynamic.view.*
import kotlinx.android.synthetic.main.search_recycle_item_dynamic.view.tv_comment_num
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_comment_header.view.*
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_comment_header.view.grid_view
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_comment_header.view.iv_avatar
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_comment_header.view.tv_content
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_comment_header.view.tv_follow_num
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_comment_header.view.tv_like_num
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_comment_header.view.tv_name
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_comment_header.view.tv_time
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_comment_header.view.videoPlayer
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_comment_title.view.*
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_img.view.*

import org.jzvd.jzvideo.TAG
import java.util.ArrayList

class SquareDynamicCommentAdapter(private val listener: () -> Unit, var context: Context?) :
    CachedAutoRefreshAdapter<CommentBean>() {
    companion object {
        const val TYPE_CONTENT = 0
        const val TYPE_HEADER = 1
        const val TYPE_TITLE = 2
        const val TYPE_HEADER2 = 3
    }

    override var data: MutableList<CommentBean>
        get() = super.data
        set(value) {}
    var mListener: OnCommentListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return when (viewType) {
            TYPE_HEADER -> CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.square_recycle_item_dynamic_comment_header,
                    parent,
                    false)).apply {
                itemView.apply {}
            }
            TYPE_HEADER2 -> CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.square_recycle_item_dynamic_comment_header2,
                    parent,
                    false)).apply {
                itemView.apply {}
            }
            TYPE_TITLE -> CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.square_recycle_item_dynamic_comment_title, parent, false)).apply {
                itemView.apply {}
            }
            else -> CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.square_recycle_item_dynamic_comment, parent, false)).apply {
                itemView.apply {}
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (data.size > 0) data[position].itemType else super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_HEADER -> setHeaderItemView(holder, position)
            TYPE_HEADER2 -> setHeaderItemView2(holder, position)
            TYPE_TITLE -> setTitleItemView(holder, position)
            else -> setContentItemView(holder, position)
        }

    }

    private fun setHeaderItemView(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            get(position).headerBean?.apply {
                if (user?.avatar?.contains("http") == false) {
                    user?.avatar = BuildConfig.IMAGE_BASE_URL + user?.avatar
                }
                Glide.with(context).load(user?.avatar).circleCrop()
                    .placeholder(R.mipmap.default_avatar).into(iv_avatar)
                tv_name.text = "${user?.nickname}"
                tv_content.text = "${content}"
                tv_view_num.text = "${view_count ?: 0}"
                tv_time.text = update_time
                tv_like_num.text = "${like_count ?: 0}"
                if (user?.id == StoredUserSources.getUserInfo2()?.id) {
                    tv_follow_num.visibility = View.INVISIBLE
                }
//                tv_follow_num.text = if (is_follow == 0) "关注" else "已关注"
                if (is_follow == 0) {
                    tv_follow_num.text = "关注"
                    tv_follow_num.setTextColor(resources.getColor(R.color.color_4688FF))
                    tv_follow_num.setBackgroundResource(R.drawable.btn_gradient_201f1f_border_white_bg_layer)
                } else {
                    tv_follow_num.text = "已关注"
                    tv_follow_num.setTextColor(resources.getColor(R.color.colorMiddleGray))
                    tv_follow_num.setBackgroundResource(R.drawable.btn_gray_border_bg)

                }
                setDrawableStart(if (is_like == 0) R.mipmap.ic_box_like_false else R.mipmap.icon_dianzhan_true,
                    tv_like_num)
                iv_avatar.apply {
                    singleClick {

                    }
                }
                tv_like_num.apply {
                    singleClick {
                        mListener?.onLikePostClick(position, is_like)
                    }
                }
                tv_follow_num.apply {
                    singleClick {
                        mListener?.onFollowClick(position)
                    }
                }

                if (url?.isNotBlank() == true) {
                    Log.e(TAG, "设置视频")
                    videoPlayer.visibility = View.VISIBLE
                    videoPlayer.setUp("${BuildConfig.IMAGE_BASE_URL}${url}", "", Jzvd.SCREEN_NORMAL)
                    videoPlayer.positionInList = position
                } else if (images?.isNotEmpty() == true) {
                    Log.e(TAG, "设置九宫格图片")
                    grid_view.visibility = View.VISIBLE
                    val requestOptions = RequestOptions().centerCrop()
                    val drawableTransitionOptions = DrawableTransitionOptions.withCrossFade()
                    grid_view.setAdapter(NineImageAdapter(context,
                        requestOptions,
                        drawableTransitionOptions,
                        images))
                    grid_view.setOnImageClickListener { pos, view ->
                        val list: MutableList<Any> = ArrayList()
                        images?.forEach {
                            list.add("${it.url}")
                        }
                        XPopup.Builder(context)
                            .asImageViewer(view as ImageView, pos, list, { popupView, position ->
                                popupView.updateSrcView(grid_view.getChildAt(position) as ImageView)
                            }, DynamicAdapter.MyImageLoader()).show()
                    }
                }

            }
            if (get(position).advBean != null) {
                context?.let { it1 ->
                    iv_banner.visibility = View.VISIBLE
                    if (get(position).advBean?.cover?.contains("http") == false) {
                        get(position).advBean?.cover =
                            BuildConfig.IMAGE_BASE_URL + get(position).advBean?.cover
                    }
                    Glide.with(it1).load(get(position).advBean?.cover).centerCrop()
                        .placeholder(R.mipmap.bg_home_page_top).into(iv_banner)
                    iv_banner.singleClick {
                        var intent =
                            Intent(Intent.ACTION_VIEW, Uri.parse("${get(position).advBean?.url}"));
                        intent.setClassName("com.android.browser",
                            "com.android.browser.BrowserActivity");
                        it1.startActivity(intent)
                    }
//                                ActivityUtils.jumpToWebView(advBean.url, it1)
                }
            } else {
                iv_banner.visibility = View.GONE
            }
        }

    }

    private fun setHeaderItemView2(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            get(position).apply {
                if (user?.avatar?.contains("http") == false) {
                    user?.avatar = BuildConfig.IMAGE_BASE_URL + user?.avatar
                }
                Glide.with(context).load(user?.avatar).circleCrop()
                    .placeholder(R.mipmap.default_avatar).into(iv_avatar)
                tv_name.text = "${user?.nickname}"
                tv_content.text = "${content}"
                tv_time.text = update_time
                tv_like_num.text = "${like_count ?: 0}"
                tv_like_num.text = "${get(position).like_count ?: 0}"
                if (StoredUserSources.getUserInfo2()?.id == user?.id) {
                    tv_follow_num.visibility = View.INVISIBLE
                }
                tv_follow_num.text = if (is_follow == 0) "关注" else "已关注"
                //                setDrawableStart(if (is_like == 0) R.mipmap.ic_box_like_false else R.mipmap.icon_dianzhan_true,
                //                    tv_like_num)
                iv_avatar.apply {
                    singleClick {

                    }
                }
                tv_like_num.apply {
                    singleClick {
                        //                        mListener?.onLikePostClick(position, is_like)
                    }
                }
                tv_follow_num.apply {
                    singleClick {
                        mListener?.onFollowClick(position)
                    }
                }

            }
        }

    }

    private fun setTitleItemView(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            tv_title.text = "${get(position).title}"
            tv_title_comment_num.text = "${get(position).comment_num}"
        }
    }

    private fun setContentItemView(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            if (get(position).user?.avatar?.contains("http") == false) {
                get(position).user?.avatar = BuildConfig.IMAGE_BASE_URL + get(position).user?.avatar
            }
            Glide.with(context).load(get(position).user?.avatar).circleCrop()
                .placeholder(R.mipmap.default_avatar).into(iv_avatar)
            tv_comment_num.text = "${get(position).data_i.size}"
            tv_name.text = get(position).user?.nickname
            tv_content.text = get(position).content
            tv_like_num.text = "${get(position).like_count ?: 0}"
            tv_time.text = get(position).update_time

            iv_avatar.singleClick {
                mListener?.onHeaderClick(position)
            }


            tv_like_num.apply {
                singleClick {}
            }

            singleClick {
                listener.invoke()
                mListener?.onCommentClick(position)
            }
        }
    }


    fun setDrawableStart(@DrawableRes drawable: Int, textView: TextView) {
        context?.resources?.getDrawable(drawable)?.let { drawable ->
            drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight);
            textView.setCompoundDrawables(drawable, null, null, null);
        }
    }

    fun setListener(listener: OnCommentListener) {
        mListener = listener
    }

    interface OnCommentListener {
        fun onHeaderClick(position: Int)
        fun onFollowClick(position: Int)
        fun onLikePostClick(position: Int, isLike: Int?)
        fun onCommentClick(position: Int)
    }

}