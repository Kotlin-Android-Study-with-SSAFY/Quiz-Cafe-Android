package com.android.quizcafe.core.datastore

sealed class LogoutReason {
    data class SessionExpired(val message: String) : LogoutReason()
    data object UserLogout : LogoutReason()
    data object UserWithdrawal : LogoutReason()
    data object PasswordUpdated : LogoutReason()
}
