package com.example.pekomon.sprtstracker.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pekomon.sprtstracker.R
import com.example.pekomon.sprtstracker.data.entity.Run
import com.example.pekomon.sprtstracker.databinding.ListitemRunBinding
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class RunAdapter : RecyclerView.Adapter<RunAdapter.RunViewHolder>() {

    inner class RunViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var binding: ListitemRunBinding

    val differCallback = object : DiffUtil.ItemCallback<Run>() {
        override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    fun submitlist(list: List<Run>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        binding = ListitemRunBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RunViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val run = differ.currentList[position]
        Timber.d("Pos: $position id: ${run.id}")
        holder.itemView.apply {

            Glide.with(this).load(run.image).into(binding.ivRunImage)
            val calendar = Calendar.getInstance().apply {
                timeInMillis = run.timestamp
            }

            val dateFormat = SimpleDateFormat("hh.mm.ss", Locale.getDefault())
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
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}