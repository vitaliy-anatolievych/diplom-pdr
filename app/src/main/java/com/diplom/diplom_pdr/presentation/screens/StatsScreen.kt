package com.diplom.diplom_pdr.presentation.screens

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.FragmentStatsBinding
import com.diplom.diplom_pdr.models.DriveStatsModel
import com.diplom.diplom_pdr.presentation.utils.adapters.DriveStatsRVAdapter
import com.diplom.diplom_pdr.presentation.utils.viewmodels.MainViewModel

class StatsScreen: Fragment() {

    private var _binding: FragmentStatsBinding? = null
    private val binding: FragmentStatsBinding
        get() = _binding ?: throw NullPointerException("FragmentStatsBinding is null")

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatsBinding.inflate(inflater, container, false)

        val adapter = DriveStatsRVAdapter()
        viewModel.getDriveStats()


        with(binding) {
            rvStats.adapter = adapter

            viewModel.driveStatsData.observe(viewLifecycleOwner) {
                adapter.submitList(it.reversed())
            }

            btnGlobalStats.setOnClickListener {
                findNavController().navigate(R.id.action_statsScreen_to_averageStatsScreen)
            }

            btnResetProgress.setOnClickListener {
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.allert))
                    .setMessage(getString(R.string.is_delete_data))
                    .setPositiveButton("Так"
                    ) { dialog, which ->
                        viewModel.deleteDriveStats()
                    }
                    .setNegativeButton("Ні"
                    ) { dialog, which -> dialog.cancel() }
                    .show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}