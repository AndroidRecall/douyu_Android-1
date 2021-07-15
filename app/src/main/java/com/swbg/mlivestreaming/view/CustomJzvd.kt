package com.swbg.mlivestreaming.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import cn.jzvd.JZDataSource
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.bumptech.glide.Glide
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.bean.UserInfoBean
import com.swbg.mlivestreaming.bean.VideoPlayBean
import com.swbg.mlivestreaming.customMedia.JZMediaIjk
import com.swbg.mlivestreaming.provider.StoredUserSources
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.ui.mine.vip.MineVipActivity
import com.swbg.mlivestreaming.utils.LogUtils
import kotlinx.android.synthetic.main.custom_jzvd.view.*

class CustomJzvd : JzvdStd {
    var mContext: Context

    var currentClear = 0

    var urlLinks: List<String> = listOf()
        set(value) {
            field = value
            currentClear = 0
            chooseCurrentClear(1)
        }

    constructor(context: Context) : super(context) {
        mContext = context
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
    }

    override fun getLayoutId(): Int {
        //传入自定义布局
        return R.layout.custom_jzvd
    }

    override fun init(context: Context) {
        super.init(context)
        //拿到自己添加的控件 设置listener
        clear_one.singleClick {
//            changeUrl()
            goAllBtn(true)
            chooseCurrentClear(0)
        }
        clear_two.singleClick {
            goAllBtn(true)
            chooseCurrentClear(1)

        }
        clear_three.singleClick {
            goAllBtn(true)
            chooseCurrentClear(2)
        }
        iv_clear_btn.singleClick {
            goAllBtn(false)
        }
    }

    private fun chooseCurrentClear(i: Int) {
        if (i == 2 && !judgeIsVip()) {
            context.startActivity(MineVipActivity.open(context))
            return
        }
        //如果已经处于当前状态 不切换
        if (i == currentClear) return
        currentClear = i
        clear_one.setTextColor(if (i == 0) ContextCompat.getColor(context,
            R.color.white) else ContextCompat.getColor(context, R.color.clear_text))
        clear_two.setTextColor(if (i == 1) ContextCompat.getColor(context,
            R.color.white) else ContextCompat.getColor(context, R.color.clear_text))
        clear_three.setTextColor(if (i == 2) ContextCompat.getColor(context,
            R.color.white) else ContextCompat.getColor(context, R.color.clear_text))
        when (i) {
            0 -> {
                Glide.with(context).load(R.mipmap.video_clear_one).centerInside().into(iv_clear_btn)
            }
            1 -> {
                Glide.with(context).load(R.mipmap.video_clear_two).centerInside().into(iv_clear_btn)
            }
            2 -> {
                Glide.with(context).load(R.mipmap.video_clear_three).centerInside()
                    .into(iv_clear_btn)
            }
        }
        //切换播放
        when (urlLinks.size) {
            1 -> {
                setUp(urlLinks[0], "", Jzvd.SCREEN_NORMAL)
                startVideo()
            }
            2 -> {
                when (i) {
                    0, 1 -> {
                        setUp(urlLinks[0], "", Jzvd.SCREEN_NORMAL)
                    }
                    2 -> {
                        setUp(urlLinks[1], "", Jzvd.SCREEN_NORMAL)
                    }
                }
                startVideo()
            }
            3 -> {
                setUp(urlLinks[i], "", Jzvd.SCREEN_NORMAL)
                startVideo()
            }
        }
        LogUtils.i("==视频数据==", "i=$i, $urlLinks")

    }


    fun setUserPlayNum(it: VideoPlayBean) {
        it.limit?.mapIndexed { index, limitVideoClear ->
            when (index) {
                0 -> {
                    clear_one.text = limitVideoClear.title
                    tv_clear_one_num.text = "${limitVideoClear.play_times}"
                }
                1 -> {
                    clear_two.text = limitVideoClear.title
                    tv_clear_two_num.text = "${limitVideoClear.play_times}"
                }
                2 -> {
                    clear_three.text = limitVideoClear.title
                    tv_clear_three_num.text = "${limitVideoClear.play_times}"
                }
            }
        }
    }

    private fun judgeIsVip(): Boolean {
        return (StoredUserSources.getUserInfo().takeIf { it != null }
            ?: UserInfoBean()).user?.vip != "0"
    }


    private fun goAllBtn(b: Boolean) {
        cl_clear_bg.visibility = if (b) View.GONE else View.VISIBLE
    }

    override fun onClick(v: View) {
        super.onClick(v)
        //设置控件单击事件
    }

    fun setUserChoseClear() {

    }

    override fun setUp(jzDataSource: JZDataSource, screen: Int) {
        super.setUp(jzDataSource, screen)
        //这两行设置播放时屏幕状态
//        Jzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//        Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    override fun changeUrl(jzDataSource: JZDataSource?, seekToInAdvance: Long) {
        super.changeUrl(jzDataSource, seekToInAdvance)

    }

    override fun onStateAutoComplete() {
        super.onStateAutoComplete()
    }

    override fun gotoFullscreen() {
        super.gotoFullscreen()
    }

    override fun setScreenFullscreen() {
        super.setScreenFullscreen()
    }

    override fun clickFullscreen() {
        super.clickFullscreen()
    }

    fun setCurrentPlaySource(it: List<String>) {
        urlLinks = it
    }


    /*override fun onInfo(iMediaPlayer: IMediaPlayer?, what: Int, extra: Int): Boolean {
        JZMediaManager.instance().mainThreadHandler.post(Runnable {
            if (JzvdMgr.getCurrentJzvd() != null) {
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    JzvdMgr.getCurrentJzvd().onPrepared()
                } else {
                    if (what == IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED) {
                        // 这里返回了视频的旋转角度，根据角度旋转视频到正确角度
                        JZMediaManager.textureView.setRotation(extra)
                    } else {
                        JzvdMgr.getCurrentJzvd().onInfo(what, extra)
                    }
                }
            }
        })
        return false
    }*/

    //    @Override
    //    public void onAutoCompletion() {
    //        super.onAutoCompletion();
    //        //播放下一集 在这里切换url
    //    }
    //
    //    @Override
    //    public void startWindowFullscreenFocus(Jzvd jzvd) {
    //        super.startWindowFullscreenFocus(jzvd);
    //        //进入全屏时调用
    //    }
    //
    //    @Override
    //    public void playOnThisJzvd() {
    //        super.playOnThisJzvd();
    //        //退出全屏
    //        //Toast.makeText(mContext, "退出全屏", Toast.LENGTH_SHORT).show();
    //    }
}