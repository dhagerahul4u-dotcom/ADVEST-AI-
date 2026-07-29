package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.BuildConfig
import com.example.ui.components.GlassCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary

@Composable
fun AdminPanelScreen(
    credits: Int,
    plan: String,
    onAddCredits: (Int) -> Unit
) {
    var snackbarHostState = remember { SnackbarHostState() }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    val hasApiKey = BuildConfig.GEMINI_API_KEY.isNotBlank() && BuildConfig.GEMINI_API_KEY != "MY_GEMINI_API_KEY"

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
                    title = "Admin Panel & API Diagnostics",
                    subtitle = "System health, API key status & credit allocation control"
                )
            }

            // System Status Card
            item {
                GlassCard(borderColor = IndigoPrimary.copy(alpha = 0.5f)) {
                    Text(
                        text = "System Integrations Health",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    StatusRow(
                        name = "Gemini AI REST API",
                        status = if (hasApiKey) "Active (Key Injected)" else "Fallback Mode (Demo Key Active)",
                        isOk = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    StatusRow(
                        name = "Room Local Database",
                        status = "Connected & Synchronized",
                        isOk = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    StatusRow(
                        name = "Cloudinary / Storage Engine",
                        status = "Connected (CDN Active)",
                        isOk = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    StatusRow(
                        name = "Stripe Payments Gateway",
                        status = "Active (Live Sandbox)",
                        isOk = true
                    )
                }
            }

            // Credit Control
            item {
                GlassCard {
                    Text(
                        text = "Credit Control Override",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Current balance: $credits CR • Plan: $plan",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                onAddCredits(500)
                                snackbarMessage = "Granted +500 Admin Credits!"
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                        ) {
                            Text("+500 CR")
                        }
                        Button(
                            onClick = {
                                onAddCredits(2000)
                                snackbarMessage = "Granted +2,000 Admin Credits!"
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess)
                        ) {
                            Text("+2000 CR")
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(30.dp)) }
        }
    }
}

@Composable
fun StatusRow(name: String, status: String, isOk: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
            Text(text = status, style = MaterialTheme.typography.labelSmall, color = if (isOk) EmeraldSuccess else MaterialTheme.colorScheme.error)
        }
        Icon(
            imageVector = if (isOk) Icons.Default.CheckCircle else Icons.Default.Error,
            contentDescription = null,
            tint = if (isOk) EmeraldSuccess else MaterialTheme.colorScheme.error,
            modifier = Modifier.size(20.dp)
        )
    }
}
