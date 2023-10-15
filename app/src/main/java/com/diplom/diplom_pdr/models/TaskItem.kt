package com.diplom.diplom_pdr.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.io.Serializable

@Entity(tableName = "task_item")
@Parcelize
data class TaskItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,
    @ColumnInfo(name = "status")
    var status: STATUS = STATUS.CLOSE,
    @ColumnInfo(name = "question")
    val question: String,
    @ColumnInfo(name = "right_answer")
    val rightAnswer: String,
    @ColumnInfo(name = "img_url")
    val imgURL: String,
    val themeItemTitle: String,
    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean = false,
): Parcelable {
    enum class STATUS: Serializable {
        FAIL, RIGHT, SELECTED, CLOSE
    }
}
