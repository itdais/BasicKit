package com.itdais.basekit.util

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Process
import android.text.TextUtils
import android.util.Log

/**
 * 应用工具类
 *
 * @author ding.jw
 */
object AppUtils {
    private val TAG = AppUtils::class.java.simpleName

    /**
     * 获取系统版本号
     *
     * @return 返回版本号
     */
    fun getVersionName(mContext: Context): String {
        return try {
            val pi = mContext.packageManager.getPackageInfo(mContext.packageName, 0)
            pi.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            Log.i(TAG, e.toString(), e)
            ""
        } catch (e: Exception) {
            Log.i(TAG, e.toString(), e)
            ""
        }
    }

    /**
     * getVersionCode
     *
     * @return int
     */
    fun getVersionCode(context: Context): Int {
        return try {
            val pi = context.packageManager.getPackageInfo(context.packageName, 0)
            pi.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            Log.i(TAG, e.toString(), e)
            0
        } catch (e: Exception) {
            Log.i(TAG, e.toString(), e)
            0
        }
    }

    /**
     * 获得应用名
     *
     * @param context context
     * @return applicationName
     */
    fun getApplicationName(context: Context?): String {
        if (context == null) {
            return ""
        }
        var appName: String? = ""
        try {
            val ai = context.packageManager
                .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            val bundle = ai.metaData
            appName = bundle.getString("APP_NAME")
        } catch (e: Exception) {
            return ""
        }
        return appName ?: ""
    }

    /**
     * 获得进程名
     *
     * @param context
     * @return
     */
    fun getProcessName(context: Context?): String? {
        if (context == null) {
            return null
        }
        var processName: String? = null
        try {
            val pid = Process.myPid()
            val manager = context.applicationContext
                .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            if (manager != null) {
                for (process in manager.runningAppProcesses) {
                    if (process.pid == pid) {
                        processName = process.processName
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return processName
    }

    /**
     * 是否是主进程
     *
     *
     * take out from NovaMainApplication.java in android-nova-business
     *
     * @param context
     * @return boolean 如果进程名取值发生错误或者包名找不到，默认为主进程
     */
    fun isMainProcess(context: Context?): Boolean {
        if (context == null) {
            return true
        }
        val processName = getProcessName(context)
        var packageName: String? = null
        try {
            packageName = context.applicationContext.packageName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return if (TextUtils.isEmpty(processName) || TextUtils.isEmpty(packageName)) {
            true
        } else packageName == processName
    }
}