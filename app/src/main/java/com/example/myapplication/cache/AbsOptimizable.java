//package com.example.myapplication.cache;
//
//import android.content.Context;
//import androidx.annotation.Nullable;
//
//
//import com.example.myapplication.ContextCheckUtil;
//import com.example.myapplication.ThreadUtil;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public abstract class AbsOptimizable<Data, Unit> implements IOptimizable<Data, Unit> {
//    private long a;
//
//    public final void a(Context paramContext, IDataResult<Integer> paramIDataResult) {
//        if (paramIDataResult == null)
//            return;
//        ThreadUtil.b(new Runnable(this, paramContext, paramIDataResult) {
//            public void run() {
//                int i = this.d.b(this.b);
//                if (i < 0 || i > 100) {
//                    ThreadUtil.a(this.b, new Runnable(this) {
//                        public void run() {
//                            if (!ContextCheckUtil.a(this.b.b))
//                                return;
//                            AbsOptimizable.null  = this.b;
//                      .c.c(.b, -1, ");
//                        }
//                    });
//                } else {
//                    ThreadUtil.a(this.b, new Runnable(this, i) {
//                        public void run() {
//                            if (!ContextCheckUtil.a(this.c.b))
//                                return;
//                            AbsOptimizable.null  = this.c;
//                      .c.b(.b, Integer.valueOf(this.b));
//                        }
//                    });
//                }
//                ThreadUtil.a(this.b, new Runnable(this) {
//                    public void run() {
//                        if (!ContextCheckUtil.a(this.b.b))
//                            return;
//                        AbsOptimizable.null  = this.b;
//                    .c.a(.b);
//                    }
//                });
//            }
//        });
//    }
//
//    public final int b(Context paramContext) {
//        return e() ? 100 : c(paramContext);
//    }
//
//    protected abstract int c(Context paramContext);
//
//    protected abstract Float d(Context paramContext);
//
//    protected final boolean e() {
//        boolean bool;
//        if (System.currentTimeMillis() - this.a < 180000L) {
//            bool = true;
//        } else {
//            bool = false;
//        }
//        return bool;
//    }
//
//    public final boolean f(Context paramContext) {
//        if (System.currentTimeMillis() - this.a < 180000L)
//            return false;
//        List<Data> list = j(paramContext, null, null);
//        return !(list == null || list.size() == 0);
//    }
//
//    protected abstract void g(Context paramContext, List<Data> paramList, IExecListener<Data, Unit> paramIExecListener, TimeController paramTimeController);
//
//    public final void h(Context paramContext, List<Data> paramList, IExecListener<Data, Unit> paramIExecListener, TimeController paramTimeController) {
//        if (paramList == null) {
//            paramList = null;
//        } else {
//            paramList = new ArrayList<Data>(paramList);
//        }
//        g(paramContext, paramList, paramIExecListener, paramTimeController);
//    }
//
//    public final void i(Context paramContext, IScanListener<Data, Unit> paramIScanListener, IDataResult<List<Data>> paramIDataResult, TimeController paramTimeController) {
//        if (paramIDataResult == null && paramIScanListener == null)
//            return;
//        ThreadUtil.b(new Runnable(this, paramContext, paramIScanListener, paramTimeController, paramIDataResult) {
//            public void run() {
//                IScanListener<Data, Unit> iScanListener;
//                AbsOptimizable<Data, Unit> absOptimizable = this.f;
//                Context context = this.b;
//                if (this.c == null) {
//                    iScanListener = null;
//                } else {
//                    iScanListener = new IScanListener<Data, Unit>(this) {
//                        public void c(Context param2Context, int param2Int, Unit param2Unit, List<Data> param2List) {
//                            ThreadUtil.a(param2Context, new Runnable(this, param2Context, param2Int, param2Unit, param2List) {
//                                public void run() {
//                                    if (!ContextCheckUtil.a(this.b))
//                                        return;
//                                    this.f.a.c.c(this.b, this.c, this.d, this.e);
//                                }
//                            });
//                        }
//
//                        public void e(Context param2Context) {
//                            ThreadUtil.a(param2Context, new Runnable(this, param2Context) {
//                                public void run() {
//                                    if (!ContextCheckUtil.a(this.b))
//                                        return;
//                                    this.c.a.c.e(this.b);
//                                }
//                            });
//                        }
//
//                        public void f(Context param2Context, int param2Int1, int param2Int2, int param2Int3, Unit param2Unit1, Unit param2Unit2, Data param2Data) {
//                            ThreadUtil.a(param2Context, new Runnable(this, param2Context, param2Int1, param2Int2, param2Int3, param2Unit1, param2Unit2, param2Data) {
//                                public void run() {
//                                    if (!ContextCheckUtil.a(this.b))
//                                        return;
//                                    this.i.a.c.f(this.b, this.c, this.d, this.e, this.f, this.g, this.h);
//                                }
//                            });
//                        }
//
//                        public void h(Context param2Context, int param2Int1, int param2Int2, int param2Int3, Unit param2Unit, Data param2Data) {
//                            ThreadUtil.a(param2Context, new Runnable(this, param2Context, param2Int1, param2Int2, param2Int3, param2Unit, param2Data) {
//                                public void run() {
//                                    if (!ContextCheckUtil.a(this.b))
//                                        return;
//                                    this.h.a.c.h(this.b, this.c, this.d, this.e, this.f, this.g);
//                                }
//                            });
//                        }
//                    };
//                }
//                List<Data> list = absOptimizable.j(context, iScanListener, this.d);
//                if (list == null) {
//                    ThreadUtil.a(this.b, new Runnable(this) {
//                        public void run() {
//                            if (!ContextCheckUtil.a(this.b.b))
//                                return;
//                            AbsOptimizable.null  = this.b;
//                            IDataResult iDataResult = .e;
//                            if (iDataResult != null)
//                                iDataResult.c(.b, -2, ");
//                        }
//                    });
//                } else {
//                    ThreadUtil.a(this.b, new Runnable(this, list) {
//                        public void run() {
//                            if (!ContextCheckUtil.a(this.c.b))
//                                return;
//                            AbsOptimizable.null  = this.c;
//                            IDataResult<List> iDataResult = .e;
//                            if (iDataResult != null)
//                                iDataResult.b(.b, this.b);
//                        }
//                    });
//                }
//                ThreadUtil.a(this.b, new Runnable(this) {
//                    public void run() {
//                        if (!ContextCheckUtil.a(this.b.b))
//                            return;
//                        AbsOptimizable.null  = this.b;
//                        IDataResult iDataResult = .e;
//                        if (iDataResult != null)
//                            iDataResult.a(.b);
//                    }
//                });
//            }
//        });
//    }
//
//    public abstract List<Data> j(Context paramContext, @Nullable IScanListener<Data, Unit> paramIScanListener, TimeController paramTimeController);
//
//    protected void k() {
//        this.a = System.currentTimeMillis();
//    }
//}
