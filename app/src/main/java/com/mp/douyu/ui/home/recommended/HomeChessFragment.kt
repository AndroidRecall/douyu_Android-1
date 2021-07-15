package com.mp.douyu.ui.home.recommended

import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.iyao.recyclerviewhelper.adapter.CachedStatusWrapper
import com.iyao.recyclerviewhelper.adapter.takeInstance
import com.mp.douyu.GlobeStatusViewHolder.isNotNeedLogin
import com.mp.douyu.R
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.base.MBaseFragment
import com.mp.douyu.bean.ChessBean
import com.mp.douyu.jumpIsToGamePageDialog
import com.mp.douyu.singleClick
import com.mp.douyu.ui.home.HomeViewModel
import com.mp.douyu.utils.LogUtils
import com.mp.douyu.utils.Utils
import kotlinx.android.synthetic.main.fragment_home_chess.*
import kotlinx.android.synthetic.main.item_chess.view.*

class HomeChessFragment : MBaseFragment() {

    private val datas: ArrayList<ChessBean> by lazy {
        arguments?.getParcelableArrayList(LIST_TYPE) ?: ArrayList<ChessBean>()
    }

    //
//    private val getTitle by lazy {
//        arguments?.getString(M_TITLE) ?: ""
//    }
    override val layoutId: Int
        get() = R.layout.fragment_home_chess

    override fun initView() {
        recyclerView.apply {
            adapter = mAdapter
            layoutManager = if (isTwoLine()) GridLayoutManager(context, 2).apply {
                /* spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                     override fun getSpanSize(position: Int): Int {
                         return if (position == 0) {
                             3
                         } else 1
                     }
                 }*/
                 addItemDecoration(object : RecyclerView.ItemDecoration() {
                     override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                         if ((view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition % 2 == 0) {
                            outRect.set(Utils().dp2px(context, 10).toInt(),
                                0,
                                0,
                                0)
                        } else {
                            outRect.set(Utils().dp2px(context, 0).toInt(),
                                Utils().dp2px(context, 0).toInt(),
                                Utils().dp2px(context, 10).toInt(),
                                0)
                        }
                    }
                })
            } else GridLayoutManager(context, 1).apply {
                /*addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//                        outRect.set(Utils().dp2px(context, 5).toInt(),
//                            0,
//                            Utils().dp2px(context, 5).toInt(),
//                            0)
                    }
                })*/
            }
        }
        mAdapter.takeInstance<CachedAutoRefreshAdapter<ChessBean>>().refresh(datas, null)
    }

    private fun isTwoLine(): Boolean {
        return arguments?.getString(M_TITLE)
            ?.isNotEmpty() ?: false && arguments?.getString(M_TITLE) == "真人"
    }

    private val mAdapter by lazy {
        CachedStatusWrapper().apply {
            client = object : CachedAutoRefreshAdapter<ChessBean>() {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
                    return CacheViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.item_chess, parent, false))
                }

                override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
                    context?.let {
//                        Glide.with(it).load(get(position).imageId).into(holder.itemView.iv_chess)
                        Glide.with(it).asBitmap().load(get(position).imageUrl).centerCrop()
                            .into(object : CustomTarget<Bitmap>() {

                                override fun onLoadCleared(placeholder: Drawable?) {
                                }

                                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                    resource.let {
                                        val height = it.height
                                        val width = it.width

                                        val screenWidth = Utils().getDisplayWidth(context!!)
                                        holder.itemView.iv_chess.apply {
                                            layoutParams.width =
                                                if (isTwoLine()) (screenWidth / 2 + Utils().dp2px(
                                                    context,
                                                    5).toInt()) else screenWidth
                                            holder.itemView.iv_chess.layoutParams.height =
                                                if (isTwoLine()) ((screenWidth / 2 * height / width) - Utils().dp2px(
                                                    context,
                                                    10).toInt())
                                                else (screenWidth * height / width - Utils().dp2px(
                                                    context,
                                                    10).toInt())
                                            setImageBitmap(it)
                                        }

                                        LogUtils.e("$height , $width,$screenWidth")

                                    }

                                }
                            })
                        holder.itemView.iv_chess.singleClick {
                            if (isNotNeedLogin(activity as MBaseActivity)) {
                                get(position).imageId?.let {
                                    getGameLink(it)
                                }
                            }
//                            ActivityUtils.jumpToWebView(get(position).url, context!!)
                        }
//
                    }
                }

            }
        }
    }

    private fun getGameLink(imageId: String) {
        (activity as MBaseActivity).showLoadingView(true)
        homeViewModel.getGameLink(hashMapOf("game_id" to "$imageId"))

    }

    private val homeViewModel by getViewModel(HomeViewModel::class.java) {
        _getGameLinkData.observe(it, Observer {
            (activity as MBaseActivity).showLoadingView(false)
            it?.let {
                jumpIsToGamePageDialog(it, activity as MBaseActivity)
            }
        })
    }

    companion object {
        const val LIST_TYPE = "list_type"
        const val M_TITLE = "M_TITLE"
        fun newInstance(datas: ArrayList<ChessBean>, title: String?): HomeChessFragment {
            val fragment = HomeChessFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(LIST_TYPE, datas)
            bundle.putString(M_TITLE, title)
            fragment.arguments = bundle
            return fragment
        }
    }

}