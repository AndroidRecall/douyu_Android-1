package com.mp.douyu.view.shadow

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.mp.douyu.R

/**
 * A [Drawable] which wraps another drawable and
 * draws a shadow around it.
 */
class ShadowDrawableWrapper(context: Context, content: Drawable) : DrawableWrapper(content) {

    var shadowMultiplier = 1.5f

    var shadowTopScale = 1f
    var shadowHorizontalScale = 1f
    var shadowBottomScale = 1f

    private val mCornerShadowPaint: Paint
    private val mEdgeShadowPaint: Paint

    private val mContentBounds: RectF

    var cornerRadius: Float = 0.toFloat()
        set(radius1) {
            val radius = Math.round(radius1).toFloat()
            if (cornerRadius == radius) {
                return
            }
            field = radius
            mDirty = true
            invalidateSelf()
        }

    private var mCornerShadowPath: Path? = null

    // updated value with inset
    private var mMaxShadowSize: Float = 0.toFloat()
    // actual value set by developer
    private var mRawMaxShadowSize: Float = 0.toFloat()

    // multiplied value to account for shadow offset
    private var mShadowSize: Float = 0.toFloat()
    // actual value set by developer
    private var mRawShadowSize: Float = 0.toFloat()

    private var mDirty = true

    var mShadowStartColor: Int
    var mShadowMiddleColor: Int
    var mShadowEndColor: Int
    private var orientation = BooleanArray(4)
    private var mAddPaddingForCorners = true

    private var mRotation: Float = 0.toFloat()

    /**
     * If shadow size is set to a value above max shadow, we print a warning
     */
    private var mPrintedShadowClipWarning = false

    var shadowSize: Float
        get() = mRawShadowSize
        set(size) = setShadowSize(size, mRawMaxShadowSize)

    var maxShadowSize: Float
        get() = mRawMaxShadowSize
        set(size) = setShadowSize(mRawShadowSize, size)

    val minWidth: Float
        get() {
            val content = 2 * Math.max(mRawMaxShadowSize, cornerRadius + mRawMaxShadowSize / 2)
            return content + mRawMaxShadowSize * 2
        }

    val minHeight: Float
        get() {
            val content = 2 * Math.max(mRawMaxShadowSize, cornerRadius + mRawMaxShadowSize * shadowMultiplier / 2)
            return content + mRawMaxShadowSize * shadowMultiplier * 2
        }

    init {

        mShadowStartColor = ContextCompat.getColor(context, R.color.shadow_start_color)
        mShadowMiddleColor = ContextCompat.getColor(context, R.color.shadow_mid_color)
        mShadowEndColor = ContextCompat.getColor(context, R.color.shadow_end_color)

        mCornerShadowPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        mCornerShadowPaint.style = Paint.Style.FILL
        mContentBounds = RectF()
        mEdgeShadowPaint = Paint(mCornerShadowPaint)
        mEdgeShadowPaint.isAntiAlias = false
        setShadowSize(0f, 0f)
    }

    fun setAddPaddingForCorners(addPaddingForCorners: Boolean) {
        mAddPaddingForCorners = addPaddingForCorners
        invalidateSelf()
    }


    override fun setAlpha(alpha: Int) {
        super.setAlpha(alpha)
        mCornerShadowPaint.alpha = alpha
        mEdgeShadowPaint.alpha = alpha
    }

    override fun onBoundsChange(bounds: Rect) {
        mDirty = true
    }

    override fun onStateChange(state: IntArray): Boolean {
        val b = super.onStateChange(state)
        if (b) {
            invalidateSelf()
        }
        return b
    }

    fun setShadowSize(shadowSize: Float, maxShadowSize: Float) {
        var shadow = shadowSize
        var maxShadow = maxShadowSize
        if (shadow < 0 || maxShadow < 0) {
            throw IllegalArgumentException("invalid shadow size")
        }
        shadow = toEven(shadow).toFloat()
        maxShadow = toEven(maxShadow).toFloat()
        if (shadow > maxShadow) {
            shadow = maxShadow
            if (!mPrintedShadowClipWarning) {
                mPrintedShadowClipWarning = true
            }
        }
        if (mRawShadowSize == shadow && mRawMaxShadowSize == maxShadow) {
            return
        }
        mRawShadowSize = shadow
        mRawMaxShadowSize = maxShadow
        mShadowSize = Math.round(shadow * shadowMultiplier).toFloat()
        mMaxShadowSize = maxShadow
        mDirty = true
        invalidateSelf()
    }

