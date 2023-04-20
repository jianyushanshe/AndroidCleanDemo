package com.example.myapplication.cache

import android.Manifest
import android.app.AppOpsManager
import android.app.usage.StorageStats
import android.app.usage.StorageStatsManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.IPackageDataObserver
import android.content.pm.IPackageStatsObserver
import android.content.pm.PackageManager
import android.content.pm.PackageStats
import android.net.Uri
import android.os.Build
import android.os.Process
import android.os.RemoteException
import android.os.storage.StorageManager
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.IOException
import java.lang.reflect.Method
import java.math.BigInteger
import java.text.DecimalFormat
import java.util.*


object CacheUtils {
    fun getAppCacheSize(context: Context,packageName:String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!canUsageStats(context)){
                //没有UsageStats权限，跳转授权
                val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                //通过以下设置跳转到自己应用的权限开通页面，而不是权限列表页面
                intent.data = Uri.parse(String.format("package:%s",context.packageName))
                context.startActivity(intent)
            }else{
                getAppSizeO(context,packageName)
            }
        } else {
            getCacheInfo(context,packageName)
        }
    }
    /**
     * 检测用户是否授权UsageStats权限
     */
    private fun canUsageStats(context: Context): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        var mode = 0
        mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.packageName
            )
        } else {
            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.packageName
            )
        }
        return if (mode == AppOpsManager.MODE_DEFAULT && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED
        } else {
            mode == AppOpsManager.MODE_ALLOWED
        }
    }
    /**
     * 需要权限PACKAGE_USAGE_STATS，可以获取到应用的缓存大小
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    fun getAppSizeO(context: Context,packageName:String) {
        val storageStatsManager = context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
        val storageManager = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        //获取所有应用的StorageVolume列表
        val storageVolumes = storageManager.storageVolumes
        for (item in storageVolumes) {
            val uuidStr = item.uuid
            val uuid: UUID = getUuid(uuidStr)
            val uid = getUid(context,packageName )
            //通过包名获取uid
            var storageStats: StorageStats
            try {
                storageStats = storageStatsManager.queryStatsForUid(uuid, uid)
                Log.i("获取缓存", "包名 $packageName     缓存大小${size(storageStats.cacheBytes)}")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun getUuid(uu: String?): UUID {
        var uuid: UUID
        val uuStr = StorageManager.UUID_DEFAULT.toString().replace("-", "")
        if (TextUtils.isEmpty(uu)) {
            uuid = getUU(uuStr)
        } else {
            try {
                uuid = getUU(uu!!.replace("-", ""))
            } catch (e: Exception) {
                Log.i("CacheUtils", "uuid: " + e.message)
                uuid = getUU(uuStr)
                e.fillInStackTrace()
            }
        }
        return uuid
    }

    private fun getUU(uuStr: String): UUID {
        return UUID(
            BigInteger(uuStr.substring(0, 16), 16).toLong(),
            BigInteger(uuStr.substring(16), 16).toLong()
        )
    }

    fun getUid(context: Context, pakName: String): Int {
        try {
            return context.packageManager
                .getApplicationInfo(pakName, PackageManager.GET_META_DATA).uid
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return -1
    }

    fun getCacheInfo(context: Context,packageName:String) {
        try {
            val method: Method = context.packageManager.javaClass.getMethod(
                "getPackageSizeInfo", *arrayOf<Class<*>>(String::class.java, IPackageStatsObserver::class.java)
            )
            // 调用 getPackageSizeInfo 方法，需要两个参数：1、需要检测的应用包名；2、回调
            method.isAccessible = true
            method.invoke(context.packageManager, packageName, Process.myUid() / 100000 ,object : IPackageStatsObserver.Stub() {
                override fun onGetStatsCompleted(pStats: PackageStats, succeeded: Boolean) {
                    Log.i("获取缓存", "包名 $packageName     缓存大小${size(pStats.cacheSize+pStats.codeSize)}")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

     fun deleteAllCache(context: Context) {
         try {
             val pm = context.packageManager
             val method = PackageManager::class.java.getMethod("freeStorageAndNotify",Long::class.java,IPackageDataObserver::class.java)
             method.isAccessible = true
             method.invoke(pm, Long.MAX_VALUE, MyPackageDataObserver())
         } catch (e: Exception) {
             e.printStackTrace()
         }
     }

    private class MyPackageDataObserver : IPackageDataObserver.Stub() {
        override fun onRemoveCompleted(packageName: String?, succeeded: Boolean) {
            Log.i("获取缓存", "缓存清理完成")

        }
    }

    /**
     * 将文件大小显示为GB,MB等形式
     */
    fun size(size: Long): String {
        return if (size / (1024 * 1024 * 1024) > 0) {
            val tmpSize = size.toFloat() / (1024 * 1024 * 1024).toFloat()
            val df = DecimalFormat("#.##")
            df.format(tmpSize) + "GB"
        } else if (size / (1024 * 1024) > 0) {
            val tmpSize = size.toFloat() / (1024 * 1024).toFloat()
            val df = DecimalFormat("#.##")
            df.format(tmpSize) + "MB"
        } else if (size / 1024 > 0) {
            (size / 1024).toString() + "KB"
        } else size.toString() + "B"
    }
}