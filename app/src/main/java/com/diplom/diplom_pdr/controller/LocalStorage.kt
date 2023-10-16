package com.diplom.diplom_pdr.controller

import android.content.Context
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.db.Dao
import com.diplom.diplom_pdr.models.Answer
import com.diplom.diplom_pdr.models.DriveStatsModel
import com.diplom.diplom_pdr.models.TaskItem
import com.diplom.diplom_pdr.models.TestsResultEntity
import com.diplom.diplom_pdr.models.ThemeItem
import com.diplom.diplom_pdr.models.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.charset.StandardCharsets

class LocalStorage(private val context: Context, private val dao: Dao) {

    suspend fun insertUser(userEntity: UserEntity) = dao.insertUser(userEntity)

    suspend fun getUser() = dao.getUser()

    suspend fun insertAnswer(answer: Answer) = dao.insertAnswer(answer)

    suspend fun getAnswerList(question: String) = dao.getAnswerList(question)

    suspend fun getRandQuestions(count: Int): List<TaskItem> {
        return try {
            val themes = dao.getThemeItemWithTasks()

            val allTasks = themes.flatMap { it.taskItem }

            val randomNums = mutableSetOf<Int>()
            while (randomNums.size < count) {
                val randNum = (allTasks.indices).random()
                randomNums.add(randNum)
            }

            val randTasks = mutableListOf<TaskItem>()
            randomNums.forEachIndexed { index, i ->
                randTasks.add(allTasks[i])
            }

            randTasks
        } catch (e: Exception) {
            emptyList<TaskItem>()
        }
    }

    suspend fun getFavoriteTasks() = dao.getFavoriteTasks(true)

    suspend fun deleteTaskItem() = dao.deleteTaskItem()

    suspend fun deleteThemes() = dao.deleteAllThemes()

    suspend fun deleteTestResult() = dao.deleteTestResult()

    suspend fun deleteDriveStats() = dao.deleteDriveStats()

    suspend fun getDriveStats() = dao.getDriveStats()

    suspend fun saveResultDrive(driveStatsModel: DriveStatsModel) =
        dao.insertDriveStats(driveStatsModel)

    fun getAllThemes() = dao.getAllThemes()

    suspend fun updateTotalTestTime(title: String, totalTestTime: Long) {
        val theme = getThemeItem(title)
        dao.updateTotalTestTime(theme.id.toInt(), totalTestTime)
    }

    suspend fun updateIsTestPassed(title: String, isTestPassed: Boolean) {
        val theme = getThemeItem(title)
        dao.updateIsTestPassed(theme.id.toInt(), isTestPassed)
    }

    suspend fun updateWrongAnswers(title: String, wrongAnswers: Int) {
        val theme = getThemeItem(title)
        dao.updateWrongAnswers(theme.id.toInt(), wrongAnswers)
    }

    suspend fun updateRightAnswers(title: String, rightAnswers: Int) {
        val theme = getThemeItem(title)
        dao.updateRightAnswers(theme.id.toInt(), rightAnswers)
    }

    fun getTestsResult() = dao.getTestsResult()

    suspend fun insertTestResult(testsResultEntity: TestsResultEntity) =
        dao.insertTestResult(testsResultEntity)

    fun getAllTasks(title: String): List<TaskItem> = dao.getAllTasks(title)


    suspend fun updateTask(taskItem: TaskItem) {
        dao.insertTaskItem(taskItem)
    }

    fun setFavorite(id: Int, isFavorite: Boolean) {
        dao.updateIsFavoriteTaskItem(id, isFavorite)
    }

    suspend fun getThemeItem(title: String) = dao.getThemeItem(title)

    suspend fun getCurrentTaskItem(id: Int) = dao.getTaskItem(id)

    suspend fun getAllData() = dao.getThemeItemWithTasks()

