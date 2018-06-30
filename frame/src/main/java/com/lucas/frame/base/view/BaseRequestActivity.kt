package com.lucas.frame.base.view

import android.os.Bundle

import com.lucas.frame.base.mvp.IPresenter

/**
 * @package     com.lucas.frame.base.view
 * @author      lucas
 * @date        2018/6/30
 * @version     V1.0
 * @describe    带网络请求的界面需集成该类
 */
abstract class BaseRequestActivity<P : IPresenter<*>> : BaseActivity() {

    lateinit var mPresenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        mPresenter = getPresenter()
        super.onCreate(savedInstanceState)
    }

    abstract fun getPresenter():P


}