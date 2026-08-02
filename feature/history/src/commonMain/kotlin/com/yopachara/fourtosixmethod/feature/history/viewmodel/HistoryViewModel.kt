package com.yopachara.fourtosixmethod.feature.history.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yopachara.fourtosixmethod.core.data.model.Recipe
import com.yopachara.fourtosixmethod.core.domain.GetRecipeHistoryListUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(
    getRecipeHistoryListUseCase: GetRecipeHistoryListUseCase,
) : ViewModel() {

    // viewModelScope, not an app-scoped one: onCleared() has to be able to tear the
    // Room-flow collector down, otherwise every HistoryViewModel leaves one running
    // for the rest of the process.
    val historyStateFlow: StateFlow<List<Recipe>> = getRecipeHistoryListUseCase()
        .catch {
            emit(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}