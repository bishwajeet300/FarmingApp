package com.farmingapp.datasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.farmingapp.datasource.entity.TerraceFieldSubMainSelectionDesignEntity

@Dao
interface TerraceFieldSubMainSelectionDesignDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDetail(model: TerraceFieldSubMainSelectionDesignEntity)

    @Query("SELECT * FROM TerraceFieldSubMainSelectionDesignEntity LIMIT 1")
    fun getDetail(): TerraceFieldSubMainSelectionDesignEntity

    @Query("DELETE FROM TerraceFieldSubMainSelectionDesignEntity")
    fun deleteAll()
}