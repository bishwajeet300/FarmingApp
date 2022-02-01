package com.farmingapp.datasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.farmingapp.datasource.entity.SystemWaterSourceDetailEntity

@Dao
interface SystemWaterSourceDetailsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDetail(model: SystemWaterSourceDetailEntity)

    @Query("SELECT * FROM SystemWaterSourceDetailEntity LIMIT 1")
    fun getDetail(): SystemWaterSourceDetailEntity

    @Query("DELETE FROM SystemWaterSourceDetailEntity")
    fun deleteAll()
}