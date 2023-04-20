package com.example.myapplication

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AppOpsManager
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Context.USAGE_STATS_SERVICE
import android.content.Intent
import android.content.pm.*
import android.net.Uri
import android.os.*
import android.os.Build.VERSION.SDK_INT
import android.provider.DocumentsContract
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import com.example.myapplication.battery.BatteryUtil
import com.example.myapplication.cache.CacheUtils
import com.example.myapplication.cache.CleanRDataUtil
import com.example.myapplication.cache.FileUriUtils
import com.example.myapplication.databinding.FragmentFirstBinding
import com.example.myapplication.image.ModifyExif
import com.example.myapplication.notification.NotificationUtils
import com.example.myapplication.processes.AppInfo
import com.example.myapplication.processes.AppProcessSpeedUtil
import com.permissionx.guolindev.PermissionX
import java.io.File
import java.lang.reflect.Method
import java.text.DecimalFormat
import java.util.*


/**
 * 内存清理
 * Android5.0通过ActivityManager.getRunningAppProcesses()该方法获取前台运行应用进程
 * Android5.0以上通过usageStatsManager.queryUsageStats获取最近活动过的应用
 * 1.检查有无PACKAGE_USAGE_STATS权限，无则提示用户授权，跳转到权限授权也授权
 * 2.有权限则通过usageStatsManager.queryUsageStats获取指定时间段内运行的应用的信息（该方法无法获取精确的当前后台进程，系统问题无法解决）
 * 3.清理用户选择的应用进程,执行清理操作前后分别通过ActivityManager.MemoryInfo获取内存占用情况并展示
 * 4.当用户在一定时间内做同样的清理操作，直接提示后台很干净无需清理
 *
 *
 *  ActivityManager activityManager = (ActivityManager)this.a.getSystemService("activity");
for (PackageInfo packageInfo : this.a.getPackageManager().getInstalledPackages(0)) {
if (!str2.contains(packageInfo.packageName) && !str1.contains(packageInfo.packageName) && activityManager != null)
activityManager.killBackgroundProcesses(packageInfo.packageName);
}
 *
 * 缓存清理
 * 1.检测有无权限
 * 2.Android11以下可以申请【所有文件管理】的权限访问data目录
 * 3.Android11，12除了申请【所有文件管理】权限外，需要再申请一次【data目录的单独访问权限】，通过SAF（Android Storage Access Framework）框架访问data目录，直接删除cache文件夹
 * 3.Android13以上需要用户为每个应用单独授权访问data目录
 *
 *
 * 文件加密：https://blog.csdn.net/wonengxing/article/details/9000696?spm=1001.2101.3001.6650.4&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-4-9000696-blog-106589643.pc_relevant_aa2&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-4-9000696-blog-106589643.pc_relevant_aa2&utm_relevant_index=5
 *
 * https://github.com/tiann/Leoric  保活方案
 * https://github.com/jaredrummler/AndroidProcesses 获取运行中进程适用于6.0
 *
 * 复杂动画实现Lottie是Airbnb开源的一套动画渲染库，可用于Web、Android，iOS。它体积小，质量高，可以实现非常漂亮的动画。
 * https://lottiefiles.com/
 * http://airbnb.io/lottie/#/
 * https://github.com/airbnb
 * https://github.com/airbnb/lottie-android
 * https://funletu.com/137/.html
 */
class FirstFragment : Fragment() {
    private val TAG = "ClearMemoryActivity"
    private var _binding: FragmentFirstBinding? = null
    private var processPackageNames  = mutableListOf<String>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SuspiciousIndentation", "UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // 杀死进程
        binding.buttonKill.setOnClickListener {
            for (item in processPackageNames){
                killPackageProcesses(item)
            }
        }
        //获取所有最近运行的进程信息
        binding.buttonGetAllProcesses.setOnClickListener {
            if (!canUsageStats(requireContext())){
                //没有UsageStats权限，跳转授权
                val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                //通过以下设置跳转到自己应用的权限开通页面，而不是权限列表页面
                intent.data = Uri.parse(String.format("package:%s", requireActivity().packageName))
               this.startActivity(intent)
            }else{
                //getForegroundPackageName()//获取当前在运行的进程，这个方法最准确（推荐）
                val processes =   AppProcessSpeedUtil.getRunningAppList(requireContext())
                Log.i(TAG,"正在运行的进程数量   ${processes.size}")
                for (app in processes){
                    Log.i(TAG,"正在运行的进程   Name:${app.label}   Package:${app.packageName}")
                }
            }
        }

