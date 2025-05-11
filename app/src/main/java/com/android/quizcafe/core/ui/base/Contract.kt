package com.android.quizcafe.core.ui.base

interface Contract<State: Contract.ViewState, Event: Contract.ViewIntent, Effect: Contract.ViewEffect>{
    interface ViewState

    interface ViewIntent

    interface ViewEffect
}
