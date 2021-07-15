package com.mp.douyu.ui.home.recommended

import android.app.Activity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.iyao.recyclerviewhelper.adapter.takeInstance
import com.mp.douyu.R
import com.mp.douyu.base.GroupedRecyclerViewAdapter
import com.mp.douyu.bean.BannerImageBean
import com.mp.douyu.bean.RecommandGameBean
import com.mp.douyu.bean.RecommendCateBean
import com.mp.douyu.bean.RecommendedBean
import com.mp.douyu.loadGlobleVideo
import com.mp.douyu.singleClick
import com.mp.douyu.utils.ActivityUtils
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.item_header_recommend_0.view.*
import kotlinx.android.synthetic.main.item_header_recommend_1.view.*

class RecommendedAdapter(val activity: Activity?, private val listener: (View) -> Unit) :
    GroupedRecyclerViewAdapter(activity) {
    var recommendedBean: RecommendedBean? = RecommendedBean()
    var listGameBean: ArrayList<RecommandGameBean?> = arrayListOf()

    override fun onBindHeaderViewHolder(holder: CacheViewHolder, groupPosition: Int) {
        when (getHeaderViewType(groupPosition)) {

            R.layout.item_header_recommend_0 -> {
                //轮播
                holder.itemView.home_banner.apply {
                    adapter =
                        object : BannerImageAdapter<BannerImageBean>(recommendedBean?.banner) {
                            override fun onBindView(holder: BannerImageHolder, data: BannerImageBean, position: Int, size: Int) {
                                loadGlobleVideo(context,data.imageUrl,holder.imageView)

//                                Glide.with(context).load(data.imageUrl).error(R.drawable.drawable_video_error_bg)
//                                    .apply(RequestOptions.bitmapTransform(RoundedCorners(5))
//                                        .centerCrop()).into(holder.imageView)
                            }
                        }
                    setOnBannerListener { data, position ->
                        activity?.let {
                            recommendedBean?.banner?.get(position)?.url?.apply {
                                ActivityUtils.jumpToWebView(this, it)
                            }
                        }
                    }
                    setIndicator(CircleIndicator(context))
                }
                holder.itemView.apply {
                    tv_scroll.text = recommendedBean?.headlines ?: ""
                    listGameBean.mapIndexed { index, recommandGameBean ->
                        when (recommandGameBean?.id) {
                            0 -> {
                                loadGlobleVideo(context,recommandGameBean.cover,ib_lottery)

//                                Glide.with(context).load(recommandGameBean.cover).into(ib_lottery)
                            }
                            1 -> {
                                loadGlobleVideo(context,recommandGameBean.cover,ib_chess)

//                                Glide.with(context).load(recommandGameBean.cover).into(ib_chess)
                            }
                            2 -> {
                                loadGlobleVideo(context,recommandGameBean.cover,ib_trueman)

//                                Glide.with(context).load(recommandGameBean.cover).into(ib_trueman)
                            }
                            3 -> {
                                loadGlobleVideo(context,recommandGameBean.cover,ib_seletronic_sports)

//                                Glide.with(context).load(recommandGameBean.cover).into(ib_seletronic_sports)
                            }
                            4 -> {
                                loadGlobleVideo(context,recommandGameBean.cover,ib_sports)

//                                Glide.with(context).load(recommandGameBean.cover).into(ib_sports)
                            }
                            5 -> {
                                loadGlobleVideo(context,recommandGameBean.cover,ib_electonic)

//                                Glide.with(context).load(recommandGameBean.cover).into(ib_electonic)
                            }
                            6 -> {
                                loadGlobleVideo(context,recommandGameBean.cover,ib_fish)

//                                Glide.with(context).load(recommandGameBean.cover).into(ib_fish)
                            }
                            else ->{

                            }
                        }
                    }
                    ib_lottery.singleClick {         //彩票
                        listener.invoke(it)
                    }
                    ib_chess.singleClick {
                        listener.invoke(it)

                    }
                    ib_trueman.singleClick {
                        listener.invoke(it)

                    }
                    ib_seletronic_sports.singleClick {
                        listener.invoke(it)

                    }
                    ib_sports.singleClick {
                        listener.invoke(it)

                    }
                    ib_electonic.singleClick {
                        listener.invoke(it)
                    }
                    ib_fish.singleClick {
                        listener.invoke(it)
                    }
                }
            }
            R.layout.item_header_recommend_1 -> {
                holder.itemView.apply {
                    rv_video.apply {
                        adapter = videoAdapter
                        layoutManager = LinearLayoutManager(context)
                    }
                    recommendedBean?.cate?.let {
                        videoAdapter.takeInstance<CachedAutoRefreshAdapter<RecommendCateBean>>()
                            .refresh(it, null)
                    }
                }
            }
        }

    }

    override fun getHeaderViewType(groupPosition: Int): Int = when (groupPosition) {
        0 -> R.layout.item_header_recommend_0
        1 -> R.layout.item_header_recommend_1
        else -> R.layout.item_header_recommend_0
    }

    override fun hasHeader(var1: Int): Boolean = true

    override fun getHeaderLayout(var1: Int): Int = var1

    override fun getChildrenCount(var1: Int): Int = 0

    override fun onBindChildViewHolder(var1: CacheViewHolder?, var2: Int, var3: Int) {
    }

    override fun onViewHolderCreated(viewHolder: CacheViewHolder?, viewType: Int) {
        when (viewType) {
            R.layout.item_header_recommend_0 -> {
            }
            R.layout.item_header_recommend_1 -> {
            }
        }
    }

    override fun getFooterLayout(var1: Int): Int = var1

    override fun hasFooter(var1: Int): Boolean = false


    private val videoAdapter by lazy {
//        RecommendedLittleTypeAdapter(context,recommendedBean)
        RecommendTestAdapter({

        }, activity)
    }

    override fun getChildLayout(var1: Int): Int = var1

    override fun onBindFooterViewHolder(var1: CacheViewHolder?, var2: Int) {
    }

    override fun getGroupCount(): Int = 2
}
