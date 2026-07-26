package com.yopachara.fourtosixmethod

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkerFactory
import com.yopachara.fourtosixmethod.di.initKoin
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory

open class FlowSixApplication : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@FlowSixApplication)
            workManagerFactory()
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(get<WorkerFactory>())
            .build()
}
