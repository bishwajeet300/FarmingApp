package com.farmingapp.datasource.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TerraceDetailEntity(
    @PrimaryKey val id: Long,
    val each_terrace_length: String = "",
    val each_terrace_width: String = "",
    val each_terrace_height: String = ""
)
