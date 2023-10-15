package com.diplom.diplom_pdr.presentation.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.FragmentThemesBinding
import com.diplom.diplom_pdr.presentation.utils.adapters.ThemesRVAdapter
import com.diplom.diplom_pdr.presentation.utils.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ThemesScreen: Fragment() {
    private var _binding: FragmentThemesBinding? = null
    private val binding: FragmentThemesBinding
        get() = _binding ?: throw NullPointerException("FragmentThemesBinding is null")

    private val viewModel: MainViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThemesBinding.inflate(inflater, container, false)

        with(binding) {
            val adapter = ThemesRVAdapter()
            rvThemes.adapter = adapter

            viewModel.themeData.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }

            adapter.onTaskClick {
                val direction = ThemesScreenDirections.actionThemesScreenToTaskScreen(it.toTypedArray())
                findNavController().navigate(direction)
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getActualData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}