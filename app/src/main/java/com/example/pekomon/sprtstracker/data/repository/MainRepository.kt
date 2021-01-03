package com.example.pekomon.sprtstracker.data.repository

import com.example.pekomon.sprtstracker.data.db.RunDao
import com.example.pekomon.sprtstracker.data.entity.Run
import javax.inject.Inject

class MainRepository @Inject constructor(
    val runDao: RunDao
) {
    suspend fun insertRun(run: Run) = runDao.insertRun(run)

    suspend fun deleteRun(run: Run) = runDao.deleteRun(run)

    fun getAllRunsByDate() = runDao.getAllRunsByDate()

    fun getAllRunsByDuration() = runDao.getAllRunsByDuration()

    fun getAllRunsByCalories() = runDao.getAllRunsByCalories()

    fun getAllRunsByAverageSpeed() = runDao.getAllRunsByAverageSpeed()

    fun getAllRunsByDistance() = runDao.getAllRunsByDistance()

    fun getTotalTimeInMillis() = runDao.getTotalTimeInMillis()

    fun getTotalCalories() = runDao.getTotalCalories()

    fun getTotalDistance() = runDao.getTotalDistance()

    fun getAverageSpeed() = runDao.getAverageSpeed()
}