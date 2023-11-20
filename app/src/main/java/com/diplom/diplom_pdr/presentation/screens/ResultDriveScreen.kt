package com.diplom.diplom_pdr.presentation.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.FragmentDriveResultBinding
import com.diplom.diplom_pdr.models.DriveStatsModel
import com.diplom.diplom_pdr.presentation.utils.viewmodels.MainViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

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

            tvDistance.text = String.format(
                getString(R.string.s_km),
                String.format("%.1f", args.distance.toDouble())
            )
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

        calculateTripRating(
            tripTimeInSeconds = args.tripTime,
            distance = args.distance.toDouble(),
            averageSpeed = args.medianSpeed,
            countOfEmergencyDown = args.emergencySlowDown,
            countOfExcessiveSpeed = args.excessiveSpeed,
            countExcessiveOver20 = args.excessOver20
        )

        return binding.root
    }

    private fun calculateTripRating(
        tripTimeInSeconds: Long,
        distance: Double,
        averageSpeed: Int,
        countOfEmergencyDown: Int,
        countOfExcessiveSpeed: Int,
        countExcessiveOver20: Int
        ) {
        // 668857 | 11.774730493720375
        Log.e("TRIP", "$tripTimeInSeconds | $distance")

        if (tripTimeInSeconds > 600_000 && distance > 3.0) {

            val today = Calendar.getInstance()
            today.add(Calendar.DAY_OF_YEAR, 1)

            today.clear(Calendar.HOUR_OF_DAY)
            today.clear(Calendar.MINUTE)
            today.clear(Calendar.SECOND)
            today.clear(Calendar.MILLISECOND)

            val tomorrow = today.time

            val user = viewModel.userData.value!!.copy()
            user.enterDate = tomorrow.time
            user.currentInterval += 1
            viewModel.updateUser(user)

            var tripRating = 0

            // fines
            if (averageSpeed >= 35) tripRating -= 1
            if (countExcessiveOver20 >= 5) tripRating -= 2
            if (countExcessiveOver20 in 2..5) tripRating -= 1
            if (countOfEmergencyDown >= 2) tripRating -= 2
            if (countOfEmergencyDown == 1)  tripRating -= 1
            if (countOfExcessiveSpeed >= 1)  tripRating -= 1

            // rewards
            if (averageSpeed < 35) tripRating += 1
            if (countExcessiveOver20 in 0 .. 1) tripRating += 1
            if (countOfEmergencyDown in 0 .. 1) tripRating += 1
            if (countOfExcessiveSpeed == 0) tripRating += 1

            viewModel.updateTripRating(tripRating)


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}