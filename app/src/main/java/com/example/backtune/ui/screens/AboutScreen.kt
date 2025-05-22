package com.example.backtune.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.backtune.R
import com.example.backtune.ui.theme.BackTuneColors
import com.example.backtune.ui.theme.BackTuneTheme
import android.content.Intent
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onNavigateBack: () -> Unit,
    onContactMe: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackTuneColors.Background)
            .verticalScroll(scrollState)
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.about_backTune),
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

        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Logo
            Image(
                painter = painterResource(id = R.drawable.ic_backtune_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.height(24.dp))

            // App Name and Tagline
            Text(
                text = "BackTune",
                style = MaterialTheme.typography.headlineLarge,
                color = BackTuneColors.Primary,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Enhance Your YouTube Experience",
                style = MaterialTheme.typography.titleMedium,
                color = BackTuneColors.TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Features Section
            FeatureSection(
                title = "Key Features",
                //move them to string file
                features = listOf(
                    stringResource(id = R.string.feature_ambient_sounds),
                    stringResource(id = R.string.feature_volume_control),
                    stringResource(id = R.string.feature_multiple_sounds),
                    stringResource(id = R.string.feature_easy_sharing)
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Creator Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = BackTuneColors.Surface
                ),
                shape = RoundedCornerShape(16.dp),

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.creator_photo),
                        contentDescription = "Creator",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Ayan Malik",
                        style = MaterialTheme.typography.titleLarge,
                        color = BackTuneColors.TextPrimary
                    )

                    Text(
                        text = "Creator & Developer",
                        style = MaterialTheme.typography.bodyLarge,
                        color = BackTuneColors.TextSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ContactButton(
                        icon = Icons.Default.Email,
                        text = stringResource(id = R.string.contact_me),
                        onClick = { onContactMe() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Version Info
            Text(
                text = "Version 1.0.0",
                style = MaterialTheme.typography.bodySmall,
                color = BackTuneColors.TextTertiary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Share Button
            Button(
                onClick = {
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "Check out BackTune - Enhance your YouTube experience with ambient sounds! Created by Ayan Malik\n\nConnect with the developer: https://www.linkedin.com/in/ayan-malik-1302a3199/")
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share BackTune"))
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = BackTuneColors.Primary
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(id = R.string.share_app))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Instructions
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = BackTuneColors.Surface
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "How to Use",
                        style = MaterialTheme.typography.titleLarge,
                        color = BackTuneColors.TextPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    InstructionItem(
                        icon = R.drawable.ic_share,
                        text = "Share a YouTube video to BackTune"
                    )
                    InstructionItem(
                        icon = R.drawable.ic_music,
                        text = "Select your preferred background sound"
                    )
                    InstructionItem(
                        icon = R.drawable.ic_volume_up,
                        text = "Adjust the volume to your liking"
                    )
                }
            }
        }
    }
}

@Composable
private fun InstructionItem(
    icon: Int,
    text: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            colorFilter = ColorFilter.tint(BackTuneColors.Primary),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = BackTuneColors.TextSecondary
        )
    }
}

@Composable
private fun FeatureSection(
    title: String,
    features: List<String>
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = BackTuneColors.TextPrimary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        features.forEach { feature ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = BackTuneColors.Primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = feature,
                    style = MaterialTheme.typography.bodyLarge,
                    color = BackTuneColors.TextSecondary
                )
            }
        }
    }
}

@Composable
private fun ContactButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = BackTuneColors.Primary
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}

@Preview
@Composable
private fun PreviewAboutScreen() {
    BackTuneTheme {
        AboutScreen(onNavigateBack = {}, onContactMe = {})
    }
}