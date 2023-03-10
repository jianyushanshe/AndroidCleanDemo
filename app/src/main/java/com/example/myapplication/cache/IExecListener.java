package com.example.myapplication.cache;

import android.content.Context;

public interface IExecListener<Data, Unit> {
    void a(Context paramContext);

    void b(Context paramContext, Unit paramUnit1, Unit paramUnit2);

    void d(Context paramContext, Unit paramUnit1, Unit paramUnit2, Unit paramUnit3);

    void g(Context paramContext, int paramInt1, int paramInt2, int paramInt3, Unit paramUnit1, Unit paramUnit2, Unit paramUnit3, Data paramData);
}
