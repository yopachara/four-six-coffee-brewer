package com.yopachara.fourtosixmethod.feature.history

import androidx.lifecycle.ViewModel
import com.yopachara.fourtosixmethod.core.data.model.Recipe
import com.yopachara.fourtosixmethod.core.domain.GetRecipeHistoryListUseCase
import com.yopachara.fourtosixmethod.core.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getRecipeHistoryListUseCase: GetRecipeHistoryListUseCase,
    private val scope: CoroutineScope
) : ViewModel() {

    private var _historyStateFlow = MutableStateFlow(emptyList<Recipe>())
    val historyStateFlow: StateFlow<List<Recipe>> = _historyStateFlow

    init {
        fetchHistory()
    }

    private fun fetchHistory() {
        scope.launch {
            when (val result = getRecipeHistoryListUseCase()) {
                is Result.Error -> {
                    _historyStateFlow.value = emptyList()
                }

                is Result.Success -> {
                    _historyStateFlow.value = result.data
                }

                else -> {
                    _historyStateFlow.value = emptyList()

                }
            }
        }
    }
}