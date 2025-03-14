package com.example.videocall.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.videocall.VideoCallApplication
import com.example.videocall.databinding.FragmentSignUpBinding
import com.example.videocall.models.RegisterRequest
import com.example.videocall.ui.hideKeyboard
import com.example.videocall.ui.navigation.Destination
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.inputPassword.isEndIconVisible = false
        binding.etPassword.setOnFocusChangeListener { _, hasFocus ->
            binding.inputPassword.isEndIconVisible = hasFocus && binding.etPassword.error == null
        }

        binding.btnSignup.setOnClickListener {
            val name = binding.etFullName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (!validateInput(name, email, password))
                return@setOnClickListener

            hideKeyboard(binding.root)
            lifecycleScope.launch {
                try {
                    val response = VideoCallApplication.instance.apiService.register(
                        RegisterRequest(name, email, password)
                    )
                    if (response.code() != 200) {
                        binding.etEmail.error = "Email Already Exists"
                        return@launch
                    }
                    val tokenResponse = response.body()!!
                    VideoCallApplication.instance.userProfileRepository.saveUserProfile(
                        tokenResponse.toUserProfile()
                    )
                    findNavController().navigate(
                        Destination.Home.route,
                        NavOptions.Builder()
                            .setPopUpTo(Destination.Login.route, inclusive = true)
                            .build()
                    )
                } catch (e: Exception) {
                    Snackbar.make(binding.root, "Network Error", Snackbar.LENGTH_SHORT).show()
                    Log.e("SignUpFragment", e.stackTraceToString())
                }
            }
        }

        return binding.root
    }

    private fun validateInput(name: String, email: String, password: String): Boolean {
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()

        if (name.isEmpty()) {
            binding.etFullName.error = "Required"
            return false
        }

        if (!emailRegex.matches(email)) {
            binding.etEmail.error = "Invalid email"
            return false
        }

        if (password.length < 8) {
            binding.inputPassword.isEndIconVisible = false
            binding.etPassword.error = "Must be at least 8 characters"
            return false
        }

        return true
    }
}