package com.itdais.basekit.base

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rxjava.rxlife.ScopeViewModel

/**
 * Author:  ding.jw
 * Description:
 * 基础ViewModel,继承自rxlife的ScopeViewModel
 * <p>
 *     rxjava
 *     java代码直接使用as(this),kotlin直接使用asLife(this)
 * </p>
 * <p>
 *      携程coroutine
 *      如果使用协程coroutines，使用viewModelScope.launch或者viewModelScope.async
 *      如果使用了第三方如com.ljx.rxlife:rxlife-coroutine，可以使用rxLifeScope.launch或rxLifeScope.async
 * </p>
 */
class BaseViewModel(application: Application) : ScopeViewModel(application) {
    private val _showProgressLive = MutableLiveData<Boolean>()
    val showProgressLive: LiveData<Boolean> = _showProgressLive

    fun showProgress() {
        _showProgressLive.postValue(true)
    }

    fun hideProgress() {
        _showProgressLive.postValue(false)
    }
}