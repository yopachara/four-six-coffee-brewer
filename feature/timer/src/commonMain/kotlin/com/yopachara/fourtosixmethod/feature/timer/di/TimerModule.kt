package com.yopachara.fourtosixmethod.feature.timer.di

import com.yopachara.fourtosixmethod.core.network.FsmDispatchers
import com.yopachara.fourtosixmethod.feature.timer.service.TimerController
import com.yopachara.fourtosixmethod.feature.timer.service.TimerEngine
import com.yopachara.fourtosixmethod.feature.timer.state.TimerSessionRepository
import com.yopachara.fourtosixmethod.feature.timer.viewmodel.TimerViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val timerModule = module {
    // Explicit rather than singleOf(): the dispatcher argument needs a qualifier.
    single { TimerSessionRepository(get(), get(named(FsmDispatchers.IO.name))) }
    singleOf(::TimerEngine)

    viewModelOf(::TimerViewModel)
}

/**
 * Platform half of [timerModule]: the [TimerController] implementation plus whatever
 * background machinery it needs (WorkManager + worker on Android, nothing extra on iOS).
 * Must be loaded alongside [timerModule].
 */
expect val platformTimerModule: Module
