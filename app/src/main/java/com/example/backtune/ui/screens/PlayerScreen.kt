package com.example.backtune.ui.screens

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.backtune.R
import com.example.backtune.model.AmbientSound
import com.example.backtune.ui.theme.BackTuneColors
import com.example.backtune.ui.theme.BackTuneTheme
import com.example.backtune.viewmodel.MainViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    videoId: String,
    onNavigateBack: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val selectedSound by viewModel.selectedSound.collectAsState()
    val volume by viewModel.volume.collectAsState()
    val isBackgroundPlaying by viewModel.isBackgroundPlaying.collectAsState()
    val isSoundSelectionVisible by viewModel.isSoundSelectionVisible.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    var youTubePlayer by remember { mutableStateOf<YouTubePlayer?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackTuneColors.Background)
            .padding(16.dp)
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.player_title),
                    color = BackTuneColors.TextPrimary
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = BackTuneColors.TextPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = BackTuneColors.Surface
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // YouTube Player
        AndroidView(
            factory = { context ->
                YouTubePlayerView(context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                        override fun onReady(player: YouTubePlayer) {
                            youTubePlayer = player
                            player.loadVideo(videoId, 0f)
                        }
                    })
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Background Sound Controls
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(
                containerColor = BackTuneColors.CardBackground
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.background_sound),
                        style = MaterialTheme.typography.titleMedium,
                        color = BackTuneColors.TextPrimary
                    )
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (selectedSound != null) {
                            IconButton(
                                onClick = { viewModel.toggleBackgroundPlayback() }
                            ) {
                                Icon(
                                    painter = painterResource(id =
                                    if (isBackgroundPlaying)
                                        R.drawable.ic_pause
                                    else
                                        R.drawable.ic_play
                                    ),
                                    contentDescription = if (isBackgroundPlaying) 
                                        "Pause Background" 
                                    else 
                                        "Play Background",
                                    tint = BackTuneColors.Primary
                                )
                            }
                        }
                        
                        Button(
                            onClick = { viewModel.showSoundSelection() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BackTuneColors.Primary
                            ),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = BackTuneColors.TextPrimary
                                )
                            } else {
                                Text(stringResource(R.string.select_sound))
                            }
                        }
                    }
                }

                if (selectedSound != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = selectedSound?.name?: "",
                        color = BackTuneColors.TextSecondary
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    Slider(
                        value = volume,
                        onValueChange = { viewModel.updateVolume(it) },
                        colors = SliderDefaults.colors(
                            thumbColor = BackTuneColors.Primary,
                            activeTrackColor = BackTuneColors.Primary,
                            inactiveTrackColor = BackTuneColors.TextTertiary
                        )
                    )
                }
            }
        }

        // Sound Selection Bottom Sheet
        if (isSoundSelectionVisible) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.hideSoundSelection() },
                containerColor = BackTuneColors.Surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.sound_selection_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = BackTuneColors.TextPrimary
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(viewModel.availableSounds) { sound ->
                            SoundItem(
                                sound = sound,
                                isSelected = sound.id == selectedSound?.id,
                                onSelect = { viewModel.selectSound(sound) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SoundItem(
    sound: AmbientSound,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val icon = when (sound.id) {
        "rain" -> R.drawable.ic_rain
        "waves" -> R.drawable.ic_waves
        "forest" -> R.drawable.ic_forest
        else -> R.drawable.ic_music
    }

    val backgroundColor = if (isSelected) {
        BackTuneColors.Primary.copy(alpha = 0.1f)
    } else {
        BackTuneColors.CardBackground
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icon Container
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) BackTuneColors.Primary
                        else BackTuneColors.Primary.copy(alpha = 0.1f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = sound.name,
                    colorFilter = ColorFilter.tint(if (isSelected) Color.White else BackTuneColors.Primary),
                    modifier = Modifier.size(24.dp)
                )
            }

            // Sound Info
            Text(
                text = sound.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = BackTuneColors.TextPrimary
            )

            // Selection Indicator
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = BackTuneColors.Primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayerScreenPreview() {
    BackTuneTheme {
        PlayerScreen(
            videoId = "dQw4w9WgXcQ",
            onNavigateBack = {}
        )
    }
} 