package com.itdais.basekit.base

import android.app.ProgressDialog
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.itdais.basekit.adapter.TablayoutAdapter

/**
 * 基础activity
 * @description
 * @author ding.jw
 */
abstract class BaseActivity(contentLayoutId: Int) : AppCompatActivity(contentLayoutId) {
    var mProgressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        initView(savedInstanceState)
    }

    abstract fun initView(savedInstanceState: Bundle?)

    override fun onDestroy() {
        super.onDestroy()
        hideProgress()
    }

    fun showProgress() {
        if (null != mProgressDialog) {
            mProgressDialog = ProgressDialog(this)
        }
        mProgressDialog?.show()
    }

    fun hideProgress() {
        mProgressDialog?.dismiss()
    }
}