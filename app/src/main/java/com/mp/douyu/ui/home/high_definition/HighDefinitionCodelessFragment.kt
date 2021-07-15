package com.mp.douyu.ui.home.high_definition

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.mp.douyu.R
import com.mp.douyu.base.MBaseFragment
import com.mp.douyu.bean.TypeBean
import com.mp.douyu.ui.home.HomeViewModel
import kotlinx.android.synthetic.main.fragment_high_definition.*

class HighDefinitionCodelessFragment : MBaseFragment() {
    private var currentPage : Int = 1
    override val layoutId: Int
        get() = R.layout.fragment_high_definition

    override fun initView() {
        recyclerView.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(context, 3).apply {

            }
        }


        refreshLayout.setRefreshHeader(ClassicsHeader(context))
        refreshLayout.setRefreshFooter(ClassicsFooter(context))
        refreshLayout.setOnRefreshListener {
            currentPage = 1
            loadBegin()
        }
        refreshLayout.setOnLoadMoreListener { refreshlayout ->
            currentPage += 1
            loadBegin()
        }
        loadBegin()
//        mAdapter.takeInstance<CachedAutoRefreshAdapter<VideoBean>>().refresh(listOf(VideoBean("视频1",
//            faceImageUrl = "https://img.zcool.cn/community/01233056fb62fe32f875a9447400e1.jpg"),
//            VideoBean("视频2",
//                faceImageUrl = "https://img.zcool.cn/community/016a2256fb63006ac7257948f83349.jpg"),
//            VideoBean("视频3",
//                faceImageUrl = "https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg"),
//            VideoBean("视频3",
//                faceImageUrl = "https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg"),
//            VideoBean("视频3",
//                faceImageUrl = "https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg"),
//            VideoBean("视频3",
//                faceImageUrl = "https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg"),
//            VideoBean("视频3",
//                faceImageUrl = "https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg"),
//            VideoBean("视频3",
//                faceImageUrl = "https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg"),
//            VideoBean("视频3",
//                faceImageUrl = "https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg"),
//            VideoBean("视频3",
//                faceImageUrl = "https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg"),
//            VideoBean("视频3",
//                faceImageUrl = "https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg"),
//            VideoBean("视频3",
//                faceImageUrl = "https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg"),
//            VideoBean("视频3",
//                faceImageUrl = "https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg"),
//            VideoBean("视频3",
//                faceImageUrl = "https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg"),
//            VideoBean("视频3",
//                faceImageUrl = "https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg"),
//            VideoBean("视频3",
//                faceImageUrl = "https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg"),
//            VideoBean("视频3",
//                faceImageUrl = "https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg"),
//            VideoBean("视频3",
//                faceImageUrl = "https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg"),
//            VideoBean("视频1",
//                faceImageUrl = "https://img.zcool.cn/community/01233056fb62fe32f875a9447400e1.jpg")),
//            null)
    }

    private val mAdapter by lazy {
        HighDefinitionVideoAdapter({}, context)
    }

    private val homeViewModel by getViewModel(HomeViewModel::class.java) {
        _videoListData.observe(it, Observer {
            refreshLayout.finishRefresh()
            refreshLayout.finishLoadMore()
            it?.let {
                if (currentPage == 1) {
                    it.data?.let { it1 -> mAdapter.refresh(it1, null) }
                } else {
                    it.data?.let { it1 -> mAdapter.addAll(it1) }
                }
            }
        })
    }

    private fun loadBegin() {
        homeViewModel.getVideoList(hashMapOf("cate_id" to getYpeBean?.cateId,
            "special_id" to getYpeBean?.specialId,
            "sort" to "new",
            "size" to "15",
            "page" to "${currentPage}"))
    }

    private val getYpeBean by lazy {
        arguments?.getParcelable<TypeBean>(LIST_TYPE)
    }
    companion object {
        const val LIST_TYPE = "list_type"
        fun newInstance(typeBean: TypeBean): HighDefinitionCodelessFragment {
            val fragment = HighDefinitionCodelessFragment()
            val bundle = Bundle()
            bundle.putParcelable(LIST_TYPE, typeBean)
            fragment.arguments = bundle
            return fragment
        }
    }
}
