package com.example.myapplication.memory;

import android.content.Context;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

public abstract class AbsListCache extends AbsConfigSharedPreference {
    private List<String> b = new ArrayList<String>();

    public AbsListCache(Context paramContext) {
        super(paramContext);
        f();
    }

    private void f() {
        String str = b(null, "list_cache", "");
        if (TextUtils.isEmpty(str))
            return;
        String[] arrayOfString = str.split("|");
        if (arrayOfString != null && arrayOfString.length != 0) {
            int i = arrayOfString.length;
            for (byte b = 0; b < i; b++) {
                String str1 = arrayOfString[b];
                if (!TextUtils.isEmpty(str1))
                    this.b.add(str1);
            }
        }
    }

    protected int c(Context paramContext) {
        return 0;
    }

    protected boolean e(String paramString) {
        // Byte code:
        //   0: aload_0
        //   1: monitorenter
        //   2: aload_1
        //   3: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
        //   6: istore_2
        //   7: iload_2
        //   8: ifeq -> 15
        //   11: aload_0
        //   12: monitorexit
        //   13: iconst_0
        //   14: ireturn
        //   15: aload_0
        //   16: getfield b : Ljava/util/List;
        //   19: aload_1
        //   20: invokeinterface contains : (Ljava/lang/Object;)Z
        //   25: istore_2
        //   26: aload_0
        //   27: monitorexit
        //   28: iload_2
        //   29: ireturn
        //   30: astore_1
        //   31: aload_0
        //   32: monitorexit
        //   33: aload_1
        //   34: athrow
        // Exception table:
        //   from	to	target	type
        //   2	7	30	finally
        //   15	26	30	finally
        return false;
    }
}
