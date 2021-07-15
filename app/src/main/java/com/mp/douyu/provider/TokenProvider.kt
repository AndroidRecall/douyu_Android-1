package com.mp.douyu.provider

/**
 * Token本地缓存
 */
interface TokenProvider : Provider {
    val accessToken: String
    val accessImToken: String
    val clientName: String
    val clientPsw: String
    val userSig: String
    val userId: Int
    val isExpired: Boolean
    fun hasToken(): Boolean

    companion object {

        const val FIELD_ACCESS_TOKEN = "access_token"
        const val FIELD_CLIENT_USER_NAME = "client_user_name"
        const val FIELD_REFRESH_USER_PSW = "client_user_psw"
        const val FIELD_TOKEN_TYPE = "token_type"
        const val FIELD_TOKEN_EXPIRE = "expire_in"
        const val FIELD_IM_USER_SIG = "user_sig"
        const val FIELD_IM_USER_ID = "user_id"
        internal const val FIELD_TOKEN_UPDATE_TIME = "token_update_time"
        val FIELDS = arrayOf(FIELD_ACCESS_TOKEN, FIELD_CLIENT_USER_NAME, FIELD_REFRESH_USER_PSW, FIELD_TOKEN_TYPE)

        fun get(): TokenProvider {
            return TokenProviderImpl.tokenProvider
        }
    }
}
