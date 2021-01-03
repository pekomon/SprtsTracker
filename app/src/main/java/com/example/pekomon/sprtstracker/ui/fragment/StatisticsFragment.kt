package com.example.pekomon.sprtstracker.ui.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.pekomon.sprtstracker.R
import com.example.pekomon.sprtstracker.ui.viewmodel.MainViewModel
import com.example.pekomon.sprtstracker.ui.viewmodel.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private val viewModel: StatisticsViewModel by viewModels()

}