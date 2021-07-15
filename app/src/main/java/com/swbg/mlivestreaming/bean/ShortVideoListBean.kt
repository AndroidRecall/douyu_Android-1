package com.swbg.mlivestreaming.bean

data class ShortVideoListBean(
    var total: Int? = 0,
    var per_page: Int? = 0,
    var current_page: Int? = 0,
    var last_page: Int? = 0,
    var data:List<ShortVideoBean> = arrayListOf()) {

}