package com.yopachara.fourtosixmethod.core.network.di

import com.yopachara.fourtosixmethod.core.network.FsmDispatchers
import com.yopachara.fourtosixmethod.core.network.ioDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dispatcherModule = module {
    single<CoroutineDispatcher>(named(FsmDispatchers.IO.name)) { ioDispatcher }
}
