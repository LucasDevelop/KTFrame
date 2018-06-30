package com.lucas.frame.base.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.support.v7.widget.CardView
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.PopupWindow
import android.widget.RadioGroup
import com.lucas.frame.R
import com.lucas.frame.interceptor.SpringScaleInterpolator
import java.util.*

/**
 * @file       BasePopup.kt
 * @brief      描述
 * @author     lucas
 * @date       2018/1/23 0023
 * @version    V1.0
 * @par        Copyright (c):
 * @par History:
 *             version: zsr, 2017-09-23
 */
abstract class BasePopup(val context: Context) : PopupWindow(context) {
    var mRootView: View
    val mHandler = Handler()

    init {
        //防止PopupWindow被软件盘挡住
//        softInputMode = PopupWindow.INPUT_METHOD_NEEDED
        softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        mRootView = LayoutInflater.from(context).inflate(getLayoutId(), null, false)
        contentView = mRootView
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.MATCH_PARENT
        setBackgroundDrawable(ColorDrawable(context.resources.getColor(android.R.color.transparent)))
        isOutsideTouchable = true
        initView(mRootView)
    }

    abstract fun initView(mRootView: View?)

    abstract fun getLayoutId(): Int

    fun showInView(view: View) {
        if (mRootView == null) return
        if (!isShowing)
            showAtLocation(view, Gravity.CENTER, 0, 0)
    }

    /**
     * 弹窗自定义动画
     */
    fun showAnim(view: View, animID: Int = -1) {
        val animation = if (animID != -1)
            AnimationUtils.loadAnimation(context, animID)
        else
            AnimationUtils.loadAnimation(context, R.anim.frame_popup_a_t)
        mRootView.startAnimation(animation)
        showAtLocation(view, Gravity.CENTER, 0, 0)
    }

    /**
     * cardview飘浮动画
     */
    fun showCardFloatAnim(view: View) {
        //找出所有的cardview执行飘浮
        val list = ArrayList<View>()
        findAllCardView(mRootView, list)
        var index = 0
        list.forEach {
            mHandler.postDelayed({
                it.visibility = View.VISIBLE
                val anim2 = ObjectAnimator.ofFloat(it, "alpha", 0.0f, 1f)
                val anim1 = ObjectAnimator.ofFloat(it, "translationY", 100f, 0f)
                val set = AnimatorSet()
                set.play(anim1).with(anim2)
                set.duration = 800
                set.interpolator = SpringScaleInterpolator(0.4f)
                set.start()
            }, 200L * index)
            index++
        }
        showAtLocation(view, Gravity.BOTTOM, 0, 0)
    }

    /**
     * 该方法谨慎使用，容易导致内存溢出
     */
    private fun findAllCardView(view: View, list: ArrayList<View>) {
        if (view is ViewGroup && view !is CardView) {
            for (index in 0 until view.childCount) {
                findAllCardView(view.getChildAt(index), list)
            }
        }
        if (view is CardView)
            list.add(view)
    }

    /**
     * 优化事件设置
     * 目标：布局ID或者View
     * 作用：给控件添加任何事件
     */
    open fun Array<out Any>.setViewListener(listener: Any) {
        this.forEach {
            when (listener) {
                is View.OnClickListener -> {
                    if (it is Int)
                        mRootView.findViewById<View>(it).setOnClickListener(listener)
                    (it as? View)?.setOnClickListener(listener)
                }
                is View.OnLongClickListener -> {
                    if (it is Int)
                        mRootView.findViewById<View>(it).setOnLongClickListener(listener)
                    (it as? View)?.setOnLongClickListener(listener)
                }
                is View.OnTouchListener -> {
                    if (it is Int)
                        mRootView.findViewById<View>(it).setOnTouchListener(listener)
                    (it as? View)?.setOnTouchListener(listener)
                }
                is RadioGroup.OnCheckedChangeListener -> {
                    if (it is Int)
                        mRootView.findViewById<RadioGroup>(it).setOnCheckedChangeListener(listener)
                    (it as? RadioGroup)?.setOnCheckedChangeListener(listener)
                }
            }
        }
    }
}