    suspend fun loadNewDBTasks() {
        putListThemes()
        val listThemes = dao.getThemeItemWithTasks()
        val assetsManager = context.assets
        for (i in 1..33) {
            coroutineScope {
                launch {
                    withContext(Dispatchers.IO) {
                        val input = assetsManager.open("test$i.txt")
                        val bytes = input.readBytes()
                        val text = String(bytes, StandardCharsets.UTF_8)
                        input.close()

                        val parse = text.split("<newline>")

                        for (task in 0 until parse.size - 1) {
                            val parseTask = parse[task].split("|")
                                .filter { it.isNotBlank() }
                                .map { it }

                            val img = parseTask[0]
                            val question = parseTask[1]
                            val answers =
                                parseTask
                                    .slice(2 until parseTask.size - 1)
                                    .map { "$it|" }
                                    .map {
                                        Answer(
                                            name = it,
                                            type = Answer.TYPE.DEFAULT,
                                            taskItemQuestion = question
                                        )
                                    }

                            dao.insertAnswer(answers)

                            val rightAnswer = parseTask[parseTask.size - 1]

                            dao.insertTaskItem(
                                TaskItem(
                                    question = question,
                                    rightAnswer = rightAnswer,
                                    imgURL = img,
                                    themeItemTitle = listThemes[i - 1].themeItem.title,
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    suspend fun checkIsDBEmpty(): Boolean = dao.getThemeItemWithTasks().isEmpty()

    private suspend fun putListThemes() = with(context) {
        val list = listOf(
            ThemeItem(title = getString(R.string.general_terms)),
            ThemeItem(title = getString(R.string.right_drivers)),
            ThemeItem(title = getString(R.string.movement_of_vehicles_with_special_signals)),
            ThemeItem(title = getString(R.string.responsibilities_and_rights_of_pedestrians)),
            ThemeItem(title = getString(R.string.responsibilities_and_rights_of_passengers)),
            ThemeItem(title = getString(R.string.requirements_for_cyclists)),
            ThemeItem(title = getString(R.string.requirements_for_persons_driving_horse_drawn)),
            ThemeItem(title = getString(R.string.traffic_regulation)),
            ThemeItem(title = getString(R.string.warning_signals)),
            ThemeItem(title = getString(R.string.starting_a_movement_and_changing_its_direction)),
            ThemeItem(title = getString(R.string.location_of_vehicles_on_the_road)),
            ThemeItem(title = getString(R.string.speed_of_movement)),
            ThemeItem(title = getString(R.string.distance_interval_oncoming_traffic)),
            ThemeItem(title = getString(R.string.overtaking)),
            ThemeItem(title = getString(R.string.stopping_and_parking)),
            ThemeItem(title = getString(R.string.passing_intersections)),
            ThemeItem(title = getString(R.string.advantages_of_route_vehicles)),
            ThemeItem(title = getString(R.string.passing_pedestrian_crossings_and_vehicle_stops)),
            ThemeItem(title = getString(R.string.use_of_external_lighting_devices)),
            ThemeItem(title = getString(R.string.moving_through_railroad_crossings)),
            ThemeItem(title = getString(R.string.passenger_transportation)),
            ThemeItem(title = getString(R.string.cargo_transportation)),
            ThemeItem(title = getString(R.string.towing_and_operation_of_transport_trains)),
            ThemeItem(title = getString(R.string.training_ride)),
            ThemeItem(title = getString(R.string.movement_of_vehicles_in_columns)),
            ThemeItem(title = getString(R.string.traffic_in_residential_and_pedestrian_areas)),
            ThemeItem(title = getString(R.string.driving_on_highways_and_roads_for_cars)),
            ThemeItem(title = getString(R.string.driving_on_mountain_roads_and_steep_descents)),
            ThemeItem(title = getString(R.string.international_movement)),
            ThemeItem(title = getString(R.string.number_plates_identification_marks_inscriptions_and_designations)),
            ThemeItem(title = getString(R.string.technical_condition_of_vehicles_and_their_equipment)),
            ThemeItem(title = getString(R.string.some_issues_of_road_traffic_organization_that_need_to_be_coordinated)),
            ThemeItem(title = getString(R.string.road_signs)),
        )

        dao.insertListThemeItem(list)
    }
}