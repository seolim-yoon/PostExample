package com.example.postexample.util

object LoginPreference {

    fun getAutoLogin() = PreferenceManager.getBoolean("auto_login", false)

    fun setAutoLogin(isAutoLogin: Boolean) {
        PreferenceManager.setBoolean("auto_login", isAutoLogin)
    }

    fun getUserName() = PreferenceManager.getString("user_name", "-")
    fun getUserEmail() = PreferenceManager.getString("user_email", "-")

    fun setUserPreference(name: String, email: String, pw: String) {
        PreferenceManager.setBoolean("auto_login", true)
        PreferenceManager.setString("user_name", name)
        PreferenceManager.setString("user_email", email)
        PreferenceManager.setString("user_pw", pw)
    }
}