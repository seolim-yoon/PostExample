package com.example.postexample.util

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionCheck(val permissionActivity: Activity, val permissions: Array<String>) {

    fun checkPermission() = if(checkGranted(permissions[0]) && checkGranted(permissions[1])) true else false

    fun checkGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(permissionActivity, permission) == PackageManager.PERMISSION_GRANTED
    }
}