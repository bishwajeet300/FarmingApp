package com.farmingapp.datasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.farmingapp.datasource.entity.CropSelectionWaterCalculationEntity

@Dao
interface CropSelectionWaterCalculationDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDetail(model: CropSelectionWaterCalculationEntity)

    @Query("SELECT * FROM CropSelectionWaterCalculationEntity LIMIT 1")
    fun getDetail(): CropSelectionWaterCalculationEntity

    @Query("DELETE FROM CropSelectionWaterCalculationEntity")
    fun deleteAll()
}