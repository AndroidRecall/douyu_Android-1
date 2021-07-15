package com.mp.douyu

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.bean.GetGameLinkBean
import com.mp.douyu.bean.TokenBean
import com.mp.douyu.provider.SessionProvider
import com.mp.douyu.provider.TokenProvider
import com.mp.douyu.ui.home.HomeViewModel
import com.mp.douyu.ui.login_register.LoginActivity
import com.mp.douyu.ui.mine.walnut.ChargeCenterActivity
import com.mp.douyu.utils.*
import com.mp.douyu.view.popupwindow.FastTransferDialog
import com.mp.douyu.view.shadow.ShadowDrawableWrapper
import kotlinx.android.synthetic.main.global_loading_viewholder.view.*
import kotlinx.android.synthetic.main.global_network_error_viewholder.view.*

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}

fun View.closeInputMethod() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
        windowToken,
        0)
}

fun loadGlobleVideo(context: Context, videoUrl: String?, imageView: ImageView) {
    Glide.with(context).load(videoUrl).error(R.drawable.drawable_video_error_bg)
        .placeholder(R.drawable.drawable_video_error_bg).into(imageView)
}

/**
 * 初始化recycler view 宽度
 */
fun initializeWidth(list: List<Any>, recyclerView: RecyclerView, width: Int = 40, displayNum: Int = 3) {
    val lp: ViewGroup.LayoutParams = recyclerView.layoutParams
    if (list.size > 3) {
        lp.width = Utils().dp2px(MApplication.getApplicationInstances, width * displayNum).toInt()
    } else {
        lp.width = Utils().dp2px(MApplication.getApplicationInstances, width * list.size).toInt()
    }
    recyclerView.layoutParams = lp
}

/**
 * 初始化recycler view 高度
 */
fun initializeHeight(list: List<Any>, recyclerView: RecyclerView, height: Int = 40, displayNum: Int = 3) {
    val lp: ViewGroup.LayoutParams = recyclerView.layoutParams
    if (list.size > 3) {
        lp.height = Utils().dp2px(MApplication.getApplicationInstances, height * displayNum).toInt()
    } else {
        lp.height = Utils().dp2px(MApplication.getApplicationInstances, height * list.size).toInt()
    }
    recyclerView.layoutParams = lp
}
/**
 * RecyclerView 移动到当前位置，
 *
 * @param manager  设置RecyclerView对应的manager
 * @param n  要跳转的位置
 */
fun RecyclerView.moveToPosition(manager: LinearLayoutManager, n: Int) {
    manager.scrollToPositionWithOffset(n, 0)
    manager.stackFromEnd = true
}
fun TokenBean.cacheSession() {
    TokenProvider.get().apply {
        update(TokenProvider.FIELD_ACCESS_TOKEN, this@cacheSession.accessToken)
        this@cacheSession.userName?.let { update(TokenProvider.FIELD_CLIENT_USER_NAME, it) }
        this@cacheSession.userPsw?.let { update(TokenProvider.FIELD_REFRESH_USER_PSW, it) }
        this@cacheSession.userSig?.let { update(TokenProvider.FIELD_IM_USER_SIG, it) }
        this@cacheSession.userID?.let { update(TokenProvider.FIELD_IM_USER_ID, it) }
    }

    SessionProvider.get().apply {
        update(SessionProvider.FIELD_IS_SUPPER, userInfo?.isSupper ?: false)
        update(SessionProvider.FIELD_IS_TIME_ADMIN, userInfo?.isTimingAdmin ?: false)
        update(SessionProvider.FIELD_ID, userInfo?.id.orEmpty())
        update(SessionProvider.FIELD_AVATAR, userInfo?.avatar.orEmpty())
        update(SessionProvider.FIELD_NAME, userInfo?.name.orEmpty())
        update(SessionProvider.FIELD_PHONE, userInfo?.phone.orEmpty())
        update(SessionProvider.FIELD_LAW_FIRM, userInfo?.law_firm_id.orEmpty())
        update(SessionProvider.FIELD_ORGANIZATION, userInfo?.organizationName.orEmpty())
        update(SessionProvider.FIELD_ORGANIZATION_ID, userInfo?.org_id.orEmpty())
        update(SessionProvider.FIELD_VISITOR, userInfo?.isVisitor() ?: true)
        update(SessionProvider.FIELD_COLOR_FLAG, userInfo?.colorFlag ?: 6)
    }
}


class LoadingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (container == null) {
            return null
        }
        return GlobeStatusViewHolder.globalLoadingHolder(container).itemView
    }

    fun show() {
        view?.post {
            if (isHidden) {
                fragmentManager?.beginTransaction()?.show(this)?.commitAllowingStateLoss()
            }
        }
    }

    fun dismiss() {
        view?.post {
            fragmentManager?.beginTransaction()?.hide(this)?.commitAllowingStateLoss()
        }
    }
}


