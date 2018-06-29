package com.lucas.frame.base.mvp

import android.app.Activity
import android.os.Handler
import com.lucas.frame.FrameApp
import com.lucas.frame.helper.CommentHelper


/**
 * @创建者     lucas
 * @创建时间   2017/12/23 0023 16:30
 * @描述          TODO
 */
open class IPresenter<V : IView>(val mV: IView) : CommentHelper {

    //注入model
//     lateinit var mM: BaseModel
     var mApp: FrameApp = FrameApp.INSTANCE
     lateinit var mHandler:Handler
//     lateinit var mApi:ApiService

    fun finishActivity(){
        (mV as Activity).finish()
    }

}