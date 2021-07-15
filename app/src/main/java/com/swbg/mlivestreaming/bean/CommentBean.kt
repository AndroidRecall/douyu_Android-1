package com.swbg.mlivestreaming.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class CommentBean(
    var id: Int? = 0,
    var post_id: Int? = 0,
    @SerializedName("like_count",alternate = ["like"])
    var like_count: Int? = 0,
    @SerializedName("reply_count",alternate = ["reply"])
    var reply_count: Int? = 0,
    var uid: Int? =0,
    var pid: Int? =0,
    var content: String? = null,
    var update_time: String? = null,
    var create_time: String? = null,
    var status: Int? = 0,
    var is_elite: Int? = 0,
    var is_follow: Int? = 0,
    var is_like: Int? = 0,
    var user: CommonUserBean? = null,

    var itemType:Int=0,
    var title:String? =null,
    var comment_num:Int? =0,
    var is_comment_like:Int? =0,
    var headerBean:DynamicBean? =null,
    var data_i:MutableList<CommentBean> = arrayListOf(),
    var advBean: AdvBean?=null
    ): Parcelable {
}