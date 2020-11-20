package com.itdais.basekit.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

/**
 * @author ding.jw
 * @description
 */
abstract class BaseMvvmFragment<VM : BaseViewModel>(contentLayoutId: Int) :
    BaseFragment(contentLayoutId) {

    lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        initObserve()
    }

    private fun initViewModel() {
        val modelClass: Class<VM>?
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            modelClass = type.actualTypeArguments[0] as Class<VM>
        } else {
            //如果没有指定泛型参数，则默认使用BaseViewModel
            modelClass = BaseViewModel::class.java as Class<VM>
        }
        mViewModel = ViewModelProvider(this)[modelClass]
        mViewModel.showProgressLive.observe(this, Observer {
            if (it) {
                showProgress()
            } else {
                hideProgress()
            }
        })
    }

    abstract fun initObserve()

}