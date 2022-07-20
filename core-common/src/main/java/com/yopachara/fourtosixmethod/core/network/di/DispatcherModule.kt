package com.yopachara.fourtosixmethod.core.network.di

import com.yopachara.fourtosixmethod.core.network.Dispatcher
import com.yopachara.fourtosixmethod.core.network.FsmDispatchers.IO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @Provides
    @Dispatcher(IO)
    fun provideIODispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Singleton // Provide always the same instance
    @Provides
    fun providesCoroutineScope(): CoroutineScope {
        // Run this code when providing an instance of CoroutineScope
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
}