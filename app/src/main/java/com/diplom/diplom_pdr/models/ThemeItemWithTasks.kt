package com.diplom.diplom_pdr.models

import androidx.room.Embedded
import androidx.room.Relation

data class ThemeItemWithTasks(
    @Embedded val themeItem: ThemeItem,
    @Relation(
        entity = TaskItem::class,
        parentColumn = "title",
        entityColumn = "themeItemTitle",
    )
    val taskItem: List<TaskItem>
)
