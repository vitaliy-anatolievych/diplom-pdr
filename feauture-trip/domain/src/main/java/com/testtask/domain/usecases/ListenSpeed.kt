package com.testtask.domain.usecases

import com.testtask.domain.commands.AppCommands

class ListenSpeed(private val appCommands: AppCommands) {

    suspend operator fun invoke(listener: ((Int) -> Unit)) = appCommands.listenSpeed(listener)
}