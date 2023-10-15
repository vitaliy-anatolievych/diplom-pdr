package com.testtask.data.commands

import com.testtask.data.db.dao.AppDao
import com.testtask.data.db.models.FuelDBModel
import com.testtask.data.db.models.SpeedDBModel
import com.testtask.data.journals.RefuelingIntervalJournal
import com.testtask.data.journals.StatsJournal
import com.testtask.data.journals.TripJournal
import com.testtask.domain.commands.AppCommands
import com.testtask.domain.entities.StatsJournalModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppCommandsImpl(
    private val tripJournal: TripJournal,
    private val refuelingIntervalJournal: RefuelingIntervalJournal,
    private val statsJournal: StatsJournal,
    private val db: AppDao
) : AppCommands {

    override suspend fun startTrip() {
        withContext(Dispatchers.Main) {
            tripJournal.startTrip()
        }
    }

    override suspend fun stopTrip() {
        tripJournal.stopTrip()
        refuelingIntervalJournal.calculateTripData()
    }

    override suspend fun fillFuelTank(fuelTankVolume: Double) {
        db.fillFuelTank(FuelDBModel(fuelVolume = fuelTankVolume))
        val refuelingJournal = db.getRefuelingJournal()
        if (refuelingJournal.isNotEmpty()) {
            val fuel = db.getFuelVolume()
            statsJournal.calculateData(fuel!!)
            db.clearRefuelingJournal()
        }
    }

    override suspend fun listenSpeed(listener: ((Int) -> Unit)) {
        tripJournal.getSpeed(listener)
    }

    override suspend fun listenDistance(listener: (Double) -> Unit) {
        tripJournal.getDistance(listener)
    }

    override suspend fun setSpeed(speed: Int) {
        db.setRecommendedSpeed(SpeedDBModel(speed = speed))
    }

    override suspend fun getRecommendedSpeed(): Int? = db.getRecommendedSpeed()

    override suspend fun getStatsJournal(): StatsJournalModel =
        statsJournal.getStatsJournal()

    override suspend fun deleteStatsJournal() {
        db.clearStatsJournal()
        db.clearRecommendedSpeed()
        db.clearFuelVolume()
    }

    override suspend fun isHaveNotesOnTripJournal() {
        val isHaveNotes = tripJournal.isHaveNotes()
        if (isHaveNotes) {
            refuelingIntervalJournal.calculateTripData()
            db.clearTripJournal()
        }
    }
}