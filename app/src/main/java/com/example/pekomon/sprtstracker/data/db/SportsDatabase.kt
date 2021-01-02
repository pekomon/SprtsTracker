package com.example.pekomon.sprtstracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pekomon.sprtstracker.data.db.typeconverters.BitmapTypeConverter
import com.example.pekomon.sprtstracker.data.entity.Run

@Database(
    entities = [Run::class],
    version = 1
)
@TypeConverters(BitmapTypeConverter::class)
abstract class SportsDatabase : RoomDatabase() {
    abstract fun runDao(): RunDao
}