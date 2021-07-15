package com.swbg.mlivestreaming.view

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import androidx.core.content.ContextCompat
import com.swbg.mlivestreaming.R
import android.graphics.LinearGradient


class WebViewProgressBar : View {
    /**
     * 进度条的宽度
     */
    private var view_width = 0

    /**
     * 画布的宽度
     */
    private var view_base_width = 0

    /**
     * 控件的宽度
     */
    private var view_edge_width = 0

    /**
     * 进度
     */
    private var progress = 0
    private var cacheCanvas: Canvas? = null

    /**
     * 背景颜色的画笔
     */
    private var backgroundPaint: Paint? = null

    /**
     * 进度条的画笔
     */
    private var progressPaint: Paint? = null

    /**
     * 进度末端的图
     */
    private var bitmap: Bitmap? = null
    private var bitmapWidth = 0
    private var bitmapHeight = 0

    //渐变色开始
    private var startColor = 0

    //渐变色结束
    private var endColor = 0

    /**
     * 缓存图片
     */
    private var cacheBitmap: Bitmap? = null
    var bmpPaint = Paint()

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context) {
        startColor = ContextCompat.getColor(context, R.color.colorAccent)
        endColor = ContextCompat.getColor(context, R.color.colorAccent)
        bitmap =
            BitmapFactory.decodeResource(context.resources, R.mipmap.icon_web_view_progress_bar)
        bitmapWidth = bitmap?.width!!
        bitmapHeight = bitmap!!.height
        backgroundPaint = Paint()
        backgroundPaint!!.strokeWidth = bitmapWidth.toFloat()
        backgroundPaint!!.color = ContextCompat.getColor(context, R.color.colorWindowBackground)
        backgroundPaint!!.isDither = true
        backgroundPaint!!.isAntiAlias = true
        progressPaint = Paint()
        progressPaint!!.strokeWidth = bitmapWidth.toFloat()
        progressPaint!!.isDither = true
        progressPaint!!.isAntiAlias = true
        val d = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(d)
        view_base_width = d.widthPixels
    }

    fun setProgress(progress: Int) {
        this.progress = progress
        view_width = if (view_width == 0) { //第一上来
            view_base_width * progress / 100
        } else {
            view_edge_width * progress / 100
        }
        if (cacheBitmap != null) {
            if (!cacheBitmap!!.isRecycled) {
                cacheBitmap!!.recycle()
                cacheBitmap = null
            }
            cacheCanvas = null
        }
        cacheBitmap = Bitmap.createBitmap(view_base_width, bitmapHeight, Bitmap.Config.ARGB_8888)
        if (cacheCanvas == null) {
            cacheCanvas = Canvas()
            cacheCanvas!!.setBitmap(cacheBitmap)
        }
        /**
         * 画背景
         */
        val r = RectF()
        r.left = 0f
        r.top = 0f
        r.right = view_base_width.toFloat()
        r.bottom = bitmapHeight.toFloat()
        cacheCanvas!!.drawRoundRect(r, 5f, 5f, backgroundPaint!!)
        if (progress > 0) {
            val lg = LinearGradient(0f,
                0f,
                view_width.toFloat(),
                bitmapWidth.toFloat(),
                startColor,
                endColor,
                Shader.TileMode.CLAMP)
            progressPaint!!.shader = lg
            r.left = 0f
            r.top = 0f
            r.right = view_width.toFloat()
            r.bottom = bitmapHeight.toFloat()
            cacheCanvas!!.drawRoundRect(r, 5f, 5f, progressPaint!!)
            cacheCanvas!!.drawBitmap(bitmap!!, view_width - bitmapWidth.toFloat(), 0f, bmpPaint)
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

//将cacheBitmap绘制到该View组件
        if (cacheBitmap != null) {
            canvas.drawBitmap(cacheBitmap!!, 0f, 0f, bmpPaint)
        }
        view_edge_width = this.width
        setProgress(progress)
    }
}