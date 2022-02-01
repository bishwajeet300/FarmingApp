package com.farmingapp.datasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.farmingapp.datasource.entity.FarmerEntity
import com.farmingapp.datasource.entity.TerraceDetailEntity
import com.farmingapp.datasource.entity.TerraceFieldLateralDetailEntity

@Dao
interface TerraceFieldLateralDetailDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDetail(model: TerraceFieldLateralDetailEntity)

    @Query("SELECT * FROM TerraceFieldLateralDetailEntity LIMIT 1")
    fun getDetail(): TerraceFieldLateralDetailEntity

    @Query("DELETE FROM TerraceFieldLateralDetailEntity")
    fun deleteAll()
}