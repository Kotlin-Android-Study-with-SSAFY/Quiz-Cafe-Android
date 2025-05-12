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

abstract class BaseViewModel<State : BaseContract.ViewState, Intent : BaseContract.ViewIntent, Effect : BaseContract.ViewEffect>(
    initialState: State,
) : ViewModel() {
    private val _state: MutableStateFlow<State> = MutableStateFlow(initialState)
    val state: StateFlow<State>
        get() = _state.asStateFlow()

    private val intent: MutableSharedFlow<Intent> = MutableSharedFlow()

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
            this@BaseViewModel.intent.emit(intent)
        }
    }

    protected abstract fun reduce(currentState: State, intent: Intent): State

    protected abstract suspend fun handleIntent(intent: Intent)

    protected suspend fun emitEffect(effect: Effect) {
        _effect.emit(effect)
    }
}
