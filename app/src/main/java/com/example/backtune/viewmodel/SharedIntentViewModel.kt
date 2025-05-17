package com.example.backtune.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SharedIntentViewModel @Inject constructor() : ViewModel() {
    private val _sharedVideoId = MutableStateFlow<String?>(null)
    val sharedVideoId: StateFlow<String?> = _sharedVideoId

    fun setSharedVideoId(videoId: String) {
        _sharedVideoId.value = videoId
    }

    fun clear() {
        _sharedVideoId.value = null
    }
}