package com.mp.douyu.im

import android.content.Context
import android.util.Log
import com.mp.douyu.bean.TokenBean
import com.mp.douyu.bean.UserInfoBean
import com.mp.douyu.cacheSession
import com.mp.douyu.interfaces.ImGroupListener
import com.mp.douyu.interfaces.ImMessageListener
import com.mp.douyu.provider.StoredUserSources
import com.mp.douyu.provider.TokenProvider
import com.tencent.imsdk.v2.*

class ImManager private constructor() {
    var groupListenerList:MutableList<ImGroupListener> = arrayListOf()
    var messageListenerList:MutableList<ImMessageListener> = arrayListOf()
    companion object {
        private val TAG: String = ImManager::class.java.simpleName
        private val sdkAppID: Int = 1400547138

        val instance: ImManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ImManager()
        }
    }

    fun init(context: Context) {
        // 1. 从 IM 控制台获取应用 SDKAppID，详情请参考 SDKAppID。
        // 2. 初始化 config 对象
        val config = V2TIMSDKConfig().apply {
            // 3. 指定 log 输出级别，详情请参考 SDKConfig。
            logLevel = V2TIMSDKConfig.V2TIM_LOG_DEBUG
        }
        // 4. 初始化 SDK 并设置 V2TIMSDKListener 的监听对象。
        // initSDK 后 SDK 会自动连接网络，网络连接状态可以在 V2TIMSDKListener 回调里面监听。
        V2TIMManager.getInstance().initSDK(context, sdkAppID, config, object : V2TIMSDKListener() {
            override fun onConnecting() {
                super.onConnecting()
                // 正在连接到腾讯云服务器
                Log.e(TAG, "正在连接到腾讯云服务器")
            }

            override fun onConnectFailed(code: Int, error: String?) {
                super.onConnectFailed(code, error)
                // 连接腾讯云服务器失败
                Log.e(TAG, "连接腾讯云服务器失败")
            }

            override fun onSelfInfoUpdated(info: V2TIMUserFullInfo?) {
                super.onSelfInfoUpdated(info)
                //当前用户的资料发生了更新
                Log.e(TAG, "当前用户的资料发生了更新")
            }

            override fun onConnectSuccess() {
                super.onConnectSuccess()
                // 已经成功连接到腾讯云服务器
                Log.e(TAG, "已经成功连接到腾讯云服务器")
                initListener()
                if (TokenProvider.get().hasToken()) {
                    Log.e(TAG,
                        "userId=${TokenProvider.get().userId},userSig=${TokenProvider.get().userSig}")
                    loginIM("${TokenProvider.get().userId}",
                        "${TokenProvider.get().userSig}",
                        object : V2TIMCallback {
                            override fun onSuccess() {
                                Log.e(TAG, "IM 登录成功 success")
                            }

                            override fun onError(p0: Int, p1: String?) {
                                Log.e(TAG, "IM 登录失败 onError: code=${p0},msg=${p1}")
                                val tokenProvider = TokenProvider.get()
                                TokenBean("",
                                    userName = tokenProvider.clientName,
                                    userPsw = tokenProvider.clientPsw).cacheSession()
                                StoredUserSources.putUserInfo(UserInfoBean())
                            }
                        })
                } else {
//                    MApplication.getApplicationInstances.startActivity(LoginActivity.open(
//                        MApplication.getApplicationInstances))
                }

            }

            override fun onKickedOffline() {
                super.onKickedOffline()
                //当前用户被踢下线
                Log.e(TAG, "当前用户被踢下线")
            }

            override fun onUserSigExpired() {
                super.onUserSigExpired()
                //登录票据已经过期
                Log.e(TAG, "登录票据已经过期")
            }
        })
    }

    private fun initListener() {
        V2TIMManager.getInstance().setGroupListener(object : V2TIMGroupListener() {
            override fun onMemberLeave(groupID: String?, member: V2TIMGroupMemberInfo?) {
                groupListenerList.forEach {
                    it.onMemberLeave(groupID,member)
                }
            }

            override fun onMemberEnter(groupID: String?, memberList: MutableList<V2TIMGroupMemberInfo>?) {
                super.onMemberEnter(groupID, memberList)
                groupListenerList.forEach {
                    it.onMemberEnter(groupID, memberList)
                }
            }
        })
        V2TIMManager.getInstance().addSimpleMsgListener(object :V2TIMSimpleMsgListener(){
            override fun onRecvC2CTextMessage(msgID: String?, sender: V2TIMUserInfo?, text: String?) {
                super.onRecvC2CTextMessage(msgID, sender, text)
                messageListenerList.forEach {
                    it.onRecvC2CTextMessage(msgID, sender, text)
                }
            }

            override fun onRecvGroupCustomMessage(msgID: String?, groupID: String?, sender: V2TIMGroupMemberInfo?, customData: ByteArray?) {
                super.onRecvGroupCustomMessage(msgID, groupID, sender, customData)
                messageListenerList.forEach {
                    it.onRecvGroupCustomMessage(msgID, groupID, sender, customData)
                }
            }

            override fun onRecvGroupTextMessage(msgID: String?, groupID: String?, sender: V2TIMGroupMemberInfo?, text: String?) {
                super.onRecvGroupTextMessage(msgID, groupID, sender, text)
                messageListenerList.forEach {
                    it.onRecvGroupTextMessage(msgID, groupID, sender, text)
                }
            }

            override fun onRecvC2CCustomMessage(msgID: String?, sender: V2TIMUserInfo?, customData: ByteArray?) {
                super.onRecvC2CCustomMessage(msgID, sender, customData)
                messageListenerList.forEach {
                    it.onRecvC2CCustomMessage(msgID, sender, customData)
                }
            }
        })
    }

    /**
     * 登录IM
     */
    fun loginIM(userID: String, userSig: String, callback: V2TIMCallback) {
        V2TIMManager.getInstance().login(userID, userSig, callback)
    }

    /**
     * 登出IM
     */
    fun logoutIM(callback: V2TIMCallback) {
        V2TIMManager.getInstance().logout(callback)
    }

    /**
     * 创建直播间聊天室
     */
    fun createLiveRoom(groupType: String = V2TIMManager.GROUP_TYPE_AVCHATROOM, groupID: String, groupName: String, callback: V2TIMValueCallback<String>) {
        V2TIMManager.getInstance().createGroup(groupType, groupID, groupName, callback)
    }

    /**
     * 监听直播间动作
     */
    fun setLiveRoomListener(listener: ImGroupListener) {
        groupListenerList.add(listener)
    }

    /**
     * 监听简单消息
     */
    fun setSimpleMsgListener(listener: ImMessageListener) {
        messageListenerList.add(listener)
    }

    fun removeLiveRoomListener(listener: ImGroupListener){
        groupListenerList.remove(listener)
    }
    fun removeSimpleMsgListener(listener: ImMessageListener){
        messageListenerList.remove(listener)
    }
    /**
     * 加入直播间聊天室
     */
    fun joinLiveRoom(groupID: String, message: String, callback: V2TIMCallback) {
        V2TIMManager.getInstance().joinGroup(groupID, message, callback)
    }

    /**
     * 退出直播间聊天室
     */
    fun quitLiveRoom(groupID: String, callback: V2TIMCallback) {
        V2TIMManager.getInstance().quitGroup(groupID, callback)
    }

    /**
     * 解散群组
     */
    fun dismissLiveRoom(groupID: String, callback: V2TIMCallback) {
        V2TIMManager.getInstance().dismissGroup(groupID, callback)
    }

    /**
     * 发送直播间普通文本消息
     */
    fun sendLiveTextMessage(text: String, groupID: String, priority: Int, callback: V2TIMValueCallback<V2TIMMessage>) {
        V2TIMManager.getInstance().sendGroupTextMessage(text, groupID, priority, callback)
    }

    /**
     * 获取个人资料
     */
    fun getUsersInfo(userIDList: List<String>, callback: V2TIMValueCallback<List<V2TIMUserFullInfo>>) {
        V2TIMManager.getInstance().getUsersInfo(userIDList, callback)
    }

    /**
     * 修改资料
     */
    fun setSelfInfo(info: V2TIMUserFullInfo, callback: V2TIMCallback) {
        V2TIMManager.getInstance().setSelfInfo(info, callback)
    }

}