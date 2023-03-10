//package com.example.myapplication.memory;
//
//import android.app.ActivityManager;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.os.Debug;
//import android.text.TextUtils;
//
//
//import com.example.myapplication.ContextCheckUtil;
//import com.example.myapplication.ThreadUtil;
//import com.example.myapplication.TimeController;
//import com.example.myapplication.UnitConverter;
//import com.example.myapplication.cache.AbsOptimizable;
//import com.example.myapplication.cache.IExecListener;
//import com.example.myapplication.cache.IScanListener;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//
//public class MemoryManager extends AbsOptimizable<AppClearInfo, Long> {
//    private static volatile MemoryManager e;
//
//    List<OptListener> b = new ArrayList<OptListener>();
//
//    private IList c;
//
//    private IList d;
//
//    private MemoryManager(Context paramContext) {
//        this.c = (IList)new WhiteList(paramContext.getApplicationContext());
//        new BlackList(paramContext.getApplicationContext());
//        this.d = (IList)new IgnoreList(paramContext);
//    }
//
//    private final boolean m(String paramString) {
//        if (!TextUtils.isEmpty(paramString)) {
//            IList iList = this.c;
//            if (iList != null)
//                return iList.a(paramString);
//        }
//        return false;
//    }
//
//    public static MemoryManager p(Context paramContext) {
//        // Byte code:
//        //   0: getstatic com/amber/lib/systemcleaner/module/memory/MemoryManager.e : Lcom/amber/lib/systemcleaner/module/memory/MemoryManager;
//        //   3: ifnonnull -> 40
//        //   6: ldc com/amber/lib/systemcleaner/module/memory/MemoryManager
//        //   8: monitorenter
//        //   9: getstatic com/amber/lib/systemcleaner/module/memory/MemoryManager.e : Lcom/amber/lib/systemcleaner/module/memory/MemoryManager;
//        //   12: ifnonnull -> 28
//        //   15: new com/amber/lib/systemcleaner/module/memory/MemoryManager
//        //   18: astore_1
//        //   19: aload_1
//        //   20: aload_0
//        //   21: invokespecial <init> : (Landroid/content/Context;)V
//        //   24: aload_1
//        //   25: putstatic com/amber/lib/systemcleaner/module/memory/MemoryManager.e : Lcom/amber/lib/systemcleaner/module/memory/MemoryManager;
//        //   28: ldc com/amber/lib/systemcleaner/module/memory/MemoryManager
//        //   30: monitorexit
//        //   31: goto -> 40
//        //   34: astore_0
//        //   35: ldc com/amber/lib/systemcleaner/module/memory/MemoryManager
//        //   37: monitorexit
//        //   38: aload_0
//        //   39: athrow
//        //   40: getstatic com/amber/lib/systemcleaner/module/memory/MemoryManager.e : Lcom/amber/lib/systemcleaner/module/memory/MemoryManager;
//        //   43: areturn
//        // Exception table:
//        //   from	to	target	type
//        //   9	28	34	finally
//        //   28	31	34	finally
//        //   35	38	34	finally
//    }
//
//    private boolean s(PackageManager paramPackageManager, String paramString) throws PackageManager.NameNotFoundException {
//        if (TextUtils.isEmpty(paramString))
//            return false;
//        if (this.d.a(paramString))
//            return true;
//        if (paramPackageManager != null)
//            try {
//                PackageInfo packageInfo = paramPackageManager.getPackageInfo(paramString, 0);
//                if (packageInfo != null && packageInfo.applicationInfo != null) {
//                    int i = packageInfo.applicationInfo.flags;
//                    if ((i & 0x1) != 0)
//                        return true;
//                }
//            } finally {}
//        return false;
//    }
//
//    protected int c(Context paramContext) {
//        if (paramContext == null)
//            return 0;
//        List<AppClearInfo> list = j(paramContext, null, null);
//        int i = 90;
//        if (list != null)
//            if (list.size() < 9) {
//                i = 90 - list.size() * 5;
//            } else {
//                i = 40;
//            }
//        long l1 = n(paramContext);
//        long l2 = q(paramContext);
//        return (int)(i - l1 / l2 / 20L * 7L);
//    }
//
//    protected Float d(Context paramContext) {
//        return Float.valueOf((float)n(paramContext) / (float)q(paramContext));
//    }
//
//    @Override
//    protected void g(Context paramContext, List<AppClearInfo> paramList, IExecListener<AppClearInfo, Long> paramIExecListener, TimeController paramTimeController) {
//        if (paramContext != null && paramList != null)
//            ThreadUtil.b(new Runnable(this, paramList, paramContext, paramIExecListener, paramTimeController) {
//                public void run() {
//                    Long long_1 = Long.valueOf(0L);
//                    Long long_2 = long_1;
//                    byte b = 0;
//                    while (b < this.b.size()) {
//                        AppClearInfo appClearInfo = this.b.get(b);
//                        Long long_ = long_2;
//                        if (appClearInfo != null)
//                            long_ = Long.valueOf(long_2.longValue() + appClearInfo.e().longValue());
//                        b++;
//                        long_2 = long_;
//                    }
//                    Long long_3 = Long.valueOf(this.f.n(this.c));
//                    if (this.d != null)
//                        ThreadUtil.a(this.c, new Runnable(this, long_3, long_2) {
//                            public void run() {
//                                MemoryManager.null  = this.d;
//                        .d.b(.c, this.b, this.c);
//                            }
//                        });
//                    ActivityManager activityManager = (ActivityManager)this.c.getSystemService("activity");
//                    int i = this.b.size();
//                    Iterator<AppClearInfo> iterator1 = this.b.iterator();
//                    for (b = 0; iterator1 != null && iterator1.hasNext(); b++) {
//                        long l1 = System.currentTimeMillis();
//                        AppClearInfo appClearInfo = iterator1.next();
//                        if (appClearInfo == null)
//                            continue;
//                        appClearInfo.killProcess(this.c, activityManager);
//                        long l2 = System.currentTimeMillis();
//                        TimeController timeController = this.e;
//                        if (timeController != null)
//                            timeController.b(b, i, l2 - l1);
//                        long_1 = Long.valueOf(long_1.longValue() + appClearInfo.e().longValue());
//                        IExecListener iExecListener = this.d;
//                        if (iExecListener != null && iExecListener != null) {
//                            if (!ContextCheckUtil.a(this.c))
//                                return;
//                            ThreadUtil.a(this.c, new Runnable(this, b, i, long_1, long_2, appClearInfo) {
//                                public void run() {
//                                    if (!ContextCheckUtil.a(this.g.c))
//                                        return;
//                                    MemoryManager.null  = this.g;
//                                    IExecListener iExecListener = .d;
//                                    Context context = .c;
//                                    int i = this.b;
//                                    int j = this.c;
//                                    iExecListener.g(context, i, j, (i + 1) * 100 / j, this.d, Long.valueOf(this.e.longValue() - this.d.longValue()), this.e, this.f);
//                                }
//                            });
//                        }
//                    }
//                    if (!this.b.isEmpty())
//                        MemoryManager.l(this.f);
//                    Iterator<MemoryManager.OptListener> iterator = this.f.b.iterator();
//                    while (iterator.hasNext())
//                        ((MemoryManager.OptListener)iterator.next()).e((int)((1.0F - (float)this.f.n(this.c) * 1.0F / (float)this.f.q(this.c) * 1.0F) * 100.0F));
//                    long l = this.f.n(this.c);
//                    if (this.d != null) {
//                        ThreadUtil.a(this.c, new Runnable(this, Long.valueOf(l), long_2, long_3) {
//                            public void run() {
//                                MemoryManager.null  = this.e;
//                                IExecListener iExecListener = .d;
//                                Context context = .c;
//                                Long long_ = this.b;
//                                iExecListener.d(context, long_, this.c, Long.valueOf(long_.longValue() - this.d.longValue()));
//                            }
//                        });
//                        ThreadUtil.a(this.c, new Runnable(this) {
//                            public void run() {
//                                MemoryManager.null  = this.b;
//                        .d.a(.c);
//                            }
//                        });
//                    }
//                }
//            });
//    }
//
//    @Override
//    public List<AppClearInfo> j(Context paramContext, IScanListener<AppClearInfo, Long> paramIScanListener, TimeController paramTimeController) {
//        PackageManager packageManager1;
//        ActivityManager activityManager1;
//        ActivityManager activityManager3;
//        HashMap<Object, Object> hashMap1;
//        int i;
//        int j;
//        HashMap<Object, Object> hashMap3;
//        PackageManager packageManager2;
//        long l2;
//        MemoryManager memoryManager = this;
//        if (paramIScanListener != null)
//            paramIScanListener.e(paramContext);
//        if (paramContext == null) {
//            paramIScanListener.c(paramContext, 0, Long.valueOf(0L), null);
//            return null;
//        }
//        ActivityManager activityManager2 = (ActivityManager)paramContext.getSystemService(Context.ACTIVITY_SERVICE);
//        if (activityManager2 == null) {
//            paramIScanListener.c(paramContext, 0, Long.valueOf(0L), null);
//            return null;
//        }
//        HashMap<Object, Object> hashMap2 = new HashMap<Object, Object>();
//        List list1 = activityManager2.getRunningAppProcesses();//获取当前运行的进程
//        List list2 = activityManager2.getRunningServices(2147483647);//获取当前运行的服务
//        if (list1 == null) {
//            i = 0;
//        } else {
//            i = list1.size();
//        }
//        if (list2 == null) {
//            j = 0;
//        } else {
//            j = list2.size();
//        }
//        int k = i + j;
//        PackageManager packageManager3 = paramContext.getApplicationContext().getPackageManager();//获取packagerManager
//        long l1 = System.currentTimeMillis();
//        if (list1 != null) {
//            HashMap<Object, Object> hashMap;
//            PackageManager packageManager4;
//            ActivityManager activityManager5;
//            Iterator<ActivityManager.RunningAppProcessInfo> iterator = list1.iterator();
//            l2 = 0L;
//            i = 0;
//            j = 0;
//            while (iterator.hasNext()) {
//                HashMap<Object, Object> hashMap4;
//                PackageManager packageManager6;
//                PackageManager packageManager7;
//                ActivityManager activityManager6;
//                ActivityManager activityManager7;
//                ActivityManager activityManager8;
//                HashMap<Object, Object> hashMap6;
//                int n = i + 1;
//                ActivityManager.RunningAppProcessInfo runningAppProcessInfo = iterator.next();
//                String[] arrayOfString = runningAppProcessInfo.pkgList;
//                if (arrayOfString == null || arrayOfString.length == 0) {
//                    activityManager7 = activityManager2;
//                    hashMap4 = hashMap2;
//                    packageManager7 = packageManager3;
//                    activityManager8 = activityManager7;
//                } else {
//                    int i1 = activityManager7.length;
//                    i = j;
//                    boolean bool = false;
//                    j = i1;
//                    for (i1 = bool; i1 < j; i1++) {
//                        ActivityManager activityManager9 = activityManager7[i1];
//                        if (!TextUtils.isEmpty((CharSequence)activityManager9)) {
//                            AppClearInfo appClearInfo;
//                            if (memoryManager.s((PackageManager)activityManager8, (String)activityManager9)) {
//                                iterator.remove();
//                                break;
//                            }
//                            if (packageManager7.containsKey(activityManager9)) {
//                                appClearInfo = (AppClearInfo)packageManager7.get(activityManager9);
//                            } else {
//                                Debug.MemoryInfo[] arrayOfMemoryInfo = hashMap4.getProcessMemoryInfo(new int[] { runningAppProcessInfo.pid });
//                                Long long_1 = Long.valueOf(0L);
//                                if (arrayOfMemoryInfo[0] != null)
//                                    long_1 = Long.valueOf(arrayOfMemoryInfo[0].getTotalPss());
//                                appClearInfo = new AppClearInfo((String)activityManager9, null, memoryManager.m((String)activityManager9), long_1);
//                                Long long_2 = appClearInfo.e();
//                                l2 += appClearInfo.e().longValue();
//                                packageManager7.put(activityManager9, appClearInfo);
//                                long l = System.currentTimeMillis();
//                                if (paramTimeController != null)
//                                    paramTimeController.b(i, -1, l - l1);
//                                i++;
//                                l1 = System.currentTimeMillis();
//                                if (paramIScanListener != null) {
//                                    if (!ContextCheckUtil.a(paramContext))
//                                        return null;
//                                    paramIScanListener.f(paramContext, n, k, n * 100 / k, long_2, Long.valueOf(l2), appClearInfo);
//                                }
//                            }
//                            appClearInfo.setPid(runningAppProcessInfo.pid);
//                        }
//                    }
//                    ActivityManager<String, AppClearInfo> activityManager = activityManager8;
//                    PackageManager<ActivityManager, AppClearInfo> packageManager = packageManager7;
//                    hashMap6 = hashMap4;
//                    activityManager6 = activityManager;
//                    packageManager6 = packageManager;
//                    j = i;
//                }
//                HashMap<Object, Object> hashMap5 = hashMap6;
//                i = n;
//                activityManager5 = activityManager6;
//                packageManager4 = packageManager6;
//                hashMap = hashMap5;
//            }
//            ActivityManager<String, AppClearInfo> activityManager4 = activityManager5;
//            PackageManager<ActivityManager, AppClearInfo> packageManager5 = packageManager4;
//            hashMap3 = hashMap;
//            activityManager3 = activityManager4;
//            packageManager1 = packageManager5;
//        } else {
//            PackageManager<ActivityManager, AppClearInfo> packageManager = packageManager1;
//            i = 0;
//            l2 = 0L;
//            j = 0;
//            activityManager1 = activityManager3;
//            hashMap1 = hashMap3;
//            packageManager2 = packageManager;
//        }
//        int m = i;
//        long l3 = l2;
//        if (list2 != null) {
//            Iterator<ActivityManager.RunningServiceInfo> iterator = list2.iterator();
//            while (true) {
//                MemoryManager memoryManager1 = this;
//                m = i;
//                l3 = l2;
//                if (iterator.hasNext()) {
//                    AppClearInfo appClearInfo;
//                    i++;
//                    ActivityManager.RunningServiceInfo runningServiceInfo = iterator.next();
//                    if (runningServiceInfo.pid == 0)
//                        continue;
//                    ComponentName componentName = runningServiceInfo.service;
//                    if (componentName == null || TextUtils.isEmpty(componentName.getPackageName()))
//                        continue;
//                    if (activityManager1.containsKey(runningServiceInfo.service.getPackageName())) {
//                        appClearInfo = (AppClearInfo)activityManager1.get(runningServiceInfo.service.getPackageName());
//                    } else {
//                        if (memoryManager1.s((PackageManager)hashMap1, runningServiceInfo.service.getPackageName()))
//                            continue;
//                        Debug.MemoryInfo[] arrayOfMemoryInfo = packageManager2.getProcessMemoryInfo(new int[] { runningServiceInfo.pid });
//                        Long long_1 = Long.valueOf(0L);
//                        if (arrayOfMemoryInfo[0] != null)
//                            long_1 = Long.valueOf(arrayOfMemoryInfo[0].getTotalPss());
//                        appClearInfo = new AppClearInfo(runningServiceInfo.service.getPackageName(), null, memoryManager1.m(runningServiceInfo.service.getPackageName()), long_1);
//                        Long long_2 = appClearInfo.e();
//                        l2 += appClearInfo.e().longValue();
//                        activityManager1.put(runningServiceInfo.service.getPackageName(), appClearInfo);
//                        l3 = System.currentTimeMillis();
//                        if (paramTimeController != null)
//                            paramTimeController.b(j, -1, l3 - l1);
//                        l1 = System.currentTimeMillis();
//                        if (paramIScanListener != null) {
//                            if (!ContextCheckUtil.a(paramContext))
//                                return null;
//                            paramIScanListener.f(paramContext, i, k, i * 100 / k, long_2, Long.valueOf(l2), appClearInfo);
//                        }
//                        j++;
//                    }
//                    appClearInfo.setPid(runningServiceInfo.pid);
//                    continue;
//                }
//                break;
//            }
//        }
//        ArrayList<AppClearInfo> arrayList = new ArrayList(activityManager1.values());
//        if (paramIScanListener != null)
//            paramIScanListener.c(paramContext, m, Long.valueOf(l3), arrayList);
//        return arrayList;
//    }
//
//    public long n(Context paramContext) {
//        return getMemoryInfo(paramContext, 1048576);
//    }
//
//    /**
//     * 获取内存信息
//     * @param paramContext
//     * @param paramInt
//     * @return
//     */
//    public long getMemoryInfo(Context paramContext, int paramInt) {
//        ActivityManager activityManager = (ActivityManager)paramContext.getSystemService(Context.ACTIVITY_SERVICE);
//        if (activityManager == null)
//            return 0L;
//        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
//        activityManager.getMemoryInfo(memoryInfo);
//        return UnitConverter.b(memoryInfo.availMem, 1, paramInt);
//    }
//
//    public long q(Context context) {
//        return r(context, 1048576);
//    }
//
//    public long r(Context paramContext, int paramInt) {
//        long l1 = 0L;
//        if (paramContext == null)
//            return 0L;
//        long l2 = l1;
//        try {
//            FileReader fileReader = new FileReader();
//            l2 = l1;
//            this("/proc/meminfo");//读取proc/meminfo文件信息，里面由内存中运行的程序信息
//            l2 = l1;
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//            l2 = l1;
//            this(fileReader, 8192);
//            l2 = l1;
//            String[] arrayOfString = bufferedReader.readLine().split("\\s+");
//            l2 = l1;
//            int i = arrayOfString.length;
//            for (byte b = 0; b < i; b++) {
//                String str = arrayOfString[b];
//                l2 = l1;
//                StringBuilder stringBuilder = new StringBuilder();
//                l2 = l1;
//                stringBuilder.append(str);
//                l2 = l1;
//                stringBuilder.append("\t");
//                l2 = l1;
//                stringBuilder.toString();
//            }
//            l2 = l1;
//            l1 = Long.valueOf(arrayOfString[1]).intValue() * 1024L;
//            l2 = l1;
//            bufferedReader.close();
//            l2 = l1;
//        } catch (IOException iOException) {}
//        return UnitConverter.b(l2, 1, paramInt);
//    }
//
//    public static interface OptListener {
//        void e(int param1Int);
//    }
//}
