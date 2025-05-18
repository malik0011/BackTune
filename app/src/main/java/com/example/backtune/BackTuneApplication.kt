package com.example.backtune

import android.app.Application
import com.example.backtune.util.NotificationScheduler
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BackTuneApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize notification scheduler
        NotificationScheduler.scheduleNotifications(this)
    }
} 