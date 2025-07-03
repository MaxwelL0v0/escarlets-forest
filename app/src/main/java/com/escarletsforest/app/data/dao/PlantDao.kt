package com.escarletsforest.app.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.escarletsforest.app.data.model.Plant
import com.escarletsforest.app.data.model.PlantType
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {
    
    // Operaciones básicas CRUD
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plant: Plant): Long
    
    @Update
    suspend fun updatePlant(plant: Plant)
    
    @Delete
    suspend fun deletePlant(plant: Plant)
    
    @Query("DELETE FROM plants WHERE id = :plantId")
    suspend fun deletePlantById(plantId: Long)
    
    // Consultas de lectura
    @Query("SELECT * FROM plants WHERE id = :plantId")
    suspend fun getPlantById(plantId: Long): Plant?
    
    @Query("SELECT * FROM plants WHERE id = :plantId")
    fun getPlantByIdLiveData(plantId: Long): LiveData<Plant?>
    
    @Query("SELECT * FROM plants ORDER BY createdAt DESC")
    fun getAllPlants(): Flow<List<Plant>>
    
    @Query("SELECT * FROM plants WHERE userId = :userId ORDER BY createdAt DESC")
    fun getPlantsByUserId(userId: String): Flow<List<Plant>>
    
    @Query("SELECT * FROM plants WHERE userId = :userId ORDER BY createdAt DESC")
    fun getPlantsByUserIdLiveData(userId: String): LiveData<List<Plant>>
    
    // Consultas por tipo de planta
    @Query("SELECT * FROM plants WHERE plantType = :plantType ORDER BY createdAt DESC")
    fun getPlantsByType(plantType: PlantType): Flow<List<Plant>>
    
    @Query("SELECT * FROM plants WHERE plantType = :plantType AND userId = :userId ORDER BY createdAt DESC")
    fun getPlantsByTypeAndUser(plantType: PlantType, userId: String): Flow<List<Plant>>
    
    // Consultas por ubicación
    @Query("SELECT * FROM plants WHERE country = :country ORDER BY createdAt DESC")
    fun getPlantsByCountry(country: String): Flow<List<Plant>>
    
    @Query("SELECT * FROM plants WHERE city = :city ORDER BY createdAt DESC")
    fun getPlantsByCity(city: String): Flow<List<Plant>>
    
    // Consultas de estadísticas
    @Query("SELECT COUNT(*) FROM plants WHERE userId = :userId")
    fun getPlantCountByUser(userId: String): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM plants WHERE plantType = :plantType AND userId = :userId")
    fun getPlantCountByTypeAndUser(plantType: PlantType, userId: String): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM plants WHERE plantType = :plantType")
    fun getPlantCountByType(plantType: PlantType): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM plants")
    fun getTotalPlantCount(): Flow<Int>
    
    // Consultas de impacto ambiental
    @Query("SELECT SUM(co2Absorbed) FROM plants WHERE userId = :userId")
    fun getTotalCO2AbsorbedByUser(userId: String): Flow<Float?>
    
    @Query("SELECT SUM(oxygenProduced) FROM plants WHERE userId = :userId")
    fun getTotalOxygenProducedByUser(userId: String): Flow<Float?>
    
    @Query("SELECT SUM(co2Absorbed) FROM plants")
    fun getTotalCO2Absorbed(): Flow<Float?>
    
    @Query("SELECT SUM(oxygenProduced) FROM plants")
    fun getTotalOxygenProduced(): Flow<Float?>
    
    // Consultas de especies
    @Query("SELECT DISTINCT species FROM plants ORDER BY species ASC")
    fun getAllSpecies(): Flow<List<String>>
    
    @Query("SELECT DISTINCT species FROM plants WHERE userId = :userId ORDER BY species ASC")
    fun getSpeciesByUser(userId: String): Flow<List<String>>
    
    // Consultas de plantas protegidas
    @Query("SELECT * FROM plants WHERE isProtected = 1 ORDER BY createdAt DESC")
    fun getProtectedPlants(): Flow<List<Plant>>
    
    @Query("SELECT * FROM plants WHERE isProtected = 1 AND userId = :userId ORDER BY createdAt DESC")
    fun getProtectedPlantsByUser(userId: String): Flow<List<Plant>>
    
    // Consultas de plantas verificadas
    @Query("SELECT * FROM plants WHERE isVerified = 1 ORDER BY createdAt DESC")
    fun getVerifiedPlants(): Flow<List<Plant>>
    
    // Consultas de búsqueda
    @Query("SELECT * FROM plants WHERE name LIKE '%' || :searchQuery || '%' OR species LIKE '%' || :searchQuery || '%' ORDER BY createdAt DESC")
    fun searchPlants(searchQuery: String): Flow<List<Plant>>
    
    @Query("SELECT * FROM plants WHERE name LIKE '%' || :searchQuery || '%' OR species LIKE '%' || :searchQuery || '%' AND userId = :userId ORDER BY createdAt DESC")
    fun searchPlantsByUser(searchQuery: String, userId: String): Flow<List<Plant>>
    
    // Consultas de plantas recientes
    @Query("SELECT * FROM plants ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentPlants(limit: Int = 10): Flow<List<Plant>>
    
    @Query("SELECT * FROM plants WHERE userId = :userId ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentPlantsByUser(userId: String, limit: Int = 10): Flow<List<Plant>>
    
    // Consultas de plantas por fecha
    @Query("SELECT * FROM plants WHERE plantedDate >= :startDate AND plantedDate <= :endDate ORDER BY plantedDate DESC")
    fun getPlantsByDateRange(startDate: Long, endDate: Long): Flow<List<Plant>>
    
    @Query("SELECT * FROM plants WHERE plantedDate >= :startDate AND plantedDate <= :endDate AND userId = :userId ORDER BY plantedDate DESC")
    fun getPlantsByDateRangeAndUser(startDate: Long, endDate: Long, userId: String): Flow<List<Plant>>
} 