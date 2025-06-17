package com.example.backtune.util

import android.util.Log
import com.example.backtune.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.tasks.await

object RemoteConfigManager {

    private const val TAG = "RemoteConfigManager"

    private val remoteConfig: FirebaseRemoteConfig by lazy { FirebaseRemoteConfig.getInstance() }

    init {
        Log.d(TAG, "Initializing RemoteConfig...")
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600) // 1 hour
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        Log.d(TAG, "RemoteConfig settings and defaults applied.")
    }

    suspend fun fetchAndActivate(): Boolean {
        return try {
            Log.d(TAG, "Fetching and activating remote config...")
            val result = remoteConfig.fetchAndActivate().await()
            Log.d(TAG, "Remote config fetchAndActivate result: $result")
            result
        } catch (e: Exception) {
            Log.e(TAG, "Remote config fetch failed: ${e.message}", e)
            false
        }
    }

    fun getString(key: String): String {
        val value = remoteConfig.getString(key)
        Log.d(TAG, "getString: key=$key, value=$value")
        return value
    }

    fun getBoolean(key: String): Boolean {
        val value = remoteConfig.getBoolean(key)
        Log.d(TAG, "getBoolean: key=$key, value=$value")
        return value
    }

    fun getLong(key: String): Long {
        val value = remoteConfig.getLong(key)
        Log.d(TAG, "getLong: key=$key, value=$value")
        return value
    }

    fun getCreatorImageUrl(): String {
        val value = remoteConfig.getString("profile_image_url")
        Log.d(TAG, "getCreatorImageUrl: $value")
        return value
    }

    fun getAppSharableURl(): String {
        val value = remoteConfig.getString("backtune_playstore_url")
        Log.d(TAG, "getAppSharableURl: $value")
        return value
    }
}