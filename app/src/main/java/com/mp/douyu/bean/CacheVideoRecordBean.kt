package com.mp.douyu.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class CacheVideoRecordBean(
    var downLoadBean: DownLoadBean?=null,//视频
    var isEdit:Boolean?=false,  //是否处于编辑状态
    var isSelect:Boolean?=false  //是否处于选中状态
    ): Parcelable {
    }
