package com.yopachara.fourtosixmethod.di

import com.yopachara.fourtosixmethod.MainActivityViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MainActivityViewModel)
}
