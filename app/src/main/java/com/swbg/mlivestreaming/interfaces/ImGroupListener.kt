package com.swbg.mlivestreaming.interfaces

import com.tencent.imsdk.v2.V2TIMGroupMemberInfo

interface ImGroupListener {
    fun onMemberLeave(groupID: String?, member: V2TIMGroupMemberInfo?)
    fun onMemberEnter(groupID: String?, memberList: MutableList<V2TIMGroupMemberInfo>?)
}