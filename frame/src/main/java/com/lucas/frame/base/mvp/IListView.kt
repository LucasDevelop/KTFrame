package com.lucas.frame.base.mvp

import com.lucas.frame.data.bean.IBean


/**
 * @创建者     lucas
 * @创建时间   2017/12/23 0023 16:28
 * @描述          带recycler view的界面回调
 */
interface IListView<B:IBean> :IView<B>{
    fun requestSuccess(data: B,mode:BaseModel.RequestMode)
}