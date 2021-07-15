package com.swbg.mlivestreaming

import android.app.Application
import cn.hadcn.keyboard.ChatKeyboardLayout
import cn.hadcn.keyboard.EmoticonEntity
import com.swbg.mlivestreaming.im.ImManager
import com.swbg.mlivestreaming.utils.LogUtils
import com.tencent.rtmp.TXLiveBase
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback
import io.alterac.blurkit.BlurKit
import java.util.*
import kotlin.properties.Delegates


class MApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        getApplicationInstances = this
        initTencent()
        initKeyboard()
        initLiveSDK()
        initIMSDK()
        //进行BlurKit初始化，在Application中初始化
        BlurKit.init(this);
    }

    private fun initIMSDK() {
       ImManager.instance.init(this)
    }


    private fun initLiveSDK() {
        val licenceURL =
            "http://license.vod2.myqcloud.com/license/v1/4a9eef04b8428cd4fcfb17a4c6d7f8ce/TXLiveSDK.licence" // 获取到的 licence url
        val licenceKey = "d5570fed49f23e8d55eb509821c07417" // 获取到的 licence key
        TXLiveBase.getInstance().setLicence(this, licenceURL, licenceKey)
    }

    private fun initKeyboard() {
        if (!ChatKeyboardLayout.isEmoticonsDBInitSuccess(this)) {
            val entities: List<EmoticonEntity> = ArrayList()
            ChatKeyboardLayout.initEmoticonsDB(this, true, entities)
        }
    }

    private fun initTencent() {
        //非wifi情况下，主动下载x5内核

        //非wifi情况下，主动下载x5内核
        QbSdk.setDownloadWithoutWifi(true)
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        val cb: PreInitCallback = object : PreInitCallback {
            override fun onViewInitFinished(arg0: Boolean) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                LogUtils.e("开启TBS===X5加速成功")
            }

            override fun onCoreInitFinished() {
                LogUtils.e("开启TBS===X5加速失败")
            }
        }
        //x5内核初始化接口
        //x5内核初始化接口
        QbSdk.initX5Environment(applicationContext, cb)


    }


    companion object {
        var getApplicationInstances: MApplication by Delegates.notNull()
    }

}