package com.mp.douyu.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jzvd.Jzvd
import com.blankj.utilcode.util.SpanUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.XPopupImageLoader
import com.mp.douyu.BuildConfig
import com.mp.douyu.R
import com.mp.douyu.bean.CommonUserBean
import com.mp.douyu.bean.DynamicBean
import com.mp.douyu.customMedia.JZMediaIjk
import com.mp.douyu.provider.StoredUserSources
import com.mp.douyu.singleClick
import com.mp.douyu.ui.square.me.SquareDynamicFragment.Companion.TYPE_MINE_FOLLOWED
import com.mp.douyu.ui.square.me.SquareDynamicFragment.Companion.TYPE_SPACE_COLLECT
import com.mp.douyu.ui.square.me.SquareDynamicFragment.Companion.TYPE_SPACE_PUBLISH
import com.mp.douyu.ui.square.me.SquareDynamicFragment.Companion.TYPE_SPACE_RECORD
import com.mp.douyu.ui.square.me.SquareDynamicFragment.Companion.TYPE_SPACE_REPLY
import com.mp.douyu.utils.DrawableUtils
import com.mp.douyu.utils.ScreenUtils
import com.mp.douyu.view.gridview.NineImageAdapter
import kotlinx.android.synthetic.main.square_recycle_item_ad.view.*
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_img.view.*
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_img.view.iv_avatar
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_img.view.recycle_comment
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_img.view.tv_comment_num
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_img.view.tv_content
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_img.view.tv_follow_num
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_img.view.tv_like_num
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_img.view.tv_name
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_img.view.tv_time
import kotlinx.android.synthetic.main.square_recycle_item_dynamic_video.view.*
import org.jzvd.jzvideo.TAG
import java.io.File
import java.util.*

