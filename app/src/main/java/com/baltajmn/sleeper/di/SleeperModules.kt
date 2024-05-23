package com.baltajmn.sleeper.di

import com.baltajmn.sleeper.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

val SleeperModules: Module
    get() = module {
        includes(
            listOf(
                PresentationModule
            )
        )
    }

val PresentationModule = module {
    viewModelOf(::MainViewModel)
}