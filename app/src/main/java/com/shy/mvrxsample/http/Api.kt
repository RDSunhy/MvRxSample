package com.shy.mvrxsample.http

object Api{
    val api : ApiService by lazy {
        HttpUtils.retrofit.create(ApiService::class.java)
    }
}