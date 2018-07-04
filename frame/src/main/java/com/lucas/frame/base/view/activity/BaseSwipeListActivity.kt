package com.lucas.frame.base.view.activity

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lucas.frame.R
import com.lucas.frame.base.adapter.BaseQuickAdapter
import com.lucas.frame.base.adapter.BaseViewHolder
import com.lucas.frame.base.mvp.BaseModel
import com.lucas.frame.base.mvp.IPresenter
import com.lucas.frame.data.bean.IBean
import com.lucas.frame.utils.Config

/**
 * @package     com.lucas.frame.base.view.activity
 * @author      lucas
 * @date        2018/7/4
 * @version     V1.0
 * @describe    自带recycler view的基类
 *               Tips:布局中的recyclerView的id必须是frame_recycleView
 */
abstract class BaseSwipeListActivity<P : IPresenter<*>, B : IBean, AB : Any> : BaseSwipeActivity<P, B>() {
    lateinit var mRecyclerView: RecyclerView
    //当前分页
    var page = 1
    lateinit var mEmptyView: View

    override fun initComment() {
        mRecyclerView = findViewById(R.id.frame_recycleView)
        if (mRecyclerView == null) {
            throw RuntimeException("布局中必须有RecyclerView，并且RecyclerView中的ID为frame_recycleView")
        }
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = getAdapter()
        mEmptyView = LayoutInflater.from(this).inflate(R.layout.frame_view_pager_no_data, mRecyclerView.parent as ViewGroup, false)
        super.initComment()
    }

    //获取adapter
    abstract fun getAdapter(): BaseQuickAdapter<AB, BaseViewHolder>

    //自动更新adapter状态
    fun notifyAdapterStatus(data: List<AB>?, mode: BaseModel.RequestMode) {
        if (mode == BaseModel.RequestMode.LOAD_MODE) {
            if (data == null) {
                //隐藏没有更多数据View
                getAdapter().loadMoreEnd(false)
            } else {
                page++
                getAdapter().addData(data)
                if (data.size < Config.PAGE_COUNT) {
                    getAdapter().loadMoreEnd(false)
                } else {
                    getAdapter().loadMoreComplete()
                }
            }
        } else {
            if (data == null || data.isEmpty()) {
                getAdapter().emptyView = mEmptyView
                return
            }
            page = 0
            if (data.size == Config.PAGE_COUNT) {
                //添加下来加载更多的监听
                getAdapter().setOnLoadMoreListener({
                    loadMoreRequest()
                },mRecyclerView)
                page++
            }
            getAdapter().setNewData(data)

        }
    }

    override fun onRefresh() {
        getAdapter().setEnableLoadMore(false)
        refreshRequest()
        super.onRefresh()
    }

    //刷新的要发送的请求
    abstract fun refreshRequest()

    //加载更多时要发送的请求
    abstract fun loadMoreRequest()

    //重置刷新
    override fun resetRecyclerAndRefresh() {
        super.resetRecyclerAndRefresh()
        if (!getAdapter().isLoadMoreEnable) {
            getAdapter().setEnableLoadMore(true)
        }
    }
}