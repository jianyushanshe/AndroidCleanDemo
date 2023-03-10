package com.example.myapplication.memory;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class AbsConfigSharedPreference implements IConfig {
    private SharedPreferences a = null;

    public AbsConfigSharedPreference(Context paramContext) {
        this.a = paramContext.getSharedPreferences(d(paramContext), c(paramContext));
    }

    public String b(Context paramContext, String paramString1, String paramString2) {
        return this.a.getString(paramString1, paramString2);
    }

    protected abstract int c(Context paramContext);

    protected abstract String d(Context paramContext);
}
