package com.example.pekomon.sprtstracker.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pekomon.sprtstracker.data.entity.Run

@Dao
interface RunDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: Run)

    @Delete
    suspend fun deleteRun(run: Run)

    @Query("SELECT * FROM run ORDER BY timestamp DESC")
    fun GetAllRunsByDate(): LiveData<List<Run>>

    @Query("SELECT * FROM run ORDER BY durationInMillis DESC")
    fun GetAllRunsByDuration(): LiveData<List<Run>>

    @Query("SELECT * FROM run ORDER BY calories DESC")
    fun GetAllRunsByCalories(): LiveData<List<Run>>

    @Query("SELECT * FROM run ORDER BY averageSpeed DESC")
    fun GetAllRunsByAverageSpeed(): LiveData<List<Run>>

    @Query("SELECT * FROM run ORDER BY distance DESC")
    fun GetAllRunsByDistance(): LiveData<List<Run>>

    @Query("SELECT SUM(durationInMillis) FROM run")
    fun getTotalTimeInMillis(): LiveData<Long>

    @Query("SELECT SUM(calories) FROM run")
    fun getTotalCalories(): LiveData<Int>

    @Query("SELECT SUM(distance) FROM run")
    fun getTotalDistance(): LiveData<Int>

    @Query("SELECT AVG(averageSpeed) FROM run")
    fun getAverageSpeed(): LiveData<Float>
}
