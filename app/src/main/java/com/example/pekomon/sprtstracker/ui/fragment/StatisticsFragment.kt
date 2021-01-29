package com.example.pekomon.sprtstracker.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.pekomon.sprtstracker.R
import com.example.pekomon.sprtstracker.databinding.FragmentStatisticsBinding
import com.example.pekomon.sprtstracker.ui.viewmodel.MainViewModel
import com.example.pekomon.sprtstracker.ui.viewmodel.StatisticsViewModel
import com.example.pekomon.sprtstracker.utils.TimeUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.round

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private val viewModel: StatisticsViewModel by viewModels()
    private lateinit var binding: FragmentStatisticsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentStatisticsBinding.bind(view)

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.totalTime.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.tvTotalTime.text = TimeUtils.getFormattedTime(it, false)
            }
        })

        viewModel.totalDistance.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.tvTotalDistance.text = (round((it/1000) * 10f) / 10f).toString()
            }
        })

        viewModel.totalDistance.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.tvTotalDistance.text = (round((it/1000) * 10f) / 10f).toString()
            }
        })

        viewModel.averageSpeed.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.tvAverageSpeed.text = (round(it * 10f) / 10f).toString()
            }
        })

        viewModel.totalCalories.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.tvTotalCalories.text = it.toString()
            }
        })

    }
}