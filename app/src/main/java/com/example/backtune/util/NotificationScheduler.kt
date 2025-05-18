package com.example.backtune.util

import android.content.Context
import androidx.work.*
import com.example.backtune.worker.NotificationWorker
import java.util.concurrent.TimeUnit

/**
 * Utility class to handle scheduling of notifications
 * Schedules daily notifications at specific times (9 PM, 10 PM, 11 PM, and 12 AM)
 */
object NotificationScheduler {

    /**
     * Schedules daily notifications at specified times
     * @param context Application context
     */
    fun scheduleNotifications(context: Context) {
        // Cancel any existing work
        WorkManager.getInstance(context).cancelAllWorkByTag("notification_work")

        // Schedule notifications for each time
        scheduleNotification(context, 21, 0) // 9 PM
        scheduleNotification(context, 13, 56) // 9 PM
        scheduleNotification(context, 22, 0) // 10 PM
        scheduleNotification(context, 23, 0) // 11 PM
        scheduleNotification(context, 0, 0)  // 12 AM
    }

    /**
     * Schedules a single notification at the specified hour and minute
     * @param context Application context
     * @param hour Hour of the day (0-23)
     * @param minute Minute of the hour (0-59)
     */
    private fun scheduleNotification(context: Context, hour: Int, minute: Int) {
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            24, TimeUnit.HOURS,
            15, TimeUnit.MINUTES
        )
            .setInitialDelay(calculateInitialDelay(hour, minute), TimeUnit.MILLISECONDS)
            .addTag("notification_work")
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                "notification_$hour:$minute",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
    }

    /**
     * Calculates the initial delay until the next occurrence of the specified time
     * @param hour Target hour
     * @param minute Target minute
     * @return Delay in milliseconds
     */
    private fun calculateInitialDelay(hour: Int, minute: Int): Long {
        val currentTime = System.currentTimeMillis()
        val calendar = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, hour)
            set(java.util.Calendar.MINUTE, minute)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }

        var targetTime = calendar.timeInMillis
        if (targetTime <= currentTime) {
            calendar.add(java.util.Calendar.DAY_OF_MONTH, 1)
            targetTime = calendar.timeInMillis
        }

        return targetTime - currentTime
    }
} 