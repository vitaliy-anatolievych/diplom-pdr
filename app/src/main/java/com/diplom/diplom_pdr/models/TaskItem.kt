package com.diplom.diplom_pdr.models

data class TaskItem(
    val id: Int,
    val status: STATUS
) {
    enum class STATUS {
        FAIL, RIGHT, SELECTED, CLOSE
    }
}
