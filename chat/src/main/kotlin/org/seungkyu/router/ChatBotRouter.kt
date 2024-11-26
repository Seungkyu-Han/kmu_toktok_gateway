package org.seungkyu.router

import org.seungkyu.handler.ChatBotHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class ChatBotRouter {

    @Bean
    fun chatBotRouterMapping(
        chatBotHandler: ChatBotHandler
    ) = coRouter {
        POST("/api/sse", chatBotHandler::postSse)
    }
}