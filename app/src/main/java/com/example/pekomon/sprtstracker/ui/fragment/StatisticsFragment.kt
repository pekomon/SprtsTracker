package com.example.pekomon.sprtstracker.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.pekomon.sprtstracker.R
import com.example.pekomon.sprtstracker.databinding.FragmentStatisticsBinding
import com.example.pekomon.sprtstracker.ui.markerview.CustomMarkerView
import com.example.pekomon.sprtstracker.ui.viewmodel.MainViewModel
import com.example.pekomon.sprtstracker.ui.viewmodel.StatisticsViewModel
import com.example.pekomon.sprtstracker.utils.TimeUtils
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
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
        setupBarChart()
    }

    private fun setupObservers() {
        viewModel.totalTime.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.tvTotalTime.text = TimeUtils.getFormattedTime(it, false)
            }
        })

        viewModel.totalDistance.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.tvTotalDistance.text = resources.getString(
                    R.string.x_km_template,
                    (round((it/1000) * 10f) / 10f).toString()
                )
            }
        })

        viewModel.averageSpeed.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.tvAverageSpeed.text = resources.getString(
                    R.string.x_km_h_template,
                    (round(it * 10f) / 10f).toString()
                )
            }
        })

        viewModel.totalCalories.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.tvTotalCalories.text =  resources.getString(
                    R.string.x_kcal_template,
                    it.toString()
                )
            }
        })

        viewModel.runsByDate.observe(viewLifecycleOwner, Observer {
            it?.let {
                val allAverages = it.indices.map { i -> BarEntry(i.toFloat(), it[i].averageSpeed) }
                val barDataSet = BarDataSet(allAverages, resources.getString(R.string.barchart_average_speed)).apply {
                    valueTextColor = Color.WHITE
                    color = ContextCompat.getColor(requireContext(), R.color.colorAccent)
                }
                binding.barChart.data = BarData(barDataSet)
                binding.barChart.marker = CustomMarkerView(it, requireContext(), R.layout.barchart_marker_view)
                binding.barChart.invalidate()

            }
        })
    }

    private fun setupBarChart() {
        binding.barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        binding.barChart.axisLeft.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        binding.barChart.axisRight.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }

        binding.barChart.apply {
            description.text = resources.getString(R.string.barchart_average_speed)
            legend.isEnabled = false

        }
    }
}