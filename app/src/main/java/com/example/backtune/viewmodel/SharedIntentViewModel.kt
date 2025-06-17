package com.example.backtune.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.backtune.util.RemoteConfigManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedIntentViewModel @Inject constructor() : ViewModel() {
    //video id
    private val _sharedVideoId = MutableStateFlow<String?>(null)
    val sharedVideoId: StateFlow<String?> = _sharedVideoId

    //creatorImage
    private val _creatorImageUrl = MutableStateFlow("") // fallback
    val creatorImageUrl: StateFlow<String> = _creatorImageUrl

    //creatorImage
    private val _appShareableURL = MutableStateFlow("") // fallback
    val appShareableURL: StateFlow<String> = _appShareableURL

    fun setSharedVideoId(videoId: String) {
        _sharedVideoId.value = videoId
    }

    fun clear() {
        _sharedVideoId.value = null
    }

    init {
        fetchRemoteConfigValues()
    }

    private fun fetchRemoteConfigValues() {
        viewModelScope.launch {
            val success = RemoteConfigManager.fetchAndActivate()
            val url1 = RemoteConfigManager.getCreatorImageUrl()
            val url2 = RemoteConfigManager.getAppSharableURl()
            Log.d("testAyan", "fetchProfileImage: $url1, $url2")

            _creatorImageUrl.value = url1.ifBlank { "https://example.com/default-profile.png" }
            _appShareableURL.value = url2.ifBlank { "" }
        }
    }
}