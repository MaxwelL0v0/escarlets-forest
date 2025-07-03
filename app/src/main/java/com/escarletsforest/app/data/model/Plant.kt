package com.escarletsforest.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.escarletsforest.app.data.converter.DateConverter
import com.escarletsforest.app.data.converter.LocationConverter
import com.google.android.gms.maps.model.LatLng
import java.util.Date

@Entity(tableName = "plants")
@TypeConverters(DateConverter::class, LocationConverter::class)
data class Plant(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    // Información básica
    val name: String,
    val species: String,
    val scientificName: String? = null,
    
    // Ubicación
    val location: LatLng,
    val address: String? = null,
    val country: String? = null,
    val city: String? = null,
    
    // Fechas
    val plantedDate: Date,
    val lastWateredDate: Date? = null,
    val lastFertilizedDate: Date? = null,
    
    // Características
    val plantType: PlantType = PlantType.TREE,
    val height: Float? = null, // en metros
    val diameter: Float? = null, // en centímetros
    val age: Int? = null, // en años
    
    // Fotos
    val photoUrl: String? = null,
    val thumbnailUrl: String? = null,
    
    // Notas y descripción
    val notes: String? = null,
    val description: String? = null,
    
    // Estado de salud
    val healthStatus: HealthStatus = HealthStatus.HEALTHY,
    val isProtected: Boolean = false,
    
    // Información del usuario
    val userId: String,
    val userName: String? = null,
    
    // Impacto ambiental (calculado)
    val co2Absorbed: Float = 0f, // en kg
    val oxygenProduced: Float = 0f, // en kg
    
    // Metadatos
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val isVerified: Boolean = false,
    val verifiedBy: String? = null,
    val verificationDate: Date? = null
) {
    
    // Métodos de utilidad
    fun getAgeInDays(): Int {
        val currentDate = Date()
        val diffInMillis = currentDate.time - plantedDate.time
        return (diffInMillis / (24 * 60 * 60 * 1000)).toInt()
    }
    
    fun getAgeInMonths(): Int {
        return getAgeInDays() / 30
    }
    
    fun getAgeInYears(): Int {
        return getAgeInDays() / 365
    }
    
    fun getEstimatedCO2Absorbed(): Float {
        // Estimación basada en el tipo de planta y edad
        val baseCO2PerYear = when (plantType) {
            PlantType.TREE -> 22.0f // kg CO2 por año para un árbol promedio
            PlantType.SHRUB -> 5.0f
            PlantType.INDOOR_PLANT -> 0.5f
            PlantType.HERB -> 0.1f
            else -> 1.0f
        }
        
        val years = getAgeInYears().toFloat()
        return baseCO2PerYear * years
    }
    
    fun getEstimatedOxygenProduced(): Float {
        // Aproximadamente 6 moléculas de CO2 producen 6 moléculas de O2
        return getEstimatedCO2Absorbed() * 0.73f // Factor de conversión
    }
}

enum class PlantType {
    TREE,           // Árbol
    SHRUB,          // Arbusto
    HERB,           // Hierba
    INDOOR_PLANT,   // Planta de interior
    FLOWER,         // Flor
    CACTUS,         // Cactus
    SUCCULENT,      // Suculenta
    FERN,           // Helecho
    VINE,           // Enredadera
    OTHER           // Otro
}

enum class HealthStatus {
    HEALTHY,        // Saludable
    GOOD,           // Bueno
    FAIR,           // Regular
    POOR,           // Malo
    SICK,           // Enfermo
    DEAD            // Muerto
}

 