package com.swbg.mlivestreaming.bean

import android.graphics.Typeface
import java.io.Serializable

data class MiniTabBean(val name:String):Serializable {
     var tabName // tab的名称，显示与顶部
            : String? = name
     var tabType // tab的类型（首页，我的，h5等）
            = 0
     var normalIcon // tab没有选中的时候的图标（网络地址）
            : ImageBean? = null
     var selectedIcon // tab选中的时候的图标（网络地址）
            : ImageBean? = null
     var topIcon // tab至顶的图标（网络地址）
            : ImageBean? = null
     var normalIconRes // tab没有选中的时候的图标（本地地址）
            = 0
     var selectedIconRes // tab选中的时候的图标（本地地址）
            = 0
     var topIconRes // tab至顶的图标（本地地址）
            = 0
     var showTips // tab是否显示提醒
            = false
     var tipsIcon // tab提醒的内容
            : ImageBean? = null
     var tabId = 0

     var normalTextColor // tab没有选中的时候的文字颜色
            : String? = null
     var selectedTextColor // tab选中的时候的文字颜色
            : String? = null
     var normalSize //tab没选中的时候的文字大小
            = 0
     var selectedSize //tab选中的时候的文字大小
            = 0
     var normalTypeface //默认文字样式
            : Typeface? = null
     var selectedTypeface //选中文字样式
            : Typeface? = null
     var isSelected = false
     var canTopToRefresh = false //是否支持滑动到顶部

     var showingTop //是否正在显示火箭
            = false
     var selectedClick //选中时点击
            = false
     var lastChangeTime: Long = 0

     var isHot = false

}