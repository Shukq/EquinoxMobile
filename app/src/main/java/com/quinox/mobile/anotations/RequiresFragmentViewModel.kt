package com.quinox.mobile.anotations

import com.quinox.mobile.base.FragmentViewModel
import java.lang.annotation.Inherited
import kotlin.reflect.KClass

@Inherited
@Retention(AnnotationRetention.RUNTIME)
annotation class RequiresFragmentViewModel(val value: KClass<out FragmentViewModel<*>>)