package com.yopachara.fourtosixmethod.feature.timer.di

import android.content.Context
import androidx.work.WorkManager
import com.yopachara.fourtosixmethod.feature.timer.service.AndroidTimerController
import com.yopachara.fourtosixmethod.feature.timer.service.TimerController
import com.yopachara.fourtosixmethod.feature.timer.service.TimerWorker
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformTimerModule: Module = module {
    single { WorkManager.getInstance(get<Context>()) }

    single<TimerController> { AndroidTimerController(get(), get(), get()) }

    worker { params ->
        TimerWorker(
            // KoinWorkerFactory only puts WorkerParameters in the params bundle - the
            // Context has to come from the graph (androidContext()), not from `params`.
            appContext = get(),
            params = params.get(),
            sessionRepository = get(),
            timerEngine = get(),
        )
    }
}
