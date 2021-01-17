package com.example.pekomon.sprtstracker.utils

import java.util.concurrent.TimeUnit

object TimeUtils {

    fun getFormattedTime(timeMillis: Long, includeMillis: Boolean): String {
        var remainingMillis = timeMillis
        val hours = TimeUnit.MILLISECONDS.toHours(remainingMillis)
        remainingMillis -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(remainingMillis)
        remainingMillis -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(remainingMillis)
        if (!includeMillis) {
            return "${hours.toString().padStart(2, '0')}:" +
            "${minutes.toString().padStart(2, '0')}:" +
            "${seconds.toString().padStart(2, '0')}"
        }
        remainingMillis -= TimeUnit.SECONDS.toMillis(seconds)
        remainingMillis /= 10
        return "${hours.toString().padStart(2, '0')}:" +
                "${minutes.toString().padStart(2, '0')}:" +
                "${seconds.toString().padStart(2, '0')}:" +
                "${remainingMillis.toString().padStart(2, '0')}"
    }
}