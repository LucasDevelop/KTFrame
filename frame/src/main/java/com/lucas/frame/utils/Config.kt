package com.lucas.frame.utils
/**
 * @创建者     lucas
 * @创建时间   2017/12/25 0025 11:22
 * @描述          配置
 */
object Config {
    //本地缓存大小
    val CACHE_SIZE: Long = 1024 * 1024 * 10

    //连接超时时间
    val CONN_TIME_OUT: Long = 15 * 1000

    //数据读取超时时间
    val READ_TIME_OUT: Long = 15 * 1000
}