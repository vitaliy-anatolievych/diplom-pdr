package com.diplom.diplom_pdr.presentation.utils.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diplom.diplom_pdr.presentation.utils.SpeedStatus
import com.testtask.domain.entities.StatsJournalModel
import com.testtask.domain.usecases.DeleteStatsJournal
import com.testtask.domain.usecases.FillFuelTank
import com.testtask.domain.usecases.GetRecommendSpeed
import com.testtask.domain.usecases.GetStatsJournal
import com.testtask.domain.usecases.IsHaveNotesTripJournal
import com.testtask.domain.usecases.ListenDistance
import com.testtask.domain.usecases.ListenSpeed
import com.testtask.domain.usecases.SetSpeed
import com.testtask.domain.usecases.StartTrip
import com.testtask.domain.usecases.StopTrip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TripViewModel(
    private val startTripUseCase: StartTrip,
    private val stopTripUseCase: StopTrip,
    private val fillFuelTankUseCase: FillFuelTank,
    private val listenSpeedUseCase: ListenSpeed,
    private val listenDistanceUseCase: ListenDistance,
    private val setSpeedUseCase: SetSpeed,
    private val getRecommendSpeedUseCase: GetRecommendSpeed,
    private val getStatsJournalUseCase: GetStatsJournal,
    private val deleteStatsJournalUseCase: DeleteStatsJournal,
    private val isHaveNotesTripJournalUseCase: IsHaveNotesTripJournal
) : ViewModel() {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private var _currentSpeed = MutableLiveData<Int>()
    val currentSpeed: LiveData<Int>
        get() = _currentSpeed

    private var _currentDistance = MutableLiveData<Double>()
    val currentDistance: LiveData<Double>
        get() = _currentDistance

    private var _recommendedSpeed = MutableLiveData<Int?>()
    val recommendedSpeed: LiveData<Int?>
        get() = _recommendedSpeed

    private var _statsJournal = MutableLiveData<StatsJournalModel>()
    val statsJournalModel: LiveData<StatsJournalModel>
        get() = _statsJournal

    private var _isProgramWorking = MutableLiveData<Boolean>()
    val isProgramWorking: LiveData<Boolean>
        get() = _isProgramWorking

    private var _speedStatus = MutableLiveData<SpeedStatus>()
    val speedStatus: LiveData<SpeedStatus>
        get() = _speedStatus

    fun startTrip() {
        coroutineScope.launch {
            startTripUseCase()
        }
    }

    fun stopTrip() {
        coroutineScope.launch {
            stopTripUseCase()
        }
    }

    fun fillFuelTank(fuelTankVolume: String): Boolean =
        if (validInputFuelTank(fuelTankVolume)) {
            coroutineScope.launch {
                fillFuelTankUseCase(fuelTankVolume.toDouble())
                getRecommendedSpeed()
            }
            true
        } else {
            false
        }

    fun setSpeed(speed: String): Boolean =
        if (validInputSpeed(speed)) {
            coroutineScope.launch {
                setSpeedUseCase(speed.toInt())
            }
            true
        } else {
            false
        }

    fun isHaveNotesTripJournal() {
        coroutineScope.launch {
            isHaveNotesTripJournalUseCase()
        }
    }

    fun getRecommendedSpeed() {
        coroutineScope.launch {
            val speed = getRecommendSpeedUseCase()
            withContext(Dispatchers.Main) {
                _recommendedSpeed.postValue(speed)
            }
        }
    }

    fun listenSpeed() {
        coroutineScope.launch {
            withContext(Dispatchers.Default) {
                listenSpeedUseCase {
                    _currentSpeed.postValue(it)
                }
            }
        }
    }

    fun listenDistance() {
        coroutineScope.launch {
            withContext(Dispatchers.Default) {
                listenDistanceUseCase {
                    _currentDistance.postValue(it)
                }
            }
        }
    }

    fun listenSpeedStatus(currentSpeed: Int) {
        coroutineScope.launch {
            withContext(Dispatchers.Main) {
                if (_recommendedSpeed.value != null) {
                    checkStatusSpeed(currentSpeed)
                }
            }
        }
    }

    fun getStatsJournal() {
        coroutineScope.launch {
            val journal = getStatsJournalUseCase()
            withContext(Dispatchers.Main) {
                _statsJournal.value = journal
            }
        }
    }

    fun deleteStatsJournal() {
        coroutineScope.launch {
            deleteStatsJournalUseCase()
            withContext(Dispatchers.Main) {
                _statsJournal.value = StatsJournalModel(listOf())
            }
        }
    }

    private fun checkStatusSpeed(speed: Int) {
        when {
            speed > recommendedSpeed.value!! -> {
                _speedStatus.value = SpeedStatus.LOWER
            }
            speed < recommendedSpeed.value!! -> {
                _speedStatus.value = SpeedStatus.FASTER
            }
            speed == recommendedSpeed.value!! -> {
                _speedStatus.value = SpeedStatus.NORMAL
            }
        }
    }

    private fun validInputFuelTank(fuelTankVolume: String): Boolean {
        return try {
            when {
                fuelTankVolume.isBlank() -> false
                fuelTankVolume.toDouble() <= 0.0 -> false
                else -> true
            }
        } catch (e: NumberFormatException) {
            false
        }
    }

    private fun validInputSpeed(speed: String): Boolean {
        return when {
            speed.isBlank() -> false
            speed.toInt() <= 0 -> false
            speed.toInt() > 300 -> false
            else -> true
        }
    }
}


