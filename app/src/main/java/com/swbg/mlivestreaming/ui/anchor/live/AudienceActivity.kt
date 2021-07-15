package com.swbg.mlivestreaming.ui.anchor.live

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.bean.EnterLiveRes
import com.swbg.mlivestreaming.bean.LiveBean
import com.swbg.mlivestreaming.inTransaction

class AudienceActivity : MBaseActivity() {
    var fragment: AudienceFragment? = null
    override val contentViewLayoutId: Int
        get() = R.layout.activity_user_sapce

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState)
    }
    override fun initView() {
        val datas = intent.getParcelableArrayListExtra<LiveBean>(EXTRA_LIVE_LIST_DATA)
        val enterLiveRes = intent.getParcelableExtra<EnterLiveRes>(EXTRA_LIVE_CUR_ROOM_DATA)
        val position = intent.getIntExtra(INITIAL_POSITION, 0)
        fragment = AudienceFragment.newInstance(datas,position,enterLiveRes)
        supportFragmentManager.inTransaction {
            add(R.id.container, fragment!!)
        }
    }

    companion object {
        const val EXTRA_LIVE_LIST_DATA = "live_list_data"
        const val EXTRA_LIVE_CUR_ROOM_DATA = "cur_room_data"
        const val INITIAL_POSITION = "position"
        fun open(context: Context?, data: MutableList<LiveBean>, position: Int = 0, curLiveInfo: EnterLiveRes?): Intent {
            return Intent(context, AudienceActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putParcelableArrayListExtra(EXTRA_LIVE_LIST_DATA, ArrayList(data))
                .putExtra(EXTRA_LIVE_CUR_ROOM_DATA,curLiveInfo)
                .putExtra(INITIAL_POSITION, position)


        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        fragment?.onVisibleFirst()
    }
}