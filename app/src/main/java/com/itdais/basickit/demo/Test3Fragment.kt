
package com.itdais.basickit.demo

import android.os.Bundle
import android.view.View
import com.itdais.basekit.base.BaseFragment
import com.itdais.basekit.base.BaseMvvmActivity
import com.itdais.basekit.base.BaseViewModel
import com.itdais.basekit.util.loadRootFragment
import com.itdais.basekit.util.loadShowFragment
import kotlinx.android.synthetic.main.activity_main.*

class Test3Fragment : BaseFragment(R.layout.fragment_main3) {
    override fun initView(view: View, savedInstanceState: Bundle?) {
    }

    override fun lazyInitData() {
    }

}