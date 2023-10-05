package com.diplom.diplom_pdr.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.FragmentProfileBinding


class ProfileScreen: Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding ?: throw NullPointerException("FragmentProfileBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        with(binding) {
            btnTrips.setOnClickListener {
                findNavController().navigate(R.id.action_profileScreen_to_statsScreen)
            }

            btnTests.setOnClickListener {
                findNavController().navigate(R.id.action_profileScreen_to_testStatsScreen)
            }

            btnRewards.setOnClickListener {
                findNavController().navigate(R.id.action_profileScreen_to_rewardScreen)
            }

            btnFaq.setOnClickListener {
                findNavController().navigate(R.id.action_profileScreen_to_faqScreen)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}