package org.seungkyu.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema


data class ChatBotReq(
    @Schema(description = "시스템 컨텐츠", example = "System message example")
    val systemContent: String,
    @Schema(description = "유저 컨텐츠", example = "User message example")
    val userContent: String
)

data class ChatGPTReq(
    @JsonProperty("assistant_id")
    val assistantId: String,
    val thread: Thread,
    val stream: Boolean
)

data class Message(
    val role: String,
    val content: String
)

data class Thread(
    val messages: List<Message>
)
