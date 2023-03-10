package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtil {
    private static final ExecutorService a = Executors.newFixedThreadPool(30);

    private static final InnerHandler b = new InnerHandler();

    public static final void a(Context paramContext, Runnable paramRunnable) {
        if (paramRunnable == null)
            return;
        if (Looper.myLooper() != Looper.getMainLooper()) {
            if (paramContext != null && paramContext instanceof Activity && ((Activity)paramContext).isFinishing())
                return;
            b.post(paramRunnable);
        } else {
            paramRunnable.run();
        }
    }

    public static final void b(Runnable paramRunnable) {
        a.execute(paramRunnable);
    }

    private static final class InnerHandler extends Handler {
        public InnerHandler() {
            super(Looper.getMainLooper());
        }
    }
}
