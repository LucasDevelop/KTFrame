package com.lucas.frame.base.mvp

import android.app.Activity
import android.support.v4.widget.SwipeRefreshLayout
import android.view.ViewGroup
import com.lucas.frame.data.bean.IBean


/**
 * @创建者     lucas
 * @创建时间   2017/12/23 0023 16:28
 * @描述          TODO
 */
interface IView<B:IBean> {
    fun showProgress(isCancel:Boolean=false)
    fun hideProgress()
    fun requestSuccess(data: B)
    fun requestFail(data: B,msg:String?)
    fun showLoadingView()
    fun showEmptyView(msg: String?)
    fun showNetError()
    fun refreshView()
    fun showLoadMoreError()
    fun resetRecyclerAndRefresh()
    //token过期
    fun tokenOverdue()

    //关闭swipeRefreshView
    fun setRefreshing(isRefresh: Boolean) {
        if (this is Activity) {
            val root = findViewById<ViewGroup>(android.R.id.content)
            getViewByType(root)?.isRefreshing = isRefresh
        }
    }

    fun getViewByType(root: ViewGroup?): SwipeRefreshLayout? {
        (root?.childCount!! - 1).downTo(0).forEach {
            val childAt = root.getChildAt(it)
            if (childAt is SwipeRefreshLayout)
                return childAt
            if (childAt is ViewGroup)
                getViewByType(childAt)
        }
        return null
    }

    //关闭界面
    fun finishView() {
        if (this is Activity)
            finish()
    }

}