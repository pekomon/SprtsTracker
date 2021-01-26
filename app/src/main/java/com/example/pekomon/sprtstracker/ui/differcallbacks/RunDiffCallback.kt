package com.example.pekomon.sprtstracker.ui.differcallbacks

import androidx.recyclerview.widget.DiffUtil
import com.example.pekomon.sprtstracker.data.entity.Run

class RunDiffCallback : DiffUtil.ItemCallback<Run>() {
    override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
        return oldItem == newItem
    }
}