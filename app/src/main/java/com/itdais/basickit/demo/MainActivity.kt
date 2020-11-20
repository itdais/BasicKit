package com.itdais.basickit.demo

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.itdais.basekit.base.BaseMvvmActivity
import com.itdais.basekit.base.BaseViewModel
import com.itdais.basekit.util.loadRootFragment
import com.itdais.basekit.util.loadShowFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseMvvmActivity<BaseViewModel>(R.layout.activity_main) {
    override fun initView(savedInstanceState: Bundle?) {
        val fragments = listOf<Fragment>(TestFragment(), Test2Fragment(), Test3Fragment())
        btn_one.setOnClickListener {
            loadShowFragment(R.id.content_view, fragments[0])
        }
        btn_two.setOnClickListener {
            loadShowFragment(R.id.content_view, fragments[1])
        }
        btn_three.setOnClickListener {
            loadShowFragment(R.id.content_view, fragments[2])
        }
    }

    override fun initObserve() {

    }

    override fun initData() {

    }
}