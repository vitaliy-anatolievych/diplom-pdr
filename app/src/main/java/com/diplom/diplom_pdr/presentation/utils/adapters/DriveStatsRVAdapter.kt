package com.diplom.diplom_pdr.presentation.utils.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.ItemStatsBinding
import com.diplom.diplom_pdr.models.DriveStatsModel

class DriveStatsRVAdapter :
    ListAdapter<DriveStatsModel, DriveStatsRVAdapter.StatsViewHolder>(DiffCallBack()) {

    inner class StatsViewHolder(private val view: View) : ViewHolder(view) {
        private val binding = ItemStatsBinding.bind(view)

        fun bind(model: DriveStatsModel) = with(binding) {

            tvDate.text = model.date
            tvDistance.text =
                String.format(view.context.getString(R.string.s_km), String.format("%.1f", model.distance))
            tvAverageDistance.text =
                String.format(view.context.getString(R.string.s_km_hour), model.averageSpeed)

            tvExcessiveSpeed.text = "${model.countOfExcessiveSpeed}"
            tvEmergencySlowDown.text = "${model.countOfEmergencyDown}"
            tvExcessiveSpeed.text = "${model.countExcessiveOver20}"
        }
    }

    class DiffCallBack() : DiffUtil.ItemCallback<DriveStatsModel>() {
        override fun areItemsTheSame(oldItem: DriveStatsModel, newItem: DriveStatsModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DriveStatsModel,
            newItem: DriveStatsModel
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_stats, parent, false)

        return StatsViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}