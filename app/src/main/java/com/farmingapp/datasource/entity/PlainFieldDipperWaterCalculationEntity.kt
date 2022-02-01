package com.farmingapp.datasource.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlainFieldDipperWaterCalculationEntity(
    @PrimaryKey val id: Long,
    val dripper: String = "",
    val dripper_spacing: String = "",
    val lateral_spacing: String = "",
    val dripper_per_plant: String = "",
    val field_length: String = "",
    val field_width: String = ""
)
