package com.mp.douyu.ui.home.special

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mp.douyu.R
import com.mp.douyu.base.MBaseFragment
import com.mp.douyu.ui.home.HomeViewModel
import kotlinx.android.synthetic.main.fragment_special_topic.*

class SpecialTopicFragment : MBaseFragment() {

    override val layoutId: Int
        get() = R.layout.fragment_special_topic

    override fun initView() {
        initRecyclerView()
        initData()
    }

    private fun initData() {
        homeViewModel.getSpecialTopic(hashMapOf())
    }

    private val homeViewModel by getViewModel(HomeViewModel::class.java) {
        _videoSpecialTopicData.observe(it, Observer {
            it?.let {
                (rv_special_topic.adapter as SpecialTopicMainAdapter).sb = it
                (rv_special_topic.adapter as SpecialTopicMainAdapter).changeDataSet()
            }
        })
    }

    private fun initRecyclerView() {
        rv_special_topic.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = SpecialTopicMainAdapter(context) { it: View, position: Int ->
                when (it.id) {
                    R.id.iv_video_face, R.id.tv_title -> {
                        (adapter as SpecialTopicMainAdapter).sb?.special?.let {
                            startActivityWithTransition(SpecialTopicDetailActivity.open(context,
                                it[position]))
                        }
                    }
                }
            }
        }
    }

}