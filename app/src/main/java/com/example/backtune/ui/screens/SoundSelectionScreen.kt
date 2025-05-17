package com.example.backtune.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.backtune.R
import com.example.backtune.model.AmbientSound
import com.example.backtune.ui.theme.BackTuneColors
import com.example.backtune.ui.theme.BackTuneTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundSelectionScreen(
    sounds: List<AmbientSound>,
    selectedSoundId: String?,
    onSelect: (AmbientSound) -> Unit,
    onBack: () -> Unit,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val currentSelectedSound by viewModel.selectedSound.collectAsState()

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
                    text = stringResource(R.string.sound_selection_title),
                    color = BackTuneColors.TextPrimary
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
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

        // Sound List
        sounds.forEach { sound ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { 
                        viewModel.selectSound(sound)
                        onSelect(sound)
                        onBack()
                    },
                colors = CardDefaults.cardColors(
                    containerColor = if (sound.id == currentSelectedSound?.id) 
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
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = BackTuneColors.TextPrimary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SoundSelectionScreenPreview() {
    val sampleSounds = listOf(
        AmbientSound("rain", "Rain", "rain"),
        AmbientSound("waves", "Waves", "rain"),
        AmbientSound("forest", "Forest", "rain")
    )
    BackTuneTheme {
        SoundSelectionScreen(
            sounds = sampleSounds,
            selectedSoundId = "rain",
            onSelect = {},
            onBack = {}
        )
    }
} 