package com.example.videocall.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.createGraph
import androidx.navigation.fragment.fragment
import com.example.videocall.ui.home.HomeFragment
import com.example.videocall.ui.home.LoginFragment
import com.example.videocall.ui.home.SignUpFragment

fun NavController.setupNavGraph() {
    graph = createGraph(startDestination = Destination.Home.route) {
        fragment<HomeFragment>(Destination.Home.route)
        fragment<LoginFragment>(Destination.Login.route)
        fragment<SignUpFragment>(Destination.SignUp.route)
    }
}