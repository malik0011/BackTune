package com.example.backtune.viewmodel

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
class MainViewModel @Inject constructor(
    private val soundService: BackgroundSoundService
) : ViewModel() {
    // UI States
    private val _isSoundSelectionVisible = MutableStateFlow(false)
    val isSoundSelectionVisible: StateFlow<Boolean> = _isSoundSelectionVisible

    private val _selectedSound = MutableStateFlow<AmbientSound?>(null)
    val selectedSound: StateFlow<AmbientSound?> = _selectedSound

    private val _volume = MutableStateFlow(0.5f)
    val volume: StateFlow<Float> = _volume

    private val _isBackgroundPlaying = MutableStateFlow(false)
    val isBackgroundPlaying: StateFlow<Boolean> = _isBackgroundPlaying

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Available sounds - make sure these match your raw resource names exactly
    val availableSounds = listOf(
        AmbientSound("rain", "Rain", "rain_sound"), // Update to match your actual resource name
        AmbientSound("waves", "Waves", "waves_sound"), // Update to match your actual resource name
        AmbientSound("forest", "Forest", "forest_sound") // Update to match your actual resource name
    )

    fun showSoundSelection() {
        _isSoundSelectionVisible.value = true
    }

    fun hideSoundSelection() {
        _isSoundSelectionVisible.value = false
    }

    fun selectSound(sound: AmbientSound) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _selectedSound.value = sound
                soundService.playSound(sound)
                _isBackgroundPlaying.value = true
                hideSoundSelection()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleBackgroundPlayback() {
        soundService.togglePlayback()
        _isBackgroundPlaying.value = soundService.isPlaying()
    }

    fun updateVolume(newVolume: Float) {
        _volume.value = newVolume.coerceIn(0f, 1f)
        soundService.updateVolume(newVolume)
    }

    override fun onCleared() {
        super.onCleared()
        soundService.release()
    }
} 