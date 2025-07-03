package com.escarletsforest.app.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.escarletsforest.app.R
import com.escarletsforest.app.databinding.ActivityMainBinding
import com.escarletsforest.app.ui.viewmodel.MainViewModel
import com.escarletsforest.app.ui.viewmodel.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupNavigation()
        setupObservers()
        
        // Configurar usuario de ejemplo para demostración
        viewModel.setCurrentUser("demo_user_123")
    }
    
    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        
        // Configurar la navegación inferior
        binding.bottomNavigation.setupWithNavController(navController)
        
        // Configurar el FAB para agregar plantas
        binding.fabAddPlant.setOnClickListener {
            // Navegar al fragmento de agregar planta
            navController.navigate(R.id.action_global_addPlantFragment)
        }
    }
    
    private fun setupObservers() {
        // Observar el estado de la UI
        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> {
                        // Mostrar indicador de carga si es necesario
                    }
                    is UiState.Success -> {
                        // Ocultar indicador de carga
                    }
                    is UiState.Error -> {
                        // Mostrar mensaje de error
                        showError(state.message)
                    }
                }
            }
        }
    }
    
    private fun showError(message: String) {
        // Implementar mostrar error (Toast, Snackbar, etc.)
        // Por ahora solo imprimimos en el log
        println("Error: $message")
    }
    
    override fun onResume() {
        super.onResume()
        // Refrescar datos cuando la actividad se reanuda
        viewModel.refreshData()
    }
} 