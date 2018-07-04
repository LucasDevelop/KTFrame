package com.lucas.frame.interceptor;


import android.text.TextUtils;


import com.lucas.frame.helper.SpHelper;

import java.io.IOException;


import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * @项目名： Ucella
 * @包名： com.enchantin.ucella.app.clienthelp
 * @创建者： haidev
 * @创建时间： 2017/3/2 0002 下午 5:16
 * @公司： Enchantin
 * @邮箱： haidev.tang@enchantin.com
 * @描述 ：   统一添加公共参数 user_id sign
 */
public class ParamsInterceptor implements Interceptor {

    public ParamsInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request oldRequest = chain.request();

        // 添加公共参数
        HttpUrl.Builder authorizedUrlBuilder = oldRequest.url()
                .newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host());
        int userId = getUserId();
        String sign = getSign();
        if (oldRequest.method().equals("GET")) {
            String needUserId = oldRequest.header("needLogin");
            if (!TextUtils.isEmpty(needUserId) && needUserId.equals("false")) {

            } else {
                if (!TextUtils.isEmpty(userId + "")) {
                    authorizedUrlBuilder.addQueryParameter("uid", userId + "");
                }
                if (!TextUtils.isEmpty(sign)) {
                    authorizedUrlBuilder.addQueryParameter("token", sign);
                }
            }

        } else if (oldRequest.method().equals("POST")) {
            if (oldRequest.body() == null || oldRequest.body() instanceof FormBody) {
                String needUserId = oldRequest.header("needLogin");
                if (!TextUtils.isEmpty(needUserId) && needUserId.equals("false")) {

                } else {
                    FormBody.Builder bodyBuilder = new FormBody.Builder();
                    if (!TextUtils.isEmpty(userId + "")) {
                        bodyBuilder.add("uid", userId + "");
                    }
                    if (!TextUtils.isEmpty(sign)) {
                        bodyBuilder.add("token", sign);
                    }

                    FormBody body;
                    if (oldRequest.body() != null) {
                        body = (FormBody) oldRequest.body();
                        for (int i = 0; i < body.size(); i++) {
                            bodyBuilder.add(body.name(i), body.value(i));
                        }
                    }
                    body = bodyBuilder.build();

                    oldRequest = oldRequest.newBuilder().post(body).build();
                }
            }
        }


        // 新的请求
        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(authorizedUrlBuilder.build())
                .removeHeader("needLogin")
                .build();

        return chain.proceed(newRequest);


    }

    private int getUserId() {
        Integer userId = SpHelper.INSTANCE.getUserId();
        return userId == null ? -1 : userId;
    }

    private String getSign() {
        return SpHelper.INSTANCE.getUserSign();
    }

}
