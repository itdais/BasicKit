package com.itdais.basekit.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter

/**
 * TabLayout 适配器使用FragmentStatePagerAdapter
 * 使用super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
 *
 * 如果是viewpager2不建议使用该adapter，推荐使用FragmentStateAdapter
 * @author ding.jw
 */
class TablayoutAdapter(
    fm: FragmentManager,
    private val mFragmentList: List<Fragment>,
    private val mTitleList: List<String>? = null
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    constructor(
        fm: FragmentManager,
        fragmentList: List<Fragment>,
        titleArr: Array<String>? = null
    ) : this(fm, fragmentList, titleArr?.let { listOf<String>(*it) })

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitleList?.get(position) ?: ""
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }
}