package com.mp.douyu.ui.anchor.live

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import cn.hadcn.keyboard.ChatKeyboardLayout
import com.mp.douyu.base.VisibilityFragment
import com.mp.douyu.im.ImManager
import com.mp.douyu.interfaces.ImGroupListener
import com.mp.douyu.interfaces.ImMessageListener
import com.tencent.imsdk.v2.V2TIMGroupMemberInfo
import com.tencent.imsdk.v2.V2TIMUserInfo

abstract class BaseLiveFragment : VisibilityFragment() {
    override fun onVisible() {
        super.onVisible()
    }

    override fun onInvisible() {
        super.onInvisible()
    }

    override fun onVisibleFirst() {
        super.onVisibleFirst()
        initMsgListener()
    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
    }

    private fun initMsgListener() {
        ImManager.instance.setLiveRoomListener(groupListener)
        ImManager.instance.setSimpleMsgListener(messageListener)
    }

    private fun removeMsgListener() {
        ImManager.instance.removeLiveRoomListener(groupListener)
        ImManager.instance.removeSimpleMsgListener(messageListener)
    }

    val groupListener by lazy {
        object : ImGroupListener {
            override fun onMemberLeave(groupID: String?, member: V2TIMGroupMemberInfo?) {
                this@BaseLiveFragment.onMemberLeave(groupID, member)
            }

            override fun onMemberEnter(groupID: String?, memberList: MutableList<V2TIMGroupMemberInfo>?) {
                this@BaseLiveFragment.onMemberEnter(groupID, memberList)
            }
        }
    }
    val messageListener by lazy {
        object : ImMessageListener {
            override fun onRecvC2CTextMessage(msgID: String?, sender: V2TIMUserInfo?, text: String?) {
                Log.e(TAG, "消息监听: 私聊文本消息 msgID=${msgID},sender=${sender?.nickName},text=${text}")
                this@BaseLiveFragment.onRecvC2CTextMessage(msgID, sender, text)
            }

            override fun onRecvC2CCustomMessage(msgID: String?, sender: V2TIMUserInfo?, customData: ByteArray?) {
                Log.e(TAG, "消息监听: 私聊自定义消息 msgID=${msgID},msgID=${msgID},sender=${sender?.nickName}")
                this@BaseLiveFragment.onRecvC2CCustomMessage(msgID, sender, customData)
            }

            override fun onRecvGroupCustomMessage(msgID: String?, groupID: String?, sender: V2TIMGroupMemberInfo?, customData: ByteArray?) {
                Log.e(TAG,
                    "消息监听: 群自定义消息 msgID=${msgID},groupID=${groupID},sender=${sender?.nickName}")
                this@BaseLiveFragment.onRecvGroupCustomMessage(msgID, groupID, sender, customData)
            }

            override fun onRecvGroupTextMessage(msgID: String?, groupID: String?, sender: V2TIMGroupMemberInfo?, text: String?) {
                Log.e(TAG,
                    "消息监听: 群文本消息 msgID=${msgID},groupID=${groupID},sender=${sender?.nickName},text=${text}")
                this@BaseLiveFragment.onRecvGroupTextMessage(msgID, groupID, sender, text)

            }

        }
    }
    open fun onMemberLeave(groupID: String?, member: V2TIMGroupMemberInfo?) {

    }

    open fun onMemberEnter(groupID: String?, memberList: MutableList<V2TIMGroupMemberInfo>?) {

    }

    open fun onRecvC2CTextMessage(msgID: String?, sender: V2TIMUserInfo?, text: String?) {
    }

    open fun onRecvC2CCustomMessage(msgID: String?, sender: V2TIMUserInfo?, customData: ByteArray?) {
    }

    open fun onRecvGroupCustomMessage(msgID: String?, groupID: String?, sender: V2TIMGroupMemberInfo?, customData: ByteArray?) {
    }

    open fun onRecvGroupTextMessage(msgID: String?, groupID: String?, sender: V2TIMGroupMemberInfo?, text: String?) {

    }

    /**
     * 初始化键盘
     */
    open fun initKeyboard(keyboard: ChatKeyboardLayout) {
        keyboard.setKeyboardStyle(ChatKeyboardLayout.Style.TEXT_EMOTICON)
        keyboard.setOnChatKeyBoardListener(object :
            ChatKeyboardLayout.SimpleOnChatKeyboardListener() {
            override fun onSendButtonClicked(text: String) {
                super.onSendButtonClicked(text)
                text?.let {
                    if (text.isNotEmpty()) {
                        //发送信息
                        sendLiveTextMsg(text)
                        keyboard.clearInputContent()
                        keyboard.hideKeyboard()
                        keyboard.visibility = View.GONE
                    }
                }
            }

            override fun onBackPressed() {
                super.onBackPressed()
                keyboard.hideKeyboard()
                keyboard.visibility = View.GONE
            }

            override fun onSoftKeyboardClosed() {
                super.onSoftKeyboardClosed()
                keyboard.hideKeyboard()
                keyboard.visibility = View.GONE
            }
        })
    }

    /**
     * 发送文本消息
     */
    open fun sendLiveTextMsg(text: String) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        removeMsgListener()
    }

    fun RecyclerView.getCurView(): View {
        return getChildAt(0)
    }
    fun RecyclerView.getCurViewHolder(): RecyclerView.ViewHolder {
        return getCurView()?.let {it ->
            getChildViewHolder(it)
        }
    }
}