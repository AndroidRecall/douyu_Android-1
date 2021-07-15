package com.mp.douyu.bean

data class SystemSettingBean(
    val alert: String? = "",
    val bg_video: String? = "",
    val contact: String? = "",
    val email: String? = "",
    var headlines: String? = "",
    var wallet_introduce: String? = "",
    val group: String? = ""
)

data class VersionUpdateBean(
    val code: String? = "",
    val name: String? = "",
    val text: String? = "",
    val url: String? = ""
)