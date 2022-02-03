package com.farmingapp.datasource.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FarmerEntity(
    @PrimaryKey val id: Long,
    val full_name: String = "",
    val address: String = "",
    val phone: String = "",
    val email: String = "",
    val field: String = ""
)
