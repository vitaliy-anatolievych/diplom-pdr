package com.diplom.diplom_pdr.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.FragmentStatsBinding
import com.diplom.diplom_pdr.models.DriveStatsModel
import com.diplom.diplom_pdr.presentation.utils.adapters.DriveStatsRVAdapter

class StatsScreen: Fragment(R.layout.fragment_stats) {

    private var _binding: FragmentStatsBinding? = null
    private val binding: FragmentStatsBinding
        get() = _binding ?: throw NullPointerException("FragmentStatsBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatsBinding.inflate(inflater, container, false)

        val adapter = DriveStatsRVAdapter()
        val list = listOf(
            DriveStatsModel(
                id = 0,
                date = "04.09.2023 11:05 - 15:45",
                distance = 100,
                averageSpeed = 112,
                countOfExcessiveSpeed = 3,
                countOfEmergencyDown = 1,
            ),
            DriveStatsModel(
                id = 1,
                date = "06.09.2023 14:05 - 11:45",
                distance = 50,
                averageSpeed = 110,
                countOfExcessiveSpeed = 0,
                countOfEmergencyDown = 0,
            ),
            DriveStatsModel(
                id = 2,
                date = "08.09.2023 15:05 - 21:00",
                distance = 93,
                averageSpeed = 60,
                countOfExcessiveSpeed = 3,
                countOfEmergencyDown = 5,
            ),
            DriveStatsModel(
                id = 3,
                date = "10.09.2023 16:05 - 12:45",
                distance = 40,
                averageSpeed = 90,
                countOfExcessiveSpeed = 0,
                countOfEmergencyDown = 1,
            ),
            DriveStatsModel(
                id = 4,
                date = "12.09.2023 17:10 - 18:45",
                distance = 70,
                averageSpeed = 60,
                countOfExcessiveSpeed = 1,
                countOfEmergencyDown = 0,
            ),
            DriveStatsModel(
                id = 5,
                date = "14.09.2023 12:05 - 12:15",
                distance = 100,
                averageSpeed = 110,
                countOfExcessiveSpeed = 0,
                countOfEmergencyDown = 4,
            ),
        )

        with(binding) {
            rvStats.adapter = adapter
            adapter.submitList(list)

            btnGlobalStats.setOnClickListener {
                findNavController().navigate(R.id.action_statsScreen_to_averageStatsScreen)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}