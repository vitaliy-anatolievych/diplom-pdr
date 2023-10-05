package com.diplom.diplom_pdr.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.FragmentRewardsBinding
import com.diplom.diplom_pdr.models.RewardItem
import com.diplom.diplom_pdr.presentation.utils.adapters.RewardRVAdapter

class RewardScreen: Fragment() {

    private var _binding: FragmentRewardsBinding? = null
    private val binding: FragmentRewardsBinding
        get() = _binding ?: throw NullPointerException("FragmentRewardsBinding is null")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRewardsBinding.inflate(inflater, container, false)

        with(binding) {
            val adapter = RewardRVAdapter()
            rvRewards.adapter = adapter

            val list = listOf(
                RewardItem(
                    id = 0,
                    idImage = R.drawable.ic_fitness,
                    description = "Reward 1"
                ),
                RewardItem(
                    id = 1,
                    idImage = R.drawable.ic_reward_2,
                    description = "Reward 2"
                ),
                RewardItem(
                    id = 2,
                    idImage = R.drawable.ic_reward_4,
                    description = "Reward 3"
                ),
                RewardItem(
                    id = 3,
                    idImage = R.drawable.ic_reward_5,
                    description = "Reward 4"
                ),
            )

            adapter.submitList(list)
        }


        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}