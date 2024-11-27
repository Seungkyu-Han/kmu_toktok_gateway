package org.seungkyu.controller

import org.seungkyu.dto.ChatBotReq
import org.seungkyu.handler.ChatBotHandler
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class ChatBotController(
    private val chatBotHandler: ChatBotHandler
) {
    @PostMapping("/api/see")
    suspend fun chatRequest(@RequestBody chatBotReq: ChatBotReq): Flux<ServerSentEvent<String>>{
        return chatBotHandler.postSse(chatBotReq)
    }

}