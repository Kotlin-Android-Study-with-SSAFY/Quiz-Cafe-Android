package com.android.quizcafe.core.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State: Contract.ViewState, Intent: Contract.ViewIntent, Effect: Contract.ViewEffect>(
    initialState: State,
): ViewModel(){
    private val _state: MutableStateFlow<State> = MutableStateFlow(initialState)
    val state: StateFlow<State>
        get() = _state.asStateFlow()

    private val _intent: MutableSharedFlow<Intent> = MutableSharedFlow()
    val intent: SharedFlow<Intent>
        get() = _intent.asSharedFlow()

    private val _effect: MutableSharedFlow<Effect> = MutableSharedFlow()
    val effect: SharedFlow<Effect>
        get() = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            intent.collect { intent ->
                val newState = reduce(_state.value, intent)
                _state.value = newState

                handleIntent(intent)
            }
        }
    }

    fun sendIntent(intent: Intent) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
    }

    protected abstract fun reduce(currentState: State, intent: Intent): State

    protected abstract suspend fun handleIntent(intent: Intent)

    protected suspend fun emitEffect(effect: Effect) {
        _effect.emit(effect)
    }
}

