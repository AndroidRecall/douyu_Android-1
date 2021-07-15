package com.mp.douyu.bean

data class ShortVideoCommentListBean(
    var id: Int? = 0,
    var uid: Int? = 0,
    var content: String? = null,
    var like_count: Int? = 0,
    var user: CommonUserBean? = null,
    var data_i: ShortVideoCommentListBean? = null
) {

}