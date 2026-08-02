package com.yopachara.fourtosixmethod.di

import com.yopachara.fourtosixmethod.core.data.di.platformDataModule
import com.yopachara.fourtosixmethod.core.data.di.repositoryModule
import com.yopachara.fourtosixmethod.core.database.databaseModule
import com.yopachara.fourtosixmethod.core.database.platformDatabaseModule
import com.yopachara.fourtosixmethod.core.domain.di.domainModule
import com.yopachara.fourtosixmethod.core.network.di.dispatcherModule
import com.yopachara.fourtosixmethod.feature.history.di.historyModule
import com.yopachara.fourtosixmethod.feature.settings.di.settingsModule
import com.yopachara.fourtosixmethod.feature.timer.di.platformTimerModule
import com.yopachara.fourtosixmethod.feature.timer.di.timerModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * Every module the app needs, in one place, so both platform entry points start the same graph.
 * The `platform*` entries are `expect val`s, so each target links its own actual.
 */
private val sharedModules = listOf(
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
    platformTimerModule,
)

/**
 * Starts Koin with the shared graph.
 *
 * @param appDeclaration platform setup the shared graph cannot express - on Android that is
 *   `androidContext()` and `workManagerFactory()`; iOS needs nothing.
 */
fun initKoin(appDeclaration: KoinAppDeclaration = {}): KoinApplication = startKoin {
    appDeclaration()
    modules(sharedModules)
}
