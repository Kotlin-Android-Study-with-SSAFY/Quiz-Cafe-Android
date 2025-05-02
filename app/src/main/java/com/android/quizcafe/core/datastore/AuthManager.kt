package com.android.quizcafe.core.datastore


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class AuthManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    }

    @Volatile
    private var cachedToken: String? = null

    init {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            dataStore.data
                .map { prefs -> prefs[ACCESS_TOKEN_KEY] }
                .collect { token ->
                    cachedToken = token
                }
        }
    }
    fun getToken(): String? = cachedToken


//    suspend fun getAccessToken(): String = getToken(ACCESS_TOKEN_KEY)
//
//    private suspend fun getToken(key: Preferences.Key<String>): String =
//        dataStore.data
//            .catch { exception ->
//                if (exception is IOException) emit(emptyPreferences())
//                else throw exception
//            }
//            .map { preferences -> preferences[key] ?: "" }
//            .first()


    suspend fun saveAccessToken(token: String) {
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = token
        }
        cachedToken = token
    }

    suspend fun deleteAccessToken() {
        dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN_KEY)
        }
        cachedToken = null
    }
}