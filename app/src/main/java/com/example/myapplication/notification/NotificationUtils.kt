package com.example.myapplication.notification

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.provider.Settings
import android.text.TextUtils
import androidx.core.content.ContextCompat


object NotificationUtils {


    /**
     * 判断是否有通知使用权
     */
     fun notificationListenerEnable(context: Context): Boolean {
        var enable = false
        val packageName: String = context.packageName
        val flat = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
        if (flat != null) {
            enable = flat.contains(packageName)
        }
        return enable
    }
    /**
     * 判断是否有通知使用权2
     */
    fun isNotificationListenersEnabled(context: Context): Boolean {
        val pkgName: String = context.packageName
        val flat = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            for (i in names.indices) {
                val cn = ComponentName.unflattenFromString(names[i])
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.packageName)) {
                        return true
                    }
                }
            }
        }
        return false
    }
    /**
     * 跳转通知使用权设置页面
     */
    fun gotoNotificationAccessSetting(context: Context): Boolean {
        return try {
            val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            true
        } catch (e: ActivityNotFoundException) { //普通情况下找不到的时候需要再特殊处理找一次
            try {
                val intent = Intent()
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val cn = ComponentName(
                    "com.android.settings",
                    "com.android.settings.Settings\$NotificationAccessSettingsActivity"
                )
                intent.component = cn
                intent.putExtra(":settings:show_fragment", "NotificationAccessSettings")
                context.startActivity(intent)
                return true
            } catch (e1: java.lang.Exception) {
                e1.printStackTrace()
            }
            e.printStackTrace()
            false
        }
    }

    /**
     * 就是在退出app后，再次打开，监听不生效，这个时候我们需要做一些处理。在app启动时，我们去重新关闭打开一次监听服务，让它正常工作
     */
    private fun toggleNotificationListenerService(context: Context) {
        val pm = context.packageManager
        pm.setComponentEnabledSetting(ComponentName(context, NotificationListener::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
        )
        pm.setComponentEnabledSetting(ComponentName(context, NotificationListener::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
        )
    }


    /**
     *
     * @param context
     * @param id 图标id
     * @param pkgName 图标所在的包名
     * @return bitmap
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    fun getSmallIcon(context: Context, pkgName: String?, id: Int): Bitmap? {
        var smallIcon: Bitmap? = null
        val remotePkgContext: Context
        try {
            remotePkgContext = context.createPackageContext(pkgName, 0)
            val drawable = remotePkgContext.resources.getDrawable(id,null)
            if (drawable != null) {
                smallIcon = (drawable as BitmapDrawable).bitmap
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return smallIcon
    }

}