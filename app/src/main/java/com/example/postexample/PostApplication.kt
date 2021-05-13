package com.example.postexample

import android.app.Application
import com.example.postexample.util.preference.PreferenceManager

class PostApplication: Application() {
     override fun onCreate() {
        super.onCreate()
        PreferenceManager.init(this)
    }
}