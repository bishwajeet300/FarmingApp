package com.farmingapp.datasource.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CropSelectionWaterCalculationEntity(
    @PrimaryKey val id: Long,
    val crop_name: String = "",
    val soil_type: String = "",
    val plant_distance: String = "",
    val row_distance: String = "",
    val plant_shadow_area: String = "",
    val e_pan: String = "",
    val wetted_area: String = ""
)
