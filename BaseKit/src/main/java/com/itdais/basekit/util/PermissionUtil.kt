package com.itdais.basekit.util

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import androidx.core.content.ContextCompat

/**
 * Author:  ding.jw
 * Description:
 * 权限工具类
 * 无权限时进行跳转的逻辑处理
 */

/**
 * 检测相机权限
 */
fun Activity.checkCameraPermission(): Boolean {
    return checkPermissions(Manifest.permission.CAMERA)
}

/**
 * 检测文件读写
 */
fun Activity.checkFilePermission(): Boolean {
    return checkPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
}

/**
 * 检测相册权限->文件权限和相机权限
 */
fun Activity.checkGalleryPermission(): Boolean {
    return checkPermissions(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
}

/**
 * 检测定位权限
 */
fun Activity.checkLocationPermission(): Boolean {
    return checkPermissions(
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
}

/**
 * 判断是否拥有权限
 * @param permissions 权限字符串 {@code Manifest.permission}
 */
fun Activity.checkPermissions(vararg permissions: String): Boolean {
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
        permissions.forEach {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, it)) {
                return false
            }
        }
    }
    return true
}

/**
 * 跳转权限设置页面
 * 小米、华为、魅族可以直接跳转
 * 其他设备跳转应用设置
 */
fun Activity.toPermissionSetting() {
    val brand = Build.BRAND //手机厂商
    if (TextUtils.equals(brand.toLowerCase(), "redmi") ||
        TextUtils.equals(brand.toLowerCase(), "xiaomi")
    ) {
        toMiuiPermissionDetail(this) //小米
    } else if (TextUtils.equals(brand.toLowerCase(), "meizu")) {
        toMeizuPermission(this)
    } else if (TextUtils.equals(brand.toLowerCase(), "huawei") ||
        TextUtils.equals(brand.toLowerCase(), "honor")
    ) {
        toHuaweiPermission(this)
    } else {
        toPermissionDetail(this)
    }
}

/**
 * 小米设备跳转应用详情
 */
private fun toMiuiPermissionDetail(activity: Activity) {
    try { // MIUI 8
        val localIntent = Intent("miui.intent.action.APP_PERM_EDITOR")
        localIntent.setClassName(
            "com.miui.securitycenter",
            "com.miui.permcenter.permissions.PermissionsEditorActivity"
        )
        localIntent.putExtra("extra_pkgname", activity.packageName)
        activity.startActivity(localIntent)
    } catch (e: Exception) {
        try { // MIUI 5/6/7
            val localIntent = Intent("miui.intent.action.APP_PERM_EDITOR")
            localIntent.setClassName(
                "com.miui.securitycenter",
                "com.miui.permcenter.permissions.AppPermissionsEditorActivity"
            )
            localIntent.putExtra("extra_pkgname", activity.packageName)
            activity.startActivity(localIntent)
        } catch (e1: Exception) { // 否则跳转到应用详情
            toPermissionDetail(activity)
        }
    }
}

/**
 * 华为的权限管理页面
 */
private fun toHuaweiPermission(activity: Activity) {
    try {
        val intent = Intent(activity.packageName).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            component = ComponentName(
                "com.huawei.systemmanager",
                "com.huawei.permissionmanager.ui.MainActivity"
            )
        }
        activity.startActivity(intent)
    } catch (e: java.lang.Exception) {
        toPermissionDetail(activity)
    }
}

/**
 * 跳转到魅族的权限管理系统
 */
private fun toMeizuPermission(activity: Activity) {
    try {
        val intent = Intent("com.meizu.safe.security.SHOW_APPSEC").apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            addCategory(Intent.CATEGORY_DEFAULT)
            putExtra("packageName", activity.packageName)
        }
        activity.startActivity(intent)
    } catch (e: java.lang.Exception) {
        toPermissionDetail(activity)
    }
}

/**
 * 跳转应用详情
 */
private fun toPermissionDetail(activity: Activity) {
    try {
        activity.startActivity(
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(Uri.parse("package:" + activity.packageName))
        )
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }

}
