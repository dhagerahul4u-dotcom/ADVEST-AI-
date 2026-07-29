package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.components.AdVestTopBar
import com.example.ui.screens.*
import com.example.ui.theme.AdVestTheme
import com.example.ui.theme.IndigoPrimary
import com.example.ui.viewmodel.AdVestViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AdVestTheme(darkTheme = true) {
                AdVestApp()
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    object PhotoStudio : Screen("photo_studio", "Photo AI", Icons.Default.PhotoCamera)
    object AdCreator : Screen("ad_creator", "Ad Copy", Icons.Default.EditNote)
    object VideoVoice : Screen("video_voice", "Video & Voice", Icons.Default.VideoCameraBack)
    object BrandKit : Screen("brand_kit", "Templates", Icons.Default.Palette)
    object ToolsSuite : Screen("tools_suite", "AI Tools", Icons.Default.Build)
    object Subscription : Screen("subscription", "Billing", Icons.Default.Star)
    object AdminPanel : Screen("admin_panel", "Admin", Icons.Default.AdminPanelSettings)
}

@Composable
fun AdVestApp(viewModel: AdVestViewModel = viewModel()) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Dashboard.route

    val creditsBalance by viewModel.creditsBalance.collectAsStateWithLifecycle()
    val subscriptionPlan by viewModel.subscriptionPlan.collectAsStateWithLifecycle()
    val projects by viewModel.projects.collectAsStateWithLifecycle(initialValue = emptyList())
    val brandKits by viewModel.brandKits.collectAsStateWithLifecycle(initialValue = emptyList())
    val templates by viewModel.templates.collectAsStateWithLifecycle(initialValue = emptyList())

    // Photo Studio State
    val selectedPhotoRes by viewModel.selectedPhotoRes.collectAsStateWithLifecycle()
    val selectedStyle by viewModel.selectedStyle.collectAsStateWithLifecycle()
    val customBgPrompt by viewModel.customBgPrompt.collectAsStateWithLifecycle()
    val isRemovingBg by viewModel.isRemovingBg.collectAsStateWithLifecycle()
    val photoGeneratedResult by viewModel.photoGeneratedResult.collectAsStateWithLifecycle()

    // Ad Creator State
    val productName by viewModel.productName.collectAsStateWithLifecycle()
    val productDesc by viewModel.productDesc.collectAsStateWithLifecycle()
    val selectedPlatform by viewModel.selectedPlatform.collectAsStateWithLifecycle()
    val selectedTone by viewModel.selectedTone.collectAsStateWithLifecycle()
    val selectedLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val targetAudience by viewModel.targetAudience.collectAsStateWithLifecycle()
    val isGeneratingCopy by viewModel.isGeneratingCopy.collectAsStateWithLifecycle()
    val currentAdResult by viewModel.currentAdResult.collectAsStateWithLifecycle()

    // Video & Voice State
    val videoAspectRatio by viewModel.videoAspectRatio.collectAsStateWithLifecycle()
    val motionEffect by viewModel.motionEffect.collectAsStateWithLifecycle()
    val voiceGender by viewModel.voiceGender.collectAsStateWithLifecycle()
    val voiceScriptText by viewModel.voiceScriptText.collectAsStateWithLifecycle()
    val subtitleStyle by viewModel.subtitleStyle.collectAsStateWithLifecycle()
    val isPlayingAudioPreview by viewModel.isPlayingAudioPreview.collectAsStateWithLifecycle()
    val isGeneratingVideo by viewModel.isGeneratingVideo.collectAsStateWithLifecycle()
    val generatedScriptOutput by viewModel.generatedScriptOutput.collectAsStateWithLifecycle()

    // Tools State
    val qrCodeText by viewModel.qrCodeText.collectAsStateWithLifecycle()
    val hashtagKeywords by viewModel.hashtagKeywords.collectAsStateWithLifecycle()
    val generatedHashtagsOutput by viewModel.generatedHashtagsOutput.collectAsStateWithLifecycle()

    val bottomNavItems = listOf(
        Screen.Dashboard,
        Screen.PhotoStudio,
        Screen.AdCreator,
        Screen.VideoVoice,
        Screen.BrandKit,
        Screen.ToolsSuite
    )

    Scaffold(
        topBar = {
            AdVestTopBar(
                credits = creditsBalance,
                plan = subscriptionPlan,
                onTopUpClick = { navController.navigate(Screen.Subscription.route) },
                onPlanClick = { navController.navigate(Screen.Subscription.route) },
                onSettingsClick = { navController.navigate(Screen.AdminPanel.route) }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = IndigoPrimary,
                tonalElevation = 8.dp
            ) {
                bottomNavItems.forEach { screen ->
                    val isSelected = currentRoute == screen.route
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title
                            )
                        },
                        label = {
                            Text(
                                text = screen.title,
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = IndigoPrimary,
                            selectedTextColor = IndigoPrimary,
                            indicatorColor = IndigoPrimary.copy(alpha = 0.15f)
                        )
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    projects = projects,
                    onNavigateToStudio = { route ->
                        when (route) {
                            "PHOTO_STUDIO" -> navController.navigate(Screen.PhotoStudio.route)
                            "AD_CREATOR" -> navController.navigate(Screen.AdCreator.route)
                            "VIDEO_STUDIO" -> navController.navigate(Screen.VideoVoice.route)
                            "BRAND_KIT" -> navController.navigate(Screen.BrandKit.route)
                        }
                    },
                    onDeleteProject = { id -> viewModel.deleteProject(id) }
                )
            }

            composable(Screen.PhotoStudio.route) {
                PhotoStudioScreen(
                    selectedPhotoRes = selectedPhotoRes,
                    selectedStyle = selectedStyle,
                    customBgPrompt = customBgPrompt,
                    isRemovingBg = isRemovingBg,
                    photoGeneratedResult = photoGeneratedResult,
                    credits = creditsBalance,
                    onPhotoSelected = { viewModel.selectedPhotoRes.value = it },
                    onStyleSelected = { viewModel.selectedStyle.value = it },
                    onPromptChanged = { viewModel.customBgPrompt.value = it },
                    onGenerateClicked = { viewModel.generatePhotoBackground() }
                )
            }

            composable(Screen.AdCreator.route) {
                AdCreatorScreen(
                    productName = productName,
                    productDesc = productDesc,
                    selectedPlatform = selectedPlatform,
                    selectedTone = selectedTone,
                    selectedLanguage = selectedLanguage,
                    targetAudience = targetAudience,
                    isGeneratingCopy = isGeneratingCopy,
                    currentAdResult = currentAdResult,
                    credits = creditsBalance,
                    onProductNameChanged = { viewModel.productName.value = it },
                    onProductDescChanged = { viewModel.productDesc.value = it },
                    onPlatformSelected = { viewModel.selectedPlatform.value = it },
                    onToneSelected = { viewModel.selectedTone.value = it },
                    onLanguageSelected = { viewModel.selectedLanguage.value = it },
                    onTargetAudienceChanged = { viewModel.targetAudience.value = it },
                    onGenerateClicked = { viewModel.generateAdCopy() }
                )
            }

            composable(Screen.VideoVoice.route) {
                VideoVoiceScreen(
                    videoAspectRatio = videoAspectRatio,
                    motionEffect = motionEffect,
                    voiceGender = voiceGender,
                    voiceScriptText = voiceScriptText,
                    subtitleStyle = subtitleStyle,
                    isPlayingAudioPreview = isPlayingAudioPreview,
                    isGeneratingVideo = isGeneratingVideo,
                    generatedScriptOutput = generatedScriptOutput,
                    credits = creditsBalance,
                    onAspectRatioSelected = { viewModel.videoAspectRatio.value = it },
                    onMotionEffectSelected = { viewModel.motionEffect.value = it },
                    onVoiceGenderSelected = { viewModel.voiceGender.value = it },
                    onVoiceScriptTextChanged = { viewModel.voiceScriptText.value = it },
                    onSubtitleStyleSelected = { viewModel.subtitleStyle.value = it },
                    onPlayVoiceoverClicked = { viewModel.playVoiceoverPreview() },
                    onGenerateVideoClicked = { viewModel.generateVideoAndScript() }
                )
            }

            composable(Screen.BrandKit.route) {
                BrandKitScreen(
                    brandKits = brandKits,
                    templates = templates,
                    onCreateBrandKit = { name, tagline, pCol, sCol ->
                        viewModel.createBrandKit(name, tagline, pCol, sCol)
                    },
                    onUseTemplate = { tmpl ->
                        viewModel.productName.value = tmpl.title
                        viewModel.productDesc.value = tmpl.description
                        navController.navigate(Screen.AdCreator.route)
                    }
                )
            }

            composable(Screen.ToolsSuite.route) {
                ToolsSuiteScreen(
                    qrCodeText = qrCodeText,
                    hashtagKeywords = hashtagKeywords,
                    generatedHashtagsOutput = generatedHashtagsOutput,
                    credits = creditsBalance,
                    onQrTextChange = { viewModel.qrCodeText.value = it },
                    onHashtagKeywordsChange = { viewModel.hashtagKeywords.value = it },
                    onGenerateHashtags = { viewModel.generateHashtags() }
                )
            }

            composable(Screen.Subscription.route) {
                SubscriptionScreen(
                    currentPlan = subscriptionPlan,
                    creditsBalance = creditsBalance,
                    onSelectPlan = { viewModel.setPlan(it) },
                    onTopUpCredits = { viewModel.addCredits(it) }
                )
            }

            composable(Screen.AdminPanel.route) {
                AdminPanelScreen(
                    credits = creditsBalance,
                    plan = subscriptionPlan,
                    onAddCredits = { viewModel.addCredits(it) }
                )
            }
        }
    }
}
