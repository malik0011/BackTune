package com.example.backtune.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.backtune.R
import com.example.backtune.ui.theme.BackTuneColors
import com.example.backtune.ui.theme.BackTuneTheme
import com.example.backtune.util.Constants
import com.example.backtune.viewmodel.MainViewModel

/**
 * Home screen of the app where users can enter a YouTube URL
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToPlayer: (String) -> Unit,
) {
    var videoUrl by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackTuneColors.Background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo and Title Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Logo
            Image(
                painter = painterResource(id = R.drawable.ic_backtune_logo),
                contentDescription = "BackTune Logo",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
            
            Text(
                text = stringResource(R.string.home_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = BackTuneColors.TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // URL Input Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(16.dp)
                ),
            colors = CardDefaults.cardColors(
                containerColor = BackTuneColors.CardBackground
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.youtube_url_hint),
                    style = MaterialTheme.typography.titleMedium,
                    color = BackTuneColors.TextPrimary
                )
                
                OutlinedTextField(
                    value = videoUrl,
                    onValueChange = { 
                        videoUrl = it
                        showError = false
                    },
                    isError = showError,
                    supportingText = {
                        if (showError) {
                            Text(
                                text = stringResource(R.string.error_invalid_url),
                                color = BackTuneColors.AccentRed
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = BackTuneColors.Primary,
                        unfocusedBorderColor = BackTuneColors.TextTertiary,
                        cursorColor = BackTuneColors.Primary,
                        focusedLabelColor = BackTuneColors.Primary,
                        unfocusedLabelColor = BackTuneColors.TextSecondary,
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = BackTuneColors.TextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Play Button
        Button(
            onClick = {
                if (videoUrl.matches(Regex(Constants.YOUTUBE_URL_PATTERN))) {
                    // Extract video ID from URL
                    val videoId = extractVideoId(videoUrl)
                    if (videoId != null) {
                        onNavigateToPlayer(videoId)
                    } else {
                        showError = true
                    }
                } else {
                    showError = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BackTuneColors.Primary
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = stringResource(R.string.play_button),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

/**
 * Extracts video ID from YouTube URL
 * @param url YouTube URL
 * @return Video ID or null if invalid
 */
private fun extractVideoId(url: String): String? {
    val pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*"
    val matcher = java.util.regex.Pattern.compile(pattern).matcher(url)
    return if (matcher.find()) matcher.group() else null
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    BackTuneTheme {
        HomeScreen(
            onNavigateToPlayer = {}
        )
    }
}