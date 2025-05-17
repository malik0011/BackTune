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

    // Available sounds - make sure these match your raw resource names exactly
    val availableSounds = listOf(
        AmbientSound("rain", "Rain", "rain"), // Should match rain.mp3 in raw folder
        AmbientSound("waves", "Waves", "waves"), // Should match waves.mp3 in raw folder
        AmbientSound("forest", "Forest", "forest") // Should match forest.mp3 in raw folder
    )

    fun showSoundSelection() {
        _isSoundSelectionVisible.value = true
    }

    fun hideSoundSelection() {
        _isSoundSelectionVisible.value = false
    }

    fun selectSound(sound: AmbientSound) {
        viewModelScope.launch {
            _selectedSound.value = sound
            soundService.playSound(sound)
            _isBackgroundPlaying.value = true
            hideSoundSelection()
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