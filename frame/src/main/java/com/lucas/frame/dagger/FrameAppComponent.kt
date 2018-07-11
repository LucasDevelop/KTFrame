package com.lucas.frame.dagger

import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * @package     com.lucas.frame
 * @author      lucas
 * @date        2018/7/11
 * @dessribe    TODO
 */
@Singleton
@Component(modules = arrayOf(
        AppModule::class,
        AndroidInjectionModule::class
))
interface FrameAppComponent {
}