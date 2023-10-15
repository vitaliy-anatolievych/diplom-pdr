package com.testtask.data.utils

import com.testtask.data.db.models.RefuelingIntervalDBEntity
import com.testtask.domain.entities.data.RefuelingIntervalData

object Mapper {

    fun mapRefuelingDataToDBModel(data: RefuelingIntervalData): RefuelingIntervalDBEntity =
        RefuelingIntervalDBEntity(
            medianSpeedForTravel = data.medianSpeedForTravel,
            totalDistanceTraveled = data.totalDistanceTraveled
        )
}