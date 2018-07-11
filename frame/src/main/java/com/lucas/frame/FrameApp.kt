package com.lucas.frame

import android.app.Application
import android.os.Handler
import com.blankj.utilcode.util.Utils
import com.google.gson.Gson
import com.squareup.otto.Bus
import dagger.android.AndroidInjector
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerApplication

open class FrameApp : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication>?  {
        return null
    }

    var BASE_URL = BuildConfig.BASE_URL
    var gson = Gson()
    var handler = Handler()
    var bus = Bus()

    companion object {
        var INSTANCE: FrameApp = FrameApp()
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        Utils.init(this)
    }

}