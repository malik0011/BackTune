package com.example.backtune.ui.screens

import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.example.backtune.viewmodel.MainViewModel

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
                            onClick = { 
                                Log.d("PlayerScreen", "Opening sound selection")
                                viewModel.showSoundSelection() 
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BackTuneColors.Primary
                            )
                        ) {
                            Text(stringResource(R.string.select_sound))
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                BackTuneColors.Primary 
            else 
                BackTuneColors.CardBackground
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = sound.name,
                style = MaterialTheme.typography.titleMedium,
                color = BackTuneColors.TextPrimary
            )
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