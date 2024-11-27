package org.seungkyu.handler

import com.fasterxml.jackson.databind.ObjectMapper
import org.seungkyu.dto.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@Service
class ChatBotHandler(
    @Value("\${OPEN_API_KEY}")
    private val openApiKey: String,
    @Value("\${ASSISTANT_ID}")
    private val assistantId: String,
    private val objectMapper: ObjectMapper
) {

    private val openAIUrl = "https://api.openai.com/v1"

    private val createAndRunWebClient =
        WebClient.builder()
            .baseUrl("$openAIUrl/threads/runs")
            .defaultHeaders {
                it.set(HttpHeaders.AUTHORIZATION, "Bearer $openApiKey")
                it.set(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                it.set("OpenAI-Beta", "assistants=v2")
            }
            .build()


    suspend fun postSse(chatBotReq: ChatBotReq): Flux<ServerSentEvent<String>> {
        return createAndRunWebClient
            .post()
            .bodyValue(
                ChatGPTReq(
                    assistantId = assistantId,
                    thread = Thread(
                        messages = listOf(
                            Message(
                                role = "user",
                                content = "Constraints: ${chatBotReq.systemContent} Content: ${chatBotReq.userContent}"
                            )
                        )
                    ),
                    stream = true
                )
            )
            .retrieve()
            .bodyToFlux(String::class.java)
            .filter { it != "[DONE]" }
            .map { objectMapper.readValue(it, ChatGPTRes::class.java) }
            .filter { it.objectType == "thread.message.delta" }
            .mapNotNull { it.delta?.content?.get(0)?.text?.value }
            .filter { content -> !content.isNullOrBlank() }
            .map { content ->
                ServerSentEvent.builder<String>()
                    .event("chat-update")
                    .data(content)
                    .build()
            }
            .onErrorContinue { ex, _ ->
                println("Error during SSE: ${ex.message}")
            }
    }
}