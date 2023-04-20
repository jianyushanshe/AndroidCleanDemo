package com.example.myapplication.processes

import android.app.ActivityManager
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Binder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

/**
 * 获取运行中进程信息 适配各版本
 */
object AppProcessSpeedUtil {


    /**
     * 获取后台正在运行的程序
     *
     * @param context
     * @return
     */
    fun getRunningAppList(context: Context): List<AppInfo> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getRunningProcessOreo(context)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getRunningProcessNougat(context)
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            getRunningProcessKitkat(context)
        } else {
            getRunningProcessLollipop(context)
        }
    }

    /**
     * 5.0以下获取运行进程方法
     *
     * @param context
     * @return
     */
    private fun getRunningProcessKitkat(context: Context): List<AppInfo> {
        val list: MutableList<AppInfo> = ArrayList()
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val pm = context.packageManager
        val pus = ProgrameUtils(pm)
        val infoList = am.runningAppProcesses
        if (infoList == null || infoList.size == 0) {
            return list
        }
        for (info in infoList) {
            if (info == null) {
                continue
            }
            val app = pus.getApplicationInfo(info.processName)
            if (app == null || context.packageName == app.packageName) {
                continue
            }
            if (app.flags and ApplicationInfo.FLAG_SYSTEM > 0) {
                continue
            }
            list.add(getAppInfo(app, pm, info.pid, am))
        }
        return list
    }

    /**
     * 5.0 ~ 6.0获取运行进程方法
     *
     * @param context
     * @return
     */
    private fun getRunningProcessLollipop(context: Context): List<AppInfo> {
        val list: MutableList<AppInfo> = ArrayList()
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val pm = context.packageManager
        val pus = ProgrameUtils(pm)
        val infoList = ProcessManager.getRunningAppProcesses()
        if (infoList == null || infoList.size == 0) {
            return list
        }
        for (info in infoList) {
            if (info == null) {
                continue
            }
            val app = pus.getApplicationInfo(info.name)
            if (app == null || context.packageName == app.packageName) {
                continue
            }
            if (app.flags and ApplicationInfo.FLAG_SYSTEM > 0) {
                continue
            }
            list.add(getAppInfo(app, pm, info.pid, am))
        }
        return list
    }

    /**
     * 7.0获取运行进程方法
     *
     * @param context
     * @return
     */
    private fun getRunningProcessNougat(context: Context): List<AppInfo> {
        val list: MutableList<AppInfo> = ArrayList()
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val pm = context.packageManager
        val pus = ProgrameUtils(pm)
        val infoList = am.getRunningServices(Int.MAX_VALUE)
        if (infoList == null || infoList.size == 0) {
            return list
        }
        for (info in infoList) {
            if (info == null) {
                continue
            }
            val app = pus.getApplicationInfo(info.process)
            if (app == null || context.packageName == app.packageName) {
                continue
            }
            if (app.flags and ApplicationInfo.FLAG_SYSTEM > 0) {
                continue
            }
            val appInfo = getAppInfo(app, pm, info.pid, am)
            if (!list.contains(appInfo)) {
                list.add(appInfo)
            }
        }
        return list
    }

    /**
     * 8.0以上获取运行进程方法
     *
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun getRunningProcessOreo(context: Context): List<AppInfo> {
        val list: MutableList<AppInfo> = ArrayList()
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val pm = context.packageManager
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        try {
            val endTime = System.currentTimeMillis() //结束时间
            val events = usageStatsManager.queryEvents(endTime - 2 * 60 * 1000, endTime)
            val localEvent = UsageEvents.Event()
            while (events.hasNextEvent()) {
                events.getNextEvent(localEvent)
                val app = pm.getApplicationInfo(localEvent.packageName, PackageManager.GET_META_DATA)
                if (context.packageName == app.packageName) {
                    continue
                }
                if (app.flags and ApplicationInfo.FLAG_SYSTEM > 0) {
                    continue
                }
                val appInfo = AppInfo()
                appInfo.icon = app.loadIcon(pm)
                appInfo.label = app.loadLabel(pm).toString()
                appInfo.packageName = app.packageName
                // 计算所占内存大小
                val myMempid = intArrayOf(Binder.getCallingPid())
                val memoryInfo = am.getProcessMemoryInfo(myMempid)
                val memSize = memoryInfo[0].dalvikPrivateDirty * 1024.0 * 2
                appInfo.memorySize = memSize.toLong()
                if (!list.contains(appInfo)) {
                    list.add(appInfo)
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return list
    }



    /**
     * 构建对象
     *
     * @param app
     * @param pm
     * @param pid
     * @param am
     * @return
     */
    private fun getAppInfo(
        app: ApplicationInfo,
        pm: PackageManager,
        pid: Int,
        am: ActivityManager
    ): AppInfo {
        val ent = AppInfo()
        ent.icon = app.loadIcon(pm)
        ent.label = app.loadLabel(pm).toString()
        ent.packageName = app.packageName
        // 计算所占内存大小
        val myMempid = intArrayOf(pid)
        val memoryInfo = am.getProcessMemoryInfo(myMempid)
        val memSize = memoryInfo[0].dalvikPrivateDirty * 1024.0 * 2
        ent.memorySize = memSize.toLong()
        return ent
    }
}