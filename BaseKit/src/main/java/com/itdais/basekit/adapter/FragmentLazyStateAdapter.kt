package com.itdais.basekit.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @description viewpager2的adapter，当frangment可见的时候在去调用onResume
 * @author ding.jw
 */
class FragmentLazyStateAdapter(
    fragmentActivity: FragmentActivity,
    private val fragments: MutableList<Fragment>
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]
}