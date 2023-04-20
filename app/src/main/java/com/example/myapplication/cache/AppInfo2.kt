package com.example.myapplication.cache

import android.content.pm.PermissionInfo
import android.graphics.drawable.Drawable




class AppInfo2 {
    var uid = 0
    var label: String? = null//应用名称
    var packageName: String? = null//应用包名
    var icon: Drawable? = null//应用icon
    var permissions = mutableListOf<String>()
    var isSystemApp = false
    var cacheSize = 0L
}