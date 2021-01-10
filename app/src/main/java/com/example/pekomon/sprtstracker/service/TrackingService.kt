package com.example.pekomon.sprtstracker.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_LOW
import androidx.lifecycle.LifecycleService
import com.example.pekomon.sprtstracker.R
import com.example.pekomon.sprtstracker.internal.Constants.ACTION_FROM_NOTIFICATION
import com.example.pekomon.sprtstracker.internal.Constants.ACTION_PAUSE_SERVICE
import com.example.pekomon.sprtstracker.internal.Constants.ACTION_START_RESUME_SERVICE
import com.example.pekomon.sprtstracker.internal.Constants.ACTION_STOP_SERVICE
import com.example.pekomon.sprtstracker.internal.Constants.NOTIFICATION_CHANNEL_ID
import com.example.pekomon.sprtstracker.internal.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.pekomon.sprtstracker.internal.Constants.NOTIFICATION_ID
import com.example.pekomon.sprtstracker.ui.MainActivity
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

class TrackingService : LifecycleService() {

    val isRunning = AtomicBoolean(false)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (it.action) {
                ACTION_START_RESUME_SERVICE -> {
                    Timber.d("Start resume")
                    if (!isRunning.getAndSet(true)) {
                        startService()
                    }
                }

                ACTION_PAUSE_SERVICE -> {
                    Timber.d("Pause")
                }
                ACTION_STOP_SERVICE -> {
                    Timber.d("Stop")
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startService() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNoticationChannel(notificationManager)
        }
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_running)
            .setContentTitle(resources.getString(R.string.app_name))
            .setContentText("00:00:00")
            .setContentIntent(getMainActivityPendingIntent())

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNoticationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java).also {
            it.action = ACTION_FROM_NOTIFICATION
        },
        FLAG_UPDATE_CURRENT
    )
}