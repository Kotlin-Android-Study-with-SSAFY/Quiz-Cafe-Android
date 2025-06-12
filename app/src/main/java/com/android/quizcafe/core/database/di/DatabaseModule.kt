package com.android.quizcafe.core.database.di

import android.content.Context
import androidx.room.Room
import com.android.quizcafe.core.database.QuizCafeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): QuizCafeDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            QuizCafeDatabase::class.java,
            "QuizCafeDatabase"
        ).build()
    }

}
