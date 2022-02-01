package com.farmingapp.datasource.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlainFieldSubMainSelectionDesignEntity(
    @PrimaryKey val id: Long,
    val sub_main_diameter: String = "",
    val sub_main_length: String = ""
)
