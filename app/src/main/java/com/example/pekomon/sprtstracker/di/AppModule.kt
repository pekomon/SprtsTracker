package com.example.pekomon.sprtstracker.di

import android.content.Context
import androidx.room.Room
import com.example.pekomon.sprtstracker.data.db.SportsDatabase
import com.example.pekomon.sprtstracker.internal.Constants.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSprtsDatabase(
        @ApplicationContext appContext: Context
    ) = Room.databaseBuilder(
        appContext.applicationContext,
        SportsDatabase::class.java,
        DB_NAME
    ).build()

    @Singleton
    @Provides
    fun provideSingleDao(db: SportsDatabase) = db.runDao()
}