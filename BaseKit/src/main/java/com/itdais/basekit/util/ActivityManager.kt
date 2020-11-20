package com.itdais.basekit.util

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
import android.os.Process
import com.itdais.basekit.util.Utils.Companion.context
import java.util.*

/**
 * @author ding.jw
 * @description
 */
object ActivityManager {
    //页面监听器
    val activityLifecycle: AppActivityLifecycleImpl

    init {
        activityLifecycle = AppActivityLifecycleImpl()
    }

    /**
     * 清空页面
     */
    fun clearActivity() {
        activityLifecycle.clearActivity()
    }

    /**
     * 退出应用
     */
    fun appExit() {
        try {
            clearActivity()
            val activityMgr =
                context!!.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
            activityMgr?.killBackgroundProcesses(context!!.packageName)
            Process.killProcess(Process.myPid())
            System.exit(0)
        } catch (e: Exception) {
        }
    }

    class AppActivityLifecycleImpl : ActivityLifecycleCallbacks {
        private val mActivityList = Stack<Activity>()
        override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
            mActivityList.add(activity)
        }

        override fun onActivityStarted(activity: Activity) {}
        override fun onActivityResumed(activity: Activity) {}
        override fun onActivityPaused(activity: Activity) {}
        override fun onActivityStopped(activity: Activity) {}
        override fun onActivitySaveInstanceState(
            activity: Activity,
            bundle: Bundle
        ) {
        }

        override fun onActivityDestroyed(activity: Activity) {
            mActivityList.remove(activity)
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
    }

}