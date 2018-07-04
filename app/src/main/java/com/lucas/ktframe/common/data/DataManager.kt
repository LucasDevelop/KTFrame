package com.lucas.ktframe.common.data

import com.lucas.frame.data.api.module.ApiServiceModule
import com.lucas.frame.helper.SpHelper
import com.lucas.ktframe.common.conn.CommonServer

/**
 * @package     com.lucas.ktframe.common.data
 * @author      lucas
 * @date        2018/7/4
 * @version     V1.0
 * @describe    数据管理，所以的数据操作都经过这个类
 */
object DataManager {
    lateinit var commonServer: CommonServer
    lateinit var spHelper: SpHelper
    init {
        commonServer = ApiServiceModule.getRetrofit().create(CommonServer::class.java)

        spHelper = SpHelper
    }
}