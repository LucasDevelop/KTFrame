package com.lucas.frame.helper

import com.blankj.utilcode.util.SPUtils
import com.lucas.frame.FrameApp
import com.lucas.frame.data.bean.User

/**
 * @package     com.lucas.ktframe.common.helper
 * @author      lucas
 * @date        2018/7/4
 * @version     V1.0
 * @describe    本地缓存SP数据
 */
object SpHelper {
    val spUtils = SPUtils.getInstance()
    val KEY_USER = "user"
    val KEY_USER_ID = "user_id"
    val KEY_USER_SIGN = "user_sign"

    //获取用户
    fun getUser(): User? {
        val json = spUtils.getString(KEY_USER)
        var user: User? = null
        if (!json.isNullOrEmpty())
            user = FrameApp.INSTANCE.gson.fromJson(json, User::class.java)
        return user
    }

    //保存用户
    fun setUser(user: User){
        spUtils.put(KEY_USER,FrameApp.INSTANCE.gson.toJson(user))
    }

    fun getUserId() = getUser()?.user_id

    fun getUserSign() = getUser()?.user_sign
}