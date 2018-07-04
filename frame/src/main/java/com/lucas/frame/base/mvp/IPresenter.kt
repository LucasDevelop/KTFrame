package com.lucas.frame.base.mvp

import android.app.Activity
import android.os.Handler
import com.lucas.frame.FrameApp
import com.lucas.frame.data.bean.IBean
import com.lucas.frame.helper.CommentHelper


/**
 * @创建者     lucas
 * @创建时间   2017/12/23 0023 16:30
 * @描述          TODO
 */
open class IPresenter<out V : IView<IBean>>(val mV: V) : CommentHelper {

    //注入modelV
    var mM: BaseModel = BaseModel(mV)
    var mApp: FrameApp = FrameApp.INSTANCE
    lateinit var mHandler: Handler

    fun finishActivity() {
        (mV as Activity).finish()
    }

}