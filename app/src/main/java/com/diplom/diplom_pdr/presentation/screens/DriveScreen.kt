package com.diplom.diplom_pdr.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.FragmentDriveBinding


class DriveScreen : Fragment() {


    private var _binding: FragmentDriveBinding? = null
    private val binding: FragmentDriveBinding
        get() = _binding ?: throw NullPointerException("FragmentDriveBinding is null")

    private var isDrivePaused = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDriveBinding.inflate(inflater, container, false)

        with(binding) {
            tvRoad.text = String.format(getString(R.string.road_s), "0.0")
            tvMaxSpeed.text = String.format(getString(R.string.max_speed_s), "0")

            setStartMode()
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

        btnAction1.setOnClickListener {
            if (!isDrivePaused) {
                iconAction1.setImageResource(R.drawable.ic_play)
                tvAction1.text = getString(R.string.start)
            } else {
                iconAction1.setImageResource(R.drawable.ic_pause)
                tvAction1.text = getString(R.string.pause)
            }

            isDrivePaused = !isDrivePaused
        }

        iconAction2.visibility = View.VISIBLE
        iconAction2.setImageResource(R.drawable.ic_stop)
        tvAction2.text = getString(R.string.stop)

        btnAction2.setOnClickListener {
            // nav stats
            findNavController().navigate(R.id.action_driveScreen_to_resultDriveScreen)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}