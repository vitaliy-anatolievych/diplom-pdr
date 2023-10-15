package com.diplom.diplom_pdr.presentation.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.FragmentAverageStatsBinding
import com.diplom.diplom_pdr.presentation.utils.viewmodels.MainViewModel

class AverageStatsScreen : Fragment() {

    private var _binding: FragmentAverageStatsBinding? = null
    private val binding: FragmentAverageStatsBinding
        get() = _binding ?: throw NullPointerException("FragmentAverageStatsBinding is null")

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAverageStatsBinding.inflate(inflater, container, false)

        with(binding) {

            var averageDistance = 0.0
            var averageSpeed = 0

            var averageSpeedExcpensiveOver20 = 0
            var excessiveSpeed = 0
            var emergencySlowDown = 0
            viewModel.driveStatsData.observe(viewLifecycleOwner) { list ->
                for (id in list.indices) {
                    averageDistance += list[id].distance
                    averageSpeed += list[id].averageSpeed
                    excessiveSpeed += list[id].countOfExcessiveSpeed
                    emergencySlowDown += list[id].countOfEmergencyDown
                    averageSpeedExcpensiveOver20 += list[id].countExcessiveOver20
                }

                if (list.isNotEmpty()) {
                    averageDistance /= list.size
                    averageSpeed /= list.size // TODO чі правильна середня швидкість?
                    averageSpeedExcpensiveOver20 /= list.size
                }

                tvDistance.text =
                    String.format(getString(R.string.s_km), String.format("%.1f", averageDistance))
                tvAverageDistance.text =
                    String.format(getString(R.string.s_km_hour), "$averageSpeed")
                tvOver20.text = averageSpeedExcpensiveOver20.toString()
                tvExcessiveSpeed.text = "$excessiveSpeed"
                tvEmergencySlowDown.text = "$emergencySlowDown"
            }


        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}