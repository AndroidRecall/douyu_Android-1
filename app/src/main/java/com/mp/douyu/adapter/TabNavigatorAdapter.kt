package com.mp.douyu.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.mp.douyu.R
import com.mp.douyu.bean.MiniTabBean
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView

class TabNavigatorAdapter(private var mTabBeans: List<MiniTabBean>, private var onTabItemClickListener: OnTabItemClickListener?) :
    CommonNavigatorAdapter() {
    private val TAG = javaClass.simpleName
    var normalColor = 0
    var selectedColor = 0
    var normalSize = 0
    var selectedSize = 0
    var isShow = true
    var isHot = false
    var normalTypeface: Typeface? = null
    var selectedTypeface: Typeface? = null


    constructor(listener: OnTabItemClickListener) : this(listOf(), listener) {
        onTabItemClickListener = listener
    }


    override fun getTitleView(context: Context?, index: Int): IPagerTitleView? {
        if (mTabBeans == null || index >= mTabBeans.size) {
            return null
        }
        if (normalColor == 0) normalColor =
            context?.resources?.getColor(R.color.tab_normal_color) ?: 0

        if (selectedColor == 0) selectedColor =
            context?.resources?.getColor(R.color.tab_select_color) ?: 0
        if (normalSize == 0) normalSize = 16
        if (selectedSize == 0) selectedSize = 16

        if (normalTypeface == null) {
            normalTypeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        }
        if (selectedTypeface == null) {
            selectedTypeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        }
        var tabNormalColor = normalColor
        var tabSelectedColor = selectedColor
        var tabNormalSize = normalSize
        var tabSelectedSize = selectedSize
        var tabNormalTypeface = normalTypeface
        var tabSelectedTypeface = selectedTypeface
        var commonPagerTitleView = CommonPagerTitleView(context)
        var tabItemLayout: View =
            LayoutInflater.from(context).inflate(R.layout.layout_tab_item, null)
        var tabText = tabItemLayout.findViewById<TextView>(R.id.tv_tab_text)
        var splitLine = tabItemLayout.findViewById<ImageView>(R.id.iv_title_split_line)
        var tabHotFlag = tabItemLayout.findViewById<TextView>(R.id.tv_hot_flag)
        val tabBean = mTabBeans[index]

        tabNormalColor =
            if (tabBean?.normalTextColor != null) Color.parseColor(tabBean?.normalTextColor) else tabNormalColor
        tabSelectedColor =
            if (tabBean?.selectedTextColor != null) Color.parseColor(tabBean.selectedTextColor) else tabSelectedColor
        if (tabBean.normalSize > 0) {
            tabNormalSize = tabBean.normalSize
        }
        if (tabBean.selectedSize > 0) {
            tabSelectedSize = tabBean.selectedSize
        }
        if (tabBean.normalTypeface != null) {
            tabNormalTypeface = tabBean.normalTypeface
        }
        if (tabBean.selectedTypeface != null) {
            tabSelectedTypeface = tabBean.selectedTypeface
        }
        tabText.text = tabBean.tabName
        tabText.setTextColor(tabNormalColor)
        tabText.textSize = tabNormalSize.toFloat()
        tabHotFlag.visibility = if (tabBean.isHot) View.VISIBLE else View.GONE
        var finalTabSelectedColor = tabSelectedColor
        var finalTabNormalColor = tabNormalColor
        var finalTabNormalSize = tabNormalSize
        var finalTabSelectedSize = tabSelectedSize
        var finalTabNormalTypeface = tabNormalTypeface
        var finalTabSelectedTypeface = tabSelectedTypeface
        commonPagerTitleView.onPagerTitleChangeListener =
            object : CommonPagerTitleView.OnPagerTitleChangeListener {
                override fun onDeselected(index: Int, totalCount: Int) {
                    if (tabBean != null && tabBean.isSelected) {
                        tabBean.isSelected = false
                        tabText.setTextColor(finalTabNormalColor)
                        tabText.textSize = finalTabNormalSize.toFloat()
                        tabText.setTypeface(finalTabNormalTypeface)
                        splitLine.visibility = View.INVISIBLE
                    }
                }

                override fun onSelected(index: Int, totalCount: Int) {
                    if (tabBean != null && !tabBean.isSelected) {
                        tabBean.isSelected = true
                        tabText.setTextColor(finalTabSelectedColor)
                        tabText.textSize = finalTabSelectedSize.toFloat()
                        tabText.setTypeface(finalTabSelectedTypeface)
                        splitLine.visibility = if (isShow) View.VISIBLE else View.INVISIBLE
                    }
                }

                override fun onLeave(index: Int, totalCount: Int, leavePercent: Float, leftToRight: Boolean) {

                }

                override fun onEnter(index: Int, totalCount: Int, enterPercent: Float, leftToRight: Boolean) {

                }

            }
        commonPagerTitleView.setContentView(tabItemLayout)
        commonPagerTitleView.setOnClickListener {
            onTabItemClickListener?.run {
                onTabItemClick(index)
            }
        }
        return commonPagerTitleView
    }

    override fun getCount(): Int {
        return mTabBeans.size
    }

    override fun getIndicator(context: Context?): IPagerIndicator? {
        return null
    }

    interface OnTabItemClickListener {
        fun onTabItemClick(position: Int)
    }
}