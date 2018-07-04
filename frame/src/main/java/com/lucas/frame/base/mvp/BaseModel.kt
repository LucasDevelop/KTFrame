package com.lucas.frame.base.mvp

import com.blankj.utilcode.util.NetworkUtils
import com.lucas.frame.FrameApp
import com.lucas.frame.data.bean.IBean
import com.lucas.frame.helper.CommentHelper
import com.lucas.frame.utils.Config
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.components.support.RxFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @file       BaseModelkt
 * @brief      网络请求解析
 * @author     lucas
 * @date       2018/4/12 0012
 * @version    V1.0
 * @par        Copyright (c):
 * @par History:
 *             version: zsr, 2017-09-23
 */
open class BaseModel(val iView: IView<IBean>) : CommentHelper {

    private val TAG = "net"
    var mFrameApp: FrameApp = FrameApp.INSTANCE
    //请求体
    lateinit var call: Observable<*>
    //加载显示的样式
    var loadStyle = ProgressStyle.NONE
    //请求模式
    var requestMode = RequestMode.FIRST
    //成功回调
    var _success: (dataBean: IBean, reqMode: RequestMode) -> Unit = { dataBean, reqMode -> }
    //失败回调
    var _fail: (e: Throwable) -> Unit = {}
    //是否只用WiFi访问网络
    var isOnlyUseWifi = false
    //同步生命周期
    var isSyncLifeCycle = true

    enum class ProgressStyle {
        NONE,//什么都不显示
        VIEW,//显示布局样式
        DIALOG,//显示popupWindow样式
        CUSTOM//显示自定义样式
    }

    enum class RequestMode {
        FIRST,//首次加载
        REFRESH,//刷新数据
        LOAD_MODE//加载更多
    }

    //自定义样式
    var _customProgress: () -> Unit = {}
    var _customError: () -> Unit = {}

    fun request(init: BaseModel.() -> Unit) {
        //初始化
        init()
        loadBeginView()
        val checkNetWork = checkNetWork()
        if (checkNetWork){
            loadErrorView()
            return
        }
        //请求数据
        val observable = call.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
        //同步生命周期
        if (isSyncLifeCycle) {
            if (iView is RxAppCompatActivity)
                observable?.compose(iView.bindUntilEvent(ActivityEvent.DESTROY))
            if (iView is RxFragment)
                observable?.compose(iView.bindUntilEvent(FragmentEvent.DESTROY))
        }
        observable?.subscribe({
            onRequestSuccess(it)
        }, {
            loadErrorView()
            _fail(it)
        }, {

        })
    }

    private fun onRequestSuccess(data: Any) {
        iView.resetRecyclerAndRefresh()
        if (data is IBean)
            when (data.status) {
                Config.Request.REQUEST_SUCCESS -> {
                    _success(data, requestMode)
                    disProgressSuccess()
                }
                Config.Request.TOKEN_OVERDUE -> {
                    iView.tokenOverdue()
                }
                else -> {
                    //其他状态码
                    iView.requestFail(data, null)
                    disProgressError()
                }
            }
        else {
            //非 status msg 类型的json数据
//            _success(data, requestMode)
            disProgressSuccess()
        }
    }

    //请求数据失败时关闭progress
    private fun disProgressError() {
        when (requestMode) {
            RequestMode.FIRST -> {
                when (loadStyle) {
                    ProgressStyle.NONE -> {
                    }
                    ProgressStyle.VIEW -> {
                        iView.showNetError()
                    }
                    ProgressStyle.DIALOG -> {
                        iView.hideProgress()
                    }
                    ProgressStyle.CUSTOM -> {
                        _customError()
                    }
                }
            }
            RequestMode.REFRESH -> {
                iView.showNetError()
            }
            RequestMode.LOAD_MODE -> {
                iView.showLoadMoreError()
            }
        }
    }

    //请求数据成功时关闭progress
    private fun disProgressSuccess() {
        if (requestMode == RequestMode.FIRST)
            when (loadStyle) {
                ProgressStyle.NONE -> {
                }
                ProgressStyle.VIEW -> {
                    iView.refreshView()
                }
                ProgressStyle.DIALOG -> {
                    iView.hideProgress()
                }
                ProgressStyle.CUSTOM -> {
                    _customError()
                }
            }
    }

    //预加载view样式
    private fun loadBeginView() {
        when (loadStyle) {
            ProgressStyle.NONE -> {
            }
            ProgressStyle.VIEW -> {
                iView.showLoadingView()
            }
            ProgressStyle.DIALOG -> {
                iView.showProgress()
            }
            ProgressStyle.CUSTOM -> {
                _customProgress()
            }
        }
    }

    //加载失败的样式
    private fun loadErrorView() {
        when (requestMode) {
            RequestMode.FIRST -> {
                when (loadStyle) {
                    ProgressStyle.NONE -> {
                    }
                    ProgressStyle.VIEW -> {
                        iView.showNetError()
                    }
                    ProgressStyle.DIALOG -> {
                        iView.hideProgress()
                    }
                    ProgressStyle.CUSTOM -> {
                        _customError()
                    }
                }
            }
            RequestMode.REFRESH -> {
                iView.showNetError()
            }
            RequestMode.LOAD_MODE -> {
                iView.showLoadMoreError()
            }
        }
        iView.resetRecyclerAndRefresh()
    }

    private fun checkNetWork(): Boolean {
        //判断wifi是否可用
        if (isOnlyUseWifi && !NetworkUtils.isWifiConnected()) {
            "wifi不可用".ld(TAG)
            "wifi不可用".showToast()
            _fail(RuntimeException("wifi不可用"))
            return true
        }
        //判断网络是否可用
        if (!isOnlyUseWifi && !NetworkUtils.isConnected()) {
            "网络不可用".ld(TAG)
            "网络不可用".showToast()
            _fail(RuntimeException("网络不可用"))
            return true
        }
        return false
    }
}