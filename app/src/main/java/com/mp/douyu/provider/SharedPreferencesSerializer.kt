package com.mp.douyu.provider

import android.os.Build
import androidx.annotation.RequiresApi
import com.mp.douyu.utils.SharedPreferenceUtils


abstract class SharedPreferencesSerializer : Serializer {


    abstract val fileName: String

    override fun save(key: String, value: Any?) {
        SharedPreferenceUtils.get(fileName).edit().apply {
            when(value) {
                is Int -> putInt(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                is Boolean -> putBoolean(key, value)
                is String -> putString(key, value)
            }
           apply()
        }
    }

    override fun get(key: String): Any? {
        return all[key]
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun saveAll(map: Map<String, *>) {
        map.forEach {key, value ->
            save(key, value)
        }
    }

    override fun getAll(): Map<String, Any?> {
        return SharedPreferenceUtils.get(fileName).all
    }

    override fun clear() {
        SharedPreferenceUtils.get(fileName).edit().clear().apply()
    }
}