class DynamicAdapter(private val listener: () -> Unit, var context: Context?, var type: Int? = TYPE_MINE_FOLLOWED) :
    CachedAutoRefreshAdapter<DynamicBean>() {
    companion object {
        const val TYPE_NOR_CONTENT = 0
        const val TYPE_VIDEO_CONTENT = 1
        const val TYPE_ADV_CONTENT = 2
    }

    override var data: MutableList<DynamicBean>
        get() = super.data
        set(value) {}
    var mListener: OnDynamicListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return when (viewType) {
            TYPE_VIDEO_CONTENT -> {
                DynamicHolder(context,
                    LayoutInflater.from(context)
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
                DynamicHolder(context,
                    LayoutInflater.from(context)
                        .inflate(R.layout.square_recycle_item_dynamic_img, parent, false)).apply {
                    itemView.apply {}
                }
            }

            else -> {
                DynamicHolder(context,
                    LayoutInflater.from(context)
                        .inflate(R.layout.square_recycle_item_dynamic_img, parent, false)).apply {
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
            if (get(position).advBean?.cover?.contains("http") == false) {
                get(position).advBean?.cover =
                    BuildConfig.IMAGE_BASE_URL + get(position).advBean?.cover
            }
            Glide.with(context).load(get(position).advBean?.cover)
                .placeholder(R.mipmap.icon_place_holder_error).into(iv_ad_cover)
            singleClick {
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse("${get(position).advBean?.url}"));
                intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                context.startActivity(intent)
            }
        }
    }

    private fun setItemView(holder: CacheViewHolder, position: Int) {
        val dynamicBean = get(position)
        holder.itemView.apply {
            Log.e("onBindViewHolder", "is_like=${dynamicBean.is_like},position=${position}")
            if (dynamicBean.user?.avatar?.contains("http") == false) {
                dynamicBean.user?.avatar = BuildConfig.IMAGE_BASE_URL + dynamicBean.user?.avatar
            }
            Glide.with(context).load(dynamicBean.user?.avatar).circleCrop()
                .placeholder(R.mipmap.default_avatar).into(iv_avatar)
            tv_name.text = "${dynamicBean.user?.nickname}"
            var contentStr = "${dynamicBean.content}"
            if (type == TYPE_SPACE_REPLY) {
                dynamicBean.images?.let {
                    if (it.size > 0) {
                        contentStr = "原文 [图片] ${dynamicBean.content}"
                    }
                }
                dynamicBean.url?.let {
                    if (it.isNotEmpty()) {
                        contentStr = "原文 [视频] ${dynamicBean.content}"
                    }
                }
                tv_content.text = contentStr
            }
            if (type != TYPE_SPACE_REPLY) {
                val spanUtils = SpanUtils.with(tv_content)
                if (dynamicBean.is_top == 1) {
                    val bitmap: Bitmap =
                        BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_top)
                    val imageScale =
                        imageScale(bitmap, ScreenUtils.dp2px(14f), ScreenUtils.dp2px(14f))
                    spanUtils.appendImage(imageScale!!,SpanUtils.ALIGN_BASELINE)
//                    spanUtils.appendImage(R.mipmap.ic_top, SpanUtils.ALIGN_BASELINE)
                }
                if (dynamicBean.is_elite == 1) {
                    val bitmap: Bitmap =
                        BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_elite)
                    val imageScale =
                        imageScale(bitmap, ScreenUtils.dp2px(14f), ScreenUtils.dp2px(14f))
                    spanUtils.appendImage(imageScale!!,SpanUtils.ALIGN_BASELINE)
//                    spanUtils.appendImage(R.mipmap.ic_elite, SpanUtils.ALIGN_BASELINE)
                }
                spanUtils.append(contentStr).create()
            }
            tv_comment_num.text = "${dynamicBean.comm_count}"
            tv_time.text = dynamicBean.update_time
            if (dynamicBean.is_follow == 0) {
                tv_follow_num.text = "关注"
                tv_follow_num.setTextColor(resources.getColor(R.color.color_4688FF))
                tv_follow_num.setBackgroundResource(R.drawable.btn_gradient_201f1f_border_white_bg_layer)
            } else {
                tv_follow_num.text = "已关注"
                tv_follow_num.setTextColor(resources.getColor(R.color.colorMiddleGray))
                tv_follow_num.setBackgroundResource(R.drawable.btn_gray_border_bg)

            }
            /*当该动态是本人发的，或者是“我的空间”里面的，则隐藏关注按钮*/
            if (dynamicBean.user?.id == StoredUserSources.getUserInfo2()?.id || type == TYPE_SPACE_PUBLISH || type == TYPE_SPACE_REPLY || type == TYPE_SPACE_COLLECT || type == TYPE_SPACE_RECORD) {
                tv_follow_num.visibility = View.GONE
            }
            tv_like_num.text = "${dynamicBean.like_count}"
            if (dynamicBean.is_like == 0) {
                DrawableUtils.setDrawable(drawable = R.mipmap.ic_box_like_false,
                    textView = tv_like_num)
            } else {
                DrawableUtils.setDrawable(drawable = R.mipmap.icon_dianzhan_true,
                    textView = tv_like_num)
            }

            iv_avatar.apply {
                singleClick {
                    mListener?.onHeaderClick(position)
                }
            }

            tv_like_num.apply {
                singleClick {
                    mListener?.onLikeClick(position, dynamicBean.is_like)
                }
            }
            tv_follow_num.apply {
                singleClick {
                    mListener?.onFollowClick(position, dynamicBean.is_follow)
                }
            }

            if (type != TYPE_SPACE_REPLY) {
                if (dynamicBean.getItemViewType() == TYPE_VIDEO_CONTENT) {
                    Log.e(TAG, "设置视频 position=${position},url=${dynamicBean.url}")
                    setVideoPlay(position)


                } else if (dynamicBean.getItemViewType() == TYPE_NOR_CONTENT) {
                    if (dynamicBean.images?.isNotEmpty() == true) {
                        Log.e(TAG, "设置九宫格图片")
                        grid_view.visibility = View.VISIBLE
                        setNineImgView(position)
                    } else {
                        grid_view.visibility = View.GONE
                    }
                }
            }
            recycle_comment.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = (holder as DynamicHolder).commentAdapter.apply {
                    setListener(object : SquareDynamicCommentAdapter.OnCommentListener {
                        override fun onHeaderClick(position: Int) {
                            this@DynamicAdapter.mListener?.onCommentHeaderClick(get(position).user)
                        }

                        override fun onFollowClick(position: Int) {
                        }

                        override fun onLikePostClick(position: Int, isLike: Int?) {
                        }

                        override fun onCommentClick(position: Int) {
                        }
                    })
                    dynamicBean.comments?.apply {
                        if (size < 3) {
                            holder.commentAdapter.refresh(this, null)
                        } else {
                            holder.commentAdapter.refresh(subList(0, 3), null)
                        }
                    }
                }


            }

            singleClick {
                //                listener.invoke()
                mListener?.onCommentClick(position)
            }
        }
    }

    private fun View.setNineImgView(position: Int) {
        val requestOptions = RequestOptions().centerCrop()
        val drawableTransitionOptions = DrawableTransitionOptions.withCrossFade()
        grid_view.setAdapter(NineImageAdapter(context,
            requestOptions,
            drawableTransitionOptions,
            get(position).images))
        grid_view.setOnImageClickListener { pos, view ->
            val list: MutableList<Any> = ArrayList()
            get(position).images?.forEach {
                list.add("${it.url}")
            }
            XPopup.Builder(context)
                .asImageViewer(view as ImageView, pos, list, { popupView, position ->
                    popupView.updateSrcView(grid_view.getChildAt(position) as ImageView)
                }, MyImageLoader()).show()
        }
    }

    private fun View.setVideoPlay(position: Int) {
        videoPlayer.setUp("${BuildConfig.IMAGE_BASE_URL}${get(position).url}",
            "",
            Jzvd.SCREEN_NORMAL,
            JZMediaIjk::class.java)
        suspend {

        }
//        videoPlayer.posterImageView.setImageBitmap(UploadUtils.getNetVideoBitmap(
//            "${BuildConfig.IMAGE_BASE_URL}${get(position).url}"))
        videoPlayer.positionInList = position
    }

    fun setNewData(data: MutableList<DynamicBean>) {
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

    class DynamicHolder(context: Context?, containerView: View) : CacheViewHolder(containerView) {
        val commentAdapter by lazy {
            SquareDynamicCommentAdapter({}, context).apply {

            }
        }
    }

//    private fun setAdView(position: Int, view: ImageView) {
//        val dynamicBean = get(position)
//        if (dynamicBean.is_show_ad == true) {
//            if (RelationManager.instance.advBeans.size > 0) {
//
//                val value = position % RelationManager.instance.advBeans.size
//                RelationManager.instance.advBeans[value]?.let { advBean ->
//                    context?.let { it1 ->
//                        Log.e(TAG,"设置广告")
//                        view.visibility = View.VISIBLE
//                        if (advBean?.cover?.contains("http") == false) {
//                            advBean?.cover = BuildConfig.IMAGE_BASE_URL + advBean?.cover
//                        }
//                        Glide.with(it1).load(advBean.cover).centerCrop()
//                            .placeholder(R.mipmap.bg_home_page_top).into(view)
//                        view.singleClick {
//                            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("${advBean.url}"));
//                            intent.setClassName("com.android.browser",
//                                "com.android.browser.BrowserActivity");
//                            it1.startActivity(intent)
//                            //                                ActivityUtils.jumpToWebView(advBean.url, it1)
//                        }
//                    }
//                }
//            } else {
//                view.visibility = View.GONE
//            }
//        } else {
//            view.visibility = View.GONE
//
//        }
//    }

    /**
     * 调整图片大小
     *
     * @param bitmap 源
     * @param dst_w  输出宽度
     * @param dst_h  输出高度
     * @return
     */
    fun imageScale(bitmap: Bitmap, dst_w: Int, dst_h: Int): Bitmap? {
        val src_w: Int = bitmap.getWidth()
        val src_h: Int = bitmap.getHeight()
        val scale_w = dst_w.toFloat() / src_w
        val scale_h = dst_h.toFloat() / src_h
        val matrix = Matrix()
        matrix.postScale(scale_w, scale_h)
        return Bitmap.createBitmap(bitmap, 0, 0, src_w, src_h, matrix, true)
    }

}