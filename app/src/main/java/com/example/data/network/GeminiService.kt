package com.example.data.network

import com.example.BuildConfig
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    val contents: List<Content>,
    val generationConfig: GenerationConfig? = null
)

@JsonClass(generateAdapter = true)
data class Content(
    val parts: List<Part>
)

@JsonClass(generateAdapter = true)
data class Part(
    val text: String
)

@JsonClass(generateAdapter = true)
data class GenerationConfig(
    val temperature: Float? = null,
    val maxOutputTokens: Int? = null
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    val candidates: List<Candidate>?
)

@JsonClass(generateAdapter = true)
data class Candidate(
    val content: Content?
)

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val service: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApiService::class.java)
    }

    suspend fun getGeminiResponse(prompt: String, systemInstruction: String = ""): String {
        try {
            val key = BuildConfig.GEMINI_API_KEY
            if (key.isEmpty() || key == "MY_GEMINI_API_KEY") {
                return "Error: Please configure your GEMINI_API_KEY in the Secrets Panel of Google AI Studio."
            }

            val finalPrompt = if (systemInstruction.isNotEmpty()) {
                "$systemInstruction\n\nUser request: $prompt"
            } else {
                prompt
            }

            val request = GeminiRequest(
                contents = listOf(
                    Content(parts = listOf(Part(text = finalPrompt)))
                ),
                generationConfig = GenerationConfig(temperature = 0.7f)
            )

            val response = service.generateContent(key, request)
            return response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "No response content received from AI engine."
        } catch (e: Exception) {
            e.printStackTrace()
            return "Error during AI generation: ${e.message}"
        }
    }
}
