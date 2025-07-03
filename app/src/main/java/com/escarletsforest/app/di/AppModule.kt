package com.escarletsforest.app.di

import android.content.Context
import com.escarletsforest.app.data.database.AppDatabase
import com.escarletsforest.app.data.dao.PlantDao
import com.escarletsforest.app.data.repository.PlantRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }
    
    @Provides
    @Singleton
    fun providePlantDao(database: AppDatabase): PlantDao {
        return database.plantDao()
    }
    
    @Provides
    @Singleton
    fun providePlantRepository(plantDao: PlantDao): PlantRepository {
        return PlantRepository(plantDao)
    }
} 