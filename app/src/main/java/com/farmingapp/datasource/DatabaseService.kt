package com.farmingapp.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.farmingapp.datasource.dao.*
import com.farmingapp.datasource.entity.*

@Database(
    entities = [
        FarmerEntity::class,
        CropSelectionWaterCalculationEntity::class,
        TerraceDetailEntity::class,
        TerraceFieldLateralDetailEntity::class,
        TerraceFieldLateralSelectionDesignEntity::class,
        TerraceFieldSubMainSelectionDesignEntity::class,
        PlainFieldDipperWaterCalculationEntity::class,
        PlainFieldLateralSelectionDesignEntity::class,
        PlainFieldSubMainSelectionDesignEntity::class,
        MainLineSelectionDesignEntity::class,
        SystemWaterSourceDetailEntity::class
               ],
    version = 1,
    exportSchema = false
)
abstract class DatabaseService : RoomDatabase() {

    abstract fun farmerDetailDAO(): FarmerDetailDAO
    abstract fun cropSelectionWaterCalculationDAO(): CropSelectionWaterCalculationDAO
    abstract fun terraceDetailsDAO(): TerraceDetailsDAO
    abstract fun terraceFieldLateralDetailDAO(): TerraceFieldLateralDetailDAO
    abstract fun terraceFieldLateralSelectionDesignDAO(): TerraceFieldLateralSelectionDesignDAO
    abstract fun terraceFieldSubMainSelectionDesignDAO(): TerraceFieldSubMainSelectionDesignDAO
    abstract fun plainFieldDipperWaterCalculationDAO(): PlainFieldDipperWaterCalculationDAO
    abstract fun plainFieldLateralSelectionDesignDAO(): PlainFieldLateralSelectionDesignDAO
    abstract fun plainFieldSubMainSelectionDesignDAO(): PlainFieldSubMainSelectionDesignDAO
    abstract fun mainlineSelectionDesignDAO(): MainLineSelectionDesignDAO
    abstract fun systemWaterSourceDetailsDAO(): SystemWaterSourceDetailsDAO


    companion object {
        @Volatile
        private var INSTANCE: DatabaseService? = null

        fun getDatabase(context: Context): DatabaseService {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseService::class.java,
                    "FarmerDB"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}