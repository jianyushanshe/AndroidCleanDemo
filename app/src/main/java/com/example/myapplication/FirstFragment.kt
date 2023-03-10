package com.example.myapplication

import android.Manifest
import android.app.ActivityManager
import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Context.USAGE_STATS_SERVICE
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentFirstBinding
import com.permissionx.guolindev.PermissionX


/**
 * 内存清理
 * 1.检查有无PACKAGE_USAGE_STATS权限，无则提示用户授权，跳转到权限授权也授权
 * 2.有权限则通过usageStatsManager.queryUsageStats获取指定时间段内运行的应用的信息（该方法无法获取精确的当前后台进程，系统问题无法解决）
 * 3.清理用户选择的应用进程,执行清理操作前后分别通过ActivityManager.MemoryInfo获取内存占用情况并展示
 * 4.当用户在一定时间内做同样的清理操作，直接提示后台很干净无需清理
 *
 * 缓存清理
 * 1.检测有无权限
 * 2.Andd11以下可以通过SAF框架访问data目录，直接删除cache文件夹
 * 3.Android11以上需要提示用户为每个应用单独授权访问data目录
 */
class FirstFragment : Fragment() {
    private val TAG = "ClearMemoryActivity"
    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // requestPermission()
        binding.buttonFirst.setOnClickListener {
            if (!canUsageStats(requireContext())){
                //没有UsageStats权限，跳转授权
                requireActivity().startActivityFromFragment(this, Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),809)
            }else{
                //getAllProcess()//获取一段时间内的运行过的应用信息
                getPackagesInfo()
            }


