package com.example.myapplication.cache;

import android.content.Context;

public interface IDataResult<T> {
    void a(Context paramContext);

    void b(Context paramContext, T paramT);

    void c(Context paramContext, int paramInt, String paramString);
}
