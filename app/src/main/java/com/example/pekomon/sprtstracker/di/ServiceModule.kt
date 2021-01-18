package com.example.pekomon.sprtstracker.di

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.pekomon.sprtstracker.R
import com.example.pekomon.sprtstracker.internal.Constants
import com.example.pekomon.sprtstracker.ui.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @ServiceScoped   //Singleton for service
    @Provides
    fun provideFusedLocationProviderClient(
        @ApplicationContext appContext: Context
    ) = FusedLocationProviderClient(appContext)


    @ServiceScoped
    @Provides
    fun provideMainActivityPendingIntent(
        @ApplicationContext appContext: Context
    ) = PendingIntent.getActivity(
        appContext,
        0,
        Intent(appContext, MainActivity::class.java).also {
            it.action = Constants.ACTION_FROM_NOTIFICATION
        },
        FLAG_UPDATE_CURRENT
    )

    @ServiceScoped
    @Provides
    fun provideBaseNotificationBuilder(
        @ApplicationContext appContext: Context,
        pendingIntent: PendingIntent
    ) = NotificationCompat.Builder(appContext, Constants.NOTIFICATION_CHANNEL_ID)
        .setAutoCancel(false)
        .setOngoing(true)
        .setSmallIcon(R.drawable.ic_running)
        .setContentTitle(appContext.resources.getString(R.string.app_name))
        .setContentText("00:00:00")
        .setContentIntent(pendingIntent)
}