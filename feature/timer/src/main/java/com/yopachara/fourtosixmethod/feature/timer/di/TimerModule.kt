package com.yopachara.fourtosixmethod.feature.timer.di

import android.content.Context
import androidx.work.WorkManager
import com.yopachara.fourtosixmethod.feature.timer.service.TimerController
import com.yopachara.fourtosixmethod.feature.timer.service.TimerWorker
import com.yopachara.fourtosixmethod.feature.timer.state.TimerSessionRepository
import com.yopachara.fourtosixmethod.feature.timer.viewmodel.TimerViewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val timerModule = module {
    single { WorkManager.getInstance(get<Context>()) }

    singleOf(::TimerSessionRepository)
    singleOf(::TimerController)

    worker { params ->
        TimerWorker(
            appContext = params.get(),
            params = params.get(),
            sessionRepository = get(),
            insertRecipeUseCase = get(),
        )
    }

    viewModelOf(::TimerViewModel)
}
