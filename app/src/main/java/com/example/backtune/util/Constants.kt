package com.example.backtune.util

object Constants {
    // Navigation
    const val HOME_ROUTE = "home"
    const val PLAYER_ROUTE = "player/{videoId}"
    const val SOUND_SELECTION_ROUTE = "sound_selection"
    
    // YouTube
    const val YOUTUBE_URL_PATTERN = "^(https?://)?(www\\.)?(youtube\\.com|youtu\\.be)/.+$"
    
    // Default values
    const val DEFAULT_VOLUME = 0.5f
    const val MAX_VOLUME = 1.0f
    const val MIN_VOLUME = 0.0f
} 