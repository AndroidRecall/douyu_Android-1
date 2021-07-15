package com.swbg.mlivestreaming.ui.anchor.live

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.bean.LiveBean
import com.swbg.mlivestreaming.bean.LiveStreamingBean
import com.swbg.mlivestreaming.event.LiveEvent
import com.swbg.mlivestreaming.inTransaction
import com.swbg.mlivestreaming.utils.RxBus
import io.reactivex.Observable

class AnchorActivity : MBaseActivity() {
    var anchorLiveFragment: AnchorFragment? = null
    var anchorPrepareFragment: AnchorPrepareFragment? = null
    var observable: Observable<LiveEvent>? =null
    override val contentViewLayoutId: Int
        get() = R.layout.activity_user_sapce

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState)
    }

    override fun initView() {

        val data = intent.getParcelableArrayListExtra<LiveBean>(LIVE_DATA)
        val position = intent.getIntExtra(INITIAL_POSITION, 0)

        loadPrepareFragment()

        observable = RxBus.getInstance().register(LiveEvent::class.java)
        (observable as Observable<LiveEvent>).subscribe {
            it.liveStreamingBean?.apply {
            loadLiveFragment(this)
            }
        }
    }

    private fun loadPrepareFragment() {
        if (anchorPrepareFragment == null)
            anchorPrepareFragment = AnchorPrepareFragment.newInstance()
        loadFragment(anchorPrepareFragment!!)
    }

    private fun loadLiveFragment(liveStreamingBean: LiveStreamingBean) {
        if (anchorLiveFragment == null)
            anchorLiveFragment = AnchorFragment.newInstance(liveStreamingBean)
        loadFragment(anchorLiveFragment!!)
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.inTransaction {
            add(R.id.container, fragment)
        }
    }

    companion object {
        const val LIVE_DATA = "data"
        const val INITIAL_POSITION = "position"
        fun open(context: Context?): Intent {
            return Intent(context, AnchorActivity::class.java).apply {

            }
        }
    }

    private val liveViewModel by getViewModel(LiveViewModel::class.java) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            // 结果回调
            anchorPrepareFragment?.chooseCallback(data)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.getInstance().unregister(LiveEvent::class.java,observable!!)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false
        }
        return super.onKeyDown(keyCode, event)
    }
}