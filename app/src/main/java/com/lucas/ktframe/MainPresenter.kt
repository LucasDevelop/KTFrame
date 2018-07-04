package com.lucas.ktframe

import com.lucas.frame.base.mvp.BaseModel
import com.lucas.frame.base.mvp.IPresenter
import com.lucas.frame.data.bean.IBean
import com.lucas.ktframe.common.data.DataManager

class MainPresenter(mainActivity: MainActivity) :IPresenter<MainActivity>(mainActivity) {

    fun login(){
        mM.request {
            call = DataManager.commonServer.login("18825204205","111111")
            requestMode = BaseModel.RequestMode.FIRST
            loadStyle = BaseModel.ProgressStyle.VIEW
            _success ={dataBean: IBean, reqMode: BaseModel.RequestMode ->
                mV.requestSuccess(dataBean)
            }
        }
    }
}