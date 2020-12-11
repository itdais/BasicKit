package com.itdais.basekit.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

/**
 * Author:  ding.jw
 * Description:
 * app 工具类
 */
public class AppUtil {

    /**
     * 获得应用名
     *
     * @return applicationName
     */
    public static String getAppName() {
        if (Utils.getContext() == null) return "";
        String appName = null;
        try {
            ApplicationInfo ai = Utils.getContext().getPackageManager()
                    .getApplicationInfo(Utils.getContext().getPackageName(),
                            PackageManager.GET_META_DATA);
            appName = ai.metaData.getString("APP_NAME");
        } catch (Exception e) {
        }
        if (TextUtils.isEmpty(appName)) {
            appName = "";
        }
        return appName;
    }

    /**
     * Return the application's version name.
     *
     * @return the application's version name
     */
    public static String getVersionName() {
        try {
            PackageManager pm = Utils.getContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(Utils.getContext().getPackageName(), 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * Return the application's version code.
     *
     * @return the application's version code
     */
    public static int getVersionCode() {
        try {
            PackageManager pm = Utils.getContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(Utils.getContext().getPackageName(), 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }
}

