package com.lucas.frame.dagger

import com.lucas.frame.base.view.activity.BaseActivity
import dagger.Subcomponent
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

/**
 * @package     com.lucas.frame.dagger
 * @author      lucas
 * @date        2018/7/11
 * @dessribe    TODO
 */
@Subcomponent(modules = arrayOf(AndroidInjectionModule::class))
interface BaseActivityComponent :AndroidInjector<BaseActivity>{
    /**
     * 每个继承baseActivity的页面都使用同一个组件
     */
    @Subcomponent.Builder
    abstract class Builder :AndroidInjector.Builder<BaseActivity>()
}