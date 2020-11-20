package com.itdais.basekit.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author ding.jw
 * @description 如果使用协程coroutines，使用viewModelScope.launch或者viewModelScope.async
 */
class BaseViewModel : ViewModel() {
    private val _showProgressLive = MutableLiveData<Boolean>()
    val showProgressLive: LiveData<Boolean> = _showProgressLive

    fun showProgress() {
        _showProgressLive.postValue(true)
    }

    fun hideProgress() {
        _showProgressLive.postValue(false)
    }

}