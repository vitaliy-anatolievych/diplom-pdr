package com.diplom.diplom_pdr.presentation.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.FragmentDriveBinding
import com.diplom.diplom_pdr.presentation.utils.AppPowerManager
import com.diplom.diplom_pdr.presentation.utils.CountUpTimer
import com.diplom.diplom_pdr.presentation.utils.viewmodels.TripViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs


class DriveScreen : Fragment() {
    private var _binding: FragmentDriveBinding? = null
    private val binding: FragmentDriveBinding
        get() = _binding ?: throw NullPointerException("FragmentDriveBinding is null")

    private var isDrivePaused = false

    private val viewModel: TripViewModel by viewModel()
    private var powerManager: AppPowerManager? = null

    private var maxSpeed = 0
    private var distance = 0.0
    private var excessiveSpeed = 0
    private var emergencySlowDown = 0
    private var excessOver20 = 0
    private var startTime = ""
    private var speedList = mutableListOf<Int>()

    private var isUniqExcess = false

    private var timer: CountUpTimer? = null
    private var tripTimeInSeconds = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDriveBinding.inflate(inflater, container, false)

        with(binding) {
            powerManager = AppPowerManager(this.root.context)
            powerManager?.stayWake()
            viewModel.listenSpeed()

            tvRoad.text = String.format(getString(R.string.road_s), "0.0")
            tvMaxSpeed.text = String.format(getString(R.string.max_speed_s), "0")
            setStartMode()

            val emergencySlowList = mutableListOf<Point>()

            var timeCounter = 0
            viewModel.currentSpeed.observe(viewLifecycleOwner) {
                if (!isDrivePaused) {

                    // Calculate Excessive Speed & Emergency Slow Down
                    if (emergencySlowList.isEmpty()) {
                        emergencySlowList.add(Point(System.currentTimeMillis(), it))
                    } else {
                        if (timeCounter < 3) {
                            timeCounter++
                        } else {
                            emergencySlowList.add(Point(System.currentTimeMillis(), it))

                            val deltaV = emergencySlowList[1].speed - emergencySlowList[0].speed
                            val deltaT = emergencySlowList[1].time - emergencySlowList[0].time

                            if (deltaT != 0L && deltaV != 0) {
                                val a = deltaV.toDouble() / deltaT.toDouble()
                                if (a > 0) {
                                    if (a >= (G / 2)) {
                                        excessiveSpeed++
                                    }
                                } else {
                                    Log.e("A", "$a | $deltaT | $deltaV | $emergencySlowDown")
                                    if (abs(a) >= (G / 2)) {
                                        emergencySlowDown++
                                    }
                                }
                            }
                            emergencySlowList.clear()
                            timeCounter = 0
                        }
                    }

                    if (it > maxSpeed) {
                        maxSpeed = it
                        tvMaxSpeed.text = String.format(getString(R.string.max_speed_s), "$it")
                    }

                    if (it > 70) {
                        if (!isUniqExcess) {
                            isUniqExcess = true
                            excessOver20++
                        }
                    }

                    if (isUniqExcess) {
                        if (it < 68) {
                            isUniqExcess = false
                        }
                    }

                    speedList.add(it)
                }
                tvSpeed.text = "$it"
            }

            viewModel.currentDistance.observe(viewLifecycleOwner) {
                if (!isDrivePaused) {
                    distance += it
                    tvRoad.text =
                        String.format(getString(R.string.road_s), String.format("%.1f", distance))
                }
            }

        }

        timer = CountUpTimer(1000) {
            tripTimeInSeconds = it
        }

        return binding.root
    }

    private fun setStartMode() = with(binding) {
        tvAction1.text = getString(R.string.stats)
        tvAction2.text = getString(R.string.start)

        btnAction1.setOnClickListener {
            // stats
            findNavController().navigate(R.id.action_driveScreen_to_statsScreen)
        }

        btnAction2.setOnClickListener {
            setDriveMode()
        }

    }

    private fun setDriveMode() = with(binding) {
        iconAction1.visibility = View.VISIBLE
        iconAction1.setImageResource(R.drawable.ic_pause)
        tvAction1.text = getString(R.string.pause)
        viewModel.startTrip()
        viewModel.listenDistance()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val startT = LocalDateTime.now().format(formatter)
        startTime = startT
        timer?.start()

        btnAction1.setOnClickListener {
            if (!isDrivePaused) {
                iconAction1.setImageResource(R.drawable.ic_play)
                tvAction1.text = getString(R.string.start)
                viewModel.stopTrip()
                timer?.stop()
            } else {
                iconAction1.setImageResource(R.drawable.ic_pause)
                tvAction1.text = getString(R.string.pause)
                viewModel.startTrip()
                timer?.start()
            }

            isDrivePaused = !isDrivePaused
        }

        iconAction2.visibility = View.VISIBLE
        iconAction2.setImageResource(R.drawable.ic_stop)
        tvAction2.text = getString(R.string.stop)

        btnAction2.setOnClickListener {
            // nav stats
            viewModel.stopTrip()
            timer?.stop()
            if (distance != 0.0) {

                val timeToKm = ((tripTimeInSeconds.toDouble() / 1000) / 60) / 60
                Log.e("TRIP", "$timeToKm | $distance")
                val averageSpeed = distance / timeToKm
//                for (speed in speedList) {
//                    averageSpeed += speed
//                }
//
//                averageSpeed /= speedList.size

                val endT = LocalDateTime.now().format(formatter)
                val action = DriveScreenDirections
                    .actionDriveScreenToResultDriveScreen(
                        distance = distance.toString(),
                        medianSpeed = averageSpeed.toInt(),
                        excessiveSpeed = excessiveSpeed,
                        emergencySlowDown = emergencySlowDown,
                        startTime = startTime,
                        endTime = endT,
                        excessOver20 = excessOver20,
                        tripTime = tripTimeInSeconds
                    )
                findNavController().navigate(action)

            } else {
                Toast.makeText(
                    requireContext(),
                    "Немає даних для запису у поїздку",
                    Toast.LENGTH_SHORT
                ).show()
                val id = findNavController().currentDestination?.id
                findNavController().popBackStack(id!!, true)
                findNavController().navigate(id)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val G = 9.8
    }

    data class Point(val time: Long, val speed: Int)
}