package com.example.backtune.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.backtune.model.AmbientSound
import com.example.backtune.service.BackgroundSoundService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val soundService: BackgroundSoundService
) : ViewModel() {
    // State for background sound
    private val _selectedSound = MutableStateFlow<AmbientSound?>(null)
    val selectedSound: StateFlow<AmbientSound?> = _selectedSound

    // State for volume
    private val _volume = MutableStateFlow(0.5f)
    val volume: StateFlow<Float> = _volume

    // State for player
    private val _isBackgroundPlaying = MutableStateFlow(false)
    val isBackgroundPlaying: StateFlow<Boolean> = _isBackgroundPlaying

    // List of available sounds
    val availableSounds = listOf(
        AmbientSound("rain", "Rain", "rain"),
        AmbientSound("waves", "Waves", "rain"),
        AmbientSound("forest", "Forest", "rain")
    )

    fun updateVolume(newVolume: Float) {
        _volume.value = newVolume.coerceIn(0f, 1f)
        soundService.updateVolume(newVolume)
    }

    fun selectSound(sound: AmbientSound) {
        viewModelScope.launch {
            _selectedSound.value = sound
            soundService.playSound(sound)
            _isBackgroundPlaying.value = true
        }
    }

    fun toggleBackgroundPlayback() {
        soundService.togglePlayback()
        _isBackgroundPlaying.value = soundService.isPlaying()
    }

    override fun onCleared() {
        super.onCleared()
        soundService.release()
    }
} 