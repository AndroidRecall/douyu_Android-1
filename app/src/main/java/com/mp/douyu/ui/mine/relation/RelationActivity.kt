package com.mp.douyu.ui.mine.relation

import android.content.Context
import android.content.Intent
import com.mp.douyu.R
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.inTransaction

class RelationActivity : MBaseActivity() {
    override val contentViewLayoutId: Int
        get() = R.layout.activity_user_sapce

    override fun initView() {
        supportFragmentManager.inTransaction {
            add(R.id.container,
                RelationFragment.newInstance(intent.getIntExtra(EXTRA_RELATION_TYPE,
                    RELATION_TYPE_FOLLOW), intent.getIntExtra(EXTRA_UID, 0)))
        }
    }

    companion object {
        const val EXTRA_RELATION_TYPE = "type"
        const val EXTRA_UID = "uid"
        const val RELATION_TYPE_FOLLOW = 0
        const val RELATION_TYPE_FANS = 1
        fun open(context: Context?, type: Int = RELATION_TYPE_FOLLOW, uid: Int? = 0): Intent {
            return Intent(context, RelationActivity::class.java).apply {
                putExtra(EXTRA_RELATION_TYPE, type)
                putExtra(EXTRA_UID, uid)
            }
        }
    }
}