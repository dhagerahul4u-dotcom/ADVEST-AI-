package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val type: String, // PHOTO_STUDIO, AD_CREATOR, VIDEO_STUDIO, VOICE, TOOL
    val platformChannel: String, // Instagram, Facebook, Google, YouTube, LinkedIn, TikTok
    val previewImage: String, // Res name or path
    val generatedHeadline: String,
    val generatedBody: String,
    val generatedCta: String,
    val generatedHashtags: String,
    val status: String = "Completed",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "brand_kits")
data class BrandKitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val brandName: String,
    val tagline: String,
    val primaryColorHex: String,
    val secondaryColorHex: String,
    val logoUri: String = "",
    val fontStyle: String = "Inter Bold",
    val toneOfVoice: String = "Professional & High Converting",
    val isDefault: Boolean = false
)

@Entity(tableName = "templates")
data class TemplateEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: String, // Fashion, Food, Cars, Electronics, Real Estate, Fitness, Education, Jewellery, Furniture
    val title: String,
    val description: String,
    val badgeText: String,
    val samplePrompt: String,
    val previewImageRes: String
)
