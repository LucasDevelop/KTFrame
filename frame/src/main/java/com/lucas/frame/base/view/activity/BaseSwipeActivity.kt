package com.lucas.frame.base.view.activity

import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import com.lucas.frame.base.mvp.IPresenter
import com.lucas.frame.data.bean.IBean

/**
 * @package     com.lucas.frame.base.view.activity
 * @author      lucas
 * @date        2018/7/4
 * @version     V1.0
 * @describe    带下拉刷新的基类
 */
abstract class BaseSwipeActivity<P : IPresenter<*>, B : IBean> : BaseRequestActivity<P, B>(), SwipeRefreshLayout.OnRefreshListener {

    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    //动态添加swipe refresh控件
    override fun initComment() {
        mSwipeRefreshLayout = SwipeRefreshLayout(this)
        val layoutID = getLayoutID()
        if (layoutID != 0)
            mSwipeRefreshLayout.addView(LayoutInflater.from(this).inflate(layoutID, mSwipeRefreshLayout, false))
        setContentView(mSwipeRefreshLayout)
        mSwipeRefreshLayout.setOnRefreshListener(this)
        super.initComment()
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