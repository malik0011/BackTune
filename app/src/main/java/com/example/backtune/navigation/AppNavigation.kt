package com.example.backtune.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalUriHandler
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.backtune.ui.screens.AboutScreen
import com.example.backtune.ui.screens.HomeScreen
import com.example.backtune.ui.screens.PlayerScreen
import com.example.backtune.ui.screens.SoundSelectionScreen
import com.example.backtune.viewmodel.MainViewModel
import com.example.backtune.viewmodel.SharedIntentViewModel

/**
 * Navigation routes for the app
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Player : Screen("player/{videoId}") {
        fun createRoute(videoId: String) = "player/$videoId"
    }
    object SoundSelection : Screen("sound_selection")
    object About : Screen("about")
}

/**
 * Main navigation component that handles navigation between screens
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    sharedIntentViewModel: SharedIntentViewModel = hiltViewModel()
) {
    val sharedVideoId by sharedIntentViewModel.sharedVideoId.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToPlayer = { videoId ->
                    navController.navigate(Screen.Player.createRoute(videoId))
                },
                onNavigateToAbout = {
                    navController.navigate(Screen.About.route)
                }
            )
        }

        composable(Screen.Player.route) { backStackEntry ->
            val videoId = backStackEntry.arguments?.getString("videoId")
            if (videoId != null) {
                PlayerScreen(
                    videoId = videoId,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(Screen.SoundSelection.route) {
            val viewModel: MainViewModel = hiltViewModel()
            val selectedSound by viewModel.selectedSound.collectAsState()
            
            SoundSelectionScreen(
                sounds = viewModel.availableSounds,
                selectedSoundId = selectedSound?.id,
                onSelect = { sound ->
                    viewModel.selectSound(sound)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.About.route) {
            val uriHandler = LocalUriHandler.current
            AboutScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onContactMe = {
                    uriHandler.openUri("https://www.linkedin.com/in/ayan-malik-1302a3199/")
                }
            )
        }
    }

    // Handle shared intents
    LaunchedEffect(sharedVideoId) {
        if (sharedVideoId != null) {
            navController.navigate(Screen.Player.createRoute(sharedVideoId!!))
            sharedIntentViewModel.clear()
        }
    }
} 
