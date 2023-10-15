package com.diplom.diplom_pdr.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "test_rating")
    var testRating: Int,
    @ColumnInfo(name = "drive_rating")
    var driveRating: Int,
)
