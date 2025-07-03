package com.escarletsforest.app.ui.plants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.escarletsforest.app.databinding.FragmentMyPlantsBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.escarletsforest.app.R
import androidx.navigation.fragment.findNavController

@AndroidEntryPoint
class MyPlantsFragment : Fragment() {
    
    private var _binding: FragmentMyPlantsBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPlantsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val recyclerView = binding.rvMyPlants
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) return

        Firebase.firestore.collection("plants")
            .whereEqualTo("userId", user.uid)
            .get()
            .addOnSuccessListener { result ->
                val plants = result.documents.map { doc ->
                    PlantListItem(
                        name = doc.getString("name") ?: "",
                        species = doc.getString("species") ?: "",
                        date = doc.getDate("plantedDate")?.toString() ?: "",
                        photoUrl = doc.getString("photoUrl")
                    )
                }
                recyclerView.adapter = PlantAdapter(plants)
            }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    data class PlantListItem(
        val name: String,
        val species: String,
        val date: String,
        val photoUrl: String?
    )

    class PlantAdapter(private val items: List<PlantListItem>) : RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                com.escarletsforest.app.R.layout.item_plant, parent, false
            )
            return PlantViewHolder(view)
        }
        override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
            holder.bind(items[position])
        }
        override fun getItemCount() = items.size
        class PlantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val ivPhoto: ImageView = itemView.findViewById(R.id.iv_plant_photo)
            private val tvName: TextView = itemView.findViewById(R.id.tv_plant_name)
            private val tvSpecies: TextView = itemView.findViewById(R.id.tv_plant_species)
            private val tvDate: TextView = itemView.findViewById(R.id.tv_plant_date)
            fun bind(item: PlantListItem) {
                tvName.text = item.name
                tvSpecies.text = item.species
                tvDate.text = item.date
                if (!item.photoUrl.isNullOrEmpty()) {
                    Glide.with(itemView).load(item.photoUrl).into(ivPhoto)
                } else {
                    ivPhoto.setImageResource(com.escarletsforest.app.R.drawable.ic_tree)
                }
            }
        }
    }
} 