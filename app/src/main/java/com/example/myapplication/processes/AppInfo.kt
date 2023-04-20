package com.example.myapplication.processes

import android.graphics.ColorSpace
import android.graphics.drawable.Drawable

class AppInfo{
    var uid = 0
    var label: String? = null//应用名称
    var packageName: String? = null//应用包名
    var icon: Drawable? = null//应用icon
    var permissions = mutableListOf<String>()
    var isSystemApp = false
    var cachePath = ""
    var cacheSize = 0L
    var memorySize = 0L
}