package com.example.videocall.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.videocall.R
import com.example.videocall.VideoCallApplication
import com.example.videocall.models.UserProfile
import com.example.videocall.ui.navigation.Destination
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var userProfile: UserProfile

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            userProfile = VideoCallApplication.instance.userProfileRepository.userProfile.first()
            Log.d("HomeFragment", userProfile.toString())
            if (userProfile.token.isEmpty()) {
                findNavController().navigate(
                    Destination.Login.route,
                    NavOptions.Builder()
                        .setPopUpTo(Destination.Home.route, inclusive = true)
                        .build()
                )
            }
        }

        return view
    }
}