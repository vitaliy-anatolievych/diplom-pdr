package com.diplom.diplom_pdr.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drive_stats")
data class DriveStatsModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "distance")
    val distance: Double,
    @ColumnInfo(name = "average_speed")
    val averageSpeed: Int,
    @ColumnInfo(name = "count_of_excessive_speed")
    val countOfExcessiveSpeed: Int,
    @ColumnInfo(name = "count_emergency_down")
    val countOfEmergencyDown: Int,
    @ColumnInfo(name = "count_excessive_over20")
    val countExcessiveOver20: Int,

)