    override fun getPadding(padding: Rect): Boolean {
        val vOffset = Math.ceil(calculateVerticalPadding(mRawMaxShadowSize, cornerRadius, mAddPaddingForCorners).toDouble()).toInt()
        val hOffset = Math.ceil(calculateHorizontalPadding(mRawMaxShadowSize, cornerRadius, mAddPaddingForCorners).toDouble()).toInt()
        padding.set(hOffset, vOffset, hOffset, vOffset)
        return true
    }

    private fun calculateVerticalPadding(maxShadowSize: Float, cornerRadius: Float, addPaddingForCorners: Boolean): Float {
        return if (addPaddingForCorners) {
            (maxShadowSize * shadowMultiplier + (1 - COS_45) * cornerRadius).toFloat()
        } else {
            maxShadowSize * shadowMultiplier
        }
    }

    private fun calculateHorizontalPadding(maxShadowSize: Float, cornerRadius: Float, addPaddingForCorners: Boolean): Float {
        return if (addPaddingForCorners) {
            (maxShadowSize + (1 - COS_45) * cornerRadius).toFloat()
        } else {
            maxShadowSize
        }
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun draw(canvas: Canvas) {
        if (mDirty) {
            buildComponents(bounds)
            mDirty = false
        }
        drawShadow(canvas)

        super.draw(canvas)
    }

    internal fun setRotation(rotation: Float) {
        if (mRotation != rotation) {
            mRotation = rotation
            invalidateSelf()
        }
    }

    private fun drawShadow(canvas: Canvas) {
        val rotateSaved = canvas.save()
        canvas.rotate(mRotation, mContentBounds.centerX(), mContentBounds.centerY())

        val edgeShadowTop = -cornerRadius - mShadowSize
        val shadowOffset = cornerRadius
        val drawHorizontalEdges = mContentBounds.width() - 2 * shadowOffset > 0
        val drawVerticalEdges = mContentBounds.height() - 2 * shadowOffset > 0

        val shadowOffsetTop = mRawShadowSize - mRawShadowSize * shadowTopScale
        val shadowOffsetHorizontal = mRawShadowSize - mRawShadowSize * shadowHorizontalScale
        val shadowOffsetBottom = mRawShadowSize - mRawShadowSize * shadowBottomScale

        val shadowScaleHorizontal = shadowOffset / (shadowOffset + shadowOffsetHorizontal)
        val shadowScaleTop = shadowOffset / (shadowOffset + shadowOffsetTop)
        val shadowScaleBottom = shadowOffset / (shadowOffset + shadowOffsetBottom)

        // LT
        var saved = canvas.save()
        canvas.translate(mContentBounds.left + shadowOffset, mContentBounds.top + shadowOffset)
        canvas.scale(shadowScaleHorizontal, shadowScaleTop)
        canvas.drawPath(mCornerShadowPath!!, mCornerShadowPaint)
        if (drawHorizontalEdges) {
            // TE
            canvas.scale(1f / shadowScaleHorizontal, 1f)
            canvas.drawRect(0f, edgeShadowTop, mContentBounds.width() - 2 * shadowOffset, -cornerRadius, mEdgeShadowPaint)
        }
        canvas.restoreToCount(saved)
        // RB
        saved = canvas.save()
        canvas.translate(mContentBounds.right - shadowOffset, mContentBounds.bottom - shadowOffset)
        canvas.scale(shadowScaleHorizontal, shadowScaleBottom)
        canvas.rotate(180f)
        canvas.drawPath(mCornerShadowPath!!, mCornerShadowPaint)
        if (drawHorizontalEdges) {
            // BE
            canvas.scale(1f / shadowScaleHorizontal, 1f)
            canvas.drawRect(0f, edgeShadowTop, mContentBounds.width() - 2 * shadowOffset, -cornerRadius + mShadowSize, mEdgeShadowPaint)
        }
        canvas.restoreToCount(saved)
        // LB
        saved = canvas.save()
        canvas.translate(mContentBounds.left + shadowOffset, mContentBounds.bottom - shadowOffset)
        canvas.scale(shadowScaleHorizontal, shadowScaleBottom)
        canvas.rotate(270f)
        canvas.drawPath(mCornerShadowPath!!, mCornerShadowPaint)
        if (drawVerticalEdges) {
            // LE
            canvas.scale(1f / shadowScaleBottom, 1f)
            canvas.drawRect(0f, edgeShadowTop, mContentBounds.height() - 2 * shadowOffset, -cornerRadius, mEdgeShadowPaint)
        }
        canvas.restoreToCount(saved)
        // RT
        saved = canvas.save()
        canvas.translate(mContentBounds.right - shadowOffset, mContentBounds.top + shadowOffset)
        canvas.scale(shadowScaleHorizontal, shadowScaleTop)
        canvas.rotate(90f)
        canvas.drawPath(mCornerShadowPath!!, mCornerShadowPaint)
        if (drawVerticalEdges) {
            // RE
            canvas.scale(1f / shadowScaleTop, 1f)
            canvas.drawRect(0f, edgeShadowTop, mContentBounds.height() - 2 * shadowOffset, -cornerRadius, mEdgeShadowPaint)
        }
        canvas.restoreToCount(saved)

        canvas.restoreToCount(rotateSaved)
    }

    private fun buildShadowCorners() {
        val innerBounds = RectF(-cornerRadius, -cornerRadius, cornerRadius, cornerRadius)
        val outerBounds = RectF(innerBounds)
        outerBounds.inset(-mShadowSize, -mShadowSize)

        if (mCornerShadowPath == null) {
            mCornerShadowPath = Path()
        } else {
            mCornerShadowPath!!.reset()
        }
        mCornerShadowPath!!.fillType = Path.FillType.EVEN_ODD
        mCornerShadowPath!!.moveTo(-cornerRadius, 0f)
        mCornerShadowPath!!.rLineTo(-mShadowSize, 0f)
        // outer arc
        mCornerShadowPath!!.arcTo(outerBounds, 180f, 90f, false)
        // inner arc
        mCornerShadowPath!!.arcTo(innerBounds, 270f, -90f, false)
        mCornerShadowPath!!.close()

        val shadowRadius = -outerBounds.top
        if (shadowRadius > 0f) {
            val startRatio = cornerRadius / shadowRadius
            val midRatio = startRatio + (1f - startRatio) / 2f
            mCornerShadowPaint.shader = RadialGradient(0f, 0f, shadowRadius, intArrayOf(0, mShadowStartColor, mShadowMiddleColor, mShadowEndColor), floatArrayOf(0f, startRatio, midRatio, 1f), Shader.TileMode.CLAMP)
        }

        // we offset the content shadowSize/2 pixels up to make it more realistic.
        // this is why edge shadow shader has some extra space
        // When drawing bottom edge shadow, we use that extra space.
        mEdgeShadowPaint.shader = LinearGradient(0f, innerBounds.top, 0f, outerBounds.top, intArrayOf(mShadowStartColor, mShadowMiddleColor, mShadowEndColor), floatArrayOf(0f, .5f, 1f), Shader.TileMode.CLAMP)
        mEdgeShadowPaint.isAntiAlias = false
    }

    private fun buildComponents(bounds: Rect) {
        // Card is offset shadowMultiplier * maxShadowSize to account for the shadow shift.
        // We could have different top-bottom offsets to avoid extra gap above but in that case
        // center aligning Views inside the CardView would be problematic.
        val verticalOffset = mRawMaxShadowSize * shadowMultiplier
        val left = bounds.left + if (orientation[0]) mRawMaxShadowSize else 0f
        val top = bounds.top + if (orientation[1]) verticalOffset else 0f
        val right = bounds.right - if (orientation[2]) mRawMaxShadowSize else 0f
        val bottom = bounds.bottom - if (orientation[3]) verticalOffset else 0f
        mContentBounds.set(left, top, right, bottom)
        wrappedDrawable.setBounds(mContentBounds.left.toInt(), mContentBounds.top.toInt(), mContentBounds.right.toInt(), mContentBounds.bottom.toInt())

        buildShadowCorners()
    }

    fun setOrientation(vararg orientation: Boolean) {
        this.orientation = orientation
    }

    companion object {
        // used to calculate content padding
        private val COS_45 = Math.cos(Math.toRadians(45.0))

        /**
         * Casts the value to an even integer.
         */
        private fun toEven(value: Float): Int {
            val i = Math.round(value)
            return if (i % 2 == 1) i - 1 else i
        }
    }
}
