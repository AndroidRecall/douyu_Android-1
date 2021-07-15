package com.mp.douyu.provider


import androidx.annotation.UiThread


internal class SessionProviderImpl private constructor() : SessionProvider {
    private val memorySerializer by lazy {
        MemoryHashMapSerializer(SessionProvider.FIELDS.size).apply {
            saveAll(sharedPreferencesSerializer.all)
        }
    }
    private val sharedPreferencesSerializer by lazy {
        object : SharedPreferencesSerializer() {
            override val fileName: String
                get() = FILE_NAME
        }
    }

    override val isSupper: Boolean
    get() = memorySerializer.get(SessionProvider.FIELD_IS_SUPPER) as? Boolean ?: false

    override val isTimingAdmin: Boolean
    get()  = memorySerializer.get(SessionProvider.FIELD_IS_TIME_ADMIN) as? Boolean ?: false

    override val userId: String
        get() = memorySerializer.get(SessionProvider.FIELD_ID) as? String ?: ""

    override val userName: String
        get() = memorySerializer.get(SessionProvider.FIELD_NAME) as? String ?: ""

    override val phoneNumber: String
        get() = memorySerializer.get(SessionProvider.FIELD_PHONE) as? String ?: ""

    override val lawFirmId: String
        get() = memorySerializer.get(SessionProvider.FIELD_LAW_FIRM) as? String ?: ""

    override val organizationId: String
        get() = memorySerializer.get(SessionProvider.FIELD_ORGANIZATION_ID) as? String ?: ""

    override val avatar: String
        get() = (memorySerializer.get(SessionProvider.FIELD_AVATAR) as? String)
                ?.replace("/resize,m_fill,h_40,w_40", "").orEmpty()

    override val organizationName: String
        get() = memorySerializer.get(SessionProvider.FIELD_ORGANIZATION) as? String ?: ""

    override val colorFlag: Int
        get() = memorySerializer.get(SessionProvider.FIELD_COLOR_FLAG) as? Int ?: 0

    override val hasOrg: Boolean
        get() = lawFirmId != "0" && organizationId != lawFirmId

    override fun isVisitor(): Boolean {
        val o = memorySerializer.get(SessionProvider.FIELD_VISITOR) as? Boolean
        return o ?: false
    }


    override fun bookStatus(): Int {
        val o = memorySerializer.get(SessionProvider.FIELD_BOOK_STATUS) as? Int
        return o ?: 0
    }

    override fun update(field: String, any: Any) {
        memorySerializer.save(field, any)
        sharedPreferencesSerializer.save(field, any)
    }

    override fun clear() {
        memorySerializer.clear()
        sharedPreferencesSerializer.clear()
    }

    companion object {

        private const val FILE_NAME = "swartz_session"
        private val sessionProvider by lazy {
            SessionProviderImpl()
        }

        @UiThread
        fun get(): SessionProvider {
            return sessionProvider
        }
    }

}
