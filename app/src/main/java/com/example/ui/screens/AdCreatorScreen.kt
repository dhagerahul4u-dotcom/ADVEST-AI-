package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.api.GeneratedAdResult
import com.example.ui.components.GlassCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.PurpleAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdCreatorScreen(
    productName: String,
    productDesc: String,
    selectedPlatform: String,
    selectedTone: String,
    selectedLanguage: String,
    targetAudience: String,
    isGeneratingCopy: Boolean,
    currentAdResult: GeneratedAdResult?,
    credits: Int,
    onProductNameChanged: (String) -> Unit,
    onProductDescChanged: (String) -> Unit,
    onPlatformSelected: (String) -> Unit,
    onToneSelected: (String) -> Unit,
    onLanguageSelected: (String) -> Unit,
    onTargetAudienceChanged: (String) -> Unit,
    onGenerateClicked: () -> Unit
) {
    val platforms = listOf("Instagram", "Facebook", "Google Display", "Stories", "YouTube", "LinkedIn")
    val tones = listOf("Persuasive", "Luxury", "Energetic", "Professional", "Humorous")
    val languages = listOf("English", "Hindi", "Marathi", "Spanish", "French")

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
                    title = "AI Ad Creator & Copywriter",
                    subtitle = "Generate high-converting headlines, copy, CTAs & hashtags with Gemini"
                )
            }

            // Platform Channel Tabs
            item {
                GlassCard {
                    Text(
                        text = "1. Select Ad Platform Channel",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(platforms) { platform ->
                            val isSelected = (platform == selectedPlatform)
                            FilterChip(
                                selected = isSelected,
                                onClick = { onPlatformSelected(platform) },
                                label = { Text(platform) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = IndigoPrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // Input Form
            item {
                GlassCard {
                    Text(
                        text = "2. Campaign Inputs",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = productName,
                        onValueChange = onProductNameChanged,
                        label = { Text("Product Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = productDesc,
                        onValueChange = onProductDescChanged,
                        label = { Text("Product Description & Core Features") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 4
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Tone of Voice",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(tones) { tone ->
                            val isSelected = (tone == selectedTone)
                            FilterChip(
                                selected = isSelected,
                                onClick = { onToneSelected(tone) },
                                label = { Text(tone) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PurpleAccent,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Target Language",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(languages) { lang ->
                            val isSelected = (lang == selectedLanguage)
                            FilterChip(
                                selected = isSelected,
                                onClick = { onLanguageSelected(lang) },
                                label = { Text(lang) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CyanAccent,
                                    selectedLabelColor = Color.Black
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = targetAudience,
                        onValueChange = onTargetAudienceChanged,
                        label = { Text("Target Audience / Persona") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }
            }

            // Generate Button
            item {
                Button(
                    onClick = onGenerateClicked,
                    enabled = !isGeneratingCopy && credits >= 10,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                ) {
                    if (isGeneratingCopy) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Gemini is crafting high-converting ad copy...")
                    } else {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Generate Ad Copy (-10 CR)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            // Result Display Card
            if (currentAdResult != null) {
                item {
                    GlassCard(
                        borderColor = EmeraldSuccess.copy(alpha = 0.6f)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Generated $selectedPlatform Ad",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldSuccess
                            )
                            IconButton(
                                onClick = {
                                    val fullAd = "${currentAdResult.headline}\n\n${currentAdResult.bodyCopy}\n\n${currentAdResult.callToAction}\n\n${currentAdResult.hashtags}"
                                    clipboardManager.setText(AnnotatedString(fullAd))
                                    snackbarMessage = "Copied complete ad copy to clipboard!"
                                }
                            ) {
                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy All", tint = EmeraldSuccess)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Headline Section
                        CopyBlockItem(
                            label = "HEADLINE",
                            content = currentAdResult.headline,
                            onCopy = {
                                clipboardManager.setText(AnnotatedString(currentAdResult.headline))
                                snackbarMessage = "Headline copied!"
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Body Copy Section
                        CopyBlockItem(
                            label = "BODY COPY",
                            content = currentAdResult.bodyCopy,
                            onCopy = {
                                clipboardManager.setText(AnnotatedString(currentAdResult.bodyCopy))
                                snackbarMessage = "Body copy copied!"
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // CTA Section
                        CopyBlockItem(
                            label = "CALL TO ACTION (CTA)",
                            content = currentAdResult.callToAction,
                            onCopy = {
                                clipboardManager.setText(AnnotatedString(currentAdResult.callToAction))
                                snackbarMessage = "CTA text copied!"
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Hashtags Section
                        CopyBlockItem(
                            label = "OPTIMIZED HASHTAGS",
                            content = currentAdResult.hashtags,
                            onCopy = {
                                clipboardManager.setText(AnnotatedString(currentAdResult.hashtags))
                                snackbarMessage = "Hashtags copied!"
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Visual Concept Section
                        CopyBlockItem(
                            label = "AI VISUAL CONCEPT SUGGESTION",
                            content = currentAdResult.visualPromptSuggestion,
                            onCopy = {
                                clipboardManager.setText(AnnotatedString(currentAdResult.visualPromptSuggestion))
                                snackbarMessage = "Visual prompt copied!"
                            }
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(30.dp)) }
        }
    }
}

@Composable
fun CopyBlockItem(
    label: String,
    content: String,
    onCopy: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = IndigoPrimary
                )
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { onCopy() }
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
