package com.farmingapp.datasource.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TerraceFieldSubMainSelectionDesignEntity(
    @PrimaryKey val id: Long,
    val sub_main_diameter: String = "",
    val sub_main_length_per_terrace: String = ""
)
