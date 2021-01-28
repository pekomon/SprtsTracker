package com.example.pekomon.sprtstracker.internal

import android.graphics.Color

object Constants {

    const val DB_NAME = "sprts_tracker_db"

    const val REQUEST_CODE_LOCATION_PERMISSION = 1

    const val ACTION_START_RESUME_SERVICE = "ACTION_START_RESUME_SERVICE"
    const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    const val ACTION_FROM_NOTIFICATION = "ACTION_FROM_NOTIFICATION"

    const val NOTIFICATION_CHANNEL_ID = "sprts_tracker"
    const val NOTIFICATION_CHANNEL_NAME = "Spörts Tracker"
    const val NOTIFICATION_ID = 1337

    const val LOCATION_UPDATE_INTERVAL_MS = 5000L
    const val MINIMUM_LOCATION_INTERVAL_MS = 2000L

    const val POLYLINE_COLOR = Color.RED
    const val POLYLINE_WIDTH = 8f

    const val MAP_ZOOM_LEVEL = 15f

    const val TIMER_UPDATE_INTERVAL_MILLIS = 50L

    // Shared Prefs
    const val SHARED_PREFS_FILE = "sprts_prefs"
    const val SETTING_VALUE_IS_FIRST_LAUNCH = "SPRTS_01"
    const val SETTING_VALUE_NAME = "SPRTS_02"
    const val SETTING_VALUE_WEIGHT = "SPRTS_03"

}