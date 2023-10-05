package com.diplom.diplom_pdr.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.FragmentDriveBinding
import com.diplom.diplom_pdr.databinding.FragmentDriveResultBinding

class ResultDriveScreen : Fragment() {

    private var _binding: FragmentDriveResultBinding? = null
    private val binding: FragmentDriveResultBinding
        get() = _binding ?: throw NullPointerException("FragmentDriveResultBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDriveResultBinding.inflate(inflater, container, false)

        with(binding) {
            tvDistance.text = String.format(getString(R.string.s_km), "0")
            tvAverageDistance.text = String.format(getString(R.string.s_km_hour), "0")

            btnActionNext.setOnClickListener {
                // nav to stats
                findNavController().navigate(R.id.action_resultDriveScreen_to_statsScreen)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}