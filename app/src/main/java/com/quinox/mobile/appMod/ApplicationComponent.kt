package com.quinox.mobile.appMod

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [InternalApplicationModule::class])
public interface  ApplicationComponent : ApplicationGraph