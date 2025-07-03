package com.escarletsforest.app.ui.plants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.escarletsforest.app.databinding.FragmentPlantDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlantDetailFragment : Fragment() {
    
    private var _binding: FragmentPlantDetailBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlantDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // TODO: Implementar detalle de planta
        binding.tvPlantDetailPlaceholder.text = "Detalle de Planta - Próximamente"
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 