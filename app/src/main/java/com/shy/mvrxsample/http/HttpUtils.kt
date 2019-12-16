package com.shy.mvrxsample.http

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import java.io.IOException

object HttpUtils {
    val retrofit : Retrofit by lazy {
        Retrofit.Builder()
                .baseUrl("https://www.wanandroid.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(
                    OkHttpClient.Builder()
                        .readTimeout(20, TimeUnit.SECONDS)//读取超时
                        .connectTimeout(6, TimeUnit.SECONDS)//连接超时
                        .writeTimeout(60, TimeUnit.SECONDS)//写入超时
                        .addInterceptor(AddHeaderInterceptor())
                        .build()
                )
                .build()
    }
}

class AddHeaderInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder()
            //.header("header", "value")    增加统一请求头
            .method(original.method(), original.body())
            .build()
        return chain.proceed(request)
    }
}