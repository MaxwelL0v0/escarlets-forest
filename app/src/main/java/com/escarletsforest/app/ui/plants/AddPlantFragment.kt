package com.escarletsforest.app.ui.plants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.escarletsforest.app.databinding.FragmentAddPlantBinding
import dagger.hilt.android.AndroidEntryPoint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.escarletsforest.app.data.model.Plant
import com.escarletsforest.app.data.model.PlantType
import java.util.Date
import androidx.navigation.fragment.findNavController

@AndroidEntryPoint
class AddPlantFragment : Fragment() {
    
    private var _binding: FragmentAddPlantBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPlantBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val etName = binding.etPlantName
        val etSpecies = binding.etPlantSpecies
        val etLocation = binding.etPlantLocation
        val etDate = binding.etPlantDate
        val btnSave = binding.btnSavePlant

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val species = etSpecies.text.toString().trim()
            val location = etLocation.text.toString().trim()
            val dateStr = etDate.text.toString().trim()
            val user = FirebaseAuth.getInstance().currentUser
            if (name.isEmpty() || species.isEmpty() || user == null) {
                Toast.makeText(requireContext(), "Completa los campos obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val plantedDate = Date() // Por simplicidad, usar fecha actual
            val plant = hashMapOf(
                "name" to name,
                "species" to species,
                "location" to location,
                "plantedDate" to plantedDate,
                "userId" to user.uid,
                "userName" to (user.displayName ?: user.email ?: "Usuario"),
                "plantType" to PlantType.TREE.name, // Por defecto árbol
                "createdAt" to Date()
            )
            Firebase.firestore.collection("plants")
                .add(plant)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "¡Planta registrada!", Toast.LENGTH_SHORT).show()
                    etName.text.clear()
                    etSpecies.text.clear()
                    etLocation.text.clear()
                    etDate.text.clear()
                    findNavController().navigate(com.escarletsforest.app.R.id.navigation_my_plants)
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Error al guardar: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 