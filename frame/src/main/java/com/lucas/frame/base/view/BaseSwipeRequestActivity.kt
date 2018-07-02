package com.lucas.frame.base.view

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.lucas.frame.R
import com.lucas.frame.base.mvp.IPresenter
import com.lucas.frame.data.bean.IBean

/**
 * @package     com.lucas.frame.base.view
 * @author      lucas
 * @date        2018/7/2
 * @version     V1.0
 * @describe    带RecyclerView的基类
 */
abstract class BaseSwipeRequestActivity<P : IPresenter<*>, B : IBean> :BaseRequestActivity<P,B>(){
    lateinit var mRecyclerView:RecyclerView

    override fun initComment() {
        mRecyclerView = findViewById(R.id.frame_recycleView)
        if (mRecyclerView==null){
            throw RuntimeException("布局中必须有RecyclerView，并且RecyclerView中的ID为frame_recycleView")
        }
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        super.initComment()
    }
}