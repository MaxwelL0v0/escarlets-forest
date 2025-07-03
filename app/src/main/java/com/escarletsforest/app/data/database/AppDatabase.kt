package com.escarletsforest.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.escarletsforest.app.data.converter.DateConverter
import com.escarletsforest.app.data.converter.LocationConverter
import com.escarletsforest.app.data.dao.PlantDao
import com.escarletsforest.app.data.model.Plant

@Database(
    entities = [Plant::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class, LocationConverter::class)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun plantDao(): PlantDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "escarlets_forest_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 