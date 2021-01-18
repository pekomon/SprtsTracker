package com.example.pekomon.sprtstracker.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.pekomon.sprtstracker.R
import com.example.pekomon.sprtstracker.internal.Constants.ACTION_FROM_NOTIFICATION
import com.example.pekomon.sprtstracker.internal.Constants.ACTION_PAUSE_SERVICE
import com.example.pekomon.sprtstracker.internal.Constants.ACTION_START_RESUME_SERVICE
import com.example.pekomon.sprtstracker.internal.Constants.ACTION_STOP_SERVICE
import com.example.pekomon.sprtstracker.internal.Constants.LOCATION_UPDATE_INTERVAL_MS
import com.example.pekomon.sprtstracker.internal.Constants.MINIMUM_LOCATION_INTERVAL_MS
import com.example.pekomon.sprtstracker.internal.Constants.NOTIFICATION_CHANNEL_ID
import com.example.pekomon.sprtstracker.internal.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.pekomon.sprtstracker.internal.Constants.NOTIFICATION_ID
import com.example.pekomon.sprtstracker.internal.Constants.TIMER_UPDATE_INTERVAL_MILLIS
import com.example.pekomon.sprtstracker.ui.MainActivity
import com.example.pekomon.sprtstracker.utils.LocationPermissionHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

@AndroidEntryPoint
class TrackingService : LifecycleService() {

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val isStarted = AtomicBoolean(false)

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    private var isTimerEnabled = false
    private var lapTime = 0L
    private var timeRun = 0L
    private var timeStarted = 0L
    private var lastSecondTimeStamp = 0L

    private val _timeRunSeconds = MutableLiveData<Long>(0L)
    val timeRunSeconds: LiveData<Long>
            get() = _timeRunSeconds

    companion object {
        private val _isTrackingOn = MutableLiveData<Boolean>(false)
        val isTrackingOn: LiveData<Boolean>
            get() = _isTrackingOn
        private val _pathPoints = MutableLiveData<Polylines>(mutableListOf())
        val pathPoints: LiveData<Polylines>
            get() = _pathPoints
        private val _timeRunMillis = MutableLiveData<Long>(0L)
        val timeRunMillis: LiveData<Long>
            get() = _timeRunMillis
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)

            if (_isTrackingOn.value!!) {
                locationResult?.locations?.let { locationList ->
                    for (location in locationList) {
                        addPoint(location)
                        Timber.d("New location $location")
                    }
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (it.action) {
                ACTION_START_RESUME_SERVICE -> {
                    Timber.d("Start resume")
                    if (!isStarted.getAndSet(true)) {
                        Timber.d("Start")
                        startService()
                    } else {
                        Timber.d("Resume")
                        startTimer()
                    }
                }

                ACTION_PAUSE_SERVICE -> {
                    Timber.d("Pause")
                    pause()
                }
                ACTION_STOP_SERVICE -> {
                    Timber.d("Stop")
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        isTrackingOn.observe(this, Observer {
            updateTracking(it)
        })
    }

    private fun startTimer() {
        addEmptyPolyline()
        _isTrackingOn.postValue(true)
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true
        CoroutineScope(Dispatchers.Main).launch {
            while (_isTrackingOn.value!!) {
                // Time elapsed from 'timeStarted'
                lapTime = System.currentTimeMillis() - timeStarted

                _timeRunMillis.postValue(timeRun + lapTime)
                if (_timeRunMillis.value!! >= lastSecondTimeStamp + 1000L) {
                    //
                    _timeRunSeconds.postValue(_timeRunSeconds.value!! +1)
                    lastSecondTimeStamp += 1000
                }
                delay(TIMER_UPDATE_INTERVAL_MILLIS)
            }
            timeRun += lapTime
        }

    }

    private fun pause() {
        _isTrackingOn.postValue(false)
        isTimerEnabled = false
    }

    // EasyPermissions library is used to get locations
    @SuppressLint("MissingPermission")
    private fun updateTracking(isTracking: Boolean) {
        if (isTracking) {
            if (LocationPermissionHelper.hasLocationPermissions(this)) {
                val locationRequest = LocationRequest().apply {
                    interval = LOCATION_UPDATE_INTERVAL_MS
                    fastestInterval = MINIMUM_LOCATION_INTERVAL_MS
                    priority = PRIORITY_HIGH_ACCURACY

                }
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    private fun startService() {

        startTimer()
        _isTrackingOn.postValue(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNoticationChannel(notificationManager)
        }

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

    private fun addEmptyPolyline() = _pathPoints.value?.apply {
        add(mutableListOf())
        _pathPoints.postValue(this)
    } ?: _pathPoints.postValue(mutableListOf(mutableListOf()))

    private fun addPoint(location: Location?) {
        location?.let {
            val position = LatLng(location.latitude, location.longitude)
            _pathPoints.value?.apply {
                last().add(position)
                _pathPoints.postValue(this)
            }
        }
    }

    private fun postInitialValues() {
        _isTrackingOn.postValue(false)
        _pathPoints.postValue(mutableListOf())
    }
}