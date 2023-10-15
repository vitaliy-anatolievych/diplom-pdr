package com.diplom.diplom_pdr.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.diplom.diplom_pdr.models.Answer
import com.diplom.diplom_pdr.models.DriveStatsModel
import com.diplom.diplom_pdr.models.TaskItem
import com.diplom.diplom_pdr.models.TestsResultEntity
import com.diplom.diplom_pdr.models.ThemeItem
import com.diplom.diplom_pdr.models.ThemeItemWithTasks

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswer(answer: Answer)

    @Insert
    suspend fun insertAnswer(answers: List<Answer>)

    @Query("SELECT * FROM answer WHERE taskItemQuestion =:question")
    suspend fun getAnswerList(question: String): List<Answer>


    @Query("SELECT * FROM task_item WHERE is_favorite = :isFavorite")
    suspend fun getFavoriteTasks(isFavorite: Boolean): List<TaskItem>

    @Query("DELETE FROM drive_stats")
    suspend fun deleteDriveStats()

    @Query("SELECT * FROM drive_stats")
    suspend fun getDriveStats(): List<DriveStatsModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDriveStats(driveStatsModel: DriveStatsModel)

    @Query("UPDATE theme_item SET test_time = :totalTestTime WHERE id = :id")
    suspend fun updateTotalTestTime(id: Int, totalTestTime: Long)

    @Query("UPDATE theme_item SET is_test_passed = :isTestPassed WHERE id = :id")
    suspend fun updateIsTestPassed(id: Int, isTestPassed: Boolean)

    @Query("UPDATE theme_item SET wrong_answers = :wrongAnswers WHERE id = :id")
    suspend fun updateWrongAnswers(id: Int, wrongAnswers: Int)

    @Query("UPDATE theme_item SET right_answers = :rightAnswers WHERE id = :id")
    suspend fun updateRightAnswers(id: Int, rightAnswers: Int)

    @Query("SELECT * FROM tests_result")
    fun getTestsResult(): TestsResultEntity

    @Query("DELETE FROM tests_result")
    suspend fun deleteTestResult()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestResult(testsResultEntity: TestsResultEntity)

    @Query("SELECT * FROM task_item WHERE themeItemTitle =:title")
    fun getAllTasks(title: String): List<TaskItem>

    @Query("DELETE FROM theme_item")
    suspend fun deleteAllThemes()

    @Query("SELECT * FROM theme_item")
    fun getAllThemes(): List<ThemeItem>

    @Transaction
    @Query("SELECT * FROM theme_item")
    suspend fun getThemeItemWithTasks(): List<ThemeItemWithTasks>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThemeItem(themeItem: ThemeItem)

    @Query("SELECT * FROM theme_item where title = :title")
    suspend fun getThemeItem(title: String): ThemeItem

    @Query("DELETE FROM task_item")
    suspend fun deleteTaskItem()

    @Query("SELECT * FROM task_item where id=:id")
    suspend fun getTaskItem(id: Int): TaskItem

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskItem(taskItem: TaskItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListTaskItem(taskItem: List<TaskItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListThemeItem(themeItem: List<ThemeItem>)

    @Query("UPDATE task_item SET is_favorite = :isFavorite WHERE id = :id")
    fun updateIsFavoriteTaskItem(id: Int, isFavorite: Boolean)
}