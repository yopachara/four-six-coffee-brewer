package com.yopachara.fourtosixmethod.core.domain.di

import com.yopachara.fourtosixmethod.core.domain.GetRecipeHistoryListUseCase
import com.yopachara.fourtosixmethod.core.domain.InsertRecipeUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::GetRecipeHistoryListUseCase)
    factoryOf(::InsertRecipeUseCase)
}
