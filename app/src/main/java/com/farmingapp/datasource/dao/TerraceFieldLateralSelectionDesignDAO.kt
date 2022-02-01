package com.farmingapp.datasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.farmingapp.datasource.entity.TerraceFieldLateralSelectionDesignEntity

@Dao
interface TerraceFieldLateralSelectionDesignDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDetail(model: TerraceFieldLateralSelectionDesignEntity)

    @Query("SELECT * FROM TerraceFieldLateralSelectionDesignEntity LIMIT 1")
    fun getDetail(): TerraceFieldLateralSelectionDesignEntity

    @Query("DELETE FROM TerraceFieldLateralSelectionDesignEntity")
    fun deleteAll()
}