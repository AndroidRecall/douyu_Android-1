package com.mp.douyu.view.popupwindow

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec.AT_MOST
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.iyao.recyclerviewhelper.adapter.CachedMultipleChoiceWrapper
import com.iyao.recyclerviewhelper.adapter.takeInstance
import com.iyao.recyclerviewhelper.touchevent.addOnItemClickListener
import com.mp.douyu.R
import com.mp.douyu.ShadowDrawableHelper
import com.mp.douyu.utils.WindowUtils.dip2Px
import kotlinx.android.synthetic.main.layout_common_warp_popup.view.*
import java.lang.Exception

class SingleChoicePopupWindow(val offsetX: Float = 0f,val offsetY:Float = 0f) {

    private lateinit var popupWindow: PopupWindow
    var itemCheckedListener: ((Int) -> Unit)? = null
    var onDismissListener: (() -> Unit)? = null
    var checkedPosition = 0

    @SuppressLint("InflateParams")
    fun create(context: Context, items: ArrayList<String>): SingleChoicePopupWindow {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.layout_common_warp_popup, null).apply {
            setOnClickListener {
                popupWindow.dismiss()
            }
            commonRecyclerView.apply {
                background = ShadowDrawableHelper.allRadiusDrawable(context, background)
                layoutManager = object : LinearLayoutManager(context) {
                    override fun setMeasuredDimension(childrenBounds: Rect, wSpec: Int,
                                                      hSpec: Int) {
                        super.setMeasuredDimension(childrenBounds, wSpec, View.MeasureSpec.makeMeasureSpec(popupWindow.height - dip2Px(40f), AT_MOST))
                    }
                }
                adapter = CachedMultipleChoiceWrapper().apply {
                    client = object : CachedAutoRefreshAdapter<String>() {
                        override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
                            (holder.itemView as? RadioButton)?.text = get(position)
                        }

                        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
                            return CacheViewHolder(inflater.inflate(R.layout.popup_list_left_item_single_choice, parent, false))
                        }
                    }.apply {
                        refresh(items, null)
                    }
                    setItemChecked(0, true)
                }
                addItemDecoration(object : RecyclerView.ItemDecoration() {
                    val bound = Rect()
                    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
                    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                        val position = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
                        when (position) {
                            in 0 until (if (parent.adapter != null && parent.adapter?.itemCount != 0) parent.adapter!!.itemCount - 1 else 0) -> {
                                outRect.set(0, 0, 0, 1)
                            }
                        }
                    }

                    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                        paint.color = ContextCompat.getColor(parent.context, R.color.colorWindowBackground)
                        for (i in 0 until parent.childCount) {
                            val child = parent.getChildAt(i)
                            parent.getDecoratedBoundsWithMargins(child, bound)
                            c.drawRect(bound.apply {
                                top = child.bottom
                            }, paint)
                        }
                    }
                })

                addOnItemClickListener { viewHolder: RecyclerView.ViewHolder ->
                    popupWindow.dismiss()
                    try {
                        itemCheckedListener?.invoke(viewHolder.adapterPosition)
                    } catch (e: Exception) {
                        e.printStackTrace()

                    }
                }
            }
        }
        popupWindow = PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true).apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                isClippingEnabled = false
            }
            animationStyle = if (offsetX >= 0) R.style.popuwin_anim_left else R.style.popuwin_anim_right
            setOnDismissListener {
                onDismissListener?.invoke()
            }
        }
        return this
    }

    fun show(anchor: View) {
        popupWindow.showAsDropDown(anchor, dip2Px(offsetX), dip2Px(offsetY), Gravity.BOTTOM)
        popupWindow.contentView.commonRecyclerView.apply {
            adapter?.takeInstance<CachedMultipleChoiceWrapper>()
                    ?.apply {
                        clearChoices()
                        setItemChecked(checkedPosition, true)
                    }
        }
    }
}