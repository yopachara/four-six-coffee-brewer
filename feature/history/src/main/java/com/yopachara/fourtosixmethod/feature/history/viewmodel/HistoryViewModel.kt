package com.yopachara.fourtosixmethod.feature.history.viewmodel

import androidx.lifecycle.ViewModel
import com.yopachara.fourtosixmethod.core.data.model.Recipe
import com.yopachara.fourtosixmethod.core.domain.GetRecipeHistoryListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    getRecipeHistoryListUseCase: GetRecipeHistoryListUseCase,
    scope: CoroutineScope,
) : ViewModel() {

    val historyStateFlow: StateFlow<List<Recipe>> = getRecipeHistoryListUseCase()
        .catch {
            emit(emptyList())
        }
        .stateIn(scope, SharingStarted.Eagerly, emptyList())
}