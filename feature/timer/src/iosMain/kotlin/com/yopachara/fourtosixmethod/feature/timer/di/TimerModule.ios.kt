package com.yopachara.fourtosixmethod.feature.timer.di

import com.yopachara.fourtosixmethod.feature.timer.service.IosTimerController
import com.yopachara.fourtosixmethod.feature.timer.service.TimerController
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformTimerModule: Module = module {
    singleOf(::IosTimerController) bind TimerController::class
}
