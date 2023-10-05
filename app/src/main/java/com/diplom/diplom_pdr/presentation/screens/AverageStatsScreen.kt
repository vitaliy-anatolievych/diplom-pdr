package com.diplom.diplom_pdr.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.FragmentAverageStatsBinding

class AverageStatsScreen: Fragment(R.layout.fragment_average_stats) {

    private var _binding: FragmentAverageStatsBinding? = null
    private val binding: FragmentAverageStatsBinding
        get() = _binding ?: throw NullPointerException("FragmentAverageStatsBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAverageStatsBinding.inflate(inflater, container, false)

        with(binding) {
            tvDistance.text = String.format(getString(R.string.s_km), "0")
            tvAverageDistance.text = String.format(getString(R.string.s_km_hour), "0")
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}