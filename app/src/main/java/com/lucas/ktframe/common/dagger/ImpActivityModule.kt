package com.lucas.ktframe.common.dagger

import com.lucas.frame.dagger.BaseActivityComponent
import dagger.Module

/**
 * @package     com.lucas.frame.dagger
 * @author      lucas
 * @date        2018/7/11
 * @dessribe    baseActivity的实现类
 */
@Module(subcomponents = arrayOf(BaseActivityComponent::class))
abstract class ImpActivityModule {

}