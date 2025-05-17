package com.example.backtune.navigation

// Navigation routes for the app
sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home")
    object Player : NavRoutes("player/{videoId}") {
        fun createRoute(videoId: String) = "player/$videoId"
    }
    object SoundSelection : NavRoutes("sound_selection")
} 