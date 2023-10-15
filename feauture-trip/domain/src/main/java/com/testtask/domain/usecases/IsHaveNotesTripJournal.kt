package com.testtask.domain.usecases

import com.testtask.domain.commands.AppCommands

class IsHaveNotesTripJournal(private val appCommands: AppCommands) {

    suspend operator fun invoke() = appCommands.isHaveNotesOnTripJournal()
}