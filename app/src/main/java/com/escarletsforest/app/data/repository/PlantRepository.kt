package com.escarletsforest.app.data.repository

import com.escarletsforest.app.data.dao.PlantDao
import com.escarletsforest.app.data.model.Plant
import com.escarletsforest.app.data.model.PlantType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlantRepository @Inject constructor(
    private val plantDao: PlantDao
) {
    
    // Operaciones básicas CRUD
    suspend fun insertPlant(plant: Plant): Long {
        return plantDao.insertPlant(plant)
    }
    
    suspend fun updatePlant(plant: Plant) {
        plantDao.updatePlant(plant)
    }
    
    suspend fun deletePlant(plant: Plant) {
        plantDao.deletePlant(plant)
    }
    
    suspend fun deletePlantById(plantId: Long) {
        plantDao.deletePlantById(plantId)
    }
    
    // Consultas de lectura
    suspend fun getPlantById(plantId: Long): Plant? {
        return plantDao.getPlantById(plantId)
    }
    
    fun getPlantByIdLiveData(plantId: Long) = plantDao.getPlantByIdLiveData(plantId)
    
    fun getAllPlants(): Flow<List<Plant>> = plantDao.getAllPlants()
    
    fun getPlantsByUserId(userId: String): Flow<List<Plant>> = plantDao.getPlantsByUserId(userId)
    
    fun getPlantsByUserIdLiveData(userId: String) = plantDao.getPlantsByUserIdLiveData(userId)
    
    // Consultas por tipo de planta
    fun getPlantsByType(plantType: PlantType): Flow<List<Plant>> = plantDao.getPlantsByType(plantType)
    
    fun getPlantsByTypeAndUser(plantType: PlantType, userId: String): Flow<List<Plant>> = 
        plantDao.getPlantsByTypeAndUser(plantType, userId)
    
    // Consultas por ubicación
    fun getPlantsByCountry(country: String): Flow<List<Plant>> = plantDao.getPlantsByCountry(country)
    
    fun getPlantsByCity(city: String): Flow<List<Plant>> = plantDao.getPlantsByCity(city)
    
    // Consultas de estadísticas
    fun getPlantCountByUser(userId: String): Flow<Int> = plantDao.getPlantCountByUser(userId)
    
    fun getPlantCountByTypeAndUser(plantType: PlantType, userId: String): Flow<Int> = 
        plantDao.getPlantCountByTypeAndUser(plantType, userId)
    
    fun getPlantCountByType(plantType: PlantType): Flow<Int> = plantDao.getPlantCountByType(plantType)
    
    fun getTotalPlantCount(): Flow<Int> = plantDao.getTotalPlantCount()
    
    // Consultas de impacto ambiental
    fun getTotalCO2AbsorbedByUser(userId: String): Flow<Float?> = plantDao.getTotalCO2AbsorbedByUser(userId)
    
    fun getTotalOxygenProducedByUser(userId: String): Flow<Float?> = plantDao.getTotalOxygenProducedByUser(userId)
    
    fun getTotalCO2Absorbed(): Flow<Float?> = plantDao.getTotalCO2Absorbed()
    
    fun getTotalOxygenProduced(): Flow<Float?> = plantDao.getTotalOxygenProduced()
    
    // Consultas de especies
    fun getAllSpecies(): Flow<List<String>> = plantDao.getAllSpecies()
    
    fun getSpeciesByUser(userId: String): Flow<List<String>> = plantDao.getSpeciesByUser(userId)
    
    // Consultas de plantas protegidas
    fun getProtectedPlants(): Flow<List<Plant>> = plantDao.getProtectedPlants()
    
    fun getProtectedPlantsByUser(userId: String): Flow<List<Plant>> = plantDao.getProtectedPlantsByUser(userId)
    
    // Consultas de plantas verificadas
    fun getVerifiedPlants(): Flow<List<Plant>> = plantDao.getVerifiedPlants()
    
    // Consultas de búsqueda
    fun searchPlants(searchQuery: String): Flow<List<Plant>> = plantDao.searchPlants(searchQuery)
    
    fun searchPlantsByUser(searchQuery: String, userId: String): Flow<List<Plant>> = 
        plantDao.searchPlantsByUser(searchQuery, userId)
    
    // Consultas de plantas recientes
    fun getRecentPlants(limit: Int = 10): Flow<List<Plant>> = plantDao.getRecentPlants(limit)
    
    fun getRecentPlantsByUser(userId: String, limit: Int = 10): Flow<List<Plant>> = 
        plantDao.getRecentPlantsByUser(userId, limit)
    
    // Consultas de plantas por fecha
    fun getPlantsByDateRange(startDate: Long, endDate: Long): Flow<List<Plant>> = 
        plantDao.getPlantsByDateRange(startDate, endDate)
    
    fun getPlantsByDateRangeAndUser(startDate: Long, endDate: Long, userId: String): Flow<List<Plant>> = 
        plantDao.getPlantsByDateRangeAndUser(startDate, endDate, userId)
    
    // Métodos de utilidad
    suspend fun addPlantWithCalculations(plant: Plant): Long {
        // Calcular CO2 absorbido y oxígeno producido antes de guardar
        val plantWithCalculations = plant.copy(
            co2Absorbed = plant.getEstimatedCO2Absorbed(),
            oxygenProduced = plant.getEstimatedOxygenProduced()
        )
        return insertPlant(plantWithCalculations)
    }
    
    suspend fun updatePlantWithCalculations(plant: Plant) {
        val plantWithCalculations = plant.copy(
            co2Absorbed = plant.getEstimatedCO2Absorbed(),
            oxygenProduced = plant.getEstimatedOxygenProduced(),
            updatedAt = java.util.Date()
        )
        updatePlant(plantWithCalculations)
    }
    
    // Métodos para estadísticas globales
    suspend fun getGlobalStatistics(): GlobalStatistics {
        val totalPlants = getTotalPlantCount().first()
        val totalCO2 = getTotalCO2Absorbed().first() ?: 0f
        val totalOxygen = getTotalOxygenProduced().first() ?: 0f
        val allSpecies = getAllSpecies().first()
        
        return GlobalStatistics(
            totalPlants = totalPlants,
            totalCO2Absorbed = totalCO2,
            totalOxygenProduced = totalOxygen,
            uniqueSpecies = allSpecies.size
        )
    }
    
    suspend fun getUserStatistics(userId: String): UserStatistics {
        val userPlants = getPlantCountByUser(userId).first()
        val userCO2 = getTotalCO2AbsorbedByUser(userId).first() ?: 0f
        val userOxygen = getTotalOxygenProducedByUser(userId).first() ?: 0f
        val userSpecies = getSpeciesByUser(userId).first()
        
        return UserStatistics(
            totalPlants = userPlants,
            totalCO2Absorbed = userCO2,
            totalOxygenProduced = userOxygen,
            uniqueSpecies = userSpecies.size
        )
    }
}

data class GlobalStatistics(
    val totalPlants: Int,
    val totalCO2Absorbed: Float,
    val totalOxygenProduced: Float,
    val uniqueSpecies: Int
)

data class UserStatistics(
    val totalPlants: Int,
    val totalCO2Absorbed: Float,
    val totalOxygenProduced: Float,
    val uniqueSpecies: Int
) 