package com.testtask.data.journals

import com.testtask.data.db.models.FuelDBModel
import com.testtask.domain.entities.StatsJournalModel

interface StatsJournal {
    suspend fun calculateData(fuel: FuelDBModel)
    suspend fun getStatsJournal(): StatsJournalModel
}