package com.testtask.domain.usecases

import com.testtask.domain.commands.AppCommands

class SetSpeed(private val appCommands: AppCommands) {

    suspend operator fun invoke(speed: Int) = appCommands.setSpeed(speed)
}