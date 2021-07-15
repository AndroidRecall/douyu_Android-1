package com.swbg.mlivestreaming.ui.mine.dynamic

import android.os.Bundle
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.VisibilityFragment
import com.swbg.mlivestreaming.inTransaction
import kotlinx.android.synthetic.main.mine_fragment_av.*
import kotlinx.android.synthetic.main.mine_fragment_dynamic_home.tv_collect
import kotlinx.android.synthetic.main.mine_fragment_dynamic_home.tv_record

class MinePlanFragment(var uid :Int =0) : VisibilityFragment() {

    companion object{
        const val EXTRA_UID = "uid"
        fun newInstance(uid: Int = 0): MinePlanFragment {
            val fragment = MinePlanFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_UID, uid)
            fragment.arguments = bundle
            return fragment
        }
    }
    override val layoutId: Int
        get() = R.layout.mine_fragment_plan

    override fun initView() {

    }

    override fun onVisible() {
        super.onVisible()
    }

    override fun onInvisible() {
        super.onInvisible()
    }

    override fun onVisibleFirst() {
        uid = arguments?.getInt(EXTRA_UID)!!
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
            add(R.id.container, MineAVDetailFragment.newInstance(MineAVDetailFragment.TYPE_AV_RECORD))
        }
    }

    private fun selCollect() {
        tv_comment.setTextColor(resources.getColor(R.color.colorNotChoice))
        tv_collect.setTextColor(resources.getColor(R.color.colorBtnBlue))
        tv_record.setTextColor(resources.getColor(R.color.colorNotChoice))
        childFragmentManager.inTransaction {
            add(R.id.container, MineAVDetailFragment.newInstance(MineAVDetailFragment.TYPE_AV_COLLECT))
        }
    }


    private fun selComment() {
        tv_comment.setTextColor(resources.getColor(R.color.colorBtnBlue))
        tv_collect.setTextColor(resources.getColor(R.color.colorNotChoice))
        tv_record.setTextColor(resources.getColor(R.color.colorNotChoice))
        childFragmentManager.inTransaction {
            add(R.id.container, MineAVDetailFragment.newInstance(MineAVDetailFragment.TYPE_AV_COMMENT))
        }
    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
    }
}