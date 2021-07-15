package com.mp.douyu.interfaces

import com.tencent.imsdk.v2.V2TIMGroupMemberInfo
import com.tencent.imsdk.v2.V2TIMUserInfo

interface ImMessageListener {
    fun onRecvC2CTextMessage(msgID: String?, sender: V2TIMUserInfo?, text: String?)
    fun onRecvGroupCustomMessage(msgID: String?, groupID: String?, sender: V2TIMGroupMemberInfo?, customData: ByteArray?)
    fun onRecvGroupTextMessage(msgID: String?, groupID: String?, sender: V2TIMGroupMemberInfo?, text: String?)
    fun onRecvC2CCustomMessage(msgID: String?, sender: V2TIMUserInfo?, customData: ByteArray?)
}