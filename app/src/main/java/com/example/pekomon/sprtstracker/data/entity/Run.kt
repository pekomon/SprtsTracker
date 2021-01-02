package com.example.pekomon.sprtstracker.data.entity

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "run")
data class Run(
    var image: Bitmap? = null,
    var timestamp: Long = 0L,
    var averageSpeed: Float = 0f,
    var distance: Int = 0,
    var durationInMillis: Long = 0L,
    var calories: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
