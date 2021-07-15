package com.swbg.mlivestreaming.bean

data class SquareDynamicCommentBean(
    var id: Int? = 0,
    var uid: Int? = 0,
    var title: String? = "",
    var url: String? = null,
    var comment_count: Int? = 0,
    var share_count: Int? = 0,
    var like_count: Int? = 0,
    var users: CommonUserBean? = null)