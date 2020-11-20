package com.itdais.basekit.base

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/**
 * androidx下的fragment
 * 如果是嵌套中viewpager中：
 * <pre>
 * 1、建议使用：ViewPager2
 *      禁用预加载:viewPager2.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
 *      ViewPager2本身就支持对实际可见的 Fragment 才调用 onResume 方法
 *      ViewPager2的adapter为androidx.viewpager2.adapter.FragmentStateAdapter
 * 2、viewpager中的fragment：adapter使用
 *      静态或页面少,会直接存储到内存中，使用->
 *      androidx.fragment.app.FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
 *      数据动态性较大、占用内存较多的情况，使用->
 *      androidx.fragment.app.FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
 * </pre>
 * 如果使用add+hide+show方法
 * 建议使用FragmentTransaction.setMaxLifecycle()来控制Fragment生命周期

 * @author ding.jw
 * @description
 */
abstract class BaseFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {
    var mProgressDialog: ProgressDialog? = null
    private var isLoaded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if (!isLoaded && !isHidden) {
            lazyInitData()
            isLoaded = true
        }
    }

    abstract fun initView(view: View, savedInstanceState: Bundle?)

    abstract fun lazyInitData()

    override fun onDestroyView() {
        super.onDestroyView()
        hideProgress()
    }

    fun showProgress() {
        if (null != mProgressDialog) {
            mProgressDialog = ProgressDialog(context)
        }
        mProgressDialog?.show()
    }

    fun hideProgress() {
        mProgressDialog?.dismiss()
    }

}