package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
fun SubscriptionScreen(
    currentPlan: String,
    creditsBalance: Int,
    onSelectPlan: (String) -> Unit,
    onTopUpCredits: (Int) -> Unit
) {
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
                    title = "Subscription & Credit Refill",
                    subtitle = "Manage your AdVest AI SaaS tier and credit balance"
                )
            }

            // Current Plan & Credits summary
            item {
                GlassCard(borderColor = IndigoPrimary.copy(alpha = 0.6f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Current Active Tier",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Text(
                                text = currentPlan,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = IndigoPrimary
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = CyanAccent.copy(alpha = 0.2f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$creditsBalance Credits Left",
                                    fontWeight = FontWeight.Bold,
                                    color = CyanAccent
                                )
                            }
                        }
                    }
                }
            }

            // Subscription Tiers
            item {
                Text(
                    text = "Choose Subscription Plan",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            item {
                PlanTierCard(
                    title = "Free Tier",
                    price = "$0",
                    period = "/month",
                    credits = "50 AI Credits / Mo",
                    features = listOf("Standard Photo BG Removal", "5 Ad Copy Generations", "Standard Export"),
                    isSelected = currentPlan == "Free Plan",
                    onSelect = {
                        onSelectPlan("Free Plan")
                        snackbarMessage = "Switched to Free Tier"
                    }
                )
            }

            item {
                PlanTierCard(
                    title = "Pro Studio Plan",
                    price = "$29",
                    period = "/month",
                    credits = "1,000 AI Credits / Mo",
                    features = listOf("HD AI Photo Studio", "Unlimited Gemini Ad Copy", "15s Video Ads & AI Voices", "All 9 Template Categories"),
                    isPopular = true,
                    isSelected = currentPlan == "Pro Plan",
                    onSelect = {
                        onSelectPlan("Pro Plan")
                        snackbarMessage = "Upgraded to Pro Studio Plan!"
                    }
                )
            }

            item {
                PlanTierCard(
                    title = "Business Agency Plan",
                    price = "$79",
                    period = "/month",
                    credits = "3,500 AI Credits / Mo",
                    features = listOf("All Pro Features", "Priority Video Rendering", "Multi-brand Kit Vault", "Team Collaboration & API Access"),
                    isSelected = currentPlan == "Business Plan",
                    onSelect = {
                        onSelectPlan("Business Plan")
                        snackbarMessage = "Upgraded to Business Agency Plan!"
                    }
                )
            }

            item {
                PlanTierCard(
                    title = "Enterprise Plan",
                    price = "$199",
                    period = "/month",
                    credits = "Unlimited Credits",
                    features = listOf("Custom AI Fine-Tuned Models", "Dedicated Account Manager", "White-Label Exports", "Instant 24/7 SLA Support"),
                    isSelected = currentPlan == "Enterprise Plan",
                    onSelect = {
                        onSelectPlan("Enterprise Plan")
                        snackbarMessage = "Activated Enterprise Plan!"
                    }
                )
            }

            // Instant Credit Refill
            item {
                SectionHeader(
                    title = "Instant Credit Top-Up (Stripe)",
                    subtitle = "Refill credits anytime without changing plan"
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CreditPackCard(
                        amount = "+500 CR",
                        price = "$9",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onTopUpCredits(500)
                            snackbarMessage = "Added 500 Credits via Stripe!"
                        }
                    )
                    CreditPackCard(
                        amount = "+2,000 CR",
                        price = "$29",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onTopUpCredits(2000)
                            snackbarMessage = "Added 2,000 Credits via Stripe!"
                        }
                    )
                    CreditPackCard(
                        amount = "+5,000 CR",
                        price = "$59",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onTopUpCredits(5000)
                            snackbarMessage = "Added 5,000 Credits via Stripe!"
                        }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(30.dp)) }
        }
    }
}

@Composable
fun PlanTierCard(
    title: String,
    price: String,
    period: String,
    credits: String,
    features: List<String>,
    isPopular: Boolean = false,
    isSelected: Boolean = false,
    onSelect: () -> Unit
) {
    GlassCard(
        borderColor = if (isSelected) IndigoPrimary else if (isPopular) PurpleAccent else MaterialTheme.colorScheme.outlineVariant
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = credits,
                        style = MaterialTheme.typography.labelSmall,
                        color = IndigoPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = price,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = period,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            features.forEach { feat ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = EmeraldSuccess,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = feat,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onSelect,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) EmeraldSuccess else IndigoPrimary
                )
            ) {
                Text(if (isSelected) "Active Plan" else "Select $title")
            }
        }
    }
}

@Composable
fun CreditPackCard(
    amount: String,
    price: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.border(1.dp, CyanAccent.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = CyanAccent)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = amount, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(text = price, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = CyanAccent)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Top-Up", style = MaterialTheme.typography.labelSmall, color = IndigoPrimary)
        }
    }
}
