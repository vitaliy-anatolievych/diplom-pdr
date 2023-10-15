package com.testtask.domain.usecases

import com.testtask.domain.commands.AppCommands

class ListenDistance(private val appCommands: AppCommands) {

    suspend operator fun invoke(listener: ((Double) -> Unit)) = appCommands.listenDistance(listener)
}