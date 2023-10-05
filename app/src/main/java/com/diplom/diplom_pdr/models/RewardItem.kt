package com.diplom.diplom_pdr.models

import org.intellij.lang.annotations.Identifier

data class RewardItem(
    val id: Int,
    @Identifier val idImage: Int,
    val description: String
)
