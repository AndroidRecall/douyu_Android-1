package com.mp.douyu.ui.home.recommended

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.mp.douyu.R
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.bean.ChessBean
import com.mp.douyu.bean.RecommandGameBean
import com.mp.douyu.matchWaterDropScreen
import com.mp.douyu.provider.StoredUserSources
import com.mp.douyu.provider.TokenProvider
import com.mp.douyu.setActivityImmersion
import com.mp.douyu.singleClick
import com.mp.douyu.ui.home.HomeViewModel
import com.mp.douyu.ui.login_register.LoginActivity
import com.mp.douyu.ui.mine.walnut.ChargeCenterActivity
import com.mp.douyu.view.popupwindow.BaseFragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_home_chess.*

class HomeRecommendedChessActivity : MBaseActivity() {
    override val contentViewLayoutId: Int
        get() = R.layout.activity_home_chess

    var mFragment: ArrayList<Fragment> = arrayListOf()
    override fun initView() {
        matchWaterDropScreen(this@HomeRecommendedChessActivity)
        //设置沉浸式
        setActivityImmersion(this@HomeRecommendedChessActivity)

        rootView.singleClick {
            onBackPressed()
        }


        tv_login.apply {
            text = if (TokenProvider.get()
                    .hasToken()
            ) getString(R.string.charge) else getString(R.string.login)
            singleClick {
                if (isNotNeedLogin()) {
                    startActivityWithTransition(ChargeCenterActivity.open(this@HomeRecommendedChessActivity))
                }
            }
        }

        homeViewModel.getRecommandGame()
    }

    override fun onResume() {
        super.onResume()
        tv_login.apply {
            text = if (TokenProvider.get()
                    .hasToken()
            ) getString(R.string.charge) else getString(R.string.login)
        }
        initData()
    }
    private fun isNotNeedLogin(): Boolean {
        return if (!TokenProvider.get().hasToken()) {
            startActivityWithTransition(LoginActivity.open(this))
            false
        } else {
            true
        }
    }
    private fun initData() {
        val userInfo = StoredUserSources.getUserInfo()
        val userInfo2 = StoredUserSources.getUserInfo2()
        tv_game_walnut.text = "${userInfo?.user?.game_balance}"
        tv_mipa_walnut.text = "${userInfo2?.balance}"
    }

    private fun initViewPager(recommendBean: List<RecommandGameBean?>) {
        mFragment.clear()
        recommendBean.map {
            val normalList = arrayListOf<ChessBean>()
            it?.games?.map {
                normalList.add(ChessBean(imageUrl = it.cover, url = it.url, imageId = "${it.id}"))
            }
            mFragment.add(HomeChessFragment.newInstance(normalList,it?.title))
/*
            when (it?.title) {
                "彩票" -> {
                    val normalList = arrayListOf<ChessBean>()
                    it.games?.map {
                        normalList.add(ChessBean(imageUrl = it.cover, url = it.url, imageId = 0))
                    }
                    mFragment.add(HomeChessFragment.newInstance(normalList))
                }
                "棋牌" -> {
                    mFragment.add(HomeChessFragment.newInstance(arrayListOf(ChessBean(R.mipmap.recommend_chess_happy,
                        it.games?.get(0)?.url),
                        ChessBean(R.mipmap.recommend_chess_double, it.games?.get(1)?.url))))
                }
                "真人" -> {
                    mFragment.add(HomeChessFragment.newInstance(arrayListOf(ChessBean(R.mipmap.recommend_hbtt,
                        it.games?.get(0)?.url),
                        ChessBean(R.mipmap.recommend_ag, it.games?.get(1)?.url),
                        ChessBean(R.mipmap.recommend_bg, it.games?.get(2)?.url),
                        ChessBean(R.mipmap.recommend_ebet, it.games?.get(3)?.url))))
                }
                "电竞" -> {
                    mFragment.add(HomeChessFragment.newInstance(arrayListOf(ChessBean(R.mipmap.recommend_ht,
                        it.games?.get(0)?.url),
                        ChessBean(R.mipmap.recommend_im_electronic, it.games?.get(1)?.url))))
                }
                "体育" -> {
                    mFragment.add(HomeChessFragment.newInstance(arrayListOf(ChessBean(R.mipmap.recommend_all_sports,
                        it.games?.get(0)?.url),
                        ChessBean(R.mipmap.recommend_im, it.games?.get(1)?.url))))
                }
                "电子" -> {
                    mFragment.add(HomeChessFragment.newInstance(arrayListOf(ChessBean(R.mipmap.recommend_ht_fish,
                        it.games?.get(0)?.url),
                        ChessBean(R.mipmap.recommend_ag_fish, it.games?.get(1)?.url),
                        ChessBean(R.mipmap.recommend_pg_el, it.games?.get(2)?.url))))
                }
            }
*/
        }
        /* mFragment = arrayListOf(HomeChessFragment.newInstance(arrayListOf(ChessBean(R.mipmap.recommend_dream_lottery),
                 ChessBean(R.mipmap.recommend_lottery_double),
                 ChessBean(R.mipmap.recommend_tcg))),
                 HomeChessFragment.newInstance(arrayListOf(ChessBean(R.mipmap.recommend_chess_happy),
                     ChessBean(R.mipmap.recommend_chess_double))),
                 HomeChessFragment.newInstance(arrayListOf(ChessBean(R.mipmap.recommend_hbtt),
                     ChessBean(R.mipmap.recommend_ag),
                     ChessBean(R.mipmap.recommend_bg),
                     ChessBean(R.mipmap.recommend_ebet))),
                 HomeChessFragment.newInstance(arrayListOf(ChessBean(R.mipmap.recommend_ht),
                     ChessBean(R.mipmap.recommend_im_electronic))),
                 HomeChessFragment.newInstance(arrayListOf(ChessBean(R.mipmap.recommend_all_sports),
                     ChessBean(R.mipmap.recommend_im))),
                 HomeChessFragment.newInstance(arrayListOf(ChessBean(R.mipmap.recommend_ht_fish),
                     ChessBean(R.mipmap.recommend_ag_fish),
                     ChessBean(R.mipmap.recommend_pg_el))))*/
        for (i in 0 until 7 - mFragment.size) {
            val normalList = arrayListOf<ChessBean>()
            mFragment.add(HomeChessFragment.newInstance(normalList, ""))
        }

        view_pager.apply {
            adapter = BaseFragmentPagerAdapter(supportFragmentManager, mFragment)
            currentItem = 0
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                    setSelect(position)
                }
            })
        }
        for (i in 0 until ll_pay_channel.childCount) {
            ll_pay_channel.getChildAt(i).singleClick {
                view_pager.currentItem = i
                setSelect(i)
            }
        }
        setSelect(getType)
        view_pager.currentItem = getType


    }

    fun setListener() {
    }


    private val homeViewModel by getViewModel(HomeViewModel::class.java) {
        _videoRecommandGameData.observe(it, Observer {
            it?.let {
                initViewPager(it)
            }
        })
    }


    private fun setSelect(position: Int) {
        for (i in 0 until ll_pay_channel.childCount) {
            ll_pay_channel.getChildAt(i).isSelected = (i == position)
        }
    }

    private val getType by lazy {
        intent.getIntExtra(TYPE, 0)
    }


    companion object {
        const val TYPE = "TYPE"
        fun open(context: Context, type: Int): Intent {
            return Intent(context, HomeRecommendedChessActivity::class.java).putExtra(TYPE, type)
        }
    }
}