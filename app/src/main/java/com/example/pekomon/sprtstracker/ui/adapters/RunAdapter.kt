package com.example.pekomon.sprtstracker.ui.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pekomon.sprtstracker.R
import com.example.pekomon.sprtstracker.data.entity.Run
import com.example.pekomon.sprtstracker.databinding.ListitemRunBinding
import com.example.pekomon.sprtstracker.ui.differcallbacks.RunDiffCallback
import java.text.SimpleDateFormat
import java.util.*

class RunAdapter : ListAdapter<Run, RunAdapter.RunViewHolder>(RunDiffCallback()) {

    private lateinit var resources: Resources

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, resources)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        resources = parent.resources
        return RunViewHolder.from(parent)
    }

    class RunViewHolder private constructor(val binding: ListitemRunBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(run: Run, resources: Resources) {
            Glide.with(binding.root).load(run.image).into(binding.ivRunImage)
            val calendar = Calendar.getInstance().apply {
                timeInMillis = run.timestamp
            }

            val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            binding.tvDate.text = dateFormat.format(calendar.time)
            binding.tvAvgSpeed.text = resources.getString(R.string.run_list_avg_speed_template,
                run.averageSpeed)
            binding.tvDistance.text = resources.getString(
                R.string.run_list_distance_in_km_template,
                run.distance / 1000
            )
            binding.tvTime.text = com.example.pekomon.sprtstracker.utils.TimeUtils.getFormattedTime(run.durationInMillis, false)
            binding.tvCalories.text = resources.getString(
                R.string.run_list_calories_burned_template,
                run.calories
            )
        }

        companion object {
            fun from(parent: ViewGroup): RunViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListitemRunBinding.inflate(layoutInflater, parent, false)
                return RunViewHolder(binding)
            }
        }
    }
}
