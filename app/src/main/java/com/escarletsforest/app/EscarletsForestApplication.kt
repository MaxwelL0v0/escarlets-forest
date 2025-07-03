package com.escarletsforest.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EscarletsForestApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Inicializaciones adicionales si son necesarias
    }
} 