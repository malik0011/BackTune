package com.example.backtune.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.backtune.MainActivity
import com.example.backtune.R
import com.example.backtune.ui.theme.Primary
import com.example.backtune.util.NotificationTextProvider

/**
 * Worker class to handle scheduled notifications
 * This worker is responsible for showing relaxation reminders at specific times
 */
class NotificationWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        private const val TAG = "NotificationWorker"
        const val CHANNEL_ID = "backtune_notification_channel"
        const val NOTIFICATION_ID = 1
    }

    override suspend fun doWork(): Result {
        Log.d(TAG, "Starting notification work")
        
        // Check for notification permission
        if (!hasNotificationPermission()) {
            Log.e(TAG, "Notification permission not granted")
            return Result.failure()
        }
        
        try {
            showNotification()
            Log.d(TAG, "Notification shown successfully")
            return Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error showing notification", e)
            return Result.failure()
        }
    }

    /**
     * Shows a notification with a relaxation reminder
     */
    private fun showNotification() {
        Log.d(TAG, "Creating notification channel")
        // Create notification channel for Android O and above
        createNotificationChannel()

        // Get random notification text
        val notificationText = NotificationTextProvider.getRandomNotificationText()
        Log.d(TAG, "Selected notification text: ${notificationText.title}")

        // Create intent to open the app when notification is clicked
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        Log.d(TAG, "Created pending intent for notification click")

        // Build the notification
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_icon) // Use the foreground icon
            .setContentTitle(notificationText.title)
            .setContentText(notificationText.content)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(notificationText.expandedText))
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Set to HIGH priority
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setColor(ContextCompat.getColor(context, R.color.primary))
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // Make notification visible on lock screen
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .build()
        Log.d(TAG, "Built notification object")

        // Show the notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
        Log.d(TAG, "Notification posted to system")
    }

    /**
     * Creates a notification channel for Android O and above
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "BackTune Notifications"
            val descriptionText = "Notifications for relaxation reminders"
            val importance = NotificationManager.IMPORTANCE_HIGH // Set to HIGH importance
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableLights(true)
                lightColor = Color.BLUE
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500) // Vibration pattern
                setShowBadge(true) // Show badge on app icon
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            Log.d(TAG, "Created notification channel for Android O and above")
        } else {
            Log.d(TAG, "Device is below Android O, no need for notification channel")
        }
    }

    /**
     * Checks if the app has notification permission
     */
    private fun hasNotificationPermission(): Boolean {
        val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // For Android 12 and below, no runtime permission is needed
        }
        Log.d(TAG, "Notification permission check result: $hasPermission")
        return hasPermission
    }
} 