package com.farmingapp.datasource.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SystemWaterSourceDetailEntity(
    @PrimaryKey val id: Long,
    val water_source: String = "",
    val water_tank_location: String = ""
)
