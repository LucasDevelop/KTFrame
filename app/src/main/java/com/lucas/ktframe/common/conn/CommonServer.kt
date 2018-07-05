package com.lucas.ktframe.common.conn

import com.lucas.frame.data.bean.IBean
import com.lucas.ktframe.ListBean
import io.reactivex.Observable
import retrofit2.http.*

interface CommonServer {

    @Headers("needLogin:false")
    @GET("/user/login")
    fun login(@Query("username") account: String, @Query("password") password: String): Observable<IBean>

    @Headers("needLogin:false")
    @GET("/project/list/{page}/json?cid=294")
    fun getList(@Path("page") page:Int):Observable<ListBean>
}