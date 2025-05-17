package com.example.backtune.service

import android.content.Context
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.backtune.model.AmbientSound
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackgroundSoundService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var exoPlayer: ExoPlayer? = null
    private var currentSound: AmbientSound? = null

    init {
        initializePlayer()
    }

    private fun initializePlayer() {
        exoPlayer = ExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_ALL
            playWhenReady = true
        }
    }

    fun playSound(sound: AmbientSound) {
        currentSound = sound
        exoPlayer?.let { player ->
            try {
                // Get the raw resource ID
                val resourceId = context.resources.getIdentifier(
                    sound.resourceName,
                    "raw",
                    context.packageName
                )
                
                Log.d("BackgroundSoundService", "Resource ID for ${sound}: $resourceId")
                
                if (resourceId != 0) {
                    // Create MediaItem from raw resource
                    val mediaItem = MediaItem.fromUri("android.resource://${context.packageName}/$resourceId")
                    
                    // Stop current playback
                    player.stop()
                    
                    // Set new media item
                    player.setMediaItem(mediaItem)
                    
                    // Prepare and play
                    player.prepare()
                    player.play()
                    
                    Log.d("BackgroundSoundService", "Started playing: ${sound.name}")
                } else {
                    Log.e("BackgroundSoundService", "Resource not found: ${sound.resourceName}")
                }
            } catch (e: Exception) {
                Log.e("BackgroundSoundService", "Error playing sound: ${e.message}", e)
            }
        }
    }

    fun togglePlayback() {
        exoPlayer?.let { player ->
            if (player.isPlaying) {
                player.pause()
                Log.d("BackgroundSoundService", "Paused playback")
            } else {
                player.play()
                Log.d("BackgroundSoundService", "Resumed playback")
            }
        }
    }

    fun isPlaying(): Boolean {
        return exoPlayer?.isPlaying ?: false
    }

    fun updateVolume(volume: Float) {
        exoPlayer?.volume = volume
        Log.d("BackgroundSoundService", "Volume updated: $volume")
    }

    fun release() {
        exoPlayer?.release()
        exoPlayer = null
        Log.d("BackgroundSoundService", "Player released")
    }
} 