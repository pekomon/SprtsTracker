package com.example.pekomon.sprtstracker.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pekomon.sprtstracker.data.entity.Run
import com.example.pekomon.sprtstracker.data.repository.MainRepository
import com.example.pekomon.sprtstracker.internal.enum.SortingType
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    val repository: MainRepository
) : ViewModel() {

    private val runsByDate = repository.getAllRunsByDate()
    private val runsByDistance = repository.getAllRunsByDistance()
    private val runsByCalories = repository.getAllRunsByCalories()
    private val runsByAverageSpeed = repository.getAllRunsByAverageSpeed()
    private val runsByDuration = repository.getAllRunsByDuration()

    val runs = MediatorLiveData<List<Run>>()

    var sorting = SortingType.CALORIES

    init {
        runs.addSource(runsByDate) { result ->
            if (sorting == SortingType.DATE) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsByDistance) { result ->
            if (sorting == SortingType.DISTANCE) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsByCalories) { result ->
            if (sorting == SortingType.CALORIES) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsByAverageSpeed) { result ->
            if (sorting == SortingType.AVERAGE_SPEED) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsByDuration) { result ->
            if (sorting == SortingType.DURATION) {
                result?.let { runs.value = it }
            }
        }
    }

    fun sortRuns(sortingType: SortingType) = when (sortingType) {
        SortingType.DATE -> runsByDate.value?.let { runs.value = it }
        SortingType.DISTANCE -> runsByDistance.value?.let { runs.value = it }
        SortingType.CALORIES -> runsByCalories.value?.let { runs.value = it }
        SortingType.AVERAGE_SPEED -> runsByAverageSpeed.value?.let { runs.value = it }
        SortingType.DURATION -> runsByDuration.value?.let { runs.value = it }
    }.also {
        this.sorting = sortingType
    }

    fun addRun(run: Run) = viewModelScope.launch {
        repository.insertRun(run)
    }
}