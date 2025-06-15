package com.example.backtune.service.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.backtune.MainActivity
import com.example.backtune.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.net.HttpURLConnection
import java.net.URL

/**
 * Handles incoming Firebase push notifications and token refreshes.
 */
class FirebaseCMService : FirebaseMessagingService() {

    private val TAG = "FirebaseCMService"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG,"FCM Token: $token")
        // TODO: Send token to your backend if needed
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.notification?.title ?: "BackTune"
        val message = remoteMessage.notification?.body ?: "You've got something new!"
        val imageUrl = remoteMessage.data["image"]
        val youtubeLink = remoteMessage.data["youtube"]

        showNotification(title, message, imageUrl, youtubeLink)
    }

    /**
     * Builds and displays a push notification.
     * @param title Title text
     * @param message Message body
     * @param imageUrl Optional image URL for rich notification
     * @param youtubeLink Optional YouTube link to open on tap
     */
    private fun showNotification(
        title: String,
        message: String,
        imageUrl: String?,
        youtubeLink: String?
    ) {
        val channelId = "backtune_notifications"
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("youtube_link", youtubeLink)
        }

        val pendingIntent: PendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            } else {
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSound)
            .setContentIntent(pendingIntent)
            .setColor(getColor(R.color.primary))

        // Load image if available
        imageUrl?.let {
            val bitmap = getBitmapFromUrl(it)
            bitmap?.let { image ->
                notificationBuilder.setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(image)
//                        .bigLargeIcon()
                )
            }
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "BackTune Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Push notifications from BackTune"
            }
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

    /**
     * Helper to load image from URL for BigPicture notifications.
     */
    private fun getBitmapFromUrl(imageUrl: String): android.graphics.Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            Log.e(TAG, "Image load failed: $e")
            null
        }
    }
}