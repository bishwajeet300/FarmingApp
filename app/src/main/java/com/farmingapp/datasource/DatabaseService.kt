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
               ],
    version = 1,
    exportSchema = false
)
abstract class DatabaseService : RoomDatabase() {

    abstract fun farmerDetailDAO(): FarmerDetailDAO

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