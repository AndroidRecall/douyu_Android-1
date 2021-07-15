package com.mp.douyu.ui.mine.dynamic

import android.os.Bundle
import com.mp.douyu.R
import com.mp.douyu.base.VisibilityFragment
import com.mp.douyu.inTransaction
import kotlinx.android.synthetic.main.mine_fragment_av.*
import kotlinx.android.synthetic.main.mine_fragment_dynamic_home.tv_collect
import kotlinx.android.synthetic.main.mine_fragment_dynamic_home.tv_record

class MineAVFragment(var uid: Int = 0) : VisibilityFragment() {

    companion object{
        const val EXTRA_UID = "uid"
        fun newInstance(uid: Int = 0): MineAVFragment {
            val fragment = MineAVFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_UID, uid)
            fragment.arguments = bundle
            return fragment
        }
    }
    override val layoutId: Int
        get() = R.layout.mine_fragment_av

    override fun initView() {

    }

    override fun onVisible() {
        super.onVisible()
    }

    override fun onInvisible() {
        super.onInvisible()
    }

    override fun onVisibleFirst() {
        uid =arguments?.getInt(EXTRA_UID)!!
        tv_comment.setOnClickListener {
            selComment()
        }
        tv_collect.setOnClickListener {
            selCollect()

        }
        tv_record.setOnClickListener {
            selRecord()

        }

        selComment()
    }

    private fun selRecord() {
        tv_comment.setTextColor(resources.getColor(R.color.colorNotChoice))
        tv_collect.setTextColor(resources.getColor(R.color.colorNotChoice))
        tv_record.setTextColor(resources.getColor(R.color.colorBtnBlue))
        childFragmentManager.inTransaction {
            add(R.id.container, MineAVDetailFragment.newInstance(MineAVDetailFragment.TYPE_AV_RECORD,uid))
        }
    }

    private fun selCollect() {
        tv_comment.setTextColor(resources.getColor(R.color.colorNotChoice))
        tv_collect.setTextColor(resources.getColor(R.color.colorBtnBlue))
        tv_record.setTextColor(resources.getColor(R.color.colorNotChoice))
        childFragmentManager.inTransaction {
            add(R.id.container, MineAVDetailFragment.newInstance(MineAVDetailFragment.TYPE_AV_COLLECT,uid))
        }
    }


    private fun selComment() {
        tv_comment.setTextColor(resources.getColor(R.color.colorBtnBlue))
        tv_collect.setTextColor(resources.getColor(R.color.colorNotChoice))
        tv_record.setTextColor(resources.getColor(R.color.colorNotChoice))
        childFragmentManager.inTransaction {
            add(R.id.container, MineAVDetailFragment.newInstance(MineAVDetailFragment.TYPE_AV_COMMENT,uid))
        }
    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
    }
}