fun View.singleClick(onClick: (View) -> Unit) {
    var enable = true
    setOnClickListener {
        if (enable) {
            enable = false
            try {
                onClick.invoke(it)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            LogUtils.i("singleClick", "Clicks are too frequent in 2s")
        }
        it.postDelayed({ enable = true }, 1000)
    }
}


/**
 * 手势识别帮助类
 */
class GestureDetectorHolder(val activity: Activity) {

    var excludeViews = hashSetOf<View?>()

    private val detector: GestureDetector by lazy {

        var isIntercept = false

        object : GestureDetector(activity, object : GestureDetector.SimpleOnGestureListener() {
            val outRect = Rect()

            override fun onDown(e: MotionEvent): Boolean {

                return activity.currentFocus is EditText && excludeViews.apply {
                    add(activity.currentFocus)
                }.filter { it != null }.all { view ->
                    outRect.apply {
                        view?.getGlobalVisibleRect(this)
                    }.let { !it.contains(e.x.toInt(), e.y.toInt()) }
                }.also {
                    isIntercept = it
                }
            }

            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return isIntercept.apply {
                    (activity.currentFocus as? EditText)?.takeIf { this }?.apply {
                        closeInputMethodAndClearFocus()
                    }
                }
            }

            override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
                if (isIntercept && e1 != null) {
                    isIntercept = false
                    e2.action = MotionEvent.ACTION_DOWN
                    e2.setLocation(e1.rawX, e1.rawY)
                }
                return false
            }
        }) {
            override fun onTouchEvent(ev: MotionEvent): Boolean {
                return activity.currentFocus.let { if (it != null) WindowUtils.isInputVisible(it) else false } and super.onTouchEvent(
                    ev)
            }
        }.also {
            it.setIsLongpressEnabled(false)
        }
    }

    fun dispatchMotionEvent(event: MotionEvent, vararg views: View?): Boolean {
        return !detector.onTouchEvent(event) || Rect().let { rect ->
            views.filterNotNull().any {
                rect.setEmpty()
                it.getGlobalVisibleRect(rect)
                rect.contains(event.x.toInt(), event.y.toInt())
            }
        }
    }
}


fun EditText.closeInputMethodAndClearFocus() {
    closeInputMethod().apply {
        isCursorVisible = false
        isFocusable = false
        isFocusableInTouchMode = false
        clearFocus()
    }
}

fun View.openInputMethod(force: Boolean = false) {
    if (force || !isFocused) {
        isFocusable = true
        isFocusableInTouchMode = true
        requestFocus()
        (this as? EditText)?.isCursorVisible = true
        postDelayed({
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
                this@openInputMethod,
                InputMethodManager.SHOW_FORCED)
        }, 100)
    }
}


//适配水滴，刘海屏等
fun matchWaterDropScreen(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val lp = activity.window.attributes
        lp.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        activity.window.attributes = lp
    }
}

//activity沉浸式设置
fun setActivityImmersion(activity: Activity) {
    val decorView = activity.window.decorView
    val option = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
    decorView.systemUiVisibility = option
    activity.window.statusBarColor = Color.TRANSPARENT
}


object GlobeStatusViewHolder {

    const val STATUS_EMPTY = -2
    const val STATUS_LOADING = -3
    const val STATUS_SINGLE_ACTION = -4
    const val STATUS_NETWORK_ERROR = -5
    const val STATUS_OTHER = -6

    fun globalEmptyNotInCentreHolder(parent: ViewGroup): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.global_empty_no_center_viewholder, parent, false))
    }

    fun globalEmptyHolder(parent: ViewGroup): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.global_empty_viewholder, parent, false))
    }

    fun globalNetworkError(parent: ViewGroup): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.global_network_error_viewholder, parent, false).apply {
                txtErrorMessage.text = "网络异常，请检查网络"
            })
    }

    fun globalLoadingHolder(parent: ViewGroup): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.global_loading_viewholder, parent, false).apply {
                globalLoading.apply {
                    addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                        override fun onViewDetachedFromWindow(v: View) {
                            (globalLoading.drawable as? AnimationDrawable)?.stop()
                        }

                        override fun onViewAttachedToWindow(v: View) {
                            (globalLoading.drawable as? AnimationDrawable)?.start()
                        }

                    })
//                Glide.with(context).load(R.mipmap.loading).into(this)
                }
            })
    }

    fun isNotNeedLogin(activity: MBaseActivity): Boolean {
        return if (!TokenProvider.get().hasToken()) {
            activity.startActivityWithTransition(LoginActivity.open(activity))
            false
        } else {
            true
        }
    }

    fun globalSingleActionHolder(parent: ViewGroup): CacheViewHolder {
        val context = parent.context
        return CacheViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.global_single_action_viewholder, parent, false).apply {
                background = ShadowDrawableHelper.allRadiusDrawable(context, background)
            })
    }

    fun globalSingleActionHolder2(parent: ViewGroup): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.global_single_action2_viewholder, parent, false))
    }

}


