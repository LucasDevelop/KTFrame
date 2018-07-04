package com.lucas.frame.data.api.module

import com.lucas.frame.FrameApp
import com.lucas.frame.interceptor.ParamsInterceptor
import com.lucas.frame.utils.Config
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
/**
 * @创建者     lucas
 * @创建时间   2017/12/25 0025 11:18
 * @描述          网络层
 */
object ApiServiceModule {
    fun getOkHttpClient(): OkHttpClient =
            OkHttpClient.Builder()
                    .addInterceptor(ParamsInterceptor())
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .readTimeout(Config.READ_TIME_OUT, TimeUnit.MILLISECONDS)
                    .connectTimeout(Config.CONN_TIME_OUT, TimeUnit.MILLISECONDS)
                    .cache(getCache())
                    .build()

    fun getCache(): Cache =
            Cache(FrameApp.INSTANCE.cacheDir, Config.CACHE_SIZE)

    fun getRetrofit(): Retrofit =
            Retrofit.Builder()
                    .baseUrl(FrameApp.INSTANCE.BASE_URL)
                    .client(getOkHttpClient())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(FrameApp.INSTANCE.gson))
                    .build()
}