package com.diplom.diplom_pdr.presentation.utils.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diplom.diplom_pdr.controller.LocalStorage
import com.diplom.diplom_pdr.models.DriveStatsModel
import com.diplom.diplom_pdr.models.TestsResultEntity
import com.diplom.diplom_pdr.models.ThemeItemWithTasks
import com.diplom.diplom_pdr.models.UserEntity
import com.testtask.data.db.dao.AppDao
import com.testtask.data.db.models.TripDataDBEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val localStorage: LocalStorage,
    private val appDb: AppDao,
) : ViewModel() {

    private val _themeData = MutableLiveData<List<ThemeItemWithTasks>>()
    val themeData: LiveData<List<ThemeItemWithTasks>>
        get() = _themeData

    private val _statsData = MutableLiveData<TestsResultEntity>()
    val statsData: LiveData<TestsResultEntity>
        get() = _statsData

    private val _driveStatsData = MutableLiveData<List<DriveStatsModel>>()
    val driveStatsData: LiveData<List<DriveStatsModel>>
        get() = _driveStatsData

    private val _tripJournalData = MutableLiveData<List<TripDataDBEntity>>()
    val tripJournalData: LiveData<List<TripDataDBEntity>>
        get() = _tripJournalData

    private val _userData = MutableLiveData<UserEntity>()
    val userData: LiveData<UserEntity>
        get() = _userData


    fun updateTripRating(tripRating: Int) {
        val user = _userData.value!!
        val userUpd = user.copy(driveRating = user.driveRating + tripRating)
        if (userUpd.driveRating < 100) {
            updateUser(userUpd)
        }
    }

    fun updateUser(userEntity: UserEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            localStorage.insertUser(userEntity)
            _userData.postValue(localStorage.getUser())
        }
    }

    fun getUser() {
        CoroutineScope(Dispatchers.IO).launch {
            _userData.postValue(localStorage.getUser())
        }
    }

    fun getTripJournal() {
        CoroutineScope(Dispatchers.IO).launch {
            _tripJournalData.postValue(appDb.getTripJournal())
        }
    }

    fun deleteDriveStats() {
        CoroutineScope(Dispatchers.IO).launch {
            localStorage.deleteDriveStats()
            _driveStatsData.postValue(emptyList())
        }
    }

    fun getDriveStats() {
        CoroutineScope(Dispatchers.IO).launch {
            _driveStatsData.postValue(localStorage.getDriveStats())
        }
    }

    fun saveResultDrive(driveStatsModel: DriveStatsModel) {
        CoroutineScope(Dispatchers.IO).launch {
            localStorage.saveResultDrive(driveStatsModel)
        }
    }

    fun fillData() {
        CoroutineScope(Dispatchers.IO).launch {
            if (checkIsFirstStartApp()) {
                localStorage.loadNewDBTasks()
                _themeData.postValue(localStorage.getAllData())
                localStorage.insertTestResult(
                    TestsResultEntity(
                        allTestsPassed = 0,
                        totalRightAnswers = 0,
                        totalWrongAnswers = 0,
                        percentRightAnswers = 0,
                        totalTime = 0L
                    )
                )
                updateUser(UserEntity(driveRating = 100, testRating = 60))
            } else {
                _themeData.postValue(localStorage.getAllData())
                getUser()
            }
        }
    }

    fun getActualData() {
        CoroutineScope(Dispatchers.IO).launch {
            _themeData.postValue(localStorage.getAllData())
        }
    }

    fun getStatsResult() {
        CoroutineScope(Dispatchers.IO).launch {
            val testResult = localStorage.getTestsResult()
            val allThemes = localStorage.getAllThemes()

            var allTestPassed = 0
            for (theme in allThemes) {
                if (theme.isTestPassed) allTestPassed++
            }

            var totalRightAnswers = 0
            for (theme in allThemes) {
                totalRightAnswers += theme.rightAnswers
            }
            var totalWrongAnswers = 0
            for (theme in allThemes) {
                totalWrongAnswers += theme.wrongAnswers
            }
            var totalTime = 0L
            for (theme in allThemes) {
                totalTime += theme.totalTestTime
            }

            testResult.allTestsPassed = allTestPassed
            testResult.totalRightAnswers = totalRightAnswers
            testResult.totalWrongAnswers = totalWrongAnswers
            testResult.totalTime = totalTime

            if (totalRightAnswers != 0) {
                val result =
                    (totalRightAnswers.toFloat() / (totalRightAnswers + totalWrongAnswers)) * 100
                testResult.percentRightAnswers = result.toInt()
            } else {
                testResult.percentRightAnswers = 0
            }


            localStorage.insertTestResult(testResult)
            _statsData.postValue(testResult)
        }
    }

    fun resetAllStats() {
        CoroutineScope(Dispatchers.IO).launch {
            localStorage.deleteTestResult()
            localStorage.deleteThemes()
            localStorage.deleteTaskItem()
            localStorage.loadNewDBTasks()
            _themeData.postValue(localStorage.getAllData())
            localStorage.insertTestResult(
                TestsResultEntity(
                    allTestsPassed = 0,
                    totalRightAnswers = 0,
                    totalWrongAnswers = 0,
                    percentRightAnswers = 0,
                    totalTime = 0L
                )
            )
            getStatsResult()
        }
    }

    private suspend fun checkIsFirstStartApp(): Boolean = localStorage.checkIsDBEmpty()
}