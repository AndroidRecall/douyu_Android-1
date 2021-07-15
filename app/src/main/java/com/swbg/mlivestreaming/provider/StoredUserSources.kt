package com.swbg.mlivestreaming.provider

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.swbg.mlivestreaming.bean.*
import com.swbg.mlivestreaming.utils.SharedPreferenceUtils
import java.util.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.set

object StoredUserSources {

    fun getSettingData(): SystemSettingBean? {
        val json = SharedPreferenceUtils.readObject("StoredUserSources", "user_setting")
        if (json != null) {
            val gson = GsonBuilder().create()
            val ssb = gson.fromJson<SystemSettingBean>(json,
                object : TypeToken<SystemSettingBean>() {}.type)
            return ssb
        }
        return SystemSettingBean()
    }

    fun getUserInfo(): UserInfoBean? {
        val json = SharedPreferenceUtils.readObject("StoredUserSources", "user_info")
        if (json != null) {
            val gson = GsonBuilder().create()
            val ssb = gson.fromJson<UserInfoBean>(json, object : TypeToken<UserInfoBean>() {}.type)
            return ssb
        }
        return UserInfoBean()
    }
    fun getUserInfo2(): UserInfo2Bean? {
        val json = SharedPreferenceUtils.readObject("StoredUserSources", "user_info2")
        if (json != null) {
            val gson = GsonBuilder().create()
            val ssb = gson.fromJson<UserInfo2Bean>(json, object : TypeToken<UserInfo2Bean>() {}.type)
            return ssb
        }
        return UserInfo2Bean()
    }
    fun getGroupId(): String? {
        val json = SharedPreferenceUtils.readObject("StoredUserSources", "group_id")
        if (json != null) {
            val gson = GsonBuilder().create()
            val ssb = gson.fromJson<String>(json, object : TypeToken<String>() {}.type)
            return ssb
        }
        return ""
    }
    fun putUserInfo(it: UserInfoBean?) {
        SharedPreferenceUtils.writeObject("StoredUserSources", "user_info", it)
    }
    fun putUserInfo2(it: UserInfo2Bean?) {
        SharedPreferenceUtils.writeObject("StoredUserSources", "user_info2", it)
    }
    fun putGroupId(id:String?){
        SharedPreferenceUtils.writeObject("StoredUserSources", "group_id", id)
    }
    fun getSearchHistory(): ArrayList<SearchHistoryBean>? {
        val json = SharedPreferenceUtils.readObject("StoredUserSources", "search_history")
        if (json != null) {
            val gson = GsonBuilder().create()
            val ssb = gson.fromJson<ArrayList<SearchHistoryBean>>(json,
                object : TypeToken<ArrayList<SearchHistoryBean>>() {}.type)
            return ssb
        }
        return arrayListOf()
    }

    fun putSearchHistory(historyList: ArrayList<SearchHistoryBean>) {
        SharedPreferenceUtils.writeObject("StoredUserSources", "search_history", historyList)
    }

    fun getPlayHistory(): ArrayList<VideoBean> {
        val json = SharedPreferenceUtils.readObject("StoredUserSources", "play_history")
        if (json != null) {
            val gson = GsonBuilder().create()
            val ssb = gson.fromJson<ArrayList<VideoBean>>(json,
                object : TypeToken<ArrayList<VideoBean>>() {}.type)
            return ssb
        }
        return arrayListOf()
    }

    fun putPlayHistory(historyList: ArrayList<VideoBean>) {
        SharedPreferenceUtils.writeObject("StoredUserSources", "play_history", historyList)
    }

    fun getCacheRecord(): ArrayList<DownLoadBean> {
        val json = SharedPreferenceUtils.readObject("StoredUserSources", "cache_video")
        if (json != null) {
            val gson = GsonBuilder().create()
            val ssb = gson.fromJson<ArrayList<DownLoadBean>>(json,
                object : TypeToken<ArrayList<DownLoadBean>>() {}.type)
            return ssb
        }
        return arrayListOf()
    }

    fun putCacheRecord(historyList: ArrayList<DownLoadBean>) {
        SharedPreferenceUtils.writeObject("StoredUserSources", "cache_video", historyList)
    }
    fun getIsFirstLoad(): FirstLoadUploadBean {
        val json = SharedPreferenceUtils.readObject("StoredUserSources", "is_first_load")
        if (json != null) {
            val gson = GsonBuilder().create()
            val ssb = gson.fromJson<FirstLoadUploadBean>(json,
                object : TypeToken<FirstLoadUploadBean>() {}.type)
            return ssb
        }
        return FirstLoadUploadBean()
    }

    fun putIsFirstUpdate(bean: FirstLoadUploadBean) {
        SharedPreferenceUtils.writeObject("StoredUserSources", "is_first_load", bean)
    }

    fun putSettingData(ssb: SystemSettingBean) {
        SharedPreferenceUtils.writeObject("StoredUserSources", "user_setting", ssb)
    }
    fun putChannelIdUpdate(s:String?) {
        SharedPreferenceUtils.writeObject("StoredUserSources", "channel_id", s)
    }

    fun getChannelIdData(): String {
        val json = SharedPreferenceUtils.readObject("StoredUserSources", "channel_id")
        if (json != null) {
            val gson = GsonBuilder().create()
            val ssb = gson.fromJson<String>(json,
                object : TypeToken<String>() {}.type)
            return ssb
        }
        return ""
    }

}