package com.swbg.mlivestreaming.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import com.swbg.mlivestreaming.interfaces.ItemViewType
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class LiveBean(
    var id:Int?=0,
    var anchor_id:Int?=0,
    var group_id:Int?=0,
    var gold:Int?=0,
    var cate_id:Int?=0,
    var duration:Int?=0,
    var avatar:String?=null,
    var title:String?=null,
    var image:String?=null,
    var direction:String?=null,
    var fans_count:Int?=0,
    var gift_count:Int = 0,
    var comment:String?=null,
    var pull_url:String?=null,
    var push_url:String?=null,
    var status:Int?=0,
    var hot:Int?=0,
    var create_time:String?=null,
    var update_time:String?=null,
    var end_time:String?=null,
    var user_id:Int?=0,
    var name:String?=null,
    var notice:String?=null,
    var pivot:PivotBean?=null,
    var itemViewType: Int =0,
    var isFollowLive: Int =0,
    var url:String? =null,
    var anchor:AnchorBean? =null,
    var user: CommonUserBean?=null,
    var is_follow:Int = 0,
    var isHorizontal: Boolean? = true,
    var post_count: Int? = 0,
    var isOpenDanmaku:Boolean = false,
    var listAvatars :MutableList<CommonUserBean> = arrayListOf()
):Parcelable {

}