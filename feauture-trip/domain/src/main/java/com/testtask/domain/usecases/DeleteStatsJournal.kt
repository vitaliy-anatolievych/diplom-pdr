package com.testtask.domain.usecases

import com.testtask.domain.commands.AppCommands

class DeleteStatsJournal(private val appCommands: AppCommands) {
    suspend operator fun invoke() = appCommands.deleteStatsJournal()
}