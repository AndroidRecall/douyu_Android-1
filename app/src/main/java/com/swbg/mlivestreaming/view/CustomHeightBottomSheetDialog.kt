package com.swbg.mlivestreaming.view

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.swbg.mlivestreaming.R

class CustomHeightBottomSheetDialog : BottomSheetDialog {
    private var mPeekHeight = 0
    private var mMaxHeight = 0
    private var mCreated = false
    private var mWindow: Window? = null
    private var mBottomSheetBehavior: BottomSheetBehavior<*>? = null

    constructor(context: Context, peekHeight: Int, maxHeight: Int) : super(context) {
        init(peekHeight, maxHeight)
    }

    constructor(context: Context, theme: Int, peekHeight: Int, maxHeight: Int) : super(context,
        theme) {
        init(peekHeight, maxHeight)
    }

    constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?, peekHeight: Int, maxHeight: Int) : super(
        context,
        cancelable,
        cancelListener) {
        init(peekHeight, maxHeight)
    }

    private fun init(peekHeight: Int, maxHeight: Int) {
        mWindow = window
        mPeekHeight = peekHeight
        mMaxHeight = maxHeight
    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        mCreated = true
        setPeekHeight()
        setMaxHeight()
        setBottomSheetCallback()
    }

    fun setPeekHeight(peekHeight: Int) {
        mPeekHeight = peekHeight
        if (mCreated) {
            setPeekHeight()
        }
    }

    fun setMaxHeight(height: Int) {
        mMaxHeight = height
        if (mCreated) {
            setMaxHeight()
        }
    }

    fun setBatterSwipeDismiss(enabled: Boolean) {
        if (enabled) {
        }
    }

    private fun setPeekHeight() {
        if (mPeekHeight <= 0) {
            return
        }
        if (bottomSheetBehavior != null) {
            mBottomSheetBehavior!!.peekHeight = mPeekHeight
        }
    }

    private fun setMaxHeight() {
        if (mMaxHeight <= 0) {
            return
        }
        mWindow!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, mMaxHeight)
        mWindow!!.setGravity(Gravity.BOTTOM)
    }

    // setContentView() 没有调用
    private val bottomSheetBehavior: BottomSheetBehavior<*>?
        private get() {
            if (mBottomSheetBehavior != null) {
                return mBottomSheetBehavior
            }
            val view = mWindow!!.findViewById<View>(R.id.design_bottom_sheet) ?: return null
            // setContentView() 没有调用
            mBottomSheetBehavior = BottomSheetBehavior.from(view)
            return mBottomSheetBehavior
        }

    private fun setBottomSheetCallback() {
        if (bottomSheetBehavior != null) {
            mBottomSheetBehavior!!.setBottomSheetCallback(mBottomSheetCallback)
        }
    }

    private val mBottomSheetCallback: BottomSheetCallback = object : BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, @BottomSheetBehavior.State newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
                BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }
}