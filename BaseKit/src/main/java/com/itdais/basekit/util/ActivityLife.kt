package com.itdais.basekit.util

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
import android.os.Process
import java.util.*

/**
 * @author ding.jw
 * @description
 * 页面生命周期
 */
object ActivityLife {
    //页面监听器
    val activityLifecycle: AppActivityLifecycleImpl

    //展示统计
    private var mActivityCount = 0
    private val mActivityList = LinkedList<Activity>()

    init {
        activityLifecycle = AppActivityLifecycleImpl()
    }

    /**
     * 清空页面
     */
    fun clearActivity() {
        for (activity in mActivityList) {
            activity?.finish()
        }
        mActivityList.clear()
    }

    /**
     * 把activity放置等到最上层
     */
    fun setTopActivity(activity: Activity) {
        if (mActivityList.contains(activity)) {
            if (mActivityList.last != activity) {
                mActivityList.remove(activity)
                mActivityList.addLast(activity)
            }
        } else {
            mActivityList.addLast(activity)
        }
    }

    /**
     * 应用是否在前台
     *
     * @return
     */
    fun isForeground(): Boolean {
        return mActivityCount > 0
    }

    /**
     * 退出应用
     */
    fun appExit() {
        try {
            clearActivity()
            val activityMgr = Utils.getContext()
                .getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
            activityMgr.killBackgroundProcesses(Utils.getContext().packageName)
            Process.killProcess(Process.myPid())
            System.exit(0)
        } catch (e: Exception) {
        }
    }

    class AppActivityLifecycleImpl : ActivityLifecycleCallbacks {

        override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
            mActivityList.add(activity)
        }

        override fun onActivityStarted(activity: Activity) {
            mActivityCount++
        }

        override fun onActivityResumed(activity: Activity) {}
        override fun onActivityPaused(activity: Activity) {}
        override fun onActivityStopped(activity: Activity) {
            mActivityCount--
            if (mActivityCount == 0) {
                //此时切后台
            }
        }

        override fun onActivitySaveInstanceState(
            activity: Activity,
            bundle: Bundle
        ) {
        }

        override fun onActivityDestroyed(activity: Activity) {
            mActivityList.remove(activity)
        }
    }
}