            //killAll(requireActivity())
//            val am = activity?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//            val infoList = am.runningAppProcesses
//            Log.d(TAG, "runningAppNumber : ${infoList.size}")
//            for (appProcessInfo in infoList){
//                Log.d(TAG, "process name : " + appProcessInfo.processName +"   importance:"+appProcessInfo.importance)
//            }
//            val beforeMem = getAvailMemory(activity!!)
//            Log.d(TAG, "-----------before memory info : $beforeMem")
//            var count = 0
//            if (infoList != null) {
//                for (i in infoList.indices) {
//                    val appProcessInfo = infoList[i]
//                    //importance 该进程的重要程度  分为几个级别，数值越低就越重要。
//                    Log.d(TAG, "process name : " + appProcessInfo.processName +"   importance:"+appProcessInfo.importance)
//
//                    // 一般数值大于RunningAppProcessInfo.IMPORTANCE_SERVICE的进程都长时间没用或者空进程了
//                    // 一般数值大于RunningAppProcessInfo.IMPORTANCE_VISIBLE的进程都是非可见进程，也就是在后台运行着
//                    if (appProcessInfo.importance > RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
//                        val pkgList = appProcessInfo.pkgList
//                        for (j in pkgList.indices) { //pkgList 得到该进程下运行的包名
//                            Log.d(TAG, "It will be killed, package name : " + pkgList[j])
//                            am.killBackgroundProcesses(pkgList[j])
//                            count++
//                        }
//                    }
//                }
//            }
//            val afterMem = getAvailMemory(activity!!)
//            Log.d(TAG, "----------- after memory info : $afterMem")
//            Toast.makeText(activity, "clear " + count + " process, "
//                    + (afterMem - beforeMem) + "M", Toast.LENGTH_LONG).show()


        }
    }

    /**
     * 检测用户是否授权UsageStats权限
     */
    fun canUsageStats(context: Context): Boolean {
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
     * 通过UsageStatsManager获取一段时间内运行的应用信息
     */
    private fun getAllProcess() {
        //Android获取前台运行应用包名UsageStatsManager和ActivityManager结合
        val usageStatsManager: UsageStatsManager = requireContext().getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        //根据beginTime，endTime时间获取
        val usageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,System.currentTimeMillis()-2000L,System.currentTimeMillis())
        for (usageStat in usageStats){
            Log.i(TAG,"运行中的进程包名:"+usageStat.packageName)
        }
    }

    /**
     * 是否是系统应用
     */
    private fun isSystemApp(info: ApplicationInfo): Boolean {
        return info.flags and ApplicationInfo.FLAG_SYSTEM > 0
    }


    /**
     *PackageManager.getApplicationInfo（）
     */
    private fun getPackagesInfo() {
        val manager = requireActivity().getSystemService(
            USAGE_STATS_SERVICE
        ) as UsageStatsManager
        val stats = manager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            PhoneUtils.getTimesMorning().time,
            PhoneUtils.getTimesNight().time
        )

        val pm: PackageManager = requireActivity().packageManager
        for (us in stats) {
            var applicationInfo: ApplicationInfo? = null
            try {
                applicationInfo =
                    pm.getApplicationInfo(us.packageName, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            if (applicationInfo != null) {
                //Log.i(TAG,"运行中的进程包名  应用时长:"+(us.totalTimeInForeground / 1000).toInt().toString() + "应用名称   "+applicationInfo.loadLabel(requireActivity().packageManager).toString())
//1、应用名称 String appName = (String) applicationInfo.loadLabel(packageManager);
//2、应用图标 Drawable appIcon = applicationInfo.loadIcon(packageManager)；
//3、其他信息 比如taskAffinity、processName、theme等
                if (!isSystemApp(applicationInfo)) {//过滤掉com.android  com.google其他的均是三方应用
                    Log.i(TAG,"运行中的进程包名${us.packageName}"+"   应用时长:"+(us.totalTimeInForeground / 1000).toInt().toString() + "   应用名称:"+applicationInfo.loadLabel(requireActivity().packageManager).toString())
                }
            }
        }
    }

    private fun requestPermission() {
        val requestList = ArrayList<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestList.add(Manifest.permission.READ_MEDIA_IMAGES)
            requestList.add(Manifest.permission.READ_MEDIA_AUDIO)
            requestList.add(Manifest.permission.READ_MEDIA_VIDEO)
        }
        if (requestList.isNotEmpty()) {
            PermissionX.init(requireActivity())
                .permissions(requestList)
                .onExplainRequestReason { scope, deniedList ->
                    val message = "PermissionX需要您同意以下权限才能正常使用"
                    scope.showRequestReasonDialog(deniedList, message, "Allow", "Deny")
                }
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        Toast.makeText(activity, "所有申请的权限都已通过", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, "您拒绝了如下权限：$deniedList", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    /*
     * 杀死后台进程
     */
    fun killAll(context: Context) {
        //获取一个ActivityManager 对象
        val activityManager = activity?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        //获取系统中所有正在运行的进程
        val appProcessInfos = activityManager.runningAppProcesses
        for (app in appProcessInfos){
            Log.i(TAG, "当前运行的进程 : ${app.processName}")
        }
        var count = 0 //被杀进程计数
        var nameList = "" //记录被杀死进程的包名
        val beforeMem: Long = getAvailMemory(context) //清理前的可用内存
        Log.i(TAG, "清理前可用内存为 : $beforeMem")
        for (appProcessInfo in appProcessInfos) {
            nameList = ""
            if (appProcessInfo.processName.contains("com.android.system")
                || appProcessInfo.pid == Process.myPid()
            ) //跳过系统 及当前进程
                continue
            val pkNameList = appProcessInfo.pkgList //进程下的所有包名
            for (pkName in pkNameList) {
                activityManager.killBackgroundProcesses("com.magicalstory.cleaner") //杀死该进程
                Log.i(TAG, "清理的包名为 : $pkName")
                count++ //杀死进程的计数+1
                nameList += "  $pkName"
            }
            Log.i(TAG, "$nameList---------------------")
        }
        val afterMem: Long = getAvailMemory(context) //清理后的内存占用
        Toast.makeText(
            context, "杀死 " + count + " 个进程, 释放"
                    + (afterMem - beforeMem) + "内存", Toast.LENGTH_LONG
        ).show()
        Log.i(TAG, "清理后可用内存为 : $afterMem")
        Log.i(TAG, "清理进程数量为 : " + count)
    }



    /*
   * *获取可用内存大小
   */
    private fun getAvailMemory(context: Context): Long {
        // 获取android当前可用内存大小
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val mi = ActivityManager.MemoryInfo()
        am.getMemoryInfo(mi)
        return mi.availMem
    }


//    fun getTotalMemSize(): Long {
//        var size: Long = 0
//        val file = File("/proc/meminfo")
//        try {
//            val buffer = BufferedReader(InputStreamReader(FileInputStream(file)))
//            var memInfo: String = buffer.readLine()
//            val startIndex = memInfo.indexOf(":")
//            val endIndex = memInfo.indexOf("k")
//            memInfo = memInfo.substring(startIndex + 1, endIndex).trim { it <= ' ' }
//            size = memInfo.toLong()
//            size *= 1024
//            buffer.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return size
//    }

//    fun getAviableMemSize(): Long {
//        var size: Long = 0
//        var memInfo:String
//        val file = File("/proc/meminfo")
//        try {
//            val buffer = BufferedReader(InputStreamReader(FileInputStream(file)))
//            var memInfos = String()
//            var i = 0
//            while (buffer.readLine().also { memInfos = it } != null) {
//                i++
//                if (i == 2) {
//                    memInfo = memInfos
//                }
//            }
//            val startIndex: Int = memInfo.indexOf(":")
//            val endIndex: Int = memInfo.indexOf("k")
//            memInfo = memInfo.substring(startIndex + 1, endIndex).trim()
//            size = memInfo.toLong()
//            size *= 1024
//            buffer.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return size
//    }
//    fun getTaskInfos(): List<TaskBean>? {
//        val processInfos: List<AndroidAppProcess> = ProcessManager.getRunningAppProcesses()
//        val taskinfos: MutableList<TaskBean> = ArrayList<TaskBean>()
//        // 遍历运行的程序,并且获取其中的信息
//        for (processInfo in processInfos) {
//            val taskinfo = TaskBean()
//            // 应用程序的包名
//            val packname: String = processInfo.name
//            taskinfo.setPackageName(packname)
//            // 湖区应用程序的内存 信息
//            val memoryInfos: Array<Debug.MemoryInfo> = UIUtils.getActManager()
//                .getProcessMemoryInfo(intArrayOf(processInfo.pid))
//            val memsize = memoryInfos[0].totalPrivateDirty * 1024L
//            taskinfo.setMemSize(memsize)
//            taskinfo.setPackageName(processInfo.getPackageName())
//            try {
//                // 获取应用程序信息
//                val applicationInfo: ApplicationInfo = UIUtils.getPacManager().getApplicationInfo(
//                    packname, 0
//                )
//                val icon = applicationInfo.loadIcon(UIUtils.getPacManager())
//                taskinfo.setIcon(icon)
//                val name = applicationInfo.loadLabel(UIUtils.getPacManager()).toString()
//                taskinfo.setName(name)
//                if (applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
//                    // 用户进程
//                    taskinfo.setUser(true)
//                } else {
//                    // 系统进程
//                    taskinfo.setSystem(true)
//                }
//            } catch (e: PackageManager.NameNotFoundException) {
//                // TODO Auto-generated catch block
//                e.printStackTrace()
//                // 系统内核进程 没有名称
//                taskinfo.setName(packname)
//                val icon: Drawable = UIUtils.getContext().getResources().getDrawable(
//                    R.drawable.ic_launcher
//                )
//                taskinfo.setIcon(icon)
//            }
//            if (taskinfo != null) {
//                taskinfos.add(taskinfo)
//                for (i in taskinfos.indices) {
//                    if (taskinfos[i].getPackageName().equals(Constants.PACKAGE_INFO)) {
//                        taskinfos.removeAt(i)
//                    }
//                }
//            }
//        }
//        return taskinfos
//    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}