package com.example.backtune.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.backtune.R
import com.example.backtune.ui.theme.BackTuneColors
import com.example.backtune.ui.theme.BackTuneTheme
import com.example.backtune.util.Constants

/**
 * Home screen of the app where users can enter a YouTube URL
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToPlayer: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var videoUrl by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackTuneColors.Background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // App Logo/Title Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(24.dp)
                )
                .clip(RoundedCornerShape(24.dp))
                .background(
                    brush = Brush.linearGradient(BackTuneColors.PrimaryGradient)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = BackTuneColors.TextPrimary
            )
        }
        
        // Welcome Text
        Text(
            text = stringResource(R.string.home_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = BackTuneColors.TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        
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
                        isError = false
                    },
                    isError = isError,
                    supportingText = {
                        if (isError) {
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
                        isError = true
                    }
                } else {
                    isError = true
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