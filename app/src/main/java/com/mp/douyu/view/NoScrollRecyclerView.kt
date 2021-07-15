package com.mp.douyu.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class NoScrollRecyclerView : RecyclerView {

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr)

    constructor(context: Context) : super(context)

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        return true
    }
}