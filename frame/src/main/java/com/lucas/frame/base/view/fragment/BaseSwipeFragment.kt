package com.lucas.frame.base.view.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lucas.frame.base.mvp.IPresenter
import com.lucas.frame.data.bean.IBean

abstract class BaseSwipeFragment<P : IPresenter<*>, B : IBean> : BaseRequestFragment<P, B>(), SwipeRefreshLayout.OnRefreshListener {

    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        mSwipeRefreshLayout = SwipeRefreshLayout(getAc())
        mSwipeRefreshLayout.addView(view)
        mSwipeRefreshLayout.setOnRefreshListener(this)
        return mSwipeRefreshLayout
    }

    override fun onRefresh() {
        onRefreshRequest()
    }

    //下拉刷新时的回调
    abstract fun onRefreshRequest()

    //下拉刷新数据请求成功后调用
    override fun resetRecyclerAndRefresh() {
        if (mSwipeRefreshLayout.isRefreshing)
            mSwipeRefreshLayout.isRefreshing = false
        if (!mSwipeRefreshLayout.isEnabled)
            mSwipeRefreshLayout.isEnabled = true
    }
}