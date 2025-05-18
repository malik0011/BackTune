package com.example.backtune.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat

/**
 * Utility class to handle notification permissions
 */
object PermissionHandler {
    /**
     * Checks if notification permission is granted
     */
    fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // For Android 12 and below, no runtime permission is needed
        }
    }

    /**
     * Composable function to request notification permission
     * @param activity The activity to request permission from
     * @param onPermissionResult Callback for permission result
     */
    @Composable
    fun RequestNotificationPermission(
        activity: Activity,
        onPermissionResult: (Boolean) -> Unit
    ) {
        var hasPermission by remember {
            mutableStateOf(hasNotificationPermission(activity))
        }

        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            hasPermission = isGranted
            onPermissionResult(isGranted)
        }

        LaunchedEffect(Unit) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasPermission) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
} 