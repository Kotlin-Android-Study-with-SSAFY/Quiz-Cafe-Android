package com.android.quizcafe.core.ui.base

interface BaseContract<State : BaseContract.ViewState, Intent : BaseContract.ViewIntent, Effect : BaseContract.ViewEffect> {
    interface ViewState

    interface ViewIntent

    interface ViewEffect
}
