package com.example.myapplication.cache;

import android.content.Context;
import java.util.List;

public interface IScanListener<Data, Unit> {
    void c(Context paramContext, int paramInt, Unit paramUnit, List<Data> paramList);

    void e(Context paramContext);

    void f(Context paramContext, int paramInt1, int paramInt2, int paramInt3, Unit paramUnit1, Unit paramUnit2, Data paramData);

    void h(Context paramContext, int paramInt1, int paramInt2, int paramInt3, Unit paramUnit, Data paramData);
}
