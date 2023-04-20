package com.example.myapplication.processes;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * 应用程序工具类
 * Created by QiuLong on 2019/8/20.
 */
public class ProgrameUtils {

    private List<ApplicationInfo> list;

    public ProgrameUtils(PackageManager pm) {
        try {
            if (pm != null) {
                list = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过一个程序名返回一个Application对象。
     *
     * @param name
     * @return
     */
    public ApplicationInfo getApplicationInfo(String name) {
        if (name == null || list == null) {
            return null;
        }
        for (ApplicationInfo info : list) {
            if (info != null && name.equals(info.processName)) {
                return info;
            }
        }
        return null;
    }

}
