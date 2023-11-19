package com.diplom.diplom_pdr.presentation.utils.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diplom.diplom_pdr.controller.LocalStorage
import com.diplom.diplom_pdr.models.Answer
import com.diplom.diplom_pdr.models.TaskItem
import com.diplom.diplom_pdr.presentation.screens.TaskScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuestViewModel(
    private val localStorage: LocalStorage
) : ViewModel() {

    private val _favoriteData = MutableLiveData<Boolean>()
    val favoriteData: LiveData<Boolean>
        get() = _favoriteData

    private val _currentQuest = MutableLiveData<TaskItem>()
    val currentQuest: LiveData<TaskItem>
        get() = _currentQuest

    private val _tasksList = MutableLiveData<MutableList<TaskItem>>()
    val tasksList: LiveData<MutableList<TaskItem>>
        get() = _tasksList

    private val _gameEnd = MutableLiveData<Unit>()
    val gameEnd: LiveData<Unit>
        get() = _gameEnd

    private val _answerList = MutableLiveData<List<Answer>>()
    val answerList: LiveData<List<Answer>>
        get() = _answerList

    var questionList: List<TaskScreen.Question>? = null

    private val randAnswers = mutableListOf<Answer>()

    fun insertAnswer(answer: Answer, isRandQuestions: Boolean) {
        if (!isRandQuestions) {
            CoroutineScope(Dispatchers.IO).launch {
                localStorage.insertAnswer(answer)
            }
        } else {
            randAnswers.add(answer)
        }
    }

    fun getAnswerList(question: String, isRandQuestions: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            val list = localStorage.getAnswerList(question)
                .map {
                    it.copy(name = it.name.replace("|", ""))
                }


            if (isRandQuestions) {
                list.map { task -> task.type = Answer.TYPE.DEFAULT }
                if (randAnswers.size > 0) {
                    var isFindAnswer = false
                    val cacheAnswers = mutableListOf<Answer>()
                    randAnswers.forEach {
                        if (it.taskItemQuestion == question) {
                            isFindAnswer = true
                            cacheAnswers.add(it)
                        }
                    }

                    if (isFindAnswer) {
                        // update temp answers

                        val resultList = list.toMutableList()

                        cacheAnswers.forEach { answer ->
                            val index = resultList.indexOfFirst { it.name == answer.name }
                            resultList[index] = answer
                        }
                        _answerList.postValue(resultList)
                    } else {
                        _answerList.postValue(list)
                    }
                } else {
                    _answerList.postValue(list)
                }

            } else {
                _answerList.postValue(list)
            }
        }
    }

    // +
    fun updateRightAnswers(title: String, rightAnswers: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            localStorage.updateRightAnswers(title, rightAnswers)
        }
    }

    // +
    fun updateWrongAnswers(title: String, wrongAnswers: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            localStorage.updateWrongAnswers(title, wrongAnswers)
        }
    }

    // +
    fun updateIsTestPassed(title: String, isTestPassed: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            localStorage.updateIsTestPassed(title, isTestPassed)
        }
    }

    // +
    fun updateTotalTestTime(title: String, totalTestTime: Long) {
        Log.e("TIME", "$totalTestTime")
        CoroutineScope(Dispatchers.IO).launch {
            localStorage.updateTotalTestTime(title, totalTestTime)
        }
    }


    fun setCurrentTask(taskItem: TaskItem) {
        _currentQuest.value = taskItem
    }

    fun setCurrentTasksList(tasks: MutableList<TaskItem>) {
        _tasksList.value = tasks
    }

    fun setFavorite(id: Int, isFavorite: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            localStorage.setFavorite(id, isFavorite)
            _favoriteData.postValue(isFavorite)
        }
    }

    fun nextQuestion(taskItem: TaskItem, step: Int, isRandQuestions: Boolean) {
        _answerList.value?.let {
            if (step < questionList?.size!! - 1) {
                CoroutineScope(Dispatchers.IO).launch {
                    if (!isRandQuestions) localStorage.updateTask(taskItem)
                    val newTaskItem = questionList!![step + 1].taskItem
                    newTaskItem.status = TaskItem.STATUS.SELECTED
                    _currentQuest.postValue(newTaskItem)
                    if (!isRandQuestions) localStorage.updateTask(newTaskItem)

                    val newTaskList =
                        localStorage.getAllTasks(newTaskItem.themeItemTitle)
                            .toMutableList()
                    _tasksList.postValue(newTaskList)
                }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    if (!isRandQuestions) localStorage.updateTask(taskItem)
                    val newTaskList =
                        localStorage.getAllTasks(taskItem.themeItemTitle)
                            .toMutableList()
                    _tasksList.postValue(newTaskList)
                    _gameEnd.postValue(Unit)
                }
            }
        }
    }
}