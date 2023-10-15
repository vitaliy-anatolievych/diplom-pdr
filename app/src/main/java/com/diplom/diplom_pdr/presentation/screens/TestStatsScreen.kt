package com.diplom.diplom_pdr.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.diplom.diplom_pdr.databinding.FragmentTestsStatsBinding
import com.diplom.diplom_pdr.presentation.utils.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TestStatsScreen : Fragment() {

    private var _binding: FragmentTestsStatsBinding? = null
    private val binding: FragmentTestsStatsBinding
        get() = _binding ?: throw NullPointerException("FragmentTestsStatsBinding is null")

    private val viewModel: MainViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTestsStatsBinding.inflate(inflater, container, false)

        viewModel.getStatsResult()
        with(binding) {

            viewModel.statsData.observe(viewLifecycleOwner) {
                tvAllTestsComplete.text = "${it.allTestsPassed}"
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
        val format = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return format.format(Date(milliseconds))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}