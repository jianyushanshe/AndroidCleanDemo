//package com.example.myapplication.memory;
//
//import android.app.ActivityManager;
//import android.content.Context;
//import android.text.TextUtils;
//import java.util.ArrayList;
//import java.util.List;
//
//public class AppClearInfo {
//    private String processName;
//
//    private List<Integer> b;
//
//    private Long c;
//
//    public AppClearInfo(String paramString, int[] paramArrayOfint, int paramInt, Long paramLong) {
//        this.processName = paramString;
//        this.b = new ArrayList<Integer>();
//        if (paramArrayOfint != null && paramArrayOfint.length > 0)
//            for (paramInt = 0; paramInt < paramArrayOfint.length; paramInt++) {
//                if (!this.b.contains(Integer.valueOf(paramArrayOfint[paramInt])))
//                    this.b.add(Integer.valueOf(paramArrayOfint[paramInt]));
//            }
//        this.c = paramLong;
//    }
//
//    public void setPid(int paramInt) {
//        if (this.b.contains(Integer.valueOf(paramInt)))
//            return;
//        this.b.add(Integer.valueOf(paramInt));
//    }
//
//    /**
//     * 杀掉指定的进程
//     * @param paramContext
//     * @param paramActivityManager
//     */
//    public final void killProcess(Context paramContext, ActivityManager paramActivityManager) {
//        if (paramActivityManager == null && paramContext == null)
//            return;
//        ActivityManager activityManager = paramActivityManager;
//        if (paramActivityManager == null)
//            activityManager = (ActivityManager)paramContext.getSystemService(Context.ACTIVITY_SERVICE);
//        if (activityManager == null)
//            return;
//        if (!TextUtils.isEmpty(getProcessesName()))
//            activityManager.killBackgroundProcesses(getProcessesName());
//    }
//
//    public boolean c(Integer paramInteger) {
//        return this.b.contains(paramInteger);
//    }
//
//    public boolean d(String paramString) {
//        return TextUtils.equals(this.processName, paramString);
//    }
//
//    public Long e() {
//        return this.c;
//    }
//
//    public boolean equals(Object paramObject) {
//        return (paramObject instanceof String) ? d((String)paramObject) : ((paramObject instanceof Integer) ? c((Integer)paramObject) : ((paramObject instanceof AppClearInfo) ? d(((AppClearInfo)paramObject).a) : super.equals(paramObject)));
//    }
//
//    public String getProcessesName() {
//        return this.processName;
//    }
//
//    public String toString() {
//        return this.processName;
//    }
//}
