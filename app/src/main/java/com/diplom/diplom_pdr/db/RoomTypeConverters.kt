package com.diplom.diplom_pdr.db

import androidx.room.TypeConverter
import com.diplom.diplom_pdr.models.Answer
import com.diplom.diplom_pdr.models.TaskItem

class RoomTypeConverters {

    @TypeConverter
    fun fromListStringToString(list: List<String>): String = list.toString()

    @TypeConverter
    fun toListStringFromString(str: String): List<String> {
        val result = ArrayList<String>()
        val split = str.replace("[", "")
            .replace("]", "")
            .split("|,")

        for (n in split.indices) {
            if (n < split.size - 1) {
                result.add(split[n])
            } else {
                result.add(split[n].replace("|", ""))
            }
        }

        return result
    }

    @TypeConverter
    fun fromTaskStatusToString(status: TaskItem.STATUS): String = status.name

    @TypeConverter
    fun fromTaskStatusToString(status: Answer.TYPE): String = status.name

    @TypeConverter
    fun fromStringToTaskAnswer(string: String): Answer.TYPE {
        // RIGHT, WRONG, DEFAULT
        return when(string) {
            "RIGHT" -> Answer.TYPE.RIGHT
            "WRONG" -> Answer.TYPE.WRONG
            "DEFAULT" -> Answer.TYPE.DEFAULT
            else -> Answer.TYPE.DEFAULT
        }
    }

    @TypeConverter
    fun fromStringToTaskStatus(str: String): TaskItem.STATUS {
        // FAIL, RIGHT, SELECTED, CLOSE
        return when (str) {
            "FAIL" -> TaskItem.STATUS.FAIL
            "RIGHT" -> TaskItem.STATUS.RIGHT
            "SELECTED" -> TaskItem.STATUS.SELECTED
            "CLOSE" -> TaskItem.STATUS.CLOSE
            else -> TaskItem.STATUS.CLOSE
        }
    }
}