package com.mp.douyu.bean

data class HttpDataListBean<T>(
    var total: Int? = 0,
    var per_page: Int? = 0,
    var current_page: Int? = 0,
    var last_page: Int? = 0,
    var data:List<T> = arrayListOf()) {

}