package com.shy.mvrxsample

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication


class App : MultiDexApplication(){

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
    }
}