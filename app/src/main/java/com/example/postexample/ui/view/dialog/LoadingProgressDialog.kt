package com.example.postexample.ui.view.dialog

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.example.postexample.R

class LoadingProgressDialog(context: Context): Dialog(context) {
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_progress)
    }
}