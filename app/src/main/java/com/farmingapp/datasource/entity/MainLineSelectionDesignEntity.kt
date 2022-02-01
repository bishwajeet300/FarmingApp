package com.farmingapp.datasource.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MainLineSelectionDesignEntity(
    @PrimaryKey val id: Long,
    val mainline_diameter: String = "",
    val mainline_length: String = "",
    val outlet_factor: String = "",
    val calculated_head_loss: String = "",
    val mainline_total_discharge: String = ""
)
