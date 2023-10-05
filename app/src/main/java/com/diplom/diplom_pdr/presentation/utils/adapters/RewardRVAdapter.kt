package com.diplom.diplom_pdr.presentation.utils.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.ItemRewardBinding
import com.diplom.diplom_pdr.models.RewardItem

class RewardRVAdapter: ListAdapter<RewardItem, RewardRVAdapter.RewardViewHolder>(DiffCallBack()) {

    inner class RewardViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemRewardBinding.bind(view)

        fun bind(model: RewardItem) = with(binding) {
            imgReward.setImageResource(model.idImage)
            tvDescReward.text = model.description
        }
    }

    class DiffCallBack() : DiffUtil.ItemCallback<RewardItem>() {
        override fun areItemsTheSame(oldItem: RewardItem, newItem: RewardItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RewardItem,
            newItem: RewardItem
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_reward, parent, false)

        return RewardViewHolder(view)
    }

    override fun onBindViewHolder(holder: RewardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}