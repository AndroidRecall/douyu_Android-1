package com.mp.douyu.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import com.tencent.rtmp.downloader.TXVodDownloadMediaInfo
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class DownLoadBean(
    var video_id:Int?=0,//视频id
    var type:Int?=0,
    var title:String?=null,//视频标题
    var cover:String?=null,//封面
    var url:String?=null,//下载地址
    var path:String?=null,//存储路径
    var create_time:String?=null,
    var update_time:String?=null,
    var downState:Int = 0 ,  //下载状态 0:下载中 1:下载完成 2:下载失败
    var isEdit:Boolean?=false,  //是否处于编辑状态
    var isSelect:Boolean?=false  //是否处于选中状态

):Parcelable {
    var mediaInfo: TXVodDownloadMediaInfo? =null
}