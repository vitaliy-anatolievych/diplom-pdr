package com.diplom.diplom_pdr.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.FragmentProfileBinding
import com.diplom.diplom_pdr.presentation.utils.viewmodels.MainViewModel


class ProfileScreen : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding ?: throw NullPointerException("FragmentProfileBinding is null")

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        with(binding) {

            viewModel.userData.observe(viewLifecycleOwner) {
                tvCurrentSplit.text = it.currentInterval.toString()
                tvCurrentCurrentRating.text = it.testRating.toString()


                when (it.currentInterval) {
                    in 0..40 -> {
                        cardCurrentSplit.setCardBackgroundColor(requireContext().getColor(android.R.color.holo_red_dark))
                    }

                    in 40..80 -> {
                        cardCurrentSplit.setCardBackgroundColor(requireContext().getColor(android.R.color.holo_orange_light))
                    }

                    in 80..100 -> {
                        cardCurrentSplit.setCardBackgroundColor(requireContext().getColor(android.R.color.holo_green_light))
                    }
                }

                when (it.testRating) {
                    in 0..40 -> {
                        cardCurrentRating.setCardBackgroundColor(requireContext().getColor(android.R.color.holo_red_dark))
                    }

                    in 40..80 -> {
                        cardCurrentRating.setCardBackgroundColor(requireContext().getColor(android.R.color.holo_orange_light))
                    }

                    in 80..100 -> {
                        cardCurrentRating.setCardBackgroundColor(requireContext().getColor(android.R.color.holo_green_light))
                    }
                }
            }

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