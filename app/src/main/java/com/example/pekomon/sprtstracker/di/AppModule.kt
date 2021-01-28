package com.example.pekomon.sprtstracker.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.example.pekomon.sprtstracker.data.db.SportsDatabase
import com.example.pekomon.sprtstracker.internal.Constants.DB_NAME
import com.example.pekomon.sprtstracker.internal.Constants.SETTING_VALUE_IS_FIRST_LAUNCH
import com.example.pekomon.sprtstracker.internal.Constants.SETTING_VALUE_NAME
import com.example.pekomon.sprtstracker.internal.Constants.SETTING_VALUE_WEIGHT
import com.example.pekomon.sprtstracker.internal.Constants.SHARED_PREFS_FILE
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

    @Singleton
    @Provides
    fun provideSharePreferences(
        @ApplicationContext appContext: Context
    ) = appContext.getSharedPreferences(SHARED_PREFS_FILE, MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideName(sharedPrefs: SharedPreferences) = sharedPrefs.getString(SETTING_VALUE_NAME, "") ?: ""  // Can sometimes be null even if default value is given

    @Singleton
    @Provides
    fun provideWeight(sharedPrefs: SharedPreferences) = sharedPrefs.getFloat(SETTING_VALUE_WEIGHT, 100f)

    @Singleton
    @Provides
    fun provideIsFirstTime(sharedPrefs: SharedPreferences) = sharedPrefs.getBoolean(
        SETTING_VALUE_IS_FIRST_LAUNCH, true)
}