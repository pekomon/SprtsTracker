package com.example.pekomon.sprtstracker.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pekomon.sprtstracker.data.entity.Run
import com.example.pekomon.sprtstracker.data.repository.MainRepository
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    val repository: MainRepository
) : ViewModel() {

    fun addRun(run: Run) = viewModelScope.launch {
        repository.insertRun(run)
    }
}