        binding.buttonGetAllProcessesNoPermission.setOnClickListener {
            val processes =   AppProcessSpeedUtil.getRunningAppList(requireContext())
            Log.i(TAG,"正在运行的进程数量   ${processes.size}")
            for (app in processes){
                Log.i(TAG,"正在运行的进程   Name:${app.label}   Package:${app.packageName}")
            }
        }


        //获取管理所有文件的权限
        binding.buttonGetFileManagerPermission.setOnClickListener {
           if (!checkFileManagerPermission()){
               requestFileManagerPermission()
           }else{
               Toast.makeText(activity, "已拥有文件管理权限", Toast.LENGTH_SHORT).show()
           }
        }


        //通过PermissionX获取相关权限
        binding.buttonPermissionX.setOnClickListener {
            requestPermission()
        }


        //获取已安装应用列表 （不需要权限，可以利用这个特性在应用安装没有权限的时候显示正在运行的应用，当用户手动关闭进程后，过滤掉关闭的应用）
        binding.buttonGetAllApps.setOnClickListener {
            getAllInstalledAppInfo(requireContext(),false)
        }


        //获取应用的缓存大小，需要权限PACKAGE_USAGE_STATS
        binding.buttonGetAppCacheWithStoragestats.setOnClickListener {
            CacheUtils.getAppCacheSize(requireContext(), "com.google.android.youtube")
        }

        //清理所有应用的缓存
        binding.buttonCleanAllCache.setOnClickListener {
            CacheUtils.deleteAllCache(requireContext())
        }



        val requestAccessDataDirLauncher = registerForActivityResult(RequestAccessAppDataDir()) {
            if (it != null) {
                //保存访问data目录权限
                requireActivity().contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }
        //android11\12获取data目录权限
        //特别注意：要在activityResult回调中保存data目录授权状态，否则每次检测授权都是未授权
        binding.buttonGetDataPermission.setOnClickListener {
            if (!FileUriUtils.isGrant(requireContext())){
                //FileUriUtils.startForRoot(requireActivity(),888)
                CleanRDataUtil.startForDataPermission(requireActivity(),888)
                //requestAccessDataDirLauncher.launch("")
            }else{
                Toast.makeText(activity, "已获取data目录访问权限", Toast.LENGTH_SHORT).show()
            }
        }
        //android11\12删除单个应用的data/cache目录权限，该操作需要对data目录授权
        binding.buttonDeleteAppCache1112.setOnClickListener {
            //即使获取了data目录访问权限，系统仍然不允许访问已授权目录的子目录文件，需要通过子文件path的方式来获取子文件
            //删除操作可以直接删除uri效率高，或者Document.delete()效率慢
           val uri =  FileUriUtils.getDocumentFileFromPath(requireContext(),"Android/data/com.android.keepgreen.debug/cache")?.uri
            Toast.makeText(activity,  "删除YouTube cache目录 = ${CleanRDataUtil.deleteCurDocument(requireContext(),uri!!)}", Toast.LENGTH_SHORT).show()
        //遍历data目录获取子目录，因为已授权data目录所以可以遍历，但是无法遍历未授权的应用目录s
//            val documentFile = DocumentFile.fromTreeUri(requireContext(), Uri.parse(FileUriUtils.changeToUri3("Android/data")))
//            for (file in  documentFile!!.listFiles()){
//                Log.i(TAG, "YouTube目录子文件夹  data   : ${file.name}   ${file.uri}")
//                if (file.name=="com.google.android.youtube"){
//                    val documentFile1 = DocumentFile.fromTreeUri(requireContext(), file.uri)
//                    for (file1 in documentFile1!!.listFiles()){
//                        Log.i(TAG, "YouTube目录子文件夹 youtube  : ${file1.name}   ${file1.uri}")
//                        if (file1.name=="cache"){
//                            Toast.makeText(activity,  "删除YouTube cache目录 = ${CleanRDataUtil.deleteCurDocument(requireContext(),file1.uri)}", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }
//            }


        }


        //android13获取单个应用的data目录权限（操作应用data目录，不需要所有文件管理权限）
        ///请求访问目标app的data目录

        binding.buttonGetDataPermissionApp.setOnClickListener {
            //打开指定包名的data文件夹
           requestAccessDataDirLauncher.launch("com.google.android.youtube")
        }

        //android13检测单个应用的data/cache目录权限
        binding.buttonCheckDataPermissionApp.setOnClickListener {
            Toast.makeText(activity,  "是否可以访问YouTube data目录 = ${CleanRDataUtil.canReadDataDir(requireContext(),"com.google.android.youtube")}", Toast.LENGTH_SHORT).show()
        }
        //android13删除单个应用的data/cache目录权限
        binding.buttonDeleteAppCache.setOnClickListener {
            CleanRDataUtil.getTargetFolderUri(requireContext(),"com.google.android.youtube","cache")?.let {
                Toast.makeText(activity,  "删除YouTube cache目录 = ${CleanRDataUtil.deleteCurDocument(requireContext(),it)}", Toast.LENGTH_SHORT).show()
            }        }


        //获取电池信息
        binding.buttonGetBatteryInfo.setOnClickListener {
            Log.i(TAG,"电池信息 容量：${BatteryUtil.getBatteryCapacity(requireContext())}")
            BatteryUtil.receiverBatteryOtherInfo(requireContext())
        }



        //修改照片信息
        binding.buttonResetImageInfo.setOnClickListener {
            val path = "/sdcard/xian.jpeg"
            Log.i(TAG,"照片信息 路径 $path")
            path.let {
                ModifyExif.setExif(it,2.23,4.33,"223:20")
            }
        }
        binding.buttonGetImageInfo.setOnClickListener {
            val path = "/sdcard/xian.jpeg"
            Log.i(TAG,"照片信息  路径 $path")
            path.let {
                ModifyExif.getExif(it)
            }
        }


        //获取通知管理权限
        binding.buttonGetNotificationManagerPermission.setOnClickListener {
            if (!NotificationUtils.notificationListenerEnable(requireActivity())){
                    NotificationUtils.gotoNotificationAccessSetting(requireContext())
            }else{
                Toast.makeText(requireContext(),"已获取通知管理权限",Toast.LENGTH_SHORT).show()
            }
        }

        //跳转导航页面
        binding.buttonNavigation.setOnClickListener {
            openActivity<MainActivity3>(requireContext())
        }
    }








