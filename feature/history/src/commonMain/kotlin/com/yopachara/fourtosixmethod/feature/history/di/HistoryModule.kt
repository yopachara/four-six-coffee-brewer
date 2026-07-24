package com.yopachara.fourtosixmethod.feature.history.di

import com.yopachara.fourtosixmethod.feature.history.viewmodel.HistoryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val historyModule = module {
    viewModelOf(::HistoryViewModel)
}
