package com.swbg.mlivestreaming.provider


interface SessionProvider : Provider {
    val userId: String
    val userName: String
    val phoneNumber: String
    val lawFirmId: String
    val organizationId: String
    val avatar: String
    val organizationName: String
    val colorFlag: Int
    val hasOrg: Boolean
    val isSupper:Boolean
    val isTimingAdmin:Boolean
    fun isVisitor(): Boolean
    fun bookStatus(): Int
    override fun update(field: String, any: Any)

    companion object {

        const val FIELD_ID = "id"
        const val FIELD_NAME = "name"
        const val FIELD_PHONE = "phone"
        const val FIELD_AVATAR = "avatar"
        const val FIELD_VISITOR = "isVisitor"
        const val FIELD_BOOK_STATUS = "book_status"
        const val FIELD_ORGANIZATION_ID = "organization_id"
        const val FIELD_ORGANIZATION = "organization"
        const val FIELD_LAW_FIRM = "law_firm"
        const val FIELD_COLOR_FLAG = "colorFlag"

        const val FIELD_IS_SUPPER = "is_supper"
        const val FIELD_IS_TIME_ADMIN = "is_time_admin"

        val FIELDS = arrayOf(FIELD_ID, FIELD_NAME, FIELD_PHONE, FIELD_AVATAR, FIELD_VISITOR,
                             FIELD_BOOK_STATUS, FIELD_ORGANIZATION_ID, FIELD_ORGANIZATION, FIELD_LAW_FIRM, FIELD_COLOR_FLAG,FIELD_IS_SUPPER,FIELD_IS_TIME_ADMIN)

        fun get(): SessionProvider {
            return SessionProviderImpl.get()
        }
    }
}