    /**
     * 检测所有文件管理权限
     */
    private fun checkFileManagerPermission(): Boolean {
        return if (SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val result =
                ContextCompat.checkSelfPermission(requireContext(), READ_EXTERNAL_STORAGE)
            val result1 =
                ContextCompat.checkSelfPermission(requireContext(), WRITE_EXTERNAL_STORAGE)
            result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * 请求所有文件管理权限（除过data目录）
     */
    private fun requestFileManagerPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                //通过以下设置跳转到自己应用的权限开通页面，而不是权限列表页面
                intent.data = Uri.parse(String.format("package:%s", requireActivity().packageName))
                requireActivity().startActivityFromFragment(this,intent, 2296)
            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                requireActivity().startActivityFromFragment(this,intent, 2296)
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf<String>(
                    WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE
                ), 2296
            )
        }
    }

    /**
     * 推荐此方法最准确
     * 获取当前在运行的进程
     * 这个方法最准确
     */
    private fun getForegroundPackageName() {
        //Get the app record in the last month
        val endTime = System.currentTimeMillis() //结束时间
        val usageStatsManager = requireActivity().getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val usageEvents = usageStatsManager.queryEvents(endTime-60*1000, endTime)//这里的时间可以任意调节获取哪个时间段的记录，时间越短获取当前的越精确
        val event = UsageEvents.Event()
        var packageName: String? = null
        processPackageNames.clear()
        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event)
            if (event.eventType == UsageEvents.Event.ACTIVITY_STOPPED||event.eventType == UsageEvents.Event.ACTIVITY_PAUSED) {
                packageName = event.packageName
                if (!packageName.contains("android")&&!processPackageNames.contains(packageName)){
                    processPackageNames.add(packageName)
                    Log.i(TAG, "正在运行的进程 : $packageName")
                }
            }
        }

    }


    /**
     * 获取手机已安装应用列表getInstalledPackages
     * @param ctx
     * @param isFilterSystem 是否过滤系统应用
     * @return
     */
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("UsableSpace")
    private fun getAllInstalledAppInfo(context: Context, isFilterSystem: Boolean): MutableList<AppInfo> {
        val myDataObserver = MyDataObserver()
         val appBeanList = mutableListOf<AppInfo>()
        var bean: AppInfo? = null
        val list = mutableListOf<PackageInfo>()
        val packageManager = context.packageManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            list.addAll(packageManager.getInstalledPackages(
                PackageManager.PackageInfoFlags.of(
                    PackageManager.GET_PERMISSIONS.toLong()
                )))
        }else{
            list.addAll(packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS))
        }
        for (packageInfo in list) {
            bean = AppInfo()
            bean.icon=packageInfo.applicationInfo.loadIcon(packageManager)
            bean.label = packageInfo.applicationInfo.loadLabel(packageManager).toString()
            bean.packageName=packageInfo.packageName
            if (bean.packageName == context.packageName){
                continue
            }
            bean.isSystemApp = packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
            bean.permissions.clear()
            packageInfo.requestedPermissions?.let { bean.permissions.addAll(it) }//获取已安装应用权限
            val appContext: Context = requireContext().createPackageContext(bean.packageName, Context.CONTEXT_INCLUDE_CODE or Context.CONTEXT_IGNORE_SECURITY)
            bean.cachePath = appContext.dataDir.absolutePath
            try {
                val myUserId = UserHandle::class.java.getDeclaredMethod("myUserId")
                val userID = myUserId.invoke(packageManager,null)
                // 调用 getPackageSizeInfo 方法，需要两个参数：1、需要检测的应用包名；2、回调
                val getPackageSizeInfo:Method = packageManager.javaClass.getDeclaredMethod("getPackageSizeInfo", String::class.java,Int::class.java, IPackageStatsObserver::class.java)
                getPackageSizeInfo.isAccessible = true
                getPackageSizeInfo.invoke(packageManager, bean.packageName, Process.myUid() / 100000, MyDataObserver())
            } catch (e:java.lang.Exception) {
                e.printStackTrace()
            }
            Log.i("获取已安装的应用","应用包名：${bean.packageName}  应用名称：${bean.label}  是否系统应用 ${bean.isSystemApp}  缓存路径大小:${bean.cachePath}   ${bean.cacheSize}")
            if (bean.isSystemApp) {
                if (!isFilterSystem){
                    appBeanList.add(bean)
                }
            } else {
                appBeanList.add(bean)
            }
        }
        return appBeanList
    }
      class MyDataObserver : IPackageStatsObserver.Stub(){
          override fun onGetStatsCompleted(pStats: PackageStats, succeeded: Boolean) {
              Log.i("获取缓存大小","应用包名：${pStats.packageName}   缓存大小:${pStats.cacheSize}  ${pStats.dataSize}  ${pStats.codeSize} ")
          }
      }

    /**
     * 根据不同应用的context获取缓存大小
     */
    fun getTotalCacheSize(context: Context): Long {
        var externalCacheSize = 0L
        val externalFileSize = 0L
        var internalCacheSize = 0L
        val internalFileSize = 0L
        val down = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        externalCacheSize = getFolderSize(down)
        internalCacheSize = getFolderSize(context.cacheDir)
        //        internalFileSize  = getFolderSize(context.getFilesDir());
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            externalCacheSize = getFolderSize(context.externalCacheDir)
            //            externalFileSize  = getFolderSize(context.getExternalFilesDir(""));
        }
        return internalCacheSize+externalCacheSize
    }



    /**
     * 获取文件
     */
    fun getFolderSize(file: File?): Long {
        var size: Long = 0
        if (file != null) {
            val fileList: Array<File> = file.listFiles() as Array<File>
            if (!fileList.isNullOrEmpty()) {
                for (value in fileList) {
                    // 如果下面还有文件
                    size += if (value.isDirectory) {
                        getFolderSize(value)
                    } else {
                        value.length()
                    }
                }
            }
        }
        return size
    }

    /**
     * 将文件大小显示为GB,MB等形式
     */
    fun size(size: Long): String? {
        return if (size / (1024 * 1024 * 1024) > 0) {
            val tmpSize = size.toFloat() / (1024 * 1024 * 1024).toFloat()
            val df = DecimalFormat("#.##")
            "" + df.format(tmpSize) + "GB"
        } else if (size / (1024 * 1024) > 0) {
            val tmpSize = size.toFloat() / (1024 * 1024).toFloat()
            val df = DecimalFormat("#.##")
            "" + df.format(tmpSize) + "MB"
        } else if (size / 1024 > 0) {
            "" + (size / 1024) + "KB"
        } else "" + size + "B"
    }
    /**
     * 获取应用的权限
     */
    fun getAppAllPermissions(context: Context, packageName: String): Array<String?>? {
        try {
            //包管理操作管理类
            val pm = context.packageManager
            val packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
            //获取到所有的权限
            return packageInfo.requestedPermissions
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 检测用户是否授权UsageStats权限
     */
    private fun canUsageStats(context: Context): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        var mode = 0
        mode = if (SDK_INT >= Build.VERSION_CODES.Q) {
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
        return if (mode == AppOpsManager.MODE_DEFAULT && SDK_INT >= Build.VERSION_CODES.M) {
            context.checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED
        } else {
            mode == AppOpsManager.MODE_ALLOWED
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
                //    ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                //        b1.e = this.c.x.getApplicationIcon(applicationInfo);
                //        b1.l = this.c.x.getApplicationLabel(applicationInfo).toString();
                //        b1.m = usageStats.getPackageName();
                //        b1.o = usageStats.getLastTimeUsed();
                //Log.i(TAG,"运行中的进程包名  应用时长:"+(us.totalTimeInForeground / 1000).toInt().toString() + "应用名称   "+applicationInfo.loadLabel(requireActivity().packageManager).toString())
//1、应用名称 String appName = (String) applicationInfo.loadLabel(packageManager);
//2、应用图标 Drawable appIcon = applicationInfo.loadIcon(packageManager)；
//3、其他信息 比如taskAffinity、processName、theme等
                Log.i(TAG,"运行中的进程包名${us.packageName}"+"   应用时长:"+(us.totalTimeInForeground / 1000).toInt().toString() + "   应用名称:"+applicationInfo.loadLabel(requireActivity().packageManager).toString())

                if (!isSystemApp(applicationInfo)) {//过滤掉com.android  com.google其他的均是三方应用
                    Log.i(TAG,"运行中的进程包名${us.packageName}"+"   应用时长:"+(us.totalTimeInForeground / 1000).toInt().toString() + "   应用名称:"+applicationInfo.loadLabel(requireActivity().packageManager).toString())
                }
            }
        }
    }
    private fun killPackageProcesses(packageName :String){
        val beforeMem: Long = getAvailMemory(requireContext()) //清理前的可用内存
        val activityManager = activity?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.killBackgroundProcesses(packageName) //杀死该进程
        val afterMem: Long = getAvailMemory(requireContext()) //清理后的内存占用
        Log.i(TAG, "当前运行在运行的进程包名   杀死后台进程释放内存 : $packageName   释放${(afterMem - beforeMem)} 内存")
    }



    private fun requestPermission() {
        val requestList = ArrayList<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestList.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE )
//            requestList.add(Manifest.permission.READ_MEDIA_AUDIO)
//            requestList.add(Manifest.permission.READ_MEDIA_VIDEO)
        }
        if (requestList.isNotEmpty()) {
            PermissionX.init(requireActivity())
                .permissions(requestList)
                .onExplainRequestReason { scope, deniedList ->
                    val message = "需要获取所有文件管理权限才可以正常运行"
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
   * *获取可用内存大小
   */
    private fun getAvailMemory(context: Context): Long {
        // 获取android当前可用内存大小
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val mi = ActivityManager.MemoryInfo()
        am.getMemoryInfo(mi)
        return mi.availMem
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

/**
 * 请求data目录权限所需要的权限类
 */
class RequestAccessAppDataDir : ActivityResultContract<String, Uri?>() {
    override fun createIntent(context: Context, input: String): Intent {
        val dirUri = CleanRDataUtil.createAppDataDirUri(input)
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        intent.flags = (Intent.FLAG_GRANT_READ_URI_PERMISSION
                or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                or Intent.FLAG_GRANT_PREFIX_URI_PERMISSION)
        val documentFile = DocumentFile.fromTreeUri(context.applicationContext, dirUri)!!
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, documentFile.uri)
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return intent?.data
    }

}