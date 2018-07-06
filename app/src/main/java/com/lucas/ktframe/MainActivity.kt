package com.lucas.ktframe

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import com.lucas.frame.base.view.activity.BaseSwipeActivity
import com.lucas.frame.data.bean.IBean
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseSwipeActivity<MainPresenter, IBean>() {
    override fun onRefreshRequest() {
        mPresenter.login()
    }

    override fun getPresenter(): MainPresenter = MainPresenter(this)

    override fun initView() {
        v_list.layoutManager = LinearLayoutManager(this)
        val mainAdapter = MainAdapter(R.layout.item_list)
        v_list.adapter = mainAdapter

        mainAdapter.setNewData(arrayOf("aaa", "bbbb", "vvvv", "wwwww", "rrrrrr").toList())

    }

    override fun initData() {
        handler.postDelayed({
            v_text.text = "loading"
            mPresenter.login()
        }, 2000)
        v_text.setOnClickListener {
            startActivity(Intent(this, RecyclerActivity::class.java))
        }
    }

    override fun getLayoutID(): Int = R.layout.activity_main

    override fun requestSuccess(data: IBean) {
        "requestSuccess".ld()
        v_text.text = data.errorMsg
    }

    //重新加载
    override fun reRequestData() {
        mPresenter.login()
    }

}
