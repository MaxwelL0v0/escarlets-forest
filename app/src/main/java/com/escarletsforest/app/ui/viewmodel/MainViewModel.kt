package com.escarletsforest.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escarletsforest.app.data.model.Plant
import com.escarletsforest.app.data.model.PlantType
import com.escarletsforest.app.data.repository.PlantRepository
import com.escarletsforest.app.data.repository.GlobalStatistics
import com.escarletsforest.app.data.repository.UserStatistics
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val plantRepository: PlantRepository
) : ViewModel() {
    
    // Estados de la UI
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    private val _plants = MutableStateFlow<List<Plant>>(emptyList())
    val plants: StateFlow<List<Plant>> = _plants.asStateFlow()
    
    private val _userPlants = MutableStateFlow<List<Plant>>(emptyList())
    val userPlants: StateFlow<List<Plant>> = _userPlants.asStateFlow()
    
    private val _globalStatistics = MutableStateFlow<GlobalStatistics?>(null)
    val globalStatistics: StateFlow<GlobalStatistics?> = _globalStatistics.asStateFlow()
    
    private val _userStatistics = MutableStateFlow<UserStatistics?>(null)
    val userStatistics: StateFlow<UserStatistics?> = _userStatistics.asStateFlow()
    
    private val _selectedPlant = MutableStateFlow<Plant?>(null)
    val selectedPlant: StateFlow<Plant?> = _selectedPlant.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _filteredPlants = MutableStateFlow<List<Plant>>(emptyList())
    val filteredPlants: StateFlow<List<Plant>> = _filteredPlants.asStateFlow()
    
    // Variables de estado
    private var currentUserId: String? = null
    private var currentPlantTypeFilter: PlantType? = null
    
    init {
        loadInitialData()
    }
    
    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                
                // Cargar estadísticas globales
                val globalStats = plantRepository.getGlobalStatistics()
                _globalStatistics.value = globalStats
                
                // Cargar todas las plantas
                plantRepository.getAllPlants().collect { plants ->
                    _plants.value = plants
                    applyFilters()
                }
                
                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Error desconocido")
            }
        }
    }
    
    fun setCurrentUser(userId: String) {
        currentUserId = userId
        loadUserData(userId)
    }
    
    private fun loadUserData(userId: String) {
        viewModelScope.launch {
            try {
                // Cargar plantas del usuario
                plantRepository.getPlantsByUserId(userId).collect { userPlants ->
                    _userPlants.value = userPlants
                }
                
                // Cargar estadísticas del usuario
                val userStats = plantRepository.getUserStatistics(userId)
                _userStatistics.value = userStats
                
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Error al cargar datos del usuario")
            }
        }
    }
    
    fun addPlant(plant: Plant) {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                
                plantRepository.addPlantWithCalculations(plant)
                
                // Actualizar estadísticas
                currentUserId?.let { userId ->
                    val userStats = plantRepository.getUserStatistics(userId)
                    _userStatistics.value = userStats
                }
                
                val globalStats = plantRepository.getGlobalStatistics()
                _globalStatistics.value = globalStats
                
                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Error al agregar planta")
            }
        }
    }
    
    fun updatePlant(plant: Plant) {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                
                plantRepository.updatePlantWithCalculations(plant)
                
                // Actualizar estadísticas
                currentUserId?.let { userId ->
                    val userStats = plantRepository.getUserStatistics(userId)
                    _userStatistics.value = userStats
                }
                
                val globalStats = plantRepository.getGlobalStatistics()
                _globalStatistics.value = globalStats
                
                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Error al actualizar planta")
            }
        }
    }
    
    fun deletePlant(plant: Plant) {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                
                plantRepository.deletePlant(plant)
                
                // Actualizar estadísticas
                currentUserId?.let { userId ->
                    val userStats = plantRepository.getUserStatistics(userId)
                    _userStatistics.value = userStats
                }
                
                val globalStats = plantRepository.getGlobalStatistics()
                _globalStatistics.value = globalStats
                
                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Error al eliminar planta")
            }
        }
    }
    
    fun selectPlant(plant: Plant) {
        _selectedPlant.value = plant
    }
    
    fun clearSelectedPlant() {
        _selectedPlant.value = null
    }
    
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        applyFilters()
    }
    
    fun setPlantTypeFilter(plantType: PlantType?) {
        currentPlantTypeFilter = plantType
        applyFilters()
    }
    
    private fun applyFilters() {
        viewModelScope.launch {
            var filteredList = _plants.value
            
            // Aplicar filtro de tipo de planta
            currentPlantTypeFilter?.let { plantType ->
                filteredList = filteredList.filter { it.plantType == plantType }
            }
            
            // Aplicar filtro de búsqueda
            val query = _searchQuery.value
            if (query.isNotEmpty()) {
                filteredList = filteredList.filter { plant ->
                    plant.name.contains(query, ignoreCase = true) ||
                    plant.species.contains(query, ignoreCase = true) ||
                    plant.city?.contains(query, ignoreCase = true) == true ||
                    plant.country?.contains(query, ignoreCase = true) == true
                }
            }
            
            _filteredPlants.value = filteredList
        }
    }
    
    fun refreshData() {
        loadInitialData()
        currentUserId?.let { loadUserData(it) }
    }
    
    fun getPlantsByLocation(latLng: LatLng, radiusKm: Double = 10.0): List<Plant> {
        return _plants.value.filter { plant ->
            val distance = calculateDistance(latLng, plant.location)
            distance <= radiusKm
        }
    }
    
    private fun calculateDistance(point1: LatLng, point2: LatLng): Double {
        val earthRadius = 6371.0 // Radio de la Tierra en kilómetros
        
        val lat1 = Math.toRadians(point1.latitude)
        val lat2 = Math.toRadians(point2.latitude)
        val deltaLat = Math.toRadians(point2.latitude - point1.latitude)
        val deltaLon = Math.toRadians(point2.longitude - point1.longitude)
        
        val a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2)
        
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        
        return earthRadius * c
    }
    
    fun createSamplePlant(
        name: String,
        species: String,
        location: LatLng,
        plantType: PlantType = PlantType.TREE,
        userId: String = "sample_user"
    ): Plant {
        return Plant(
            name = name,
            species = species,
            location = location,
            plantedDate = Date(),
            plantType = plantType,
            userId = userId,
            userName = "Usuario Ejemplo"
        )
    }
}

sealed class UiState {
    object Loading : UiState()
    object Success : UiState()
    data class Error(val message: String) : UiState()
} 