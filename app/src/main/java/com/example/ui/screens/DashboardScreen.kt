package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ProjectEntity
import com.example.ui.components.GlassCard
import com.example.ui.components.MetricChip
import com.example.ui.components.ResDrawableImage
import com.example.ui.components.SectionHeader
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.PinkAccent
import com.example.ui.theme.PurpleAccent

@Composable
fun DashboardScreen(
    projects: List<ProjectEntity>,
    onNavigateToStudio: (String) -> Unit,
    onDeleteProject: (Long) -> Unit
) {
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Hero Banner
            item {
                GlassCard(
                    borderColor = IndigoPrimary.copy(alpha = 0.5f)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        ResDrawableImage(
                            resName = "img_hero_banner_1785330043995",
                            contentDescription = "AdVest AI Hero",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            Color.Black.copy(alpha = 0.3f),
                                            Color.Black.copy(alpha = 0.85f)
                                        )
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = IndigoPrimary
                            ) {
                                Text(
                                    text = "AI Ad Suite v3.0",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Create High-Converting Ads in Seconds",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Photos, Copy, Videos, Voiceovers & Subtitles — Powered by Gemini",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { onNavigateToStudio("AD_CREATOR") },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = IndigoPrimary
                                ),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Create New Ad Campaign", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Analytics / Metrics Bar
            item {
                SectionHeader(
                    title = "Campaign Analytics Overview",
                    subtitle = "Real-time AI performance metrics"
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        MetricChip(
                            icon = Icons.Default.TrendingUp,
                            label = "Avg CTR",
                            value = "4.8%",
                            color = EmeraldSuccess,
                            modifier = Modifier.width(130.dp)
                        )
                    }
                    item {
                        MetricChip(
                            icon = Icons.Default.ShoppingCart,
                            label = "Conversions",
                            value = "1,420",
                            color = CyanAccent,
                            modifier = Modifier.width(130.dp)
                        )
                    }
                    item {
                        MetricChip(
                            icon = Icons.Default.Visibility,
                            label = "Total Reach",
                            value = "98.4K",
                            color = PurpleAccent,
                            modifier = Modifier.width(130.dp)
                        )
                    }
                    item {
                        MetricChip(
                            icon = Icons.Default.MonetizationOn,
                            label = "Est. ROAS",
                            value = "4.2x",
                            color = PinkAccent,
                            modifier = Modifier.width(130.dp)
                        )
                    }
                }
            }

            // Quick Studio Tools Shortcuts
            item {
                SectionHeader(
                    title = "AI Studio Tools",
                    subtitle = "Select a tool to start generating content"
                )
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StudioToolCard(
                            title = "AI Product Photo",
                            subtitle = "Bg Removal & Preset Backgrounds",
                            icon = Icons.Default.PhotoCamera,
                            gradient = listOf(IndigoPrimary, PurpleAccent),
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigateToStudio("PHOTO_STUDIO") }
                        )
                        StudioToolCard(
                            title = "AI Ad Copywriter",
                            subtitle = "High Converting Headlines & Copy",
                            icon = Icons.Default.EditNote,
                            gradient = listOf(PurpleAccent, PinkAccent),
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigateToStudio("AD_CREATOR") }
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StudioToolCard(
                            title = "Video & Voice Studio",
                            subtitle = "Viral Scripts, Voiceover & Subtitles",
                            icon = Icons.Default.VideoCameraBack,
                            gradient = listOf(CyanAccent, IndigoPrimary),
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigateToStudio("VIDEO_STUDIO") }
                        )
                        StudioToolCard(
                            title = "Brand & Templates",
                            subtitle = "9+ Categories & Brand Kit",
                            icon = Icons.Default.Palette,
                            gradient = listOf(PinkAccent, EmeraldSuccess),
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigateToStudio("BRAND_KIT") }
                        )
                    }
                }
            }

            // Recent Projects
            item {
                SectionHeader(
                    title = "Recent Projects",
                    subtitle = "${projects.size} saved campaigns"
                )
            }

            if (projects.isEmpty()) {
                item {
                    GlassCard {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.FolderOpen,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "No saved projects yet",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Use any tool above to generate and save ad assets",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            } else {
                items(projects, key = { it.id }) { project ->
                    ProjectItemCard(
                        project = project,
                        onCopyCopy = {
                            clipboardManager.setText(AnnotatedString("${project.generatedHeadline}\n\n${project.generatedBody}\n\n${project.generatedHashtags}"))
                            snackbarMessage = "Copied project text to clipboard!"
                        },
                        onDelete = { onDeleteProject(project.id) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            item { Spacer(modifier = Modifier.height(30.dp)) }
        }
    }
}

@Composable
fun StudioToolCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    gradient: List<Color>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.height(110.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            gradient[0].copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    )
                )
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(gradient)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun ProjectItemCard(
    project: ProjectEntity,
    onCopyCopy: () -> Unit,
    onDelete: () -> Unit
) {
    GlassCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                ResDrawableImage(
                    resName = project.previewImage,
                    contentDescription = project.title,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = IndigoPrimary.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = project.platformChannel,
                            style = MaterialTheme.typography.labelSmall,
                            color = IndigoPrimary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = project.type.replace("_", " "),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = project.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = project.generatedHeadline,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row {
                IconButton(onClick = onCopyCopy) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy text",
                        tint = IndigoPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
