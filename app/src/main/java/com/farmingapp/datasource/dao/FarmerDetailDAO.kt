package com.farmingapp.datasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.farmingapp.datasource.entity.FarmerEntity

@Dao
interface FarmerDetailDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFarmer(consumer: FarmerEntity)

    @Query("SELECT * FROM FarmerEntity LIMIT 1")
    fun getFarmer(): FarmerEntity

    @Query("DELETE FROM FarmerEntity")
    fun deleteAll()
}