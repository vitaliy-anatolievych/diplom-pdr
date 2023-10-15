package com.testtask.domain.usecases

import com.testtask.domain.commands.AppCommands

class FillFuelTank(private val appCommands: AppCommands) {

    suspend operator fun invoke(fuelTankVolume: Double) = appCommands.fillFuelTank(fuelTankVolume)
}