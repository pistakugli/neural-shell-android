package com.neuralshell.android.ai

import com.google.gson.annotations.SerializedName

data class ClaudeRequest(
    val model: String = "claude-sonnet-4-20250514",
    val max_tokens: Int = 1024,
    val messages: List<Message>
)

data class Message(
    val role: String,
    val content: String
)

data class ClaudeResponse(
    val id: String,
    val type: String,
    val role: String,
    val content: List<ContentBlock>,
    val model: String,
    @SerializedName("stop_reason") val stopReason: String?
)

data class ContentBlock(
    val type: String,
    val text: String?
)
