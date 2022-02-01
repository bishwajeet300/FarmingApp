package com.farmingapp.datasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.farmingapp.datasource.entity.MainLineSelectionDesignEntity

@Dao
interface MainLineSelectionDesignDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDetail(model: MainLineSelectionDesignEntity)

    @Query("SELECT * FROM MainLineSelectionDesignEntity LIMIT 1")
    fun getDetail(): MainLineSelectionDesignEntity

    @Query("DELETE FROM MainLineSelectionDesignEntity")
    fun deleteAll()
}