package com.itdais.basekit.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 16/12/08
 *     desc  :
 * </pre>
 */
public class Utils {

    @SuppressLint("StaticFieldLeak")
    private static Application sApp;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param app
     */
    public static void init(Application app) {
        if (app == null) {
            Log.e("Utils", "app is null.");
            return;
        }
        sApp = app;
    }

    public static Context getContext() {
        return sApp.getApplicationContext();
    }

    public static Resources getResources() {
        return sApp.getResources();
    }
}
