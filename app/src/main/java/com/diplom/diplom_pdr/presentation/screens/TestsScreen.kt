package com.diplom.diplom_pdr.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.FragmentStatsBinding
import com.diplom.diplom_pdr.databinding.FragmentTestsBinding


class TestsScreen: Fragment() {

    private var _binding: FragmentTestsBinding? = null
    private val binding: FragmentTestsBinding
        get() = _binding ?: throw NullPointerException("FragmentTestsBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTestsBinding.inflate(inflater, container, false)

        with(binding) {

            btnQuestionOfDay.setOnClickListener {
                findNavController().navigate(R.id.action_testsScreen_to_taskScreen)
            }

            btnRandQuestions.setOnClickListener {
                findNavController().navigate(R.id.action_testsScreen_to_taskScreen)
            }

            btnAllThemes.setOnClickListener {
                findNavController().navigate(R.id.action_testsScreen_to_themesScreen)
            }

            btnStats.setOnClickListener {
                findNavController().navigate(R.id.action_testsScreen_to_testStatsScreen)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}