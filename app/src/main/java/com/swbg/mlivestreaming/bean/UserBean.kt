package com.swbg.mlivestreaming.bean

class UserBean(var phone: String? = "", var verifyCode: String? = "", var verifyCodeId: String? = "", var psw: String? = "", var userName: String? = "", var extensionCode : String? = "", var confirmPsw : String? = "", var type : String? = "") {


}
data class UserInfoBean(
    val headlines: String? = "",
    val user: User? = User()
)

data class User(
    var age: String? = "0",
    var avatar: String? = "",
    val balance: String? = "0",
    val username: String? = "",
    val email: String? = "",
    var fans: Int? = 0,
    var follow: Int? = 0,
    val game_balance: String? = "0",
    var gender: Int? = 0,
    val id: Int? = 0,
    val nickname: String? = "",
    var node: Int? = 0,
    val phone: String? = "",
    val points: Int? = 0,
    val qq: String? = "",
    var sign: String? = "",
    val vip: String? = "",
    var vip_time: String? = "",
    val wallet_url: String? = "",
    val wechat: String? = ""
)