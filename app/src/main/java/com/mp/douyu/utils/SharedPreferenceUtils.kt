package com.mp.douyu.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.mp.douyu.MApplication.Companion.getApplicationInstances

/**
 * Created by HT on 2017/9/27.
 */
object SharedPreferenceUtils {
    fun <T> readObject(fileName: String?, key: String?, type: Class<T>?): T? {
        val preferences: SharedPreferences =
            getApplicationInstances.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        val json = preferences.getString(key, null)
        return if (json == null) {
            null
        } else {
            val gson = GsonBuilder().create()
            gson.fromJson(json, type)
        }
    }

    fun readObject(fileName: String?, key: String?): String? {
        val preferences: SharedPreferences =
            getApplicationInstances.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        return preferences.getString(key, null)
    }

    fun <T> writeObject(fileName: String?, key: String?, `object`: T) {
        val preferences: SharedPreferences =
            getApplicationInstances.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        preferences.edit().putString(key, GsonBuilder().create().toJson(`object`)).apply()
    }

    operator fun get(fileName: String?): SharedPreferences {
        return getApplicationInstances
            .getSharedPreferences(fileName, Context.MODE_PRIVATE)
    }
}