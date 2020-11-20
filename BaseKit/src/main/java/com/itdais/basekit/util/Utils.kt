package com.itdais.basekit.util

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.Resources

/**
 * 工具类Context提供类
 * 需要在Application中初始化
 *
 * @author ding.jw
 */
class Utils private constructor() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var sApplication: Application? = null

        /**
         * 初始化工具类
         *
         * @param context 上下文
         */
        fun init(context: Context) {
            sApplication = context.applicationContext as Application
        }

        /**
         * 获取上下文
         *
         * @return
         */
        @JvmStatic
        val context: Context
            get() = sApplication!!

        /**
         * 获取资源类
         *
         * @return
         */
        @JvmStatic
        val resources: Resources
            get() = sApplication!!.resources
    }

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }
}