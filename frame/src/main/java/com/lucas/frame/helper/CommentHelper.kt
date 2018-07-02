package com.lucas.frame.helper

import android.app.Activity
import android.content.Context
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.lucas.frame.FrameApp

/**
 * @创建者     lucas
 * @创建时间   2017/12/25 0025 16:33
 * @描述         通用型工具
 */
interface CommentHelper {


    /**
     * 扩展函数
     * 对Any对象扩展toString方法，消除空指针异常
     */
    fun Any?.ts(): String = if (this == null) "null" else toString()

    /**
     * 对Any对象扩展log方法
     */
    fun Any?.ld(tag: String = "lucas") {
        LogUtils.d(tag, this.toString())
    }

    fun Any?.le(tag: String = "lucas") {
        LogUtils.e(tag, this.toString())
    }

    fun String.showToast() {
        //判断当前线程是否是主线程
        if (Thread.currentThread() == Looper.getMainLooper().thread)
            Toast.makeText(FrameApp.INSTANCE, this, Toast.LENGTH_SHORT).show()
        else
            FrameApp.INSTANCE.handler.post { Toast.makeText(FrameApp.INSTANCE, this, Toast.LENGTH_SHORT).show() }
    }

    /**
     * 资源获取
     *
     */
    fun Activity.getResStr(strIdRes: Int) = this.resources.getString(strIdRes) ?: "null"

    fun Activity.getResColor(colorIdRes: Int) = this.resources.getColor(colorIdRes)

    //三元运算
    infix fun <A> A?.T(isFalse: A): A {
        if (this == null || (this is String && this == "")) {
            return isFalse
        } else
            return this
    }
}