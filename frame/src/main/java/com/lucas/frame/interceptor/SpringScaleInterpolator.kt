package com.lucas.frame.interceptor

import android.view.animation.Interpolator

/**
 * @file       SpringScaleInterpolator.kt
 * @brief      动画插入器
 * @author     lucas
 * @date       2018/5/10 0010
 * @version    V1.0
 * @par        Copyright (c):
 * @par History:
 *             version: zsr, 2017-09-23
 */
class SpringScaleInterpolator(val factor:Float) :Interpolator{
    override fun getInterpolation(input: Float): Float {
        return (Math.pow(2.0, (-10 * input).toDouble()) * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1).toFloat()
    }
}