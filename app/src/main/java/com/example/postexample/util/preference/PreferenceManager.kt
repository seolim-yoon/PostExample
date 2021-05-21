package com.example.postexample.util.preference

import android.content.Context
import android.content.SharedPreferences

object PreferenceManager {
    private lateinit var preference: SharedPreferences

    fun init(context: Context) {
        preference = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
    }

    fun getString(key: String, value: String) : String =
        preference.getString(key, value).toString()

    fun setString(key: String, value: String) {
        preference.edit()
            .putString(key, value)
            .apply()
    }

    fun getBoolean(key: String, value: Boolean) : Boolean =
        preference.getBoolean(key, value)

    fun setBoolean(key: String, value: Boolean) {
        preference.edit()
            .putBoolean(key, value)
            .apply()
    }
}