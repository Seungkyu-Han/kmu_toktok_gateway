package org.seungkyu.handler

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.withContext
import org.seungkyu.dto.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

@Service
class ChatBotHandler(
    @Value("\${OPEN_API_KEY}")
    private val openApiKey: String,
    private val objectMapper: ObjectMapper
) {

    private val openAIUrl = "https://api.openai.com/v1"
    private val errorMessage = "챗봇 요청 중 에러가 발생했습니다."

    private val createAndRunWebClient =
        WebClient.builder()
            .baseUrl("$openAIUrl/threads/runs")
            .defaultHeaders {
                it.set(HttpHeaders.AUTHORIZATION, "Bearer $openApiKey")
                it.set(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                it.set("OpenAI-Beta", "assistants=v2")
            }
            .build()


    suspend fun postSse(serverRequest: ServerRequest): ServerResponse {
        return withContext(Dispatchers.IO) {
            val chatBotReq = serverRequest.bodyToMono(ChatBotReq::class.java).awaitSingle()

            val responseStream = createAndRunWebClient
                .post()
                .bodyValue(
                    ChatGPTReq(
                        assistantId = chatBotReq.assistantId,
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
                .filter{
                    it != "[DONE]"
                }
                .doOnNext { println(it) }
                .map{
                    objectMapper.readValue(it, ChatGPTRes::class.java)
                }
                .filter{
                    it.objectType == "thread.message.delta"
                }
                .mapNotNull {
                    it.delta?.content?.get(0)?.text?.value
                }
                .map { content ->
                    ServerSentEvent.builder<String>()
                        .event("chat-update")
                        .data(content)
                        .build()
                }
                .onErrorStop()

            ServerResponse
                .ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(BodyInserters.fromServerSentEvents(responseStream))
                .awaitSingle()
        }
    }
}