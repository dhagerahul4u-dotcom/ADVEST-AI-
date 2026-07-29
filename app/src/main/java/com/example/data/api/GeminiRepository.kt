package com.example.data.api

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiRepository {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun generateAdCopy(
        productName: String,
        productDescription: String,
        platformChannel: String,
        tone: String,
        language: String,
        targetAudience: String
    ): GeneratedAdResult = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext generateFallbackAdCopy(productName, platformChannel, tone, language)
        }

        val prompt = """
            You are an expert SaaS copywriter for AdVest AI creating ads for $platformChannel.
            Product Name: $productName
            Description: $productDescription
            Tone: $tone
            Language: $language
            Audience: $targetAudience

            Return your response in EXACTLY this format separated by line tags:
            [HEADLINE] High impact headline
            [BODY] Persuasive ad body text (2-3 sentences)
            [CTA] Strong Call to Action button text
            [HASHTAGS] #Hashtag1 #Hashtag2 #Hashtag3 #Hashtag4
            [VISUAL] Visual suggestion for image/video backdrop
        """.trimIndent()

        try {
            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", prompt)
                            })
                        })
                    })
                })
            }

            val request = Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                val responseStr = response.body?.string() ?: ""
                val responseJson = JSONObject(responseStr)
                val candidates = responseJson.optJSONArray("candidates")
                val text = candidates?.optJSONObject(0)
                    ?.optJSONObject("content")
                    ?.optJSONArray("parts")
                    ?.optJSONObject(0)
                    ?.optString("text") ?: ""

                if (text.isNotBlank()) {
                    return@withContext parseAdCopyResponse(text, productName)
                }
            }
        } catch (_: Exception) {}

        return@withContext generateFallbackAdCopy(productName, platformChannel, tone, language)
    }

    suspend fun generateVideoScript(
        productName: String,
        durationSeconds: Int,
        topic: String
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext """
                [00:00 - 00:03] HOOK: "Tired of dull ads? Meet $productName!"
                [00:03 - 00:08] PROBLEM: Traditional product shots take days and cost thousands.
                [00:08 - 00:15] SOLUTION: AdVest AI generates studio grade photos and viral video ads in 10 seconds.
                [00:15 - 00:20] CALL TO ACTION: "Tap link in bio to claim 500 free AI credits today!"
            """.trimIndent()
        }

        val prompt = "Create a $durationSeconds-second high-converting video ad script for $productName on $topic with timestamp markers and visual cues."

        try {
            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", prompt)
                            })
                        })
                    })
                })
            }

            val request = Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                val responseStr = response.body?.string() ?: ""
                val responseJson = JSONObject(responseStr)
                val candidates = responseJson.optJSONArray("candidates")
                val text = candidates?.optJSONObject(0)
                    ?.optJSONObject("content")
                    ?.optJSONArray("parts")
                    ?.optJSONObject(0)
                    ?.optString("text") ?: ""

                if (text.isNotBlank()) {
                    return@withContext text
                }
            }
        } catch (_: Exception) {}

        return@withContext """
            [00:00 - 00:03] HOOK: "Transform your product marketing instantly with $productName!"
            [00:03 - 00:10] HIGHLIGHT: High conversion ads, auto voiceovers, and HD backgrounds.
            [00:10 - 00:15] CTA: "Get started for free on AdVest AI!"
        """.trimIndent()
    }

    private fun parseAdCopyResponse(raw: String, fallbackTitle: String): GeneratedAdResult {
        var headline = "Elevate Your Brand with $fallbackTitle"
        var body = "Discover unparalleled luxury and performance. Crafted for creators who demand perfection."
        var cta = "Shop Now & Get 20% Off"
        var hashtags = "#$fallbackTitle #AdVestAI #Marketing #Trending #ProductLaunch"
        var visual = "High contrast studio backdrop with dramatic ambient neon lighting"

        raw.lines().forEach { line ->
            when {
                line.startsWith("[HEADLINE]") -> headline = line.removePrefix("[HEADLINE]").trim()
                line.startsWith("[BODY]") -> body = line.removePrefix("[BODY]").trim()
                line.startsWith("[CTA]") -> cta = line.removePrefix("[CTA]").trim()
                line.startsWith("[HASHTAGS]") -> hashtags = line.removePrefix("[HASHTAGS]").trim()
                line.startsWith("[VISUAL]") -> visual = line.removePrefix("[VISUAL]").trim()
            }
        }

        if (headline.length < 3) headline = "Unleash The Power of $fallbackTitle"
        return GeneratedAdResult(headline, body, cta, hashtags, visual)
    }

    private fun generateFallbackAdCopy(
        productName: String,
        platform: String,
        tone: String,
        language: String
    ): GeneratedAdResult {
        return GeneratedAdResult(
            headline = "Experience Supreme Quality with $productName",
            bodyCopy = "Designed for modern achievers. Transform your daily routine with high performance design, premium durability, and instant elegance.",
            callToAction = "Claim Exclusive Offer Now",
            hashtags = "#${productName.replace(" ", "")} #AdVestAI #$platform #ViralAd #Trending2026",
            visualPromptSuggestion = "Ultra high resolution $tone product photography with cinematic studio lighting and glossy reflections"
        )
    }
}
