package com.swbg.mlivestreaming

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.BounceInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import cn.jzvd.Jzvd
import com.bumptech.glide.Glide
import com.swbg.mlivestreaming.GlobeStatusViewHolder.isNotNeedLogin
import com.swbg.mlivestreaming.base.BaseWebViewActivity
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.bean.AdvBean
import com.swbg.mlivestreaming.download.DownLoadManager
import com.swbg.mlivestreaming.event.LiveHomeEvent
import com.swbg.mlivestreaming.provider.StoredUserSources
import com.swbg.mlivestreaming.provider.TokenProvider
import com.swbg.mlivestreaming.relation.RelationManager
import com.swbg.mlivestreaming.ui.activity.ActivityViewModel
import com.swbg.mlivestreaming.ui.anchor.LiveHomeFragment
import com.swbg.mlivestreaming.ui.game.GameFragment
import com.swbg.mlivestreaming.ui.home.HomeFragment
import com.swbg.mlivestreaming.ui.home.HomeViewModel
import com.swbg.mlivestreaming.ui.home.history.PlayRecorderActivity
import com.swbg.mlivestreaming.ui.home.history.VideoCacheRecordActivity
import com.swbg.mlivestreaming.ui.home.search.Search2Activity
import com.swbg.mlivestreaming.ui.home.search.SearchActivity
import com.swbg.mlivestreaming.ui.mine.MineFragment
import com.swbg.mlivestreaming.ui.mine.MineViewModel
import com.swbg.mlivestreaming.ui.mine.space.UserSpaceActivity
import com.swbg.mlivestreaming.ui.square.RelationViewModel
import com.swbg.mlivestreaming.ui.square.SquareHomeFragment
import com.swbg.mlivestreaming.utils.LogUtils
import com.swbg.mlivestreaming.utils.RxBus
import com.swbg.mlivestreaming.utils.RxUtils
import com.swbg.mlivestreaming.view.popupwindow.MessageAlarmDialog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.circle_fragment_detail.*
import kotlinx.android.synthetic.main.et_search_shape.*
import kotlinx.android.synthetic.main.main_title_bar.*
import kotlinx.android.synthetic.main.main_title_bar.cl_title_bar
import java.util.concurrent.TimeUnit


class MainActivity : MBaseActivity(), View.OnClickListener {
    val TAG: String = javaClass.simpleName
    private val fragments: Array<Fragment> by lazy {
        arrayOf(HomeFragment(),
            LiveHomeFragment(),
            GameFragment(),
            SquareHomeFragment(),
            MineFragment())
    }
    private var activatedPosition: Int = 0
    private val tabs by lazy {
        arrayOf(bottom_tab0, bottom_tab1, bottom_tab2, bottom_tab3, bottom_tab4).apply {
            forEachIndexed { i, cl ->
                cl.setOnClickListener(this@MainActivity)
            }
        }
    }
    override val contentViewLayoutId: Int
        get() = R.layout.activity_main

    override fun initView() {
        onHomeSelected()
        initListener()
        initData()
        initAnim()
        initEvent()
    }

    private fun initEvent() {

        RxBus.getInstance().register(LiveHomeEvent::class.java).subscribe {
            Log.e("initevent=====", "进来了==")
            setTabActivated(1)
//            when (it.type) {
//                0 -> {
//                    toLive()
//                }
//                1 -> {
//                    toHook()
//                }
//            }

        }
    }

    private fun initAnim() {

        val rotation = ObjectAnimator.ofFloat(iv2, "rotation", 0f, 720f).apply {
            duration = 2000
        }
        val scaleX = ObjectAnimator.ofFloat(iv_game_header, "scaleX", 0f, 1f).apply {
            duration = 1000

        }
        val scaleY = ObjectAnimator.ofFloat(iv_game_header, "scaleY", 0f, 1f).apply {
            duration = 1000

        }
//        val alpha = ObjectAnimator.ofFloat(iv_game_header, "alpha", 0f, 1f).apply {
//            duration =500
//
//        }


        AnimatorSet().apply {
            interpolator = BounceInterpolator()
            play(rotation)
            play(scaleX).with(scaleY).after(1000)
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    Observable.timer(2000, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe {
                            start()
                        }
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {

                }
            })
            start()
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
    }

    private fun initData() {
        LogUtils.e(TAG, "token=${TokenProvider.get().accessToken}")
        if (TokenProvider.get().hasToken()) {
            relationViewModel.getAllFollowsData(hashMapOf())
            relationViewModel.getAllPostLikesData(hashMapOf())
            relationViewModel.getAllShortVideoLikesData(hashMapOf())
            relationViewModel.getAllFollowLiveListData(hashMapOf())
        }
        activityViewModel.getAdvList(hashMapOf("type" to "1"))
        mineViewModel.getDialogList()
        DownLoadManager.instance.startCacheVideo()
    }


    private fun initListener() {
        ib_history.singleClick {
            LogUtils.i("==", "==")
            startActivityWithTransition(PlayRecorderActivity.open(this))
        }
        ib_cache.singleClick {
            LogUtils.i("==", "==")
            if (isNotNeedLogin(this)) startActivityWithTransition(VideoCacheRecordActivity.open(this))
        }
        click_btn.singleClick {
            startActivityWithTransition(Search2Activity.open(this,
                type = SearchActivity.SEARCH_TYPE_POST))
//            startActivityWithTransition(SearchActivity.open(this))
        }
        et_search.apply {
            isFocusable = false

        }
        iv_avatar.singleClick {
            if (isNotNeedLogin(this)) {
                startActivityWithTransition(UserSpaceActivity.open(this))
            }

            //
        }
    }

