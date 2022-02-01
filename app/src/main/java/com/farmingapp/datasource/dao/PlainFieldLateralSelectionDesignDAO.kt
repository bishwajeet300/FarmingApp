package com.farmingapp.datasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.farmingapp.datasource.entity.PlainFieldLateralSelectionDesignEntity

@Dao
interface PlainFieldLateralSelectionDesignDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDetail(model: PlainFieldLateralSelectionDesignEntity)

    @Query("SELECT * FROM PlainFieldLateralSelectionDesignEntity LIMIT 1")
    fun getDetail(): PlainFieldLateralSelectionDesignEntity

    @Query("DELETE FROM PlainFieldLateralSelectionDesignEntity")
    fun deleteAll()
}