package com.neuralshell.android.ai

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ClaudeApi {
    @POST("v1/messages")
    suspend fun sendMessage(@Body request: ClaudeRequest): ClaudeResponse
}

object ClaudeService {
    private const val BASE_URL = "https://api.anthropic.com/"
    private const val API_KEY = "YOUR_API_KEY_HERE" // TODO: Move to secure storage
    
    private val client: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        val authInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("x-api-key", API_KEY)
                .addHeader("anthropic-version", "2023-06-01")
                .addHeader("content-type", "application/json")
                .build()
            chain.proceed(request)
        }
        
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }
    
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    val api: ClaudeApi by lazy {
        retrofit.create(ClaudeApi::class.java)
    }
    
    suspend fun parseCommand(naturalLanguageInput: String): String {
        val systemPrompt = """
            You are a shell command assistant. Convert natural language to shell commands.
            Return ONLY the shell command, no explanation.
            Examples:
            - "list files" -> "ls -la"
            - "show running processes" -> "ps aux"
            - "what's my ip" -> "ip addr show"
        """.trimIndent()
        
        val request = ClaudeRequest(
            messages = listOf(
                Message("user", "$systemPrompt\n\nConvert to command: $naturalLanguageInput")
            )
        )
        
        return try {
            val response = api.sendMessage(request)
            response.content.firstOrNull()?.text?.trim() ?: naturalLanguageInput
        } catch (e: Exception) {
            e.printStackTrace()
            naturalLanguageInput
        }
    }
}
