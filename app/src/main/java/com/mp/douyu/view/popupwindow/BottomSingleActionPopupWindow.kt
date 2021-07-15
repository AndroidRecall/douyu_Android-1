package com.mp.douyu.view.popupwindow

import android.animation.ValueAnimator
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
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iyao.recyclerviewhelper.adapter.*
import com.mp.douyu.R
import com.mp.douyu.addOnItemClickListener
import com.mp.douyu.singleClick
import kotlinx.android.synthetic.main.bottom_single_action_popup_header.view.*
import kotlinx.android.synthetic.main.bottom_single_action_popup_view.view.*
import kotlinx.android.synthetic.main.popup_list_item_single_cutline_select.view.*

class BottomSingleActionPopupWindow(val context: Context) {

    private val animatorDuration = 200L
    private lateinit var popupWindow: PopupWindow
    var itemCheckedListener: ((Int) -> Unit)? = null
    var onDismissListener: (() -> Unit)? = null

    @ColorRes
    var color = R.color.color07142e
    var title: String? = null
    var cancel = "取消"
    var items: ArrayList<String> = ArrayList()
    var checkedPosition = 0

    @SuppressLint("InflateParams")
    fun create() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.bottom_single_action_popup_view, null).apply {
            setOnClickListener {
                runDismissAnimator {
                    popupWindow.dismiss()
                    onDismissListener?.invoke()
                }
            }
            if (!title.isNullOrBlank()) {
                apply {
                    cl1.visibility = View.VISIBLE
                    tv_bottom_title.text = title
                    tv_confirm.apply {
                        singleClick {
                            runDismissAnimator {
                                popupWindow.dismiss()
                                onDismissListener?.invoke()
                            }
                        }
                    }
                }

            }
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = CachedHeaderAndFooterWrapper().apply {
                    client = object : CachedAutoRefreshAdapter<String>() {
                        override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
                            holder.itemView.apply {
                                tvContent.text = items[position]
                            }
                        }

                        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
                            return CacheViewHolder(inflater.inflate(R.layout.popup_list_item_single_cutline_select,
                                parent,
                                false)).apply {}
                        }
                    }.apply {
                        refresh(items, null)
                    }
                    /* if (!title.isNullOrBlank()) {
                         addHeader(1,
                             CacheViewHolder(inflater.inflate(R.layout.bottom_single_action_popup_header,
                                 recyclerView,
                                 false)).apply {
                                 itemView.apply {
                                     tv_bottom_title.text = title
                                     tv_confirm.apply {
                                         singleClick {
                                             runDismissAnimator {
                                                 popupWindow.dismiss()
                                                 onDismissListener?.invoke()
                                             }
                                         }
                                     }
                                 }

                             })
                     }*/
                }
                addItemDecoration(object : RecyclerView.ItemDecoration() {
                    val bound = Rect()
                    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
                    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                        val position =
                            (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
                        val startPosition = if (title.isNullOrEmpty()) 0 else 1
                        when (position) {
                            in startPosition until (parent.adapter?.itemCount ?: 0) - 1 -> {
                                outRect.set(0, 0, 0, 1)
                            }
                        }
                    }

                    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                        paint.color =
                            ContextCompat.getColor(parent.context, R.color.colorWindowBackground)
                        val startPosition = if (title.isNullOrEmpty()) 0 else 1
                        for (i in startPosition until parent.childCount) {
                            val child = parent.getChildAt(i)
                            parent.getDecoratedBoundsWithMargins(child, bound)
                            c.drawRect(bound.apply {
                                top = child.bottom
                            }, paint)
                        }
                    }
                })

                addOnItemClickListener { viewHolder: RecyclerView.ViewHolder ->
                    runDismissAnimator {
                        popupWindow.dismiss()
                        onDismissListener?.invoke()
                        itemCheckedListener?.invoke(
                            adapter?.takeInstance<CachedAutoRefreshAdapter<Pair<String, Int>>>()
                            ?.let {
                                adapter?.getWrappedPosition(it, viewHolder.adapterPosition)
                            } ?: -1)
                    }
                }
            }

        }
        popupWindow = PopupWindow(view,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            true).apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            contentView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M || Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                isClippingEnabled = false
            }
            animationStyle = R.style.FadeInAnimation
        }
    }


    fun show(anchor: View) {
        if (!::popupWindow.isInitialized) {
            create()
        }
        popupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, 0, 0)
        runStartAnimator()

    }

    private fun runStartAnimator() {
        popupWindow.contentView.apply {
            ValueAnimator.ofArgb(Color.TRANSPARENT,
                ContextCompat.getColor(context, R.color.colorBlur)).apply {
                addUpdateListener {
                    (background as ColorDrawable).color = it.animatedValue as Int
                }
            }.setDuration(animatorDuration).start()
            recyclerView.translationY = 400f
            cl1.translationY = 400f
            ViewCompat.animate(recyclerView).translationY(0f).setDuration(animatorDuration).start()
            ViewCompat.animate(cl1).translationY(0f).setDuration(animatorDuration).start()
        }
    }

    private fun runDismissAnimator(endAction: () -> Unit) {
        popupWindow.contentView.apply {
            ValueAnimator.ofArgb(ContextCompat.getColor(context, R.color.colorBlur),
                Color.TRANSPARENT).apply {
                addUpdateListener {
                    (background as ColorDrawable).color = it.animatedValue as Int
                }
            }.setDuration(animatorDuration).start()
            val tranY = 400f
            ViewCompat.animate(recyclerView).translationY(tranY).setDuration(animatorDuration)
                .withEndAction(endAction).start()
            ViewCompat.animate(cl1).translationY(tranY).setDuration(animatorDuration)
                .withEndAction(endAction).start()
        }
    }
}