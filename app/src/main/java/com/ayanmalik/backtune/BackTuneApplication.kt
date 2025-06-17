package com.ayanmalik.backtune

import android.app.Application
import com.ayanmalik.backtune.util.NotificationScheduler
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BackTuneApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        
        // Initialize notification scheduler
        NotificationScheduler.scheduleNotifications(this)
    }
} 