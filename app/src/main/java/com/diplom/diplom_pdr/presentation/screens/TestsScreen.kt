package com.diplom.diplom_pdr.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.FragmentStatsBinding
import com.diplom.diplom_pdr.databinding.FragmentTestsBinding
import com.diplom.diplom_pdr.presentation.utils.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class TestsScreen : Fragment() {

    private var _binding: FragmentTestsBinding? = null
    private val binding: FragmentTestsBinding
        get() = _binding ?: throw NullPointerException("FragmentTestsBinding is null")

    private val viewModel: MainViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTestsBinding.inflate(inflater, container, false)

        with(binding) {

            btnQuestionOfDay.setOnClickListener {
                viewModel.getQuestionsForADay()
                viewModel.questionsForADayData.observe(viewLifecycleOwner) {
                    val action =
                        TestsScreenDirections.actionTestsScreenToTaskScreen(it.toTypedArray())
                    findNavController().navigate(action)
                }
            }

            btnAllThemes.setOnClickListener {
                findNavController().navigate(R.id.action_testsScreen_to_themesScreen)
            }

            btnRandQuestions.setOnClickListener {
                viewModel.getRandomQuestions()
                viewModel.questionsRandomData.observe(viewLifecycleOwner) {
                    val action =
                        TestsScreenDirections.actionTestsScreenToTaskScreen(it.toTypedArray())
                    findNavController().navigate(action)
                }
            }

            btnFavoriteQuestions.setOnClickListener {
                viewModel.getFavoriteTests()
                viewModel.favoriteData.observe(viewLifecycleOwner) {
                    if (it.isNotEmpty()) {
                        val action =
                            TestsScreenDirections.actionTestsScreenToTaskScreen(it.toTypedArray())
                        findNavController().navigate(action)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Немає обраних завдань",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
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