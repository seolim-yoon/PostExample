package com.example.postexample.util

object LoginPreference {

    fun getAutoLogin() = PreferenceManager.getBoolean("auto_login", false)

    fun setAutoLogin(isAutoLogin: Boolean) {
        PreferenceManager.setBoolean("auto_login", isAutoLogin)
    }

    fun setUserPreference(email: String, pw: String) {
        PreferenceManager.setBoolean("auto_login", true)
        PreferenceManager.setString("user_email", email)
        PreferenceManager.setString("user_pw", pw)
    }
}