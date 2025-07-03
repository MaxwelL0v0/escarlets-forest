package com.escarletsforest.app.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.escarletsforest.app.R
import com.escarletsforest.app.databinding.FragmentWorldMapBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorldMapFragment : Fragment() {
    
    private var _binding: FragmentWorldMapBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorldMapBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // TODO: Implementar mapa mundial con Google Maps
        binding.tvMapPlaceholder.text = "Mapa Mundial - Próximamente"
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 