package com.example.pekomon.sprtstracker.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.pekomon.sprtstracker.data.repository.MainRepository

class StatisticsViewModel@ViewModelInject constructor(
    val repository: MainRepository
) : ViewModel()  {

}