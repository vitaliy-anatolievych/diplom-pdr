package com.diplom.diplom_pdr.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "answer")
data class Answer(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,
    val name: String,
    var type: TYPE,
    val taskItemQuestion: String,
) {

    enum class TYPE {
        RIGHT, WRONG, DEFAULT
    }

    override fun toString(): String {
        return name
    }
}