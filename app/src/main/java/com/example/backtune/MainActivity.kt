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
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.backtune.navigation.AppNavigation
import com.example.backtune.ui.theme.BackTuneTheme
import com.example.backtune.util.PermissionHandler
import com.example.backtune.viewmodel.SharedIntentViewModel
import com.google.firebase.messaging.FirebaseMessaging
import com.onesignal.OneSignal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private val sharedIntentViewModel: SharedIntentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO: need move it to .propitiates class to ignore git and access it from build
        val apiKey = BuildConfig.MY_SECRET_API_KEY
        Log.d(TAG,"onCreate: Starting MainActivity $apiKey")


        if(apiKey.isNotEmpty()) { OneSignal.initWithContext(this, apiKey) }

        handleIntent(intent)

        setContent {
            BackTuneTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // ✅ Request notification permission (required from Android 13+)
                    PermissionHandler.RequestNotificationPermission(
                        activity = this,
                        onPermissionResult = { isGranted ->
                            Log.d(TAG,"Notification permission result: $isGranted")

                            if (isGranted) {
                                // ✅ Initialize Firebase Messaging and get the token
                                FirebaseMessaging.getInstance().token
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            val token = task.result
                                            Log.d(TAG,"FCM Token: $token")
                                        } else {
                                            Log.d(TAG, "Fetching FCM token failed")
                                        }
                                    }
                            } else {
                                Log.d(TAG, "Notification permission was not granted.")
                            }
                        }
                    )

                    // 🔁 Navigation
                    AppNavigation(
                        navController = navController,
                        sharedIntentViewModel = sharedIntentViewModel
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d(TAG,"onNewIntent:  $intent")
        handleIntent(intent)
    }

    // 🔗 Handle YouTube link from notification or share intent
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

    // ✅ YouTube link validation (non-shorts only)
    private fun isValidYouTubeUrl(url: String): Boolean {
        val pattern = "^(https?://)?(www\\.)?(youtube\\.com|youtu\\.be)/.+$"
        return url.matches(Regex(pattern)) && !url.contains("/shorts/")
    }

    // 🔍 Extract YouTube video ID from URL
    private fun extractVideoId(url: String): String? {
        val pattern = "(?<=watch\\?v=|/videos/|embed/|youtu.be/|/v/|/e/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*"
        val matcher = java.util.regex.Pattern.compile(pattern).matcher(url)
        return if (matcher.find()) matcher.group() else null
    }
}