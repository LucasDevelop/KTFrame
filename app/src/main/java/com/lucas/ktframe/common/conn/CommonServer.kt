package com.lucas.ktframe.common.conn

import com.lucas.frame.data.bean.IBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface CommonServer {

    @Headers("needLogin:false")
    @GET("/api/user/login")
    fun login(@Query("account") account:String,@Query("password") password:String): Observable<IBean>
}