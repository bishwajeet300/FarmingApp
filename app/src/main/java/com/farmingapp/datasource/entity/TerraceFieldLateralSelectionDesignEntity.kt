package com.farmingapp.datasource.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TerraceFieldLateralSelectionDesignEntity(
    @PrimaryKey val id: Long,
    val lateral_diameter: String = "",
    val pipe_material: String = "",
    val lateral_per_terrace: String = "",
    val lateral_length_per_terrace: String = ""
)
