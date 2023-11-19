package com.diplom.diplom_pdr.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.FragmentTestsBinding
import com.diplom.diplom_pdr.models.TaskItem
import com.diplom.diplom_pdr.presentation.utils.viewmodels.TestQuestionsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class TestsScreen : Fragment() {

    private var _binding: FragmentTestsBinding? = null
    private val binding: FragmentTestsBinding
        get() = _binding ?: throw NullPointerException("FragmentTestsBinding is null")

    private val viewModel: TestQuestionsViewModel by viewModel()

    private var questionsForADayData = emptyList<TaskItem>()
    private var questionsRandomData = emptyList<TaskItem>()
    private var favoriteData = emptyList<TaskItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTestsBinding.inflate(inflater, container, false)

        with(binding) {
            viewModel.getQuestionsForADay()
            viewModel.getRandomQuestions()
            viewModel.getFavoriteTests()

            viewModel.questionsForADayData.observe(viewLifecycleOwner) { list ->
                // clear
                list.map { task -> task.status = TaskItem.STATUS.CLOSE }
                questionsForADayData = list
            }

            viewModel.questionsRandomData.observe(viewLifecycleOwner) { list ->
                // clear
                list.map { task -> task.status = TaskItem.STATUS.CLOSE }
                questionsRandomData = list
            }

            viewModel.favoriteData.observe(viewLifecycleOwner) {
                favoriteData = it
            }

            btnQuestionOfDay.setOnClickListener {
                if (questionsForADayData.isNotEmpty()) {
                    val action =
                        TestsScreenDirections.actionTestsScreenToTaskScreen(
                            questionsForADayData.toTypedArray(),
                            true
                        )
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Немає завдань",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            btnAllThemes.setOnClickListener {
                findNavController().navigate(R.id.action_testsScreen_to_themesScreen)
            }

            btnRandQuestions.setOnClickListener {
                if (questionsRandomData.isNotEmpty()) {
                    val action =
                        TestsScreenDirections.actionTestsScreenToTaskScreen(
                            questionsRandomData.toTypedArray(),
                            true
                        )

                    findNavController().navigate(action)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Немає завдань",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            btnFavoriteQuestions.setOnClickListener {
                if (favoriteData.isNotEmpty()) {
                    val action =
                        TestsScreenDirections.actionTestsScreenToTaskScreen(favoriteData.toTypedArray())
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Немає обраних завдань",
                        Toast.LENGTH_SHORT
                    ).show()
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