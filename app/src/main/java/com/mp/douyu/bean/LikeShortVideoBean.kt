package com.mp.douyu.bean

class LikeShortVideoBean (
    var id: Int?,
    var is_recommend: Int?,
    var uid: Int?,
    var like_count: Int?,
    var share_count: Int?,
    var comment_count: Int?,
    var status: Int?,
    var title:String?=null,
    var url:String?=null,
    var create_time:String?=null,
    var image:String?=null,
    var update_time:String?=null,
    var hash:String?=null,
    var pivot:PivotBean?=null
   )