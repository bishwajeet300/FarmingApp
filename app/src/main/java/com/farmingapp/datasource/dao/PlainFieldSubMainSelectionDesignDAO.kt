package com.farmingapp.datasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.farmingapp.datasource.entity.PlainFieldSubMainSelectionDesignEntity

@Dao
interface PlainFieldSubMainSelectionDesignDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDetail(model: PlainFieldSubMainSelectionDesignEntity)

    @Query("SELECT * FROM PlainFieldSubMainSelectionDesignEntity LIMIT 1")
    fun getDetail(): PlainFieldSubMainSelectionDesignEntity

    @Query("DELETE FROM PlainFieldSubMainSelectionDesignEntity")
    fun deleteAll()
}