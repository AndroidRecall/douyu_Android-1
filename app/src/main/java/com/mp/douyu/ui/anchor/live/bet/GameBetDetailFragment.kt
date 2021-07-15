package com.mp.douyu.ui.anchor.live.bet

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.lxj.xpopup.XPopup
import com.mp.douyu.R
import com.mp.douyu.adapter.LiveGameListAdapter
import com.mp.douyu.base.VisibilityFragment
import com.mp.douyu.bean.GameBean
import com.mp.douyu.dialog.TransferDialog
import kotlinx.android.synthetic.main.live_fragment_game_bet_detail.*

class GameBetDetailFragment : VisibilityFragment() {
    private var currentPage: Int = 1
    private var pageSize: Int = 10
    private var type: Int? = GAME_TYPE_LOTTERY
    private val lotteryData: Array<GameBean> by lazy {
        arrayOf(GameBean(itemViewType = GAME_TYPE_LOTTERY,
            iconId = R.mipmap.img_game_lottery_ybcp_por),
            GameBean(itemViewType = GAME_TYPE_LOTTERY,
                iconId = R.mipmap.img_game_lottery_double_por),
            GameBean(itemViewType = GAME_TYPE_LOTTERY,
                iconId = R.mipmap.img_game_lottery_bingo_por))
    }
    private val cardData: Array<GameBean> by lazy {
        arrayOf(GameBean(itemViewType = GAME_TYPE_CARD, iconId = R.mipmap.img_game_chess_ybqp_por),
            GameBean(itemViewType = GAME_TYPE_CARD, iconId = R.mipmap.img_game_chess_double_por))
    }
    private val realityData: Array<GameBean> by lazy {
        arrayOf(GameBean(itemViewType = GAME_TYPE_REALITY, iconId = R.mipmap.img_game_rp_hbtt_por),
            GameBean(itemViewType = GAME_TYPE_REALITY, iconId = R.mipmap.img_game_rp_ag_por),
            GameBean(itemViewType = GAME_TYPE_REALITY, iconId = R.mipmap.img_game_rp_bgzr_por),
            GameBean(itemViewType = GAME_TYPE_REALITY, iconId = R.mipmap.img_game_rp_ebet_por))
    }
    private val esportData: Array<GameBean> by lazy {
        arrayOf(GameBean(itemViewType = GAME_TYPE_ESPORT, iconId = R.mipmap.img_game_esport_ht_por),
            GameBean(itemViewType = GAME_TYPE_ESPORT, iconId = R.mipmap.img_game_esport_im_por))
    }
    private val physicalData: Array<GameBean> by lazy {
        arrayOf(GameBean(itemViewType = GAME_TYPE_PHYSICAL,
            iconId = R.mipmap.img_game_sport_yabo_por),
            GameBean(itemViewType = GAME_TYPE_PHYSICAL, iconId = R.mipmap.img_game_sport_im_por))
    }
    private val electronicData: Array<GameBean> by lazy {
        arrayOf(GameBean(itemViewType = GAME_TYPE_ELECTRONIC,
            iconId = R.mipmap.img_game_egame_htby),
            GameBean(itemViewType = GAME_TYPE_ELECTRONIC, iconId = R.mipmap.img_game_egame_ag),
            GameBean(itemViewType = GAME_TYPE_ELECTRONIC, iconId = R.mipmap.img_game_egame_pg))
    }

    companion object {
        const val EXTRA_GAME_TYPE = "type"
        const val GAME_TYPE_LOTTERY = 0
        const val GAME_TYPE_CARD = 1
        const val GAME_TYPE_REALITY = 2
        const val GAME_TYPE_ESPORT = 3
        const val GAME_TYPE_PHYSICAL = 4
        const val GAME_TYPE_ELECTRONIC = 5
        fun newInstance(type: Int = GAME_TYPE_LOTTERY): GameBetDetailFragment {
            val fragment = GameBetDetailFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_GAME_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }
    }

    override val layoutId: Int
        get() = R.layout.live_fragment_game_bet_detail

    override fun initView() {
        print("initView:FollowFragment")
    }

    override fun onVisible() {
        super.onVisible()
        Log.e(TAG, "onVisible")
    }

    override fun onInvisible() {
        super.onInvisible()
        Log.e(TAG, "onInvisible")
    }

    override fun onVisibleFirst() {
        type = arguments?.getInt(EXTRA_GAME_TYPE, GAME_TYPE_LOTTERY)
        recycler_view?.apply {
            adapter = mAdapter
            layoutManager =when (type) {
                GAME_TYPE_LOTTERY ->  LinearLayoutManager(context)
                GAME_TYPE_CARD ->  LinearLayoutManager(context)
                GAME_TYPE_REALITY ->  GridLayoutManager(context, 2)
                GAME_TYPE_ESPORT -> LinearLayoutManager(context)
                GAME_TYPE_PHYSICAL ->  LinearLayoutManager(context)
                GAME_TYPE_ELECTRONIC ->  LinearLayoutManager(context)
                else -> LinearLayoutManager(context)
            }
        }
//        refreshLayout.setRefreshHeader(ClassicsHeader(context))
//        refreshLayout.setRefreshFooter(ClassicsFooter(context))
//        refreshLayout.setOnRefreshListener {
//            currentPage = 1
//           loadData()
//        }
//        refreshLayout.setOnLoadMoreListener { refreshlayout ->
//            currentPage += 1
//            loadData()
//        }
        loadData()
    }

    private fun loadData() {


    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
    }

    val mAdapter by lazy {
        LiveGameListAdapter({}, context).apply {
            addAll(when (type) {
                GAME_TYPE_LOTTERY -> lotteryData
                GAME_TYPE_CARD -> cardData
                GAME_TYPE_REALITY -> realityData
                GAME_TYPE_ESPORT -> esportData
                GAME_TYPE_PHYSICAL -> physicalData
                GAME_TYPE_ELECTRONIC -> electronicData
                else -> lotteryData
            })
            setOnGameListener(object :LiveGameListAdapter.OnGameListener{
                override fun onPlay(position: Int) {
                    XPopup.Builder(activity).asCustom(TransferDialog(activity!!)).show()
                }
            })
        }
    }

}