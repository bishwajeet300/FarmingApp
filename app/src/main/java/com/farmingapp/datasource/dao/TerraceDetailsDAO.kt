package com.farmingapp.datasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.farmingapp.datasource.entity.TerraceDetailEntity

@Dao
interface TerraceDetailsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDetail(model: TerraceDetailEntity)

    @Query("SELECT * FROM TerraceDetailEntity LIMIT 1")
    fun getDetail(): TerraceDetailEntity

    @Query("DELETE FROM TerraceDetailEntity")
    fun deleteAll()
}