package com.mp.douyu.ui.guide

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.mp.douyu.MainActivity
import com.mp.douyu.R
import com.mp.douyu.base.MBaseFragment
import com.mp.douyu.singleClick
import kotlinx.android.synthetic.main.fragment_guide.*

class GuideFragment : MBaseFragment() {
    override val layoutId: Int
        get() = R.layout.fragment_guide

    override fun initView() {

        context?.let {
            Glide.with(it).load(index?.let { it1 -> imageId?.get(it1) }).into(iv_image)
        }
        if (imageId.size - 1 == index!!) {
            tv_btn.visibility = View.VISIBLE
        } else {
            tv_btn.visibility = View.GONE
        }
        iv_image.singleClick {
            context?.let {
                if (imageId.size - 1 == index!!) {
                    startActivityWithTransition(MainActivity.open(it))
                    finishView()
                }
            }
        }
    }

    val imageId by lazy {
        arguments?.getIntegerArrayList(IMAGE_ID) ?: arrayListOf()
    }

    val index by lazy {
        arguments?.getInt(TYPE, 0)
    }

    companion object {
        const val TYPE = "type"
        const val IMAGE_ID = "IMAGE_ID"
        fun newInstance(type: Int, images: ArrayList<Int>): GuideFragment {
            val fragment = GuideFragment()
            val bundle = Bundle()
            bundle.putInt(TYPE, type)
            bundle.putIntegerArrayList(IMAGE_ID, images)
            fragment.arguments = bundle
            return fragment
        }
    }

}
