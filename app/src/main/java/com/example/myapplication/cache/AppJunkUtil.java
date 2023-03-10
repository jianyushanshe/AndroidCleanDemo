//package com.example.myapplication.cache;
//
//import android.content.Context;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.os.Environment;
//import android.text.TextUtils;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.annotation.RequiresPermission;
//
//import com.example.myapplication.ContextCheckUtil;
//import com.example.myapplication.R;
//import com.example.myapplication.ThreadUtil;
//import com.example.myapplication.TimeController;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Random;
//
//public class AppJunkUtil {
//    private static final String a;
//
//    private static final String b;
//
//    private static final String c;
//
//    static {
//        File file = Environment.getExternalStorageDirectory();
//        if (file != null) {
//            String str = file.getAbsolutePath();
//            if (!TextUtils.isEmpty(str)) {
//                StringBuffer stringBuffer = new StringBuffer();
//                stringBuffer.append(str);
//                stringBuffer.append(File.separator);
//                stringBuffer.append("Android");
//                stringBuffer.append(File.separator);
//                stringBuffer.append("data");
//                stringBuffer.append(File.separator);
//                a = stringBuffer.toString();
//                stringBuffer.append("%s");
//                b = stringBuffer.toString();
//                stringBuffer.append(File.separator);
//                stringBuffer.append("cache");
//                c = stringBuffer.toString();
//            } else {
//                b = null;
//                c = null;
//                a = null;
//            }
//        } else {
//            b = null;
//            c = null;
//            a = null;
//        }
//    }
//
//    private static AppJunk a(Context paramContext, String paramString, boolean paramBoolean, List<String> paramList) {
//        if (paramContext == null || (!paramBoolean && TextUtils.isEmpty(paramString)))
//            return null;
//        AppJunk appJunk = new AppJunk();
//        appJunk.w(paramBoolean);
//        appJunk.u(paramString);
//        byte b = 0;
//        if (!paramBoolean) {
//            appJunk.q(String.format(c, new Object[] { appJunk.i() }));
//            appJunk.s(String.format(String.valueOf(b), new Object[] { appJunk.i() }));
//        } else {
//            File file = new File(a);
//            if (file.isDirectory() && file.exists()) {
//                File[] arrayOfFile = file.listFiles();
//                if (arrayOfFile == null)
//                    return appJunk;
//                int i = arrayOfFile.length;
//                while (b < i) {
//                    File file1 = arrayOfFile[b];
//                    if (file1 != null && file1.exists() && !TextUtils.isEmpty(file1.getPath())) {
//                        String str = file1.getName();
//                        if (!TextUtils.isEmpty(str) && paramList != null && !paramList.contains(str))
//                            appJunk.a(file1.getAbsolutePath());
//                    }
//                    b++;
//                }
//            }
//        }
//        return appJunk;
//    }
//
//    private static void b(@NonNull AppJunk paramAppJunk) {
//        long l2;
//        if (paramAppJunk == null)
//            return;
//        long l1 = 0L;
//        if (paramAppJunk.l()) {
//            List<String> list = paramAppJunk.h();
//            l2 = l1;
//            if (list != null) {
//                l2 = l1;
//                if (list.size() > 0) {
//                    Iterator<String> iterator = list.iterator();
//                    while (true) {
//                        l2 = l1;
//                        if (iterator.hasNext()) {
//                            String str = iterator.next();
//                            if (TextUtils.isEmpty(str))
//                                continue;
//                            l1 += FileUtil.c(str);
//                            continue;
//                        }
//                        break;
//                    }
//                }
//            }
//        } else if (paramAppJunk.k()) {
//            l2 = FileUtil.c(paramAppJunk.g());
//        } else {
//            l2 = FileUtil.c(paramAppJunk.e());
//        }
//        l1 = l2;
//        if (l2 < 1048576L)
//            l1 = l2 + 1048576L + (new Random(System.currentTimeMillis())).nextInt(7340032);
//        paramAppJunk.r(l1);
//        paramAppJunk.t(l1);
//    }
//
//    private static void c(@NonNull Context paramContext, @NonNull AppJunk paramAppJunk, @Nullable PackageManager paramPackageManager, @Nullable PackageInfo paramPackageInfo) {
//        if (paramAppJunk == null)
//            return;
//        if (paramAppJunk.l()) {
//            paramAppJunk.m(paramContext.getResources().getDrawable(R.mipmap.__ic_other_junk));
//            paramAppJunk.n("垃圾文件");
//            paramAppJunk.o(1);
//            paramAppJunk.p("1.0.0");
//        } else {
//            PackageManager packageManager = paramPackageManager;
//            PackageInfo packageInfo = paramPackageInfo;
//            if (paramPackageInfo == null) {
//                PackageManager packageManager1 = paramPackageManager;
//                if (paramPackageManager == null) {
//                    packageManager1 = paramPackageManager;
//                    if (paramContext != null)
//                        packageManager1 = paramContext.getPackageManager();
//                }
//                if (packageManager1 == null)
//                    return;
//                try {
//                    packageInfo = packageManager1.getPackageInfo(paramAppJunk.i(), 0);
//                    packageManager = packageManager1;
//                } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
//                    nameNotFoundException.printStackTrace();
//                    return;
//                }
//            }
//            if (packageInfo == null)
//                return;
//            paramAppJunk.m(packageInfo.applicationInfo.loadIcon(packageManager));
//            paramAppJunk.n((String)packageInfo.applicationInfo.loadLabel(packageManager));
//            paramAppJunk.o(packageInfo.versionCode);
//            paramAppJunk.p(packageInfo.versionName);
//        }
//    }
//
//    @RequiresPermission(allOf = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"})
//    public static List<AppJunk> d(Context paramContext, IScanListener<AppJunk, Long> paramIScanListener, TimeController paramTimeController) {
//        if (paramIScanListener != null)
//            paramIScanListener.e(paramContext);
//        ArrayList<AppJunk> arrayList = new ArrayList();
//        ArrayList<String> arrayList1 = new ArrayList();
//        if (paramContext == null || paramContext.getPackageManager() == null) {
//            if (paramIScanListener != null) {
//                if (!ContextCheckUtil.a(paramContext))
//                    return null;
//                paramIScanListener.c(paramContext, 0, Long.valueOf(0L), arrayList);
//            }
//            return arrayList;
//        }
//        PackageManager packageManager = paramContext.getApplicationContext().getPackageManager();
//        try {
//            List<PackageInfo> list = packageManager.getInstalledPackages(0);
//            if (list == null) {
//                i = 0;
//            } else {
//                i = list.size();
//            }
//            long l = System.currentTimeMillis();
//            Long long_2 = Long.valueOf(0L);
//            boolean bool = false;
//            byte b = 0;
//            int j = i;
//            int i = bool;
//            while (b < j) {
//                PackageInfo packageInfo = list.get(b);
//                if (packageInfo != null && (0x1 & packageInfo.applicationInfo.flags) == 0) {
//                    if (!TextUtils.isEmpty(packageInfo.packageName))
//                        arrayList1.add(packageInfo.packageName);
//                    AppJunk appJunk1 = a(paramContext, packageInfo.packageName, false, arrayList1);
//                    if (appJunk1 != null && FileUtil.h(appJunk1.e())) {
//                        long l1 = System.currentTimeMillis();
//                        if (paramTimeController != null)
//                            paramTimeController.b(i, -1, l1 - l);
//                        arrayList.add(appJunk1);
//                        c(paramContext, appJunk1, packageManager, packageInfo);
//                        if (paramIScanListener != null)
//                            ThreadUtil.a(paramContext, new Runnable(paramContext, paramIScanListener, b, j, long_2, appJunk1) {
//                                public void run() {
//                                    if (!ContextCheckUtil.a(this.b))
//                                        return;
//                                    IScanListener iScanListener = this.c;
//                                    Context context = this.b;
//                                    int i = this.d;
//                                    int j = this.e;
//                                    iScanListener.h(context, i, j + 1, i * 100 / j + 1, this.f, this.g);
//                                }
//                            });
//                        l = System.currentTimeMillis();
//                        b(appJunk1);
//                        Long long_ = Long.valueOf(appJunk1.f());
//                        long_2 = Long.valueOf(long_2.longValue() + long_.longValue());
//                        l1 = System.currentTimeMillis();
//                        if (paramTimeController != null)
//                            paramTimeController.b(i, -1, l1 - l);
//                        if (paramIScanListener != null)
//                            ThreadUtil.a(paramContext, new Runnable(paramContext, paramIScanListener, b, j, long_, long_2, appJunk1) {
//                                public void run() {
//                                    if (!ContextCheckUtil.a(this.b))
//                                        return;
//                                    IScanListener iScanListener = this.c;
//                                    Context context = this.b;
//                                    int i = this.d;
//                                    int j = this.e;
//                                    iScanListener.f(context, i, j + 1, i * 100 / j + 1, this.f, this.g, this.h);
//                                }
//                            });
//                        i++;
//                        l = System.currentTimeMillis();
//                    }
//                }
//                b++;
//            }
//            i++;
//            AppJunk appJunk = a(paramContext, null, true, arrayList1);
//            Long long_1 = long_2;
//            if (appJunk.h().size() > 0) {
//                long l1 = System.currentTimeMillis();
//                if (paramTimeController != null)
//                    paramTimeController.b(i, -1, l1 - l);
//                c(paramContext, appJunk, null, null);
//                arrayList.add(appJunk);
//                if (paramIScanListener != null) {
//                    if (!ContextCheckUtil.a(paramContext))
//                        return null;
//                    ThreadUtil.a(paramContext, new Runnable(paramContext, paramIScanListener, j, long_2, appJunk) {
//                        public void run() {
//                            if (!ContextCheckUtil.a(this.b))
//                                return;
//                            IScanListener iScanListener = this.c;
//                            Context context = this.b;
//                            int i = this.d;
//                            iScanListener.h(context, i, i + 1, 100, this.e, this.f);
//                        }
//                    });
//                }
//                b(appJunk);
//                Long long_ = Long.valueOf(appJunk.f());
//                long_2 = Long.valueOf(long_2.longValue() + long_.longValue());
//                l1 = System.currentTimeMillis();
//                if (paramTimeController != null)
//                    paramTimeController.b(i, -1, l1 - l);
//                long_1 = long_2;
//                if (paramIScanListener != null) {
//                    if (!ContextCheckUtil.a(paramContext))
//                        return null;
//                    ThreadUtil.a(paramContext, new Runnable(paramContext, paramIScanListener, j, long_, long_2, appJunk) {
//                        public void run() {
//                            if (!ContextCheckUtil.a(this.b))
//                                return;
//                            IScanListener iScanListener = this.c;
//                            Context context = this.b;
//                            int i = this.d;
//                            iScanListener.f(context, i, i + 1, 100, this.e, this.f, this.g);
//                        }
//                    });
//                    long_1 = long_2;
//                }
//            }
//            ThreadUtil.a(paramContext, new Runnable(paramContext, paramIScanListener, i, long_1.longValue(), arrayList) {
//                public void run() {
//                    if (!ContextCheckUtil.a(this.b))
//                        return;
//                    this.c.c(this.b, this.d, Long.valueOf(this.e), this.f);
//                }
//            });
//            return arrayList;
//        } catch (Exception exception) {
//            exception.printStackTrace();
//            return arrayList;
//        }
//    }
//}
