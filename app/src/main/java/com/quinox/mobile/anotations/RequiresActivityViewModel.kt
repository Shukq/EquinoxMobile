package com.quinox.mobile.anotations

import com.quinox.mobile.base.ActivityViewModel
import java.lang.annotation.Inherited
import kotlin.reflect.KClass

@Inherited
@Retention(AnnotationRetention.RUNTIME)
annotation class RequiresActivityViewModel(val value: KClass<out ActivityViewModel<*>>)