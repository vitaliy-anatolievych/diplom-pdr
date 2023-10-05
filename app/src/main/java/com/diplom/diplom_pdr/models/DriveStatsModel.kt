package com.diplom.diplom_pdr.models

data class DriveStatsModel(
    val id: Int,
    val date: String,
    val distance: Int,
    val averageSpeed: Int,
    val countOfExcessiveSpeed: Int,
    val countOfEmergencyDown: Int
)
