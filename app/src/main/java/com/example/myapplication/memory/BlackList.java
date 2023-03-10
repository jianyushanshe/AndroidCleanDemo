package com.example.myapplication.memory;

import android.content.Context;


public class BlackList extends AbsListCache implements IList {
    public BlackList(Context paramContext) {
        super(paramContext);
    }

    public boolean a(String paramString) {
        return e(paramString);
    }

    protected String d(Context paramContext) {
        return "__lib_systemclear_blacklist";
    }
}
