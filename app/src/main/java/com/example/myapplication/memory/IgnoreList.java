package com.example.myapplication.memory;

import android.content.Context;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

public class IgnoreList implements IList {
    private String a = "com.amber";

    private String b = "mobi.infolife";

    private List<String> c;

    public IgnoreList(Context paramContext) {
        ArrayList<String> arrayList = new ArrayList();
        this.c = arrayList;
        arrayList.add(this.a);
        this.c.add(this.b);
        this.c.add(paramContext.getPackageName());
    }

    public boolean a(String paramString) {
        if (TextUtils.isEmpty(paramString))
            return false;
        for (String str : this.c) {
            if (!TextUtils.isEmpty(str) && paramString.startsWith(str))
                return true;
        }
        return false;
    }
}
