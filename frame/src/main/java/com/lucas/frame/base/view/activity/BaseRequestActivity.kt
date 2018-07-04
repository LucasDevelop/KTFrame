package com.lucas.frame.base.view.activity

import android.content.DialogInterface
import android.os.Bundle
import android.view.ViewGroup
import com.blankj.utilcode.util.ToastUtils
import com.lucas.frame.R

import com.lucas.frame.base.mvp.IPresenter
import com.lucas.frame.base.mvp.IView
import com.lucas.frame.data.bean.IBean
import com.lucas.frame.loading.VaryViewHelperController
import com.lucas.frame.widget.CustomProgress

/**
 * @package     com.lucas.frame.base.view
 * @author      lucas
 * @date        2018/6/30
 * @version     V1.0
 * @describe    带网络请求的界面需集成该类
 */
abstract class BaseRequestActivity<P : IPresenter<*>, B : IBean> : BaseActivity(), IView<B> {

    lateinit var mPresenter: P
    //通用加载控件
    var mCustomProgress: CustomProgress? = null
    //网络状态同步布局控制器
    var mVaryViewHelperController: VaryViewHelperController? = null

    var mCancelListener = DialogInterface.OnCancelListener {
        if (hasProgressStyle() && mCustomProgress?.isShowing!!)
            mCustomProgress?.dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mPresenter = getPresenter()
        super.onCreate(savedInstanceState)
    }

    override fun initComment() {
        initBaseView()
        super.initComment()
    }

    //请求相关的控件初始化
    private fun initBaseView() {
        if (hasProgressStyle())
            mCustomProgress = CustomProgress(this)
        val rootView = findViewById<ViewGroup>(R.id.frame_root_view)
        if (rootView == null) {
//            throw RuntimeException("布局中没有指定frame_root_view ID")
        } else {
            mVaryViewHelperController = VaryViewHelperController(rootView)
        }
    }

    abstract fun getPresenter(): P

    //是否使用加载样式
    fun hasProgressStyle() = true

    override fun showProgress(isCancel: Boolean) {
        mCustomProgress?.show(getResStr(R.string.frame_request_loading), isCancel, mCancelListener)
    }

    override fun hideProgress() {
        mCustomProgress?.hide()
    }

    //请求失败后显示toast
    override fun requestFail(data: B, msg: String?) {
        ToastUtils.showShort(msg ?: getResStr(R.string.frame_request_error))
    }

    //显示正在加载View
    override fun showLoadingView() {
        mVaryViewHelperController?.showLoading()
    }

    //显示无数据View
    override fun showEmptyView(msg: String?) {
        mVaryViewHelperController?.showEmpty(msg ?: getResStr(R.string.frame_view_no_data))
    }

    //显示数据加载失败View，以及用户触发重新加载
    override fun showNetError() {
        if (mVaryViewHelperController != null){
            mVaryViewHelperController?.showNetworkError {
                reRequestData()
            }
        }else{
            getResStr(R.string.frame_view_net_error).showToast()
        }
    }

    override fun refreshView() {
        mVaryViewHelperController?.restore()
    }

    //加载更多失败时
    override fun showLoadMoreError() {

    }

    //token过期
    override fun tokenOverdue() {

    }

    //加载数据失败时，用户点击重新加载时重新请求数据
    open fun reRequestData() {

    }

    override fun onDestroy() {
        if (hasProgressStyle() && mCustomProgress?.isShowing!!)
            mCustomProgress?.dismiss()
        super.onDestroy()
    }

    override fun resetRecyclerAndRefresh() {

    }
}