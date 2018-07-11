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
import com.lucas.frame.base.mvp.IListView
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
abstract class BaseSwipeListActivity<P : IPresenter<*>, B : IBean, AB : Any> : BaseSwipeActivity<P, B>(),IListView<B> {
    lateinit var mRecyclerView: RecyclerView
    //当前分页
    var page = 1
    lateinit var mEmptyView: View
    lateinit var mAdapter:BaseQuickAdapter<AB,BaseViewHolder>

    override fun initComment() {
        super.initComment()
        mRecyclerView = findViewById(R.id.frame_recycleView)
        if (mRecyclerView == null) {
            throw RuntimeException("布局中必须有RecyclerView，并且RecyclerView中的ID为frame_recycleView")
        }
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = getAdapter()
        mRecyclerView.adapter = mAdapter
        mEmptyView = LayoutInflater.from(this).inflate(R.layout.frame_view_pager_no_data, mRecyclerView.parent as ViewGroup, false)
    }

    override fun requestSuccess(data: B) {

    }

    //获取adapter
    abstract fun getAdapter(): BaseQuickAdapter<AB, BaseViewHolder>

    //自动更新adapter状态
    fun notifyAdapterStatus(data: List<AB>?, mode: BaseModel.RequestMode) {
        if (mode == BaseModel.RequestMode.LOAD_MODE) {
            if (data == null) {
                //隐藏没有更多数据View
                mAdapter.loadMoreEnd(false)
            } else {
                page++
                mAdapter.addData(data)
                if (data.size < Config.PAGE_COUNT) {
                    mAdapter.loadMoreEnd(false)
                } else {
                    mAdapter.loadMoreComplete()
                }
            }
        } else {
            if (data == null || data.isEmpty()) {
                mAdapter.emptyView = mEmptyView
                return
            }
            page = 1
            if (data.size == Config.PAGE_COUNT) {
                //添加下来加载更多的监听
                mAdapter.setOnLoadMoreListener({
                    loadMoreListRequest(page)
                },mRecyclerView)
                page++
            }
            mAdapter.setNewData(data)
        }
    }

    override fun onRefresh() {
        mAdapter.setEnableLoadMore(false)
//        refreshListRequest()
        onRefreshRequest()
        super.onRefresh()
    }

    //刷新的要发送的请求
//    abstract fun refreshListRequest()

    //加载更多时要发送的请求
    abstract fun loadMoreListRequest(page: Int)


    //重置刷新
    override fun resetRecyclerAndRefresh() {
        super.resetRecyclerAndRefresh()
        if (!mAdapter.isLoadMoreEnable) {
            mAdapter.setEnableLoadMore(true)
        }
    }
}