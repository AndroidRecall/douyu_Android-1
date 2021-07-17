package com.mp.douyu.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
 data  class Liveban (
    var id: Int?=0,
    var anchor_id: Int?=0,
    var gold: Int?=0,
    var avatar:String?=null,
    var user_name:String?=null,
    var fans_count: Int?=0,
    var post_count: Int?=0,
    var follow_count: Int?=0,
    var circle_count: Int?=0,
    var gift_count: Int?=0,
    var level_id: Int?=0,
    var cate_id: Int?=0,
    var comment: String?=null,
    var status: Int?=0,
    var create_time:String?=null,
    var update_time:String?=null,
    var union_id:String?=null,
    var image:String?=null,
    var user_id: Int?=0,
    var is_ban: Int?=0,
    var next_level_time: Int?=0,
    var next_level_experience: Int?=0,
    var title: String?=null,
    var name: String?=null,
    var time: String?=null,
    var experience: String?=null,
    var user: CommonUserBean?=null,
    var level: LevelBean?=null
): Parcelable {}