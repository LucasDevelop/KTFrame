package com.lucas.ktframe

import com.lucas.frame.base.mvp.BaseModel
import com.lucas.frame.base.mvp.IPresenter
import com.lucas.ktframe.common.data.DataManager

class RecyclerPresenter(recyclerActivity: RecyclerActivity) : IPresenter<RecyclerActivity>(recyclerActivity) {

    fun loadData(isRefresh: Boolean, page: Int = 1) {
        val mode: BaseModel.RequestMode = if (isRefresh) BaseModel.RequestMode.FIRST else BaseModel.RequestMode.LOAD_MODE
        mM.request {
            call = DataManager.commonServer.getList(page)
            requestMode = mode
            loadStyle = BaseModel.ProgressStyle.DIALOG
            _success = { dataBean, reqMode ->
                mV.requestSuccess(dataBean,reqMode)
            }
        }
    }
}