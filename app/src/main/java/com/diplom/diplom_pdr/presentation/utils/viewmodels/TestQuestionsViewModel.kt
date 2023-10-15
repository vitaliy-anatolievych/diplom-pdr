package com.diplom.diplom_pdr.presentation.utils.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diplom.diplom_pdr.controller.LocalStorage
import com.diplom.diplom_pdr.models.TaskItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TestQuestionsViewModel(
    private val localStorage: LocalStorage
): ViewModel()  {

    private val _questionsForADayData = MutableLiveData<List<TaskItem>>()
    val questionsForADayData: LiveData<List<TaskItem>>
        get() = _questionsForADayData

    private val _questionsRandomData = MutableLiveData<List<TaskItem>>()
    val questionsRandomData: LiveData<List<TaskItem>>
        get() = _questionsRandomData

    private val _favoriteData = MutableLiveData<List<TaskItem>>()
    val favoriteData: LiveData<List<TaskItem>>
        get() = _favoriteData

    fun getFavoriteTests() {
        CoroutineScope(Dispatchers.IO).launch {
            _favoriteData.postValue(localStorage.getFavoriteTasks())
        }
    }

    fun getRandomQuestions() {
        CoroutineScope(Dispatchers.IO).launch {
            val list = localStorage.getRandQuestions(20)
            _questionsRandomData.postValue(list)
        }
    }

    fun getQuestionsForADay() {
        CoroutineScope(Dispatchers.IO).launch {
            val list = localStorage.getRandQuestions(5)
            _questionsForADayData.postValue(list)
        }
    }
}