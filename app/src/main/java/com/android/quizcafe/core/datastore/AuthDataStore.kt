package com.android.quizcafe.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private fun Preferences.Key<String>.flowIn(store: DataStore<Preferences>) =
    store.data.map { it[this] }

private suspend fun Preferences.Key<String>.saveTo(store: DataStore<Preferences>, value: String) =
    store.edit { it[this] = value }

private suspend fun Preferences.Key<String>.deleteFrom(store: DataStore<Preferences>) =
    store.edit { it.remove(this) }

class AuthDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    val accessTokenFlow = ACCESS_TOKEN.flowIn(dataStore)
    val refreshTokenFlow = REFRESH_TOKEN.flowIn(dataStore)

    suspend fun saveAccessToken(token: String) = ACCESS_TOKEN.saveTo(dataStore, token)
    suspend fun deleteAccessToken() = ACCESS_TOKEN.deleteFrom(dataStore)

    suspend fun saveRefreshToken(token: String) = REFRESH_TOKEN.saveTo(dataStore, token)
    suspend fun deleteRefreshToken() = REFRESH_TOKEN.deleteFrom(dataStore)
}
