package com.escarletsforest.app.ui.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.escarletsforest.app.databinding.FragmentCommunityBinding
import dagger.hilt.android.AndroidEntryPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.widget.TextView

@AndroidEntryPoint
class CommunityFragment : Fragment() {
    
    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val tvTotalPlants = binding.tvTotalPlants
        val tvTotalCO2 = binding.tvTotalCo2

        Firebase.firestore.collection("plants").get()
            .addOnSuccessListener { result ->
                val totalPlants = result.size()
                var totalCO2 = 0f
                for (doc in result) {
                    // Estimación simple: 22kg CO2 por árbol por año
                    totalCO2 += 22f
                }
                tvTotalPlants.text = "Plantas/Árboles registrados: $totalPlants"
                tvTotalCO2.text = "CO₂ reducido estimado: ${totalCO2.toInt()} kg"
            }
            .addOnFailureListener {
                tvTotalPlants.text = "Error al cargar datos"
                tvTotalCO2.text = ""
            }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 