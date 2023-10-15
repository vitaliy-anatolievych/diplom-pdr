package com.testtask.data.utils

import android.os.Parcelable
import com.testtask.data.db.dao.AppDao
import com.testtask.data.db.models.TripDataDBEntity
import com.testtask.data.dto.TripDto
import com.testtask.domain.entities.data.TripData
import com.testtask.domain.utils.ConvertUtils
import kotlinx.parcelize.Parcelize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.parcelize.IgnoredOnParcel

@Parcelize
class TripDataManager: Parcelable {

    @IgnoredOnParcel
    private var _currentTripList: List<TripDataDBEntity>? = null
    private val currentTripList: List<TripDataDBEntity>
        get() = _currentTripList ?: throw NullPointerException("Поездка не была начата")

    companion object {
         var speedListener: ((Int) -> Unit)? = null
         var distanceListener: ((Double) -> Unit)? = null
    }

    fun addTripData(tripDto: TripDto, db: AppDao) {
        val currentSpeed = ConvertUtils.meterOnSecInKmPerHour(tripDto.speed)
        CoroutineScope(Dispatchers.IO).launch {
            _currentTripList = db.getTripJournal()
            if (currentSpeed > 7.0) {
                if (currentTripList.isNotEmpty()) {

                    val tripData = TripData(
                        time = tripDto.time,
                        speed = ConvertUtils.meterOnSecInKmPerHour(tripDto.speed),
                        time_interval = calculateTimeInterval(currentTripList.size, tripDto),
                        average_speed = calculateAverageSpeed(currentTripList.size, tripDto),
                    )

                    tripData.distance =
                        calculateDistance(tripData.time_interval!!, tripData.average_speed!!)

                    /**
                     * Callback distance
                     */

                    distanceListener?.invoke(tripData.distance ?: 0.0)

                    addDataToDb(tripData, db)
                } else {
                    val tripData = TripData(
                        time = tripDto.time,
                        speed = ConvertUtils.meterOnSecInKmPerHour(tripDto.speed)
                    )

                    addDataToDb(tripData, db)
                }
            }
        }

        speedListener?.invoke(currentSpeed.toInt())
    }

    private fun addDataToDb(tripData: TripData, db: AppDao) {
        db.saveTripJournal(
            TripDataDBEntity(
                time = tripData.time,
                speed = tripData.speed,
                time_interval = tripData.time_interval,
                average_speed = tripData.average_speed,
                distance = tripData.distance
            )
        )
    }

    private fun calculateTimeInterval(index: Int, tripDto: TripDto): Long {
        val currentTime = tripDto.time
        val previousTime = currentTripList[index - 1].time

        return currentTime - previousTime
    }

    private fun calculateAverageSpeed(index: Int, tripDto: TripDto): Double {
        val currentSpeed = ConvertUtils.meterOnSecInKmPerHour(tripDto.speed)
        val previousSpeed = currentTripList[index - 1].speed

        return (currentSpeed + previousSpeed) / 2
    }

    private fun calculateDistance(deltaTime: Long, averageSpeed: Double): Double {
        val convertDeltaTime = (deltaTime.toDouble() / 1000) // приходит 999

        return ((ConvertUtils.kmPerHourImMeterOnSec(averageSpeed) * convertDeltaTime) / 1000)

    }
}