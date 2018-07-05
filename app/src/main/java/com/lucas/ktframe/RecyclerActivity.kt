package com.lucas.ktframe

import com.lucas.frame.base.adapter.BaseQuickAdapter
import com.lucas.frame.base.adapter.BaseViewHolder
import com.lucas.frame.base.mvp.BaseModel
import com.lucas.frame.base.view.activity.BaseSwipeListActivity
import com.lucas.frame.data.bean.IBean

class RecyclerActivity : BaseSwipeListActivity<RecyclerPresenter, IBean, ListBean.DataBean.DatasBean>() {
    override fun requestSuccess(data: IBean, mode: BaseModel.RequestMode) {
        if (data is ListBean)
            notifyAdapterStatus(data.data.datas,mode)
    }

    override fun getAdapter(): BaseQuickAdapter<ListBean.DataBean.DatasBean, BaseViewHolder> = RecyclerAdapter(R.layout.item_list)

    override fun loadMoreListRequest(page: Int) {
        mPresenter.loadData(false, page)
    }

    override fun onRefreshRequest() {
        mPresenter.loadData(true)
    }

    override fun getPresenter(): RecyclerPresenter = RecyclerPresenter(this)

    override fun initView() {
        setToolbar("List")
    }

    override fun initData() {
        mPresenter.loadData(true)
    }

    override fun getLayoutID(): Int = R.layout.activity_recycler


}
