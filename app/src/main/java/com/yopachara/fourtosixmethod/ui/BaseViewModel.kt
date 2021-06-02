package com.yopachara.fourtosixmethod.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<T, U> : ViewModel() {
    protected val _dataStates: MutableState<DataState<T>> = mutableStateOf(DataState.Success(null))
    val dataStates: State<DataState<T>> = _dataStates

    protected val _viewEffects = Channel<U>(Channel.BUFFERED)
    val viewEffects = _viewEffects.receiveAsFlow()

    protected fun launchRequest(block: suspend () -> Unit): Job {
        val currentViewState = getViewState()
        return viewModelScope.launch {
            try {
                _dataStates.value = DataState.Loading(currentViewState)
                block()
            } catch (exception: Exception) {
                _dataStates.value = DataState.Error(currentViewState, Event(exception))
            }
        }
    }

    protected fun getViewState(): T? = _dataStates.value.toData()
}