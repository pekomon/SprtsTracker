package com.example.pekomon.sprtstracker.extensions

import android.location.Location
import com.example.pekomon.sprtstracker.service.Polyline

fun Polyline.length(): Float {
    var distance = 0f
    for (i in 0..this.size - 2) {
        val position1 = this[i]
        val position2 = this[i + 1]

        val result = FloatArray(1)
        Location.distanceBetween(
            position1.latitude,
            position1.longitude,
            position2.latitude,
            position2.longitude,
            result
        )
        distance += result[0]
    }
    return distance
}