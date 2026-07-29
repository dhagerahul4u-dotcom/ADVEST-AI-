package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.PinkAccent
import com.example.ui.theme.PurpleAccent

@Composable
fun VideoVoiceScreen(
    videoAspectRatio: String,
    motionEffect: String,
    voiceGender: String,
    voiceScriptText: String,
    subtitleStyle: String,
    isPlayingAudioPreview: Boolean,
    isGeneratingVideo: Boolean,
    generatedScriptOutput: String,
    credits: Int,
    onAspectRatioSelected: (String) -> Unit,
    onMotionEffectSelected: (String) -> Unit,
    onVoiceGenderSelected: (String) -> Unit,
    onVoiceScriptTextChanged: (String) -> Unit,
    onSubtitleStyleSelected: (String) -> Unit,
    onPlayVoiceoverClicked: () -> Unit,
    onGenerateVideoClicked: () -> Unit
) {
    val aspectRatios = listOf("9:16", "16:9", "1:1")
    val motionEffects = listOf("Zoom & Pan", "Auto B-Roll", "3D Tilt", "Cinematic Fade")
    val voices = listOf(
        "Female - Aria",
        "Male - Marcus",
        "Female - Ananya (Hindi)",
        "Male - Rahul (Marathi)",
        "Female - Elena (Spanish)"
    )
    val subtitleStyles = listOf("Karaoke Glow", "Animated Pop", "Minimalist Bold", "Emoji Highlight")

    var selectedTab by remember { mutableStateOf(0) } // 0: Video Generator, 1: Voice & Subtitles

    val clipboardManager = LocalClipboardManager.current
    var snackbarHostState = remember { SnackbarHostState() }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            item {
                SectionHeader(
                    title = "AI Video & Voiceover Studio",
                    subtitle = "Create viral video ads with motion, AI voices, and animated subtitles"
                )
            }

            // Tab Selector
            item {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = IndigoPrimary
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("AI Video Generator", fontWeight = FontWeight.Bold) },
                        icon = { Icon(imageVector = Icons.Default.VideoCameraBack, contentDescription = null) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("AI Voice & Subtitles", fontWeight = FontWeight.Bold) },
                        icon = { Icon(imageVector = Icons.Default.RecordVoiceOver, contentDescription = null) }
                    )
                }
            }

            if (selectedTab == 0) {
                // AI Video Generator Controls
                item {
                    GlassCard {
                        Text(
                            text = "Aspect Ratio",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            aspectRatios.forEach { ratio ->
                                val isSelected = (ratio == videoAspectRatio)
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { onAspectRatioSelected(ratio) },
                                    label = { Text(ratio) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = IndigoPrimary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Motion Effects & Transitions",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(motionEffects) { effect ->
                                val isSelected = (effect == motionEffect)
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { onMotionEffectSelected(effect) },
                                    label = { Text(effect) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = PurpleAccent,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }

                item {
                    Button(
                        onClick = onGenerateVideoClicked,
                        enabled = !isGeneratingVideo && credits >= 50,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                    ) {
                        if (isGeneratingVideo) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Rendering Video Ad & Auto Script...")
                        } else {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Generate AI Video & Script (-50 CR)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                if (generatedScriptOutput.isNotBlank()) {
                    item {
                        GlassCard(borderColor = CyanAccent.copy(alpha = 0.6f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Generated Video Script ($videoAspectRatio)",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = CyanAccent
                                )
                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(generatedScriptOutput))
                                        snackbarMessage = "Copied script to clipboard!"
                                    }
                                ) {
                                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = CyanAccent)
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = generatedScriptOutput,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = { snackbarMessage = "Exported MP4 Video with Subtitles to downloads!" },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess)
                            ) {
                                Icon(imageVector = Icons.Default.Download, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Export Rendered MP4 Video")
                            }
                        }
                    }
                }
            } else {
                // AI Voice & Subtitles Tab
                item {
                    GlassCard {
                        Text(
                            text = "Select Voice Actor & Language",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(voices) { v ->
                                val isSelected = (v == voiceGender)
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { onVoiceGenderSelected(v) },
                                    label = { Text(v) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = IndigoPrimary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = voiceScriptText,
                            onValueChange = onVoiceScriptTextChanged,
                            label = { Text("Voice Script Text") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp),
                            shape = RoundedCornerShape(12.dp),
                            maxLines = 4
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Animated Subtitle Style",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(subtitleStyles) { style ->
                                val isSelected = (style == subtitleStyle)
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { onSubtitleStyleSelected(style) },
                                    label = { Text(style) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = PinkAccent,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }

                item {
                    // Audio Waveform & Preview Card
                    GlassCard(borderColor = PurpleAccent.copy(alpha = 0.5f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = onPlayVoiceoverClicked,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(PurpleAccent)
                            ) {
                                Icon(
                                    imageVector = if (isPlayingAudioPreview) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Play Preview",
                                    tint = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isPlayingAudioPreview) "Synthesizing $voiceGender..." else "Voiceover Preview ($voiceGender)",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Subtitle Glow: $subtitleStyle",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }

                        if (isPlayingAudioPreview) {
                            Spacer(modifier = Modifier.height(12.dp))
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth(),
                                color = CyanAccent,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { snackbarMessage = "Voiceover MP3 downloaded!" },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                            ) {
                                Icon(imageVector = Icons.Default.Download, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Export Voice MP3")
                            }
                            OutlinedButton(
                                onClick = { snackbarMessage = "Subtitles SRT file downloaded!" },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Subtitles, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Export SRT")
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(30.dp)) }
        }
    }
}
