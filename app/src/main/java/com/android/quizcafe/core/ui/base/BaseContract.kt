package com.android.quizcafe.core.ui.base

interface BaseContract<State : BaseContract.UiState, Intent : BaseContract.ViewIntent, Effect : BaseContract.ViewEffect> {
    interface UiState

    interface ViewIntent

    interface ViewEffect
}
