package com.diplom.diplom_pdr.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,
    @ColumnInfo(name = "rating")
    var rating: Int,
    @ColumnInfo(name = "current_interval")
    var currentInterval: Int = 0,
    @ColumnInfo(name = "enter_date")
    var enterDate: Long? = null
)
