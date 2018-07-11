package com.lucas.frame

import android.app.Application
import android.os.Handler
import com.blankj.utilcode.util.Utils
import com.google.gson.Gson
import com.squareup.otto.Bus
import dagger.android.AndroidInjector
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerApplication

 abstract class FrameApp : DaggerApplication() {
    var BASE_URL = BuildConfig.BASE_URL
    var gson = Gson()
    var handler = Handler()
    var bus = Bus()

    companion object {
        lateinit var INSTANCE: FrameApp
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        Utils.init(this)
    }

}