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
import com.example.videocall.databinding.FragmentLoginBinding
import com.example.videocall.models.LoginRequest
import com.example.videocall.ui.hideKeyboard
import com.example.videocall.ui.navigation.Destination
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.inputPassword.isEndIconVisible = false
        binding.etPassword.setOnFocusChangeListener { _, hasFocus ->
            binding.inputPassword.isEndIconVisible = hasFocus && binding.etPassword.error == null
        }

        binding.btnSignup.setOnClickListener {
            findNavController().navigate(Destination.SignUp.route)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (!validateInput(email, password))
                return@setOnClickListener

            hideKeyboard(binding.root)
            lifecycleScope.launch {
                try {
                    val response = VideoCallApplication.instance.apiService.login(
                        LoginRequest(email, password)
                    )
                    if (response.code() != 200) {
                        binding.etEmail.error = "Invalid Credentials"
                        binding.etPassword.error = "Invalid Credentials"
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

    private fun validateInput(email: String, password: String): Boolean {
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()

        if (!emailRegex.matches(email)) {
            binding.etEmail.error = "Invalid email"
            return false
        }

        if (password.isEmpty()) {
            binding.inputPassword.isEndIconVisible = false
            binding.etPassword.error = "Required"
            return false
        }

        return true
    }
}