package com.mp.douyu.provider

import android.text.TextUtils

/**
 * 实际提供者
 */
internal class TokenProviderImpl private constructor() : TokenProvider {

    private val memorySerializer: Serializer
    private val sharedPreferencesSerializer: Serializer

    init {
        memorySerializer = MemoryHashMapSerializer(TokenProvider.FIELDS.size)
        sharedPreferencesSerializer = object : SharedPreferencesSerializer() {
            override val fileName: String
                get() = FILE_NAME
        }
        initAccessToken()
    }

    /**
     * 判断是否有Token
     */
    override fun hasToken(): Boolean {
        val accessToken = memorySerializer.get(TokenProvider.FIELD_ACCESS_TOKEN) as? String ?: ""
        return !TextUtils.isEmpty(accessToken)
    }

    /**
     * 访问Token
     */
    override val accessToken: String
        get() {
            val tokenType = memorySerializer.get(TokenProvider.FIELD_TOKEN_TYPE) as? String ?: ""
            val accessToken = memorySerializer.get(TokenProvider.FIELD_ACCESS_TOKEN) as? String ?: ""
            return String.format("%s %s", tokenType, accessToken).trim { it <= ' ' }
        }


    /**
     * websocket IM不拼接access token
     */
    override val accessImToken: String
        get() = memorySerializer.get(TokenProvider.FIELD_ACCESS_TOKEN) as? String ?: ""


    override val clientName: String
        get() = memorySerializer.get(TokenProvider.FIELD_CLIENT_USER_NAME) as? String ?: ""

    /**
     * 密码
     */
    override val clientPsw: String
        get() = memorySerializer.get(TokenProvider.FIELD_REFRESH_USER_PSW) as? String ?: ""

    /**
     * 本地判断Token是否失效，主要是由于权限也会导致Token失效，所以使用这个判断失效原因
     */
    override val isExpired: Boolean
        get() = System.currentTimeMillis() / 1000 - (memorySerializer.get(
                TokenProvider.FIELD_TOKEN_UPDATE_TIME) as? Long ?: 0) >= memorySerializer.get(
                TokenProvider.FIELD_TOKEN_EXPIRE) as? Int ?: 0

    override val userSig: String
        get() = memorySerializer.get(TokenProvider.FIELD_IM_USER_SIG) as? String ?: ""
    override val userId: Int
        get() = memorySerializer.get(TokenProvider.FIELD_IM_USER_ID) as? Int ?: 0

    private fun initAccessToken() {
        memorySerializer.saveAll(sharedPreferencesSerializer.all)
    }

    override fun update(field: String, any: Any) {
        if (field == TokenProvider.FIELD_ACCESS_TOKEN) {
            val updateTime = System.currentTimeMillis() / 1000
            memorySerializer.save(TokenProvider.FIELD_TOKEN_UPDATE_TIME, updateTime)
            sharedPreferencesSerializer.save(TokenProvider.FIELD_TOKEN_UPDATE_TIME, updateTime)
        }
        memorySerializer.save(field, any)
        sharedPreferencesSerializer.save(field, any)
    }

    override fun clear() {
        memorySerializer.clear()
        sharedPreferencesSerializer.clear()
    }

    companion object {
        private const val FILE_NAME = "living_streaming_token"

        internal val tokenProvider by lazy {
            TokenProviderImpl()
        }
    }
}
