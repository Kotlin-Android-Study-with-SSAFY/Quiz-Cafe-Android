package com.android.quizcafe.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.android.quizcafe.core.common.network.di.ApplicationScope
import com.android.quizcafe.core.datastore.AuthDataStore
import com.android.quizcafe.core.datastore.AuthManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_preferences")

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthManger(
        authDataStore: AuthDataStore,
        @ApplicationScope applicationScope: CoroutineScope
    ): AuthManager = AuthManager(authDataStore, applicationScope)

    @Provides
    @Singleton
    fun provideAuthDataStore(
        dataStore: DataStore<Preferences>
    ): AuthDataStore = AuthDataStore(dataStore)

    @Provides
    @Singleton
    fun provideAuthPreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.dataStore
    }
}
