package com.diplom.diplom_pdr.models

data class ThemeItem(
    val id: Int,
    val title: String,
    val allQuestion: Int,
    val rightAnswers: Int = 0,
    var isStarted: Boolean = false
)
