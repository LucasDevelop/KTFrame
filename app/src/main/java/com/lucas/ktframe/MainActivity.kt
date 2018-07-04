package com.lucas.ktframe

import com.lucas.frame.base.view.activity.BaseRequestActivity
import com.lucas.frame.base.view.activity.BaseSwipeActivity
import com.lucas.frame.data.bean.IBean
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseSwipeActivity<MainPresenter, IBean>() {
    override fun onRefreshRequest() {
        mPresenter.login()
    }

    override fun getPresenter(): MainPresenter = MainPresenter(this)

    override fun initView() {
    }

    override fun initData() {
        handler.postDelayed({
            v_text.text = "loading"
        mPresenter.login()
        },2000)
    }

    override fun getLayoutID(): Int = R.layout.activity_main

    override fun requestSuccess(data: IBean) {
        "requestSuccess".ld()
        v_text.text = data.message
    }

    //重新加载
    override fun reRequestData() {
        mPresenter.login()
    }

}
