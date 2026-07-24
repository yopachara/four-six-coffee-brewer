package com.yopachara.fourtosixmethod

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkerFactory
import com.yopachara.fourtosixmethod.core.data.di.platformDataModule
import com.yopachara.fourtosixmethod.core.data.di.repositoryModule
import com.yopachara.fourtosixmethod.core.database.databaseModule
import com.yopachara.fourtosixmethod.core.database.platformDatabaseModule
import com.yopachara.fourtosixmethod.core.domain.di.domainModule
import com.yopachara.fourtosixmethod.core.network.di.dispatcherModule
import com.yopachara.fourtosixmethod.di.appModule
import com.yopachara.fourtosixmethod.feature.history.di.historyModule
import com.yopachara.fourtosixmethod.feature.settings.di.settingsModule
import com.yopachara.fourtosixmethod.feature.timer.di.timerModule
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

open class FlowSixApplication : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FlowSixApplication)
            workManagerFactory()
            modules(
                dispatcherModule,
                repositoryModule,
                platformDataModule,
                databaseModule,
                platformDatabaseModule,
                domainModule,
                appModule,
                historyModule,
                settingsModule,
                timerModule,
            )
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(get<WorkerFactory>())
            .build()
}
