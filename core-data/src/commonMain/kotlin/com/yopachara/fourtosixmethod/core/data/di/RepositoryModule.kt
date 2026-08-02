package com.yopachara.fourtosixmethod.core.data.di

import com.yopachara.fourtosixmethod.core.data.repository.DataStoreUserSettingsRepository
import com.yopachara.fourtosixmethod.core.data.repository.OfflineRecipeRepository
import com.yopachara.fourtosixmethod.core.data.repository.RecipeRepository
import com.yopachara.fourtosixmethod.core.data.repository.UserSettingsRepository
import com.yopachara.fourtosixmethod.core.network.FsmDispatchers
import org.koin.core.qualifier.named
import org.koin.core.module.Module
import org.koin.dsl.module

/** Supplies the platform preferences `DataStore` consumed by [DataStoreUserSettingsRepository]. */
expect val platformDataModule: Module

val repositoryModule = module {
    single<RecipeRepository> { OfflineRecipeRepository(get(), get(named(FsmDispatchers.IO.name))) }
    single<UserSettingsRepository> { DataStoreUserSettingsRepository(get()) }
}
