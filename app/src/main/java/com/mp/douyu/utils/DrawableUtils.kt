package com.mp.douyu.utils

import android.widget.TextView
import androidx.annotation.DrawableRes
import com.mp.douyu.MApplication

class DrawableUtils {
    companion object {
        val DRAWABLE_START = 0
        val DRAWABLE_TOP = 1
        val DRAWABLE_END = 2
        val DRAWABLE_BOTTOM = 3
        fun setDrawable(@DrawableRes drawable: Int, w: Int? = 0, h: Int? = 0, place: Int? = DRAWABLE_START, textView: TextView) {
            MApplication.getApplicationInstances?.resources?.getDrawable(drawable)
                ?.let { drawable ->

                    drawable.setBounds(0, 0, if (w==0)drawable.minimumWidth else w!!, if (h==0)drawable.minimumHeight else h!!)
                    when (place) {
                        DRAWABLE_START -> {
                            textView.setCompoundDrawables(drawable, null, null, null)
                        }
                        DRAWABLE_TOP -> {
                            textView.setCompoundDrawables(null, drawable, null, null)
                        }
                        DRAWABLE_END -> {
                            textView.setCompoundDrawables(null, null, drawable, null)
                        }
                        DRAWABLE_BOTTOM -> {
                            textView.setCompoundDrawables(null, null, null, drawable)
                        }
                    }
                }
        }
        fun clearDrawable(textView: TextView?){
            textView?.let {it->
                it.setCompoundDrawables(null, null, null, null)
            }
        }
    }
}