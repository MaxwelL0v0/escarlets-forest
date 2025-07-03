package com.escarletsforest.app.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.escarletsforest.app.R
import com.escarletsforest.app.databinding.FragmentProfileBinding
import com.escarletsforest.app.ui.viewmodel.ProfileViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.EditText
import android.widget.Button
import android.app.AlertDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.Query

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    private var userName: String? = null
    
    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(Exception::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al iniciar sesión: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGoogleSignIn()
        setupUI()
        observeUser()
        binding.btnEditProfile.setOnClickListener {
            showEditProfileDialog()
        }
        binding.btnRegister.setOnClickListener {
            showRegisterDialog()
        }
    }
    
    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }
    
    private fun setupUI() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                profileViewModel.login(email, password) { success, error ->
                    if (!success) {
                        Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                profileViewModel.register(email, password) { success, error ->
                    if (!success) {
                        Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnSignOut.setOnClickListener {
            profileViewModel.signOut()
        }
    }
    
    private fun observeUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                profileViewModel.user.collectLatest { user ->
                    if (user != null) {
                        // Obtener nombre personalizado de Firestore
                        Firebase.firestore.collection("users").document(user.uid).get()
                            .addOnSuccessListener { document ->
                                userName = document.getString("name") ?: "Usuario"
                                binding.tvProfileName.text = userName
                            }
                        // Actualizar estadísticas personales
                        updateUserStats(user.uid)
                        binding.ivProfileImage.visibility = View.VISIBLE
                        binding.tvProfileName.visibility = View.VISIBLE
                        binding.tvProfileEmail.visibility = View.GONE
                        binding.tvProfileMessage.visibility = View.VISIBLE
                        binding.cardStats.visibility = View.VISIBLE
                        binding.btnSignOut.visibility = View.VISIBLE
                        binding.btnEditProfile.visibility = View.VISIBLE
                        binding.etEmail.visibility = View.GONE
                        binding.etPassword.visibility = View.GONE
                        binding.btnLogin.visibility = View.GONE
                        binding.btnRegister.visibility = View.GONE
                        binding.tvProfileMessage.text = "¡Gracias por ser parte del bosque global, ${userName ?: "amig@"}! 🌱"
                        Glide.with(this@ProfileFragment)
                            .load(user.photoUrl)
                            .placeholder(R.drawable.ic_profile)
                            .into(binding.ivProfileImage)
                    } else {
                        binding.ivProfileImage.visibility = View.GONE
                        binding.tvProfileName.visibility = View.GONE
                        binding.tvProfileEmail.visibility = View.GONE
                        binding.tvProfileMessage.visibility = View.GONE
                        binding.cardStats.visibility = View.GONE
                        binding.btnSignOut.visibility = View.GONE
                        binding.btnEditProfile.visibility = View.GONE
                        binding.etEmail.visibility = View.VISIBLE
                        binding.etPassword.visibility = View.VISIBLE
                        binding.btnLogin.visibility = View.VISIBLE
                        binding.btnRegister.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
    
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        if (account == null) return
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d("ProfileFragment", "Autenticación con Google exitosa")
                    profileViewModel.refreshUser()
                } else {
                    Log.e("ProfileFragment", "Error de autenticación con Google", task.exception)
                    Toast.makeText(requireContext(), "Error de autenticación con Google", Toast.LENGTH_SHORT).show()
                }
            }
    }
    
    private fun showRegisterDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_register, null)
        val etEmail = dialogView.findViewById<EditText>(R.id.et_register_email)
        val etPassword = dialogView.findViewById<EditText>(R.id.et_register_password)
        val etName = dialogView.findViewById<EditText>(R.id.et_register_name)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btn_register_confirm)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        btnConfirm.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val name = etName.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
                profileViewModel.register(email, password) { success, error ->
                    if (success) {
                        // Guardar nombre en Firestore
                        val user = profileViewModel.user.value
                        user?.let {
                            val db = FirebaseFirestore.getInstance()
                            db.collection("users").document(it.uid)
                                .set(mapOf("name" to name, "email" to email))
                        }
                        dialog.dismiss()
                    } else {
                        Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }
    
    private fun showEditProfileDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_profile, null)
        val etEditName = dialogView.findViewById<EditText>(R.id.et_edit_name)
        etEditName.setText(userName ?: "")
        val btnSave = dialogView.findViewById<Button>(R.id.btn_save_profile)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
        btnSave.setOnClickListener {
            val newName = etEditName.text.toString().trim()
            val user = profileViewModel.user.value
            if (user != null && newName.isNotEmpty()) {
                Firebase.firestore.collection("users").document(user.uid)
                    .update("name", newName)
                    .addOnSuccessListener {
                        userName = newName
                        binding.tvProfileName.text = newName
                        binding.tvProfileMessage.text = "¡Gracias por ser parte del bosque global, $newName! 🌱"
                        dialog.dismiss()
                    }
            } else {
                Toast.makeText(requireContext(), "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }
    
    private fun updateUserStats(userId: String) {
        Firebase.firestore.collection("plants")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val total = result.size()
                val co2 = total * 22 // 22kg por árbol/planta
                binding.tvStatPlants.text = total.toString()
                binding.tvStatTrees.text = total.toString()
                binding.tvStatCo2.text = "$co2 kg"
            }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 