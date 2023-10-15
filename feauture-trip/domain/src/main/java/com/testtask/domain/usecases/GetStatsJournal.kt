package com.testtask.domain.usecases

import com.testtask.domain.commands.AppCommands

class GetStatsJournal(private val appCommands: AppCommands) {

    suspend operator fun invoke() = appCommands.getStatsJournal()
}