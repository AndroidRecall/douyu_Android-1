package com.mp.douyu.ui.home.recommended

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mp.douyu.R
import com.mp.douyu.base.MBaseFragment
import com.mp.douyu.provider.StoredUserSources
import com.mp.douyu.ui.home.HomeViewModel
import kotlinx.android.synthetic.main.fragment_recommended.*

class RecommendedFragment : MBaseFragment() {
    override val layoutId: Int
        get() = R.layout.fragment_recommended

    override fun initView() {
        initRecyclerView()
        initData()
    }

    private val homeViewModel by getViewModel(HomeViewModel::class.java) {
        _recommendData.observe(it, Observer {
            it?.let {
                mAdapter.recommendedBean = it
                mAdapter.changeDataSet()

                StoredUserSources.getSettingData()?.let { it1 ->
                    it1.headlines = it.headlines
                    StoredUserSources.putSettingData(it1)
                }
            }
        })

        _videoRecommandGameData.observe(it, Observer {
            it?.let {
                mAdapter.listGameBean.addAll(it)
                mAdapter.changeHeader(0)
            }
        })
    }

    private fun initData() {
        homeViewModel.getRecommendData()
        homeViewModel.getRecommandGame()
    }


    private fun initRecyclerView() {
        rv_recommended.apply {
            /* layoutManager = GridLayoutManager(activity, 3).apply {
                 spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                     override fun getSpanSize(position: Int): Int {
                         return if (position == 0) {
                             3
                         } else 1
                     }
                 }
             }*/
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter

        }
    }

    private val mAdapter by lazy {
        RecommendedAdapter(activity) {
            when (it.id) {
                R.id.ib_lottery -> {
                    openPage(0)

                }
                R.id.ib_chess -> {
                    openPage(1)

                }
                R.id.ib_trueman -> {
                    openPage(2)

                }
                R.id.ib_seletronic_sports -> {
                    openPage(3)

                }
                R.id.ib_sports -> {
                    openPage(4)

                }
                R.id.ib_electonic -> {
                    openPage(5)
                }
                R.id.ib_fish -> {
                    openPage(6)
                }
            }
        }
    }

    private fun openPage(i: Int) {
        context?.let { it1 ->
            HomeRecommendedChessActivity.open(it1, i)
        }?.let { it2 -> startActivityWithTransition(it2) }
    }
}