    private fun setTabActivated(tabPosition: Int) {
        Log.e("tabPosition=====", tabPosition.toString())
        activatedPosition = tabPosition
        val transaction = supportFragmentManager.beginTransaction()
        for (i in tabs.indices) {
            val activated = tabPosition == i
            tabs[i].isActivated = activated

            val fragment = fragments[i]
            if (!fragment.isAdded) {
                transaction.add(R.id.fragment_container, fragment, i.toString()).hide(fragment)
            }
            if (activated) {
                transaction.show(fragment)
            } else {
                transaction.hide(fragment)
            }
        }
//        if (!supportFragmentManager.isStateSaved) {
            transaction.commitAllowingStateLoss()
//        }
//        setTitleBarChange(tabPosition)
        if (!transaction.equals(4)) {
            Jzvd.releaseAllVideos()
        }
    }


    private fun setTitleBarChange(tabPosition: Int) {
        cl_title_bar.visibility = if (tabPosition == 0) VISIBLE else GONE
        when (tabPosition) {
            0 -> {
            }
            1 -> {
            }
            2 -> {
            }
            3 -> {
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (TokenProvider.get().hasToken()) {
            Glide.with(this).load(StoredUserSources.getUserInfo()?.user?.avatar)
                .error(R.mipmap.default_avatar).into(iv_avatar)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.bottom_tab0 -> onHomeSelected()
            R.id.bottom_tab1 -> onLiveSelected()
            R.id.bottom_tab2 -> onGameSelected()
            R.id.bottom_tab3 -> onSquareSelected()
            R.id.bottom_tab4 -> onMineSelected()
        }
    }

    private fun onSquareSelected() {
        setTabActivated(3)
    }

    private fun onVideoSelected() {

    }

    private fun onMineSelected() {
        setTabActivated(4)

    }

    private fun onGameSelected() {
        if (isNotNeedLogin(this)) homeViewModel.getGameLink(hashMapOf("game_id" to "0"))

//        setTabActivated(2)
    }

    private fun onLiveSelected() {
        setTabActivated(1)
    }

    private fun onHomeSelected() {
        setTabActivated(0)
    }

    open fun toHook() {
        onLiveSelected()
        RxUtils.delay(500).subscribe {
            if (fragments[activatedPosition] is LiveHomeFragment) {
                (fragments[activatedPosition] as LiveHomeFragment).toHook()
            }
        }
    }

    open fun toLive() {
        onLiveSelected()
        RxUtils.delay(500).subscribe {
            if (fragments[activatedPosition] is LiveHomeFragment) {
                (fragments[activatedPosition] as LiveHomeFragment).toLive()
            }
        }
    }

    private val homeViewModel by getViewModel(HomeViewModel::class.java) {
        _getGameLinkData.observe(it, Observer {
            it?.let {
                startActivityWithTransition(BaseWebViewActivity.open(this@MainActivity,
                    "",
                    link = "${it.url}",
                    isGamePage = true,
                    isMainGamePage = true))
//                jumpIsToGamePageDialog(it, this@MainActivity)
            }
        })
    }
    private val mineViewModel by getViewModel(MineViewModel::class.java) {
        _getDialogList.observe(it, Observer {
            it?.let {
                it.mapIndexed { index, s ->
                    MessageAlarmDialog.newInstance(s?.title, s?.content, "我知道了", "")
                        .setOnClickListener(DialogInterface.OnClickListener { dialog, which ->
                            if (which == DialogInterface.BUTTON_POSITIVE) {

                            }
                        }).show(supportFragmentManager, null)
                }
            }
        })
        _getUserInfo.observe(it, Observer {
            it?.let {
                StoredUserSources.putUserInfo(it)
                Glide.with(this@MainActivity).load(StoredUserSources.getUserInfo()?.user?.avatar)
                    .error(R.mipmap.default_avatar).into(iv_avatar)
            }
        })
    }

    private val relationViewModel by getViewModel(RelationViewModel::class.java) {
        followsData.observe(it, Observer {
            it?.let {
                RelationManager.instance.follows = it.toMutableList()
            }
        })
        postLikesData.observe(it, Observer {
            it?.data?.let {
                RelationManager.instance.postLikes = it.toMutableList()
            }
        })
        shortVideoLikesData.observe(it, Observer {
            it?.data?.let {
                RelationManager.instance.shortVideoLikes = it.toMutableList()
            }
        })
        followLiveListData.observe(it, Observer {
            it?.data?.let {
                RelationManager.instance.followLives = it.toMutableList()
            }
        })
    }
    private val activityViewModel by getViewModel(ActivityViewModel::class.java) {
        _getAdvList.observe(it, Observer {
            it?.let { list ->
                RelationManager.instance.advBeans = list.toMutableList()
            }
        })
    }


    companion object {
        const val MAIN_TAB = "MAIN_TAB"
        fun open(context: Context, tab: Int? = 0): Intent {
            Log.e("==========tab", tab.toString())
            return Intent(context, MainActivity::class.java).putExtra(MAIN_TAB, tab)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        DownLoadManager.instance.stopCacheVideo()
    }
}

