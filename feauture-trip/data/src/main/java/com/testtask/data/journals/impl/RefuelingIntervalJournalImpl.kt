package com.testtask.data.journals.impl

import com.testtask.data.db.dao.AppDao
import com.testtask.data.db.models.TripDataDBEntity
import com.testtask.data.journals.RefuelingIntervalJournal
import com.testtask.data.utils.Mapper
import com.testtask.domain.entities.data.RefuelingIntervalData
import com.testtask.domain.utils.ConvertUtils

class RefuelingIntervalJournalImpl(
    private val db: AppDao
) : RefuelingIntervalJournal {

    override suspend fun calculateTripData() {
        val tripData = db.getTripJournal()

        if (tripData.isNotEmpty() && tripData.size >= 3) {
            saveJournal(tripData)
            db.clearTripJournal()
        }
    }

    private fun saveJournal(tripData: List<TripDataDBEntity>) {
        db.saveRefuelingJournal(
            Mapper.mapRefuelingDataToDBModel(
                RefuelingIntervalData(
                    medianSpeedForTravel = calculateMedianSpeed(tripData),
                    totalDistanceTraveled = calculateTotalDistance(tripData)
                )
            )
        )
    }

    private fun calculateMedianSpeed(tripData: List<TripDataDBEntity>): Double {
        val listOfAverageSpeed = mutableListOf<Double>()
        tripData.forEach {
            if (it.average_speed != null && it.average_speed != 0.0) {
                listOfAverageSpeed.add(it.average_speed.toDouble())
            }
        }

//        listOfAverageSpeed.sortBy { it }

        return ConvertUtils.calculateMedian(
            mutableListOf<Double>().apply {
                listOfAverageSpeed.map {
                    this.add(it)
                }
            }
        )
    }

    private fun calculateTotalDistance(tripData: List<TripDataDBEntity>): Double {
        var resultCalculate = 0.0
        if (tripData.size > 1) {
            tripData.forEachIndexed { index, tripData ->
                if (index >= 1) {
                    resultCalculate += tripData.distance!!
                }
            }
        }

        return ((resultCalculate * 100) / 100.0)
    }
}