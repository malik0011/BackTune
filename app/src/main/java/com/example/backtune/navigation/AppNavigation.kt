package com.example.backtune.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.backtune.R
import com.example.backtune.model.AmbientSound
import com.example.backtune.ui.screens.HomeScreen
import com.example.backtune.ui.screens.PlayerScreen
import com.example.backtune.ui.screens.SoundSelectionScreen
import com.example.backtune.ui.screens.PlayerViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.backtune.viewmodel.MainViewModel

/**
 * Navigation routes for the app
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Player : Screen("player/{videoId}") {
        fun createRoute(videoId: String) = "player/$videoId"
    }
    object SoundSelection : Screen("sound_selection")
}

/**
 * Main navigation component that handles navigation between screens
 */
@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Home Screen
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToPlayer = { videoId ->
                    navController.navigate(Screen.Player.createRoute(videoId))
                }
            )
        }

        // Player Screen
        composable(
            route = Screen.Player.route,
            arguments = listOf(
                navArgument("videoId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val videoId = backStackEntry.arguments?.getString("videoId") ?: ""
            val viewModel: MainViewModel = hiltViewModel()
            
            PlayerScreen(
                videoId = videoId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Sound Selection Screen
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
    }
} 