package com.diplom.diplom_pdr.presentation.utils.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diplom.diplom_pdr.controller.LocalStorage
import com.diplom.diplom_pdr.models.DriveStatsModel
import com.diplom.diplom_pdr.models.TaskItem
import com.diplom.diplom_pdr.models.TestsResultEntity
import com.diplom.diplom_pdr.models.ThemeItem
import com.diplom.diplom_pdr.models.ThemeItemWithTasks
import com.testtask.data.db.AppDataBase
import com.testtask.data.db.dao.AppDao
import com.testtask.data.db.models.TripDataDBEntity
import com.testtask.data.journals.TripJournal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Duration

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
            } else {
                _themeData.postValue(localStorage.getAllData())
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

            Log.e("TESTS", "$totalRightAnswers | $totalWrongAnswers")

            if (totalRightAnswers != 0 && totalWrongAnswers != 0) {
                val result =  (totalRightAnswers.toFloat() / (totalRightAnswers + totalWrongAnswers)) * 100
                testResult.percentRightAnswers = result.toInt()
                Log.e("TESTS", "${testResult.percentRightAnswers} | $result")
            } else {
                if (totalRightAnswers > 0) {
                    testResult.percentRightAnswers = 100
                } else {
                    testResult.percentRightAnswers = 0
                }
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