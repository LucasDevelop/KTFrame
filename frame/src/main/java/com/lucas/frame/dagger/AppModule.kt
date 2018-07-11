package com.lucas.frame.dagger

import com.google.gson.Gson
import com.lucas.frame.FrameApp
import com.squareup.otto.Bus
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @package     com.lucas.frame.dagger
 * @author      lucas
 * @date        2018/7/11
 * @dessribe    应用层工具
 */
@Module
class AppModule {

    //提供解析工具
    @Singleton
    @Provides
    fun provideGson() = Gson()

    //提供事件工具
    @Singleton
    @Provides
    fun provideBus() = Bus()

    //提供应用级上下文
    @Singleton
    @Provides
    fun provideAppContext(app: FrameApp) = app
}