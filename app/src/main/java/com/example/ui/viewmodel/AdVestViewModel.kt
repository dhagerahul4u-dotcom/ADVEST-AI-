package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeneratedAdResult
import com.example.data.api.GeminiRepository
import com.example.data.local.AdVestDatabase
import com.example.data.local.BrandKitEntity
import com.example.data.local.ProjectEntity
import com.example.data.local.TemplateEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AdVestViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AdVestDatabase.getDatabase(application)
    private val projectDao = db.projectDao()
    private val brandKitDao = db.brandKitDao()
    private val templateDao = db.templateDao()
    private val geminiRepo = GeminiRepository()

    // Credits & Subscription
    private val _creditsBalance = MutableStateFlow(500)
    val creditsBalance: StateFlow<Int> = _creditsBalance.asStateFlow()

    private val _subscriptionPlan = MutableStateFlow("Pro Plan")
    val subscriptionPlan: StateFlow<String> = _subscriptionPlan.asStateFlow()

    // Room Flows
    val projects = projectDao.getAllProjects()
    val brandKits = brandKitDao.getAllBrandKits()
    val templates = templateDao.getAllTemplates()

    // Photo Studio State
    val selectedPhotoRes = MutableStateFlow("img_product_luxury_1785330055988")
    val selectedStyle = MutableStateFlow("Luxury")
    val customBgPrompt = MutableStateFlow("")
    val isRemovingBg = MutableStateFlow(false)
    val photoGeneratedResult = MutableStateFlow("img_product_luxury_1785330055988")

    // Ad Creator State
    val productName = MutableStateFlow("Aura Pro Smart Watch")
    val productDesc = MutableStateFlow("Ultra luxury titanium smartwatch with sapphire glass, 14-day battery life, and AI health monitoring.")
    val selectedPlatform = MutableStateFlow("Instagram")
    val selectedTone = MutableStateFlow("Luxury")
    val selectedLanguage = MutableStateFlow("English")
    val targetAudience = MutableStateFlow("Tech enthusiasts, executives, fitness professionals")
    val isGeneratingCopy = MutableStateFlow(false)
    val currentAdResult = MutableStateFlow<GeneratedAdResult?>(null)

    // Video & Voice Studio State
    val videoAspectRatio = MutableStateFlow("9:16")
    val motionEffect = MutableStateFlow("Zoom & Pan")
    val voiceGender = MutableStateFlow("Female - Aria")
    val voiceScriptText = MutableStateFlow("Stop scrolling! Meet the Aura Pro Smart Watch. Engineered for those who demand ultimate luxury and power.")
    val subtitleStyle = MutableStateFlow("Karaoke Glow")
    val isPlayingAudioPreview = MutableStateFlow(false)
    val isGeneratingVideo = MutableStateFlow(false)
    val generatedScriptOutput = MutableStateFlow("")

    // Tools Suite State
    val qrCodeText = MutableStateFlow("https://advestai.com/deal")
    val hashtagKeywords = MutableStateFlow("Luxury Watch, Smart Tech, Lifestyle")
    val generatedHashtagsOutput = MutableStateFlow("")

    init {
        seedInitialTemplatesAndData()
    }

    private fun seedInitialTemplatesAndData() {
        viewModelScope.launch {
            val existingProjects = projects.first()
            if (existingProjects.isEmpty()) {
                projectDao.insertProject(
                    ProjectEntity(
                        title = "Aura Watch Launch Campaign",
                        type = "AD_CREATOR",
                        platformChannel = "Instagram",
                        previewImage = "img_product_luxury_1785330055988",
                        generatedHeadline = "Elegance Meets Intelligence",
                        generatedBody = "Crafted with titanium and sapphire glass. Experience 14-day battery life and precision fitness telemetry.",
                        generatedCta = "Claim 20% Off Launch Deal",
                        generatedHashtags = "#AuraWatch #AdVestAI #LuxuryTech #FitnessGoals",
                        status = "Completed"
                    )
                )
                projectDao.insertProject(
                    ProjectEntity(
                        title = "Studio Headphones Lifestyle Ad",
                        type = "PHOTO_STUDIO",
                        platformChannel = "Facebook",
                        previewImage = "img_product_lifestyle_1785330069416",
                        generatedHeadline = "Sound Pure As Silence",
                        generatedBody = "Active noise cancellation engineered for high focus environments.",
                        generatedCta = "Shop Sound Collection",
                        generatedHashtags = "#Headphones #StudioSound #Audiophile",
                        status = "Completed"
                    )
                )
            }

            val existingBrands = brandKits.first()
            if (existingBrands.isEmpty()) {
                brandKitDao.insertBrandKit(
                    BrandKitEntity(
                        brandName = "Aura Studio Tech",
                        tagline = "Innovating Future Elegance",
                        primaryColorHex = "#6366F1",
                        secondaryColorHex = "#8B5CF6",
                        fontStyle = "Plus Jakarta Sans Bold",
                        toneOfVoice = "Luxury & High Converting",
                        isDefault = true
                    )
                )
            }

            val existingTemplates = templates.first()
            if (existingTemplates.isEmpty()) {
                val seedTemplates = listOf(
                    TemplateEntity(category = "Fashion", title = "Luxury Apparel Spotlight", description = "High-end cinematic backdrop for haute couture & jewelry", badgeText = "POPULAR", samplePrompt = "Silk aesthetic luxury lighting", previewImageRes = "img_product_luxury_1785330055988"),
                    TemplateEntity(category = "Electronics", title = "Minimalist Studio Desk", description = "Clean oak & daylight aesthetic for modern gadgets", badgeText = "BESTSELLER", samplePrompt = "Daylight workspace setting", previewImageRes = "img_product_lifestyle_1785330069416"),
                    TemplateEntity(category = "Food", title = "Artisanal Gourmet Table", description = "Warm marble & fresh herbs display for dining & beverages", badgeText = "NEW", samplePrompt = "Rustic gourmet restaurant backdrop", previewImageRes = "img_product_luxury_1785330055988"),
                    TemplateEntity(category = "Real Estate", title = "Modern Luxury Villa", description = "Architectural glass & sunlit poolside framing for properties", badgeText = "HOT", samplePrompt = "Contemporary penthouse suite view", previewImageRes = "img_hero_banner_1785330043995"),
                    TemplateEntity(category = "Jewellery", title = "Diamond Velvet Showcase", description = "Soft velvet reflections with ambient warm bokeh glow", badgeText = "PREMIUM", samplePrompt = "Dark velvet jewel lighting", previewImageRes = "img_product_luxury_1785330055988"),
                    TemplateEntity(category = "Fitness", title = "High Octane Gym Studio", description = "Neon electric highlights for activewear & supplements", badgeText = "TRENDING", samplePrompt = "Industrial high energy gym lighting", previewImageRes = "img_product_lifestyle_1785330069416"),
                    TemplateEntity(category = "Cars", title = "Neon Highway Showcase", description = "Futuristic asphalt reflections for automotive ads", badgeText = "PRO", samplePrompt = "Cyberpunk highway night reflections", previewImageRes = "img_hero_banner_1785330043995"),
                    TemplateEntity(category = "Education", title = "Interactive Masterclass", description = "Professional studio framing for courses & ebooks", badgeText = "ESSENTIAL", samplePrompt = "Modern educational studio backdrop", previewImageRes = "img_product_lifestyle_1785330069416"),
                    TemplateEntity(category = "Furniture", title = "Scandinavian Interior Living", description = "Warm pastel indoor lighting for home furnishings", badgeText = "POPULAR", samplePrompt = "Sunlit Scandinavian living room", previewImageRes = "img_product_lifestyle_1785330069416")
                )
                templateDao.insertTemplates(seedTemplates)
            }
        }
    }

    fun deductCredits(amount: Int): Boolean {
        if (_creditsBalance.value >= amount) {
            _creditsBalance.value -= amount
            return true
        }
        return false
    }

    fun addCredits(amount: Int) {
        _creditsBalance.value += amount
    }

    fun setPlan(plan: String) {
        _subscriptionPlan.value = plan
    }

    fun generatePhotoBackground() {
        if (!deductCredits(20)) return
        viewModelScope.launch {
            isRemovingBg.value = true
            delay(1500)
            isRemovingBg.value = false
            // Switch photo preview to illustrate background replacement
            photoGeneratedResult.value = if (selectedPhotoRes.value == "img_product_luxury_1785330055988") {
                "img_product_lifestyle_1785330069416"
            } else {
                "img_product_luxury_1785330055988"
            }

            projectDao.insertProject(
                ProjectEntity(
                    title = "AI Photo - ${selectedStyle.value} Style",
                    type = "PHOTO_STUDIO",
                    platformChannel = "E-Commerce",
                    previewImage = photoGeneratedResult.value,
                    generatedHeadline = "${selectedStyle.value} Studio Photo",
                    generatedBody = "Background replaced with AI ${selectedStyle.value} preset setting.",
                    generatedCta = "Download HD",
                    generatedHashtags = "#AIPhotography #ProductShots #AdVestAI",
                    status = "Completed"
                )
            )
        }
    }

    fun generateAdCopy() {
        if (!deductCredits(10)) return
        viewModelScope.launch {
            isGeneratingCopy.value = true
            val result = geminiRepo.generateAdCopy(
                productName = productName.value,
                productDescription = productDesc.value,
                platformChannel = selectedPlatform.value,
                tone = selectedTone.value,
                language = selectedLanguage.value,
                targetAudience = targetAudience.value
            )
            currentAdResult.value = result
            isGeneratingCopy.value = false

            projectDao.insertProject(
                ProjectEntity(
                    title = "${productName.value} - ${selectedPlatform.value}",
                    type = "AD_CREATOR",
                    platformChannel = selectedPlatform.value,
                    previewImage = selectedPhotoRes.value,
                    generatedHeadline = result.headline,
                    generatedBody = result.bodyCopy,
                    generatedCta = result.callToAction,
                    generatedHashtags = result.hashtags,
                    status = "Completed"
                )
            )
        }
    }

    fun generateVideoAndScript() {
        if (!deductCredits(50)) return
        viewModelScope.launch {
            isGeneratingVideo.value = true
            val script = geminiRepo.generateVideoScript(
                productName = productName.value,
                durationSeconds = 15,
                topic = "${selectedPlatform.value} Video Ad"
            )
            generatedScriptOutput.value = script
            isGeneratingVideo.value = false

            projectDao.insertProject(
                ProjectEntity(
                    title = "${productName.value} Video Ad (${videoAspectRatio.value})",
                    type = "VIDEO_STUDIO",
                    platformChannel = selectedPlatform.value,
                    previewImage = selectedPhotoRes.value,
                    generatedHeadline = "15s Viral Video Script",
                    generatedBody = script,
                    generatedCta = "Export MP4 Video",
                    generatedHashtags = "#AIVideo #Reels #Shorts #AdVestAI",
                    status = "Completed"
                )
            )
        }
    }

    fun playVoiceoverPreview() {
        if (_creditsBalance.value < 5) return
        viewModelScope.launch {
            isPlayingAudioPreview.value = true
            delay(2500)
            isPlayingAudioPreview.value = false
            deductCredits(5)
        }
    }

    fun createBrandKit(name: String, tagline: String, primaryHex: String, secondaryHex: String) {
        viewModelScope.launch {
            brandKitDao.insertBrandKit(
                BrandKitEntity(
                    brandName = name,
                    tagline = tagline,
                    primaryColorHex = primaryHex,
                    secondaryColorHex = secondaryHex,
                    isDefault = true
                )
            )
        }
    }

    fun generateHashtags() {
        if (!deductCredits(5)) return
        viewModelScope.launch {
            delay(800)
            val keywordsList = hashtagKeywords.value.split(",").map { it.trim() }
            val tags = keywordsList.joinToString(" ") { "#${it.replace(" ", "")}" } + " #AdVestAI #ViralMarketing #ProductLaunch #ECommercePro #TrendingNow"
            generatedHashtagsOutput.value = tags
        }
    }

    fun deleteProject(id: Long) {
        viewModelScope.launch {
            projectDao.deleteProjectById(id)
        }
    }
}
