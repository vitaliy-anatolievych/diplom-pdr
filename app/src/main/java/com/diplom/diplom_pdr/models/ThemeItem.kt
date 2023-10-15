package com.diplom.diplom_pdr.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "theme_item")
data class ThemeItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "right_answers")
    val rightAnswers: Int = 0,
    @ColumnInfo(name = "wrong_answers")
    val wrongAnswers: Int = 0,
    @ColumnInfo(name = "is_started")
    var isStarted: Boolean = false,
    @ColumnInfo(name = "is_test_passed")
    val isTestPassed: Boolean = false,
    @ColumnInfo(name = "test_time")
    val totalTestTime: Long = 0,
)
