package com.diplom.diplom_pdr.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tests_result")
data class TestsResultEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "all_tests_passed")
    var allTestsPassed: Int,
    @ColumnInfo(name = "total_right_answers")
    var totalRightAnswers: Int,
    @ColumnInfo(name = "total_wrong_answers")
    var totalWrongAnswers: Int,
    @ColumnInfo(name = "percent_right_answers")
    var percentRightAnswers: Int,
    @ColumnInfo(name = "total_time")
    var totalTime: Long,
)
