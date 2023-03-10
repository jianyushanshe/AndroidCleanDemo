package com.example.myapplication;

import android.app.Activity;
import android.content.Context;

public class ContextCheckUtil {
    public static final boolean a(Context paramContext) {
        return (paramContext == null) ? false : (!(paramContext instanceof Activity && ((Activity)paramContext).isFinishing()));
    }
}
