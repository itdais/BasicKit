package com.itdais.basekit.util

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle

/**
 * @author ding.jw
 * @description
 * 扩展Fragment及Activity,使用add+show+hide模式下的Fragment的懒加载
 * 推荐使用FragmentActivity.loadShowHideFragment()，未添加则调用add方法
 *
 * 当调用 Fragment.showHideFragment ，确保已经先调用 Fragment.loadFragments
 * 当调用 FragmentActivity.showHideFragment，确保已经先调用 FragmentActivity.loadFragments
 *
 */

/**
 * 加载并显示fragment，并隐藏其他fragment
 * @param containerViewId 布局id
 * @param showFragment 需要显示的fragment
 */
fun FragmentActivity.loadShowHideFragment(
    @IdRes containerViewId: Int,
    showFragment: Fragment
) {
    loadShowFragmentTransaction(containerViewId, supportFragmentManager, showFragment)
}

/**
 * 加载并显示fragment，并隐藏其他fragment
 * 推荐只加载一次的fragment使用，如果是首页需要切换的 推荐使用 loadShowFragment()方法
 * @param containerViewId 布局id
 * @param showFragment 需要显示的fragment
 */
fun FragmentActivity.loadRootFragment(
    @IdRes containerViewId: Int,
    showFragment: Fragment
) {
    loadFragmentsTransaction(containerViewId, 0, supportFragmentManager, listOf(showFragment))
}

/**
 * 加载同级的Fragment
 * 推荐只加载一次的fragment使用，如果是首页需要切换的 推荐使用 loadShowFragment()方法
 * @param containerViewId 布局id
 * @param showPosition  默认显示的角标
 * @param fragments    加载的fragment
 */
fun FragmentActivity.loadFragments(
    @IdRes containerViewId: Int,
    showPosition: Int = 0,
    fragmentList: List<Fragment>
) {
    loadFragmentsTransaction(containerViewId, showPosition, supportFragmentManager, fragmentList)
}

/**
 * 加载根Fragment
 * @param containerViewId 布局id
 * @param rootFragment  根fragment
 */
fun Fragment.loadRootFragment(@IdRes containerViewId: Int, rootFragment: Fragment) {
    loadFragmentsTransaction(containerViewId, 0, childFragmentManager, listOf(rootFragment))
}

/**
 * 加载同级的Fragment
 * 推荐只加载一次的fragment使用，如果是首页需要切换的 推荐使用 loadShowFragment()方法
 * @param containerViewId 布局id
 * @param showPosition  默认显示的角标
 * @param fragments    加载的fragment
 */
fun Fragment.loadFragments(
    @IdRes containerViewId: Int,
    showPosition: Int = 0,
    fragmentList: List<Fragment>
) {
    loadFragmentsTransaction(
        containerViewId, showPosition, childFragmentManager, fragmentList
    )
}

/**
 * 显示目标fragment，并隐藏其他fragment
 * @param showFragment 需要显示的fragment
 */
fun FragmentActivity.showHideFragment(showFragment: Fragment) {
    showHideFragmentTransaction(supportFragmentManager, showFragment)
}

/**
 * 使用add+show+hide模式加载fragment
 * fragment如未添加，使用add，添加过则show，并隐藏其他的fragment
 * @param containerViewId
 * @param fm FragmentManager
 * @param showFragment 需要显示的fragment
 */
private fun loadShowFragmentTransaction(
    @IdRes containerViewId: Int,
    fm: FragmentManager,
    showFragment: Fragment
) {
    fm.beginTransaction().apply {
        fm.fragments.forEach {
            if (it != showFragment) {
                hide(it)
                setMaxLifecycle(it, Lifecycle.State.STARTED)
            }
        }
        if (!showFragment.isAdded) {
            add(containerViewId, showFragment, showFragment.javaClass.name)
        } else {
            show(showFragment)
        }
        setMaxLifecycle(showFragment, Lifecycle.State.RESUMED)
    }.commit()

}

/**
 * 使用add+show+hide模式加载fragment
 * 重复add会造成崩溃
 * 默认显示位置[showPosition]的Fragment，最大Lifecycle为Lifecycle.State.RESUMED
 * 其他隐藏的Fragment，最大Lifecycle为Lifecycle.State.STARTED
 *
 *@param containerViewId 容器id
 *@param showPosition  fragments
 *@param fragmentManager FragmentManager
 *@param fragments  控制显示的Fragments
 */
private fun loadFragmentsTransaction(
    @IdRes containerViewId: Int,
    showPosition: Int,
    fm: FragmentManager,
    fragmentList: List<Fragment>
) {
    if (fragmentList.isNotEmpty()) {
        fm.beginTransaction().apply {
            for (index in fragmentList.indices) {
                val fragment = fragmentList[index]
                add(
                    containerViewId,
                    fragment,
                    fragment.javaClass.name
                )
                if (showPosition == index) {
                    setMaxLifecycle(fragment, Lifecycle.State.RESUMED)
                } else {
                    hide(fragment)
                    setMaxLifecycle(fragment, Lifecycle.State.RESUMED)
                }
            }
        }.commit()
    } else {
        throw IllegalStateException("fragments must not empty")
    }
}

/**
 * 显示需要显示的Fragment[showFragment]，并设置其最大Lifecycle为Lifecycle.State.RESUMED。
 * 同时隐藏其他Fragment,并设置最大Lifecycle为Lifecycle.State.STARTED
 * @param fragmentManager
 * @param showFragment
 */
private fun showHideFragmentTransaction(fragmentManager: FragmentManager, showFragment: Fragment) {
    fragmentManager.beginTransaction().apply {
        show(showFragment)
        setMaxLifecycle(showFragment, Lifecycle.State.RESUMED)

        //获取其中所有的fragment,其他的fragment进行隐藏
        fragmentManager.fragments.forEach {
            if (it != showFragment) {
                hide(it)
                setMaxLifecycle(it, Lifecycle.State.STARTED)
            }
        }
    }.commit()
}