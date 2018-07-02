package com.lucas.frame.base.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lucas.frame.base.mvp.IPresenter
import com.lucas.frame.base.mvp.IView
import com.lucas.frame.data.bean.IBean

/**
 * @package     com.lucas.frame.base.view
 * @author      lucas
 * @date        2018/6/30
 * @version     V1.0
 * @describe    带网络请求的界面需集成该类
 */
abstract class BaseRequestFragment<P : IPresenter<*>,B:IBean> : BaseFragment(),IView<B> {
    lateinit var mIPresenter: P

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mIPresenter = getPresenter()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    abstract fun getPresenter(): P

}