fun RecyclerView.addOnItemClickListener(runnable: RecyclerView.(RecyclerView.ViewHolder) -> Unit): RecyclerView.OnItemTouchListener {
    return object : RecyclerView.OnItemTouchListener {
        val listener = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return findChildViewUnder(e.x, e.y)?.apply {
                    runnable.invoke(this.parent as RecyclerView, getChildViewHolder(this))
                } != null
            }
        })

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
            listener.onTouchEvent(e)
        }

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            return false.apply {
                listener.onTouchEvent(e)
            }
        }
    }.apply {
        addOnItemTouchListener(this)
    }
}

fun jumpIsToGamePageDialog(it: GetGameLinkBean, baseActivity: MBaseActivity) {
    if (it.alert == null) {
        ActivityUtils.jumpToWebView(it.url, baseActivity, true, true)
    } else {
        FastTransferDialog.newInstance(it.alert.title, it.alert.balance, it.alert.game_balance)
            .apply {
                setOnClickListener(DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        R.id.tv_immediately_charge -> {
                            baseActivity.startActivityWithTransition(ChargeCenterActivity.open(
                                baseActivity))
                        }
                        R.id.tv_confirm -> {
//                        callback.invoke(which,currentChooseItem)
//                        baseActivity.startActivityWithTransition(TransferAccountActivity.open(baseActivity,
//                            currentChooseItem))
                            transferAccount(baseActivity, currentChooseItem, it.url, this)
                        }
                        R.id.tv_join_game -> {
                            it.url?.let {
                                ActivityUtils.jumpToWebView(it, baseActivity, true, true)
                            }
                            dismiss()
                        }

                    }
                }).show(baseActivity.supportFragmentManager, null)
            }
    }
}

fun transferAccount(baseActivity: MBaseActivity, transferNum: String?, gameUrl: String?, fastTransferDialog: FastTransferDialog?) {
    val homeViewModel by baseActivity.getViewModel(HomeViewModel::class.java) {
        _getTransferData.observe(it, Observer {
            it?.let {
                ToastUtils.showToast("转账成功", true)
                gameUrl?.let {
                    ActivityUtils.jumpToWebView(it, baseActivity, true, true)
                    fastTransferDialog?.dismiss()
                }
            }
        })
    }

    homeViewModel.beginTransfer(hashMapOf("amount" to transferNum))
}


object ShadowDrawableHelper {

    fun buttonRadiusDrawable(context: Context, background: Drawable): ShadowDrawableWrapper {
        return ShadowDrawableWrapper(context, background).apply {
            val resources = context.resources
            shadowMultiplier = 1f
            setOrientation(true, true, true, true)
            mShadowStartColor = Color.parseColor("#ffb9d0ff")
            mShadowMiddleColor = Color.parseColor("#80b9d0ff")
            mShadowEndColor = Color.parseColor("#00b9d0ff")
            cornerRadius = WindowUtils.dip2Px(24f).toFloat()
            setShadowSize(resources.getDimension(R.dimen.item_card_elevation),
                resources.getDimension(R.dimen.max_item_card_elevation))
        }
    }

    fun allRadiusDrawable(context: Context, background: Drawable): ShadowDrawableWrapper {
        return ShadowDrawableWrapper(context, background).apply {
            val resources = context.resources
            setOrientation(true, true, true, true)
            cornerRadius = resources.getDimension(R.dimen.item_card_radius)
            setShadowSize(resources.getDimension(R.dimen.item_card_elevation),
                resources.getDimension(R.dimen.max_item_card_elevation))
        }
    }

    fun allRadiusLightDrawable(context: Context, background: Drawable): ShadowDrawableWrapper {
        return ShadowDrawableWrapper(context, background).apply {
            val resources = context.resources
            setOrientation(true, true, true, true)
            mShadowStartColor = Color.parseColor("#fff2f3f8")
            mShadowMiddleColor = Color.parseColor("#80f2f3f8")
            mShadowEndColor = Color.parseColor("#00f2f3f8")
            cornerRadius = resources.getDimension(R.dimen.item_card_radius)
            setShadowSize(resources.getDimension(R.dimen.mid_item_card_elevation),
                resources.getDimension(R.dimen.max_item_card_elevation))
        }
    }

    fun allRadiusByNoEdgesDrawable(context: Context, background: Drawable): ShadowDrawableWrapper {
        return ShadowDrawableWrapper(context, background).apply {
            val resources = context.resources
            shadowMultiplier = 1f
            setOrientation(true, true, true, true)
            cornerRadius = resources.getDimension(R.dimen.item_card_radius)
            setShadowSize(resources.getDimension(R.dimen.item_card_elevation),
                resources.getDimension(R.dimen.max_item_card_elevation))
            setAddPaddingForCorners(false)
        }
    }

