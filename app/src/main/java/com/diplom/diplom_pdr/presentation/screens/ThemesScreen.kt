package com.diplom.diplom_pdr.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.FragmentThemesBinding
import com.diplom.diplom_pdr.models.ThemeItem
import com.diplom.diplom_pdr.presentation.utils.adapters.ThemesRVAdapter

class ThemesScreen: Fragment() {
    private var _binding: FragmentThemesBinding? = null
    private val binding: FragmentThemesBinding
        get() = _binding ?: throw NullPointerException("FragmentThemesBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThemesBinding.inflate(inflater, container, false)

        with(binding) {
            val adapter = ThemesRVAdapter()
            rvThemes.adapter = adapter

            val list = listOf(
                ThemeItem(
                    id = 0,
                    title = getString(R.string.general_terms),
                    allQuestion = 10
                ),
                ThemeItem(
                    id = 1,
                    title = getString(R.string.right_drivers),
                    allQuestion = 15
                ),
                ThemeItem(
                    id = 2,
                    title = getString(R.string.movement_of_vehicles_with_special_signals),
                    allQuestion = 7
                ),
                ThemeItem(
                    id = 3,
                    title = getString(R.string.responsibilities_and_rights_of_pedestrians),
                    allQuestion = 8
                ),
                ThemeItem(
                    id = 4,
                    title = getString(R.string.responsibilities_and_rights_of_passengers),
                    allQuestion = 15
                ),
            )

            adapter.submitList(list)

            adapter.onTaskClick {
                findNavController().navigate(R.id.action_themesScreen_to_taskScreen)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}