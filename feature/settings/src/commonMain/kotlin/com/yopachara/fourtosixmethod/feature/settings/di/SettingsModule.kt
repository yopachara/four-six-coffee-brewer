package com.yopachara.fourtosixmethod.feature.settings.di

import com.yopachara.fourtosixmethod.feature.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingsModule = module {
    viewModelOf(::SettingsViewModel)
}