    fun allNoRadiusByNoEdgesDrawable(context: Context, background: Drawable): ShadowDrawableWrapper {
        return ShadowDrawableWrapper(context, background).apply {
            val resources = context.resources
            shadowMultiplier = 1f
            setOrientation(false, false, false, false)
            cornerRadius = resources.getDimension(R.dimen.no_elevation)
            setShadowSize(resources.getDimension(R.dimen.no_elevation),
                resources.getDimension(R.dimen.no_elevation))
            setAddPaddingForCorners(false)
        }
    }

    fun topRadiusDrawable(context: Context, background: Drawable): Drawable {
        return ShadowDrawableWrapper(context, background).apply {
            val resources = context.resources
            setOrientation(true, true, true, false)
            cornerRadius = resources.getDimension(R.dimen.item_card_radius)
            setShadowSize(resources.getDimension(R.dimen.item_card_elevation),
                resources.getDimension(R.dimen.max_item_card_elevation))
        }
    }

    fun leftAndRightRadiusDrawable(context: Context, background: Drawable): Drawable {
        return ShadowDrawableWrapper(context, background).apply {
            val resources = context.resources
            setOrientation(true, false, true, false)
            cornerRadius = resources.getDimension(R.dimen.item_card_radius)
            setShadowSize(resources.getDimension(R.dimen.item_card_elevation),
                resources.getDimension(R.dimen.max_item_card_elevation))
        }
    }

    fun bottomRadiusDrawable(context: Context, background: Drawable): ShadowDrawableWrapper {
        return ShadowDrawableWrapper(context, background).apply {
            val resources = context.resources
            setOrientation(true, false, true, true)
            cornerRadius = resources.getDimension(R.dimen.item_card_radius)
            setShadowSize(resources.getDimension(R.dimen.item_card_elevation),
                resources.getDimension(R.dimen.max_item_card_elevation))
        }
    }

    fun calendarRadiusDrawable(context: Context, background: Drawable): ShadowDrawableWrapper {
        return ShadowDrawableWrapper(context, background).apply {
            val resources = context.resources
            setOrientation(false, false, false, true)
            cornerRadius = resources.getDimension(R.dimen.item_card_radius)
            setShadowSize(resources.getDimension(R.dimen.item_card_elevation),
                resources.getDimension(R.dimen.max_item_card_elevation))
        }
    }
}

class DragViewOnTouchListener() : View.OnTouchListener {

    private var lastX = 0
    private var lastY = 0 //手指在屏幕上的坐标


    private var isDraged = false //View是否被移动过

    private var isDrag = false //判断是拖动还是点击

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val parentRight = (v.parent as ViewGroup).width
        val parentBottom = (v.parent as ViewGroup).height
        val action = event.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                isDrag = false
                isDraged = false
                lastX = event.rawX.toInt()
                lastY = event.rawY.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                val dx: Int = event.rawX.toInt() - lastX
                val dy: Int = event.rawY.toInt() - lastY //手指在屏幕上移动的距离
                if (isDraged) {
                    isDrag = true //如果已经被拖动过，那么无论本次移动的距离是否为零，都判定本次事件为拖动事件
                } else {
                    if (dx == 0 && dy == 0) {
                        isDraged = false //如果移动的距离为零，则认为控件没有被拖动过，灵敏度可以自己控制
                    } else {
                        isDraged = true
                        isDrag = true
                    }
                }
                var l = v.left + dx
                var b = v.bottom + dy
                var r = v.right + dx
                var t = v.top + dy
                if (l < 0) { //处理按钮被移动到父布局的上下左右四个边缘时的情况，防止控件被拖出父布局
                    l = 0
                    r = l + v.width
                }
                if (t < 0) {
                    t = 0
                    b = t + v.height
                }
                if (r > parentRight) {
                    r = parentRight
                    l = r - v.width
                }
                if (b > parentBottom) {
                    b = parentBottom
                    t = b - v.height
                }
                lastX = event.rawX.toInt()
                lastY = event.rawY.toInt()
                v.layout(l, t, r, b)


                //在拖动过按钮后，如果其他view刷新导致重绘，会让按钮重回原点，所以需要更改布局参数
                val lp = v.layoutParams as ViewGroup.MarginLayoutParams
//                v.layout(left, top, right, bottom)
//                lp.setMargins(left, top, 0, 0)
                v.layoutParams = lp

//                v.postInvalidate() //其他view刷新时，会导致view回到原点，可以用设置LayoutParams的方式代替
            }
        }
        return isDrag //如果没有给view设置点击事件，需返回true，否则不会响应ACTION_MOVE,导致view不会被拖动
    }

}
