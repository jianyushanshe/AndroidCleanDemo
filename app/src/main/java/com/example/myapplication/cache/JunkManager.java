//package com.example.myapplication.cache;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import androidx.annotation.RequiresPermission;
//import com.example.myapplication.ContextCheckUtil;
//import com.example.myapplication.ThreadUtil;
//import com.example.myapplication.TimeController;
//
//
//import java.util.Iterator;
//import java.util.List;
//
//public class JunkManager extends AbsOptimizable<AppJunk, Long> {
//    private static volatile JunkManager b;
//
//    private JunkManager(Context paramContext) {}
//
//    @RequiresPermission(allOf = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"})
//    public static JunkManager l(Context paramContext) {
//        // Byte code:
//        //   0: getstatic com/amber/lib/systemcleaner/module/cache/JunkManager.b : Lcom/amber/lib/systemcleaner/module/cache/JunkManager;
//        //   3: ifnonnull -> 43
//        //   6: ldc com/amber/lib/systemcleaner/module/cache/JunkManager
//        //   8: monitorenter
//        //   9: getstatic com/amber/lib/systemcleaner/module/cache/JunkManager.b : Lcom/amber/lib/systemcleaner/module/cache/JunkManager;
//        //   12: ifnonnull -> 31
//        //   15: new com/amber/lib/systemcleaner/module/cache/JunkManager
//        //   18: astore_1
//        //   19: aload_1
//        //   20: aload_0
//        //   21: invokevirtual getApplicationContext : ()Landroid/content/Context;
//        //   24: invokespecial <init> : (Landroid/content/Context;)V
//        //   27: aload_1
//        //   28: putstatic com/amber/lib/systemcleaner/module/cache/JunkManager.b : Lcom/amber/lib/systemcleaner/module/cache/JunkManager;
//        //   31: ldc com/amber/lib/systemcleaner/module/cache/JunkManager
//        //   33: monitorexit
//        //   34: goto -> 43
//        //   37: astore_0
//        //   38: ldc com/amber/lib/systemcleaner/module/cache/JunkManager
//        //   40: monitorexit
//        //   41: aload_0
//        //   42: athrow
//        //   43: getstatic com/amber/lib/systemcleaner/module/cache/JunkManager.b : Lcom/amber/lib/systemcleaner/module/cache/JunkManager;
//        //   46: areturn
//        // Exception table:
//        //   from	to	target	type
//        //   9	31	37	finally
//        //   31	34	37	finally
//        //   38	41	37	finally
//    }
//
//    protected int c(Context paramContext) {
//        if (paramContext == null)
//            return 0;
//        List<AppJunk> list = j(paramContext, null, null);
//        int i = 100;
//        if (list != null || !list.isEmpty())
//            if (list.size() < 9) {
//                i = 100 - list.size() * 5;
//            } else {
//                i = 40;
//            }
//        return i;
//    }
//
//    protected Float d(Context paramContext) {
//        long l1 = FileUtil.f(paramContext);
//        long l2 = FileUtil.g(paramContext);
//        return Float.valueOf((float)(l1 / l2));
//    }
//
//
//    public void g(Context paramContext, List<AppJunk> paramList, IExecListener<AppJunk, Long> paramIExecListener, TimeController paramTimeController) {
//        Long long_;
//        if (paramContext == null || paramList == null) {
//            if (paramIExecListener != null) {
//                long_ = Long.valueOf(FileUtil.f(paramContext));
//                ThreadUtil.a(paramContext, new Runnable(this, paramContext, paramIExecListener, long_) {
//                    public void run() {
//                        if (!ContextCheckUtil.a(this.b))
//                            return;
//                        this.c.b(this.b, this.d, Long.valueOf(0L));
//                    }
//                });
//                ThreadUtil.a(paramContext, new Runnable(this, paramContext, paramIExecListener, long_) {
//                    public void run() {
//                        if (!ContextCheckUtil.a(this.b))
//                            return;
//                        this.c.d(this.b, this.d, Long.valueOf(0L), Long.valueOf(0L));
//                    }
//                });
//                ThreadUtil.a(paramContext, new Runnable(this, paramContext, paramIExecListener) {
//                    public void run() {
//                        if (!ContextCheckUtil.a(this.b))
//                            return;
//                        this.c.a(this.b);
//                    }
//                });
//            }
//            return;
//        }
//        ThreadUtil.b(new Runnable(this, (List)long_, paramContext, paramIExecListener, paramTimeController) {
//            public void run() {
//                Long long_1 = Long.valueOf(0L);
//                Long long_2 = long_1;
//                byte b = 0;
//                while (b < this.b.size()) {
//                    AppJunk appJunk = this.b.get(b);
//                    Long long_ = long_2;
//                    if (appJunk != null)
//                        long_ = Long.valueOf(long_2.longValue() + appJunk.f());
//                    b++;
//                    long_2 = long_;
//                }
//                Long long_3 = Long.valueOf(FileUtil.f(this.c));
//                if (this.d != null)
//                    ThreadUtil.a(this.c, new Runnable(this, long_3, long_2) {
//                        public void run() {
//                            JunkManager.null  = this.d;
//                      .d.b(.c, this.b, this.c);
//                        }
//                    });
//                Iterator<AppJunk> iterator = this.b.iterator();
//                int i = this.b.size();
//                b = 0;
//                while (iterator.hasNext()) {
//                    AppJunk appJunk = iterator.next();
//                    if (appJunk == null) {
//                        b++;
//                        continue;
//                    }
//                    long l1 = System.currentTimeMillis();
//                    appJunk.b(this.c);
//                    Long long_5 = long_1;
//                    if (appJunk.f() >= 0L)
//                        long_5 = Long.valueOf(long_1.longValue() + appJunk.f());
//                    long l2 = System.currentTimeMillis();
//                    TimeController timeController = this.e;
//                    if (timeController != null)
//                        timeController.b(b, i, l2 - l1);
//                    if (!ContextCheckUtil.a(this.c))
//                        return;
//                    if (this.d != null)
//                        ThreadUtil.a(this.c, new Runnable(this, b, i, long_5, long_2, appJunk) {
//                            public void run() {
//                                if (!ContextCheckUtil.a(this.g.c))
//                                    return;
//                                JunkManager.null  = this.g;
//                                IExecListener iExecListener = .d;
//                                Context context = .c;
//                                int i = this.b;
//                                int j = this.c;
//                                iExecListener.g(context, i, j, (i + 1) * 100 / j, this.d, Long.valueOf(this.e.longValue() - this.d.longValue()), this.e, this.f);
//                            }
//                        });
//                    b++;
//                    Long long_4 = long_5;
//                }
//                if (this.d != null) {
//                    long l = FileUtil.f(this.c);
//                    ThreadUtil.a(this.c, new Runnable(this, l, long_2, long_3) {
//                        public void run() {
//                            if (!ContextCheckUtil.a(this.e.c))
//                                return;
//                            JunkManager.null  = this.e;
//                      .d.d(.c, Long.valueOf(this.b), this.c, Long.valueOf(this.b - this.d.longValue()));
//                        }
//                    });
//                    ThreadUtil.a(this.c, new Runnable(this) {
//                        public void run() {
//                            if (!ContextCheckUtil.a(this.b.c))
//                                return;
//                            JunkManager.null  = this.b;
//                      .d.a(.c);
//                        }
//                    });
//                }
//            }
//        });
//    }
//
//    @SuppressLint({"MissingPermission"})
//    public List<AppJunk> j(Context paramContext, IScanListener<AppJunk, Long> paramIScanListener, TimeController paramTimeController) {
//        return AppJunkUtil.d(paramContext, paramIScanListener, paramTimeController);
//    }
//}
