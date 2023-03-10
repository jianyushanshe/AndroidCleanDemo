package com.example.myapplication.memory;

import android.content.Context;
import android.text.TextUtils;

public class WhiteList extends AbsListCache implements IList {
    public WhiteList(Context paramContext) {
        super(paramContext);
    }

    public boolean a(String paramString) {
        return TextUtils.isEmpty(paramString) ? false : e(paramString);
    }

    protected String d(Context paramContext) {
        return "__lib_systemclear_whitelist";
    }
}
