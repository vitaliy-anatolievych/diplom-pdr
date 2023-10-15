package com.diplom.diplom_pdr.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.app.App
import com.diplom.diplom_pdr.databinding.FragmentDriveBinding
import com.diplom.diplom_pdr.databinding.FragmentDriveResultBinding
import com.diplom.diplom_pdr.models.DriveStatsModel
import com.diplom.diplom_pdr.presentation.utils.viewmodels.MainViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ResultDriveScreen : Fragment() {

    private var _binding: FragmentDriveResultBinding? = null
    private val binding: FragmentDriveResultBinding
        get() = _binding ?: throw NullPointerException("FragmentDriveResultBinding is null")

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDriveResultBinding.inflate(inflater, container, false)

        val args = ResultDriveScreenArgs.fromBundle(requireArguments())


        with(binding) {

            tvDistance.text = String.format(getString(R.string.s_km), String.format("%.1f", args.distance.toDouble()))
            tvAverageDistance.text = String.format(getString(R.string.s_km_hour), args.medianSpeed)
            tvOver20.text = args.excessOver20.toString()
            tvExcessiveSpeed.text = args.excessiveSpeed.toString()
            tvEmergencySlowDown.text = args.emergencySlowDown.toString()

            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val date = LocalDateTime.now().format(formatter)

            viewModel.saveResultDrive(
                DriveStatsModel(
                    distance = args.distance.toDouble(),
                    averageSpeed = args.medianSpeed,
                    countOfEmergencyDown = args.emergencySlowDown,
                    countOfExcessiveSpeed = args.excessiveSpeed,
                    date = "$date ${args.startTime} - ${args.endTime} ",
                    countExcessiveOver20 = args.excessOver20
                )
            )

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