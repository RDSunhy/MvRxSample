package com.shy.mvrxsample.http

import com.shy.mvrxsample.sample.ArticleData
import io.reactivex.Observable
import retrofit2.http.GET

interface ApiService {

    @GET("https://www.wanandroid.com/article/list/0/json")
    fun getArticleList(): Observable<ArticleData>

}