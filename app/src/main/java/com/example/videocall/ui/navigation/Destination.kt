package com.example.videocall.ui.navigation

sealed class Destination(val route: String) {
    data object Home: Destination("home")
    data object Login: Destination("login")
    data object SignUp: Destination("signup")
}