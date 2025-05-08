package com.android.quizcafe.core.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.android.quizcafe.core.common.network.di.ApplicationScope
import com.android.quizcafe.core.datastore.AuthDataStore
import com.android.quizcafe.core.datastore.AuthManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

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
}
