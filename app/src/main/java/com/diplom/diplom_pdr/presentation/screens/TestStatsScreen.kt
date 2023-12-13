package com.diplom.diplom_pdr.presentation.screens

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.diplom.diplom_pdr.databinding.FragmentTestsStatsBinding
import com.diplom.diplom_pdr.presentation.utils.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.time.Duration

class TestStatsScreen : Fragment() {

    private var _binding: FragmentTestsStatsBinding? = null
    private val binding: FragmentTestsStatsBinding
        get() = _binding ?: throw NullPointerException("FragmentTestsStatsBinding is null")

    private val viewModel: MainViewModel by activityViewModel()

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTestsStatsBinding.inflate(inflater, container, false)

        viewModel.getStatsResult()
        with(binding) {

            viewModel.statsData.observe(viewLifecycleOwner) {
                tvAllTestsComplete.text = "${it.totalRightAnswers + it.totalWrongAnswers}"
                tvCountRightAnswers.text = "${it.totalRightAnswers}"
                tvPercentRightAnswers.text = "${it.percentRightAnswers}%"
                tvCountFailAnswers.text = "${it.totalWrongAnswers}"
                tvTotalTestTime.text = formatMilliseconds(it.totalTime)
            }

            btnResetProgress.setOnClickListener {
                viewModel.resetAllStats()
            }
        }

        return binding.root
    }

    private fun formatMilliseconds(milliseconds: Long): String {
        val duration = Duration.ofMillis(milliseconds)
        val hours = duration.toHours()
        val minutes = duration.toMinutes()
        val seconds = duration.seconds

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}