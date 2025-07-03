package com.escarletsforest.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.escarletsforest.app.R
import com.escarletsforest.app.databinding.FragmentHomeBinding
import com.escarletsforest.app.ui.viewmodel.MainViewModel
import com.escarletsforest.app.ui.viewmodel.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: MainViewModel by activityViewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupUI()
        setupObservers()
        setupClickListeners()
    }
    
    private fun setupUI() {
        // Configurar el SwipeRefreshLayout
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshData()
        }
    }
    
    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            // Observar estado de la UI
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.swipeRefresh.isRefreshing = true
                    }
                    is UiState.Success -> {
                        binding.swipeRefresh.isRefreshing = false
                    }
                    is UiState.Error -> {
                        binding.swipeRefresh.isRefreshing = false
                        showError(state.message)
                    }
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            // Observar estadísticas globales
            viewModel.globalStatistics.collectLatest { stats ->
                stats?.let {
                    updateGlobalStats(it)
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            // Observar estadísticas del usuario
            viewModel.userStatistics.collectLatest { stats ->
                stats?.let {
                    updateUserStats(it)
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            // Observar plantas recientes
            viewModel.userPlants.collectLatest { plants ->
                updateRecentPlants(plants.take(3)) // Mostrar solo las 3 más recientes
            }
        }
    }
    
    private fun setupClickListeners() {
        // Botón para plantar nuevo árbol
        binding.btnPlantTree.setOnClickListener {
            findNavController().navigate(com.escarletsforest.app.R.id.action_global_addPlantFragment)
        }
        
        // Botón para agregar planta interior
        binding.btnAddIndoorPlant.setOnClickListener {
            findNavController().navigate(com.escarletsforest.app.R.id.action_global_addPlantFragment)
        }
        
        // Ver todas las plantas
        binding.btnViewAllPlants.setOnClickListener {
            // Navegar a la lista de plantas
            // findNavController().navigate(R.id.action_homeFragment_to_myPlantsFragment)
        }
        
        // Ver mapa mundial
        binding.btnViewWorldMap.setOnClickListener {
            // Navegar al mapa mundial
            // findNavController().navigate(R.id.action_homeFragment_to_worldMapFragment)
        }
    }
    
    private fun updateGlobalStats(stats: com.escarletsforest.app.data.repository.GlobalStatistics) {
        val hasData = stats.totalPlants > 0
        binding.tvNoGlobalData.visibility = if (hasData) View.GONE else View.VISIBLE
        binding.gridGlobalStats.visibility = if (hasData) View.VISIBLE else View.GONE
        if (hasData) {
            binding.tvTotalPlantsGlobal.text = stats.totalPlants.toString()
            binding.tvTotalCo2Global.text = "${String.format("%.1f", stats.totalCO2Absorbed)} kg"
            binding.tvTotalOxygenGlobal.text = "${String.format("%.1f", stats.totalOxygenProduced)} kg"
            binding.tvUniqueSpeciesGlobal.text = stats.uniqueSpecies.toString()
        }
    }
    
    private fun updateUserStats(stats: com.escarletsforest.app.data.repository.UserStatistics) {
        val hasData = stats.totalPlants > 0
        binding.tvNoUserData.visibility = if (hasData) View.GONE else View.VISIBLE
        binding.gridUserStats.visibility = if (hasData) View.VISIBLE else View.GONE
        if (hasData) {
            binding.tvTotalPlantsUser.text = stats.totalPlants.toString()
            binding.tvTotalCo2User.text = "${String.format("%.1f", stats.totalCO2Absorbed)} kg"
            binding.tvTotalOxygenUser.text = "${String.format("%.1f", stats.totalOxygenProduced)} kg"
            binding.tvUniqueSpeciesUser.text = stats.uniqueSpecies.toString()
        }
    }
    
    private fun updateRecentPlants(plants: List<com.escarletsforest.app.data.model.Plant>) {
        if (plants.isEmpty()) {
            binding.layoutRecentPlants.visibility = View.GONE
        } else {
            binding.layoutRecentPlants.visibility = View.VISIBLE
            
            // Actualizar la lista de plantas recientes
            // Aquí podrías usar un RecyclerView o actualizar TextViews individuales
            plants.forEachIndexed { index, plant ->
                when (index) {
                    0 -> {
                        binding.tvRecentPlant1.text = plant.name
                        binding.tvRecentPlant1Species.text = plant.species
                    }
                    1 -> {
                        binding.tvRecentPlant2.text = plant.name
                        binding.tvRecentPlant2Species.text = plant.species
                    }
                    2 -> {
                        binding.tvRecentPlant3.text = plant.name
                        binding.tvRecentPlant3Species.text = plant.species
                    }
                }
            }
        }
    }
    
    private fun showError(message: String) {
        // Implementar mostrar error (Toast, Snackbar, etc.)
        // Por ahora solo imprimimos en el log
        println("Error en HomeFragment: $message")
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 