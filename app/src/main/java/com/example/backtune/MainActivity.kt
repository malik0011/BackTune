package com.example.backtune

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.backtune.navigation.AppNavigation
import com.example.backtune.service.NotificationService
import com.example.backtune.ui.theme.BackTuneTheme
import com.example.backtune.viewmodel.SharedIntentViewModel
import com.example.backtune.util.PermissionHandler
import com.example.backtune.worker.NotificationWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private val sharedIntentViewModel: SharedIntentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: Starting MainActivity")
        
        // Start the notification service
        startService(Intent(this, NotificationService::class.java))
        
        handleIntent(intent)
        setContent {
            BackTuneTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Request notification permission
                    PermissionHandler.RequestNotificationPermission(
                        activity = this,
                        onPermissionResult = { isGranted ->
                            Log.d(TAG, "Notification permission result: $isGranted")
                        }
                    )
                    
                    // Test notification after 10 seconds
                    LaunchedEffect(Unit) {
                        Log.d(TAG, "Starting 10-second delay for test notification")
                        delay(10000) // 10 seconds delay
                        Log.d(TAG, "Delay completed, triggering test notification")
//                        testNotification()
                    }
                    
                    val navController = rememberNavController()
                    AppNavigation(navController = navController, sharedIntentViewModel = sharedIntentViewModel)
                }
            }
        }
    }

//    private fun testNotification() {
//        Log.d(TAG, "Creating test notification work request")
//        // Create a one-time work request
//        val notificationWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
//            .build()
//
//        // Enqueue the work request
//        WorkManager.getInstance(applicationContext)
//            .enqueue(notificationWorkRequest)
//        Log.d(TAG, "Test notification work request enqueued")
//    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
            if (sharedText != null && isValidYouTubeUrl(sharedText)) {
                val videoId = extractVideoId(sharedText)
                if (videoId != null) {
                    sharedIntentViewModel.setSharedVideoId(videoId)
                }
            }
        }
    }

    fun isValidYouTubeUrl(url: String): Boolean {
        val pattern = "^(https?://)?(www\\.)?(youtube\\.com|youtu\\.be)/.+$"
        return url.matches(Regex(pattern)) && !url.contains("/shorts/")
    }

    fun extractVideoId(url: String): String? {
        val pattern = "(?<=watch\\?v=|/videos/|embed/|youtu.be/|/v/|/e/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*"
        val matcher = java.util.regex.Pattern.compile(pattern).matcher(url)
        return if (matcher.find()) matcher.group() else null
    }
}