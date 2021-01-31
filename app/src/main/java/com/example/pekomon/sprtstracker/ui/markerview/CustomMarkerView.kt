package com.example.pekomon.sprtstracker.ui.markerview

import android.content.Context
import android.view.View
import com.example.pekomon.sprtstracker.R
import com.example.pekomon.sprtstracker.data.entity.Run
import com.example.pekomon.sprtstracker.databinding.BarchartMarkerViewBinding
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.text.SimpleDateFormat
import java.util.*

class CustomMarkerView(
    val runs: List<Run>,
    context: Context,
    layoutId: Int
) : MarkerView(context, layoutId) {

    private lateinit var binding: BarchartMarkerViewBinding
    // com.example.pekomon.sprtstracker.ui.markerview.CustomMarkerView cannot be cast to androidx.cardview.widget.CardView
    // binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    //val binding = DataBindingUtil


    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        child?.let {
            binding = BarchartMarkerViewBinding.bind(it)
        }

    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
        e?: return

        /*
        if (!::binding.isInitialized) {
            binding = BarchartMarkerViewBinding.bind(this@CustomMarkerView)
        }
        */

        // x axis is id of run
        val runId = e.x.toInt()
        val run = runs[runId]

        val calendar = Calendar.getInstance().apply {
            timeInMillis = run.timestamp
        }

        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        binding.tvDate.text = dateFormat.format(calendar.time)
        binding.tvAvgSpeed.text = resources.getString(
            R.string.run_list_avg_speed_template,
            run.averageSpeed)
        binding.tvDistance.text = resources.getString(
            R.string.run_list_distance_in_km_template,
            run.distance / 1000
        )
        binding.tvDuration.text = com.example.pekomon.sprtstracker.utils.TimeUtils.getFormattedTime(run.durationInMillis, false)
        binding.tvCaloriesBurned.text = resources.getString(
            R.string.run_list_calories_burned_template,
            run.calories
        )
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-width / 2f, -height.toFloat())
    }
}