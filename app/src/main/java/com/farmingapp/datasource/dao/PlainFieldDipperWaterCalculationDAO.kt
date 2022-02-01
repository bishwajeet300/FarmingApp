package com.farmingapp.datasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.farmingapp.datasource.entity.PlainFieldDipperWaterCalculationEntity

@Dao
interface PlainFieldDipperWaterCalculationDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDetail(model: PlainFieldDipperWaterCalculationEntity)

    @Query("SELECT * FROM PlainFieldDipperWaterCalculationEntity LIMIT 1")
    fun getDetail(): PlainFieldDipperWaterCalculationEntity

    @Query("DELETE FROM PlainFieldDipperWaterCalculationEntity")
    fun deleteAll()
}