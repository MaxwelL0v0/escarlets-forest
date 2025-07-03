package com.escarletsforest.app.data.converter

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng

class LocationConverter {
    @TypeConverter
    fun fromLatLng(latLng: LatLng?): String? {
        return latLng?.let { "${it.latitude},${it.longitude}" }
    }

    @TypeConverter
    fun toLatLng(latLngString: String?): LatLng? {
        return latLngString?.let {
            val parts = it.split(",")
            if (parts.size == 2) {
                LatLng(parts[0].toDouble(), parts[1].toDouble())
            } else {
                null
            }
        }
    }
} 